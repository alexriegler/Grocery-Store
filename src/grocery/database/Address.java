package grocery.database;

import java.sql.*;

import static grocery.database.AddressDatabase.*;

public class Address {
   // Address variables
   private int addressID;
   private int streetNumber;
   private String streetName;
   private String aptNumber;
   private String city;
   private String state;
   private String zipCode;
   private String country;

   /**
    * Constructs an address using its address ID to find its information in
    * the database.
    *
    * @param addressID the ID associated with an address
    * @throws SQLException
    */
   public Address(int addressID) throws SQLException
   {
      this.addressID = addressID;
      streetNumber = queryStreetNumber(addressID);
      streetName = queryStreetName(addressID);
      aptNumber = queryAptNumber(addressID);
      city = queryCity(addressID);
      state = queryState(addressID);
      zipCode = queryZip(addressID);
      country = queryCountry(addressID);
   }

   // Getters
   public int getAddressID() {
      return addressID;
   }

   public int getStreetNumber() {
      return streetNumber;
   }

   public String getStreetName() {
      return streetName;
   }

   public String getAptNumber() {
      return aptNumber;
   }

   public String getCity() {
      return city;
   }

   public String getState() {
      return state;
   }

   public String getZipCode() {
      return zipCode;
   }

   public String getCountry() {
      return country;
   }
}
