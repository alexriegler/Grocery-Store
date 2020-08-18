package grocery.database;

import java.sql.SQLException;

public class AddressDatabase extends Database {
    // Address table names
    static final String ADDRESS_TABLE_NAME = "address";
    static final String ADDRESS_ID_COLUMN = "address_id";
    static final String ST_NUM_COLUMN = "street_number";
    static final String ST_NAME_COLUMN = "street_name";
    static final String APT_NUM_COLUMN = "apt_number";
    static final String CITY_COLUMN = "city";
    static final String STATE_COLUMN = "state";
    static final String ZIP_COLUMN = "zip";
    static final String COUNTRY_COLUMN = "country";

    /**
     * Gets the value of an attribute from the address table. Calls the
     * parent method Database.getFirstAttributeValue().
     *
     * @param addressID the ID associated with the address
     * @param attributeName the attribute of which to find its value
     * @return the value of the specified attribute
     * @throws SQLException
     */
    private static Object getAddressAttributeValue(int addressID,
                                                  String attributeName)
          throws SQLException {
        String[] cKey = {ADDRESS_ID_COLUMN};
        Object[] cKeyValue = {addressID};
        return getFirstAttributeValue(ADDRESS_TABLE_NAME, cKey, cKeyValue,
              attributeName);
    }

    /**
     * Returns the street number associated with an address ID in the database.
     * @param addressID the ID of an address
     * @return the address' street number
     * @throws SQLException
     */
    public static int queryStreetNumber(int addressID) throws SQLException {
        return (int) getAddressAttributeValue(addressID, ST_NUM_COLUMN);
    }

    /**
     * Returns the street name associated with an address ID in the database.
     * @param addressID the ID of an address
     * @return the address' street name
     * @throws SQLException
     */
    public static String queryStreetName(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, ST_NAME_COLUMN);
    }

    /**
     * Returns the apartment number associated with an address ID in the
     * database.
     * @param addressID the ID of an address
     * @return the address' apartment number
     * @throws SQLException
     */
    public static String queryAptNumber(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, APT_NUM_COLUMN);
    }

    /**
     * Returns the city associated with an address ID in the database.
     * @param addressID the ID of an address
     * @return the address' city
     * @throws SQLException
     */
    public static String queryCity(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, CITY_COLUMN);
    }

    /**
     * Returns the state associated with an address ID in the database.
     * @param addressID the ID of an address
     * @return the address' state
     * @throws SQLException
     */
    public static String queryState(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, STATE_COLUMN);
    }

    /**
     * Returns the 5-digit zip code associated with an address ID in the
     * database.
     * @param addressID the ID of an address
     * @return the address' zip code
     * @throws SQLException
     */
    public static String queryZip(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, ZIP_COLUMN);
    }

    /**
     * Returns the country associated with an address ID in the database.
     * @param addressID the ID of an address
     * @return the address' country
     * @throws SQLException
     */
    public static String queryCountry(int addressID) throws SQLException {
        return (String) getAddressAttributeValue(addressID, COUNTRY_COLUMN);
    }

    public static void updateAddress(String addressID, int streetNumber,
                              String streetName, String aptNumber, String city,
                              String state, String zipCode, String country) {

    }
}
