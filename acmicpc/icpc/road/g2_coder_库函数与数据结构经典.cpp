#include <iostream>
using namespace std;
const int N = 105;
/* 01_strcpy 的实现，但要考虑地址重叠的实现 */
char* strcpy(char* dst, const char* src) {
    int size = strlen(src);
    if(dst == NULL || src == NULL) return NULL;
    const char *psrc;
    char *pdst;
    if(src < dst && src + size > dst) {
        psrc = src + size - 1;
        pdst = dst + size - 1;
        while(size--) {
            *pdst-- = *psrc--;
        }
    }
    else {
        psrc = src;
        pdst = dst;
        while(size--) {
            *pdst++ = *psrc++;
        }
    }
    return dst;
}
/* 02_memcpy 的实现，区域不可以重叠 */
void memcpy(void* dst, const void* src, size_t n); // dst头部不可以和src尾部重叠
/* 03_memmove(void* dst, const void* src, size_t n); // 区域可以重叠 dst头部可以和src尾部重叠 */
/* 04_strcmp 的实现 */
int strcmp(const char* src1, const char* src2) {
    int ret = 0;
    while(!(ret == *src1 - *src2) && *src2 && *src1) {
        ++src1; ++src2;
    }
    if(ret < 0) return -1;
    else if(ret > 0) return 1;
    return 0;
}
/* 05_strstr 的实现 */
char* strstr(char *src1, const char* src2) {
    if(src2 == NULL || !*str2) return src1;
    if(src1 == NULL || !*str1) return NULL;
    int pj = kmp(strlen(len1), strlen(len2));
    if(pj == -1) return NULL;
    return src1 + pj;
}
char* strstr2(char* src1, const char* src2) {
    if(src2 == NULL || !*src2) return src1;
    if(src1 == NULL || !*src1) return NULL;
    while(*src1) {
        for(int i = 0; *(src1 + i) == *(src2 + i); i++) {
            if(!*(src2+i+1)) return src1;
        }
        src1++;
    }
    return NULL;
}
/* 06_二叉树非递归的 先根遍历和中序遍历 */
void pre(Node* root) {
    Node* p = root;
    stack<Node*> S;
    while(1) {
        if(p != NULL) {
            cout << p->value << ' ';
            S.push(p);
            p = p->lchild;
        }
        else {
            if(S.empty()) return;
            p = S.top(); S.pop();
            p = p->rchild;
        }
    }
}
void ino(Node* root) {
    Node* p = root;
    stack<Node*> S;
    while(1) {
        if(p != NULL) {
            S.push(p);
            p  = p->lchild;
        }
        else {
            if(S.empty()) return;
            p = S.top(); S.pop();
            cout << p->value << ' ';
            p = p->rchild;
        }
    }
}
/* 07_八皇后的实现 */
const int N = 105;
int pos[N];
int num = 8, cnt = 0;
bool ok(int n) {
    for(int i = 0; i < n; i++) {
        if(pos[i] == pos[n]) return false;
        if(abs(pos[n] - pos[i]) == n-i) return false;
    }
    return true;
}
void dfs(int n) {
    if(n == num) {
        cnt++;
        return;
    }
    for(int i = 0; i < num; i++) {
        pos[n] = i;
        if(ok(n)) {
            dfs(n+1);
        }
    }
}
/* 08_Fire Net  dfs+回溯 如果题目数据大一点还要用到二分匹配。我好喜欢的一道题目！*/
const int N = 5; // hdoj 1045
char map[N][N];
int n, mmax = -1;
bool ok(int x, int y) {
    if(map[x][y] != '.') return false;
    for(int i = y-1; i >= 0; --i) {
        if(map[x][i] == 'X') break;
        if(map[x][i] == 'B') return false;
    }
    for(int i = x-1; i >= 0; --i) {
        if(map[i][y] == 'X') break;
        if(map[i][y] == 'B') return false;
    }
    return true;
}
void dfs(int pos, int num) {
    if(pos == n*n) {
        if(num > mmax) mmax = num;
        return;
    }
    int x = pos / n;
    int y = pos % n;
    if(ok(x, y)) {
        map[x][y] = 'B';
        dfs(pos+1, num+1);
        map[x][y] = '.';
    }
    dfs(pos+1, num);
}
using namespace std;
const int N = 5;
const int M = 30;
char str[N][N];
int p[N][N];
int rp[N][N];
int mmap[M][M];
int pre[M];
bool vis[M];
int n;
int x_id, y_id;
bool dfs(int u) {
    for(int i = 1; i <= y_id; i++) {
        if(mmap[u][i] && !vis[i]) {
            vis[i] = true;
            if(pre[i] == 0 || dfs(pre[i])) {
                pre[i] = u;
                return true;
            }
        }
    }
    return false;
}
int maxMatch() {
    memset(pre, 0, sizeof(pre));
    int num = 0;
    for(int i = 1; i <= x_id; i++) {
        memset(vis, 0, sizeof(vis));
        if(dfs(i)) num++;
    }
    return num;
}
int main() {
    while(cin >> n, n) {
        for(int i = 0; i < n; i++) {
            scanf("%s", str[i]);
        }
        memset(p, 0, sizeof(p));
        memset(rp, 0, sizeof(rp));
        x_id = 0, y_id = 0;
        for(int i = 0; i < n; i++) {
            int k = 0;
            while(k < n) {
                if(str[i][k] == '.') {
                    ++x_id;
                    while(k < n && str[i][k] != 'X') {
                        p[i][k] = x_id;
                        k++;
                    }
                }
                else k++;
            }
        }
        for(int i = 0; i < n; i++) {
            int k = 0;
            while(k < n) {
                if(str[k][i] == '.') {
                    ++y_id;
                    while(k < n && str[k][i] != 'X') {
                        rp[k][i] = y_id;
                        k++;
                    }
                }
                else k++;
            }
        }
        memset(mmap, 0, sizeof(mmap));
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(str[i][j] == '.') {
                    mmap[p[i][j]][rp[i][j]] = 1;
                }
            }
        }
        printf("%d\n", maxMatch());
    }
    return 0;
}
/* 09_ 矩阵相乘 */
a[m][n] * b[n][m] = c[m][m]
a :    a b c
       d e f
b:     1 2
       3 4
       5 6
结果 c :  1a+3b+5c  2a+4b+6c
         1d+3e+5f  2d+4e+6f    
int a[2][3] = {{100, 200, 300}, {400, 500, 600}};  
int b[3][2] = {{1, 2}, {3, 4}, {5, 6}};  
int c[2][2];  
void solve(int m, int n) {  
    for(int i = 0; i < m; ++i) {  
        for(int j = 0; j < m; j++) {  
            for(int k = 0; k < n; k++) {  
                c[i][j] += a[i][k]*b[k][j];
/* 10 链表的归并，翻转，找环， 查找。 */
struct Node { Node *next; int value; }; 
/* --- 你一定要相信递归是一个强大的思想 */
Node* mergeList(Node* head1, Node* head2) { // 有序链表的合并
    if(head1 == NULL) return head2;
    if(head2 == NULL) return head1;
    Node* tmp;
    if(head1->value < head2->value) {
        tmp = head1;
        head1 = head1->next;
    }
    else {
        tmp = head2;
        head2 = head2->next;
    }
    tmp->next = mergeList(head1, head2);
    return tmp;
}
Node* mergeSort(Node* head) {
    if(head == NULL) return NULL;
    Node* r_head = head;
    Node* head1 = head;
    Node* head2 = head;
    while(head2->next != NULL && head2->next->next != NULL) {
        head1 = head1->next;
        head2 = head2->next->next;
    }
    if(head1->next == NULL) return r_head; // 只有一个节点
    head2 = head1->next;
    head1 = head;
    r_head = mergeList(mergeSort(head1), mergeSort(head2));
    return r_head;
}
/* 11 poj 3903 最长上升子序列 dp n^2 vs （二分 nlogn）
 F[t] 表示从 1 到t这一段中以 t 结尾的最长上升子序列的长度
 F[t] = max(1, F[j]+1); (j = 1, 2, ..., t-1. 且 A[j] < A[t])
此方程让我想到可以对比连续子数组的最大和。  dp: F[i] = max(a[i], F[i-1]+a[i]);
*/
/* 12 圆圈中最后剩下的数字。 */
int cirnm(int n, int m) {
    if(n < 1 || m < 1) return -1;
    int i = 0, count = 0; 
    list<int> L;
    for(int i = 0; i < n; i++) L.push_back(i);
    list<int>::iterator lcur = L.begin(), next;
    while(!L.empty()) {
        for(int i = 2; i <= m; ++i) {
            lcur++;
            if(lcur == L.end()) lcur = L.begin();
        }
        next = lcur + 1;
        if(next == L.end()) next = L.begin();
        L.erase(lcur);
        count++;
        if(count == n - 1) break;
        lcur = next;
    }
    return *lcur;
}
/* 13_[C++] 分别设计只能在栈, 堆中分配内存的类！
HeapOnly  -->  设计技巧是 ： 析构函数为 private
StackOnly  -->  设计技巧是 ： private 化 static void* operator new(size_t size);*/
[算法] 数组 - 相关的一些经典题目 ：一网打尽
1.1   如何用递归实现数组求和。   分析 ： return n == 0 ? 0 : getSum(a, n-1) + a[n-1];
1.4   如何用递归算法判断一个数组是否是递增   分析 ： return (a[n-1] > a[n-2]) && isIncrease(a, n-1);
1.5   如何分别使用递归与非递归实现二分查找算法(过)
1.7   如何计算两个有序整型数组的交集
   情况1 : 两数组长度相差不悬殊，解决方法：哈希其中一数组，在遍历另一个数组。 
   情况2 : 相差悬殊。小的数组中的数字，分别在长数组上二分查找。
1.8   如何找出数组中重复次数最多的数  ( 非常好的题目，map，遍历，if(++m[a[i]] >= m[val]) val = a[i] )
1.17 如何把一个整型数组中重复的数字去掉(sort1 1 1 2 2 3, 遍历数组，k=0, i=1; if(a[k] != a[i])) {a[k+1]=a[i];k++}
1.18 如何找出一个数组中第二大的数
   (解析 ：法1 : 堆筛。 法2 : 遍历，用两个变量记录 maxv=a[0] ,sec_max=MIN, 更新)
1.19 如何最少的比较次数寻找数组中的最小值和最大值。 《编程之美》 168P，给的证明比较次数为 1.5N 次。
void getMaxMin(int *a, int low, int high, int& maxv, int& minv) {  
    int mid, max1, max2, min1, min2;  
    if(high - low == 1 || high - low == 0) {  
        a[low] > a[high] ? (maxv = a[low], minv = a[high]) : (maxv = a[high], minv = a[low]);  
    }  
    else {  
        mid = (high + low) / 2;  
        getMaxMin(a, low, mid, max1, min1);  
        getMaxMin(a, mid+1, high, max2, min2);  
        maxv = max2 > max1 ? max2 : max1;  
        minv = min2 > min1 ? min1 : min2;  
    }  
}  
1.21 如何计算出序列的前n项数据
 描述 ： 正整数序列Q中的每个元素都至少能被正整数 a 和 b 中的一个整除。 求 序列前 n 项数据？
    解析 ： 类似于归并的思想
     for(int k = 0; k < N; k++) {tmpA = a*i; tmpB = b*j; if(tmpA <= tmpB) {Q[k] = tmpA; i++;} else}
1.23 如何判断一个整数x是否可以表示成n（n≥2）个连续正整数的和
  解析 ：
     x = m + (m+1) + (m+2) + ... + (m+n-1), 推出 : m = (2*x / n - n + 1) / 2; m >= 1;
    也就是判断 (2*x / n - n + 1) 是不是偶数的问题。
    tmp = m; if(tmp == (int)tmp) 就 OK！
[算法] 链表 - 经典的一些题目 - (小网打尽) 
2.9 如何进行单链表排序(很好 ： 归并排序法)
2.10 如何实现单链表交换任意两个元素（不包括表头）(非常好的题目-面试的时候很难写上)
struct Node {
    int value;
    Node* next;
};
Node* findPre(Node* head, Node* p) {
    Node* q = head;
    while(q) {
        if(q->next == p)
            return q;
        else q = q->next;
    }
    return NULL;
}
Node* swap(Node* head, Node* p, Node* q) {
    if(head == NULL || p == NULL || q == NULL) { return NULL; }
    if(p == q) return head;
    if(p->next == q) {
        Node* pre_p = findPre(head, p);
        pre_p->next = q;
        p->next = q->next;
        q->next = p;
    }
    else if(q->next == p) {
        Node* pre_q = findPre(head, q);
        pre_q->next = p;
        q->next = p->next;
        p->next = q;
    }
    else if(p != q) {
        Node* pre_p = findPre(head, p);
        Node* pre_q = findPre(head, q);
        Node* after_p = p->next;
        q->next = after_p;
        pre_p->next = q;
        pre_q->next = p;
    }
    return head;
}
2.11 如何检测一个较大的单链表是否有环
 answ1 : fast, slow;
 answ2 : STL 中的 map<Node*, int>
2.13 如何删除单链表中的重复结点
 answ : hash_map
2.12 如何判断两个单链表（无环）是否交叉
2.15 什么是循环链表
2.16 如何实现双向链表的插入、删除操作
2.18 如何删除结点的前驱结点
2.19 如何实现双向循环链表的删除与插入操作 343
2.20 如何在不知道头指针的情况下将结点删除 344
m_0606 如何判断两颗二叉树是否相等。
 A, B 两棵树相等，当且仅当 rootA->c == rootB->c; 而且A,B左右子树相等。我觉得是这样！
******** 第 14 章 海量数据处理 ********     
14-1 基本方法
  针对海量数据 ： Hash法，Bit-map法， Bloom filter法， 数据库优化法，倒排索引法，外排序法，Trie法，堆，双层桶法以及MapReduce法。
m_0301 topK 问题
topK : 分治 + Trie, 树/hash + 小项堆， 即先将数据集按照Hash方法分解成多个小数据集，
 然后使用Trie树或者Hash统计每个小数据集中出现频率最高的前K个数 ，最后在所有 topK 中求出最终的 top K.
例如 ： 有 1 亿个浮点数，如何找出其中最大的 10000 个？ [一亿个float 400M， 内存小的计算机排不了！]
 answ1 : 局部淘汰法  用一个容器保存前 10000 个数，然后将剩余的所有数字一一与容器内的最小数字相比。
 answ2 : 分治法  1 亿 = 100 个 100万。  100W 查找 10000 用类似快排的分治法。
 answ3 : 先 hash 去重。然后通过 分治法 或 最小堆法 查找最大的 10000 个数。
 answ4 : 最小堆。首先读入 1W 个数来创建大小为 1W 的小项堆。建堆的时间复杂度为 O(logm)
下面针对不同的应用场景分析 ： 1) 单机器，单核，足够大内存 2) 单机，多核，足够大内存 3) 单机，单核，受限内存 4) 多机，受限内存
MapReduce 是云计算的核心技术之一，是一种简化并行计算的分布式编程模型。
