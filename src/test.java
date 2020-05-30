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
 * 
 * Note: this test only check when printLast = deg in CH and b in F0table.
 * @author Yu-jong Tzeng
 * @version 2.0
 * @since May 22, 2020.
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
        testHir(0, 10, 1, 3);    //default (10, 1, 3, 1)
        testHir(0, 4, 2, 3);     //default(4, 2, 3, 2)
        testHir(0, 6, 3, 7);     //default (6, 3, 7, 3)
        testHir(0, 4, 4, 10);    //default(4, 4, 10, 4)
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
                    System.out.println(" Fail for " + fname);
                }  
            }
        }
        System.out.println("Done");
    }
    /**
     * Output the comparison between test files and output files of F0table.
     */
    private static void testHir(int n, int a, int b, int maxNode) {
        HirTable ht = new HirTable(n, a, b, maxNode);
        ht.printLast = a + 1;
        ht.compute();             
        System.out.format("Testing bidegree (%d, %d) and maxNode = %d: on F%d\n", 
            a, b, maxNode, n);
        for (int i = 0; i <= a; i++) {
            int j = b + n * (a - i);
            for (int r = 0; r <= maxNode; r++) {
                int g = g_a(n, i, j) - r;
                String fname = String.format("%dh+%df_g=%d.txt", i, j, g);
                String Fn = "F" + n + "/";
                //System.out.println(fname);
                String dir = String.format("F%d(%d, %d, %d)/", n, a, b, maxNode);
                //System.out.println("testFiles/" +  dir + fname);
                //System.out.println("../output/Hir/F"+ n + "/" + fname);
                Boolean diffL = compare(new File("testFiles/" +  dir 
                    + fname), new File("../output/Hir/"+ Fn + fname));
                if (diffL) {
                    System.out.println(" Fail for " + fname);
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
            System.out.println("There is an error in I/O for Test.java.");
        }
        return false;
    }
    private static int g_a(int m, int i, int j) {  
        return (i - 1) * (j - 1) + i * (i - 1) * m / 2;
    }
}
