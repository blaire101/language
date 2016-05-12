#include <iostream>
using namespace std;

int main() {
    int a, b;
    while(cin >> a >> b) {
        cout << a+b << endl;
    }
}
---------python----------------------
import sys
for line in sys.stdin:
    a = line.split()
    print int(a[0]) + int(a[1])
---------java------------------------
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (in.hasNextInt()) {
			int a = in.nextInt();
			int b = in.nextInt();
			System.out.println(a + b);
		}
	}
}

