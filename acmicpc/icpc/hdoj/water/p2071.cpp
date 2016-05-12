#include <iostream> // 水仙花
#include <cstdio>

int main() {
	int T, n;
	std::cin >> T;
	while (T--) {
		scanf("%d", &n);
		double num, maxv;
		for (int i = 0; i < n; i++) {
			scanf("%lf", &num);
			if (i == 0) {
				maxv = num;
				continue;
			}
			if (num > maxv) maxv = num;
		}
		printf("%.2f\n", maxv);
	}
    return 0;
}
                    

                
