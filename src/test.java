import java.io.File; 
import java.util.Scanner; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
/**
 * The Test class runs pre-designed tests for CH and HirTable classes.
 * It compares files in src/testFiles directory with program outputs line 
 * by line. 
 * If there are mismatchs, the difference will be printed out.
 * <p>
 * It will run tests for CH class with (deg, maxNode) = (6, 15), (10, 4) and 
 * tests for HirTable class for (a, b, maxNode) = (10, 1, 3), (4, 2, 3), 
 * (6, 3, 7) and (4, 4, 10) on F0. 
 * This tests covers all numbers previously compute in Caporaso-Harris' paper
 * "Counting plane curves of any genus" p.5-6 and in Vakil's paper "Counting 
 * curves on rational surfaces" p.75 for P^1*P^1.
 * 
 * Note: this test only check when printLast = deg in CH and a + 1 in Hirtable.
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 3.0
 */
public class Test
{   
    /**
     * Run this method without argument will start the tests.
     * @param args unused
     */
    public static void main(String[] args) {
        // Select the range of testing. The answers are already in 
        // src/testFiles/.
        testCH(6, 15);     
        testCH(10, 4);     
        testHir(0, 4, 2, 3);     
        testHir(0, 4, 4, 10); 
        testHir(0, 5, 12, 10);  
        testHir(0, 6, 3, 7);    
        testHir(0, 10, 1, 3);        
        testHir(1, 2, 1, 1); 
        testHir(1, 2, 2, 2);
        testHir(1, 2, 3, 3);
        testHir(1, 2, 4, 4);
        testHir(2, 2, 0, 1);
        testHir(2, 2, 1, 2);
        testHir(2, 2, 2, 3);
        testHir(3, 2, 0, 2);
        testHir(3, 2, 1, 3);
        testHir(4, 2, 0, 3);
    } 
    /**
     * Output the comparison between test files and output files of CH.
     */
    private static void testCH(int maxdeg, int delta) {
        CH ch = new CH(maxdeg, delta);
        //ch.printLast = maxdeg;
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
        //ht.printLast = a + 1;
        ht.compute();             
        System.out.format("Testing %dh+%df and maxNode = %d: on F%d\n", 
            a, b, maxNode, n);
        for (int i = 0; i <= a; i++) {
            int j = b + n * (a - i);
            for (int r = 0; r <= maxNode; r++) {
                int g = MyF.g_a(n, i, j) - r;
                String fname = String.format("%dh+%df_g=%d.txt", i, j, g);
                String fn = "F" + n + "/";
                //System.out.println(fname);
                String dir = String.format("F%d(%d, %d, %d)/", n, a, b, maxNode);
                //System.out.println("testFiles/" +  dir + fname);
                //System.out.println("../output/Hir/F"+ n + "/" + fname);
                Boolean diffL = compare(new File("testFiles/" +  dir 
                    + fname), new File("../output/Hir/" + fn + fname));
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
            return true;
        }
        return false;
    }
}
