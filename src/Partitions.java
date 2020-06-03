import java.util.ArrayList;
import java.util.Arrays;
/**
 * The Partitions class generates all integer partitions of nonnegative 
 * integers less or equal to n. Partitions are expressed in the notation of
 * Caporaso-Harris' paper "Counting plane curves of any genus", 
 * i.e. if k = 1*a_1 + 2*a_2 + ....+ n*a_n is a partition of k, then this 
 * partition is recorded as a byte array [a_1, a_2, ..., a_n]. 
 * Note all partitions will have a fixed length n given by user input. 
 * 
 * The input n has to be less or equal than 127 (byte maximum). 
 * 
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0
 */

public class Partitions
{
    // parArray[i] is the ArrayList consisting of all partitions of i
    private ArrayList<byte[]>[] parArray;   
    private int maxL;
    /**
     * Constructor for the class
     * 
     * @param n the upper bound of the integers for which all integer 
     * partitions will be generated. n is requied to be less or equal than
     * 127. 
     */
    public Partitions(int n)
    {
        if (n < 0 || n > 127) {
            System.out.print("The input of the Partitions class is ");
            System.out.println("requred to be betwee 0 and 127.");
        }
        else {
            parArray = new ArrayList[n + 1];
            maxL = n;
            for (int k = 0; k <= n; k++) {
                // generate all partitions of k.            
                parArray[k] = new ArrayList<byte[]>();
                initialize(k, new byte[n], 0, parArray[k]);       
            }
        }
    }
    /**
     * Return all partitions of the input number k.
     * @param k Any integer with 0 <= k <= n
     * @return All partitions of k if 0 <= k <= n. Otherwise
     * return an empty Arraylist.
     */
    public ArrayList<byte[]> get(int k)
    {
        if (k >= 0 && k < parArray.length) { return parArray[k]; }
        else { return new ArrayList<byte[]>(); }
    }
    /**
     * Generate all partitions of k. 
     * @param temp The work space. 
     * @param current The current working index.
     * @param res The storage of the result. 
     */
    private void initialize(int k, byte[] temp, int current, 
                            ArrayList<byte[]> res) {
        int remain = k - I(temp, current);
        // if finished then add to the collection
        if (remain == 0) { res.add(temp.clone()); }
        // if not finished but the maxLength is reached
        if (current == maxL) return;
        // not finished but not enough to cover next multiplicity
        if (remain < current + 1) return;
        // not finished, and length not reached
        for (int i = remain / (current + 1); i >= 0; i--) {
            temp[current] = (byte) i;            
            initialize(k, temp, current + 1, res);
        }                                            
    }
    /**
     * Print all partitions generated in the object.  
     */
    public void print() {
        for (int i = 0; i < parArray.length; i++) {
            System.out.println("Partition of " + i);
            for (byte[] par : parArray[i]) {
                System.out.println(Arrays.toString(par).replaceAll("\\s", ""));
            }                    
        }    
    }
    /**
     * Find the current weighted sum of elements in array. 
     */
    private int I(byte[] array, int index) {
        int ans = 0;        
        for (int i = 0; i < index; i++)
        {
            ans = ans + (i + 1) * array[i];
        }        
        return ans; 
    }
}
