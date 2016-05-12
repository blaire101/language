/*good topic*/
#include <iostream>
#include <cstdio>
#include <string>
#include <cstring>
#include <algorithm>
#include <cmath>
using namespace std;
const int M = 1000005;
const int N = 10005;
char s[M], t[M];
int next[N];
void get_next(int len) {
    int i = 0, k = -1;
    next[0] = -1;
    while(i < len) {
        if(k == -1 || t[i] == t[k]) {
            i++, k++;
            if(t[i] == t[k]) next[i] = next[k];
            else next[i] = k;
        }
        else {
            k = next[k];
        }
    }
}
int kmp(int sl, int tl) {
    int i= 0, j = 0;
    int cnt = 0;
    while(i < sl && j < tl) {
        if(j == -1 || s[i] == t[j]) {
            i++, j++;
        }
        else {
            j = next[j];
        }
        if(j == tl) {
            cnt++;
            j = next[j];
        }
    }
    return cnt;
}
int main() {
    int n, tl, sl;
    scanf("%d", &n);
    while(n--) {
        scanf("%s", t);
        scanf("%s", s);
        tl = strlen(t);
        sl = strlen(s);
        get_next(tl);
        printf("%d\n", kmp(sl, tl));
    }
    return 0;
}
