#include <iostream>
#include <cstdio>

int get_ans(int x) {
    if (x % 10 == 0) x = x / 10;
    else x = x / 10 + 1;
    return x;
}

int main() {
    int T, n;
    std::cin >> T;
    while (T--) {
        scanf("%d", &n);
        int a, b, c;
        a = n / 2;
        b = (n - a) * 2 / 3; // is n - a, is not a
        c = n - a - b;
        printf("%d\n", get_ans(a) + get_ans(b) + get_ans(c));
    }
    return 0;
}
        
        
