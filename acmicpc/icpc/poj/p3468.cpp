#include <iostream>
#include <cstdio>
#include <cstring>
#include <string>
#include <algorithm>

using namespace std;
const int N = 100005;

inline int getMid(int x, int y) {
    return x + (y-x) / 2;
}

struct Node {
    int a, b;
    long long sum;
    long long kind;
}t[4*N];
int n, m;
int a[N];
long long SUM;

void makeTree(int x, int y, int num) {
    t[num].a = x; t[num].b = y;
    t[num].kind = 0;
    if(x == y) { // exit.
        t[num].sum = a[y];
    }
    else {
        int mid = getMid(x, y);
        makeTree(x, mid, 2*num);
        makeTree(mid+1, y, 2*num+1);
        t[num].sum = t[2*num].sum + t[2*num+1].sum;
    }
}
void add(int x, int y, int d, int num) {
    if(x <= t[num].a && t[num].b <= y) { // terminal interval
        t[num].sum += (t[num].b - t[num].a + 1) * d;
        t[num].kind += d;
    }
    else {
        if(t[num].kind != 0) {
            t[2*num].kind += t[num].kind;
            t[2*num+1].kind += t[num].kind;
            t[2*num].sum += (t[2*num].b - t[2*num].a + 1) * t[num].kind;
            t[2*num+1].sum += (t[2*num+1].b - t[2*num+1].a + 1) * t[num].kind;
            t[num].kind = 0; 
        }
        int mid = getMid(t[num].a, t[num].b);
        if(x <= mid) {
            add(x, y, d, 2*num);
        }
        if(y > mid) {
            add(x, y, d, 2*num+1);
        }
        t[num].sum = t[2*num].sum + t[2*num+1].sum;
    }
}
void query(int x, int y, int num) {
    if(x <= t[num].a && t[num].b <= y) {
        SUM += t[num].sum;
    }
    else {
        if(t[num].kind != 0) {
            t[2*num].kind += t[num].kind;
            t[2*num+1].kind += t[num].kind;
            t[2*num].sum += (t[2*num].b - t[2*num].a + 1) * t[num].kind;
            t[2*num+1].sum += (t[2*num+1].b - t[2*num+1].a + 1) * t[num].kind;
            t[num].kind = 0;
        }
        int mid = getMid(t[num].a, t[num].b);
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
        scanf("%d", a+i);
    }
    makeTree(1, n, 1);
    while(m--) {
        int a, b, c;
        char str[3];
        scanf("%s", str);
        if(str[0] == 'C') {
            scanf("%d%d%d", &a, &b, &c);
            add(a, b, c, 1);
        }
        else {
            scanf("%d%d", &a, &b);
            SUM = 0;
            query(a, b, 1);
            cout << SUM << endl;
        }
    }
    return 0;
}
