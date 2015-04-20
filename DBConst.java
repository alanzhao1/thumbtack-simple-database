/**
 * DBConstants class
 * Centralizes the constant values needed for the program
 * All members are static so we don't have to create the object
 * @author Alan Zhao
 */

public class DBConst {
    
    // Names of data commands
    static final String SET = "SET";
    static final String GET = "GET";
    static final String UNSET = "UNSET";
    static final String NUMEQUALTO = "NUMEQUALTO";
    static final String END = "END";

    // Names of transaction commands
    static final String BEGIN = "BEGIN"; 
    static final String ROLLBACK = "ROLLBACK";
    static final String COMMIT = "COMMIT";

    // Number of words in data commands
    static int NUM_WORDS_IN_SET = 3;
    static int NUM_WORDS_IN_GET = 2;
    static int NUM_WORDS_IN_UNSET = 2;
    static int NUM_WORDS_IN_NUMEQUALTO = 2;
    static int NUM_WORDS_IN_END = 1;
}
