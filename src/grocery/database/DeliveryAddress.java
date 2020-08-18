package grocery.database;

import java.sql.SQLException;
import java.util.List;

import static grocery.database.DeliveryAddressDatabase.getDeliveryAddressResult;

public class DeliveryAddress {
   /**
    * Returns a list of the customer's delivery addresses.
    *
    * @param customerID the ID of a customer
    * @return a list of the customer's delivery addresses
    * @throws SQLException
    */
   public static List<Address> queryDeliveryAddresses(int customerID)
         throws SQLException {
      return getDeliveryAddressResult(customerID);
   }
}
