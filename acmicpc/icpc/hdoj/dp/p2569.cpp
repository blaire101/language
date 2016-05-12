#include <iostream>
#include <cstdio>

const int N = 45;
long long dp[N];

int main() {
    dp[1] = 3; dp[2] = 9;
    for (int i = 3; i <= 40; i++) {
        dp[i] = 2 * dp[i-1] + dp[i-2];
    }
    int T, n;
    scanf("%d", &T);
    while (T--) {
        scanf("%d", &n);
        printf("%lld\n", dp[n]);
    }
    return 0;
}
        
        
