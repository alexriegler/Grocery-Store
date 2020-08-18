package grocery.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    // Database connection information
    static final String URL = "jdbc:postgresql://localhost/amm";
    static final String USERNAME = "postgres";
    static final String PASSWORD = "password";

    /**
     * Update types enum class. Used by generateUpdateQuery().
     */
    private enum UpdateType {
        INCREASE, DECREASE, CHANGE
    }

    // Query generators
    /**
     * Generates a simple predicate for use in an SQL where clause. The
     * predicate has the following form.
     *
     *      {attributes[0]} = ? AND
     *      {attributes[1]} = ? AND
     *      ...
     *
     * The predicate is returned as a String.
     *
     * @param attributes the attribute(s) of the table used to identify row(s)
     * @return a simple SQL where clause
     */
    private static String generateSimplePredicate(String[] attributes) {
        StringBuilder predicate = new StringBuilder();
        String delimiter = "";
        for (String attribute : attributes) {
            predicate.append(delimiter);
            predicate.append(attribute);
            predicate.append(" = ?");
            delimiter = " AND ";
        }
        return predicate.toString();
    }

    /**
     * Generates an SQL select query with the following format,
     *
     *      SELECT  *
     *      FROM    {table}
     *      WHERE   {attributes[0]} = ? AND
     *              {attributes[1]} = ? AND
     *              ...;
     *
     * and returns it as a string.
     *
     * @param table the table to select from
     * @param attributes the attribute(s) of the table used to identify row(s)
     * @return an SQL select query
     */
    private static String generateSelectQuery(String table,
                                              String[] attributes) {
        String predicate = generateSimplePredicate(attributes);
        return "SELECT * FROM " + table + " WHERE " + predicate;
    }

    // Accessors
    /**
     * Executes an SQL prepared statement with the given parameters and returns
     * the first row. The format of the SQL query is:
     *
     *      SELECT  *
     *      FROM    {table}
     *      WHERE   {attributes[0]} = {attributeValues[0]} AND
     *              {attributes[1]} = {attributeValues[1]} AND
     *              ...;
     *
     * The Strings within the attributes and attributeValues arrays must
     * be in the same order.
     *
     * @param table the table to select from
     * @param attributes the attribute(s) of the table used to identify row(s)
     * @param attributeValues the value(s) of the attribute(s)
     * @return results returned by the query as a List of Maps
     */
    protected static List<Map<String, Object>> getResult(String table,
                                                   String[] attributes,
                                                   Object[] attributeValues)
            throws SQLException {
        List<Map<String, Object>> resultList =
                new ArrayList<Map<String, Object>>();
        Map<String, Object> row = null;

        final String QUERY = generateSelectQuery(table, attributes);

        try (Connection conn =
                     DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            // Insert parameters into prepared statement
            for (int i = 1; i <= attributeValues.length; i++) {
                pstmt.setObject(i, attributeValues[i - 1]);
            }

            System.out.println(pstmt);

            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()) {
                row = new HashMap<String, Object>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
            return resultList;
        }
    }

    /**
     * Gets the value of a specified attribute associated with an
     * identifying set of attributes from a table in the database. If the is
     * more than one row in the result, then this method returns the first one.
     *
     * @param table the table to select from
     * @param attributes the attribute(s) of the table used to identify row(s)
     * @param attributeValues the value(s) of the attribute(s)
     * @param attributeName the name of the attribute to get the value of
     * @return the value of the first instance of a specified attribute from
     * a list of results
     */
    protected static Object getFirstAttributeValue(String table,
                                                   String[] attributes,
                                                   Object[] attributeValues,
                                                   String attributeName)
            throws SQLException {
        List<Map<String, Object>> resultList = getResult(table, attributes,
                attributeValues);
        Object attributeValue = null;
        if (resultList != null) {
            attributeValue = resultList.get(0).get(attributeName);
        }
        return attributeValue;
    }

    // Updates
    /**
     * Generates an SQL update query of a specified type. The query has the
     * following general format.
     *
     *      UPDATE  {table}
     *      SET     {targetAttribute} = {setClause}
     *      WHERE   {attributes[0]} = ? AND
     *              {attributes[1]} = ? AND
     *              ...;
     *
     * The setClause is dependent on updateType. It can be one of the following.
     *
     *      (INCREASE)  {targetAttribute} = {targetAttribute} + ?
     *      (DECREASE)  {targetAttribute} = {targetAttribute} - ?
     *      (CHANGE)    {targetAttribute} = ?
     *
     * Returns the query as a String.
     *
     * @param table the table to update
     * @param attributes the attribute(s) of the table used to identify row(s)
     * @param targetAttribute the name of the attribute to be updated
     * @param updateType the type of update to be performed
     * @return an SQL update query
     */
    private static String generateUpdateQuery(String table,
                                              String[] attributes,
                                              String targetAttribute,
                                              UpdateType updateType) {
        String setClause = targetAttribute + " = ";
        switch (updateType) {
            case INCREASE:
                setClause += targetAttribute + " + ?";
                break;
            case DECREASE:
                setClause += targetAttribute + " - ?";
                break;
            case CHANGE:
                setClause += "?";
                break;
        }
        String predicate = generateSimplePredicate(attributes);
        return "UPDATE " + table +
                " SET " + setClause +
                " WHERE " + predicate;
    }

    /**
     * Executes an SQL update query. The query should come from the
     * generateUpdateQuery() method.
     *
     * @param query an SQL update query with parameters for an update value
     *              and attribute values
     * @param updateValue the value that will go into the set clause
     * @param attributeValues the value(s) of the attribute(s) used to identify
     *                        the tuple(s) to be updated
     * @throws SQLException if the connection or update fails
     */
    private static void update(String query, Object updateValue,
                               Object[] attributeValues)
            throws SQLException {
        try (Connection conn =
                     DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // First, insert updateValue into the prepared statement
            pstmt.setObject(1, updateValue);

            // Next, insert the attribute values into the prepared statement
            for (int i = 2; i <= attributeValues.length; i++) {
                pstmt.setObject(i, attributeValues[i - 1]);
            }

            System.out.println(pstmt);

            int updateCount = pstmt.executeUpdate();

            System.out.println("Update count: " + updateCount);
        }
    }

    /**
     * Increases the value of an attribute from a table in the database by an
     * specified amount. The row(s) to be updated are identified by the
     * attribute(s) and their value(s).
     *
     * @param table the table in the database to update
     * @param attributes the attribute(s) used to find the row(s) to update
     * @param attributeValues the values of the attribute(s) used to find the
     *                        row(s) to update
     * @param targetAttribute the attribute to be increased
     * @param amount the amount to increase the attribute by
     * @throws SQLException if a database related issue arises
     */
    protected static void increaseAttributeValue(String table,
                                              String[] attributes,
                                              Object[] attributeValues,
                                              String targetAttribute,
                                              Object amount)
            throws SQLException {
        final String UPDATE_QUERY = generateUpdateQuery(table, attributes,
                targetAttribute, UpdateType.INCREASE);
        update(UPDATE_QUERY, amount, attributeValues);
    }
}
