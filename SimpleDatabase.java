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

    // Stack containing linked lists of commands
    // Stack used for nesting of transactions
    // LinkedList containing commands used as implementation of Queue for running commands in order of entry
    
    private Stack<LinkedList<String>> transactStack;

    // Private DatabaseData object containing the data
    // DatabaseData class located in DatabaseData.java
    
    private DatabaseData theData;

    // Constructor function
    // Initializes theData and Stack
    public SimpleDatabase() {
        transactStack = new Stack<>();
        theData = new DatabaseData();
    }

    /**
     * This function first checks for transaction statments or transaction blocks
     * If neithe is found, then pass to main parsing function
     * @param command the command
     */

    public void transactCheck(String command) {
         // Push statement into Linked List if there is an item in the stack
        if (!transactStack.empty()){
            LinkedList<String> currentTransact = transactStack.pop(); // Pop the current transaction
            currentTransact.add(command); // Append the current command to  the current transaction
            transactStack.push(currentTransact); // Push the modified transaction back into the stack
            return;
        }       
        
        //Split the strings into commands 
        String[] splitCmd = command.split(" ");

        // Create a new LinkedList in the stack if statement is a begin statement
        if (splitCmd[0].equalsIgnoreCase(DBConst.BEGIN)){
            if(splitCmd.length == 1)
                transactStack.push(new LinkedList<>());
             else
                System.out.println("Syntax error for BEGIN statement");
            return;
        }
 
        // Remove last block on rollback
        if (splitCmd[0].equalsIgnoreCase(DBConst.ROLLBACK)){
            if(splitCmd.length == 1){
                // If stack is empty, return error message and abort
                if (transactStack.empty())
                    System.out.println("NO TRANSACTION");
                else // Delete the current transaction from stack
                   transactStack.pop(); 
            } else
                System.out.println("Syntax error for ROLLBACK statement");
            return;
        }

        // Iterate through the last transaction block on commit
        if (splitCmd[0].equalsIgnoreCase(DBConst.COMMIT)){
            if(splitCmd.length == 1){
                // If stack is empty, return error message and abort
                if (transactStack.empty())
                    System.out.println("NO TRANSACTION");
                else{ // Else iterate through the loop
                    LinkedList<String> currentTransact = transactStack.pop();
                    for(String currentCommand : currentTransact)
                        runCmd(currentCommand);
                }
            } else
                System.out.println("Syntax error for COMMIT statement");
            return;
        }

    
        // Else pass command to command interpreter
        runCmd(command);
    }

    /**
     * This command interprets and runs the main command
     * @param command the command to be run
     */
    public void runCmd(String command) {
        // Split the command into individual words   
        String[] splitCmd = command.split(" ");
        
        // Check for SET command
        if (splitCmd[0].equalsIgnoreCase(DBConst.SET)){
            if (splitCmd.length == DBConst.NUM_WORDS_IN_SET)
                theData.setVal(splitCmd[1], splitCmd[2]);
            else
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
            if (splitCmd.length == DBConst.NUM_WORDS_IN_UNSET)
                theData.unsetVal(splitCmd[1]);
            else
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
                myDB.transactCheck(theCommand);
        } catch(IOException e){
            System.out.println("I/O Error while reading command");
            System.exit(1);
        }
    }
}

