#include <iostream> // 水仙花
#include <cstdio>

int main() {
    int a, b;
    while (std::cin >> a >> b) {
        int cnt = 0;
        for (int i = a; i <= b; i++) {
            int k1 = i % 10;
            int k2 = i / 10 % 10;
            int k3 = i / 100;
            if (k1*k1*k1 + k2*k2*k2 + k3*k3*k3 == i) {
                if (cnt == 0) {
                    printf("%d", i);
                    cnt++;
                }
                else {
                    printf(" %d", i);
                }
            }
        }
        if (cnt == 0) {
            printf("no");
        }
        printf("\n");
    }
    return 0;
}
                    

                
