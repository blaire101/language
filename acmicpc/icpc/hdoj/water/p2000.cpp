#include <iostream>

void swap(char* x, char* y) {
    char temp = *x;
    *x = *y;
    *y = temp;
}
int main() {
    char a, b, c;
    while (std::cin >> a >> b >> c) {
        if (a > b) swap(&a, &b);
        if (b > c) swap(&b, &c);
        if (a > b) swap(&a, &b);
        std::cout << a << ' ' << b << ' ' << c << std::endl;
    }
    return 0;
}
