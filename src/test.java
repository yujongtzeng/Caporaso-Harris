import java.io.File; 
import java.util.Scanner; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
/**
 * The Test class runs pre-designed tests for CH and F0table classes.
 * It compares files in testFiles directory with program outputs line 
 * by line. 
 * If there are mismatchs, the difference will be printed out.
 * <p>
 * It will run tests for CH class with (deg, maxNode) = (6, 15), (10, 4) and 
 * tests for F0table class for (a, b, maxNode) = (10, 1, 3), (4, 2, 3), 
 * (6, 3, 7) and (4, 4, 10). 
 * This tests covers all numbers previously compute in Caporaso-Harris' paper
 * "Counting plane curves of any genus" p.5-6 and in Vakil's paper "Counting 
 * curves on rational surfaces" p.75 for P^1*P^1.

 * @author Yu-jong Tzeng
 * @version 2.0
 * @since May 21, 2020.
 */
public class Test
{   
    /**
     * Run this method without argument will start the tests.
     * @param args unused
     */
    public static void main(String[] args) {
        // Select the range of testing. Need to put test files into 
        // testFiles/ first.
        testCH(6, 15);     //default (10, 4, 1)
        testCH(10, 4);     //default (6, 15, 2)
        testF0(10, 1, 3);    //default (10, 1, 3, 1)
        testF0(4, 2, 3);     //default(4, 2, 3, 2)
        testF0(6, 3, 7);     //default (6, 3, 7, 3)
        testF0(4, 4, 10);    //default(4, 4, 10, 4)
    } 
    /**
     * Output the comparison between test files and output files of CH.
     */
    private static void testCH(int maxdeg, int delta) {
        CH ch = new CH(maxdeg, delta);
        ch.printLast = maxdeg;
        ch.compute();     
        System.out.format("Testing maxdeg = %d and maxNode = %d:\n", 
            maxdeg, delta);
        for (int d = 1; d <= maxdeg; d++) {
            for (int r = 0; r <= delta; r++) {
                String fname = String.format("O(%d)_r=%d.txt", d, r);
                //System.out.println(fname);
                String dir = String.format("CH(%d, %d)/", maxdeg, delta);
                Boolean diffL = compare(new File("testFiles/" +  dir 
                    + fname), new File("../output/CH/" + fname));
                if (diffL) {
                    System.out.println(" for " + fname);
                }  
            }
        }
        System.out.println("Done");
    }
    /**
     * Output the comparison between test files and output files of F0table.
     */
    private static void testF0(int a, int b, int maxNode) {
        F0table f0 = new F0table(a, b, maxNode);
        f0.printLast = a + 1;
        f0.compute();     
        System.out.format("Testing bidegree (%d, %d) and maxNode = %d:\n", 
            a, b, maxNode);
        for (int i = 0; i <= a; i++) {
            for (int r = 0; r <= maxNode; r++) {
                int g = MyF.g_a(i, b) - r;
                String fname = String.format("O(%d, %d)_g=%d.txt", i, b, g);
                //System.out.println(fname);
                String dir = String.format("F0(%d, %d, %d)/", a, b, maxNode);
                Boolean diffL = compare(new File("testFiles/" +  dir 
                    + fname), new File("../output/F0/" + fname));
                if (diffL) {
                    System.out.println(" for " + fname);
                }  
            }
        }
        System.out.println("Done");
    }
    /**
     * Print the difference of two files. Return True if the two files
     * have different number of lines. 
     */
    private static Boolean compare(File file1, File file2) {
        try { 
            Scanner ans = new Scanner(file1);
            Scanner test = new Scanner(file2);  
            while (ans.hasNextLine() && test.hasNextLine()) {
                String line1 = ans.nextLine();
                String line2 = test.nextLine();
                if (!line1.equals(line2)) {
                     //System.out.println("Two lines not equal");
                    System.out.println(line1);
                    System.out.println(line2);                   
                }                        
            }        
            if (ans.hasNextLine() || test.hasNextLine()) {
                System.out.print("Different number of lines"); 
                return true;    
            }       
        } 
        catch (IOException e) {
            System.out.println("There is an error in I/O.");
        }
        return false;
    }
}
