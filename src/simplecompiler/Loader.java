/*
 * Parser.java
 *
 * Created on September 29, 2007, 2:18 PM
 *
 * This parses the SML files (assemblish)
 *
 */

package simplecompiler;
//Packages used for File and Scanner
import java.util.*;
import java.io.*;
/**
 *
 * @author mcginleyr1
 */
public class Loader {
    /**********************************************
     * This is the assumed name of a "assembler like"
     * Program to have your program work you must place
     * your text file in the project folder and then run
     * the virtual machine. 
     ***********************************************/    
    File file = new File("assemble.txt");
    //Scanner definition for reading the text file later.
    Scanner sc;
     
    
    /** Creates a new instance of Parser */
    public Loader() {
        
    }
    
    public int[] loadProgram(){
        //Array that initially recieves the parsed program
        int[] temp = new int[100];
        //Current insertion index
        int location = 0;
        try{
            //Instantiating the text file scanner
            sc = new Scanner(file);
            //Loop while there is program lines
            while (sc.hasNextInt()){
                //Add the next instruction to the array
                temp[location] = sc.nextInt();
                //Increment instruction location
                location++;
            }
        //An exception is thrown if the file is not found
        //These lines catch the exception and stop the vm
        }catch(FileNotFoundException e){
            System.out.println("File Read Error");
            System.exit(1);
        }
        return temp;
    }    
}
