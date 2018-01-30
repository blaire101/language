#include <iostream>
#include <cstdio>
#include <cstring>
#include <vector>
#include <algorithm>

using namespace std;
const int N = 16; // [3, 15]

int n;
char str[N];
void dfs(int i, int sum, int last) { // 当前位置， 和， 和中最后的加数
    if(i == n+1) {
        if(sum == 0) {
            printf("1");
            for(int j = 2; j <= n; j++) {
                printf("%c%d", str[j], j);
            }
            printf("\n");
        }
        return;
    }
    int tmp = last > 0 ? i : -i;
    str[i] = ' ';
    if(i < 10) {
        dfs(i+1, sum + last*10 + tmp - last, last*10 + tmp);
    }
    else {
        dfs(i+1, sum + last*100 + tmp - last, last*100 + tmp);
    }
    str[i] = '+';
    dfs(i+1, sum + i, i);
    str[i] = '-';
    dfs(i+1, sum - i, -i);
}
int main() {
    /// cout << (int)'-' << ' ' << (int)' ' << ' ' << (int)'+' << endl;
    while(scanf("%d", &n) == 1) {
        dfs(2, 1, 1);
    }
    return 0;
}
    
    
