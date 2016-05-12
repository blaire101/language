#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;
const int N = 3405;
const int M = 12885;
int bag[M];
int n, m;
int w[N], v[N];
int main() {
    while(scanf("%d%d", &n, &m) == 2) {
        for(int i = 1; i <= n; i++) {
            scanf("%d%d", w+i, v+i);
        }
        memset(bag, 0, sizeof(bag));
        for(int i = 1; i <= n; i++) {
            for(int j = m; j >= w[i]; j--) {
                bag[j] = max(bag[j], bag[j-w[i]] + v[i]);
            }
        }
        printf("%d\n", bag[m]);

    }
    return 0;
}

