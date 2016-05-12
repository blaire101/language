#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
const int N = 10;

int n, m;
int map[N][N];

struct Point {
    int x, y;
    int steps;
};
int dx[] = {1, 1, -1, -1, 2, 2, -2, -2};
int dy[] = {2, -2, 2, -2, 1, -1, 1, -1};

char sa[3], sb[3];
bool ok(int x, int y) {
    return (x >= 1 && x <= 8 && y >= 1 && y <= 8 && map[x][y] != -1);
}
Point you;
int bfs() {
    if(strcmp(sa, sb) == 0) return 0;
    queue<Point> Q;
    you.x = sa[0] - 'a' + 1;
    you.y = (int)sa[1] - 48;
    you.steps = 0;
    map[you.x][you.y] = -1;
    map[sb[0] - 'a' + 1][(int)sb[1] - 48] = 1;
    Q.push(you);
    Point cur, nex;
    while(!Q.empty()) {
        cur = Q.front(); Q.pop();
        for(int i = 0; i < 8; i++) {
            nex.x = cur.x + dx[i];
            nex.y = cur.y + dy[i];
            if(!ok(nex.x, nex.y)) continue;
            nex.steps = cur.steps + 1;
            if(map[nex.x][nex.y] == 1) {
                return nex.steps;
            }
            map[nex.x][nex.y] = -1;
            Q.push(nex);
        }
    }
    return -1;
}
int main() {
    while(scanf("%s%s", sa, sb) != EOF) {
        memset(map, 0, sizeof(map));
        int ans = bfs();
        printf("To get from %s to %s takes %d knight moves.\n", sa, sb, ans);
    }
    return 0;
}
