/*
 * Main.java
 *
 * Author Robert McGinley
 *
 * Created on September 26, 2007, 12:58 AM
 *
 * This Class is the main class and is first to execute.
 * The Entire SML Virutal Machine is part of the smlvirtualmachine 
 * package.
 *
 */

package simplecompiler;

public class VirtualMachineMain{
    
    /** Constructor for the Main Class 
     * this will start to let the program execute
     */
    public VirtualMachineMain() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //This instantiates the program count which begins execution.
        programCounter pc = new programCounter();
    }
    
}
