/*
 * infixToPostfix.java
 *
 * Created on December 10, 2007, 11:20 PM
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
public class infixToPostfix {
    
    Stack<String> operators = new Stack<String>();
    String Postfix = new String();
    
    /** Creates a new instance of infixToPostfix */
    public infixToPostfix() {
    }
    
    public String convert(String line){
        StringTokenizer infix = new StringTokenizer(line," +-()/*", true);
        while(infix.hasMoreTokens()){
            String temp = infix.nextToken();
            if(temp.equals("+") || temp.equals("-")){
                if(!operators.empty() && (operators.peek().equals("*") || operators.peek().equals("/")))
                    while(!operators.empty() && !operators.peek().equals("(")){
                        Postfix = Postfix + " " + operators.pop();
                    }
                operators.push(temp);
                
            }else if(temp.equals("*") || temp.equals("/")){
                operators.push(temp);       
            }else if(temp.equals("(")){
                operators.push(temp);
            }else if(temp.equals(")")){
                while(!operators.empty() && !operators.peek().equals("(")){
                    Postfix = Postfix + " " + operators.pop();
                }
                if(operators.peek().equals("(")){
                    operators.pop();
                }
            }else{
                if(Postfix.length() > 0){
                    Postfix = Postfix + " " + temp;
                }else{
                    Postfix += temp;
                }
            }
        }
        if(!operators.empty()){
            Postfix = Postfix + " " + operators.pop();
        }
        return Postfix;
    }
    
}
