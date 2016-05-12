#include <iostream>
#include <cstdio>
#include <cstring>
#include <cstdlib>

using namespace std;

/*  qu_01 相对路径转绝对路径_利用栈的思想  */
char* convert(const char* path) {
    if(NULL == path) {
        fprintf(stderr, "invalid argument!\n");
        return NULL;
    }
    int len = strlen(path);
    char* result = (char*)malloc(len * sizeof(char));
    if(result == NULL) {
        fprintf(stderr, "failed to malloc.\n");
        return NULL;
    }
    int i = 0, top = 0;
    while(i < len) {
        while(i < len && path[i] != '.') {
            result[top++] = path[i++];
        }
        int point_cnt = 0;
        for(; i < len && path[i] == '.'; i++) {
            point_cnt++;
        }
        if(point_cnt == 2) {
            --top;
            while(--top > 0 && result[top] != '/');
            ++top;
        }
        i++;
    }
    result[top] = '\0';
    return result;
}
/* qu_02 比较两个字符串的不同，返回不同点的位置（相对第二字符串而言） */
char* compare(const char* ps1, const char* ps2) {
    if(ps1 == NULL || ps2 == NULL) {
        fprintf(stderr, "invalid argument.\n");
        return NULL;
    }
    int i = 0, j = 0;
    while(ps1[i] != '\0' && ps2[j] != '\0') {
        if(ps1[i] != ps2[j])
            break;
        ++i;
        ++j;
    }
    return ps2 + j;
}
/* qu_03 最小的 K 个数，大数据，文件中100亿行数据，每行一个数字 */
void solve(const char* filename, multiset<int, greater<int> >& MS, int k) {
    MS.clear();
    FILE *output_file = NULL;
    int data = 0;
    output_file = fopen(filename, "r");
    if(output_file == NULL) {
        cout << "error\n";
    }
    while(fscanf(output_file, "%d", &data) != EOF) {
        cout << "data: " << data << endl;
        if((int)MS.size() < k)
            MS.insert(data);
        else {
            multiset<int, greater<int> >::iterator sit = MS.begin();
            if(data < *sit) {
                MS.erase(sit);
                MS.insert(data);
            }
        }
    }
    fclose(output_file);
}
/* qu_04 五子棋 */
const int N = 11;
int map[N][N];
void init(int n) {
    srand(time(0));
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            map[i][j] = rand() % 4;
        }
    }
}
void solve(int n) {
    if(n <= 0) {
        fprintf(stderr, "invalid argument.\n");
        return;
    }
    for(int i = 0; i < n; i++) {
        for(int j = 0; j < n; j++) {
            for(int cnt = 0, k = j; k < n; k++) { // 纵向
                if(map[i][j] == map[i][k]) {
                    cnt++;
                    if(cnt == 5) {
                        for(int e = k-4; e <= k; e++) {
                            cout << i << ' ' << e << endl;
                            return;}}}} /* ... 横向，斜下，斜上 */}}

int main() {
    return 0;
}
