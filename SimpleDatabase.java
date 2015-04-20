/**
 * SimpleDatabase.java
 * Main class for SimpleDatabase implementation
 *
 * @author Alan Zhao
 */


// Imports
import java.util.LinkedList;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Start of main class
public class SimpleDatabase {

    // Stack containing reverse commands 
    
    private Stack<String> transactStack;

    // Private DatabaseData object containing the data
    // DatabaseData class located in DatabaseData.java
    
    private DatabaseData theData;
    
    // Private boolean containing letting us know if we should commit all
    // transactions
    private boolean CommitAll; 

    // Constructor function
    // Initializes theData, Stack, and Boolean
    public SimpleDatabase() {
        transactStack = new Stack<>();
        theData = new DatabaseData();
        CommitAll = true;
    }

   /**
     * This command interprets and runs the main command
     * It also handles the processing of transactions using
     * the stack
     * @param command a string containing the command to be run
     */
    public void runCmd(String command) {
         
        // Split the command into individual words   
        String[] splitCmd = command.split(" ");

        // Create a new LinkedList in the stack if statement is a begin statement
        if (splitCmd[0].equalsIgnoreCase(DBConst.BEGIN)){
            if(splitCmd.length == 1){
                transactStack.push(new String(DBConst.BEGIN));
                CommitAll = false;
             } else
                System.out.println("Syntax error for BEGIN statement");
            return;
        }
 
        // On rollback, execute reversible commands
        if (splitCmd[0].equalsIgnoreCase(DBConst.ROLLBACK)){
            if(splitCmd.length == 1){
                // If stack is empty, return error message and abort
                if (transactStack.empty())
                    System.out.println("NO TRANSACTION");
                else {// Delete the current transaction from stack
                    String theCommand = transactStack.pop();
                    CommitAll = true; // Set flag
                    
                    // Loop until BEGIN statement is reached;
                    while(!theCommand.equals(DBConst.BEGIN)){
                        runCmd(theCommand);
                        theCommand = transactStack.pop();
                    }

                    // Reset the CommitAll flag if the stack is now empty
                    if (transactStack.empty())
                    CommitAll = true;
                }
 
            } else
                System.out.println("Syntax error for ROLLBACK statement");
            return;
        }

        // Remove reversible transactions on commit 
        if (splitCmd[0].equalsIgnoreCase(DBConst.COMMIT)){
            if(splitCmd.length == 1){
                // If stack is empty, return error message and abort
                if (transactStack.empty())
                    System.out.println("NO TRANSACTION");
                else{ // Else pop items and remove 
                    String theCommand = new String(""); 
                    while(!theCommand.equals(DBConst.BEGIN)){
                        theCommand = null;
                        theCommand = transactStack.pop();
                    } 
                    
                    // Reset the CommitAll flag if the stack is now empty
                    if (transactStack.empty())
                        CommitAll = true;
                }
            } else
                System.out.println("Syntax error for COMMIT statement");
            return;
        }


        // Check for SET command
        if (splitCmd[0].equalsIgnoreCase(DBConst.SET)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_SET){
                
                //Create a reverse command if we are in a transaction
                if(!CommitAll){
                     
                    // If this variable did not exist before
                    // Create a transaction that will unset the variable
                    if (theData.getVal(splitCmd[1])==null)
                        transactStack.push(new String("UNSET" + " " +splitCmd[1]));
                    
                    // Else create a set command that sets to the original value
                    else
                        transactStack.push(new String("SET" + " " + splitCmd[1]+" " + theData.getVal(splitCmd[1])));
                }    
                theData.setVal(splitCmd[1], splitCmd[2]);
            }else
                System.out.println("Syntax error for SET statement");
            return;
        }            
        
        // Check for GET command
        if (splitCmd[0].equalsIgnoreCase(DBConst.GET)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_GET){
                String theValue = theData.getVal(splitCmd[1]);
                if (theValue == null)
                    System.out.println("NULL");
                else
                    System.out.println(theValue);
            }
            else
                System.out.println("Syntax error for GET statement");
            return;
        }

        // Check for UNSET command
        if (splitCmd[0].equalsIgnoreCase(DBConst.UNSET)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_UNSET){
            
                // If we are currently in a transaction
                // Push a command that sets the original variable/value pair into the stack
                if(!CommitAll)
                    transactStack.push(new String("SET" + " " + splitCmd[1]+" " + theData.getVal(splitCmd[1])));
            
                theData.unsetVal(splitCmd[1]);
            }else
                System.out.println("Syntax error for UNSET statement");
        return;
        }            

        // Check for NUMEQLTO
        if (splitCmd[0].equalsIgnoreCase(DBConst.NUMEQUALTO)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_NUMEQUALTO)
                System.out.println(theData.numEqualTo(splitCmd[1]).toString());
            else
                System.out.println("Syntax error for NUMEQUALTO statement");
            return;
        }

        // Check for END
        if (splitCmd[0].equalsIgnoreCase(DBConst.END)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_END)
                System.exit(0);
            else
                System.out.println("Syntax error for END statement");
            return;
        }

        // Else command not recognized
        System.out.println("Command not recognized");
        
    }

    // Main function
    public static void main (String args[]) {
        // Create the object
        SimpleDatabase myDB = new SimpleDatabase();
        String theCommand;

        // Create a bufferedReader
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){

            while((theCommand = br.readLine())!=null) // Should catch EOF
                myDB.runCmd(theCommand);
        } catch(IOException e){
            System.out.println("I/O Error while reading command");
            System.exit(1);
        }
    }
} 

