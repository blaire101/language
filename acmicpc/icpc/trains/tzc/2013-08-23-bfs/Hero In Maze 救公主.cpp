#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
const int N = 25;

int n, m, T;
char map[N][N];
struct Point {
    int x, y;
    int steps;
    friend bool operator < (Point t1, Point t2) {
        return t1.steps > t2.steps;
    }
};
int dx[] = {0, 0, 1, -1};
int dy[] = {1, -1, 0, 0}; // E, W, S, N

bool ok(int x, int y) {
    return (x >= 0 && x < m && y >= 0 && y < n && map[x][y] != '*');
}
Point you;
int bfs() {
    priority_queue<Point> Q;
    Q.push(you);
    Point cur, nex;
    while(!Q.empty()) {
        cur = Q.top(); Q.pop();
        for(int i = 0; i < 4; i++) {
            nex.x = cur.x + dx[i];
            nex.y = cur.y + dy[i];
            if(!ok(nex.x, nex.y)) continue;
            if(map[nex.x][nex.y] == 'P') {
                return cur.steps + 1;
            }
            nex.steps = cur.steps + 1;
            map[nex.x][nex.y] = '*';
            Q.push(nex);
        }
    }
    return -1;
}
int main() {
    while(scanf("%d%d%d", &n, &m, &T) != EOF) {
        if(m == 0 && n == 0 && T == 0) break;
        for(int i = 0; i < m; i++) {
            scanf("%s", map[i]);
            for(int j = 0; j < n; j++) {
                if(map[i][j] == 'S') {
                    you.x = i;
                    you.y = j;
                    map[i][j] = '*';
                    you.steps = 0;
                }
            }
        }
        int ans = bfs();
        if(ans == -1 || ans > T)
            printf("NO");
        else printf("YES");
        printf("\n");
    }
    return 0;
}
