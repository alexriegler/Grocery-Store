package grocery.database;

import java.sql.*;
import java.util.List;

import static grocery.database.CustomerDatabase.*;
import static grocery.database.DeliveryAddress.queryDeliveryAddresses;

public class Customer
{
   private int customerID;
   private String customerName;
   private double balance;
   private String username;
   private String password;

   // Related info
   private List<Address> deliveryAddresses;
   // private CreditCard[] creditCards;

   /**
    * Constructs a customer using its username to find its attribute values
    * in the database.
    *
    * @param username the username of the customer
    * @throws SQLException if database exception occurs
    */
   public Customer(String username) throws SQLException {
      customerID = queryCustomerID(username);
      customerName = queryCustomerNameUsingUsername(username);
      balance = queryBalanceUsingUsername(username);
      this.username = username;
      password = queryPasswordUsingUsername(username);
      deliveryAddresses = queryDeliveryAddresses(customerID);
   }

   // Getters
   public int getCustomerID() {
      return customerID;
   }

   public String getCustomerName() {
      return customerName;
   }

   public double getBalance() {
      return balance;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public List<Address> getDeliveryAddresses() {
      return deliveryAddresses;
   }

   // "Setters"
    /**
     * Increases the balance of a customer.
     *
     * @param amount the amount to increase the balance by
     * @throws SQLException
     */
    public void addToBalance(double amount) throws SQLException {
      increaseBalance(customerID, amount);
      balance = queryBalanceUsingUsername(username);
   }
}
