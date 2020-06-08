import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.math.BigInteger; 
/**
 * <p>
 * Let N(a, b, g, alpha, beta) be the number of  genus g curves in |O(a,b)| 
 * on P^1*P^1 which satisfy tangency conditions (alpha, beta) with a fixed 
 * line in |O(1,0)|. 
 * F0decreaseCheck is a program to check that for fixed a, b, g, beta, 
 * whether N(a, b, g, alpha, beta), is a non-increasing sequence in 
 * lexicographic order of alpha. 
 * If not, the counterexample will be printed out on the screen in the form
 * "N(a, b, g, alpha, beta) > last" where last is the number right before. 
 * </p>
 * 
 * (a,b): the maximal bi-degrees of the curve class <br>
 * 
 * gdiff:  max difference between the arithmetic genus and geometric genus of 
 * the curve the program will compute.  <br>
 * 
 * The program will check N(i, b, g', alpha, beta) for all i <= a, g' between 
 * the arithmetic genus of (i, b) and (the arithmetic genus of (i, b) - gdiff)
 * and all valid alpha and beta. 
 * 
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0
 */
public class F0decreaseCheck {  
    private static int a;
    private static int b;         
    private static int gdiff;
    private static ArrayOp arrOp;
    private Partitions parArr; 
    private HashMap<ArrayList<Byte>, BigInteger> prevMap;
    private HashMap<ArrayList<Byte>, BigInteger> curMap;    

    /**
     * The constructor of the class.
     * @param a the maximal curve bidegree is O(a, b)
     * @param b the second bidegree is b.
     * @param gdiff the max difference between arithmetic genus and geometric 
     * genus of the curve we'll compute
     */
    public F0decreaseCheck(int a, int b, int gdiff) {        
        this.a = a;
        this.b = b;
        this.gdiff = gdiff;          
        arrOp = new ArrayOp(b);  
        parArr = new Partitions(b);
        prevMap = new HashMap<ArrayList<Byte>, BigInteger>();
        curMap = new HashMap<ArrayList<Byte>, BigInteger>();
    }
    
    /** 
     * Call this method compute the results and generate output. 
     * User needs to enter a, b, and gdiff. 
     * @param args Unused
     */
    public static void main(String[] args)
    {        
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("This program will check whether the number of " 
                    + "nodal curves on P^1*P^1 of fixed");
        System.out.println("bidegree and nodes and beta is non-increasing " 
            + "in lexicographic order of alpha.");
        System.out.println("Enter a:");        
        System.out.println("a = ");
        int aIn = reader.nextInt();
        System.out.println("Enter b:");        
        System.out.println("b = ");
        int bIn = reader.nextInt();
        System.out.println("Enter the max number of (arithmetic genus"
            + "- geometric genus):");        
        System.out.println("gdiff = ");
        int gIn = reader.nextInt();        
        System.out.println("If not, the increasing part will be printed out.");
        reader.close();
                 
        F0decreaseCheck check = new F0decreaseCheck(aIn, bIn, gIn);
        check.compute();        
    }
    
    /** 
     * Run this method to compute and create output file.  
     */
    private void compute() {
        // Here we put N(O(i, b), all valid alpha and beta) into dictionary
        for (int i = 0; i <= a; i++) {
            System.out.println("Computing a = " + i);
            prevMap = curMap;
            curMap = new HashMap<ArrayList<Byte>, BigInteger>();
            // Compute N and put in the table           
            for (int g = MyF.g_a(i, b) - gdiff; g <= MyF.g_a(i, b); g++) {
                for (int j = 0; j <= b; j++) {
                    for (byte[] beta : parArr.get(j)) {
                        BigInteger lastN = BigInteger.valueOf(-1);
                        //System.out.print(MyF.str(beta));
                        for (byte[] alpha : parArr.get(b - j)) {
                            BigInteger ansN =  N(i, g, alpha, beta);
                            curMap.put(Key.make(g, alpha, beta), ansN);
                            if (lastN.compareTo(ansN) < 0 
                                && lastN.compareTo(BigInteger.ZERO) >= 0) {
                                System.out.format("N(%d, %d, %d, %s, %s) > %d\n"
                                    , i, b, g, MyF.str(alpha), MyF.str(beta), lastN);
                            }
                            lastN = ansN;   
                        }  
                    }
                }
            }
        }                   
    }    
    /** 
     * The recursive formula is implemented here. 
     */
    private BigInteger N(int aa, int g, byte[] alpha, byte[] beta) {      
        // invalid inputs
        if (arrOp.I(alpha) + arrOp.I(beta) != b) {
            System.out.format("I(%s) + I(%s) must equal to %d\n", 
                MyF.str(alpha), MyF.str(beta), b);
            return BigInteger.ZERO;             
        }
        if (aa < 0) {
            System.out.format("Degree can't be negative: " + aa);
            return BigInteger.ZERO;
        }
        if (g > MyF.g_a(aa, b)) {
            System.out.format("The number of nodes can't be negative: " + g);
            return BigInteger.ZERO;
        }
        // Base case. Only fiber class passing through points. 
        // from the beginning of Section 8. 
        if (aa == 0 && arrOp.I(beta) == 0) { 
            if (alpha[0] == arrOp.sum(alpha) && g == 1 - b ) {
                return BigInteger.ONE;    
            }
            else {
                return BigInteger.ZERO;
            }
        } 
        BigInteger ans = BigInteger.ZERO; 
        // the first term
        for (int k = 0; k < b; k++) {    
            if (beta[k] > 0) {
                ans = ans.add(first(aa, g, alpha, beta, k));
            }                
        }
        // the second term
        if (aa > 0) {  
            int bdj = Math.max(0, arrOp.sum(beta) - MyF.g_a(aa, b) + g + b);
            for (int j = bdj; j <= b; j++) {
                for (byte[] bP : parArr.get(j)) {
                    for (byte[] aP : parArr.get(b - j)) {
                        ans = ans.add(second(aa, g, alpha, beta, aP, bP)); 
                    }
                }                
            }
        }    
        return ans;        
    }  
    /** 
     * Computes a single term in the first term
     */
    private BigInteger first(int i, int g, byte[] alpha, byte[] beta, 
                            int k) {                                           
        byte[] tempAlpha = alpha.clone();
        byte[] tempBeta = beta.clone();
        //alpha_+e_k, beta-e_k
        tempAlpha[k] = (byte) (alpha[k] + 1);  
        tempBeta[k] = (byte) (beta[k] - 1);    
        ArrayList<Byte> key = Key.make(g, tempAlpha, tempBeta);
        if (curMap.containsKey(key)) {
            return curMap.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else {
            System.out.format("Finding N(%d, %d, %d, %s, %s)\n", 
                i, b, g, MyF.str(alpha), MyF.str(beta));
            System.out.format("N(%d, %d, %d, %s, %s) can't be found.\n", 
                i, b, g, MyF.str(tempAlpha), MyF.str(tempBeta));
        }
        return BigInteger.ZERO;  
    }       
    /** 
     *  Computes a single term in the second term
     */
    private BigInteger second(int aa, int g, byte[] alpha, byte[] beta, 
                      byte[] aP, byte[] bP) {
        if (arrOp.greater(alpha, aP) && arrOp.greater(bP, beta)) {
            byte[] gamma = arrOp.substract(bP, beta);
            int gP = g - arrOp.sum(gamma) + 1;
            // no need to check gP >= MyF.g_a(aa - 1, b) - maxNode since
            // it's proven.
            if (gP <= MyF.g_a(aa - 1, b)) {                
                ArrayList<Byte> key = Key.make(gP, aP, bP);
                if (prevMap.containsKey(key)) {
                    BigInteger coeff = MyF.prod(arrOp.J(gamma),  
                         arrOp.binom(alpha, aP), arrOp.binom(bP, beta));
                    return coeff.multiply(prevMap.get(key));
                }
                else { // Table doesn't contain this term
                    System.out.format("Finding N(%d, %d, %d, %s, %s)\n",
                        aa, b, g, MyF.str(alpha), MyF.str(beta));
                    System.out.format("N(%d, %d, %d, %s, %s) wasn't found.\n", 
                        aa - 1, b, gP, MyF.str(aP), MyF.str(bP));
                }              
            }
        }
        return BigInteger.ZERO;
    }    
}    