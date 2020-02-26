import java.util.ArrayList;
import java.util.Arrays;

/** The Key class has three static methods to concatenate intputs into an 
 * ArrayList of Integer. All methods are called make. The only difference 
 * is they accept different inputs. 
 * @author Yu-jong Tzeng
 * @version 2.1
 * @since August 24, 2019.
 */

public class Key
{ 
    /**
     * The make method concatenate three integers and two ArrayList<Integers>. 
     * 
     * @param a int
     * @param b int
     * @param g int
     * @param alpha ArrayList<Byte>
     * @param beta ArrayList<Byte>
     * @return ArrayList<Byte> The concatenation of the inputs 
     * (no separators). 
     */    
    public static ArrayList<Byte> make(int a, int b, int g, 
                        ArrayList<Byte> alpha, ArrayList<Byte> beta)
    {
        ArrayList<Byte> key = new ArrayList<Byte>();
        key.add((byte) a); //not needed
        key.add((byte) b); //not needed
        key.add((byte) g);      
        key.addAll(alpha);
        key.addAll(beta);
        return key;        
    }      
    
    /**
     * The make method concatenate three integers and two ArrayList<Integers>. 
     * 
     * @param a int
     * @param b int
     * @param g int
     * @param alpha int[]
     * @param beta int[]
     * @return ArrayList<Byte> The concatenation of the inputs 
     * (no separators). 
     */
    public static ArrayList<Byte> make(int a, int b, int g, 
                                   byte[] alpha, byte[] beta) {
        ArrayList<Byte> key = new ArrayList<Byte>();
        key.add((byte) a); //not needed
        key.add((byte) b); //not needed
        key.add((byte) g);      
        for (int i = 0; i < alpha.length; i++) {
            key.add(alpha[i]);
        }
        for (int i = 0; i < beta.length; i++) {
            key.add(beta[i]);
        }
        return key;        
    }   
    
    /**
     * The make method concatenate one integer and two int[]. 
     * 
     * @param r int
     * @param alpha int[]
     * @param beta int[]
     * @return ArrayList<Byte> The concatenation of the inputs 
     * (no separators). 
     */
    public static ArrayList<Byte> make(int r, byte[] alpha, byte[] beta) {
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
