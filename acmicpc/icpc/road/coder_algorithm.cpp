#include <iostream>
using namespace std;
const int N = 105;
/* 01_KMP  I */
// -1 0  0  0  1  2  3  4  5  0
// a  b  c  a  b  c  a  b  d  a
// -1 0  0  -1 0  0 -1  0  5  -1
int next[M];
void get_next(int len) {
    int k = -1, j = 0;
    next[0] = -1;
    while (j < len) {
        if (k == -1 || sz[k] == sz[j]) {
            k++; j++;
            if (sz[k] == sz[j]) next[j] = next[k]; // 我已经深刻理解优化与不优化的优缺点,优化的很好，next 数组，避免不必要的匹配比较，不仅仅记录的是，之前的前缀和后缀的信息，还记录了当前位置的信息。
            else next[j] = k;
        }
        else {
            k = next[k]; 
        }
    }
}
/* 02_并查集 */
const int N = 10005;
struct Node {
    int par, sum;
};
int SUM;
Node p[2*N+5];
void makeSet(int n) {
    for (int i = 0; i <= 2*n; i++) {
        p[i].par = i;
        p[i].sum = 1;
    }
    SUM = 1;
}
int find(int x) {
    if (x == p[x].par) return x;
    return p[x].par = find(p[x].par);
}
void union1(int x, int y) {
    int fx = find(x);
    int fy = find(y);
    if (fx != fy) {
        p[fx].par = fy;
        p[fy].sum += p[fx].sum;
    }
    if (p[fy].sum > SUM) {
        SUM = p[fy].sum;
    }
}
/* 03_字典树 */
/* 1) 根节点不包含字符
 * 2) 从根节点到某一节点，路径经过的字符连接起来，为该节点对应的字符串
 * 3) 每个节点的所有子节点包含的字符都不相同 */
struct Node {
    int num;
    bool terminal;
    Node* next[26];
    Node() : num(1), terminal(false) {
        memset(next, 0, sizeof(next));
    }
};
string s1;
void insert(Node* root, string s1) {
    Node* p = root;
    int index;
    int len = s1.length(); /// band
    for(int i = 0; i < len; i++) { /// band
        index = s1[i] - 'a';
        if(p->next[index] == NULL) {
            p->next[index] = new Node();
        }
        else {
            p->next[index]->num++;
        }
        p = p->next[index];
    }
    p->terminal = true;
}
/* 04_六种排序的手写 input ： 8, 5, 4, 9, 2, 3, 6  */
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
    if(left < right) { // exit. good idea!
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
void mergeSort(int a[], int l, int r) { //  8, 5, 4, 9, 2, 3, 6
    if(l >= r) return;   // exit.
    int mid = (l+r) / 2; // overflow  <->  l + (r-l)/2
    mergeSort(a, l, mid);
    mergeSort(a, mid+1, r);  
    int *arr = new int[r-l+1];  
    int k = 0;
    int i = l, j = mid + 1;
    while(i <= mid && j <= r) {  
        if(a[i] <= a[j]) {
            arr[k++] = a[i++]; 
        }
        else {
            arr[k++] = a[j++]; // ans += (mid-i+1);  
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
/* 05_0, 1背包 */
dp[i, j] = MAX(dp[i-1, j], dp[i-1, j-w[i]] + v[i]);
// 递归代码
int f(int i, int j) { // n, m  <->  3, 50
    if(i == 0 || j == 0) return 0;
    if(j >= w[i]) {
        return max(f(i-1, j), f(i-1, j-w[i]) + v[i]);
    }
    else return f(i-1, j);
}
// 二维 dp 代码
// n 物品个数， m 背包容量
for(int i = 1; i <= n; i++) {
    for(int j = 1; j <= m; j++) {
        if(j >= w[i]) {
            bag[i][j] = max(bag[i-1][j], bag[i-1][j-w[i]]+v[i]);
        }
        else bag[i][j] = bag[i-1][j];
    }
}
// 一维 DP 代码， 优化来空间
for(int i = 1; i <= n; i++) {
    for(int k = m; k >= w[i]; k--) {
        bag[k] = max(bag[k], bag[k-w[i]] + v[i]);
    }
}
/* 06_线段树 */
#define MID(x, y) (x + ((y-x) >> 1))
using namespace std;
const int N = 50005;
int r[N];
struct node {
    int a, b;
    int sum;
}t[4*N];
int SUM = 0;
void makeTree(int x, int y, int num) {
    t[num].a = x;
    t[num].b = y;
    if(x == y) t[num].sum = r[y]; // exit
    else {
        int mid = MID(x, y);
        makeTree(x, mid, 2*num);
        makeTree(mid+1, y, 2*num+1);
        t[num].sum = t[2*num].sum + t[2*num+1].sum;
    }
}
void add(int x, int ren, int num) {
    if(x == t[num].a && t[num].b == x) { // terminal interval
        t[num].sum += ren;
        return;
    }
    if(x <= MID(t[num].a, t[num].b)) {
        add(x, ren, 2*num);
    }
    else add(x, ren, 2*num+1);
    t[num].sum = t[2*num].sum + t[2*num+1].sum;  //  harmony and unity
}
void query(int x, int y, int num) {
    if(x <= t[num].a && t[num].b <= y) {
        SUM += t[num].sum;
    }
    else {
        int mid = MID(t[num].a, t[num].b);
        if(x <= mid) query(x, y, 2*num);
        if(y > mid) query(x, y, 2*num+1);
    }
}
void add(int x, int y, int d, int num) {
    if(x <= t[num].a && t[num].b <= y) { // terminal interval
        t[num].sum += (t[num].b - t[num].a + 1) * d;
        t[num].kind += d;
    }
    else {
        if(t[num].kind != 0) {
            t[2*num].kind += t[num].kind;
            t[2*num+1].kind += t[num].kind;
            t[2*num].sum += (t[2*num].b - t[2*num].a + 1) * t[num].kind;
            t[2*num+1].sum += (t[2*num+1].b - t[2*num+1].a + 1) * t[num].kind;
            t[num].kind = 0; 
        }
        int mid = getMid(t[num].a, t[num].b);
        if(x <= mid) {
            add(x, y, d, 2*num);
        }
        if(y > mid) {
            add(x, y, d, 2*num+1);
        }
        t[num].sum = t[2*num].sum + t[2*num+1].sum;
    }
}
/* 07_Hungary Algorithm */
vector<int> p[N];
int pre[N];
bool vis[N];
int n, m;
bool dfs(int u) {
    for(unsigned i = 0; i < p[u].size(); i++) {
        int to = p[u][i];
        if(!vis[to]) {
            vis[to] = true;
            if(pre[to] == -1 || dfs(pre[to])) {
                pre[to] = u;
                return true;
            }
        }
    }
    return false;
}
int maxMatch() {
    memset(pre, 255, sizeof(pre));
    int cnt = 0;
    for(int i = 1; i <= n; i++) {
        memset(vis, false, sizeof(vis));
        if(dfs(i)) {
            cnt++;
        }
    }
    return cnt;
}
/* 07_prim 与 kruskal */
const int INF = 0x7fffffff;
int cost[N][N];
int lowc[N];
bool vis[N];
int n, m;
void prim(int p) {
    memset(vis, false, sizeof(vis));
    memset(lowc, 0, sizeof(lowc));
    for(int i = 1; i <= p; i++) {
        if(cost[1][i] != -1) {
            lowc[i] = cost[1][i];
        }
        else lowc[i] = INF;
    }
    vis[1] = true;
    int sum = 0;
    for(int i = 1; i < p; i++) {
        int mmin = INF, c = 0;
        for(int j = 1; j <= p; j++) {
            if(!vis[j] && lowc[j] < mmin) {
                mmin = lowc[j];
                c = j;
            }
        }
        sum += mini;
        vis[c] = true;
        for(int j = 1; j <= p; j++) {
            if(!vis[j] && cost[c][j] != -1 && cost[c][j] + mmin > 0 && cost[c][j] + mmin < lowc[j]) {
                lowc[j] = cost[c][j];
            }
        }
    }
    cout << sum << endl;
}
// kruskal
int pre[N];
int n, m;
struct Edge {
    int u, v;
    int w;
}e[N];
bool cmp(const Edge a, const Edge b) {
    return a.w < b.w;
}
void make_set(int n) {
    for(int i = 0; i <= n; i++)
        pre[i] = i;
}
int find_set(int a) {
    if(pre[a] == a) return a;
    return pre[a] = find_set(pre[a]);
}
void kruskal() {
    int sum = 0;
    sort(e, e + m, cmp);
    make_set(n);
    for (int i = 0, fu, fv, cnt_e; i < m; i++) {
        fu = find_set(e[i].u);
        fv = find_set(e[i].v);
        if (fu != fv) {
            sum += e[i].w;
            cnt_e++;
            if (cnt_e == n-1) break;
            pre[fv] = fu;
        }
    }
    cout << sum << endl;
}
/* 08_dijstra / bellman / SPFA / floyd */
/* dijstra */
int data[M][M]; // init INF
int lowc[M];
int vis[M];
int n, m;
void djst(int p) {
    int i, j;
    for(i = 1; i <= n; i++) {
        vis[i] = 0;
        lowc[i] = data[p][i];
    }
    vis[p] = 1;
    for(i = 1; i <= n-1; i++) {
        int minc = INF, c = 0, lk;
        for(j = 1; j <= n; j++) {
            if(vis[j] == 0 && lowc[j] < minc) {
                minc = lowc[j];
                c = j;
            }
        }
        if(c == 1) break;
        vis[c] = 1;
        for(j = 1; j <= n; j++) {
            if(vis[j] == 0 && data[c][j] + minc > 0 && data[c][j] + minc < lowc[j]) {
                lowc[j] = data[c][j] + minc;
            }
        }
    }
    cout << lowc[1] << endl;
}
/* Bellman */
#define INF ((long long)(1))<<62
#define N 301
using namespace std;
struct edge{
   int u;
   int v;
   long long w; // 注意
}e[N*N];
int m, n;
long long d[1005];
bool bellman_ford(int s, int di) {
    int i, j;
     for(i = 1; i < n; i++) {
        d[i] = INF;
     }
     d[s] = 0;
     for(i = 1; i <= n-1; i++) {
        for(j = 1; j <= m; j++) {
            if(d[e[j].u] != INF && d[e[j].u]+e[j].w < d[e[j].v])    // 对边进行操作 、松弛
                d[e[j].v] = d[e[j].u] + e[j].w;
        }
     }
     for(j = 1; j <= m; j++) {
        if(d[e[j].u] != INF && (d[e[j].v] > d[e[j].u]+e[j].w))    // 很理解
            d[e[j].v] = -INF;
     }
     if(d[di] == INF || d[di] == -INF) return false;
     return true;
}
/** SPFA **/
const int INF = 0x7fffffff;
const int N = 5501;
struct edge {
    int to;
    int w;
};
vector<edge> p[N]; // vector 实现邻接表
int d[N];
bool inque[N];     // 记录顶点是否在队列中，SPFA算法可以入队列多次
int cnt[N];        // 记录顶点入队的次数
int n, m, q;
bool SPFA(int s) {
    queue<int> Q;
    while(!Q.empty()) Q.pop();
    int i;
    for(i = 0; i <= n; i++) {
        d[i] = INF;
    }
    d[s] = 0;      // 源点的距离为 0
    memset(inque, 0, sizeof(inque));
    memset(cnt, 0, sizeof(cnt));
    Q.push(s);
    inque[s] = true;
    cnt[s]++;      // 源点入队列的次数增加
    while(!Q.empty()) {
        int t = Q.front();
        Q.pop();
        inque[t] = false;
        for(i = 0; i < p[t].size(); i++) {
            int to = p[t][i].to;
            if(d[t] < INF && d[to] > d[t] + p[t][i].w) {
                d[to] = d[t] + p[t][i].w;
                cnt[to]++;
                if(cnt[to] >= n) {  //当一个点入队的次数>=n时就证明出现了负环
                    return false;
                }
                if(!inque[to]) {
                    Q.push(to);
                    inque[to] = true;
                }
            }
        }
    }
    return true;
}
/* 09_korasaju / tarjan Algorithm */
const int N = 10005;
int n, m;
vector<int> p[N];   // 原图
vector<int> rp[N];  // 转置图
int vis[N];    // 遍历时标记该结点是否访问过
int f[N];      // 记录结束时间
int member[N]; // 缩点之后的成员（即某个强连通包含点个数）
int con[N];    // 表示该点i所属的强连通编号
int tim = 0;   // 变量
int ko;
void dfs1(int x) {
    vis[x] = 1;
    int len = rp[x].size();
    for(int i = 0; i < len; i++) {
        if(!vis[rp[x][i]]) dfs1(rp[x][i]);
    }
    f[++tim] = x; // tim 是结束时间
}
void DFS1() { //DFS1()就是遍历一下要各个点的结束时间
    int i;
    for(i = 1; i <= n; i++) {
        if(!vis[i]) dfs1(i);
    }
    memset(vis, 0, sizeof(vis));
}
// -------------------------------
//按f[i]从大到小的顺序深搜转置图，
//得出各点所属的强连通分量，
//和各强连通分量包含的点的数量。---------
void dfs2(int x) {
    vis[x] = 1;
    con[x] = ko;
    member[ko]++;
    int len = p[x].size();
    for(int i = 0; i < len; i++) {
        if(!vis[p[x][i]]) dfs2(p[x][i]);
    }
}
void DFS2() {
    ko = 0;
    for(int i = n; i > 0; i--) {
        if(!vis[f[i]]) {
            ko++; // 记录强连通分量的个数
            dfs2(f[i]);
        }
    }
    memset(vis, 0, sizeof(vis));
}
void solve() {
    int i, j;
    for(i = 1; i <= n; i++) {
        int len = p[i].size();
        for(j = 0; j < len; j++) {
            int t = p[i][j];
            if(con[i] != con[t]) {
                vis[con[i]] = 1;
                break;
            }
        }
    }
    int ans, cnt = 0;
    for(i = 1; i <= ko; i++) {
        if(vis[i] == 0) {
            cnt++;
            ans = member[i];
        }

    }
    if(cnt == 1) cout << ans << endl;
    else cout << 0 << endl;
}
int main() {
    int i, j, a, b;
    cin >> n >> m;      // 如果是多组数据，记得要 clear  vector容器
    for(i = 0; i < m; i++) {
        cin >> a >> b;
        p[a].push_back(b);
        rp[b].push_back(a);
    }
    DFS1();
    DFS2();
    solve();
    return 0;
}
/* tarjan Algorithm  */
const int N = 101;
int n, map[N][N], in[N], out[N];
int dfn[N], low[N], index, cnt, belong[N];
bool instack[N];
stack<int> Q;
void tarjan(int x) {
    dfn[x] = low[x] = index++;
    Q.push(x);
    instack[x] = true; // 标记在栈内
    for(int i = 1; i <= n; i++) {
        if(!map[x][i]) continue;
        if(!dfn[i]) { // map[x][i]有边，并且没有被访问
            tarjan(i);
            low[x] = min(low[x], low[i]);
        }
        else if(instack[i]) {
            low[x] = min(low[x], dfn[i]);
        }
    }
    if(low[x] == dfn[x]) { 
        cnt++; //记录强连通的个数
        while(1) {
            int tmp = Q.top();
            Q.pop();
            belong[tmp] = cnt;
            instack[tmp] = false;
            if(tmp == x) break;
        }
    }
}
void output() {
    int A = 0, B = 0;
    for(int i = 1; i <= n; i++) {
        for(int j = 1; j <= n; j++) {
            if(!map[i][j]) continue;
            if(belong[i] != belong[j]) {
                in[belong[j]]++;
                out[belong[i]]++;
            }
        }
    }
    for(int i = 1; i <= cnt; i++) {
        if(!in[i]) A++;
        if(!out[i]) B++;
    }
    if(cnt == 1) cout << 1 << endl << 0 << endl;
    else {
        printf("%d\n%d\n", A, max(A, B));
    }
}
int main() {
    scanf("%d", &n);
    index = 1;
    cnt = 0;
    for(int i = 1; i <= n; i++) {
        while(1) {
            int tmp;
            scanf("%d", &tmp);
            if(tmp == 0) break;
            map[i][tmp] = 1;
        }
    }
    for(int i = 1; i <= n; i++) {
        if(!dfn[i]) tarjan(i);
    }
    output();
    return 0;
}
