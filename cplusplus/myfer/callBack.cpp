#include <stdio.h>
#include <string.h>
void MyFun(int x);     //这个申明也可写成：void MyFun( int );
void (*FunP)(int);    //也可申明成void(*FunP)(int x)，但习惯上一般不这样。

/* ---定义和实现一个类的成员函数为回调函数--- */
typedef void (*FunPtr)(int );

// 定义回调函数
class A {
public :
    // 回调函数，必须为public
    static void callBackFun(int value) {
        printf("I am callBackFun.\n%d\n", value);
    }
};
// 设置触发条件
void Funtype(FunPtr p) {
    (*p)(99);
}

int main() {
    Funtype(A::callBackFun);
    printf("success\n");
    
    MyFun(10);      //这是直接调用MyFun函数
    FunP = MyFun;  //将MyFun函数的地址赋给FunP变量
   (*FunP)(20);    //这是通过函数指针变量FunP来调用MyFun函数的。
    return 0;
}
    

void MyFun(int x) {  //这里定义一个MyFun函数
    printf("%d\n", x);
}
