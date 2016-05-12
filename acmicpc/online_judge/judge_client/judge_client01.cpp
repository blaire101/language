#include <stdio.h>   // FILE * fopen(const char * path, const char * mode); 返回指向该流的文件指针。snprintf...
#include <stdlib.h>  // exit, NULL
#include <string.h>
#include <ctype.h>
#include <unistd.h>  // fork()， execl()
#include <stdarg.h>  /* va_list, va_start, va_arg, va_end */

#include <mysql/mysql.h>

#define STD_MB 1048576
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
    va_start(ap, fmt); // 将第一个可变参数的地址赋给ap，即ap指向可变参数列表的开始
    int l = vsprintf(buffer, fmt, ap); // 将参数 ap 和 format 进行转化形成格式化字符串，即可以显示的字符串。返回生成子串的长度。
    printf("L : %d\n", l);
    fprintf(fp, "%s\n", buffer);
    va_end(ap);   // 将参数ap复位
    fclose(fp);   // 关闭文件流指针
}
int init_mysql_conn() {       
    conn = mysql_init(NULL);  // 初始化MYSQL结构，用于接下来的connect
    const char timeout = 30;
    mysql_options(conn, MYSQL_OPT_CONNECT_TIMEOUT, &timeout);  //可用于设置额外的连接选项，并影响连接的行为
	if(!mysql_real_connect(conn, host_name, user_name, password, db_name, port_number, 0, 0)) {
        write_log("%s", mysql_error(conn)); // string mysql_error(MYSQL *mysql); 返回最近一次MySQL函数的执行状态
        return 0;
    }
    const char * utf8sql = "set names utf8"; // 设置 mysql 编码方式，尽量与网站编码一致，避免乱码。
    if (mysql_real_query(conn, utf8sql, strlen(utf8sql))) { // 返回值不为0,则查询错误。
        write_log("%s", mysql_error(conn));
        return 0;
    }
    return 1;
}
int after_equal(char * c) { //  等号= 之后的位置。
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
void read_int(char * buf, const char * key, int * value) {
    char buf2[BUFFER_SIZE];
    if (read_buf(buf, key, buf2))
        sscanf(buf2, "%d", value);  // 从一个字符串中读进与指定格式相符的数据.
}
void init_mysql_conf() { // 读取配置文件
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
            read_int(buf, "OJ_HTTP_JUDGE", &http_judge);     //
            read_buf(buf, "OJ_HTTP_BASEURL", http_baseurl);  //
            read_buf(buf, "OJ_HTTP_USERNAME", http_username);
            read_buf(buf, "OJ_HTTP_PASSWORD", http_password);
            read_int(buf, "OJ_USE_MAX_TIME", &use_max_time); //
        }
        //fclose(fp);
    }
}
void init_parameters(int argc, char ** argv, int& solution_id, int& runner_id) {
    if (argc < 3) {
        fprintf(stderr, "Debug:%s solution_id runner_id judge_base_path debug.\n", argv[0]); // 发送信息(参数)到由FILE *stream(流)指定的文件 格式化输出到一个流/文件中
        exit(1);
    }
    DEBUG = (argc > 4);
	if (argc > 3)
        strcpy(oj_home, argv[3]);  // argv[0] argv[1] argv[2] argv[3], argv[1]是solution_id,argv[2]是client_id,argv[3]=oj_home
	else
        strcpy(oj_home, "/home/judge"); // 参数个数为3,没有指定oj_home,则自设定！
        
    chdir(oj_home); // change the dir// init our work
    solution_id = atoi(argv[1]);
    runner_id = atoi(argv[2]); // client_id
}
int main(int argc, char** argv) {
    int solution_id = 1000;
    int runner_id = 0;
    init_parameters(argc, argv, solution_id, runner_id); // 初始化参数
    init_mysql_conf();                                   // 读取配置文件
    if(!http_judge && !init_mysql_conn()) {
        exit(0);  // exit if mysql is down
    }
    // 连接成功
    // set work directory to start running & judging
    char work_dir[BUFFER_SIZE];
    sprintf(work_dir, "%s/run%s/", oj_home, argv[2]); // name， oj_home， solution_id, runner_id.
    chdir(work_dir);
    execute_cmd("rm %s/*", work_dir); // 清空 /oj_home/runX , 清空运行目录。
	return 0;
}
