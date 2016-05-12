#include <iostream>
#include <string>
#include <cstring>
#include <cstdlib>
#include <cstdio>
#include <cmath>
#include <vector>
#include <stack>
#include <deque>
#include <queue>
#include <bitset>
#include <list>
#include <map>
#include <set>
#include <iterator>
#include <algorithm>
#include <functional>
#include <utility>
#include <sstream>
#include <climits>
#include <cassert>
#define MID(x, y) (x + ((y-x) >> 1))
using namespace std;
const int N = 50005;
struct Node {
    int a, b;
    int sum;
}t[4*N];
int SUM = 0;
int r[N];
void makeTree(int x, int y, int num) {
    t[num].a = x;
    t[num].b = y;
    if(x == y) t[num].sum = r[y];
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
    t[num].sum = t[2*num].sum + t[2*num+1].sum;  // harmony and unity
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
int main() {
    int e = 0, T, n;
    cin >> T;
    while(T--) {
        memset(r, 0, sizeof(r));
        memset(t, 0, sizeof(t));
        cout << "Case " << ++e << ':' << endl;
        cin >> n;
        for(int i = 1; i <= n; i++) {
            scanf("%d", r+i);
        }
        makeTree(1, n, 1);
        char str[10];
        int a, b;
        while(scanf("%s", str)) {
            if(str[0] == 'E') {
                break;
            }
            else if(str[0] == 'A') {
                scanf("%d%d", &a, &b);
                add(a, b, 1);
            }
            else if(str[0] == 'S') {
                scanf("%d%d", &a, &b);
                add(a, (-b), 1);
            }
            else if(str[0] == 'Q') {
                scanf("%d%d", &a, &b);
                SUM = 0;
                query(a, b, 1);
                printf("%d\n", SUM);
            }
        }
    }
    return 0;
}
