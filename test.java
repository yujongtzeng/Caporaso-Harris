import java.io.File; 
import java.util.Scanner; 
import java.io.FileNotFoundException;  
/**
 * Set printLast to d in CH and a+1 in F0table.
 */
public class test
{
    public static void main(String[] args) {
        // Select the range of testing. Need to put test files into test/CH/ first
        // maxdegree needs to match with the length of tangency conditions in test files
        //testCH(10, 4, 1);     //default (10, 4, 1)
        //testCH(6, 15, 2);     //default (6, 15, 2)
        //testF0(10, 1, 3, 1);    //default (10, 1, 3, 1)
        //testF0(4, 2, 3, 2);     //default(4, 2, 3, 2)
        //testF0(6, 3, 7, 3);     //default (6, 3, 7, 3)
        //testF0(4, 4, 10, 4);    //default(4, 4, 10, 4)
    }    
    private static void testCH(int maxdegree, int delta, int testN) {
        CH ch = new CH(maxdegree, delta);
        ch.compute();     
        for (int d = 1; d <= maxdegree; d++) {
            for (int r = 0; r <= delta; r++) {
                String fname = "O("+ d + ")_r=" + r + ".txt";
                System.out.println(fname);
                compare(new File("test/CH" + testN +"/" + fname), new File("output/CH/" + fname));
            }
        }
    }
    /*private static void testF0(int a, int b, int delta, int testN) {
        F0table f0 = new F0table(a, b, delta);
        f0.compute();     
        for (int i = 0; i <= a; i++) {
            for (int r = 0; r <= delta; r++) {
                int g = MyF.g_a(i, b) - r;
                String fname = "O("+ i + ", " + b + ")_g=" + g + ".txt";
                System.out.println(fname);
                compare(new File("test/F0" + testN +"/" + fname), new File("output/F0/" + fname));
            }
        }
    }*/    
    private static void compare(File file1, File file2) {
        Boolean ok = true;
        try { 
             Scanner old = new Scanner(file1);
             Scanner test = new Scanner(file2);  
             while (old.hasNextLine()) {
                 String line1 = old.nextLine();
                 //System.out.println(line1);
                 String line2 = test.nextLine();
                 if (!line1.equals(line2)) {
                      System.out.println("Two lines not equal");
                      System.out.println(line1);
                      System.out.println(line2);  
                      ok = false;
                 }                        
             }        
             if (test.hasNextLine()) {
                  System.out.println("The current output has more lines");
                  ok = false;
             }       
             } catch (FileNotFoundException e) {   
                 System.out.println("can't find file" );
                 ok = false;
        }  
        if (ok) {System.out.println("OK");}              
    }
}
