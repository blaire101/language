#include <iostream>
#include <cstring>
#include <cstdio>
#include <cmath>
#include <string>
#include <list>
#include <queue>
#include <deque>
#include <vector>
#include <map>
#include <set>
using namespace std;
const int N = 105;
/* cpp 程序，会有一个宏 __cplusplus */
#ifdef __cplusplus
#define USING_C 0
#else
#define USING_C 1
#endif
/* 02_Singleton模式 */
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
    L(const L&);
    L& operator=(const L&);
};
L* L::pi = NULL;
/* 03_二维数组中的查找 */
/* 04_替换空格 */
/* 05_从尾到头打印链表 */
/* 06_重建二叉树 根据前序与中序 */
Node* solve(int pre[], int ino[], int len) { // 重新建立二叉树
    if(len <= 0) {
        return 0;
    }
    int r_v = pre[0];
    Node *root = new Node();
    root->value = r_v;
    int i = 0;
    for(i = 0; i < len; ++i) {
        if(r_v == ino[i]) {
            break;
        }
    }
    if(i != len) {
        root->lchild = solve(pre+1, ino, i);
        if(!ok) return 0;
        root->rchild = solve(pre+i+1, ino+i+1, len-i-1);
        return root;
    }
    ok = false;
    return NULL;
}
/* 07_两个栈实现队列 */
template<typename T>
class Queue {
public :
    Queue() {}
    ~Queue() {}
    T front();
    void pop();
    void push(const T&);
private :
    stack<T> S1;
    stack<T> S2;
};
template<typename T>
T Queue<T>::front() { // 核心
    if(!S2.empty()) {
        T tmp = S2.top();
        return tmp;
    }
    else {
        assert(!S1.empty());
        while(!S1.empty()) {
            T tmp = S1.top();
            S1.pop();
            S2.push(tmp);
        }
        T tmp = S2.top();
        S2.pop();
        return tmp;
    }
}
template<typename T>
void Queue<T>::push(const T& data) {
    S1.push(data);
}
template<typename T>
void Queue<T>::pop() { // 该函数为了 AC 题需要。
    if(!S2.empty()) {
        cout << S2.top() << endl; //
        S2.pop();
    }
    else {
        if(S1.empty()) {
            cout << -1 << endl; //
            return;
        }
        while(!S1.empty()) {
            T tmp = S1.top();
            S1.pop();
            S2.push(tmp);
        }
        cout << S2.top() << endl; //
        S2.pop();
    }
}
/* 08_旋转数组的最小元素 */
int get_min(int arr[], int len) { // 非常好的方法。
    if(arr == NULL || len <= 0) return -1;
    int p1 = 0, p2 = len - 1;
    int mid = p1;
    while(arr[p1] >= arr[p2]) {
        if(p2 - p1 == 1) {
            mid = p2; break;
        }
        mid = (p1+p2) / 2;
        if(arr[p1] <= arr[mid]) p1 = mid;
        else if(arr[p2] >= arr[mid]) p2 = mid;
    }
    return mid;
}
/* 09_斐波那契数列及其变形 */
/* 10_二进制中 1 的个数 */
/* 11_数值的整数次方 */
const double EPS = 1e-12;
double Power(double base, int exp) { // 注意 0.00 -5
    if(exp == 0) return 1;
    if(exp == 1) return base;
    double result = Power(base, exp>>1);
    result *= result;
    if((exp & 0x1) == 1) {
        result *= base;
    }
    return result;
}
/* 12_打印1到最大的n位数 */
char str[N];
void dfs(int i, int len) { // i 代表位置
    if(i == len) {
        int k = 0;
        while(str[k] == '0') ++k;
        while(k < len) {
            cout << str[k++];
            if(k == len) {
                printf("\n");
                break;
            }
        }
        return;
    }
    for(int j = (int)'0'; j <= (int)'9'; ++j) {
        str[i] = (char)j;
        dfs(i+1, len);
    }
}
/* 13_在O(1)时间删除链表结点 */
/* 14_调整数组位数使奇数位于前面 */
void pre_odd(int a[], int l, int r) {
    while(1) {
        while(l < r && (a[r] & 0x1) == 0) r--;
        while(l < r && (a[l] & 0x1) == 1) l++;
        if(l >= r) break;
        swap(a[l], a[r]);
    }
}
/* 15_链表中倒数第k个结点 */
/* 16_反转链表 三指针法*/
Node* verse(Node* head) {
    if(head == NULL) return NULL;
    Node *p1 = head;
    Node *p2;
    while(p1->next != NULL) {
        p2 = p1->next;
        p1->next = p2->next;
        p2->next = head;
        head = p2;
    }
    return head;
}
/* 17_合并两个排序的链表 */
Node* merge(Node* head1, Node *head2) {
    if(head1 == NULL) return head2;
    if(head2 == NULL) return head1;
    Node *head3;
    if(head1->value < head2->value) {
        head3 = head1;
        head3->next = merge(head1->next, head2);
    }
    else {
        head3 = head2;
        head3->next = merge(head1, head2->next);
    }
    return head3;
}
/* 18_树的子结构 */
bool son(Node* p1, Node* p2) {
    if(p2 == NULL) return true;
    if(p1 == NULL) return false;
    if(p1->value == p2->value) {
        return son(p1->lchild, p2->lchild) && son(p1->rchild, p2->rchild);
    }
    return false;
}
bool f_18(Node* root1, Node* root2) {
    if(root2 == NULL) return true;
    if(root1 == NULL) return false;
    bool flag = false;
    if(root1->value == root2->value) {
        flag = son(root1, root2);
    }
    if(!flag) flag = f_18(root1->lchild, root2);
    if(!flag) flag = f_18(root1->rchild, root2);
    return flag;
}
/* 19_二叉树的镜像 */
/* 20_顺时针打印矩阵 狗血*/
/* 21_包含min函数的栈 */
/* 22_栈的压入、弹出序列 */
// 1 2 3 4 5
// 4 3 5 1 2
bool sovle(int *pPush, int *pPop, int len) {
    bool flag = false;
    if(pPush == NULL || pPop == NULL || len <= 0) return flag;
    int *pNextPop = pPop;
    int *pNextPush = pPush;
    stack<int> S;
    while(pNextPop - pPop < len) {
        while(S.empty() || S.top() != *pNextPop) {
            if(pNextPush - pPush == len) break; // good!
            S.push(*pNextPush);
            ++pNextPush;
        }
        if(S.top() != *pNextPop) break;
        S.pop();
        pNextPop++;
        if(pNextPop - pPop == len) flag = true;
    }
    return flag;
}
/* 23_从上到下打印二叉树 */
/* 24_二叉搜索树的后序遍历序列 */
bool recur(int *sec, int len) {
    if(sec == NULL) return false;
    if(len <= 1) return true;
    int i, rv = sec[len-1];
    for(i = 0; i < len - 1; ++i) {
        if(sec[i] > rv) {
            break;
        }
    }
    for(int j = i; j < len; j++) {
        if(sec[j] < rv) return false;
    }
    return recur(sec, i) && recur(sec+i, len-i-1);
}
/* 25_二叉树中和为某一值的路径 */
void recur(Node*, int, int, vector<int>&);
void solve(Node* root, int sum) {
    if(root == NULL) return;
    vector<int> V;
    int curSum = 0;
    recur(root, exSum, curSum, V);
}
void recur(Node* root, int exSum, int curSum, vector<int>& path) {
    curSum += root->value;
    path.push_back(root->value);
    if(curSum == exSum && root->lchild == NULL && root->rchild == NULL) {
        ; //  打印路径
    }
    if(root->lchild) recur(root->lchild, exSum, curSum, path);
    if(root->rchild) recur(root->rchild, exSum, curSum, path);
    curSum -= root->value;
    path.pop_back();
}
/* 26_复杂链表的复制 */
/* 27_二叉搜索树与双向链表 */
void convert(Node* root, Node*& pLast) { // 左子链表 <-> 当前节点 <-> 右子链表， 中序遍历
    if(root == NULL) return;
    if(root->lchild) convert(root->lchild, pLast);
    Node* pCur = root;
    pCur->lchild = pLast;
    if(pLast) pLast->rchild = pCur;
    pLast = pCur;
    if(root->rchild) convert(root->rchild, pLast);
}
/* 28_字符串的全排列 */
void recur(char* str, char* ps) {
    if(*ps == '\0') printf("%s\n", str);
    for(char* i = ps; *i != '\0'; ++i) {
        char tmp = *ps;
        *ps = *i;
        *i = tmp;
        recur(str, ps+1);
        tmp = *ps;
        *ps = *i;
        *i = tmp;
    }
}
/* 29_数组中出现次数超过一半的次数 */
/* 30_最小的K个数 */
// multiset< int, greater<int> > MS;
// multiset< int, greater<int> >::iterator;
void solve(const vector<int>& data, multiset<int, greater<int> >& MS, int k) {
    MS.clear();
    if(k < 1 || data.size() < k) return;
    vector<int>::const_iterator iter = data.begin();
    for(; iter != data.end(); ++iter) {
        if(MS.size() < k) {
            MS.insert(*iter);
        }
        else {
            multiset< int, greater<int> >::iterator sit = MS.begin();
            if(*iter < *sit) {
                MS.erase(sit);
                MS.insert(*iter);
            }
        }
    }
}
int assist(int *arr, int left, int right) {
    if(arr == NULL) return -1;
    if(left < right) {
        int x = a[0], l = left, r = right;
        while(l < r) {
            while(l < r && a[r] >= x) r--;
            while(l < r && a[l] <= x) l++;
            if(l >= r) break;
            swap(a[l], a[r]);
        }
        swap(x, a[l]);
        return l;
    }
    else return left;
}
// 解法二 ：
void set_k(int arr[], int n, int k) {
    if(arr == NULL || k > n || k <= 0 || n <= 0) return;
    int start = 0;
    int end = n - 1;
    int index = assist(arr, start, end);
    while(index != k-1) {
        if(index > k-1) end = index - 1;
        else if(index < k-1) start = index + 1;
        else break;
        index = assist(arr, start, end);
    }
}
/* 31_连续子数组的最大和 */  // 扩展 ： 环形加难
/* 32_从1到n整数中1出现的次数 狗血题 */
/* 33_把数组排成最小的数 */ // 心得 ： 排序cmp写好
/* 34_丑数 */
    arr[1] = 1; p2 = p3 = p5 = arr + 1;
    while(k <= n) {
        arr[++k] = get_min(*p2*2, *p3*3, *p5*5);
        while(*p2*2 <= arr[k]) p2++;
        while(*p3*3 <= arr[k]) p3++;
        while(*p5*5 <= arr[k]) p5++;
    } // 心得 ： 不在非丑数上浪费时间
/* 35_第一个只出现一次的字符
 * 36_数组中的逆序对 */
void merge_sort(int a[], int l, int h) {
    if(l >= h) return; 
    int m = (l+h)/2;
    merge_sort(a, l, m);
    merge_sort(a, m+1, h);
    int *arr = new int[h-l+1];
    int k = 0;
    int i = l, j = m + 1;   ///////////////////////i j
    while(i <= m && j <= h) {
        if(a[i] <= a[j]) {
            arr[k] = a[i];
            k++, i++;
        }
        else {
            ans += m - i + 1;
            arr[k] = a[j];
            k++, j++;
        }
    }
    while(i <= m) {
        arr[k] = a[i];
        k++, i++;
    }
    while(j <= h) {
        arr[k] = a[j];
        k++, j++;
    }
    for(i = l; i <= h; i++) {
        a[i] = arr[i-l];
    }
    delete []arr;
}
/* 37_两个链表的第一个公共节点 */ // 会(略)
/* 38_数字在排序数组中出现的次数 */
int a[] = {1, 2, 3, 3, 3, 4, 5};
int find(int a[], int l, int h, int num, int left) {
    int last = -1;
    while(l <= h) {
        int mid = (l + h) / 2;
        if(a[mid] > num) {
            h = mid - 1;
        }
        else if(a[mid] < num) {
            l = mid + 1;
        }
        else {
            last = mid;
            if(left) {
                h = mid - 1;
            }
            else l = mid + 1;
        }
    }
    return last;
}
/* 39_二叉树的深度 
 * 从二叉树的深度扩展到判断是否为平衡二叉树 */
bool isBalance(Node* root, int *dep) {
    if(root == NULL) {
        *dep = 0;
        return true;
    }
    int left = 0, right = 0;
    if(isBalance(root->lchild, &left) && isBalance(root->rchild, &right)) {
        int dif = left - right;
        if(dif >= -1 && dif <= 1) {
            *dep = 1 + (left > right) ? left : right;
            return true;
        }
    }
    return false;
}
/* 40_数组中只出现一次的数字 41_递增序列和为S的两个数字  */ // 略
/* 42_翻转单词顺序  Is core ! */
void solve() {
    char *p1 = str, *p2 = str;
    while(1) {
        if(*p2 == ' ' || *p2 == '\0') {
            vers(p1, p2-1);
            if(*p2 == '\0') break;
            p1 = p2 + 1;
            while(*p1 == ' ') p1++;
            p2 = p1 + 1;
        }
        else p2++;
    }
}
/* 42_左旋转字符串 同上 */
/* 43_N个骰子的点数 DP 好题目 */
int a[12][85];
// F(k, n) = F(k-1, n-1) + F(k-1, n-2) + ... + F(k-1, n-6);
// F(1, 1) = F(1, 2) = F(1, 3) = F(1, 4) = F(1, 5) = F(1, 6)
int main() {
    int k, n;
    while(scanf("%d%d", &k, &n) == 2) {
        memset(a, 0, sizeof(a));
        for(int i = 1; i <= 6; i++) {
            a[1][i] = 1;
        }
        for(int i = 2; i <= k; i++) {
            for(int j = i; j <= 6*i; j++) {
                for(int z = j-1; z >= 1 && z >= j-6; z--) {
                    a[i][j] += a[i-1][z];
                }
            }
        }
        // cout << a[k][n] << endl;
        cout << double(a[k][n]) / (double)(k*n) << endl;
    }
    return 0;
}
/* 44_扑克牌的顺子*/
1）确认5张牌中除了0，其余数字没有重复的（可以用表统计的方法）;
2) 满足这样的逻辑：（max，min分别代表5张牌中的除0以外的最大值最小值）
     如果没有0，   则 max-min = 4，则为顺子，否则不是
     如果有一个0， 则 max-min = 4或者3，则为顺子，否则不是
     如果有两个0， 则 max-min = 4或者3或者2，则为顺子，否则不是
/* 45_圆圈中最后剩下的数字 创新DP解法，秒杀offer */
f(n, m) = [0]   (n=1)
f(n, m) = [f(n-1, m) + m] % n   (n>1)
for(int i = 2; i <= n; i++) {
    last = (last+m) % i;
return last;
/* 46_求1+2+……+n */
class A {
public :
    static int sum;
    static int k;
    A() {
        k++;
        sum += k;
    }
    static void init() {
        sum = 0;
        k = 0;
    }
    static int getSum() {
        return sum;
    }
};
int A::k = 0;
int A::sum = 0;
int main() {
    int n;
    while(scanf("%d", &n) == 1) {
        A::init();
        A* pa = new A[n];
        printf("%d\n", A::getSum());
    }
    return 0;
}
int func(int n) {
	int i = 1;
	(n > 1) && (i = func(n-1) + n);
	return i;
}
/* 47_不用加减乘除做加法 */
/* 48_不能被继承的类 */
class A {
public :
	static A* get() {
        return new A();
	}
	static void del(A* p) {
		delete p;
		p = 0;
	}
private :
	A() {}
	~A() {}
} // 以上方法只能得到位于 堆 上的实例
// 方法二 ：
template<typename T> 
class MakeFinal {
	friend T;
private :
	MakeFinal() {}
	~MakeFinal() {}
}
class FinalClass : virtual public MakeFinal<FinalClass> { 
public :
    FinalClass() {}
    ~FinalClass() {}
} // extern 知识 : 虚继承在多重继承中可以防止二义性
/* 49_把字符串转换成整数 
     数字与字符串的相互转化  atoi 与 itoa
 */
// 0, 是否有输入 1, 前面的空格 2, 正负号 3, 溢出 4, 只要有非数字就不合法 *//* 溢出，空串，非数字，都返回 0 */
long long atoi_core(const char *str, int sign);
int my_atoi(const char *str) {
    if(str == NULL） {
        printf("Invalid Input\n");
        return -1;
    }
    while(*str == ' ') str++;
    int sign = (*str == '-') ? -1 : 1; // 确定符号位
    if(*str == '+' || *str == '-') str++;
    int result = 0;
    if(*str != '\0') {
        result = atoi_core(str, sign);
    }
    return sign * result;
}
long long atoi_core(const char *str, int sign) {
    long long result = 0;
    while(*str >= '0' || *str <= '9') {
        result = result * 10 + (*str - '0');
        if(sign == 1 && result > 0x7fffffff) {
            return 0;
        }
    if(*str == '\0') return result;
    return 0;
}
char *itoa(int num) {
    int sign = num;
    if(sign < 0) num = -num;
    char str[N];
    char temp[N];
    int i = 0, j = 0;
    do {
        temp[i] = num % 10 + '0';
        i++;
        num /= 10;
    } while(num != 0);
    if(sign < 0) temp[i] = '-';
    while(i >= 0) {
        str[j] = temp[i];
        i--;
        j++;
    }
    str[j] = '\0';
    return str;
}
/* 50_树中两个结点的最低公共祖先 */
vector<Node*> V1;
vector<NOde*> V2;
bool getNodePath(Node* root, Node* tar, vector<Node*>& V) { // 根左右, 回溯
    if(root == NULL) return false;
    V.push_back(root);
    if(root == tar) return true;
    bool flag = false;
    if(root->lchild) {
        flag = getNodePath(root->lchild, tar, V);
    }
    if(!flag && root->rchild) {
        getNodePath(root->rchild, tar, V);
    }
    if(!flag) V.pop_back();
    return flag;
}
Node* getCom(const vector<Node*>& V1, const vector<Node*>& V2) {
    vector<Node*>::const_iterator it1 = V1.begin();
    vector<Node*>::const_iterator it1 = V2.begin();
    Node *pLast = NULL;
    while(it1 != V1.end() && it2 != V2.end()) {
        if(*it1 != *it2) break;
        pLast = *it1;
        ++it1;
        ++it2;
    }
    return pLast;
}
