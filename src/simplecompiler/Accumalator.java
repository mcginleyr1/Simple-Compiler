/*
 * Accumalator.java
 *
 * Created on September 29, 2007, 2:18 PM
 *
 * This is where the data processing takes place
 *
 */

package simplecompiler;

/**
 *
 * @author mcginleyr1
 */
public class Accumalator {
    int value = 0;
    /** Creates a new instance of Accumalator */
    public Accumalator() {
    }
    
    public void nextOperation(int opCode, int address, boolean sign){
           
    }
    
    public int getValue(){
        return value;
    }
    
    public void setValue(int val){
        value = val;
    }
    
    public void addValue(int val, boolean sign){
        if(sign)
            val *= -1;
        value += val;
    }
    
    public void subtractValue(int val, boolean sign){
        if(sign)
            val *= -1;
        value -= val;
    }
    
    public void divideValue(int val, boolean sign){
        if(val == 0){
            System.out.println("Dividing by zero is not a valid operation.");
            Memory.memoryDump();
        }else if(sign){
            val *= -1;
        }else{
            value /= val;
        }
    }
    
    public void multiplyValue(int val, boolean sign){
        if(sign)
            val *= -1;
        value *= val;
    }
}
