#include <iostream>
#include <cstdio>
#include <cmath>
#include <cstring>
#include <string>
#include <vector>
#include <list>
#include <queue>
#include <deque>
#include <algorithm>
/*  自然语言处理|数据挖掘|机器学习  垂直搜索相关技术很好！
百度
-熟悉Linux(类Unix)操作系统，熟悉TCP/IP协议 
-精通或熟练掌握Linux环境下的C/C++/PERL/PYTHON/SHELL等1至2种语言，对计算机体系结构有一定理解 
-有相关系统开发经验的更好 
职位要求：
热爱互联网，对搜索技术、探索未知领域有浓厚的兴趣精通C/C++、Java、PHP中至少一门编程语言，对数据结构和算法设计有较为深刻的理解具有windows、unix、linux等主流平台工程经验优秀的分析问题和解决问题的能力，勇于解决难题强烈的上进心和求知欲，较强的学习能力和沟通能力，具备良好的团队合作精神
奇虎360
2.熟练掌握C/C++语言；
3.有一定的服务端开发经验；
4.优秀的学习能力，对数据结构、算法分析、计算机系统结构、操作系统、网络等计算机基础具备扎实的功底；
6.对LAMP开发架构熟悉者优先。
腾讯
具有良好的算法基础及系统分析能力。 
熟悉LINUX/UNIX操作和开发环境，熟悉TCP/IP协议相关知识，有C/C++开发和网络编程经验。
阿里
 职位名称：研发工程师
【职位要求】
2.熟练掌握操作系统原理，扎实的数据结构及算法基础，并有所实践
3.拥有面向对象思想、具备良好的系统分析能力，至少熟悉C++或Java一门开发语言
4.熟悉并深入学习过一种或多种互联网应用所涉及的相关技术，包括但不限于：
TCP/IP原理，HTTP协议，Linux/BSD操作系统，分布式系统，Web应用开发技术及规范等
7.有竞赛经验和实习经验者优先 */
using namespace std;
struct Node {
	Node* lchild;
	Node* rchild;
};
/* 01_八皇后的实现 */
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
/* 02_Fire Net  dfs+回溯 如果题目数据大一点还要用到二分匹配。我好喜欢的一道题目！*/
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
/* 03_根据前序中序重新建立二叉树 */
Node* f3(int* pre, int* ino, int len) { // pre : 1, 2, 4, 7, 3, 5, 6, 8  ino : 4, 7, 2, 1, 5, 3, 8, 6
	if(pre == NULL || ino == NULL || len <= 0) return NULL;
	int r_v = pre[0];
	Node* root = new Node();
	root->value = r_v;
	int i;
	for(i = 0; ; i++) {
		if(ino[i] == r_v) break;
	}
	root->lchild = f3(pre+1, ino, i);
	root->rchild = f3(pre+i+1, ino+i+1, len-1-i);
	return root;
}
/* 04_二叉树中和为某一值的所有路径 */
void f4(Node*, int, int, vector<int>&);
void f4(Node* root, int exSum) {
	if(root == NULL) return;
	vector<int> V;
	int curSum = 0;
	f4(root, exSum, curSum, V);
}
void f4(Node* root, int exSum, int curSum, vecotr<int>& path) {
	curSum += root->value;
	path.push_back(root->value);
	if(curSum == exSum && root->lchild == NULL && root->rchild == NULL) {
		//; 打印vector中的路径
	}
	if(root->lchild) f4(root->lchild, exSum, curSum, path);
	if(root->rchild) f4(root->rchild, exSum, curSum, path);
	curSum -= root->value;
	path.pop_back();
}
/* 05_树2 是否是 树1 的子结构 */
bool son(Node* p1, Node* p2) {
	if(p2 == NULL) return true;
	if(p1 == NULL) return false;
	if(p1->value == p2->value) return son(p1->lchild, p2->lchild) && son(p1->rchild, p2->rchild);
	return false;
}
bool f5(Node* root1, Node* root2) {
	if(root2 == NULL) return true;
	if(root1 == NULL) return false;
	if(root1->value == root2->value) return son(root1, root2);
	bool flag = false;
	flag = f5(root->lchild, root2);
	if(!flag) {
		flag = f5(root->rchild, root2);
	}
	return flag;
}
/* 06_二叉搜索树后序遍历的结果 */
bool f6(int* sec, int len) {
	if(sec == NULL) return false;
	if(len <= 1) return true;
	int i, rv = sec[len-1];
	for(i = 0; i < len-1; i++) {
		if(sec[i] > rv) break;
	}
	for(int j = i; j < len-1; j++) {
		if(sec[j] < rv) return false;
	}
	return f6(sec, i) && f6(sec+i, len-i-1);
}
/* 07_二叉树两节点的最低公共祖先 */
vector<Node*> V1;
vector<Node*> V2;
bool getNodePath(Node* root, Node* tar, vector<Node*>& V) { // 根左右，回溯
	if(root == NULL) return false;
	V.push_back(root);
	if(root == tar) return true;
	bool flag = false;
	if(root->lchild) flag = getNodePath(root->lchild, tar, V);
	if(!flag && root->rchild) flag = getNodePath(root->rchild, tar, V);
	if(!flag) V.pop_back();
	return flag;
}
Node* getCom(const vector<Node*>& V1, const vector<Node*>& V2) {
	vector<Node*>::const_iterator it1 = V1.begin();
	vector<Node*>::const_iterator it2 = V2.begin();
	Node* pLast = NULL;
	while(it != V1.end() && it2 != V2.end()) {
		if(*it != *it2) break;
		pLast = *it1;
		++it1;
		++it2;
	}
	return pLast;
}
/* 08_从二叉树的深度扩展到判断是否为平衡二叉树 */
bool isBalance(Node* root, int* dep) {
	if(root == NULL) {
		*dep = 0;
		return true;
	}
	int left = 0, right = 0;
	if(isBalance(root->lchild, &left) && isBalance(root->rchild, &right)) {
		int diff = left - right;
		if(diff >= -1 && diff <= 1) {
			*dep = 1 + (left > right ? left : right);
			return true;
		}
	}
	return false;
}
/* 09_二叉搜索树与双向链表 */
void convert(Node* root, Node*& pLast) {
	if(root == NULL) return;
	if(root->lchild) convert(root->lchild, pLast);
	Node* pCur = root;
	pCur->lchild = pLast;
	if(pLast) pLast->rchild = pCur;
	pLast = pCur;
	if(root->rchild) convert(root->rchild, pLast);
}
/* 10_strcpy 的实现，但多要考虑地址重叠的实现 */
char* my_strcpy(char *dst, const char* src) {
	size_t size = strlen(src);
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
void my_memcpy(void* dst, const void* src, size_t n); // 区域不可以重叠
void my_memmove(void* dst, const void* src, size_t n); // 区域可以重叠
int my_strcmp(const char* src1, const char* src2) {
	int ret = 0;
	while(!(ret = *src1 - *src2) && *src2 && *str1) {
		++src1; ++src2;
	}
	if(ret < 0) return -1;
	else if(ret > 0) return 1;
	return 0;
}
char* my_strstr(char* src1, const char* src2) {
	if(src2 == NULL || !*src2) return src1;
	if(src1 == NULL || !*src2) return NULL; //
	int pj = kmp(strlen(str1), strlen(src2));
	if(pj == -1) return NULL;
	return src1 + pj;
}
char* my_strstr2(char* src1, const char* src2) {
	if(src2 == NULL || !*src2) return src1;
	if(src1 == NULL || !*src1) return NULL; //
    while(*str1) {
		for(int i = 0; *(str1+i) == *(str2+i); ++i) {
			if(!*(str2+i+1)) return str1;
		}
		str1++;
	}
	return NULL;
}
*/
/* 11_Linux端口占用情况的命令 netstat -an  
 *   -a 是显示所有连接和监听的端口
 *   -n 以数字形式
 */
/* 11_[C++] 句柄类的实现 *
为了避免每个使用指针的类自己去控制引用计数，可以用一个类把指针封装起来。这个类对象可以出现在用户类使用指针的任何地方，而表现为一个指针的行为。*/
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
	Handle<int> pc;
}  */
/* 12 6种排序的手写 input ： 8, 5, 4, 9, 2, 3, 6  */
void heapify(int a[], int i, int size) { // 堆化的维持需要用递归  
    int ls = 2*i, rs = 2*i + 1;  
    int large = i;  
    if(ls <= size && a[ls] > a[i]) {  
        large = ls;  
    }  
    if(rs <= size && a[rs] > a[large]) {  
        large = rs;  
    }  
    if(large != i) {  
        swap(a[i], a[large]);  
        heapify(a, large, size);  
    }  
}  
void buildHeap(int a[], int size) {  
    for(int i = size/2; i >= 1; i--) {  
        heapify(a, i, size);  
    }  
}  
void heapSort(int a[], int size) {  
    buildHeap(a, size);  
    int len = size;  
    for(int i = len; i >= 2; i--) {  
        swap(a[i], a[1]);  
        len--;  
        heapify(a, 1, len);  
    }  
}  
void quickSort(int a[], int left, int right) {  
    if(left < right) {  
        int l = left, r = right, x = a[l];  
        while(1) {  
            while(l < r && a[r] >= x) r--;  
            while(l < r && a[l] <= x) l++;  
            if(l >= r) break;  
            swap(a[r], a[l]);  
        }  
        swap(a[left], a[l]);  
        quickSort(a, left, l-1);  
        quickSort(a, l+1, right);  
    }  
}
void mergeSort(int a[], int l, int r) {  
    if(l >= r) return;  
    int mid = (l+r) / 2;  
    mergeSort(a, l, mid);  
    mergeSort(a, mid+1, r);  
    int *arr = new int[r-l+1];  
    int k = 0;  
    int i = l, j = mid+1;  
    while(i <= mid && j <= r) {  
        if(a[i] <= a[j]) {  
            arr[k++] = a[i++];  
        }  
        else {  
            arr[k++] = a[j++]; // ans += (mid+1-i);  
        }  
    }  
    while(i <= mid) arr[k++] = a[i++];  
    while(j <= r) arr[k++] = a[j++];  
  
    for(int i = l; i <= r; i++) {  
        a[i] = arr[i-l];  
    }  
    delete []arr;  
}
void insertSort(int a[], int len) {  
    int j;  
    for(int i = 1; i < len; i++) {// 新抓的每张扑克牌  
        int temp = a[i];  
        for(j = i-1; a[j] > temp && j >= 0; j--) {  
            a[j+1] = a[j];  
        }  
        a[j+1] = temp;  
    }  
}
void bubbleSort(int a[], int len) {  
    for(int i = 1; i < len; i++) {  
        for(int j = 0; j < len-i; j++) {  
            if(a[j] > a[j+1]) swap(a[j], a[j+1]);  
        }  
    }  
}
void selectSort(int a[], int len) {  
    int i, j, k;  
    for(i = 0; i < len-1; i++) {  
        k = i;  
        for(j = i+1; j < len; j++) {  
            if(a[j] < a[k]) k = j;  
        }  
        swap(a[i], a[k]);  // 将第i位小的数放入i位置  
    }  
}
/* 13_字符串的全排列 */
void res(char *str, char *pStr) {
	if(*pStr == '\0') cout << str << endl;
	for(char *p = pStr; p != '\0'; ++p) {
		char tmp = *p;
		*p = *pStr;
		*pStr = tmp;
		res(str, pStr+1);
		tmp = *p;
		*p = *pStr;
		*pStr = tmp;
	}
}
*m_14 字典树 trie / 并查集 / KMP (重要算法之一)
/* 1) 根节点不包含字符
 * 2) 从根节点到某一节点，路径经过的字符连接起来，为该节点对应的字符串
 * 3) 每个节点的所有子节点包含的字符都不相同 */
const int kind = 26;
char str[N][25];
struct Node {
	int num;
	bool tail;
	Node* next[kind];
	Node() : num(1), tail(false) {
		memset(next, 0, sizeof(next));
	}
};
void insert(Node* root, char *s) {
	Node* p = root;
	int i = 0, index;
	while(s[i]) {
		index = s[i] - 'a';
		if(p->next[index] == NULL) {
			p->next[index] = new Node();
		}
		else {
			p->next[index]->num++;
		}
		p = p->next[index];
		++i;
	}
	p->tail = true;
}
m_并查集 (必会算法之一)
using namespace std;
const int N = 10005;
struct Node {
	int par, sum;
};
int SUM;
Node p[2*N+5];
void makeSet(int n) {
	for(int i = 0; i <= 2*n; i++) {
		p[i].par = i;
		p[i].sum = 1;
	}
	SUM = 1;
}
int find(int x) {
	if(x == p[x].par) return x;
	return p[x].par = find(p[x].par);
}
void union1(int x, int y) {
	int fx = find(x);
	int fy = find(y);
	if(fx != fy) {
		p[fx].par = fy;
		p[fy].sum += p[fx].sum;
	}
	if(p[fy].sum > SUM) {
		SUM = p[fy].sum;
	}
}
m_KMP
const int N = 1005;
const int M = 105;
char s[N];
char t[M], next[M];
void get_next(int len) {
	int i = 0, j = -1;
	next[0] = -1;
	while(i < len - 1) {
		if(j == -1 || t[i] == t[j]) {
			i++, j++, next[i] = j;
		}
		else j = next[j];
	}
}
int kmp(int sl ,int tl) {
	int i = 0, j = 0;
	while(i < sl && j < tl) {
		if(j == -1 || s[i] == t[j]) {
			i++, j++;
		}
		else j = next[j];
	}
	if(j == tl) return i - j + 1;
	else return -1;
}
m_15 二叉树非递归的 先根遍历和中序遍历   (必会数据结构之一)
void fPre(Node* root) { // 先根遍历  根->左->右
	Node* p = root;
	stack<Node*>  S;
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
void fIno(Node* root) {
	Node* p = root;
	stack<Node*> S;
	while(1) {
		if(p != NULL) {
			S.push(p);
			p = p->lchild;
		}
		else {
			if(S.empty()) return;
			p = S.top(); S.pop();
			cout << p->value <<  ' ';
			p = p->rchild;
		}
	}
}			
m_16 链表的归并，翻转，找环， 查找。
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
/* 16_2 链表的翻转 */
Node* head;   
Node* reverse(Node* head) {  
    if(head == NULL) return NULL;  
    Node* tmp = head;  
    Node*p;  
    while(tmp->next != NULL) {  
        p = tmp->next;  
        tmp->next = p->next;  
        p->next = head;  
        head = p;  
    }  
    return head;  
}  
 /* 16_3 查找链表的第 k 个节点 [过]
  * 16_4 O(1) 时间删除链表节点
  * 16_5 在单链表末尾添加一个节点 (陷阱 ： 链表为空)
  * 16_6 删除链表中第一个指定值的节点。(健壮性)
  * 16_7 复杂链表的复制。 空间-O(1) 时间O(n)
  *   answer : (1), 对原链表任意节点N，创建N', 将其放在 N 之后。
  *            (2), N 的Sib指向 S，复制 N'的Sib, 指向 S 的下一个节点S’。
  *            (3), 拆开链表，奇数位一个链表，偶数位一个。
  */ 16_8 linkStack
          struct Node { char value; Node* next; };
bool push(Node* top, char x) {
	Node* tmp = new Node();
	if(tmp == NULL) return false;
	tmp->value = x;
	tmp->next = top->next; // 插入一个节点so easy!
	top->next = tmp;
	return true;
}
bool pop(Node* top) {
	Node* tmp = top->next;
	if(tmp == NULL) return false;
	top->next = tmp->next;
	delete tmp;
	return true;
}
char get_top(Node* top) {
	if(top->next != NULL) {
		return top->next->value;
	}
	return '#';
}
/* 17 数组的一些题目
* 17_01 找最小的 k 个元素 */
const int N = 105;
int a[N] = {4, 5, 1, 6, 2, 7, 3, 8};
int part (int *a, int left, int right) {
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
void set_k(int *input, int n, int k) { 
	if(input == NULL || k > n || k <= 0 || n <= 0) return;
	int start = 0, end = n  - 1;
	int index = part(input, start, end);
	while(index != k-1) {
		if(index > k-1) end = index - 1;
		else if(index < k - 1) start = index + 1;
		else break;
		index = part(input, start, end);
	}
}
/* 17_02数组中出现次数超过一半的数字  发帖水王 */
int a[N] = { 1, 2, 3, 2, 2, 2, 5, 4, 2};
int core(int *a, int len) {
	if(a == NULL || len < 1) return -1;
	int result = a[0];
	int times = 1;
	for(int i = 1; i < len; ++i) {
		if(times == 0) {
			result = a[i];
			times = 1;
		}
		else {
			if(a[i] == result) times++;
			else --times;
		}
	}
	return result; // 在数据合法的情况下。
}
/*
 * 17_03 顺时针打印矩阵 （col > start*2 && row > start*2）
 * 17_04 调整数组顺序使奇数位于偶数前面 (bool (*func)(int))
 * 17_05 二维数组中的查找 （剑指）
 * 17_06 旋转数组的最小元素。 [3, 4, 5, 1, 2], [1, 2, 3, 4, 5]
int a[N] = {3, 4, 5, 1, 2};
int get_min(int *a, int len) {
	if(a == NULL || len <= 0) return -1;
	int p1 = 0, p2 = len-1;
	int mid = p1;
	while(a[p1] >= a[p2]) {
		if(p2 - p1 == 1) {mid = p2; break;}
		mid = (p1 + p2) / 2;
		if(a[p1] <= a[mid]) p1 = mid;
		else if(a[p2] >= a[mid]) p2 = mid;
	}
	return mid;
}*/
m_12 涉及 栈，队列的的 3 道题。
/* 01 包含 min 函数的栈 */
template <typename T>
class StackWithMin {
public :
	StackWithMin() {}
	virtual ~StackWithMin() {}
	const size_t size() const;
	void pop();
	void push(const T&);
	T top() const;
	T min();
private :
	deque<T> m_data;
	deque<T> m_min;
};
template <typename T>
const size_t StackWithMin<T>::size() const {
	return m_data.size();
}
template <typename T>
void StackWithMin<T>::pop() {
	assert(!m_data.empty() && !m_min.empty());
	m_data.pop_front();
	m_min.pop_front();
}
template <typename T>
void StackWithMin<T>::push(const T& value) { // core 就是这个函数
	m_data.push_front(value);
	if(m_min.empty() || value < m_min[0]) {
		m_min.push_front(m_min[0]);
	}
	else m_min.push_front(m_min[0]);
}
template <typename T>
T StackWithMin<T>::top() const {
	assert(!m_data.empty() && !m_min.empty());
	return m_data[0];
}
template <typename T>
T StackWithMin<T>::min() {
	assert(!m_data.empty() && !m_min.empty());
	return m_min[0];
}
/* 两个栈实现队列 */
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
T Queue<T>::front() { // core 就是这个函数
	if(!S2.empty()) {
		T tmp = S2.top();
		return tmp;
	}
	else {
		assert(!S1.empty());
		while(!S1.empty()) {
			S2.push(S1.top());
			S1.pop();
		}
		T tmp = S2.top(); S2.pop();
		return tmp;
	}
}
template<typename T>
void Queue<T>::pop() {
	if(!S2.empty()) {
		S2.pop();
	}
	else {
		assert(!S1.empty());
		while(!S1.empty()) {
			S2.push(S1.top());
			S1.pop();
		}
		S2.pop();
	}
}
template<typename T>
void Queue<T>::push(const T& value) {
	S1.push(value);
}
/* 栈的 push pop 序列 */
// 1 2 3 4 5
// 4 3 5 1 2
bool solve(int *pPush, int *pPop, int len) {
	bool flag = false;
	if(pPush != NULL && pPop != NULL && len > 0) {
		int *pNextPush = pPush;
		int *pNextPop = pPop;
		stack<int> S;
		while(pNextPop - pPop < len) {
			while(S.empty() || S.top() != *pNextPop) {
				if(pNextPush - pPush == len) break;
				S.push(*pNextPush);
				++pNextPush;
			}
			if(S.top() != *pNextPop) break;
			S.pop();
			++pNextPop;
		}
		if(pNextPop - pPop == len) flag = true;
	}
	return flag;
}	
m_18 蓄水池原理 随即取样问题 [baidu]
要求从 N 个元素中随机的抽取 k 个元素，其中 N 无法确定。
for i=k+1 to N
	M = random（1, i);
	if(M < k) swap(Mth value, ith value);
m_14 poj 3903 最长上升子序列 dp n^2 vs （二分 nlogn）
 F[t] 表示从 1 到t这一段中以 t 结尾的最长上升子序列的长度
 F[t] = max(1, F[j]+1); (j = 1, 2, ..., t-1. 且 A[j] < A[t])
此方程让我想到可以对比连续子数组的最大和。  dp: F[i] = max(a[i], F[i-1]+a[i]);
面试题06 : 寻找丑数 
 -answer- 不在非丑数上浪费时间，只看在丑数上的方法！ pun[0] = 1; ind = 1; p2 = p3 = p5 = pun; while;
面试题07 : 圆圈中最后剩下的数字。
int cirnm(int n, int m) {
	if(n < 1 || m < 1) return -1;
	int i = 0, count = 0; 
	list<int> L;
	for(int i = 0; i < n; i++) L.push_back(i);
	list<int>::iterator lcur = L.begin(), next;
	while(!L.empty()) {
		for(int i = 1; i < m; ++i) {
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
面试题08 : 从 1 到 n 的整数中 1 出现的次数.( f(10^n-1) = n * 10^(n-1) )
面试题09 : 数值的整数次方。 a^n = a^n/2 * a^n/2;（偶数）	奇数的话再乘以 a.（递归神技）
面试题10 : 打印 1 到最大的 n位数。
面试题11 : 替换空格。(从后向先，先计算出长度！) ' ' -> %20
面试题12 : 无符号整数二进制中 1 的个数。 (n-1)&n   O(1的个数)。
面试题13 : 求 1+2+3+...+n, 要求不能使用，乘除法，for,while,if,else,switch,case。(n>1)&&(i=res(n-1)+n);
面试题14 : 不能用 +, -, *, %, /,实现int add(int num1, int num2); （^, &<<1）
面试题15 : 子数组的最大乘积。计算任意(N-1)个数的组合乘积最大的一组。P(0, 正，负)
answer10 :  总结 ： 我们要活学活用！
void res(char *number, int len, int index) {
	if(index == len-1) {
		printNum(number, len); return;
	}
	for(int i = 0; i < 10; i++) {
		number[index+1] = '0'+1;
		res(number, len, index+1);
	}
}
void solve(int n) {
	if(n <= 0) return;
	char *str = new char[n+1];
	str[n] = '\0';
	for(int i = 0; i < 10; i++) {
		str[0] = '0' + i;
		res(str, n, 0);
	}
	delete []str;
}				
m_15 只能在 heap 中, stack 中分配空间的类。写出 string 的复制控制。
class HeapOnly {
public :
	HeapOnly() {}
	void destroy() const {
		delete this;
	}
private :
	~HeapOnly() {}
};
class StackOnly {
public :
	StackOnly() {}
	~StackOnly() {}
private :
	static void* operator new(size_t size);
};
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
        int len = static_cast<</span>int> (strlen(rhs.m_data));  
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
        int len = static_cast<</span>int> (strlen(rhs.m_data));  
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
/* m_21 c++ 单件模式的实现 */
class L {  
public :  
    static L* instance() {  
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
    };  
    static L* pi;  
private :  
    L() {};   // 防止产生实例          
    L(const L&); // 防止复制构造另一个实例  
    L& operator= (const L&); // 防止赋值构造出另一个实例  
};  
L* L::pi = NULL;  
  
int main() {  
    L::pi = L::instance();  
    return 0;  
}  
/* m_22 矩阵相乘 */
a[m][n] * b[n][m] = c[m][m];
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
            }  
        }  
    }  
    for(int i = 0; i < m; ++i) {  
        for(int j = 0; j < m; ++j) {  
            cout << c[i][j] << ' ';  
        }  
        cout << endl;  
    }  
}  
int main() {  
    solve(2, 3);  
    return 0;  
}  
Output :
1400 2600 
3200 6200
[算法] 数组 - 相关的一些经典题目 ：一网打尽
1.1   如何用递归实现数组求和。   分析 ： return n == 0 ? 0 : getSum(a, n-1) + a[n-1];
1.2   如何用一个for循环打印出一个二维数组 (过)
1.4   如何用递归算法判断一个数组是否是递增   分析 ： return (a[n-1] > a[n-2]) && isIncrease(a, n-1);
1.5   如何分别使用递归与非递归实现二分查找算法(过)
1.6   如何在排序数组中，找出给定数字出现的次数(二分好的题目) int solve(int *a, int len, int num, bool isLeft);
1.7   如何计算两个有序整型数组的交集
   情况1 : 两数组长度相差不悬殊，解决方法：哈希其中一数组，在遍历另一个数组。 
   情况2 : 相差悬殊。小的数组中的数字，分别在长数组上二分查找。
1.8   如何找出数组中重复次数最多的数  ( 非常好的题目，map，遍历，if(++m[a[i]] >= m[val]) val = a[i] )
1.9   如何在O(n)的时间复杂度内找出数组中出现次数超过了一半的数(过，但是非常好的题目！，有一个count)
1.11 如何判断一个数组中的数值是否连续相邻 (未描述完整，解决 ： 非0最大 - 非0最小 <= 4)
1.12 如何找出数组中出现奇数次的元素 (过)
1.13 如何找出数列中符合条件的数对的个数 (排序，两边夹-O(nlogn) /  计数排序-O(n))
1.14 如何寻找出数列中缺失的数 (异或 O(2*n)  /  (n+1)*n / 2 - sum O(n))
1.15 如何判定数组是否存在重复元素 a[n],元素取值范围也是 1~n  
   (解析 ： bitmap, 费空间 /  茂旭神之法，死磕到底O(2n)
1.16 如何重新排列数组使得数组左边为奇数，右边为偶数 (类似于快排的思想)
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
1.20 如何将数组的后面m个数移动为前面m个数。解析 ： 不就是左旋转！ OK！
1.21 如何计算出序列的前n项数据
 描述 ： 正整数序列Q中的每个元素都至少能被正整数 a 和 b 中的一个整除。 求 序列前 n 项数据？
    解析 ： 类似于归并的思想
     for(int k = 0; k < N; k++) {tmpA = a*i; tmpB = b*j; if(tmpA <= tmpB) {Q[k] = tmpA; i++;} else}
1.23 如何判断一个整数x是否可以表示成n（n≥2）个连续正整数的和
  解析 ：
     x = m + (m+1) + (m+2) + ... + (m+n-1), 推出 : m = (2*x / n - n + 1) / 2; m >= 1;
    也就是判断 (2*x / n - n + 1) 是不是偶数的问题。
    tmp = m; if(tmp == (int)tmp) 就 OK！
poj 1274 The Perfect Stall (匈牙利入门) .
匈牙利算法入门基于 dfs 实现
#include <iostream>  
#include <cstdio>  
#include <cstring>  
using namespace std;  
const int N = 205;  
bool map[N][N];  
int mat[N];  
bool vis[N];  
int n, m;  
bool dfs(int u) {  
    for(int i = 1; i <= m; i++) {  
        if(map[u][i] && !vis[i]) {  
            vis[i] = true;  
            if(mat[i] == 0 || dfs(mat[i])) {  
                mat[i] = u;  
                return true;  
            }  
        }  
    }  
    return false;  
}  
int maxMatch() {  
    memset(mat, 0, sizeof(mat));  
    int count = 0;  
    for(int i = 1; i <= n; i++) {  
        memset(vis, 0, sizeof(vis));  
        if(dfs(i)) count++;  
    }  
    return count;  
}  
int main() {  
    while(scanf("%d%d", &n, &m) == 2) {  
        memset(map, 0, sizeof(map));  
        int ca, j;  
        for(int i = 1; i <= n; ++i) {  
            scanf("%d", &ca);  
            while(ca--) {  
                scanf("%d", &j);  
                map[i][j] = 1;  
            }  
        }  
        printf("%d\n", maxMatch());  
    }  
    return 0;  
}  
[算法] 链表 - 经典的一些题目 - (小网打尽) 
链表
2.5 如何找出单链表中的倒数第k个元素(过)
2.6 如何实现单链表反转 (很好的题目，三指针法！)
2.7 如何从尾到头输出单链表(过)
2.9 如何进行单链表排序(很好 ： 归并排序法)
2.10 如何实现单链表交换任意两个元素（不包括表头）(非常好的题目)
2.11 如何检测一个较大的单链表是否有环
2.12 如何判断两个单链表（无环）是否交叉
2.13 如何删除单链表中的重复结点
2.15 什么是循环链表
2.16 如何实现双向链表的插入、删除操作
2.18 如何删除结点的前驱结点
2.19 如何实现双向循环链表的删除与插入操作 343
2.20 如何在不知道头指针的情况下将结点删除 344

