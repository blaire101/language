#include <stdio.h>   // FILE * fopen(const char * path, const char * mode); 返回指向该流的文件指针。snprintf...
#include <stdlib.h>  // exit, NULL
#include <string.h>
#include <ctype.h>    /* int isspace(int c) */
#include <unistd.h>   /* fork()， execl(,) */
#include <stdarg.h>   /* va_list, va_start, va_arg, va_end */

#include <sys/wait.h> /* waitpid(,,) */
#include <mysql/mysql.h>

#define STD_MB 1048576  /* 1M */
#define STD_T_LIM 2
#define STD_F_LIM (STD_MB << 5)
#define STD_M_LIM (STD_MB << 7)
#define BUFFER_SIZE 512

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

static int DEBUG = 0;
static char host_name[BUFFER_SIZE];
static char user_name[BUFFER_SIZE];
static char password[BUFFER_SIZE];
static char db_name[BUFFER_SIZE];
static char oj_home[BUFFER_SIZE]; // home/judge
static int port_number;      // 3306
static int max_running;      // hp
static int use_max_time = 0; // 测试数据中最大的运行时间
static int http_judge = 0;   // hp
static char http_baseurl[BUFFER_SIZE];
static char http_username[BUFFER_SIZE];
static char http_password[BUFFER_SIZE];

MYSQL *conn;
static char lang_ext[10][8] = { "c", "cc"};

int execute_cmd(const char * fmt, ...) {
    char cmd[BUFFER_SIZE];
    int ret = 0;
    va_list ap;
    va_start(ap, fmt);
    vsprintf(cmd, fmt, ap);
    ret = system(cmd);
    va_end(ap);
    return ret;
}
void write_log(const char *fmt, ...) {
    va_list ap;
    char buffer[4096];
    sprintf(buffer, "%s/log/client.log", oj_home); /*  /oj_home/log/client.log   */
    FILE *fp = fopen(buffer, "a+");
    if (fp == NULL) {
        fprintf(stderr, "openfile error!\n");
        system("pwd");  // 打印出当前所在路径
    }
    va_start(ap, fmt);  // 将第一个可变参数的地址赋给ap，即ap指向可变参数列表的开始
    int l = vsprintf(buffer, fmt, ap); // 将参数 ap 和 format 进行转化形成格式化字符串，即可以显示的字符串。返回生成子串的长度。
    printf("L : %d\n", l);
    fprintf(fp, "%s\n", buffer);
    va_end(ap);   // 将参数ap复位
    fclose(fp);   // 关闭文件流指针
}
void clean_workdir(char * work_dir ) {
    execute_cmd("umount %s/proc", work_dir);
    execute_cmd("rm -Rf %s/*", work_dir);
}
void _update_problem_mysql(int p_id) {
    char sql[BUFFER_SIZE];
    sprintf(sql, "UPDATE `problem` SET `accepted`=(SELECT count(*) FROM `solution` WHERE `problem_id`=\'%d\' AND `result`=\'4\') WHERE `problem_id`=\'%d\'", p_id, p_id);
    if (mysql_real_query(conn, sql, strlen(sql)))
        write_log(mysql_error(conn));
    sprintf(sql, "UPDATE `problem` SET `submit`=(SELECT count(*) FROM `solution` WHERE `problem_id`=\'%d\') WHERE `problem_id`=\'%d\'", p_id, p_id);
    if (mysql_real_query(conn, sql, strlen(sql))) {
        write_log(mysql_error(conn));
    }
}
void update_problem(int pid) {
    _update_problem_mysql(pid);
}
void _update_user_mysql(char * user_id) {
    char sql[BUFFER_SIZE];
    sprintf(sql, "UPDATE `users` SET `solved`=(SELECT count(DISTINCT `problem_id`) FROM `solution` WHERE `user_id`=\'%s\' AND `result`=\'4\') WHERE `user_id`=\'%s\'", user_id, user_id);
    if (mysql_real_query(conn, sql, strlen(sql))) {
        write_log(mysql_error(conn));
    }
    sprintf(sql, "UPDATE `users` SET `submit`=(SELECT count(*) FROM `solution` WHERE `user_id`=\'%s\') WHERE `user_id`=\'%s\'", user_id, user_id);
    if (mysql_real_query(conn, sql, strlen(sql))) {
        write_log(mysql_error(conn));
    }
}
void update_user(char  *user_id) {
    _update_user_mysql(user_id);
}
void _addceinfo_mysql(int solution_id) {
    char sql[(1 << 16)], *end;
    char ceinfo[(1 << 16)], *cend;
    FILE *fp = fopen("ce.txt", "r");
    snprintf(sql, (1 << 16) - 1, "DELETE FROM compileinfo WHERE solution_id=%d", solution_id); // 将可变个参数(...)按照format格式化成字符串，然后将其复制到str中
    mysql_real_query(conn, sql, strlen(sql));
    cend = ceinfo;
    while (fgets(cend, 1024, fp)) {
        cend += strlen(cend);
        if (cend - ceinfo > 40000) break;
    }
    cend = 0;
    end = sql;
    strcpy(end, "INSERT INTO compileinfo VALUES(");
    end += strlen(sql);
    *end++ = '\'';
    end += sprintf(end, "%d", solution_id);
    *end++ = '\'';
    *end++ = ',';
    *end++ = '\'';
    end += mysql_real_escape_string(conn, end, ceinfo, strlen(ceinfo));
    *end++ = '\'';
    *end++ = ')';
    *end = 0;
    //  printf("%s\n",ceinfo);
    if (mysql_real_query(conn, sql, end - sql))
        printf("%s\n", mysql_error(conn));
    fclose(fp);
}
void addceinfo(int solution_id) {
    _addceinfo_mysql(solution_id);
}
void _update_solution_mysql(int solution_id, int result, int time, int memory, int sim, int sim_s_id, double pass_rate) {
    char sql[BUFFER_SIZE];
    sprintf(sql, "UPDATE solution SET result=%d,time=%d,memory=%d,judgetime=NOW() WHERE solution_id=%d LIMIT 1%c", result, time, memory, solution_id, 0);
    mysql_real_query(conn, sql, strlen(sql));
}
void update_solution(int solution_id, int result, int time, int memory, int sim, int sim_s_id, double pass_rate) {
    _update_solution_mysql(solution_id,  result,  time,  memory, sim, sim_s_id, pass_rate);
}
int compile(int lang) {
    const char *CP_C[] = { "gcc", "Main.c", "-o", "Main", "-O2","-Wall", "-lm", "--static", "-std=c99", "-DONLINE_JUDGE", NULL };
    const char *CP_X[] = { "g++", "Main.cc", "-o", "Main", "-O2", "-Wall", "-lm", "--static", "-DONLINE_JUDGE", NULL };
    pid_t pid = fork();
    if (pid == 0) {
        struct rlimit LIM;  
        LIM.rlim_max = 60;
        LIM.rlim_cur = 60;
        setrlimit(RLIMIT_CPU, &LIM);   // RLIMIT_CPU 最大允许的CPU使用时间，秒为单位
        
        LIM.rlim_max = 60 * STD_MB;
        LIM.rlim_cur = 60 * STD_MB; 
        setrlimit(RLIMIT_FSIZE, &LIM); // RLIMIT_FSIZE 进程可建立的文件的最大长度。

        LIM.rlim_max = 1024 * STD_MB;
        LIM.rlim_cur = 1024 * STD_MB;
        setrlimit(RLIMIT_AS, &LIM);    // RLIMIT_AS 进程的最大虚内存空间，字节为单位。
        
        freopen("ce.txt", "w", stderr);
        
        switch (lang) {
            case 0:
                execvp(CP_C[0], (char * const *)CP_C); // execvp()会从PATH 环境变量所指的目录中查找符合参数file 的文件名，找到后便执行该文件，然后将第二个参数argv传给该欲执行的文件。
                break;
            case 1:
                execvp(CP_X[0], (char * const *)CP_X); break;
            default:
                printf("nothing to do!\n");
        }
        exit(0);
    } 
    else {
        int status = 0;
        waitpid(pid, &status, 0); // 等待子进程运行结束。
        return status;            // 反映子进程的结束状态
    }
}
void _get_solution_mysql(int solution_id, char *work_dir, int lang) {
    char sql[BUFFER_SIZE], src_pth[BUFFER_SIZE];
    MYSQL_RES *res;
    MYSQL_ROW row;
    sprintf(sql, "SELECT source FROM source_code WHERE solution_id=%d", solution_id);
    mysql_real_query(conn, sql, strlen(sql));
    res = mysql_store_result(conn);
    row = mysql_fetch_row(res);
    sprintf(src_pth, "Main.%s", lang_ext[lang]);
    FILE *fp_src = fopen(src_pth, "w");  /* 创建 Main.cc 文件 */
    fprintf(fp_src, "%s", row[0]);
    mysql_free_result(res);
    fclose(fp_src);
}
void get_solution(int solution_id, char *work_dir, int lang) {
    _get_solution_mysql(solution_id,  work_dir, lang) ;
}
void _get_problem_info_mysql(int p_id, int& time_lmt, int& mem_lmt, int& isspj) {
    char sql[BUFFER_SIZE];
    MYSQL_RES *res;
    MYSQL_ROW row;
    sprintf(sql, "SELECT time_limit,memory_limit,spj FROM problem where problem_id=%d", p_id);
    mysql_real_query(conn, sql, strlen(sql));
    res = mysql_store_result(conn);
    row = mysql_fetch_row(res);
    time_lmt = atoi(row[0]);
    mem_lmt = atoi(row[1]);
    isspj = row[2][0] - '0';
    mysql_free_result(res);
}
void get_problem_info(int p_id, int& time_lmt, int& mem_lmt, int& isspj) {
    _get_problem_info_mysql(p_id, time_lmt, mem_lmt, isspj);
}
void _get_solution_info_mysql(int solution_id, int& p_id, char *user_id, int& lang) {
    MYSQL_RES *res; // result
    MYSQL_ROW row;  // row
    
    char sql[BUFFER_SIZE]; // get the problem id and user id from Table:solution
    sprintf(sql, "SELECT problem_id, user_id, language FROM solution where solution_id=%d", solution_id);
    mysql_real_query(conn, sql, strlen(sql));
    res = mysql_store_result(conn);  // 返回查询结果集, 0没有结果集
    row = mysql_fetch_row(res);  // 从结果集中取得下一行，返回下一行的 MYSQL_ROW 结构。
    p_id = atoi(row[0]);
    strcpy(user_id, row[1]);
    lang = atoi(row[2]);
    mysql_free_result(res);   // mysql_free_result() 函数释放结果内存
}
void get_solution_info(int solution_id, int &p_id, char *user_id, int &lang) {
    _get_solution_info_mysql(solution_id, p_id, user_id, lang);
}
int init_mysql_conn() {       
    conn = mysql_init(NULL);  // 初始化MYSQL结构，用于接下来的connect
    const char timeout = 30;
    mysql_options(conn, MYSQL_OPT_CONNECT_TIMEOUT, &timeout);  //可用于设置额外的连接选项，并影响连接的行为
    if(!mysql_real_connect(conn, host_name, user_name, password, db_name, port_number, 0, 0)) {
        write_log("%s", mysql_error(conn)); // string mysql_error(MYSQL *mysql); 返回最近一次MySQL函数的执行状态
        return 0;
    }
    const char *utf8sql = "set names utf8"; // 设置 mysql 编码方式，尽量与网站编码一致，避免乱码。
    if (mysql_real_query(conn, utf8sql, strlen(utf8sql))) { // 返回值不为0,则查询错误。
        write_log("%s", mysql_error(conn));
        return 0;
    }
    return 1;
}
int after_equal(char *c) { //  等号= 之后的位置。
    int i = 0;
    for(; c[i] != '\0' && c[i] != '='; i++);
    return ++i;
}
void trim(char *c) {
    char buf[BUFFER_SIZE];
    char *start, *end;
    strcpy(buf, c);
    start = buf;
    while (isspace(*start)) start++;
    end = start;
    while (!isspace(*end)) end++;
    *end = '\0';
    strcpy(c, start);
}
bool read_buf(char *buf, const char *key, char *value) {
    if(strncmp(buf, key, strlen(key)) == 0) {
        strcpy(value, buf + after_equal(buf));
        trim(value);
        if (DEBUG) printf("%s\n", value); //hp
        return 1;
    }
    return 0;
}
void read_int(char *buf, const char *key, int *value) {
    char buf2[BUFFER_SIZE];
    if (read_buf(buf, key, buf2)) {
        sscanf(buf2, "%d", value);  // 从一个字符串中读进与指定格式相符的数据.
    }
}
void init_mysql_conf() {
    host_name[0] = 0;
    user_name[0] = 0;
    password[0] = 0;
    db_name[0] = 0;
    port_number = 3306;
    max_running = 3;
    FILE *fp = NULL;
    char buf[BUFFER_SIZE];
    sprintf(buf, "%s/etc/judge.conf", oj_home);
    fp = fopen("./etc/judge.conf", "r");
    if(fp != NULL) {
        while(fgets(buf, BUFFER_SIZE - 1, fp)) { // 从文件结构体指针fp中读取数据，每次读取一行。读取的数据保存在buf指向的字符数组中。
            read_buf(buf, "OJ_HOST_NAME", host_name);
            read_buf(buf, "OJ_USER_NAME", user_name);
            read_buf(buf, "OJ_PASSWORD", password);
            read_buf(buf, "OJ_DB_NAME", db_name);
            read_int(buf, "OJ_PORT_NUMBER", &port_number);
            read_int(buf, "OJ_HTTP_JUDGE", &http_judge);
            read_buf(buf, "OJ_HTTP_BASEURL", http_baseurl);
            read_buf(buf, "OJ_HTTP_USERNAME", http_username);
            read_buf(buf, "OJ_HTTP_PASSWORD", http_password);
            read_int(buf, "OJ_USE_MAX_TIME", &use_max_time);
        }
        fclose(fp);
    }
}
void init_parameters(int argc, char ** argv, int& solution_id, int& runner_id) {
    strcpy(oj_home, argv[3]);  /* argv[1]=solution_id, argv[2]=client_id, argv[3]=oj_home */
    chdir(oj_home);         // change the dir -> init our work
    solution_id = atoi(argv[1]);
    runner_id = atoi(argv[2]); // client_id
}
int main(int argc, char** argv) {
    int solution_id = 1000;
    int runner_id = 0;
    init_parameters(argc, argv, solution_id, runner_id); // 初始化参数
    init_mysql_conf();    // Reads the configuration file
    if(!http_judge && !init_mysql_conn()) {
        exit(0);  // exit if mysql is down
    }
    // 连接成功
    // set work directory to start running & judging
    char work_dir[BUFFER_SIZE]; // argv[0]: name，argv[1]: solution_id
    sprintf(work_dir, "%s/run%s/", oj_home, argv[2]); // argv[2]: client_id.
    chdir(work_dir);                   //  entra run dir
    execute_cmd("rm %s/*", work_dir);  //  clear /home/judge/runX
    
    int p_id, time_lmt, mem_lmt, lang, isspj;
    char user_id[BUFFER_SIZE];
    
    get_solution_info(solution_id, p_id, user_id, lang); /* 根据 solution table，取得 p_id, user_id, lang */
    get_problem_info(p_id, time_lmt, mem_lmt, isspj);    /* 根据 problem table 取得 time_limit, memory_limit, isspj */
    get_solution(solution_id, work_dir, lang);           /* 创建 Main.cc 文件 source 从 Mysql source_code 表中取出 */
    
    if (time_lmt > 300 || time_lmt < 1) time_lmt = 300;  // s
    if (mem_lmt > 1024 || mem_lmt < 1) mem_lmt = 1024;   // MB
    int Compile_OK;
    Compile_OK = compile(lang);
    if (Compile_OK != 0) { // 编译未通过将错误结果写入数据库， 增加错误信息，更新user,更新problem, 关闭数据库连接，清理工作目录，结束。
        update_solution(solution_id, OJ_CE, 0, 0, 0, 0, 0.0);
        addceinfo(solution_id);
        update_user(user_id);
        update_problem(p_id);
        mysql_close(conn);
        clean_workdir(work_dir);
        exit(0);
    }
    else {
        update_solution(solution_id, OJ_RI, 0, 0, 0, 0, 0.0); // Running_Judging
    }
    return 0;
}

