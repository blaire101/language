Chapter 11 进程与信号
 进程与信号构成了Linux操作环境的基础部分。
 如何在自己的程序中启动和停止其他的进程，如何让进程收发消息，如何避免僵尸进程。
 大纲 『
  (1) 进程的结构、类型和调度
  (2) 用不同的方法启动新进程
  (3) 父进程、子进程和僵尸进程
  (4) 什么是信号以及如何使用它们
』
11.1 什么是进程？
 进程由程序代码、数据、变量(占用着系统内存)、打开的文件和环境组成。
 Linux系统会在进程之间共享程序代码和系统函数库，所以在任何时刻内存中都只有代码的一份副本。
 Linux系统本身会运行着一些管理系统资源和控制用户访问的程序。
11.2 进程的结构 
 user : neil 和 user : rick 它们同时运行 grep 程序查找不同字符串。
          neil                               rick
   $ grep kirk trek.txt              $ grep troi nextgen.doc
           PID 101                           PID 102
            代码   ----> grep程序代码 <----    代码
         数据s="kirk"                       数据s="troi"
           函数库  ---->  C语言函数   <-----    函数库
            文件                                文件
  PID 2 ~ 32768
  init进程负责管理其他进程。
 程序代码是以只读的方式加载到内存中的，虽然不能对这个区域执行写操作，但它可以被多
 个进程安全地共享。
 @1, 共享函数库带来的另一个优点 ： 包含可执行程序grep的磁盘文件比较小。
                             因为它不包含共享函数库代码。
 @2，并不是程序在运行时，所需要的所有东西都可以被共享。例如：进程使用的变量，进程通过
      各自的文件描述符来访问文件。
 @3，进程有自己的桟空间，还有自己的环境变量。进程还必须维护自己的程序计数器。
      计数器用来记录它执行到的位置。即在执行线程中的位置。
        在使用线程时，进程可以有不止一个执行线程。
 @4，Linux， 在目录/proc中有一组特殊的文件，这些文件的特殊之处允许你
             “窥视”正在运行进程的内部情况。（类似于看目录里的文件一样）
11.2.1-进程表
  Linux进程表就像一个数据结构，它把当前加载到内存中的所有进程的有关信息保存在一个
    表中，其中包括进程的PID，进程的状态，命令字符串和其他一些PS命令输出的各类信息。
  OS通过进程的PID对它们进行管理，这些PID是进程表的索引。
   进程表的长度是有限制的，所以系统能够支持的同时运行的进程数也是有限制的。
11.2.2-查看进程
  ps -ef
  启动新进程并等待它们结束的能力是整个系统的基础。
 显示出来的进程只表示它是准备好运行，并不表示它正在运行，因为时间片很短。
 @3，Linux内核用进程调度器来决定下一个时间片应该分配给哪个进程。
         判断依据是进程的优先级。高的运行的更频繁。
         抢先式多任务处理，所以进程的挂起和继续运行无需彼此之间的协作。
    在一个如Linux这样的多任务系统中，多个程序可能会竞争使用同一资源。
    执行短期的突发性工作并暂停运行来等待输入的程序，要比持续占用处理器
   不断轮询来查看是否有新的输入到达的程序要更好。
   我们称表现良好的程序是nice程序。OS根据进程的nice值来决定它的优先级。
   我们可以用nice命令设置进程的nice值，使用renice命令调整它的值。
 @4，在某些情况下，只要还有高优先级的进程可以运行，低优先级
      的进程就根本不能运行。
11.3 启动新进程
#include <stdio.h>
#include <stdlib.h>
int main() {
    printf("Running ps with system\n");
    system("ps ax");
    printf("Done.\n");
    exit(0);
}
   system()函数用一个shell来启动想要执行的程序，所以可以把这个程序放到后台执行。
         局限性 ： 程序还必须要等待由 system 函数启动的进程结束之后才能继续。
 @2，创建进程的底层接口exec
     使用system函数远非启动其他进程的理想手段，它依赖shell, 还效率不高！
  1，替换进程映像
   exec 系列函数由一组相关的函数组成，他们在进程的启动方式和程序参数的
    表达方式各有不同。
  2，复制进程映像
    要想让进程同时执行多个函数，我们可以使用线程或者从原程序中创建
    一个完全分离的进程，后者就像init的做法一样，而不像exec替换线程
    我们可以通过调用fork创建一个新进程。这个系统调用复制当前进程，
    在进程表中创建一个新的表项，新表项中的许多属性与当前进程是相同的
    。但新进程有自己的数据空间，环境和文件描述符。fork和exec函数结合
    在一起使用就是创建新进程所需要的一切。
    最初的进程
        |
        V
      fork() --------------------
        |                        |
        V                        V
    返回一个新的PID             返回0
        |                        |
        V                        V
   原进程继续执行               新进程

   父进程中的fork()调用返回的是子进程的PID。子进程中的fork()调用返回的是0.
   pid_t new_pid;
   new_pid = fork();
   switch(new_pid) {
       case -1 :    // Error
            break;
       case 0 :     // we are child
            break;
       default :    // we are parent
            break;
   }
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>

int main() {
    pid_t pid;
    char *message;
    int n;
    printf("fork program starting\n");
    pid = fork();
    switch(pid) {
        case -1 :
            perror("fork failed");
            exit(1);
        case 0 :
            message = "This is the child";
            n = 5;
            break;
        default :
            message = "This is the parent";
            n = 3;
            break;
    }
    for(; n > 0; n--) {
        puts(message);
        sleep(1);
    }
    exit(0);
}
hp@ubuntu:~/linux$ ./fork1
fork program starting
7594
This is the parent
0
This is the child
This is the parent
This is the child
This is the parent
This is the child
This is the child
hp@ubuntu:~/linux$ This is the child
 3,等待一个进程
   当用fork启动一个子进程时，子进程就有了它自己的生命周期并将独立运行。
 wait系统调用将暂停父进程直到它的子进程结束为止。
 4，僵尸进程
  用fork来创建进程确实很有用，但你必须清楚子进程的运行情况。
  子进程终止时，它与父进程之间的关联还会保持，直到父进程也
  正常终止或父进程调用wait才告结束。因此，进程表中代表子进程
  的表项不会立刻释放。虽然子进程已经不再运行，但它仍然存在于
  系统中，因为它的退出码还需要保存起来，以备父进程今后的wait调用
  使用。这时它将成为一个死进程或僵尸进程。
 5，如果父进程异常终止，子进程就自动把PID为1的进程init作为自己的父进程。
   子进程已经是一个不再运行的僵尸进程，但因为其父进程异常终止，所以它由
   init接管，僵尸进程将一直保留在进程表中直到被init进程发现并释放。进程表
   越大，这一过程越慢，应该尽量避免zombie因为它一直消耗系统资源。
#include <sys/types.h>
#include <sys/wait.h>
pid_t waitpid(pid_t pid, int *stat_loc, int options); WNOHANG
11.3.3 输入输出重定向
11.3.4 线程
Linux系统中的进程可以互相协作，互相发送消息，互相中断，甚至可以共享内存段
 但是他们是自个独立的实体，要想在它们之间共享变量很难。线程程序不好写。 
11.4 信号
 信号 ： 响应某些条件而产生的一个事件。信号由shell和终端处理器生成来引起中断。
   可进程间传递消息或修改行为的一种方式。信号可被 ：  生成，捕获，响应，忽略。
   #include <signal.h>
   SIGKILL
   SIGSEGV
   SIGABORT ...
   核心转储文件 core ,  SIGCONT
 例 ：
  $ kill -HUP 512
  $ killall -HUP inetd
  使用信号并挂起程序的执行是Linux程序设计中一个重要部分。等事发生。。
  在只有一个CPU的多用户环境中尤其重要，进程共享着一个处理器，繁忙等待
  将对系统效率带来很大的影响。
 库函数 #include <signal.h>
 void (*signal(int sig, void (*func)(int)))(int); 实验 ： 信号处理 407P

