#include <iostream>
#include <cstdio>
#include <algorithm>
using namespace std;
const int N = 1005;
char str[N];
void reverse(int p1, int p2) {
    while (p1 < p2) {
        char tmp = str[p1];
        str[p1] = str[p2];
        str[p2] = tmp;
        p1++;
        p2--;
    }
}
void solve(char* str) {
    int start = 0, end = 0;
    while (true) {
        if (str[end] == ' ' || str[end] == '\0') {
            reverse(start, end-1);
            if(str[end] == '\0') break;
            start = ++end;
        }
        else if (str[start] == ' ') {
            start++;
            end++;
        }
        else end++;
    }
    cout << str << endl;
}
int main() {
    int T;
    cin >> T;
    getchar(); //度掉回车,因为后面有 gets .
    while(T--) {
        gets(str);
        solve(str);
    }
    return 0;
}
