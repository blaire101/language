#include <iostream>
#include <cstring>
#include <cstdio>
#include <cmath>
#include <string>
#include <list>
#include <queue>
#include <deque>
#include <vector>
#include <map>
#include <set>
using namespace std; //

int count(int num) {
    int cnt = 0;
    while(num) {
        cnt++;
        num = num & (num-1);
    }
    return cnt;
}
int main() {
    int T, num;
    cin >> T;
    while(T--) {
        scanf("%d", &num);
        cout << count(num) << "\n";
    }
    return 0;
}
        
