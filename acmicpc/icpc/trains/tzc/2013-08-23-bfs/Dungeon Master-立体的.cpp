#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
const int N = 35;

int n, m, q;
char map[N][N][N];
struct Point {
    int x, y, z;
    int steps;
    friend bool operator < (Point t1, Point t2) {
        return t1.steps > t2.steps;
    }
};
int dz[] = {-1, 1, 0, 0, 0, 0};
int dx[] = {0, 0, 0, 0, 1, -1};
int dy[] = {0, 0, 1, -1, 0, 0}; // E, W, S, N

bool ok(int x, int y, int z) {
    return (x >= 0 && x < m && y >= 0 && y < n && z >= 0 && z < q && map[z][x][y] != '#');
}
Point you;
int bfs() {
    priority_queue<Point> Q;
    Q.push(you);
    Point cur, nex;
    while(!Q.empty()) {
        cur = Q.top(); Q.pop();
        for(int i = 0; i < 6; i++) {
            nex.x = cur.x + dx[i];
            nex.y = cur.y + dy[i];
            nex.z = cur.z + dz[i];
            if(!ok(nex.x, nex.y, nex.z)) continue;
            
            nex.steps = cur.steps + 1;
            
            if(map[nex.z][nex.x][nex.y] == 'S') {
                return nex.steps;
            }
            map[nex.z][nex.x][nex.y] = '#';
            Q.push(nex);
        }
    }
    return -1;
}
int main() {
    while(scanf("%d%d%d", &q, &m, &n) != EOF) {
        if(m == 0 && n == 0 && q == 0) break;
        for(int k = 0; k < q; ++k) {
            for(int i = 0; i < m; i++) {
                scanf("%s", map[k][i]);
                for(int j = 0; j < n; j++) {
                    if(map[k][i][j] == 'E') {
                        you.x = i;
                        you.y = j;
                        you.z = k;
                        map[k][i][j] = '#';
                        you.steps = 0;
                    }
                }
            }
        }
        int ans = bfs();
        if(ans == -1)
            printf("Trapped!");
        else printf("Escaped in %d minute(s).", ans);
        printf("\n");
    }
    return 0;
}
