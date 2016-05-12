#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
const int N = 205;

int n, m;
char map[N][N];
struct Point {
    int x, y;
    int steps;
    friend bool operator < (Point t1, Point t2) {
        return t1.steps > t2.steps;
    }
};
Point start;
int dx[] = {0, 0, 1, -1};
int dy[] = {1, -1, 0, 0};
bool ok(int x, int y) {
    return (x >= 0 && x < m && y >= 0 && y < n && map[x][y] != '#');
}
int bfs() {
    priority_queue<Point> Q;
    Q.push(start);
    Point cur, nex;
    while(!Q.empty()) {
        cur = Q.top(); Q.pop();
        for(int i = 0; i < 4; i++) {
            nex.x = cur.x + dx[i];
            nex.y = cur.y + dy[i];
            if(!ok(nex.x, nex.y)) {
                continue;
            }
            if(map[nex.x][nex.y] == 'r') return cur.steps + 1;
            if(map[nex.x][nex.y] == 'x') {
                nex.steps = cur.steps + 2;
            }
            else nex.steps = cur.steps + 1;
            map[nex.x][nex.y] = '#';
            Q.push(nex);
        }
    }
    return -1;
}
int main() {
    while(scanf("%d%d", &m, &n) != EOF) {
        for(int i = 0; i < m; i++) {
            scanf("%s", map[i]);
            for(int j = 0; j < n; j++) {
                if(map[i][j] == 'a') {
                    map[i][j] = '#';
                    start.x = i;
                    start.y = j;
                    start.steps = 0;
                }
            }
        }
        int ans = bfs();
        if(ans == -1) printf("Poor ANGEL has to stay in the prison all his life.");
        else printf("%d", ans);
        printf("\n");
    }
    return 0;
}
