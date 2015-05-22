/*
 * Register.java
 *
 * Created on September 29, 2007, 2:18 PM
 *
 * This is the Object for the register
 * 
 */

package simplecompiler;

/**
 * @author Robert McGinley
 */
public class Memory {
    /* ***************************************************
     * This is the array where the program originally gets 
     * loaded into after it is parsed from a text file 
     * and checked for negatives locally
     ******************************************************/
    private static int[] register = new int[100];
    //Corresponding to register value is set to true if number
    // is negative.
    private static boolean[] sign = new boolean[100];
    private int currentIndex = 0;
    
    /** Creates a new instance of Register */
    public Memory() {
    }
    
    public void loadProgram(){
        //Instantiate the parser class
        Loader loader = new Loader();
        //Get the program temporarily
        int[] temp = loader.loadProgram();
        //We check for negatives and set corresponding
        //sign value the make local register postitive
        for(int i = 0; i < temp.length; i++){
            if(temp[i] < 0){
                sign[i] = true;
                temp[i] *= -1;
            }else{
                sign[i] = false;
            }
            if(temp[i] > 9999 || temp[i] < -9999){
                System.out.println("Invalid input.  Length too long for register");
                System.exit(1);
            }
        }
        //Set the register to the program.
        register = temp;
    }
    
    //For iteration over the register array in program counter
    public int getNextRegister(){
        //We just get the next register and increment our location
        int temp = register[currentIndex];
        currentIndex++;
        //Return active register value.
        return temp;        
    }
    
    /*************************************
     * When we want to retrive the value
     * from a specific register for 
     * mathematics we can use this method
     *************************************/
    public int getRegister(int reg){
        if (addressCheck(reg)){
            if(sign[reg]){
                return register[reg] * -1;
            }else{
                return register[reg];
            }
        }else{
            System.exit(1);
            return 0;
        }
    }
    
    /***********************************
     * This is a simple test that checks
     * boolean sign associated with register
     * if the register is a valid register.
     * if the register is outside the address
     * space we exit program.
     ***********************************/
    public boolean getSign(int reg){
        if(reg < sign.length && reg > 0){
            return sign[reg];
        }else{
            System.out.print("Invalid register");
            System.exit(1);
        }
        //Function must return something if if and else
        //fail.  This is a Java/Netbeans constraint
        return false;
    }

    //This allows us to set a specific register.
    public void setRegister(int reg, int value){
        // If the value is larger then our word it 
        // is invalid and vm stops execution.
        if(value > 9999){
            System.out.println("Value too large");
            System.exit(1);
        // If value is too small for our word it is
        // invalid and vm stops execution.
        }else if(value < -9999){
            System.out.println("Value to small");
            System.exit(1);
        // If value is less then zero set sign
        // and make it positive to go in register.
        }else if(value < 0){
            sign[reg] = true;
            value *= -1;
            register[reg] = value;
        // Other wise just set the register
        }else{
            register[reg] = value;
        }
    }
    
    // If we branch we need to change
    // the next exectuion index.
    public void branch(int reg){
        currentIndex = reg;
    }
    
    // Made individual incase of further
    // constraints at time of original dev
    public void branchneg(int reg){
        currentIndex = reg;
    }
    
    // Again made incase I thought of constraints
    // That might be special.
    public void branchzero(int reg){
        currentIndex = reg;
    }
    
    // Doing a test to see if the next register
    // is inside the the bounds this is primarily
    // used for the dump.
    public boolean hasNextRegister(){
        if(currentIndex + 1 >= register.length)
            return false;
        else
            return true;
    }
    
    // Checking to see if the address is valid.
    // if its invalid exit.
    public boolean addressCheck(int address){
        if(address > register.length || address < 0){
            System.out.println("The address " + address + " is an invalid address");
            memoryDump();
            System.exit(1);
            return false;
        }else{
            return true;
        }
    }
    
   
    // This is for when there is an error we show the user all
    // the values in memory.
    public static void memoryDump(){
        for(int i = 0; i < register.length; i++){
            if(sign[i]){
                if((i + 1) % 5 == 0){
                    System.out.println("-" + register[i]);
                }else{
                    System.out.print("-" + register[i] + "\t");
                }
            }else{
                if((i + 1) % 5 == 0){
                    System.out.println(register[i]);
                }else{
                    System.out.print(register[i] + "\t");
                }
            }
        }
    }
}
