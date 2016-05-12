#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>
#include <algorithm>

using namespace std;
const int kind = 26;
const int N = 1005;
string ss[N];
struct Node {
    int num;
    bool tail;
    Node* next[kind];
    Node() : num(1), tail(false) {
        memset(next, 0, sizeof(next));
    }
};
void insert(Node* root, string s) {
    Node* p = root; // p or root not equal null
    int i = 0, index;
    while(s[i]) {
        index = s[i] - 'a'; // char - char is int
        if(p->next[index] == NULL) {
            p->next[index] = new Node();
        }
        else {
            p->next[index]->num++;
        }
        i++;
        p = p->next[index];
    }
    p->tail = true;
}
void find(Node* root, string s) {
    cout << s << ' ';
    Node* p = root;
    for(unsigned i = 0; i < s.length(); i++) {
		cout << s[i];
        int index = s[i] - 'a';
        if(p->next[index]->num == 1) {
            break;
        }
        p = p->next[index];
    }
    cout << endl;
}
int main() {
    Node* root = new Node();
    int i = 0;
    while(cin >> ss[i]) {
        insert(root, ss[i++]);
    }
    for(int j = 0; j < i; j++) {
        find(root, ss[j]);
    }
    return 0;
}

