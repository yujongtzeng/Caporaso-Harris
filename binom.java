import java.math.BigInteger; 

public class binom
{
    public binom(int n) {
        long[][] C = new long[n + 1][n + 1];
        
        C[0][0] = 1;
        for (int j = 1; j <= n; j++) {
            C[0][j] = 0;
            C[j][0] = 1;
        }    
        System.out.println(MyF.str(C[0]));
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (j > i) {
                    C[i][j] = 0;
                }
                else {
                    C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
                }
            }
            System.out.println(i + MyF.str(C[i]));
        } 
    } 
    public void test(int n) {
        BigInteger f = BigInteger.valueOf(n);
        System.out.println(f.toString());
    }
}
