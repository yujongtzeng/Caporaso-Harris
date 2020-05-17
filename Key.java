import java.util.ArrayList;
import java.util.Arrays;
/** The Key class has two static methods to concatenate intputs into an 
 * ArrayList of Bytes. Both methods are called make. The only difference 
 * between them is the types of inputs. 
 * @author Yu-jong Tzeng
 * @version 2.2
 * @since March 12, 2020.
 */

public class Key   
{        
    /**
     * The make method concatenates one integer and two byte[]. 
     * @param r int, need to be small enough to fit in byte type. 
     * @param alpha byte[]
     * @param beta byte[]
     * @return ArrayList<Byte> The concatenation of the inputs 
     * (no separators). 
     */
    public static ArrayList<Byte> make(int r, byte[] alpha, byte[] beta) {
        //for CH and F0Table class
        ArrayList<Byte> key = new ArrayList<Byte>();
        key.add((byte) r);  
        for (int i = 0; i < alpha.length; i++) {
            key.add(alpha[i]);
        }
        for (int i = 0; i < beta.length; i++) {
            key.add(beta[i]);
        }
        return key;        
    }     
     /**
     * The make method concatenates one integer and two ArrayList<Integers>. 
     * @param g int
     * @param alpha ArrayList<Integer>
     * @param beta ArrayList<Integer>
     * @return ArrayList<Integer> The concatenation of the inputs 
     * (no separators). 
     */    
    public static ArrayList<Integer> make(int g, 
                        ArrayList<Integer> alpha, ArrayList<Integer> beta) {
        // for HirTable class                    
        ArrayList<Integer> key = new ArrayList<Integer>();
        key.add(g);      
        key.addAll(alpha);
        key.addAll(beta);
        return key;        
    }  
}
