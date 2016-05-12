#include <iostream>
#include <cstdio>
#include <cstring>
#include <queue>
#include <algorithm>
using namespace std;
/*
 * 完全二叉树(Complete Binary Tree)： 若设二叉树的高度为h，除第 h 层外，其它各层 (1～h-1) 的结点数都达到最大个数，
 * 第 h 层所有的节点都连续集中在最左边，这就是完全二叉树。 
 * 判断思路 ：广度优先搜索整个二叉树，一旦找一个不含有子节点或者只含有一个左子节点之后，那么后续的所有节点都必须是叶子节点。否则，该树就不是完全二叉树。
 */
struct Node {
	int value;
	Node *lchild, *rchild;
	Node(int v) : value(v) {
		lchild = NULL;
		rchild = NULL;
	}
	~Node() {
		if(lchild != NULL) {
			delete lchild;
			lchild = NULL;
		}
		if(rchild != NULL) {
			delete rchild;
			rchild = NULL;
		}
	}
};
bool bfs(Node* root) {
	if(root == NULL) return true;
	queue<Node*> Q;
	Q.push(root);
	bool flag = false;
	while(!Q.empty()) {
		Node *top = Q.front(); Q.pop();
		if(flag) {
			if(top->lchild != NULL || top->rchild != NULL) {
				return false;
			}
		}
		if(!flag && top->rchild == NULL) {
			flag = true;
		}
		if(top->lchild) Q.push(top->lchild);
		if(top->rchild) Q.push(top->rchild);
	}
	return true;
}
int main() {
	return 0;
}
