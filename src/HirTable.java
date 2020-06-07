import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.math.BigInteger; 
/**
 * <p>
 * The HirTable class uses dynamic programming approach to implement the 
 * recursive formula of Vakil in "Counting curves on rational surfaces". 
 * The formula computes the number of singular curves on Hirzebruch surfaces 
 * F_n  which satisfy given tangency conditions with the divisor E 
 * (E^2 = -n). 
 * <p>
 * Users will be prompted to enter four integers: n, a, b and gdiff. 
 * <p>
  * n : The Hirzebruch surface is F_n. <br />
 * (a,b): the curve class is ah + bf with h^2 = n, f = fiber class. <br />
 * gdiff:  max difference between the arithmetic genus and geometric genus of 
 * the curve the program will compute. <br />  
 * <p>
 * The program will print two kinds of computation results to txt files:
 * numbers of nodal curves line by line and generating series. 
 * <p>
 * The first kind of files contain the number of geometric genus g curves in 
 * curve class ah + bf - iE which satisfy tangency conditions 
 * (alpha, beta) with E for  <br />
 * 1) i = 0,..., a (due to the recursive nature 
 * of Vakil's formula). To limit the range, modify the variable printLast  
 * to let i be the biggest printLast different numbers less or equal to a.  
 * <br />
 * 2) all g between (arithmetic genus of (ah + bf - iE) - gdiff) and 
 *    arithmetic genus of (ah + bf - iE),  <br />
 * 3) all valid tangency conditions alpha and beta 
 *    (valid := satisfy I(alpha) + I(beta) = (ah + bf - iE)E). 
 * <p>
 * alpha : tangency conditions at assigned points.  <br>
 * beta : tangency conditions at unassigned points. 
 * beta = (beta_1, beta_2,....)
 * <p> 
 * The first kind of output will be located at ../output/Hir and be split 
 * according to n, the degrees and number of nodes. 
 * <p>
 * The second kind of files contain the generating series of those numbers. 
 * They can be found at ../output/genHir.
 * See the documentation of CH class for the definition and range of generating
 * series. 
 * <p>
 * Note on algorithm:  <br />
 * This class and CH use the same algorithm. 
 * <p>
 * alpha and beta are stored by byte arrays. The length of them (and 
 * variations) are of fixed length b + a*n. The bound is from  
 * I(alpha) + I(beta) <= b + a*n for all (alpha, beta) in the program.
 * All methods in arrayOp will check if the length of inputs equals b + a*n. 
 * <p>
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0.
 */

public class HirTable {  
    private static int n;
    private static int a;
    private static int b;  
    private static int gdiff;
    private static int maxLength;
    private static ArrayOp arrOp;
    private Partitions parArr; 
    /**
     * The program will print out results in curve class ah + bf - iE  for 
     * i = the biggest printLast integers less or equal to a. Default is a + 1
     */
    public static int printLast;   
    // wDeg is the bound of weight degree of output monomial 
    private static int wDeg; 
    // The computation will start from i = 0 to a one by one. 
    // At stage i, prevMap: results of ah + bf - (i-1) E from last CurSave
    // curSave: results of ah + bf - iE may be used for ah + bf - (i + 1)E
    // curDump: results of ah + bf - iE won't be used for ah + bf - (i + 1)E
    // When a new stage starts, prevMap = curSave and curDump is empty
    private HashMap<ArrayList<Byte>, BigInteger> prevMap;
    private HashMap<ArrayList<Byte>, BigInteger> curSave;   
    private HashMap<ArrayList<Byte>, BigInteger> curDump;     

    /**
     * The constructor of the class. Will do setups before actual computation.
     * @param n The Hirzebruch surface is F_n, n >= 0. 
     * @param a The number of ample class h in the curve class ah + bf.
     * @param b The number of fiber class f in the curve class ah + bf.
     * @param gdiff The maximal difference between arithmetic genus and 
     * geometric genus of the curves.
     */
    public HirTable(int n, int a, int b, int gdiff) {        
        this.a = a;
        this.b = b;
        this.n = n;
        this.gdiff = gdiff; 
        maxLength = b + a * n;
        arrOp = new ArrayOp(maxLength);  
        parArr = new Partitions(maxLength);
        printLast = a + 1;
        wDeg = 10;
        prevMap = new HashMap<ArrayList<Byte>, BigInteger>();                
        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
    }    
    /** 
     * This method prompt for user input, create objects then call compute(). 
     * Parameters n, a, b, and gdiff are initialized by user input. 
     * @param args Unused
     */
    public static void main(String[] args)
    {        
        // Read from user input
        Scanner reader = new Scanner(System.in);  
        System.out.println("This program computes the number of singular"
            + "curves in curve class ah +bf on Hirzebruch surface F_n");
        System.out.println("satisfy tangency conditions with a given line"
                + " in |E|.");
        System.out.println("Enter n in F_n:");        
        System.out.println("n = ");   
        int inputn = reader.nextInt();
        System.out.println("Enter the maximal number of a:");        
        System.out.println("a = ");
        int inputa = reader.nextInt();
        System.out.println("Enter the maximal number of b:");        
        System.out.println("b = ");
        int inputb = reader.nextInt();
        System.out.println("Enter the max number of (arithmetic genus"
            + "- geometric genus):");        
        System.out.println("gdiff = ");
        int inputgdiff = reader.nextInt();        
        System.out.format("The output will be written in the directory" + 
                           "../output/Hir and ../output/genHir\n");
        reader.close();                 
        HirTable ht = new HirTable(inputn, inputa, inputb, inputgdiff);
        ht.compute();        
    }    
    /** 
     * Compute all N(i ,j, g, alpha and beta) in the specified range and 
     * generate output. 
     */
    public void compute() {
        // put N(ah+bf - (a-i)E, all possible alpha and beta) into dictionary. 
        // Let D = ih + jf = ah + bf - (a-i)E,
        // this implies j = b + n(a - i)
        // tangency condition satisfy I(alpha) + I(beta) = D.E = j
        for (int i = 0; i <= a; i++) {
            int j = b + n * (a - i);
            System.out.format("Computing %dh + %df \n",  i, j);
            prevMap = curSave;
            curSave = new HashMap<ArrayList<Byte>, BigInteger>();
            curDump = new HashMap<ArrayList<Byte>, BigInteger>();
            if (i <= a - printLast) {
                // Compute N and put in the dictionary
                // cur is a working dictionary. First is curDump then curSave
                HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
                for (int r = 0; r <= gdiff; r++) {
                    int g = g_a(n, i, j) - r; 
                    for (int k = j; k >= 0; k--) {
                        // If k > gdiff - r then next beta will be negative. 
                        // because |next beta| = r - r' - k so these
                        // |next beta| <= gdiff - r - k < 0. 
                        if (k <= gdiff - r) { cur = curSave; }
                        for (byte[] alpha : parArr.get(k)) {
                            for (byte[] beta : parArr.get(j - k)) {
                                cur.put(Key.make(g, alpha, beta), 
                                    N(i, g, alpha, beta));
                            }
                        }
                    }
                }
            }   
            else {
                for (int r = 0; r <= gdiff; r++) {
                    // Space saving feature. Since no i = a + 1 terms and 
                    // the first term has the same d, r, data in curDump
                    // and curSave can be thrown away.      
                    if (i == a) { 
                        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
                        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
                    }
                    output(i, r);     
                }    
            }
        }       
    }
    /** Write to output files and put results in dictionary.
     * @param i the working degree
     * @param r the working number of nodes
     */
    private void output(int i, int r) {
        int j = b + n * (a - i); 
        int g = g_a(n, i, j) - r;
        try {
            String fname = String.format("F%d/%dh+%df_g=%d.txt", n, i, j, g);
            File outputfile = new File("../output/Hir/" + fname);  
            File genFun = new File("../output/genHir/" + fname);         
            outputfile.getParentFile().mkdirs();
            genFun.getParentFile().mkdirs();
            PrintWriter num = new PrintWriter(outputfile, "UTF-8"); 
            PrintWriter gen = new PrintWriter(genFun, "UTF-8");
            HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
            for (int k = j; k >= 0; k--) {                
                if (k <= gdiff - r) { cur = curSave; }
                for (byte[] alpha : parArr.get(k)) {
                    if (k <= 4) { 
                        gen.println("\n");
                        gen.println("alpha = " + MyF.str(alpha));
                    } 
                    for (byte[] beta : parArr.get(j - k)) {
                        BigInteger ansN = N(i, g, alpha, beta);
                        cur.put(Key.make(g, alpha, beta), ansN);
                        num.printf("N(%dh+%df, %d, %s, %s) = %d\n", 
                             i, j, g, MyF.str(alpha), MyF.str(beta), ansN);
                        if (k <= 4 && j - k - beta[0] <= wDeg) {
                            gen.printf(ansN + MyF.toVar(beta) + "+");
                        }            
                    }
                }    
            }
            num.close();
            gen.close();
        } 
        catch (IOException e) {
            System.out.println("There is an error in I/O for HirTable.");
        } 
    }
    /** 
     * The recursive formula is implemented here. 
     */
    private BigInteger N(int i, int g, byte[] alpha, byte[] beta) {      
        // invalid inputs
        int j = b + n * (a - i);  
        if (arrOp.I(alpha) + arrOp.I(beta) != j) {
            System.out.format("I(%s) + I(%s) must equal to %d\n", 
                MyF.str(alpha), MyF.str(beta), j);
            return BigInteger.ZERO;             
        }
        if (i < 0) {
            System.out.format("The number of h can't be negative: " + i);
            return BigInteger.ZERO;
        }
        if (g > g_a(n, i, j)) {
            System.out.format("The number of nodes can't be negative: " + g);
            return BigInteger.ZERO;
        }
        // Base case. Only fiber class passing through points. 
        // from the beginning of Section 8. 
        if (i == 0 && arrOp.I(beta) == 0) { 
            if (alpha[0] == arrOp.sum(alpha) && g == 1 - j) {
                return BigInteger.ONE;    
            }
            else {
                return BigInteger.ZERO;
            }
        } 
        BigInteger ans = BigInteger.ZERO; 
        //System.out.format("Computing i = %d, j = %d, g = %d, %s, %s\n",
        //            i, j, g, MyF.str(alpha), MyF.str(beta));
        // the first term
        for (int k = 0; k < j; k++) {    
            if (beta[k] > 0) {
                ans = ans.add(first(i, g, alpha, beta, k));
            }                
        }
        // the second term
        if (i > 0) {  
            // |\beta'| = g - g' + |beta| + 1
            int bdj = g - g_a(n, i - 1, j + n) + arrOp.sum(beta) + 1;
            //System.out.format("bound = [%d, j + n], %d- %d + %d + %d\n", 
            //    bdj, g,  g_a(n, i -1, j + n), arrOp.sum(beta), 1);
            for (int k = Math.max(0, bdj); k <= j + n; k++) {
                for (byte[] bP : parArr.get(k)) {
                    for (byte[] aP : parArr.get(j + n - k)) {       
                        ans = ans.add(second(i, g, alpha, beta, aP, bP)); 
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
        int j = b + n * (a - i);                          
        byte[] tempAlpha = alpha.clone();
        byte[] tempBeta = beta.clone();
        //alpha_+e_k, beta-e_k
        tempAlpha[k] = (byte) (alpha[k] + 1);  
        tempBeta[k] = (byte) (beta[k] - 1);    
        ArrayList<Byte> key = Key.make(g, tempAlpha, tempBeta);
        if (curSave.containsKey(key)) {
            //System.out.println(curSave.get(key));
            return curSave.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else if (curDump.containsKey(key)) {
            //System.out.println(curDump.get(key));
            return curDump.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else {
            System.out.format("Finding N(%d, %d, %d, %s, %s)\n", 
                i, j, g, MyF.str(alpha), MyF.str(beta));
            System.out.format("N(%d, %d, %d, %s, %s) can't be found.\n", 
                i, j, g, MyF.str(tempAlpha), MyF.str(tempBeta));
        }
        //System.out.println(0);
        return BigInteger.ZERO;  
    }       
    /** 
     *  Computes a single term in the second term
     */
    private BigInteger second(int i, int g, byte[] alpha, byte[] beta, 
                      byte[] aP, byte[] bP) {
        int j = b + n * (a - i);                    
        if (arrOp.greater(alpha, aP) && arrOp.greater(bP, beta)) {
            byte[] gamma = arrOp.substract(bP, beta);
            int gP = g - arrOp.sum(gamma) + 1;
            // no need to check gP >= MyF.g_a(aa - 1, b) - gdiff --
            // it's proven.
            if (gP <= g_a(n, i - 1, j + n)) {                
                ArrayList<Byte> key = Key.make(gP, aP, bP);                
                if (prevMap.containsKey(key)) {
                    BigInteger coeff = MyF.prod(arrOp.J(gamma),  
                         arrOp.binom(alpha, aP), arrOp.binom(bP, beta));
                    return coeff.multiply(prevMap.get(key));
                }
                else { // Table doesn't contain this term
                    System.out.format("Finding N(%dh + %df, %d, %s, %s)\n",
                        i, j, g, MyF.str(alpha), MyF.str(beta));
                    System.out.format("N(%dh + %df, %d, %s, %s) not found.\n",
                        i - 1, j + n, gP, MyF.str(aP), MyF.str(bP));
                }              
            }
        }
        //System.out.println("Nope");
        return BigInteger.ZERO;        
    } 
    /**
     * Compute the arithmetic genus of curve class ih + jf on F_m.
     */
    private static int g_a(int m, int i, int j) {  
        return (i - 1) * (j - 1) + i * (i - 1) * m / 2;
    }
}    