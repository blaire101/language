/* 01_KMP
// -1 0  0  0  1  2  3  4  5  0
// a  b  c  a  b  c  a  b  d  a
// -1 0  0  -1 0  0 -1  0  5  -1
int next[M];
void get_next(int len) {
    int k = -1, j = 0;
    next[0] = -1;
    while(j < len) {
        if(k == -1 || sz[k] == sz[j]) {
            k++; j++;
            if(sz[k] == sz[j]) next[j] = next[k]; // 我已经深刻理解优化与不优化的优缺点,优化的最后一位没影响。
            else next[j] = k;
        }
        else {
            k = next[k]; 
        }
    }
}
/* poj 2406 Power Strings 单元串个数 20131006 */
#include <iostream>
#include <cstdio>
#include <string>
#include <cstring>
#include <algorithm>
using namespace std;
const int M = 1000005;

char sz[M];
int next[M];
void get_next(int len) {
    int k = -1, j = 0;
    next[0] = -1;
    while(j < len) {
        if(k == -1 || sz[k] == sz[j]) {
            k++; j++;
            if(sz[k] == sz[j]) next[j] = next[k];
            else next[j] = k;
        }
        else {
            k = next[k]; 
        }
    }
}
int main() {
    while(scanf("%s", sz) && strcmp(sz, ".") != 0) {
        int sz_len = strlen(sz);
        get_next(sz_len);
        if(sz_len % (sz_len - next[sz_len]) == 0) {
            printf("%d\n", sz_len / (sz_len - next[sz_len]));
        }
        else printf("1\n");
    }
    return 0;
}
