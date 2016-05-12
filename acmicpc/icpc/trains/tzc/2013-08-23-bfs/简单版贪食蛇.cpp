#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;
const int N = 105;

int n, m;
char map[N][N];
struct Point {
    int x, y;
    string steps;
};
int dx[] = {0, 0, 1, -1};
int dy[] = {1, -1, 0, 0}; // E, W, S, N
string ds[] = {"E", "W", "S", "N"};
bool ok(int x, int y) {
    return (x >= 0 && x < m && y >= 0 && y < n && map[x][y] != '#');
}
Point you;
string bfs() {
    queue<Point> Q;
    Q.push(you);
    Point cur, nex;
    while(!Q.empty()) {
        cur = Q.front(); Q.pop();
        for(int i = 0; i < 4; i++) {
            nex.x = cur.x + dx[i];
            nex.y = cur.y + dy[i];
            if(!ok(nex.x, nex.y)) continue;
            if(map[nex.x][nex.y] == 'E') {
                return cur.steps + ds[i];
            }
            nex.steps = cur.steps + ds[i];
            map[nex.x][nex.y] = '#';
            Q.push(nex);
        }
    }
    return "-1";
}
int main() {
    while(scanf("%d%d", &m, &n) != EOF) {
        for(int i = 0; i < m; i++) {
            scanf("%s", map[i]);
            for(int j = 0; j < n; j++) {
                if(map[i][j] == 'S') {
                    you.x = i;
                    you.y = j;
                    map[i][j] = '#';
                    you.steps = "";
                }
            }
        }
        string ans = bfs();
        if(ans == "-1")
            printf("Can't eat it!");
        else cout << ans;
        printf("\n");
    }
    return 0;
}
