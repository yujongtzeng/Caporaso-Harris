import java.util.Arrays;
import java.math.BigInteger; 
/**
 * MyF is a class which contains short misc functions. 
 *
 * @author Yu-jong Tzeng
 * @version 2.0
 * @since August 27, 2019.
 */
public class MyF 
{
    /**
     * Convert the input array to a monomial "b^(a0) c^(a1) d^(a2)...." 
     * The elements of the input array is the power of b, c, d,...,z.  
     * For example, if arr = [1, 3, 0, 4] then output = "b^1 c^3 e^4".
     * Only the first 25 elements will be used, if the length of input 
     * is greater. 
     * @param arr An integer array 
     * @return The string of correponding monomial.
     */
    public static String toVar(byte[] arr) {
        String ans = "";
        for (int i = 1; i < Math.min(26, arr.length); i++) {
            if (arr[i] > 0) {
                ans = ans + (char) (i + 97) + "^" + arr[i] + " ";            
            }
        }
        return (ans.length() == 0 ? "" : ans.substring(0, ans.length() - 1));
    }
    /** 
     * Return the string representation of an array (seperated by "," ). 
     * @param arr An integer array
     * @return String
     */   
    public static String str(byte[] arr) {
        return Arrays.toString(arr).replaceAll("\\s", "");
    } 
    public static String str(long[] arr) {
        return Arrays.toString(arr).replaceAll("\\s", "");
    }   
    /**
     * Return the arithmetic genus of the curve class O(a,b), which is 
     * (a - 1)*(b - 1). 
     * @param a An Integer
     * @param b An Integer
     * @return The integer (a - 1)*(b - 1) 
     */
    public static int g_a (int a, int b) { 
        return (a - 1) * (b - 1);
    }
    public static BigInteger add(long a, long b) {
        return BigInteger.valueOf(a).add(BigInteger.valueOf(b));       
    } 
    public static BigInteger add(BigInteger a, long b) {
        return a.add(BigInteger.valueOf(b));       
    } 
    public static BigInteger add(long a, BigInteger b) {
        return b.add(BigInteger.valueOf(a));       
    } 
    /*public static BigInteger prod(long a, long b, long c, long d) {
        BigInteger ans = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        ans = ans.multiply(BigInteger.valueOf(c));
        return ans.multiply(BigInteger.valueOf(d));       
    }*/
    public static BigInteger prod(long a, long b, long c) {
        BigInteger ans = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        return ans.multiply(BigInteger.valueOf(c));       
    } 
}
