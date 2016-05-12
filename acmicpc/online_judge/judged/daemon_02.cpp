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
  
void call_for_exit(int s) {
    STOP = true;
    printf("Stopping judged...\n");
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
    fl.l_type = F_WRLCK; // 独占性写锁定  
    fl.l_start = 0;     
    fl.l_whence = SEEK_SET;  
    fl.l_len = 0;            
    return (fcntl(fd, F_SETLK, &fl)); // F_SETLK 设置文件锁定的状态， 如果无法锁定返回 -1.  
}
int already_running() {
    int fd;
    char buf[16];
    fd = open(LOCKFILE, O_RDWR|O_CREAT, LOCKMODE);
    if(fd < 0) {
        syslog(LOG_ERR|LOG_DAEMON, "can't open %s: %s", LOCKFILE, strerror(errno));
        exit(1);
    }
    if(lockfile(fd) < 0) {
        if (errno == EACCES || errno == EAGAIN) { 
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
    signal(SIGQUIT, call_for_exit); // 输入Quit Key的时候（CTRL+\）发送给所有Foreground Group的进程  
    signal(SIGKILL, call_for_exit); // 无法处理和忽略。中止某个进程  
    signal(SIGTERM, call_for_exit); // 请求中止进程，kill命令缺省发送  
    return 0;
}
