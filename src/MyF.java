import java.util.Arrays;
import java.math.BigInteger; 
/**
 * MyF is a class which contains short static functions. 
 *
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0
 */
public class MyF 
{
    /**
     * toVar method converts the input array (a0, a1, ...) to a monomial 
     * "b^(a1) c^(a2) d^(a3)...". 
     * The elements of the input array is the power of b, c, d,...,z.  
     * If the power is greater than nine
     * then parenthesis will be added. Negative elements will be ignored. 
     * For example, if arr = [2, 1, 3, -1, 14] then output is "b^1 c^3 e^(14)"
     * (with space between each monomial).  
     * Only the first 26 elements will be used.
     * @param arr A byte array {a0, a1, a2...}
     * @return The correponding monomial "b^(a1) c^(a2) d^(a3)...".
     */
    public static String toVar(byte[] arr) {
        String ans = "";
        for (int i = 1; i < Math.min(26, arr.length); i++) {
            if (arr[i] > 0) {
                String temp = "";
                if (arr[i] > 9) {
                    temp = "(" + arr[i] + ")";
                }
                else { temp = "" + arr[i]; }
                ans = ans + (char) (i + 97) + "^" + temp + " ";            
            }
        }
        return (ans.length() == 0 ? "" : ans.substring(0, ans.length() - 1));
    }
    /** 
     * Return the string representation of an array (seperated by "," ). 
     * @param arr Any byte array
     * @return The string form of input array (seperated by "," ).
     */   
    public static String str(byte[] arr) {
        return Arrays.toString(arr).replaceAll("\\s", "");
    } 
    /*public static String str(long[] arr) {
        return Arrays.toString(arr).replaceAll("\\s", "");
    }*/   
    /**
     * Return the arithmetic genus of the curve class O(a,b) on P^1*P^1, 
     * which is (a - 1)*(b - 1). 
     * @param a An integer
     * @param b An integer
     * @return The integer (a - 1)*(b - 1) 
     */
    public static int g_a (int a, int b) { 
        return (a - 1) * (b - 1);
    }
    /**
     * Return the sum of two long integers.
     * @param a A long integer
     * @param b A long integer
     * @return a + b as a BigInteger
     */
    public static BigInteger add(long a, long b) {
        return BigInteger.valueOf(a).add(BigInteger.valueOf(b));       
    } 
    /**
     * Return the sum of a BigInteger and a long integer.
     * @param a A BitInteger
     * @param b A long integer
     * @return a + b as a BigInteger
     */
    public static BigInteger add(BigInteger a, long b) {
        return a.add(BigInteger.valueOf(b));       
    } 
    /**
     * Return the sum of a long integer and a BigInteger.
     * @param a A long integer
     * @param b A BigInteger
     * @return a + b as a BigInteger
     */
    public static BigInteger add(long a, BigInteger b) {
        return b.add(BigInteger.valueOf(a));       
    } 
    /*public static BigInteger prod(long a, long b, long c, long d) {
        BigInteger ans = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        ans = ans.multiply(BigInteger.valueOf(c));
        return ans.multiply(BigInteger.valueOf(d));       
    }*/
    /**
     * Return the product of all inputs. 
     * @param a A long integer
     * @param b A long integer
     * @param c A long integer
     * @return a*b*c as a BigInteger
     */
    public static BigInteger prod(long a, long b, long c) {
        BigInteger ans = BigInteger.valueOf(a).multiply(BigInteger.valueOf(b));
        return ans.multiply(BigInteger.valueOf(c));       
    } 
}
