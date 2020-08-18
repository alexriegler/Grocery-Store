package grocery.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeliveryAddressDatabase extends Database {
   // Delivery address table names
   static final String DELIVERY_ADDRESS_TABLE = "delivery_address";
   static final String CID_COLUMN = "cid";
   static final String ADDRESS_ID_COLUMN = "address_id";

   /**
    * Returns a list of a customer's delivery addresses in the database.
    *
    * @param customerID the ID of a customer
    * @return a list of addresses
    * @throws SQLException
    */
   public static List<Address> getDeliveryAddressResult(int customerID) throws SQLException
   {
      List<Address> addresses = new ArrayList<>();
      String[] cKey = {CID_COLUMN};
      Object[] cKeyValue = {customerID};
      List<Map<String, Object>> results = getResult(DELIVERY_ADDRESS_TABLE,
            cKey, cKeyValue);
      for (Map<String, Object> result : results) {
         int addressID = (int) result.get(ADDRESS_ID_COLUMN);
         Address address = new Address(addressID);
         addresses.add(address);
      }
      return addresses;
   }
}
