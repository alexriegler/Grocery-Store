package grocery.database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * This class provides static methods for accessing information in the
 * customer table within the database.
 */
public class CustomerDatabase extends Database {
    // Customer table names
    static final String CUSTOMER_TABLE = "customer";
    static final String CID_COLUMN = "cid";
    static final String NAME_COLUMN = "customer_name";
    static final String BALANCE_COLUMN = "balance";
    static final String USERNAME_COLUMN = "username";
    static final String PASSWORD_COLUMN = "password";

    // Accessors
    /**
     * See Database.getResultSet().
     * The possible candidate keys are CID_COLUMN or USERNAME_COLUMN.
     *
     * @param candidateKey the candidate key of the table
     * @param candidateKeyValue the value of the candidate key
     * @return the result of the query
     */
    private static List<Map<String, Object>> getCustomerResult(String candidateKey,
                                                               Object candidateKeyValue)
            throws SQLException {
        String[] cKey = {candidateKey};
        Object[] cKeyValue = {candidateKeyValue};
        return getResult(CUSTOMER_TABLE, cKey, cKeyValue);
    }

    /**
     * See Database.getFirstAttributeValue().
     *
     * @param candidateKey the candidate key of the table (cid or username)
     * @param candidateKeyValue the value of the candidate key
     * @param attributeName the name of the attribute to get the value of
     * @return the value of the specified attribute
     * @throws SQLException
     */
    private static Object getCustomerAttributeValue(String candidateKey,
                                                    Object candidateKeyValue,
                                                    String attributeName)
            throws SQLException {
        String[] cKey = {candidateKey};
        Object[] cKeyValue = {candidateKeyValue};
        return getFirstAttributeValue(CUSTOMER_TABLE, cKey, cKeyValue,
                attributeName);
    }

    /**
     * Returns the customer ID associated with a username in the database.
     *
     * @param username the username of a customer
     * @return the customer's ID
     */
    public static int queryCustomerID(String username) throws SQLException {
        return (int) getCustomerAttributeValue(USERNAME_COLUMN, username,
                CID_COLUMN);
    }

    /**
     * Returns the customer name associated with a username in the database.
     *
     * @param username the username of a customer
     * @return the customer's name
     */
    public static String queryCustomerNameUsingUsername(String username)
            throws SQLException {
        return (String) getCustomerAttributeValue(USERNAME_COLUMN, username,
                NAME_COLUMN);
    }

    /**
     * Returns the customer name associated with a customer ID in the database.
     *
     * @param customerID the ID of a customer
     * @return the customer's name
     */
    public static int queryCustomerNameUsingCustomerID(int customerID)
            throws SQLException {
        return (int) getCustomerAttributeValue(CID_COLUMN, customerID,
                NAME_COLUMN);
    }

    /**
     * Returns the balance associated with a username in the database.
     *
     * @param username the username of a customer
     * @return the customer's balance
     */
    public static double queryBalanceUsingUsername(String username)
            throws SQLException {
        return ((BigDecimal) getCustomerAttributeValue(USERNAME_COLUMN,
                username, BALANCE_COLUMN)).doubleValue();
    }

    /**
     * Returns the balance associated with a customer ID in the database.
     *
     * @param customerID the ID of a customer
     * @return the customer's balance
     */
    public static double queryBalanceUsingCustomerID(String customerID)
            throws SQLException{
        return ((BigDecimal) getCustomerAttributeValue(CID_COLUMN, customerID,
                BALANCE_COLUMN)).doubleValue();
    }

    /**
     * Returns the password associated with a username in the database.
     *
     * @param username the username of a customer
     * @return the customer's password
     */
    public static String queryPasswordUsingUsername(String username)
            throws SQLException {
        return (String) getCustomerAttributeValue(USERNAME_COLUMN, username,
                PASSWORD_COLUMN);
    }

    /**
     * Returns the password associated with a customer ID in the database.
     *
     * @param customerID the ID of a customer
     * @return the customer's password
     */
    public static String queryPasswordUsingCustomerID(String customerID)
            throws SQLException {
        return (String) getCustomerAttributeValue(CID_COLUMN, customerID,
                PASSWORD_COLUMN);
    }

    // Mutators
    /**
     * Increases the balance of the customer associated with the customerID
     * by an amount.
     *
     * @param customerID the ID of the customer
     * @param amount the amount to increase the balance by
     * @throws SQLException if database exception occurs
     */
    public static void increaseBalance(int customerID, double amount)
            throws SQLException {
        String[] cidColumn = {CID_COLUMN};
        Object[] cidValue = {customerID};
        // Balance is stored as a numeric data type in the database so the
        // double parameter must be cast to BigDecimal.
        BigDecimal updateValue = BigDecimal.valueOf(amount);

        increaseAttributeValue(CUSTOMER_TABLE, cidColumn, cidValue,
                BALANCE_COLUMN, updateValue);
    }
}
