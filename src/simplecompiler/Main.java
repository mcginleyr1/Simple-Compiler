/*
 * Main.java
 *
 * Created on December 4, 2007, 6:33 PM
 *
 * NOTE: Compiled Program is saved to assemble.txt in project folder
 */

package simplecompiler;
import java.util.*;
import java.io.*;

/**
 *
 * @author mcginleyr1
 */
public class Main {
    

    /** Creates a new instance of Main */
    public Main() {     
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // *** Local Variables ***
        Scanner compile; //This will read the file being compiled    
        Scanner sc; //This is used for the file name to compile
       
        //This keeps track of what needs a second passover.
        Boolean[] secondPass = new Boolean[100];  
        int on = 0; //Counter for what line of the file where on.
       
        // The symbol table that is compiling the program.
        SymbolTable SymTbl = new SymbolTable();
        // *** END Local Variables ****
        
        
        
        // I decided to prompt the user to so they may
        // Speicfy which file they want to compile.
        sc = new Scanner(System.in);
        System.out.println("Enter File Name to Compile:");
        String filename = sc.next();
        // Create the file object so it may be opened
        File file = new File(filename);
        
        // Doing all the file reads in a try and catch because
        // if they file does not exist there will be an exception.
        try{
            compile = new Scanner(file);
            while(compile.hasNextLine()){
                if(SymTbl.processLine(compile.nextLine())){
                    secondPass[on] = true;
                }else{
                    secondPass[on] = false;
                }
                on++;
            }
            //Close the file so you can start over from beginning.
            compile.close(); 
         
         //This section catches the file opening exception if
         // it is thrown and prompts the user with which pass
         // the exception occured.
         }catch(FileNotFoundException e){
            System.out.println("File Read Error Pass 1");
            System.exit(1);
         }
        
        // I am doing the second pass in its own try and catch
        // So that debugging is much easer if there is a compile
        // failure.
         try{
            //Start our second pass
            compile = new Scanner(file);
            
            //Reset the line counter to zero
            on = 0;
            //Loop over the file while there is still lines to read
            while(compile.hasNextLine()){
                //If the line needs to be reread on the second pass
                //send it to the secondPass method of SymbolTable
                if(secondPass[on]){
                    SymTbl.secondPass(compile.nextLine());
                
                //Other wise ignore it
                }else{
                    compile.nextLine();
                }
                on++;
            }
            //Close the file since we don't need it.
            compile.close();
            //Save the assembled program.
            //FILE IS SAVED TO assemble.txt
            SymTbl.saveTable(args);
            
        //This section catches the file opening exception if
        // it is thrown and prompts the user with which pass
        // the exception occured.
        }catch(FileNotFoundException e){
            System.out.println("File Read Error Pass 2");
            System.exit(1);
        }
    }
    
}
