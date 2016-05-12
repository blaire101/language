#include <iostream>
#include <cstdio>
#include <cstring>
#include <vector>
#include <queue>
#include <algorithm>
#define MID(x, y) (x + ((y-x) >> 1))
using namespace std;
const int N = 50005;
int MAX = 0x80000000;
int MIN = 0x7fffffff;
int r[N];
int n, m;
struct node {
    int a, b;
    int mmax;
    int mmin;
}t[4*N];
void make_tree(int x, int y, int num) {
    t[num].a = x;
    t[num].b = y;
    if(x == y) {
        t[num].mmax = r[y];
        t[num].mmin = r[y];
        return;
    }
    else {
        make_tree(x, MID(x, y), 2*num);
        make_tree(MID(x, y)+1, y, 2*num+1);
        t[num].mmax = max(t[2*num].mmax, t[2*num+1].mmax);
        t[num].mmin = min(t[2*num].mmin, t[2*num+1].mmin);
    }
}
void query(int x, int y, int num) {
    if(x <= t[num].a && t[num].b <= y) {
        MAX = max(MAX, t[num].mmax);
        MIN = min(MIN, t[num].mmin);
    }
    else {
        int mid = MID(t[num].a, t[num].b);
        if(x <= mid) {
            query(x, y, 2*num);
        }
        if(y > mid) {
            query(x, y, 2*num+1);
        }
    }
}
int main() {
    scanf("%d%d", &n, &m);
    for(int i = 1; i <= n; i++) {
        scanf("%d", &r[i]);
    }
    make_tree(1, n, 1);
    while(m--) {
        int a, b;
        scanf("%d%d", &a, &b);
        MAX = 0x80000000;
        MIN = 0x7fffffff;
        query(a, b, 1);
        printf("%d\n", MAX-MIN);
    }
    return 0;
}

