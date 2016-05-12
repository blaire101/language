#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>

#define BUG puts("here!!!");
using namespace std;
const int N = 205;
struct Node {
    int k1, k2;
    int steps;
    int op;
    int pre;
};
bool vis[N][N];
string str[10] = { "FILL(1)", "DROP(1)", "FILL(2)", "DROP(2)", "POUR(1,2)", "POUR(2,1)" }; 
Node cur, nex;
Node que[N*N];
int A, B, C;
void dfs(Node pn) {
    if(pn.pre != 0) {
	dfs(que[pn.pre]);
    }
    cout << str[pn.op] << endl;
}
void bfs(int v1, int v2) {
    cur.k1 = v1; cur.k2 = v2; cur.steps = 0; cur.pre = 0;
    que[0] = cur;
    vis[0][0] = 1;
    int head = 0, tail = 1;
    while(head < tail) {
	Node top = que[head++];
	if(top.k1 == C || top.k2 == C) {
            cout << top.steps << endl;
	    if(C != 0) dfs(top);
	    return;
	}
	int yi = 0;
	for(int i = 0; i < 6; i++) {
	    switch(i) {
		case 0 : nex.k1 = A; nex.k2 = top.k2; break;
		case 1 : nex.k1 = 0; nex.k2 = top.k2; break;
		case 2 : nex.k1 = top.k1; nex.k2 = B; break;
		case 3 : nex.k2 = top.k1; nex.k2 = 0; break;
		case 4 : 
		    yi = top.k1 + top.k2 - B;
		    if(yi > 0) nex.k1 = yi, nex.k2 = B;
		    else nex.k1 = 0, nex.k2 = B + yi;
		    break;
		case 5 :
		    yi = top.k1 + top.k2 - A;
		    if(yi > 0) nex.k1 = A, nex.k2 = yi;
	   	    else nex.k1 = A + yi, nex.k2 = 0;
			break;
            }
            nex.op = i;
	    nex.steps = top.steps + 1;
	    nex.pre = head - 1;
            if(!vis[nex.k1][nex.k2]) {
		que[tail++] = nex;
		vis[nex.k1][nex.k2] = true;
	    }
        }
    }
    printf("impossible\n");
}
int main() {
    while(scanf("%d%d%d", &A, &B, &C) == 3) {
	memset(vis, 0, sizeof(vis));
	bfs(0, 0);
    }
    return 0;
}
