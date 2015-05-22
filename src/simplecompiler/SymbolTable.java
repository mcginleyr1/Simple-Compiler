/*
 * SymbolTable.java
 *
 * Created on December 4, 2007, 6:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package simplecompiler;
import java.util.*;
import java.io.*;
/**
 *
 * @author mcginleyr1
 */
public class SymbolTable {
    
    //This array is used to construct the actual SML code
    int[] SML = new int[100];
    //This is the actual Symbol Table
    ArrayList<TableEntry> table = new ArrayList<TableEntry>();
    //Keeping track of memory locations we have placed variables
    int variables = 99;
    //Keeping track of what lines of memory we have used for code
    int location = 0;
        
    /** Creates a new instance of SymbolTable */
    public SymbolTable() {
    }
    
    
    //Processes the incomming line of Simple
    public boolean processLine(String line){
        
        //The tokenizer breaks the line up according to white space.  
        StringTokenizer scanner = new StringTokenizer(line);
        
        //Get our line number that were on.
        int lineNumber = Integer.parseInt(scanner.nextToken());
        
        //Set up our table entry for the incoming statement
        TableEntry entry = new TableEntry();
        entry.symbol(lineNumber);
        //Actual location
        entry.location(location);
        //State that it is a Line number
        entry.type('L');
        //Add the entry to the symbol table
        table.add(entry);
        
        
        //While there is data left in the scanner process
        while(scanner.hasMoreTokens()){
            
            //Store the comand in a string for testing
            String temporary = scanner.nextToken();
            
            //If the command is rem ignore it.
            if(temporary.equals("rem")){
                return false;
                
            //If the command is input search for the variable
            //to see if it exists and if it does store it to existing
            //location else enter it into new postion.
            }else if(temporary.equals("input")){
                //Entry to save command
                entry = new TableEntry();
                entry.type('C');
                entry.location(location);
                entry.symbol(lineNumber);
                //Add it to symbol table
                table.add(entry);
                
                //Save the first part of the SML to array
                SML[location] = 1000;
                
                //make sure there is only one varible to put to
                if(scanner.countTokens() == 1){
                    
                    //Instantiate new entry to use
                    entry = new TableEntry();
                    
                    //Hold variable for testing
                    String hold = scanner.nextToken();
                    
                    //Search the Symbol table fore the variable
                    int varLocation = this.search(hold.charAt(0));
                    
                    //If the variable exists place the value in 
                    //The existing spot
                    if(varLocation > -1){
                        entry.symbol(hold.charAt(0));
                        entry.type('V');
                        entry.location(varLocation);
                        //variables--;  -- testing
                        //Complete the sml
                        SML[location] += entry.location();
                    
                    //If it doesnt exist add it in new spot
                    }else{
                        entry.symbol(hold.charAt(0));
                        entry.type('V');
                        //Setting the new location
                        entry.location(variables);
                        //Setting location as marked
                        variables--;
                        
                        //Completing SML line
                        SML[location] += entry.location();
                    }
                    
                    //Add entry to symbol table
                    table.add(entry);
                }
                
                //Set SML line as done
                location++;
                
                //We do not need to return to this line.
                return false;
                
            //Testing to see if the command is a let.
            }else if(temporary.equals("let")){

                //The character on the left of the equal sign
                char toWrite = scanner.nextToken().charAt(0);
                
                //Testing to see if this character exists in memory alread.
                if(this.search(toWrite) == -1){
                    //If character doesn't exist create its
                    //new entry in the symbol table.
                    entry = new TableEntry();
                    entry.type('V');
                    entry.location(variables);
                    entry.symbol(toWrite);
                    //Add entry to symbol table
                    table.add(entry);
                    //Set memory as used
                    variables--;
                }
                
                //Get the next token for testing.
                char equalTest = scanner.nextToken().charAt(0);
                //If the token is an = and the let is not just assigning
                //a single value to a variable then we need arithmatic.
                if(equalTest == '=' && scanner.countTokens() > 1){
                    //Need to go to postfix so gather up our infix string
                    String toConvert = new String();
                    while(scanner.hasMoreTokens()){
                        toConvert += scanner.nextToken();
                    }
                    
                    //Now that we have the infix string conver it to postfix.
                    infixToPostfix converter = new infixToPostfix();
                    String postfix = converter.convert(toConvert);
                    
                    //Now that we have the converted string tokenize it so
                    //we can easily work with it.
                    StringTokenizer post = new StringTokenizer(postfix);
                    
                    //Keeping track of how many variables we have in the array
                    //I'm kind of using the array as a stack but not really
                    int on = 0; 
                    //Make sure we have more then enough spots in the array
                    int[] arith = new int[post.countTokens()];
                    
                    //Now process the tokens while there are some left
                    while(post.hasMoreTokens()){
                        //get first token
                        String token = post.nextToken();
                        //see if its a constant.
                        if(this.testIfNumber(token)){
                            
                            //if this constanst already exists use existing
                            if(this.search(Integer.parseInt(token)) >= 0){
                                //Get the constants postion in SML
                                int loc = this.search(Integer.parseInt(token));
                                entry.location(loc);
                                entry.symbol(Integer.parseInt(token));
                                entry.type('V');
                                //Keeping the address for math in the array
                                arith[on] = loc;
                                //Increment that we've used this spot
                                on++;

                            //If the constant doesn't arleady exist create it
                            }else{
                                entry = new TableEntry();
                                entry.location(variables);
                                entry.symbol(Integer.parseInt(token));
                                entry.type('V');
                                SML[variables] = Integer.parseInt(token);
                                arith[on] = variables;
                                variables--;
                                on++;
                            }
                        }else if(token.equals("*")){
                            entry = new TableEntry();
                            entry.symbol('*');
                            entry.type('C');
                            entry.location(location + 1);
                            TableEntry entry2 = new TableEntry();
                            entry2.location(location);
                            entry2.symbol();
                            entry2.type('C');
                            SML[location] = 2000;
                            SML[location] += arith[--on];
                            location++;
                            SML[location] = 3300;
                            SML[location] += arith[on - 1];
                            location++;
                            SML[location] = 2100 + arith[on - 1];
                            location++;
                            table.add(entry);
                            table.add(entry2);
                        }else if(token.equals("/")){
                            entry = new TableEntry();
                            entry.symbol('/');
                            entry.type('C');
                            entry.location(location + 1);
                            TableEntry entry2 = new TableEntry();
                            entry2.location(location);
                            entry2.symbol();
                            entry2.type('C');
                            SML[location] = 2000;
                            SML[location] += arith[on - 2];
                            location++;
                            SML[location] = 3200;
                            SML[location] += arith[on - 1];
                            location++;
                            SML[location] = 2100;
                            SML[location] += arith[on -1];
                            location++;
                            table.add(entry);
                            table.add(entry2);
                            on--;
                        }else if(token.equals("+")){
                            entry = new TableEntry();
                            entry.symbol('+');
                            entry.type('C');
                            entry.location(location + 1);
                            TableEntry entry2 = new TableEntry();
                            entry2.location(location);
                            entry2.symbol();
                            entry2.type('C');
                            SML[location] = 2000;
                            SML[location] += arith[--on];
                            location++;
                            SML[location] = 3000;
                            SML[location] += arith[on - 1];
                            location++;
                            SML[location] = 2100;
                            SML[location] += arith[on -1];
                            location++;
                            table.add(entry);
                            table.add(entry2);
                            
                        }else if(token.equals("-")){
                            entry = new TableEntry();
                            entry.symbol('-');
                            entry.type('C');
                            entry.location(location + 1);
                            TableEntry entry2 = new TableEntry();
                            entry2.location(location);
                            entry2.symbol();
                            entry2.type('C');
                            SML[location] = 2000;
                            SML[location] += arith[on-2];
                            location++;
                            SML[location] = 3100;
                            SML[location] += arith[on - 1];
                            location++;
                            SML[location] = 2100;
                            SML[location] += arith[on -1];
                            location++;
                            table.add(entry);
                            table.add(entry2);
                            on--;
                            
                        }else{
                            entry = new TableEntry();
                            if(this.search(token.charAt(0)) > -1)
                            {
                                entry.location(this.search(token.charAt(0)));
                                entry.symbol(token.charAt(0));
                                entry.type('V');
                                table.add(entry);
                                arith[on] = entry.location();
                                on++;
                            }else{
                                entry.location(location);
                                entry.symbol(token.charAt(0));
                                entry.type('V');
                                table.add(entry);
                                arith[on] = entry.location();
                                on++;
                            }
                            
                        }
                        
                    }
                }else if(scanner.countTokens() == 1){
                    String right = scanner.nextToken();
                    if(this.testIfNumber(right)){
                        SML[this.search(toWrite)] = Integer.parseInt(right);
                    }else{
                        SML[this.search(toWrite)] = SML[this.search(right.charAt(0))];
                    }
                    return false;
                }
                
                
                SML[location] = 2100;
                int varLoc = this.search(toWrite);
                entry = new TableEntry();
                if(varLoc == -1){
                    entry.location(variables);
                    entry.symbol(toWrite);
                    entry.type('V');
                    table.add(entry);

                    variables--;
                }else{
                    entry.location(varLoc);
                    entry.symbol(toWrite);
                    entry.type('V');
                    table.add(entry);
                }
                SML[location] += entry.location();
                location++;
                return false;
            }else if(temporary.equals("print")){
                //Create new table entry for the command;
                entry = new TableEntry();
                entry.type('C');
                entry.symbol(lineNumber);
                //Set the location of SML
                entry.location(location);
                //Set the SML up
                SML[location] = 1100;
                //Add the entry to Sybol table
                table.add(entry);
                
                //get the variable that needs to be printed
                char sym = scanner.nextToken().charAt(0);
                
                //Find the actual SML location of the variable
                int toPrint = this.search(sym);
                //Create entry for the variable printing
                entry = new TableEntry();
                entry.type('V');
                entry.location(toPrint);
                entry.symbol(sym);

                //Add the location of the variable to print to SML
                SML[location] += toPrint;

                //Add entry to Symbol Table
                table.add(entry);

                //We've used this loction so mark it used
                location++;
                
                //We don't need to revisit this line
                return false;
            }else if(temporary.equals("goto")){
                entry.type('L');
                entry.symbol(lineNumber);
                entry.location(location);
                SML[location] = 4000;
                // Need to finish resolving the goto at the second pass.
                location++;
                table.add(entry);
                return true;

            }else if(temporary.equals("if")){
                entry.type('L');
                entry.symbol(lineNumber);
                entry.location(location);
                table.add(entry);
                //Now test expression
                String one = scanner.nextToken();
                String compare = scanner.nextToken();
                String two = scanner.nextToken();
                entry = new TableEntry();
                if(this.search(one.charAt(0)) > -1){
                    entry.type('V');
                    entry.symbol(one.charAt(0));
                    entry.location(this.search(one.charAt(0)));
                }else{
                    entry.type('V');
                    entry.symbol(one.charAt(0));
                    entry.location(variables);
                    variables--;
                }
                table.add(entry);
                TableEntry entry2 = new TableEntry();
                if(this.search(two.charAt(0)) > -1){
                    entry2.type('V');
                    entry2.symbol(two.charAt(0));
                    entry.location(this.search(two.charAt(0)));
                }else{
                    entry2.type('V');
                    entry2.symbol(two.charAt(0));
                    entry.location(variables);
                    variables--;
                }
                table.add(entry);
//                SML[location] = 2000;
//                SML[location] += this.search(one.charAt(0));
                if(compare.equals(">")){
                    entry = new TableEntry();
                    entry.location(location);
                    entry.symbol(compare.charAt(0));
                    entry.type('C');
                    SML[location] = 2000;
                    SML[location] += this.search(two.charAt(0));
                    location++;
                    SML[location] = 3100;
                    SML[location] += this.search(one.charAt(0));
                    location++;
                    SML[location] = 4100;
                    location++;
                }else if(compare.equals("<")){
                    entry = new TableEntry();
                    entry.location(location);
                    entry.symbol(compare.charAt(0));
                    entry.type('C');
                    SML[location] = 2000;
                    SML[location] += this.search(one.charAt(0));
                    location++;
                    
                    SML[location] = 3100;
                    SML[location] += this.search(two.charAt(0));
                    location++;
                    SML[location] = 4100;
                    location++;
                    
                }else if(compare.equals(">=")){
                    entry = new TableEntry();
                    entry.location(location);
                    entry.symbol(compare.charAt(0) + compare.charAt(1));
                    SML[location] = 2000;
                    SML[location] = this.search(two.charAt(0));
                    location++;
                    SML[location] = 3100;
                    SML[location] += this.search(one.charAt(0));
                    location++;
                    SML[location] = 4100;
                    location++;
                    SML[location] = 4200;
                    location++;
                    
                }else if(compare.equals("<=")){
                    entry = new TableEntry();
                    entry.location(location);
                    entry.symbol(compare.charAt(0)+compare.charAt(1));
                    SML[location] = 2000;
                    SML[location] = this.search(one.charAt(0));
                    location++;
                    SML[location] = 3100;
                    SML[location] += this.search(two.charAt(0));
                    location++;
                    SML[location] = 4100;
                    location++;
                    SML[location] = 4200;
                    location++;
                }else if(compare.equals("==")){
                    entry = new TableEntry();
                    entry.location(location);
                    entry.symbol(compare.charAt(0) + compare.charAt(1));
                    entry.type('C');
                    SML[location] = 2000;
                    SML[location] += this.search(one.charAt(0));
                    location++;
                    SML[location] = 3100;
                    SML[location] += this.search(two.charAt(0));
                    location++;
                    SML[location] = 4200;
                    location++;
                    
                }
                return true;
            }else if(temporary.equals("end")){
                entry.type('C');
                entry.symbol(lineNumber);
                entry.location(location);
                SML[location] = 4300;
                location++;
                table.add(entry);
//                
            }
        }

//        table[location] = entry;
        return false;
    }
    
    public void secondPass(String line){
        StringTokenizer p2 = new StringTokenizer(line);
        int origLine = Integer.parseInt(p2.nextToken());
        int smlIndexLook = this.findLine(origLine);
        while(p2.hasMoreTokens()){
            String temp = p2.nextToken();
            if(temp.equals("if")){
                p2.nextToken();
                String type = p2.nextToken();
                p2.nextToken();
                if(type.equals(">") || type.equals("<") || type.equals("==")){
                    p2.nextToken();
                    int togoto = Integer.parseInt(p2.nextToken());
                    int smlOrig = this.findLine(origLine);
                    int smlGoTo = this.findLine(togoto);
                    SML[smlOrig] += smlGoTo;
                }else if(type.equals(">=") || type.equals("<=")){
                    p2.nextToken();
                    int togoto = Integer.parseInt(p2.nextToken());
                    int smlOrig = this.findLine(origLine);
                    int smlGoTo = this.findLine(togoto);
                    SML[smlOrig] += smlGoTo;
                    SML[smlOrig + 1] += smlGoTo;
                }
                
            }else if(temp.equals("goto")){
                int togoto = Integer.parseInt(p2.nextToken());
                int smlOrig = this.findLine(origLine);
                int smlGoTo = this.findLine(togoto);
                SML[smlOrig] += smlGoTo;
            }
        }
        
    }
    
    
    
    //This function saves the SML Array to a file named assemble.txt
    public void saveTable(String[] args){
        
        //Set print stream to null for testing later
        PrintStream assembled = null;
        
        //Instantiation of printstream needs to be in try incse it fails
        try{
            //Create a FileOutPutStream and pass it into constructor of PrintStream
            assembled = new PrintStream(new FileOutputStream("assemble.txt"));
        }catch(IOException e){
            //Catch exception and tell user.
            System.out.println("Unable to create save file");
        }
        //If the PrintStream instantiation worked print everything to the file
        if(assembled != null){
            for(int i = 0; i <SML.length; i++){
                //Printing to file (like System.out.println();
                assembled.println(SML[i]);
            }
            //Close the file to free it.
            assembled.close();
        }
        
        //Running the virtual machine after telling user
        System.out.println("Now instantiating virtual machine and running program:");
        //Calling the virtual machines main method to start it.
        VirtualMachineMain.main(args);
    }
    
    
    //Search for a variable
    public int search(char temp){
        //Using java's new foreach to iterate over the SymbolTable
        for(TableEntry entry : table){
            //If the symbol is the same and the type is a variable return the location
            if(entry.symbol() == temp && entry.type() == 'V'){
                return entry.location();
            }
        }
        //otherwise -1
        return -1;
    }
    
    //Search for constants
    public int search(int temp){
        //Use foreach to iterate over SymbolTable
        for(TableEntry entry : table){
            //If same constant and variable return location
            if(entry.symbol() == temp && entry.type() == 'V'){
                return entry.location();
            }
        }
        //otherwise -1
        return -1;
    }
    
    
    //Search for linenumber
    public int findLine(int lineNum){
        //Iterate over SymbolTable
        for(TableEntry entry : table){
            //If same linenumber and type is line return location
            if(entry.symbol() == lineNum && entry.type() == 'L'){
                return entry.location();
            }
        }
        //otherwise -1
        return -1;
    }
    
    //Testing if the string is a number (aka constant)
    public boolean testIfNumber(String theString){
        //Set true false if any part of string isn't a digit
        boolean is = true;
        //Loop over string
        for(int i = 0; i < theString.length(); i++){
            //If the current index isn't a digit set the boolean false
            if(!Character.isDigit(theString.charAt(i))){
                is = false;
            }
        }
        //return the result
        return is;
    }
    

}
