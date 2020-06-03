import java.util.ArrayList;
import java.util.Arrays;
/** The Key class has a static method which concatenates inputs into an 
 * ArrayList. 
 * @author Yu-jong Tzeng
 * @version 4.0
 * @since 2.0
 */

public class Key   
{        
    /**
     * The make method concatenates one integer and two byte[]. 
     * @param r An integer small enough to fit in byte type. 
     * @param alpha Any byte[]
     * @param beta Any byte[]
     * @return The concatenation of the inputs as an ArrayList<Byte>.
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
}
