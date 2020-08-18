package grocery.details;

import java.sql.*;

/**
 * @author Martijn
 */
public class DetailsDatabase {

   private static final String url = "jdbc:postgresql://localhost/amm";
   private static final String user = "postgres";
   private static final String password = "password";//"<add your password>";
   private String product_name;
   private String URL = "default.jpg";

   public DetailsDatabase(String product_name) {
      setProduct_name(product_name);
   }

   public Connection connect() {
      Connection conn = null;
      try {
         //Attempts to establish a connection to the given database URL
         conn = DriverManager.getConnection(url, user, password);
         System.out.println("Connected to the PostgreSQL server successfully.");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return conn;
   }

   public void getImg() {
      String select = "SELECT description " +
            "FROM product " +
            "WHERE product_name = ? ";


      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Opened database successfully");

         pStmt.setString(1, product_name);

         ResultSet rs = pStmt.executeQuery();
         rs.next();
         URL = rs.getString("description");

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public String getProduct_name() {
      return product_name;
   }

   public void setProduct_name(String product_name) {
      this.product_name = product_name;
   }

   public String getURL() {
      return URL;
   }

   public void setURL(String URL) {
      this.URL = URL;
   }


}
