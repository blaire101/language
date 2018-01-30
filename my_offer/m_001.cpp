#include <iostream>
#include <vector>
#include <string>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <algorithm>
#define BUG puts("here");
using namespace std;
// abc ： a, b, c, ab, ac, bc, abc;
// dfs + 回溯， 每一个位置可以选与不选。
void combine(char*, int, vector<char>&);
void combine(char* str) {
	if(str == NULL) return;
	int len = strlen(str);
	vector<char> result;
	for(int i = 1; i <= len; i++) {
		combine(str, i, result);
	}
}
void combine(char* str, int num, vector<char>& result) {
	if(num == 0) {
		vector<char>::iterator it = result.begin();
		for(; it != result.end(); it++) {
			cout << *it;
		}
		cout << endl;
		return;
	}
	if(str == NULL || *str == '\0') return;
	result.push_back(*str);
	combine(str+1, num-1, result);
	result.pop_back();
	combine(str+1, num, result);
}
int main() {
	char *ps = new char[4];
	strcpy(ps, "abc");
	combine(ps);
	return 0;
}
