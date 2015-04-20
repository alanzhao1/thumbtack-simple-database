/**
 * DatabaseData.java
 * This class is a wrapper around two HashMaps used to store the database data
 *
 * @author Alan Zhao
 */

// Imports
import java.util.HashMap;

// Start of main class
public class DatabaseData {
    
    // Stores the data
    private HashMap<String, String> dbData;    
    
    // Stores the number of occurances of each data
    // This keeps NUMEQUALTO running under O(log(n)) (actually runs in constant time)
    // One other solution, to iterate through dbData, runs in O(n) 
    private HashMap<String, Integer> numValue; // Stores the number of occurances of the data


    // Constructor function
    // Initalializes hashmaps dbData and numValue
    public DatabaseData() {
        dbData = new HashMap<>();
        numValue = new HashMap<>();
    }
    
    /**
     * This function updates DatabaseData with the name/value pair
     * @param name the name of the variable
     * @param value the value of the variable
     */
    public void setVal(String name, String value) {
        
        // Insert the name, value pair into dbData
        dbData.put(name, value);

        // Update the corresponding numValue
        // If key is found corresponding to value, increment occurances by 1 
        // Else insert a new key/value pair into the HashMap 
        if (numValue.containsKey(value))
            numValue.put(value, new Integer(numValue.get(value)+1));
        else
            numValue.put(value, new Integer(1));
    }

    /**
     * This function returns the value of the variable name
     * @param name the name of the variable
     * @returns a string containing the value of the variable. Returns null if it doesn't exist
     *          which is what is done by the HashMap get() function
     */

    public String retVal(String name){
        return dbData.get(name);
    }

    /**
     * This function deletes(unsets) the variable name
     * @param name the name of the variable
     */

    public void unsetVal(String name){
        
        // Update numValue
        // If key is equal to 1 (last variable with this value)
        // Then delete this key/value pair of the HashMap
        // Else just decrement by 1
        String theValue = dbData.get(name);

        if (numValue.get(theValue).equals(1))
            numValue.remove(theValue);
        else
            numValue.put(theValue, new Integer(numValue.get(theValue)-1));

        // Now remove the variable from the database
        dbData.remove(name);
    }

    /**
     * This function returns the number of varibles with value equal
     * to a given value
     * @param value the value to check
     * @returns an Integer containing the number of occurances of value
     */

    public Integer numEqualTo(String value){
        return numValue.get(value);
    }
}
        

