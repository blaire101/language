# C 语言编译详细过程

>  GCC编译过程 [ISO -> -ESc]

## 1. 预处理

```
[gan@localhost gcc]# gcc –E hello.c –o hello.i
``` 

## 2. 编译阶段

```
[gan@localhost gcc]# gcc –S hello.i –o hello.s
```

## 3. 汇编阶段

```c
[gan@localhost gcc]# gcc –c hello.s –o hello.o
```

## 4. 链接阶段

```c
[gan@localhost gcc]# gcc hello.o –o hello
```
