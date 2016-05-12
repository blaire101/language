#include <stdio.h>   // FILE * fopen(const char * path, const char * mode); 返回指向该流的文件指针。snprintf...
#include <stdlib.h>  // exit, NULL
#include <string.h>
#include <ctype.h>    /* int isspace(int c) */
#include <unistd.h>   /* fork()， execl(,), execvp, setsid */
#include <stdarg.h>   /* va_list, va_start, va_arg, va_end */
#include <dirent.h>

#include <sys/stat.h>
#include <sys/user.h>
#include <sys/syscall.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h> /* waitpid(,,) */
#include <mysql/mysql.h>

#include "okcalls.h" // zoj

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

#define ZOJ_COM
/*copy from ZOJ
 http://code.google.com/p/zoj/source/browse/trunk/judge_client/client/tracer.cc?spec=svn367&r=367#39
 */
#ifdef __i386
#define REG_SYSCALL orig_eax
#define REG_RET eax
#define REG_ARG0 ebx
#define REG_ARG1 ecx
#else
#define REG_SYSCALL orig_rax
#define REG_RET rax
#define REG_ARG0 rdi
#define REG_ARG1 rsi

#endif

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
int get_proc_status(int pid, const char *mark) {
    FILE * pf;
    char fn[BUFFER_SIZE], buf[BUFFER_SIZE];
    int ret = 0;
    sprintf(fn, "/proc/%d/status", pid);
    pf = fopen(fn, "r");
    int m = strlen(mark);
    while (pf && fgets(buf, BUFFER_SIZE - 1, pf)) {
        buf[strlen(buf) - 1] = 0;
        if (strncmp(buf, mark, m) == 0) {
            sscanf(buf + m + 1, "%d", &ret);
        }
    }
    if (pf)
        fclose(pf);
    return ret;
}
int get_page_fault_mem(struct rusage & ruse, pid_t & pidApp) {
    //java use pagefault
    int m_vmpeak, m_vmdata, m_minflt;
    m_minflt = ruse.ru_minflt * getpagesize();
    if (0 && DEBUG) {
        m_vmpeak = get_proc_status(pidApp, "VmPeak:");
        m_vmdata = get_proc_status(pidApp, "VmData:");
        printf("VmPeak:%d KB VmData:%d KB minflt:%d KB\n", m_vmpeak, m_vmdata,
                m_minflt >> 10);
    }
    return m_minflt;
}
void print_runtimeerror(char * err){
    FILE *ferr=fopen("error.out","a+");
    fprintf(ferr,"Runtime Error:%s\n",err);
    fclose(ferr);
}
long get_file_size(const char *filename) {
    struct stat f_stat;
    if (stat(filename, &f_stat) == -1) {
        return 0;
    }
    return (long)f_stat.st_size;
}
void write_log(const char *fmt, ...) {
    va_list ap;
    char buffer[4096];
    sprintf(buffer, "%s/log/client.log", oj_home); /**  /oj_home/log/client.log   */
    FILE *fp = fopen(buffer, "a+");
    if (fp == NULL) {
        fprintf(stderr, "openfile error!\n");
        system("pwd");  /// 打印出当前所在路径
    }
    va_start(ap, fmt);  /// 将第一个可变参数的地址赋给ap，即ap指向可变参数列表的开始
    int l = vsprintf(buffer, fmt, ap); /// 将参数 ap 和 format 进行转化形成格式化字符串，即可以显示的字符串。返回生成子串的长度。
    printf("L : %d\n", l);
    fprintf(fp, "%s\n", buffer);
    va_end(ap);   /// 将参数ap复位
    fclose(fp);   /// 关闭文件流指针
}
void clean_workdir(char * work_dir ) {
    execute_cmd("umount %s/proc", work_dir);
    execute_cmd("rm -Rf %s/*", work_dir);
}

int call_counter[512];
void init_syscalls_limits(int lang) {
    memset(call_counter, 0, sizeof(call_counter));
    if (lang <= 1) { // C & C++
        for (int i = 0; LANG_CC[i]; i++) {
            call_counter[LANG_CV[i]] = LANG_CC[i];
        }
    } 
}
void prepare_files(char *filename, int namelen, char *infile, int& p_id, char *work_dir, char *outfile, char *userfile, int runner_id) {
    char fname[BUFFER_SIZE];
    strncpy(fname, filename, namelen);
    fname[namelen] = 0;
    sprintf(infile, "%s/data/%d/%s.in", oj_home, p_id, fname);   /* infile == /oj_home/data/1005/sample.in  */
    execute_cmd("cp %s %sdata.in", infile, work_dir);    // work_dir == /oj_home/run2/data.in -- 2 is client_id
    /*  /home/judge/run6/data.in */
    sprintf(outfile, "%s/data/%d/%s.out", oj_home, p_id, fname); /* outfile =  /home/judge/data/1003/sample.out */
    sprintf(userfile, "%s/run%d/user.out", oj_home, runner_id);  /* userfile = /home/judge/run2/user.out   */
}
int isInFile(const char fname[]) {
    int l = strlen(fname);
    if (l <= 3 || strcmp(fname + l - 3, ".in") != 0)
        return 0;
    else
        return l - 3;
}
/*
int fgetc(FILE *stream);
 意为从文件指针 stream 指向的文件中读取一个字符, 读取一个字节后，光标位置后移一个字节。
 这个函数的返回值，是返回所读取的一个字节。如果读到文件末尾或者读取出错时返回EOF。
*/
void find_next_nonspace(int& c1, int& c2, FILE*& f1, FILE*& f2, int& ret) {
    // Find the next non-space character or \n.
    while ((isspace(c1)) || (isspace(c2))) {
        if (c1 != c2) {
            if (c2 == EOF) {
                do {
                    c1 = fgetc(f1);
                } while (isspace(c1));
                
                continue;
            } 
            else if (c1 == EOF) {
                do {
                    c2 = fgetc(f2);
                } while (isspace(c2));
                continue;
            } 
            else if ((c1 == '\r' && c2 == '\n')) {
                c1 = fgetc(f1);
            } 
            else if ((c2 == '\r' && c1 == '\n')) {
                c2 = fgetc(f2);
            } 
            else {
                ret = OJ_PE;
            }
        }
        if (isspace(c1)) {
            c1 = fgetc(f1);
        }
        if (isspace(c2)) {
            c2 = fgetc(f2);
        }
    }
}
/*
 * translated from ZOJ judger r367
 * http://code.google.com/p/zoj/source/browse/trunk/judge_client/client/text_checker.cc#25
 *
 */
int compare_zoj(const char *file1, const char *file2) {
    int ret = OJ_AC;
    FILE * f1, *f2;
    f1 = fopen(file1, "r");  // FILE *fopen(const char *restrict pathname, const char *restrict type);
    f2 = fopen(file2, "r");
    if (!f1 || !f2) {
        ret = OJ_RE;
    } 
    else {
        while(true) {
            // Find the first non-space character at the beginning of line.
            // Blank lines are skipped.
            int c1 = fgetc(f1);
            int c2 = fgetc(f2);
            find_next_nonspace(c1, c2, f1, f2, ret);
            // Compare the current line.
            while(true) {
                // Read until 2 files return a space or 0 together.
                while ((!isspace(c1) && c1) || (!isspace(c2) && c2)) {
                    if (c1 == EOF && c2 == EOF) {
                        goto end;
                    }
                    if (c1 == EOF || c2 == EOF) {
                        break;
                    }
                    if (c1 != c2) {
                        // Consecutive non-space characters should be all exactly the same
                        ret = OJ_WA;
                        goto end;
                    }
                    c1 = fgetc(f1);
                    c2 = fgetc(f2);
                }
                find_next_nonspace(c1, c2, f1, f2, ret);
                if (c1 == EOF && c2 == EOF) {
                    goto end;
                }
                if (c1 == EOF || c2 == EOF) {
                    ret = OJ_WA;
                    goto end;
                }

                if ((c1 == '\n' || !c1) && (c2 == '\n' || !c2)) {
                    break;
                }
            }
        }
    }
    end: if (f1)
        fclose(f1);
    if (f2)
        fclose(f2);
    return ret;
}

void delnextline(char s[]) {
    int L;
    L = strlen(s);
    while (L > 0 && (s[L - 1] == '\n' || s[L - 1] == '\r'))
        s[--L] = 0;
}

int compare(const char *file1, const char *file2) {
#ifdef ZOJ_COM
    //compare ported and improved from zoj don't limit file size
    return compare_zoj(file1, file2);
#endif
#ifndef ZOJ_COM
    //the original compare from the first version of hustoj has file size limit
    //and waste memory
    FILE *f1,*f2;
    char *s1,*s2,*p1,*p2;
    int PEflg;
    s1=new char[STD_F_LIM+512];
    s2=new char[STD_F_LIM+512];
    if (!(f1=fopen(file1,"r")))
    return OJ_AC;
    for (p1=s1;EOF!=fscanf(f1,"%s",p1);)
    while (*p1) p1++;
    fclose(f1);
    if (!(f2=fopen(file2,"r")))
    return OJ_RE;
    for (p2=s2;EOF!=fscanf(f2,"%s",p2);)
    while (*p2) p2++;
    fclose(f2);
    if (strcmp(s1,s2)!=0) {
        //              printf("A:%s\nB:%s\n",s1,s2);
        delete[] s1;
        delete[] s2;

        return OJ_WA;
    } else {
        f1=fopen(file1,"r");
        f2=fopen(file2,"r");
        PEflg=0;
        while (PEflg==0 && fgets(s1,STD_F_LIM,f1) && fgets(s2,STD_F_LIM,f2)) {
            delnextline(s1);
            delnextline(s2);
            if (strcmp(s1,s2)==0) continue;
            else PEflg=1;
        }
        delete [] s1;
        delete [] s2;
        fclose(f1);fclose(f2);
        if (PEflg) return OJ_PE;
        else return OJ_AC;
    }
#endif
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
/* write runtime error message back to database */
void _addreinfo_mysql(int solution_id) {
    char sql[(1 << 16)], *end;
    char reinfo[(1 << 16)], *rend;
    FILE *fp = fopen("error.out", "r");
    snprintf(sql, (1 << 16) - 1,
            "DELETE FROM runtimeinfo WHERE solution_id=%d", solution_id);
    mysql_real_query(conn, sql, strlen(sql));
    rend = reinfo;
    while (fgets(rend, 1024, fp)) {
        rend += strlen(rend);
        if (rend - reinfo > 40000)
            break;
    }
    rend = 0;
    end = sql;
    strcpy(end, "INSERT INTO runtimeinfo VALUES(");
    end += strlen(sql);
    *end++ = '\'';
    end += sprintf(end, "%d", solution_id);
    *end++ = '\'';
    *end++ = ',';
    *end++ = '\'';
    end += mysql_real_escape_string(conn, end, reinfo, strlen(reinfo));
    *end++ = '\'';
    *end++ = ')';
    *end = 0;
    //  printf("%s\n",ceinfo);
    if (mysql_real_query(conn, sql, end - sql))
        printf("%s\n", mysql_error(conn));
    fclose(fp);
}
void addreinfo(int solution_id) {
    _addreinfo_mysql(solution_id);
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
    row = mysql_fetch_row(res); // fopen是标准c里的,而open是linux的系统调用.
    sprintf(src_pth, "Main.%s", lang_ext[lang]);
    FILE* fp_src = fopen(src_pth, "w");  /* 创建 Main.cc 文件 */
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
void get_problem_info(int p_id, int &time_lmt, int &mem_lmt, int &isspj) {
    _get_problem_info_mysql(p_id, time_lmt, mem_lmt, isspj);
}
void _get_solution_info_mysql(int solution_id, int & p_id, char * user_id, int & lang) {
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
    const char* utf8sql = "set names utf8"; // 设置 mysql 编码方式，尽量与网站编码一致，避免乱码。
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
void read_int(char *buf, const char * key, int * value) {
    char buf2[BUFFER_SIZE];
    if (read_buf(buf, key, buf2)) {
        sscanf(buf2, "%d", value);  // 从一个字符串中读进与指定格式相符的数据.
    }
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
            read_int(buf, "OJ_HTTP_JUDGE", &http_judge);
            read_buf(buf, "OJ_HTTP_BASEURL", http_baseurl);
            read_buf(buf, "OJ_HTTP_USERNAME", http_username);
            read_buf(buf, "OJ_HTTP_PASSWORD", http_password);
            read_int(buf, "OJ_USE_MAX_TIME", &use_max_time);
        }
        fclose(fp);
    }
}
void run_solution(int& lang, char *work_dir, int& time_lmt, int& usedtime, int& mem_lmt) {
    nice(19); /***  调整进程运行的优先级  linux 进程运行的优先级分为-20~19等40个级别  19 进程运行的优先级最低 */
    struct rlimit LIM;        ///  系统单进程资源参数限制，当前进程的资源限制，并由其子进程继承 ///
    // cpu limit
    LIM.rlim_cur = (time_lmt - usedtime / 1000) + 1;  /**  用掉的扣除(严密吧！)，+1给超时程序1秒宽限  */
    LIM.rlim_max = LIM.rlim_cur;
    setrlimit(RLIMIT_CPU, &LIM); /// CPU时间的最大值(秒)，当超过时，进程发送 SIGXCPU 信号。
    
    alarm(LIM.rlim_cur * 2 + 3); /// alarm 称为闹钟函数 时间到的时候 它向进程发送 SIGALRM 信号  信号由内核产生
    // file limit
    LIM.rlim_max = STD_F_LIM + STD_MB;
    LIM.rlim_cur = STD_F_LIM;        /// 总文件限制 32M
    setrlimit(RLIMIT_FSIZE, &LIM);   /// 可以创建的文件的最大字节长度
    // proc limit
    LIM.rlim_cur = LIM.rlim_max = 1; /// 就一个
    setrlimit(RLIMIT_NPROC, &LIM);   /// 每个实际用户(real user)可拥有的最大子进程数目 
    // set the stack
    LIM.rlim_cur = STD_MB << 6;      
    LIM.rlim_max = STD_MB << 6;      /// 栈限制 - 64M 
    setrlimit(RLIMIT_STACK, &LIM);   /// 栈的最大字节长度
    // set the memory
    LIM.rlim_cur = STD_MB *mem_lmt*1.5;  /// mem_lmt  + 0.5 * mem_lmt  ( + 0.5倍标准题内存的限制)
    LIM.rlim_max = STD_MB *mem_lmt*2;    /// 2倍 M
    
    setrlimit(RLIMIT_AS, &LIM);  /// 进程可用内存最大字节数， 这里限制为 1.5倍场。
    
    chdir(work_dir);  ///  cd /home/judge/run2
    // open the files
    freopen("data.in", "r", stdin);
    freopen("user.out", "w", stdout);
    freopen("error.out", "a+", stderr);
    /// trace me   -  explain :  使用ptrace
    ptrace(PTRACE_TRACEME, 0, NULL, NULL); 
    /** 
     * 使用 PTRACE_TRACEME, 当前进程(子进程) 它告诉内核 ： 让别人跟踪我吧！
     *  然后，在子进程系统调用之后，它将控制权交还给父进程。当时父进程正使用wait()函数来等待来自内核的通知 
     */
    // run me
    chroot(work_dir); /// http://www.ibm.com/developerworks/cn/linux/l-cn-chroot/  子进程将继承！
     /** (1) 增加了系统的安全性，限制了用户的权力；
      *  (2) 建立一个与原系统隔离的系统目录结构，方便用户的开发；
      */
    // now the user is "judger"
    /** RUID, 用于在系统中标识一个用户是谁，当用户使用用户名和密码成功登录后一个UNIX系统后就唯一确定了他的RUID.
        EUID, 用于系统决定用户对系统资源的访问权限，通常情况下等于RUID。 
        SUID，用于对外权限的开放。跟RUID及EUID是用一个用户绑定不同，它是跟文件而不是跟用户绑定。
    */
    setuid(1536); // 改变进程 euid
    setresuid(1536, 1536, 1536);
    switch (lang) {
        case 0:
        case 1:
        case 2:
            execl("./Main", "./Main", NULL); /// 系统调用之后，它将控制权交还给父进程。当时父进程正使用wait4()函数来等待来自内核的通知 
        break;
    }
    exit(0);
}
void watch_solution(pid_t pidApp, char *infile, int& ACflg, int isspj,
        char *userfile, char *outfile, int solution_id, int lang,
        int& topmemory, int mem_lmt, int& usedtime, int time_lmt, int& p_id,
        int& PEflg, char *work_dir) {
    // parent
    int tempmemory;

    int status, sig, exitcode;
    struct user_regs_struct reg;
    struct rusage ruse;
    int sub = 0;
    while (1) {  
        wait4(pidApp, &status, 0, &ruse); /// 该 rusage 参数，内核返回由终止进程及其所有子进程使用的资源汇总。
        /// 等待进程ID等于pidApp的子进程 -- 终止进程终止的状态存在 status 中！
        /// 因为前面有 ptrace 所有等到了来自内核的信号
        if (WIFEXITED(status)) {  /// WIFEXITED（status）如果子进程正常结束则为非 0 值。
            break;
        } /// 其实这时候，我试验被跟踪进程(子进程)是不会退出的！其实是 内核暂停当前进程，将控制权交给跟踪进程(这里为父进程)
        if (get_file_size("error.out")) {
            ACflg = OJ_RE;
            ptrace(PTRACE_KILL, pidApp, NULL, NULL); // pidApp 被跟踪进程
            /**
             * PTRACE_KILL被用来终止子进程.”谋杀”是这样进行的: 首先 ptrace() 查看子进程是不是已经死了.
             *  如果不是, 子进程的返回码被设置为sigkill. single-step位被复位.
             *  然后子进程被唤醒,运行到返回码时子进程就死掉了.
             */
            break;
        }
        if (!isspj && get_file_size(userfile) > get_file_size(outfile) * 10+1024) {
            ACflg = OJ_OL;   // Output Limit Exceeded
            ptrace(PTRACE_KILL, pidApp, NULL, NULL); // PTRACE_KILL被用来终止子进程.”
            break;
        }
        exitcode = WEXITSTATUS(status); // 取得子进程exit（）返回的结束代码

        if (exitcode == 0x05 || exitcode == 0) {  /// go on and on
            ; /// exitcode == 5 waiting for next CPU allocation
        }
        else {
            if (ACflg == OJ_AC) {
                switch (exitcode) {
                    case SIGCHLD:  // 子进程状态改变
                    case SIGALRM:  // 超时 alarm
                    case SIGKILL:  // 终止
                    case SIGXCPU:  // 超过CPU限制
                        ACflg = OJ_TL;
                        break;
                    case SIGXFSZ:  // 超过文件长度限制
                        ACflg = OJ_OL;
                        break;
                    default:
                        ACflg = OJ_RE;
                }
                print_runtimeerror(strsignal(exitcode));
            }
            ptrace(PTRACE_KILL, pidApp, NULL, NULL);
            break;
        }
        if (WIFSIGNALED(status)) {
        /**
          * WIFSIGNALED: if the process is terminated by signal 
          * psignal(int sig, char *s)，like perror(char *s)，print out s, with error msg from system of sig  
          * sig = 5 means Trace/breakpoint trap
          * sig = 11 means Segmentation fault
          * sig = 25 means File size limit exceeded
          */
            sig = WTERMSIG(status);///取得子进程因信号而中止的信号代码
            if (ACflg == OJ_AC){
                switch (sig) {
                case SIGCHLD:
                case SIGALRM:
                case SIGKILL:
                case SIGXCPU:
                    ACflg = OJ_TL;
                    break;
                case SIGXFSZ:
                    ACflg = OJ_OL;
                    break;
                default:
                    ACflg = OJ_RE;
                }
                print_runtimeerror(strsignal(sig));
            }
            break;
        }
        /**
           WIFSTOPPED: return true if the process is paused or stopped while ptrace is watching on it
           WSTOPSIG: get the signal if it was stopped by signal
         */
        // check the system calls
        ptrace(PTRACE_GETREGS, pidApp, NULL, &reg);  // 用来读子进程的寄存器

        if (call_counter[reg.REG_SYSCALL] == 0) { //do not limit JVM syscall for using different JVM
            ACflg = OJ_RE;
            char error[BUFFER_SIZE];
            sprintf(error,"[ERROR] A Not allowed system call: runid:%d callid:%ld\n", solution_id, reg.REG_SYSCALL);
            write_log(error);
            print_runtimeerror(error);
            ptrace(PTRACE_KILL, pidApp, NULL, NULL);
        } 
        else {
            if (sub == 1) {
                call_counter[reg.REG_SYSCALL]--;
            }
        }
        sub = 1 - sub;

        //jvm gc ask VM before need,so used kernel page fault times and page size

        tempmemory = get_proc_status(pidApp, "VmPeak:") << 10;
        
        if (tempmemory > topmemory)
            topmemory = tempmemory;
        if (topmemory > mem_lmt * STD_MB) {
            if (ACflg == OJ_AC) {
                ACflg = OJ_ML;
            }
            ptrace(PTRACE_KILL, pidApp, NULL, NULL);
            break;
        }
        ptrace(PTRACE_SYSCALL, pidApp, NULL, NULL);
    }
    usedtime += (ruse.ru_utime.tv_sec * 1000 + ruse.ru_utime.tv_usec / 1000);  // 用户级时间
    usedtime += (ruse.ru_stime.tv_sec * 1000 + ruse.ru_stime.tv_usec / 1000);  // 内核级时间
}
void judge_solution(int & ACflg, int & usedtime, int time_lmt, int isspj,
        int p_id, char * infile, char * outfile, char * userfile, int & PEflg,
        int lang, char * work_dir, int & topmemory, int mem_lmt, int solution_id ,double num_of_test)  {
    int comp_res;
    num_of_test=1.0;
    if (ACflg == OJ_AC && usedtime > time_lmt * 1000 * (num_of_test+1.0))
        ACflg = OJ_TL;
    // compare
    if (ACflg == OJ_AC) {
        comp_res = compare(outfile, userfile);
        if (comp_res == OJ_WA) {
            ACflg = OJ_WA;
        } 
        else if (comp_res == OJ_PE)
            PEflg = OJ_PE;
        ACflg = comp_res;
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
    get_solution(solution_id, work_dir, lang);        /* 创建 Main.cc 文件 source 从 Mysql source_code 表中取出 */
    
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
/** ---------************-----------  * */
    char fullpath[BUFFER_SIZE];
    char infile[BUFFER_SIZE];
    char outfile[BUFFER_SIZE];
    char userfile[BUFFER_SIZE];
    sprintf(fullpath, "%s/data/%d", oj_home, p_id); /** /home/judge/data/1003  **/
    // open DIRs
    DIR *dp;
    dirent *dirp;
    if ((dp = opendir(fullpath)) == NULL) {   /** 打开 /home/judge/data/1003 目录失败 **/
        write_log("No such dir:%s!\n", fullpath);
        if(!http_judge) {
            mysql_close(conn);
        }
        exit(-1);
    }
    /** 打开 /home/judge/data/1003 目录成功 **/
    int ACflg, PEflg;
    ACflg = PEflg = OJ_AC;
    int namelen;
    int usedtime = 0, topmemory = 0;
    // read files and run
    int num_of_test = 0;
    
    while ((ACflg == OJ_AC) && (dirp = readdir(dp)) != NULL) { /* readdir 用来读取目录。返回是dirent结构体指针 */
        namelen = isInFile(dirp->d_name); // check if the file is *.in or not,  为了获取某文件夹目录内容，所使用的结构体
        if (namelen == 0) continue;
        // 是 *.in file, 此时 infile, outfile, userfile 未知。
        prepare_files(dirp->d_name, namelen, infile, p_id, work_dir, outfile, userfile, runner_id); // client_id
        // prepare_files 准备infile 输入文件, outfile 输出文件!,  userfile(程序输出结果)
        init_syscalls_limits(lang);
        
        pid_t pidApp = fork(); // 调用 fork() 准备运行程序！
        if (pidApp == 0) {
            run_solution(lang, work_dir, time_lmt, usedtime, mem_lmt);  /* 语言，时间限制，内存限制  */
        }
        else {
            watch_solution(pidApp, infile, ACflg, isspj, userfile, outfile,
                    solution_id, lang, topmemory, mem_lmt, usedtime, time_lmt,
                    p_id, PEflg, work_dir); /// topmemory = 0,  usedtime = 0.
            
            judge_solution(ACflg, usedtime, time_lmt, isspj, p_id, infile,
                    outfile, userfile, PEflg, lang, work_dir, topmemory,
                    mem_lmt, solution_id,num_of_test);
            //clean_session(pidApp);
        }
    }
    if (ACflg == OJ_AC && PEflg == OJ_PE)
        ACflg = OJ_PE;
    
    if (ACflg == OJ_RE) addreinfo(solution_id);

    update_solution(solution_id, ACflg, usedtime, topmemory >> 10, 0, 0,0);

    update_user(user_id);
    update_problem(p_id);
    clean_workdir(work_dir);

    mysql_close(conn);
    return 0;
}
