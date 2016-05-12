#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>
#include <vector>
#include <algorithm>
#define BUG puts("here");
using namespace std;
const int N = 205;
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
int main() {
    while(scanf("%d%d", &n, &m) == 2) {
        for(int i = 0; i <= n; i++) {
            p[i].clear();
        }
        int loop, to;
        for(int i = 1; i <= n; i++) {
            scanf("%d", &loop);
            while(loop--) {
                scanf("%d", &to);
                p[i].push_back(to);
            }
        }
        int ans = maxMatch();
        printf("%d\n", ans);
    }
    return 0;
}

