#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <unistd.h>
#include <fcntl.h>
#include <syslog.h>
#include <errno.h>
#include <sys/stat.h> // umask

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
static char oj_home[BUFFER_SIZE];

int lockfile(int fd) {  // unix提供记录锁(字节范围锁)机制确保进程正在单独修改一个文件。
    struct flock fl;  
    fl.l_type = F_WRLCK; // 独占性写锁定  
    fl.l_start = 0;      // 相对偏移量
    fl.l_whence = SEEK_SET;  // 相对偏移量起点， SEEK_SET 文件头  
    fl.l_len = 0;            // 开始直至最大可能的偏移量，整个文件就是。
    return (fcntl(fd, F_SETLK, &fl)); // F_SETLK 设置文件锁定的状态， 如果无法锁定返回 -1.  
}
int already_running() {
    int fd;
    char buf[16];
    fd = open(LOCKFILE, O_RDWR|O_CREAT, LOCKMODE); // "/var/run/judged.pid" (S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH)Permits the file's owner to read it.
    if(fd < 0) {
        syslog(LOG_ERR|LOG_DAEMON, "can't open %s: %s", LOCKFILE, strerror(errno));
        exit(1);
    }
    if(lockfile(fd) < 0) {
        if (errno == EACCES || errno == EAGAIN) { // 无权限 / 无数据可读
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
        syslog(LOG_ERR|LOG_DAEMON, "This daemon program is already running!\n"); // LOG_ERR-出错状态,LOG_DAEMON-系统守护进程
        return 1;  // openlog、syslog和closelog,日志信息会写入syslog.conf文件指定的位置->..
    }
    return 0;
}
