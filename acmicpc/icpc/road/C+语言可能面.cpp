C+语言，可能面到。
/* 01_句柄类的实现 
 为了避免每个使用指针的类自己去控制引用计数，可以用一个类把指针封装起来。
 这个类对象可以出现在用户类使用指针的任何地方，而表现为一个指针的行为。 */
#include <iostream>  
#include <cstdio>  
#include <stdexcept>    
using namespace std;  
template <typename T>  
class Handle {  
private :  
    T *ptr;  
    size_t *use;  
    void decrUse() {  
        --(*use);  
        if(*use == 0) {  
            delete ptr;  
            delete use;  
        }  
    }  
public :  
    Handle(T *p = 0) : ptr(p), use(new size_t(1)) {}  
    Handle(const Handle& rhs) : ptr(rhs.ptr), use(rhs.use) { ++(*use); } // 初始化用到  
    Handle& operator= (const Handle& rhs) { // 覆盖以前的值  
        ++(*rhs.use);  
        decrUse();  
        ptr = rhs.ptr;  
        use = rhs.use;  
        return *this;
    }
    T* operator-> () {  
        if(ptr) return ptr;  
        throw std::runtime_error("NULL point");  
    }  
    const T* operator-> () const {  
        if(ptr) return ptr;  
        throw std::runtime_error("NULL point");  
    }  
    T& operator* () {  
        if(ptr) return *ptr;  
        throw std::runtime_error("NULL point");  
    }  
    const T& operator* () const {  
        if(ptr) return *ptr;  
        throw std::runtime_error("NULL point");  
    }  
    ~Handle() {  
        decrUse();  
    }  
};  
class A {  
public :  
    void show() {  
        printf("Hello! , I am A!!! \n");  
    }  
};  
int main() {  
    try {  
        Handle t(new A);  
        t->show();  
    } catch (const exception& err) {  
        cout << err.what() << endl;  
    }  
    return 0;  
} 
/* 句柄类理解补充 ：
class TA {
    int a;
    int *pi;
    char *pc;
};
句柄类写 对应于 
class TA {
    int a;
    Handle<int> pi;
    Handle<char> pc;
}  */
/* 02_单件模式的实现 */
class L {
public :
    static L* get() {
        if(!pi) {
            pi = new L();
        }
        return pi;
    }
    ~L() {
        if(pi) {
            delete pi;
            pi = 0;
        }
    }
    static L* pi;
private :
    L() {}; // 防止产生实例
    L(const L&) // 防止复制构造另一个实例
    L& operator= (const L&); // 防止赋值构造出另一个实例
}
L::pi = NULL;
[712] 已知 String 类定义，如何实现其函数体
/* m_20 String 的复制控制 */
#include <iostream>  
#include <cstring>  
using namespace std;  
  
class String { // 一切都是深拷贝！  
private :  
    char *m_data;  
public :  
    String(const char *str = NULL);  
    String(const String&);  
    String& operator =(const String& rhs);  
    ~String();  
    void show() {  
        cout << m_data << endl;  
    }  
};  
String::String(const char *str) { // 记住这里你是不能写 m_data = str, 不用犯这种想当然的错误！  
    if(str == NULL) {  
        m_data = new char[1];  
        m_data[0] = '\0';  
    }  
    else {  
        int len = strlen(str);  
        m_data = new char[len+1];  
        strcpy(m_data, str);  
    }  
}  
String::String(const String& rhs) {  
    if(rhs.m_data == NULL) {  
        m_data = new char[1];  
        m_data[0] = '\0';  
    }  
    else {  
        int len = static_cast<int> (strlen(rhs.m_data));  
        m_data = new char[len+1];  
        strcpy(m_data, rhs.m_data);  
    }  
}  
String& String::operator =(const String& rhs) {  
    if(this == &rhs) return *this; // *别落下！*  
    delete []m_data;  // *一定要先释放以前的！*  
    if(rhs.m_data == NULL) {  
        m_data = new char[1];  
        m_data[0] = '\0';  
    }  
    else {  
        int len = static_cast<int> (strlen(rhs.m_data));  
        m_data = new char[len+1];  
        strcpy(m_data, rhs.m_data);  
    }  
    return *this;  
}
String::~String() { 
    delete []m_data;
}
int main() {
    String s("hello");
    String s1("hi");
    s = s1;
    s.show();
    return 0;
}
1，OO的基本要素 : 封装，继承，多态。
2，虚函数表，多态的解析
   泛型技术，说白了就是试图使用不变的代码来实现可变的算法。比如：模板技术，RTTI技术，虚函数技术，
             要么是试图做到在编译时决议，要么试图做到运行时决议。
   C++的编译器应该是保证虚函数表的指针存在于对象实例中最前面的位置（这是为了保证取到虚函数表的有最高
   的性能——如果有多层继承或是多重继承的情况下）。 这意味着我们通过对象实例的地址得到这张虚函数表，然后
   就可以遍历其中函数指针，并调用相应的函数。
   (1) 一般继承（无虚函数覆盖） 
   (2) 一般继承（有虚函数覆盖） 
   (3) 多重继承（无虚函数覆盖）
   (4) 多重继承（有虚函数覆盖）
