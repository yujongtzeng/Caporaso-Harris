import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.math.BigInteger; 
// todo: re-run test speed and update to paper
/**
 * <p>
 * The F0table class uses dynamic programming approach to implement the 
 * recursive formula of Vakil in "Counting curves on rational surfaces". 
 * The formula computes the number of singular curves on P^1*P^1 which 
 * satisfy given tangency conditions with a fixed line in |O(1,0)|. 
 * <p>
 * Users will be prompted to enter three integers: a, b and maxNode. 
 * <p>
 * (a,b): the maximal bi-degrees of the curve class <br>
 * maxNode:  max difference between the arithmetic genus and geometric 
 * genus of the curves in concern.  
 * <p>
 * The program will print two kinds of computation results to txt files:
 * numbers of nodal curves line by line and generating series. 
 * <p>
 * The first kind of files contain the number of geometric genus g curves of 
 * bi-degrees (i, b) on P^1*P^1 which satisfy tangency conditions 
 * (alpha, beta) with a fixed line in |O(1,0)| for  <br />
 * 1) (i,b) for i = (a - 5 + 1),..., a (due to the recursive nature 
 * of Vakil's formula) (5 is the default number and can be changed by 
 * modifying the instance variable printLast),  <br />
 * 2) all g between (arithmetic genus of O(i,b) - maxNode) and 
 *    arithmetic genus of O(i,b),  <br />
 * 3) all valid tangency conditions alpha and beta 
 *    (valid := satisfy I(alpha)+I(beta) = b). 
 * <p>
 * alpha : tangency conditions at assigned points.  <br>
 * beta : tangency conditions at unassigned points. 
 * beta = (beta_1, beta_2,....)
 * <p> 
 * The first kind of output will be located at ../output/F0 and be split 
 * according to the degrees and number of nodes. 
 * <p>
 * The second kind of files contain the generating series of those numbers. 
 * They can be found at ../output/genF0.
 * See the documentation of CH class for the definition and range of generating
 * series. 
 * <p>
 * Note on algorithm:  <br />
 * This class and CH use the same algorithm. 
 * <p>
 * alpha and beta are stored by byte arrays. The length of them (and 
 * variations) are of fixed length b. The bound is from the max length of 
 * alpha, alpha', beta, beta' <= b. 
 * All methods in arrayOp will check if the length of inputs equals b. 
 * <p>
 * @author Yu-jong Tzeng
 * @version 2.1
 * @since May 21, 2020.
 */

public class F0table {  
    private static int a;
    private static int b;         
    private static int maxNode;
    private static ArrayOp arrOp;
    /**
     * The number of top degrees of the curve classes  which 
     * will be printed out. 
     * The output will will contain number of curves in |O(i,b)| for 
     * i = (a - printLast + 1) to a.
     */
    public static int printLast;   
    // wDeg is the bound of weight degree of output monomial 
    private static int wDeg; 
    // The computation will start from i = 0 to b one by one. 
    // At stage i, prevMap: results of bi-degrees (i - 1, b) from last CurSave
    // curSave: results of (i, b) will possibly be used for (i + 1, b)
    // curDump: results of (i, b) won't be used for (i + 1, b)
    // When a new stage starts, prevMap = curSave and curDump is empty
    private HashMap<ArrayList<Byte>, BigInteger> prevMap;
    private HashMap<ArrayList<Byte>, BigInteger> curSave;   
    private HashMap<ArrayList<Byte>, BigInteger> curDump;     
    private Partitions parArr; 
    /**
     * The constructor of the class.
     * @param a the number of ample class h in the curve class O(a,b)
     * @param b the number of fiber class h in the curve class O(a,b)
     * @param maxNode the maximal difference between arithmetic genus and 
     * geometric genus of the curves.
     */
    public F0table(int a, int b, int maxNode) {        
        this.a = a;
        this.b = b;
        this.maxNode = maxNode;       
        arrOp = new ArrayOp(b);  
        parArr = new Partitions(b);
        printLast = 5;
        wDeg = 10;
        prevMap = new HashMap<ArrayList<Byte>, BigInteger>();                
        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
    }    
    /** 
     * This method prompt for user input, create objects then call compute(). 
     * Parameters a, b, and maxNode are initialized by user input. 
     * @param args Unused
     */
    public static void main(String[] args)
    {        
        // Read from user input
        Scanner reader = new Scanner(System.in);  
        System.out.println("This program computes the number of singular"
            + "curves of bi-degrees (a,b) on P^1*P^1");
        System.out.println("satisfy tangency conditions with a given line"
                + " in |O(1,0)|.");
        System.out.println("Enter the maximal number of a:");        
        System.out.println("a = ");
        int inputa = reader.nextInt();
        System.out.println("Enter the maximal number of b:");        
        System.out.println("b = ");
        int inputb = reader.nextInt();
        System.out.println("Enter the max number of (arithmetic genus"
            + "- geometric genus):");        
        System.out.println("maxNode = ");
        int inputMaxNode = reader.nextInt();        
        System.out.format("The output will be written in the directory" + 
                           "../output/F0 and ../output/genF0\n");
        reader.close();                 
        F0table f0Table = new F0table(inputa, inputb, inputMaxNode);
        f0Table.compute();        
    }    
    /** 
     * Compute all N(i ,b, g, alpha and beta) in the specified range. 
     * If i >= a - printLast, write the result in the output files.  
     */
    public void compute() {
        // Here we put N(O(i, b), all valid alpha and beta) into dictionary
        for (int i = 0; i <= a; i++) {
            System.out.println("Computing a = " + i);
            prevMap = curSave;
            curSave = new HashMap<ArrayList<Byte>, BigInteger>();
            curDump = new HashMap<ArrayList<Byte>, BigInteger>();
            if (i <= b - printLast) {
                // Compute N and put in the dictionary
                // cur is a working dictionary. First is curDump then curSave
                HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
                for (int g = MyF.g_a(i, b) - maxNode, j = b; 
                    g <= MyF.g_a(i, b) && j >= 0 ; g++, j--) {
                    // If j > maxNode then next beta will be negative. 
                    // because b - j = I(beta') >= |beta| - r + b so these
                    // numbers will not be used again for bigger degree.
                    if (j <= maxNode) { cur = curSave; }
                    for (byte[] alpha : parArr.get(j)) {
                        for (byte[] beta : parArr.get(b - j)) {
                            cur.put(Key.make(g, alpha, beta), 
                                N(i, g, alpha, beta));
                        }
                    }
                }
            }   
            else {
                for (int g = MyF.g_a(i, b) - maxNode; 
                    g <= MyF.g_a(i, b); g++) {
                    // Space saving feature. Since no (i+1, b) terms and 
                    // the first term has the same d, r, data in curDump
                    // and curSave can be thrown away.      
                    if (i == a) {
                        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
                        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
                    }
                    output(i, g);     
                }    
            }
        }       
    }
    /** Write to output files and put results in dictionary.
     * @param i the working degree
     * @param g the working number of nodes
     */
    private void output(int i, int g) {
        try {
            String fname = String.format("O(%d, %d)_g=%d.txt", i, b, g);
            File outputfile = new File("../output/F0/" + fname);  
            File genFun = new File("../output/genF0/" + fname);         
            outputfile.getParentFile().mkdirs();
            genFun.getParentFile().mkdirs();
            PrintWriter num = new PrintWriter(outputfile, "UTF-8"); 
            PrintWriter gen = new PrintWriter(genFun, "UTF-8");
            HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
            for (int j = b; j >= 0; j--) {
                if (j <= maxNode) { cur = curSave; }
                for (byte[] alpha : parArr.get(j)) {
                    if (j <= 4) { 
                        gen.println("\n");
                        gen.println("alpha = " + MyF.str(alpha));
                    } 
                    for (byte[] beta : parArr.get(b - j)) {
                        BigInteger ansN = N(i, g, alpha, beta);
                        cur.put(Key.make(g, alpha, beta), ansN);
                        num.printf("N(O(%d, %d), %d, %s, %s) = %d\n", 
                             i, b, g, MyF.str(alpha), MyF.str(beta), ansN);
                        if (j <= 4 && b - j - beta[0] <= wDeg) {
                            gen.printf(ansN + MyF.toVar(beta) + "+");
                        }            
                    }
                }    
            }
            num.close();
            gen.close();
        } 
        catch (IOException e) {
            System.out.println("There is an error in I/O.");
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
        if (curSave.containsKey(key)) {
            return curSave.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else if (curDump.containsKey(key)) {
            return curDump.get(key).multiply(BigInteger.valueOf(k + 1));
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