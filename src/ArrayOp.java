/** 
 * The ArrayOp class implements operations on arrays of bytes. 
 * <p>
 * Each object of this class has a fixed instance variable which specifies the 
 * required length of input and output arrays. Error message will be 
 * thrown if the length of input is incorrect.
 * <p>
 * All components of the input are assumed to be nonnegative integers. 
 * All components of the output will be nonnegative integers too. 
 * For substract and binom, the program checks whether the first input is 
 * greater or equal to the second input so that those operations work in the 
 * naive way. 
 * 
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0
 */

public class ArrayOp
{    
    private static long[][] C;
    private static int length;
    
    /** 
     * Before doing any operations, the class builds a table of all 
     * binomial coefficients m choose k for all 0 <= k <= m <= n.
     * n is the required length for inputs. 
     * 
     * @param n The max parameter allowed for binomial coefficients in the 
     * object.
     * It is also the required length for input arrays and outputs arrays of 
     * all methods. 
     */
    public ArrayOp(int n) {
        length = n;
        C = new long[n + 1][n + 1];
        
        C[0][0] = 1;
        for (int j = 1; j <= n; j++) {
            C[0][j] = 0;
            C[j][0] = 1;
        }    
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (j > i) {
                    C[i][j] = 0;
                }
                else {
                    C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
                }
            }
        }
    } 
    
    /** 
     * Return true if the length of two input arrays are the same and every 
     * component of the first array is greater or equal to the second.
     * @param c byte[]
     * @param d byte[]
     * @return True if c.length == d.length and c[i] >= d[i] for all i. 
     */
    public boolean greater(byte[] c, byte[] d) {
        checkLength(c);
        checkLength(d);
        if (!checkLength(c) || !checkLength(d)) return false;
        
        // The length of c and d are both length
        for (int i = 0; i < length; i++) {
            if (c[i] < d[i]) return false;        
        }
        return true;
    }
    /**
     * Return True if the length of c equals the user input n.
     */    
    private boolean checkLength(byte[] c) {
        if (c.length != length) {
            System.out.println("The length of " + c + " should = " + length);
        }
        return c.length == length;
    } 
    
    /**
     * Return the componentwise sum of two arrays.
     * @param c byte[]
     * @param d byte[]
     * @return An array with the i-th element being c[i] + d[i].
     */
    public byte[] add(byte[] c, byte[] d) {        
        checkLength(c);
        checkLength(d);
        if (!checkLength(c) || !checkLength(d)) return new byte[length];
        
        byte[] ans = new byte[length];        
        for (int i = 0; i < length; i++) {
            ans[i] = (byte) (c[i] + d[i]);      
        }     
        return ans;
    }  

    /**
     * Substract the second input from the first input. Every component 
     * of the first input must be great or equal to the second input. 
     * @param c byte[]
     * @param d byte[]
     * @return The componentwise difference of the inputs. Send error 
     * message if the length of c or d is not equal to n.
     */
    public byte[] substract(byte[] c, byte[] d) {        
        if (!greater(c, d)) {
            System.out.println(c + " must be greater then " + d);      
            return new byte[length];
        } 
        
        // Now  c>=d, so we only have to do c_i -d_i for i = 0,....length-1
        byte[] ans = new byte[length];  
        for (int i = 0; i < length; i++) {
            ans[i] = (byte) (c[i] - d[i]);
        }  
        return ans;   
    } 
    
    /**
     * Return Ic. 
     * @param c byte[]
     * @return If c = {c1, c2, c3,..},  return 1* c1 + 2* c2 + 3 * c3 + ....
     */
    public int I(byte[] c)   
    {
        if (!checkLength(c)) return 0;
        int ans = 0;        
        for (int i = 0; i < length; i++) {
            ans = ans + (i + 1) * c[i];
        }        
        return ans; 
    }    
    
    /**
     * Return Ic only to the given index.
     * @param c byte[]
     * @param index int
     * @return If c = {c1, c2, c3,..},  
     * return 1* c1 + 2* c2 + 3 * c3 + .... + index * cindex.
     */
    public int I(byte[] c, int index)    
    {
        int ans = 0;        
        for (int i = 0; i < index; i++)
        {
            ans = ans + (i + 1) * c[i];
        }        
        return ans; 
    }
    
     /**
     * Return I^c.
     * @param c byte[]
     * @return If c = {c1, c2, c3,..},  
     * return 1^(c1) + 2^(c2) + 3^(c3) + .... 
     */
    public long J(byte[] c)    
    {
        if (!checkLength(c)) return 0;
        long ans = 1;        
        for (int i = 0; i < length; i++)
        {
            ans = ans * ((long) Math.pow(i + 1, c[i]));
        }        
        return ans; 
    }
    
    /**
     * Return |c|.
     * @param c byte[]
     * @return If c = {c1, c2, c3,..}, return the sum of all ci. 
     */
    public int sum(byte[] c)   
    {
        int ans = 0;        
        for (int i = 0; i < length; i++) {
            ans = ans + c[i];
        }        
        return ans; 
    } 
    
    /**
     * Return the product of componentwise binomial coefficients. 
     * Every component of the first input must be greater or equal to the 
     * second input. 
     * @param c byte[]
     * @param d byte[]
     * @return The product of all C[ci][di] (C[n][k] is the binomial 
     * coeffient).  
     */
    public long binom(byte[] c, byte[] d) 
    {
        long ans = 1;
        // Send error message if c is not >= d
        if (!greater(c, d)) {
            System.out.println(c + " must be greater than " + d);
            return 0;        
        }
        
        // Now c >=d, only need to multiply C[c_i][d_i] in their common range
        // Outside common range, d_i must be zero. C gives 1.        
        for (int i = 0; i < length; i++) {
            if (c[i] > length) {
                System.out.println(c[i] + " is bigger than" + length +  
                           "       Please initiate a larger object.");
                return 0;
            }
            ans = ans * C[c[i]][d[i]];
        }          
        return ans; 
    }    
}


