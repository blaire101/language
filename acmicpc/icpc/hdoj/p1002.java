import java.util.Scanner;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int T = cin.nextInt();
        int ca = 0;
        while (T != 0) {
            BigInteger a = cin.nextBigInteger();
            BigInteger b = cin.nextBigInteger();
            BigInteger sum = a.add(b);
            System.out.println("Case " + (++ca) + ":");
            System.out.println(a + " + " + b + " = " + sum);
            if (T != 1) System.out.println();
            T--;
        }
    }
}
