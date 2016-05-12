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
#define MID(x,y) ( ( x + y ) >> 1 )
#define L(x) ( x << 1 )
#define R(x) ( x << 1 | 1 )
using namespace std;
const int N = 100005;
int f[N+N];
int n, m;
int find(int x) {
    if(f[x] < 0) return x;
    return f[x] = find(f[x]);
}
int main() {
    int loop;
    cin >> loop;
    while(loop--) {
        scanf("%d%d", &n, &m);
        memset(f, 255, sizeof(f));
        while(m--) {
            int a, b;
            char s[3];
            scanf("%s%d%d", s, &a, &b);
            if(s[0] == 'A') {
                if(find(a) != find(b) && find(a) != find(b+n)) {
                    printf("Not sure yet.\n");
                }
                else if(find(a) == find(b)) {
                    printf("In the same gang.\n");
                }
                else printf("In different gangs.\n");
            }
            else {
                if(find(a) != find(b+n)) {
                    f[find(a)] = find(b+n);
                    f[find(b)] = find(a+n);
                }
            }
        }
    }
    return 0;
}
