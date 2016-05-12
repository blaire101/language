#include <iostream> // 寻梦 - 字符串放在中间
#include <string>
#include <cstdio>

std::string str1, str2;

int main() {
    int T;
    std::cin >> T;
    while (T--) {
        std::cin >> str1 >> str2;
        unsigned int i;
        for (i = 0; i < str1.length()/2; i++) {
            std::cout << str1[i];
        }
        std::cout << str2;
        for (; i < str1.length(); i++) {
            std::cout << str1[i];
        }
        std::cout << std::endl;
    }
    return 0;
}
    
