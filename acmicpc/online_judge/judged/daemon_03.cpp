#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>  // isspace()

#include <unistd.h>
#include <fcntl.h>
#include <syslog.h>
#include <errno.h>
#include <sys/stat.h> // umask
#include <signal.h>
#include <sys/wait.h>

#include <mysql/mysql.h>

#define LOCKFILE "/var/run/judged.pid"
#define LOCKMODE (S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH)

#define OJ_WT0 0 // Pending // 正等待...
#define OJ_WT1 1 // Pending_Rejudging
#define OJ_CI 2  // Compiling
#define OJ_RI 3  // Running_Judging
#define OJ_AC 4  // Accepted
#define OJ_PE 5  // Presentation Error
#define OJ_WA 6  // Wrong Answer
#define OJ_TL 7  // Time Limit Exceeded
#define OJ_ML 8  // Memory Limit Exceeded
#define OJ_OL 9  // Output Limit Exceeded
#define OJ_RE 10 // Runtime Error
#define OJ_CE 11 // Compilation Error
#define OJ_CO 12 // Compile_OK

#define BUFFER_SIZE 1024
#define STD_MB 1048576
static char host_name[BUFFER_SIZE];
static char user_name[BUFFER_SIZE];
static char password [BUFFER_SIZE];
static char db_name  [BUFFER_SIZE];
static char oj_home  [BUFFER_SIZE];
static char oj_lang_set[BUFFER_SIZE];
static int port_number;  
static int max_running; // 可以运行的 judge_client 的数量上限。
static int sleep_time;  // 数据库轮询间隔
static int sleep_tmp;

static bool STOP = false;

static char query[BUFFER_SIZE];

static MYSQL *conn;  // MYSQL结构
static MYSQL_RES *result; // 结果集结构
static MYSQL_ROW row;     // 从 结果集结构 提取的记录在 MYSQL_ROW 结构 中表示出来

void call_for_exit(int s) {
    STOP = true;
    printf("Stopping judged...\n");
}
int _get_jobs_mysql(int *jobs) {  
    if (mysql_real_query(conn, query, strlen(query))) { // 成功返回 0， 读取未判的那些 solution_id 们。
        sleep(20); // 不存在 pending 中的 solution_id , 进程挂起 20 秒。
        return 0;
    }
    result = mysql_store_result(conn);
    int i = 0, count = 0;
    while ((row = mysql_fetch_row(result)) != NULL) {
        jobs[i++] = atoi(row[0]);
    }
    count = i;
    while(i <= max_running*2) jobs[i++] = 0;
    return count;
}
int get_jobs(int *jobs) {
    return _get_jobs_mysql(jobs);
}
bool _check_out_mysql(int solution_id, int result) { // workcnt < max_running && check_out(runid, OJ_CI)
    char sql[BUFFER_SIZE];
    sprintf(sql, "UPDATE solution SET result=%d,time=0,memory=0,judgetime=NOW() WHERE solution_id=%d and result<2 LIMIT 1"
            ,result, solution_id);
    if (mysql_real_query(conn, sql, strlen(sql))) {
        syslog(LOG_ERR | LOG_DAEMON, "%s", mysql_error(conn));
        return false;
    } else {
        if(mysql_affected_rows(conn) > 0ul)
            return true;
        else
            return false;
    }
}
bool check_out(int solution_id, int result) {
    return _check_out_mysql(solution_id, result);
}
void run_client(int runid, int clientid) { // 每个进程都有一组资源限制，其中某一些可以用getrlimit和setrlimit函数查询和更改。
    char buf[BUFFER_SIZE], runidstr[BUFFER_SIZE];
    struct rlimit LIM;
    LIM.rlim_max = 800;
    LIM.rlim_cur = 800;;         /* 软限制：当前限制 -- 内核所能支持的资源上限*/
    setrlimit(RLIMIT_CPU, &LIM); /* CPU 时间的最大量值(秒) 超过限制，向该进程发送 SIGXCPU 信号*/
  
    LIM.rlim_max = 80*STD_MB;
    LIM.rlim_cur = 80*STD_MB;
    setrlimit(RLIMIT_FSIZE, &LIM); /* 可以创建文件的最大长度, SIGXFSZ */
  
    LIM.rlim_max = STD_MB<<11;
    LIM.rlim_cur = STD_MB<<11;
    setrlimit(RLIMIT_AS, &LIM);    // 进程可用存储区的最大总长度(字节)
          
    LIM.rlim_cur = LIM.rlim_max = 200;
    setrlimit(RLIMIT_NPROC, &LIM); // RLIMIT_NPROC 进程的子进程数量限制

    sprintf(runidstr, "%d", runid);
    sprintf(buf, "%d", clientid);
    execl("/usr/bin/judge_client", "/usr/bin/judge_client", runidstr, buf, oj_home, (char *)NULL);
}
int executesql(const char * sql) {
    if (mysql_real_query(conn, sql, strlen(sql))) { // 成功，函数返回零
        sleep(20);
        conn = NULL;
        return 1;
    } else
        return 0;
}
int work() {
    static pid_t ID[100];
    int jobs[max_running*2+1];
    static int retcnt = 0;
    static int workcnt = 0;
    int runid = 0, i;
    pid_t tmp_pid = 0;
        /* get the database info */
    if(!get_jobs(jobs)) retcnt = 0; //  检索出的 solution_id 们，全都存在 jobs[] 中。retcnt 为 pending的solution_id的数量。
        /* exec the submit */
    for (int j = 0; jobs[j] > 0; j++) {
        runid = jobs[j];
        if (workcnt >= max_running) {    // if no more client can running
            tmp_pid = waitpid(-1, NULL, 0);     // wait 4 one child exit, pid=-1时，等待任何一个子进程退出，没有任何限制，此时waitpid和wait的作用一模一样
            workcnt--; retcnt++;                // retcnt 代表判出题的个数
            for (i = 0; i < max_running; i++)     // get the client id
                if (ID[i] == tmp_pid) break;      // got the client id
            ID[i] = 0;
        } else {                                  // have free client
            for (i = 0; i < max_running; i++)     // find the client id
                if (ID[i] == 0) break;    // got the client id
        }
        if(workcnt < max_running && check_out(runid, OJ_CI)) { // 要判题了，将result置为 OJ_CI
            workcnt++;                                      
            ID[i] = fork();   // start to fork
            if (ID[i] == 0) {
                run_client(runid, i);    // if the process is the son, run it，父亲继续运行。
                exit(0);
            }
        } else ID[i] = 0;
    }
    //下面回收运行完的进程号
    while ( (tmp_pid = waitpid(-1, NULL, WNOHANG) ) > 0) { // pid=-1 等待任何子进程,相当于 wait()。
                   // WNOHANG 若 pid 指定的子进程没有结束，则 waitpid() 函数返回 0，不予以等待。若结束，则返回该子进程的 ID
        workcnt--; retcnt++;
        for (i = 0; i < max_running; i++)     // get the client id
            if (ID[i] == tmp_pid) break;      // got the client id
        ID[i] = 0;
        printf("tmp_pid = %d\n", tmp_pid);
    }
    mysql_free_result(result);     // free the memory
    executesql("commit");
    return retcnt;
}
int init_mysql() {
    if(conn == NULL) {
        conn = mysql_init(NULL);   // 初始化MYSQL结构，用于接下来的connect
        /* connect the database */
        const char timeout = 30;
        mysql_options(conn, MYSQL_OPT_CONNECT_TIMEOUT, &timeout); // 可用于设置额外的连接选项，并影响连接的行为

        if(!mysql_real_connect(conn, host_name, user_name, password, db_name, port_number, 0, 0)) {
            sleep(20);
            return 1;
        }
    }
    if (executesql("set names utf8"))
        return 1; // 执行失败
    return 0;
}
void trim(char *c) {
    char buf[BUFFER_SIZE];
    char *start, *end;
    strcpy(buf, c);
    start = buf;
    while(isspace(*start)) start++;
    end = start;
    while(!isspace(*end) && (*end != '\0')) end++;
    *end = '\0';
    strcpy(c, start);
}
int after_equal(char *c) {
    int i = 0;
    for(; c[i] != '\0' && c[i] != '='; i++);
    return ++i;
}
bool read_buf(char *buf, const char *key, char *value) {
   if (strncmp(buf, key, strlen(key)) == 0) {
        strcpy(value, buf + after_equal(buf));
        trim(value);
        return 1;
   }
   return 0;
}
void read_int(char *buf, const char *key, int *value) {
    char buf2[BUFFER_SIZE];
    if(read_buf(buf, key, buf2))
        sscanf(buf2, "%d", value);
}
// read the configue file
void init_mysql_conf() {
    FILE *fp = NULL;
    char buf[BUFFER_SIZE];
    host_name[0] = 0;
    user_name[0] = 0;
    password[0] = 0;
    db_name[0] = 0;
    port_number = 3306;
    max_running = 3; // 最多允许 3 个 judge_client 同时存在。
    sleep_time = 3;
    strcpy(oj_lang_set, "0,1");
    fp = fopen("./etc/judge.conf", "r");
    if(fp != NULL) {
        while(fgets(buf, BUFFER_SIZE - 1, fp)) {
            read_buf(buf, "OJ_HOST_NAME", host_name);
            read_buf(buf, "OJ_USER_NAME", user_name);
            read_buf(buf, "OJ_PASSWORD", password);
            read_buf(buf, "OJ_DB_NAME", db_name);
            read_int(buf , "OJ_PORT_NUMBER", &port_number);
            read_int(buf, "OJ_RUNNING", &max_running);
            read_int(buf, "OJ_SLEEP_TIME", &sleep_time);
            read_buf(buf,"OJ_LANG_SET", oj_lang_set);
        }
        sprintf(query, "SELECT solution_id FROM solution WHERE language in (%s) and result<2 ORDER BY result ASC,solution_id ASC limit %d", oj_lang_set, max_running*2);  
        sleep_tmp = sleep_time;
    }
}
int lockfile(int fd) {  
    struct flock fl;  
    fl.l_type = F_WRLCK;    // 独占性写锁定  
    fl.l_start = 0;     
    fl.l_whence = SEEK_SET; // 文件指针指向文件开头
    fl.l_len = 0;            
    return (fcntl(fd, F_SETLK, &fl)); // F_SETLK 设置文件锁定的状态，如果无法锁定返回 -1.  
    /* F_SETLK
     * 按照指向结构体 flock 的指针的第三个参数 arg 所描述的锁的信息设置或者清除一个文件segment锁.
     * F_SETLK被用来实现共享(或读)锁 (F_RDLCK)或独占(写)锁(F_WRLCK),同样可以去掉这两种锁(F_UNLCK).
     * 如果共享锁或独占锁不能被设置,fcntl()将立即返 回EAGAIN.
     * */
}
int already_running() {
    int fd;
    char buf[16];
    fd = open(LOCKFILE, O_RDWR|O_CREAT, LOCKMODE);
    if(fd < 0) {
        syslog(LOG_ERR|LOG_DAEMON, "can't open %s: %s", LOCKFILE, strerror(errno));
        exit(1);
    }
    if(lockfile(fd) < 0) { // -1 无法锁定
        if (errno == EACCES || errno == EAGAIN) { // 权限被拒绝 、 EAGAIN 不处理(内存不够用等问题)
            close(fd);
            return 1; // daemon is already running.
        }
        syslog(LOG_ERR|LOG_DAEMON, "can't lock %s: %s", LOCKFILE, strerror(errno));
        exit(1);
    }
    ftruncate(fd, 0); // 改变文件大小
    sprintf(buf, "%d", getpid());
    write(fd, buf, strlen(buf)+1);
    return 0;
}
int daemon_init(void) {
    pid_t pid;
    if((pid = fork()) < 0) return (-1);
    else if(pid != 0) exit(0);  /* parent exit */
    /* child continue */
    setsid(); 
    chdir(oj_home);
    umask(0); /* clear file mode creation mask */
    close(0); /* close stdin  */
    close(1); /* close stdout */
    close(2); /* close stderr */
    return (0);
}
int main(int argc, char** argv) {
    strcpy(oj_home, "/home/judge");
    chdir(oj_home);
    
    daemon_init();
    if( strcmp(oj_home, "/home/judge") == 0 && already_running() ) {
        syslog(LOG_ERR|LOG_DAEMON, "This daemon program is already running!\n");
        return 1;
    }
    init_mysql_conf();  // set the database info
    // signal() 为设置某一信号的对应动作
    signal(SIGQUIT, call_for_exit); // 输入Quit Key的时候（CTRL+\）发送给所有Foreground Group的进程
    signal(SIGKILL, call_for_exit); // 无法处理和忽略。中止某个进程
    signal(SIGTERM, call_for_exit); // 这种信号时有系统提供给普通程序使用的，按照规定，他被用来请求中止进程，kill命令缺省发送
    int j = 1;
    while (!STOP) {          // start to run
        while (j && (!init_mysql())) {
            j = work();
        }
        sleep(sleep_time); // judged 通过轮询数据库发现新任务，轮询间隔的休息时间，单位秒， 对应流程图。
        j = 1;
    }
    return 0;
}
