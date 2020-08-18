package grocery.creditcard;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Martijn
 */
public class CreditCard
{
    private String name;
    private String number;
    private String date;
    private String streetNum;
    private String streetName;
    private String aptNum;
    private String city;
    private String state;
    private String zip;
    private String country;
    private int addressId;

    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<String> address_id = new ArrayList<>();


    public boolean isSuccess() {
        return success;
    }

    private boolean success;
    private int cvv;

    static final String url = "jdbc:postgresql://localhost/amm";
    static final String user = "postgres";
    static final String password = "password";//"<add your password>";

    public CreditCard(String nameField, String numberField, String cvvField, String monthField, String yearField,
                      String streetNumField, String streetNameField, String aptNumField, String cityField,
                      String stateField, String zipField, String countryField) {
        setName(nameField);
        setNumber(numberField);
        setCVV(cvvField);
        setDate(monthField, yearField);
        setStreetNum(streetNumField);
        setStreetName(streetNameField);
        setAptNum(aptNumField);
        setCity(cityField);
        setState(stateField);
        setZip(zipField);
        setCountry(countryField);
    }

    public CreditCard(String nameField, String numberField, String cvvField, String monthField, String yearField, int a_id) {
        setName(nameField);
        setNumber(numberField);
        setCVV(cvvField);
        setDate(monthField, yearField);
        setAddressId(a_id);
    }

    public CreditCard() {

    }

    /**
     * establish connection
     * @return connection
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
           // System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void getAddress(int customerID) {
        int cid = customerID;

        // get addresses
        address_id.add("Choose Address");

        String select = "SELECT address_id, street_number, street_name, apt_number, city, state, zip, country " +
                        "FROM address NATURAL JOIN credit_card " +
                        "WHERE cid = ? " +
                        "UNION " +
                        "SELECT address_id, street_number, street_name, apt_number, city, state, zip, country " +
                        "FROM address NATURAL JOIN delivery_address " +
                        "WHERE cid = ? ";

        try (Connection conn = connect();
             PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Getting addresses for choice box");

            pStmt.setInt(1, cid);
            pStmt.setInt(2, cid);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                //addresses.add(rs.getString("street_number") + " " + rs.getString("street_name"));
                address_id.add(rs.getString("address_id"));
                streetNum = rs.getString("street_number");
                streetName = rs.getString("street_name");
                aptNum = rs.getString("apt_number");
                city = rs.getString("city");
                state = rs.getString("state");
                zip = rs.getString("zip");
                country = rs.getString("country");
                addressId = rs.getInt("address_id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // gets addresses for choicebox
    public void getAddressDropDown(int a_id) {

        // get addresses
        String select = "SELECT * " +
                "FROM address " +
                "WHERE address_id = ? ";

        try (Connection conn = connect();
             PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Drop down address selection");

            pStmt.setInt(1, a_id);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                streetNum = rs.getString("street_number");
                streetName = rs.getString("street_name");
                aptNum = rs.getString("apt_number");
                city = rs.getString("city");
                state = rs.getString("state");
                zip = rs.getString("zip");
                country = rs.getString("country");
                addressId = rs.getInt("address_id");
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Adds credit card info to DB
    public void addCC(int customerID) {
        int cid = customerID;

        try (Connection conn = connect();) {

            conn.setAutoCommit(false);

            String insert = "INSERT INTO credit_card (cc_number, cc_name, cvv, exp_date, cid, address_id) "
                    + "VALUES (?, ?, ?, ?::date, ?, ?)";

            try (PreparedStatement pStmt = conn.prepareStatement(insert)) {


                System.out.println("Adding credit card");

                pStmt.setString(1, number);
                pStmt.setString(2, name);
                pStmt.setInt(3, cvv);
                pStmt.setString(4, date);
                pStmt.setInt(5, cid);
                pStmt.setInt(6, addressId);

                pStmt.executeUpdate();

                success = true;

                System.out.println("Records created successfully");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                conn.rollback();
                success = false;
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // adds address info from credit card screen to address table
    public void addAddress() {

        String insert = "INSERT INTO address (street_number, street_name, apt_number, city, state, zip, country) "
                + "VALUES (?::int, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pStmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("Adding address");

            pStmt.setString(1, streetNum);
            pStmt.setString(2, streetName);
            pStmt.setString(3, aptNum);
            pStmt.setString(4, city);
            pStmt.setString(5, state);
            pStmt.setString(6, zip);
            pStmt.setString(7, country);

            pStmt.executeUpdate();
            ResultSet tableKeys = pStmt.getGeneratedKeys();
            tableKeys.next();
            addressId = tableKeys.getInt(1);

            success = true;
            System.out.println("Records created successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCVV(String cvv) {
        this.cvv = Integer.parseInt(cvv);
    }

    public void setDate(String month, String year) {
        this.date = month + "/1/" + year;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setAptNum(String aptNum) {
        this.aptNum = aptNum;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public ArrayList<String> getAddress_id() {
        return address_id;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getAptNum() {
        return aptNum;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    private void setAddressId(int a_id) {
        this.addressId = a_id;
    }

}
