import static java.lang.Math.*;
/**
 * The Complexity class is a simple tool to compute the true and estimated 
 * time and space complexity of the CH class. 
 * The true complexity is stored by static arrays and the estimated complexity
 * can be computed by methods. 
 * Those arrays have a fixed length 50. It can be enlarged by adding more 
 * partition numbers in the code. 
 * <p>
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 3.1
 */
public class Complexity
{
    /**  parN[n] is the number of integer partitions of n. */
    public static int[] parN;
    private static int length;
    /** 
     * chN[d] is the number of Caparaso-Harris invariants N(d, r, alpha, beta)
     * for any fixed r, which is the number of (alpha, beta) satisfying 
     * I(alpha) + I(beta) = d.
     */
    public static long[] chN = new long[length];
    /**
     * space[d] is the number of keys in dictionaries at stage d for maxNode = 
     * 4 in CH class. 
     */
    public static long[] space = new long[length];
    /**
     * time[d] is the time complexity for deg = d and maxNode = 4.
     */
    public static long[] time =  new long[length];
    private static int maxNode;
    private static ArrayOp arrOP; 
    private static Partitions parArr;
    /**
     * Construct the complexity class and declare instance variables. 
     */
    public Complexity () {    
        parN = new int[] {1, 1, 2, 3, 5, 7, 11, 15, 22, 30, 42,  //0-10
            56, 77, 101, 135, 176, 231, 297, 385, 490, 627,         //11-20
            792, 1002, 1255, 1575, 1958, 2436, 3010, 3718, 4565, 5604, //21-30
            6842, 8349, 10143, 12310, 14883,  // 31-35
            17977, 21637, 26015, 31185, 37338, //36-40
            44583, 53174, 63261, 75175, 89134, //41-45
            105558, 124754, 147273, 173525, 204226}; //46-50*/
        length = parN.length; 
        maxNode = 4;
        chN = new long[length];
        space = new long[length];
        time =  new long[length];
        arrOP = new ArrayOp(length); 
        parArr = new Partitions(length);
    }   
    /**
     * Compute the instance variables to the given degree. 
     * @param deg The max degree (required to be <= 50).  
     */ 
    public void calculate(int deg) {
        if (deg > length) {
            System.out.println("deg can not be greater than " + length);
            return;
        }
        //calclate the number of all valid CH invariants of degree d and fix r
        for (int i = 0; i <= deg; i++) {
            chN[i] = 0;
            for (int j = 0; j <= i; j++) {
                chN[i] += parN[j] * parN[i - j];
            }
        }
        //calculate space <=  curSave[i-1] + curSave[i] + chN[i]  
        space[0] = 0;
        long[] curSave = new long[length];
        curSave[0] = 0;
        for (int i = 1; i < deg; i++) {
            curSave[i] = 0;
            for (int j = Math.min(i, maxNode); j >= 0; j--) {
                curSave[i] += parN[j] * parN[i - j];
            }
        }                         
        for (int i = 1; i <= deg; i++) {
            // state i: max: all CH inv for current r
            // stage i - 1: curSave
            space[i] = chN[i] + curSave[i] + curSave[i - 1];
        }
        //calculate time
        time[0] = 0;
        for (int d = 1; d <= deg; d++) {
            time[d] = 0;
            for (int r = 0; r <= maxNode; r++) {                     
                for (int j = d; j >= 0; j--) {
                    for (byte[] alpha : parArr.get(j)) {
                        for (byte[] beta : parArr.get(d - j)) {
                            N(d, r, alpha, beta);  
                        }
                    }
                }    
            }     
        }                                 
    }
    /**
     * Compute the numbr of operations for the N method in CH by simulation. 
     */
    private void N(int d, int r, byte[] alpha, byte[] beta) {  
        time[d]++;
        for (int k = 0; k < beta.length; k++) { // the first term
            if (beta[k] > 0) {
                time[d]++;      
            }                
        }        
        if (d > 0) {                              // the second term
            for (int j = Math.max(0, arrOP.sum(beta) - r + d - 1); j < d; j++) {
                time[d] += parN[j] * parN[d - 1 - j];             
            }
        }                   
    }
    /**
     * @param deg The maximal degree. 
     * @return the estimated partition function for degree deg.
     */
    public double ePar(int deg) {
        return 1.0 / 4 / deg / sqrt(3) * exp(PI * sqrt(2.0 * deg / 3));
    }
    /**
     * @param deg The maximal degree.
     * @return the estimated number of CH invariants for fixed deg, delta.  
     */  
    public double eCh(int deg) {
        return 1.0 / 48 / deg * exp(2 * PI * sqrt(deg / 3.0));
    }
    /**
     * @param deg The maximal degree.
     * @return the estimated number of keys in HashMaps in the CH class for 
     * degree deg.
     */
    public double eSpace(int deg) {
        return 1.0 / 24 / deg * exp(2 * PI * sqrt(deg / 3.0));
    }
    /**
     * @param deg The maximal degree.
     * @return the estimated running time for the CH class for degree deg.
     */
    public double eTime(int deg) {
        return deg * deg * deg * exp(PI * sqrt(8.0 * deg / 3));
    }
    /**
     * @param deg The maximal degree.
     * @return the estimated running time for naive CH formula for degree deg
     * and maxNode = 4.
     */
    public double naive(int deg) {
        return deg * 5 * exp(4 * PI * sqrt(deg / 3.0)) / 48 / 48;
    }
}
