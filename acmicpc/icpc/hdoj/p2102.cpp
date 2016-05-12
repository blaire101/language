#include <iostream>
#include <cstdio>
#include <cstring>
#include <queue>
#define BUG puts("here");
using namespace std;
const int N = 12;
char map[2][N][N];
bool vis[2][N][N];
int n, m, t;
struct Node {
	int op;
	int x, y;
	int steps;
};
Node start, end;
int dir_x[4] = {0, 0, 1, -1};
int dir_y[4] = {1, -1, 0, 0};
Node top, nex;
bool ok(int op, int x, int y) {
	if(x >= 0 && x < n && y >= 0 && y < m && map[op][x][y] != '*') return true;
	return false;
}
void bfs() {
	queue<Node> Q;
	start.op = 0, start.x = 0, start.y = 0, start.steps = 0;
	Q.push(start);
	vis[0][0][0] = true;
	while(!Q.empty()) {
		top = Q.front();
		Q.pop();
		if(map[top.op][top.x][top.y] == '#') {
			if(top.op == 0) top.op = 1;
			else if(top.op == 1) top.op = 0;
		}
		if(map[top.op][top.x][top.y] == 'P') {
			if(top.steps <= t) {
				printf("YES\n");
				return;
			}
			break;
		}
		for(int i = 0; i < 4; i++) {
			nex.x = top.x + dir_x[i];
			nex.y = top.y + dir_y[i];
			nex.op = top.op;
			nex.steps = top.steps + 1;
			if(ok(nex.op, nex.x, nex.y) && !vis[nex.op][nex.x][nex.y]) {
				Q.push(nex);
				vis[nex.op][nex.x][nex.y] = true;
			}
		}
	}
	printf("NO\n");
}
int main() {
	int T;
	cin >> T;
	while(T--) {
		scanf("%d%d%d", &n, &m, &t);
		for(int i = 0; i < n; i++) {
			scanf("%s", map[0][i]);
		}
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				cin >> map[1][i][j];
				if(map[1][i][j] == '#' && (map[0][i][j] == '#' || map[0][i][j] == '*')) {
					map[0][i][j] = '*';
					map[1][i][j] = '*';
				}
				if(map[1][i][j] == '*' && map[0][i][j] == '#') {
					map[0][i][j] = '*';
				}
			}
		}
		memset(vis, 0, sizeof(vis));
		bfs();
	}
	return 0;
}
