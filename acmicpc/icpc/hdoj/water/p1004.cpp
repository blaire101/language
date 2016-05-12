#include <iostream>
#include <map>
#include <string>

typedef std::string String;
using std::cin;
using std::cout;
using std::endl;
/*
int main() {
    int n;
    while (std::cin >> n, n) {
        std::map<String, int> M;
        String s, ans;
        int maxv = 0;
        while (n--) {
            std::cin >> s;
            M[s]++;
            if (M[s] > maxv) {
                maxv = M[s];
                ans = s;
            }
        }
        std::cout << ans << endl;
    }
    return 0;
}
*/
int main() {
    int n;
    while (std::cin >> n, n) {
        std::map<String, int> M;
        String s, ans;
        int maxv = 0;
        while (n--) {
            std::cin >> s;
            if (M.count(s) == 0) {
                M[s] = 1;
            }
            else M[s]++;
            if (M[s] > maxv) {
                maxv = M[s];
                ans = s;
            }
        }
        std::cout << ans << endl;
    }
    return 0;
}

    
