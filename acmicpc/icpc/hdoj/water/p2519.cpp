#include <iostream> // Cnm = (n - (m-1)) * (n - (m-2)) * ... * (n - (m-m)) / 1 * 2 * ... * m; 防止溢出才表示成这样
#include <cstdio>

int main() {
    int T, n, m;
    scanf("%d", &T);
    while (T--) {
        scanf("%d%d", &n, &m);
        if (m > n) {
            printf("0\n");
            continue;
        }
        if (n == m || m == 0) {
            printf("1\n");
            continue;
        }
        int sum = 1;
        for (int i = 1; i <= m; i++) {
            sum = sum * (n - (m-i)) / i; // 防止溢出
        }
        printf("%d\n", sum);
    }
    return 0;
}
