#include <iostream>
#include <cstdio>

const int N = 52;

long long fib[N];

int main() {
    fib[1] = 1;
    fib[2] = 2;
    for (int i = 3; i <= 50; i++) {
        fib[i] = fib[i-1] + fib[i-2];
    }
    int T;
    std::cin >> T;
    while (T--) {
        int a, b;
        std::cin >> a >> b;
        //printf("%I64d\n", fib[b-a]); windows
        //printf("lld\n", fib[b-a]); Linux
        std::cout << fib[b-a] << std::endl;
    }
    return 0;
}
