/*
 * TableEntry.java
 *
 * Created on December 11, 2007, 12:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package simplecompiler;

/**
 *
 * @author mcginleyr1
 */
public class TableEntry {
    
    //ASCII
    int symbol;
    // 'C', 'L', or 'V'
    char type;
    //SML location
    int location;
    
    /** Creates a new instance of TableEntry */
    public TableEntry() {
    }
    
    //Sets the symbol variable
    public void symbol(int t){
        symbol = t;
    }
    
    //Sets the type variable
    public void type(char t){
        type = t;
    }
    
    
    //Sets the location variable
    public void location(int t){
        location = t;
    }
    
    //Returns the symbol to call
    public int symbol(){
        return symbol;
    }
    
    //Returns type to call
    public char type(){
        return type;
    }
    
    //Returns location to call
    public int location(){
        return location;
    }
}
