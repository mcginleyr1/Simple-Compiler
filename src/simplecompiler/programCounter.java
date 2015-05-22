/*
 * programCounter.java
 *
 * Created on October 2, 2007, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package simplecompiler;

import java.util.*;

/**
 *
 * @author mcginleyr1
 */
public class programCounter {
    
    Memory register= new Memory();
    Accumalator machine = new Accumalator();
    int currOpcode = 0, currValue = 0;
    /** Creates a new instance of programCounter */
    public programCounter() {
        register.loadProgram();
        while(register.hasNextRegister()){
            if(register.hasNextRegister()){
                int currReg = register.getNextRegister();
                currOpcode = currReg / 100;
                int address = currReg % 100;
                switch (currOpcode){
                    //Read - this reads from the user input into register
                    case 10:
                        Scanner sc = new Scanner(System.in);
                        int input = sc.nextInt();
                        register.setRegister(address,input);
                        break;
                    //Write - this writes register to screen
                    case 11:
                        System.out.println(register.getRegister(address));
                        break;
                    //Load - this loads a register into the accumalator
                    case 20:
                        machine.setValue(register.getRegister(address));
                        break;
                    //Store - this stores teh accumalators value into the register
                    // at the specified address.
                    case 21:
                        register.setRegister(address,machine.getValue());
                        break;
                    //Add - this adds the value from the specified register to
                    // the accumalator.
                    case 30:
                        machine.addValue(register.getRegister(address), 
                                register.getSign(address));
                        break;
                    //Subtract - this subtracts the value of the register from
                    // the accumalator.
                    case 31:
                        machine.subtractValue(register.getRegister(address), 
                                register.getSign(address));
                        break;
                    //Divide - this divides the value in the accumalator by the
                    // requested value in the register.
                    case 32:
                        machine.divideValue(register.getRegister(address), 
                                register.getSign(address));
                        break;
                    //Multiply - this multiplies the value in the accumalator
                    // by the value requested in the register.
                    case 33:
                        machine.multiplyValue(register.getRegister(address), 
                                register.getSign(address));
                        break;
                    //Branch - this sets the next executed instruction to the
                    // given register.
                    case 40:
                        register.branch(address);
                        break;
                    //Branch Neg - if the value in the accumulator is a negitive value
                    // then we will branch to the given register.  if it is not negative
                    // we continue on with execution.
                    case 41:
                        if(machine.getValue() < 0){
                            register.branchneg(address);
                        }
                        break;
                    //Branch Zero - if the value in the accumulator is zero then we branch
                    // to the register given. if it is not zero we go on executing.
                    case 42:
                        if(machine.getValue() == 0){
                            register.branchzero(address);
                        }
                        break;
                    //Halt - if the instruction is 43 we end the program.
                    case 43:
                        System.exit(1);
                        break;
                    // if the operation code given is not valid the 
                    // virtual machine halts.
                    default:
                        System.out.println("Invalid Operation");
                        System.exit(1);
                  }
            }
        }
    }
    
}
