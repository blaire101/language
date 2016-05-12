/* 题意： 给你一个变量，变量初始值a，终止值b，每循环一遍加c，问一共循环几遍终止，数大就mod2^k. */
/* 分析 ：转化为 (b-a) % 2^k == (c * x1) % 2^k; */
#include <iostream>
#include <cstdio>

#define LL long long
long long extend_gcd(LL a, LL b, LL& x, LL& y) {
    if (b == 0) {
        x = 1; y = 0;
        return a;
    }
    else {
        LL r = extend_gcd(b, a%b, y, x);
        y -= x*(a/b);
        return r;
    }
}
long long line_mod_equation(long long a, long long b, long long n) {
    long long x, y;
    long long d = extend_gcd(a, n, x, y);
    if (b % d == 0) {
        LL t = n/d; // t为x解的最小正周期
        return ((x * (b/d) % t + t) % t); // 不管b是否大于0, 都得到最小正解。
    }
    return -1;
} // POJ 2115
int main() {
    LL A, B, C, K;
    while(scanf("%lld%lld%lld%lld", &A, &B, &C, &K), A|B|C|K) {
        long long bb = 1;
        for(int i = 0; i < K; ++i) bb *= 2;
        long long ans = line_mod_equation(C, B-A, bb);
        if(ans == -1) {
            printf("FOREVER\n");
        }
        else printf("%lld\n", ans);
    }
    return 0;
}
