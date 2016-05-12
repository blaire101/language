#include <iostream> // 前进 - 剑招杀死蝙蝠

int main() {
    int T, n;
    std::cin >> T;
    while (T--) {
        std::cin >> n;
        int k = 0;
        while (n) {
            if ((n & 1) == 1) {
                k++;
            }
            n = n / 2;
        }
        std::cout << k << std::endl;
    }
    return 0;
}
