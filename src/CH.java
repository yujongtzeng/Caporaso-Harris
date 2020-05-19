import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.lang.StringBuilder; 
import java.math.BigInteger; 
/**
 * <p>
 * This CH class implements the recursive formula of Caporaso-Harris in 
 * the article "Counting plane curves of any genus" which can be used to 
 * find the number of r-nodal curves on the projective plane of any degree
 * d and r. We will follow the notation in Section 1.1 of the article as 
 * much as possible.
 * <p>
 * Users will be prompted to enter the maximal degree and the maximal number 
 * of nodes for the curves in concern. <br>
 * 
 * The program will print the computation results in two kinds of txt files:
 * numbers of nodal curves line by line and generating series. 
 * <p>
 * The first kind of files contain the number of degree d curves on the 
 * projective plane with r nodes (strictly speaking, geometric genus 
 * (d-1)*(d-2)/2 - r)
 * which satisfy tangency conditions (alpha, beta) with a given line for  
 * <br>
 * 1) d = the biggest 5 positive integers less or equal to max degree 
 *    (5 is the default number and can be changed by modifying the instance 
 *    variable printLast), <br>  
 * 2) all nonnegative integers r less or equal to max number of nodes, <br>   
 * 3) all valid tangency conditions alpha and beta 
 *    (valid := satisfy I(alpha)+I(beta) = d). 
 * <p>
 * alpha : tangency conditions at assigned points.  <br>
 * beta : tangency conditions at unassigned points. 
 * beta = (beta_1, beta_2,....).
 * <p> 
 * The output numbers will be located at output/CH and splitted according to 
 * degree and the number of nodes. 
 * <p>
 * The second kind of files contain the generating series of those numbers. 
 * Define the generating series on the projective plane P^2 and line
 * bundle O(d) to be <br>
 * T_{alpha}(P^2, O(d), line) = \sum_{r, beta} N(d, r, alpha, beta) z^r 
 * b^{beta_2} c^{beta_3} d^{beta_4}....<br>
 * Define the degree and weighted degree of a monomial to be <br>
 * degree(z^r b^{beta_2} c^{beta_3} d^{beta_4}....) = r + b + c + d... <br>
 * wdegree(z^r b^{beta_2} c^{beta_3} d^{beta_4}....) = 2*b + 3*c + 4*d...<br>
 * All terms in the generating series satisfying total degree <= 5 and 
 * weighted degree <= 10 will be written in files at output/genFunCH. <br>
 * The number of terms in ourput can be modified but the length of tangency 
 * conditions must <=25. 
 * <p>
 * Notes on algorithm: <br>
 * This class and F0Table use the same algorithm. 
 * @author Yu-jong Tzeng
 * @version 3.0
 * @since May 19, 2020.
 */

public class CH {
    private static int deg;
    private static int maxNode;
    private static ArrayOp arrOP;   
    /**
     * The number of top degrees which will be printed out. 
     * The output will will contain CH invariants for d = 
     * (maxNode -printLast +1) to maxNode. 
     */
    public static int printLast;  
    // wDeg is the bound of weight degree of output monomial 
    private static int wDeg;  
    // The computation will start from degree 1 to deg one by one. 
    // At stage d, prevMap contains results of degree d - 1
    // curSav contains results of d which will possibly be used for d + 1
    // curDump contains results of d which won't be used for d + 1
    // When a new stage starts, prevMap = curSave and curDump is empty
    private HashMap<ArrayList<Byte>, BigInteger> prevMap;
    private HashMap<ArrayList<Byte>, BigInteger> curSave;   
    private HashMap<ArrayList<Byte>, BigInteger> curDump;   
    private Partitions parArr; 
    /**
    * The constructor of the class.
    * @param deg The maximal degree of the curves. 
    * @param maxNode The maximal number of nodes of the curves.
    */
    public CH (int deg, int maxNode) {
        this.deg = deg;
        this.maxNode = maxNode; 
        arrOP = new ArrayOp(deg);
        parArr = new Partitions(deg);
        printLast = deg;
        wDeg = 10;        
        prevMap = new HashMap<ArrayList<Byte>, BigInteger>();                
        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
    }    
    /** 
     * This method promopt for user input, create objects then call compute().
     * Paramaters deg and maxNode are initialzed by user input. 
     * @param args Unused
     */
    public static void main(String[] args) {
        // Read from user input
        Scanner reader = new Scanner(System.in);  
        System.out.println("This program computes the number of singular " + 
                           "curves on the projective plane.");
        System.out.println("Enter the max degree of the curve:");        
        System.out.println("degree = ");
        int input1 = reader.nextInt();
        System.out.println("Enter the max number of nodes:");        
        System.out.println("maxNode = ");
        int input2 = reader.nextInt();        
        System.out.format("The output will be written in the directory" + 
                           "../output/CH and ../output/genCH\n");
        reader.close();                 
        CH ch = new CH(input1, input2);
        ch.compute();
    }    
    /** 
     * Put N(O(d), r, alpha and beta) into dictionary 
     * for given d = 1,...,deg, r = 0,...,maxNode and all valid (alpha, beta).
     * If d >= deg - printLast, write the result in the output file. 
     */
    private void compute() {
        for (int d = 1; d <= deg; d++) {
            System.out.println("Computing d = " + d);
            prevMap = curSave;
            curSave = new HashMap<ArrayList<Byte>, BigInteger>();  
            curDump = new HashMap<ArrayList<Byte>, BigInteger>();   
            if (d <= deg - printLast) {
                // Only compute N and put in dictionary
                // cur is a working dictionary. First is curDump then curSave
                HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
                for (int r = 0, j = d; r <= maxNode && j >= 0; r++, j--) {
                    // If j > maxNode then beta will be negative. 
                    // by d - 1 - j = Ibeta' >= |beta| - r + d - 1 so
                    // these numbers will not be used again for bigger degree.
                    if (j <= maxNode) { cur = curSave; }
                    for (byte[] alpha : parArr.get(j)) {
                        for (byte[] beta : parArr.get(d - j)) {
                            BigInteger ansN = N(d, r, alpha, beta);
                            cur.put(Key.make(r, alpha, beta), ansN);
                        }
                    }
                }  
            }
            else {
                for (int r = 0; r <= maxNode; r++) {
                    // Space saving feature. Since no deg + 1 terms and the
                    // first term has the same d, r, data in curDump and 
                    // curSave can be thrown away. 
                    if (d == deg) {
                        curDump = new HashMap<ArrayList<Byte>, BigInteger>();
                        curSave = new HashMap<ArrayList<Byte>, BigInteger>();
                    }
                    output(d, r);     
                }    
            }
        }       
    } 
    /** Write to output files and put results in dictionary.
     * @param d the working degree
     * @param r the working number of nodes
     */
    private void output(int d, int r) {
        try {
            String fileCH = String.format("../output/CH/O(%d)_r=%d.txt", d, r);
            String fgen = String.format("../output/genCH/O(%d)_r=%d.txt", d, r);
            File outputfile = new File(fileCH);
            File genFun = new File(fgen); 
            outputfile.getParentFile().mkdirs();
            genFun.getParentFile().mkdirs();
            PrintWriter num = new PrintWriter(outputfile, "UTF-8");
            PrintWriter gen = new PrintWriter(genFun, "UTF-8");
            HashMap<ArrayList<Byte>, BigInteger> cur = curDump;
            for (int j = d; j >= 0; j--) {
                if (j <= maxNode) { cur = curSave; }
                for (byte[] alpha : parArr.get(j)) {
                    if (j <= 4) { 
                        gen.println("\n");
                        gen.println("alpha = " + MyF.str(alpha));
                    } 
                    for (byte[] beta : parArr.get(d - j)) {
                        BigInteger ansN = N(d, r, alpha, beta);
                        cur.put(Key.make(r, alpha, beta), ansN);
                        num.printf("N(O(%d), %d, %s, %s) = %d\n", 
                            d, r, MyF.str(alpha), MyF.str(beta), ansN);
                        if (j <= 4 && d - j - beta[0] <= wDeg) {
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
    private BigInteger N(int d, int r, byte[] alpha, byte[] beta) {  
        //invalid outputs
        if (arrOP.I(alpha) + arrOP.I(beta) != d) {
            System.out.format("I(%s) + I(%s) must equal to %d\n", 
                               MyF.str(alpha), MyF.str(beta), d);
            return BigInteger.ZERO; 
        }    
        if (d <= 0) {
            System.out.format("Degree should be positive: " + d);
            return BigInteger.ZERO;
        }
        if (r < 0) {
            System.out.format("The number of nodes can't be negative: " + r);
            return BigInteger.ZERO;
        }
        //base cases
        if (d == 1 && r == 0 ) return BigInteger.ONE;
        if (d == 1 && r != 0 ) return BigInteger.ZERO;   
        
        BigInteger ans = BigInteger.ZERO;  
        // the first term
        for (int k = 0; k < deg; k++) { 
            if (beta[k] > 0) {
                ans = ans.add(first(d, r, alpha, beta, k));
            }
        }    
        // the second term
        for (int j = Math.max(0, arrOP.sum(beta) - r + d - 1); j < d; j++) {
            for (byte[] bP : parArr.get(j)) {
                for (byte[] aP : parArr.get(d - 1 - j)) {
                    ans = ans.add(second(d, r, alpha, beta, aP, bP));
                }                
            }
        }    
        return ans;                                     
    }  
    /** 
     * Computes a single term in the first term
     */
    private BigInteger first(int d, int r, byte[] alpha, byte[] beta, 
                                int k) {      
        //now d >=2        
        byte[] tempAlpha = alpha.clone();
        byte[] tempBeta = beta.clone();
        //alpha_+e_k, beta-e_k
        tempAlpha[k] = (byte) (alpha[k] + 1);  
        tempBeta[k] = (byte) (beta[k] - 1);    
        ArrayList<Byte> key = Key.make(r, tempAlpha, tempBeta);
        if (curSave.containsKey(key)) {
            return curSave.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else if (curDump.containsKey(key)) {
            return curSave.get(key).multiply(BigInteger.valueOf(k + 1));
        }
        else {
            System.out.format("Finding N(%d, %d, %s, %s)\n", 
                d, r, MyF.str(alpha), MyF.str(beta));
            System.out.format("N(%d, %d, %s, %s) can't be found.\n", 
                d, r, MyF.str(tempAlpha), MyF.str(tempBeta));
        }
        return BigInteger.ZERO;  
    }       
    /** 
     *  Computes a single term in the second term
     */
    private BigInteger second(int d, int r, byte[] alpha, byte[] beta, 
                      byte[] aP, byte[] bP) {
        if (arrOP.greater(alpha, aP) && arrOP.greater(bP, beta)) {
            byte[] gamma = arrOP.substract(bP, beta);
            int rP = r + arrOP.sum(gamma) - d + 1;
            // no need to check rP <= maxNode since 
            // r + |gamma| - d + 1 <= maxNode + |bP| -d + 1
            if (rP >= 0) {                
                ArrayList<Byte> key = Key.make(rP, aP, bP);
                if (prevMap.containsKey(key)) {
                    BigInteger coeff = MyF.prod(arrOP.J(gamma),  
                         arrOP.binom(alpha, aP), arrOP.binom(bP, beta));
                    return coeff.multiply(prevMap.get(key));
                }
                else { // Table doesn't contain this term
                    System.out.format("Finding N(%d, %d, %s, %s)\n", 
                        d, r, MyF.str(alpha), MyF.str(beta));
                    System.out.format("N(%d, %d, %s, %s) not found.\n", 
                        d - 1, rP, MyF.str(aP), MyF.str(bP));
                }              
            }
        }
        return BigInteger.ZERO;
    }    
}