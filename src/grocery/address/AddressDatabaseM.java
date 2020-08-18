package grocery.address;

import java.sql.*;

/**
 * @author Martijn
 */
public class AddressDatabaseM
{
   private String streetNum;
   private String streetName;
   private String aptNum;
   private String city;
   private String state;
   private String zip;
   private String country;
   private int addressId;

   public boolean isSuccess() {
      return success;
   }

   private boolean success;

   static final String url = "jdbc:postgresql://localhost/amm";
   static final String user = "postgres";
   static final String password = "password";//"<add your password>";

   public AddressDatabaseM(String streetNumField, String streetNameField, String aptNumField, String cityField,
                           String stateField, String zipField, String countryField) {
      setStreetNum(streetNumField);
      setStreetName(streetNameField);
      setAptNum(aptNumField);
      setCity(cityField);
      setState(stateField);
      setZip(zipField);
      setCountry(countryField);
   }

   public AddressDatabaseM() {

   }

   /**
    * establish connection
    *
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


   // adds address info from credit card screen to address table
   public void addAddress(int customerID) {

      try (Connection conn = connect();) {
         conn.setAutoCommit(false);
         String insertAddress = "INSERT INTO address (street_number, street_name, apt_number, city, state, zip, country) "
               + "VALUES (?::int, ?, ?, ?, ?, ?, ?)";

         try (PreparedStatement pStmt = conn.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS)) {

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
            conn.rollback();
         }

         String insertDeliveryAddress = "INSERT INTO delivery_address (cid, address_id) "
               + "VALUES (?, ?)";

         try (PreparedStatement pStmt = conn.prepareStatement(insertDeliveryAddress, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("Adding delivery address");

            pStmt.setInt(1, customerID);
            pStmt.setInt(2, addressId);

            pStmt.executeUpdate();
            ResultSet tableKeys = pStmt.getGeneratedKeys();
            tableKeys.next();
            addressId = tableKeys.getInt(1);

            success = true;
            System.out.println("Records created successfully");

         } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
            conn.rollback();
         }
         conn.commit();
         conn.setAutoCommit(true);

      } catch (SQLException e) {
         e.printStackTrace();
      }

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

}