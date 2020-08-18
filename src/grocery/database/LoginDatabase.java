package grocery.database;

import java.sql.*;

import static grocery.database.Database.*;

public class LoginDatabase
{

   private static final String CUSTOMER_QUERY =
         "SELECT * FROM customer WHERE username = ? and password = ?";
   private static final String STAFF_QUERY =
         "SELECT * FROM staff WHERE username = ? and password = ?";

   private static final String CUSTOMER = "Customer";
   private static final String STAFF = "Staff";

   public static boolean checkCredentials(String userType, String username,
                                   String password) {
      String SELECT_QUERY = "";
      if (userType.equals(CUSTOMER)) {
         SELECT_QUERY = CUSTOMER_QUERY;
      }
      else if (userType.equals(STAFF)) {
         SELECT_QUERY = STAFF_QUERY;
      }
      else {
         return false;
      }

      try (Connection connection =
                 DriverManager.getConnection(URL, USERNAME, PASSWORD);
           PreparedStatement preparedStatement =
                 connection.prepareStatement(SELECT_QUERY)) {

         preparedStatement.setObject(1, username);
         preparedStatement.setObject(2, password);

         System.out.println(preparedStatement);

         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            return true;
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return false;
   }
}
