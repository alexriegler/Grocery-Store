package grocery.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class HomeDatabase {
   private static final String url = "jdbc:postgresql://localhost/amm";
   private static final String user = "postgres";
   private static final String password = "password";//"<add your password>";

   private ObservableList<ProductItem> productItems = FXCollections.observableArrayList();
   private String product_name, category, add_info;
   private double price;

   public Connection connect() {
      Connection conn = null;
      try {
         //Attempts to establish a connection to the given database URL
         conn = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return conn;
   }

   public int getWarehouseID(String state) {
      int warehouseID = 0;

      String selectWID = "SELECT wid FROM warehouse NATURAL JOIN address " +
            "WHERE state = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(selectWID)) {

         System.out.println("Getting warehouse...");

         pStmt.setString(1, state);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            warehouseID = rs.getInt("wid");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return warehouseID;
   }

   public ObservableList<ProductItem> getProductItems(String state) {
      int warehouseID = 0;

      // Find state warehouse
      String selectWID = "SELECT wid FROM warehouse NATURAL JOIN address " +
            "WHERE state = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(selectWID)) {

         System.out.println("Getting warehouse...");

         pStmt.setString(1, state);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            warehouseID = rs.getInt("wid");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      // Get available products in state
      String select = "SELECT product_name, category, price, add_info, description " +
            "FROM product NATURAL JOIN warehouse_stock NATURAL JOIN pricing " +
            "WHERE wid = ?::int AND state = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Getting state catalog...");

         pStmt.setInt(1, warehouseID);
         pStmt.setString(2, state);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            productItems.add(new ProductItem(rs.getString("product_name"),
                  rs.getString("category"), rs.getString("price"),
                  rs.getString("add_info"), rs.getString("description")
            ) );
         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return productItems;
   }

   // Get products with search query
   public ObservableList<ProductItem> getProductItemsSearch(String state,
                                                      String searchQuery) {
      int warehouseID = 0;

      // Find state warehouse
      String selectWID = "SELECT wid FROM warehouse NATURAL JOIN address " +
            "WHERE state = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(selectWID)) {

         System.out.println("Getting warehouse...");

         pStmt.setString(1, state);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            warehouseID = rs.getInt("wid");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      // Get available products in state
      String select = "SELECT product_name, category, price, add_info, description " +
            "FROM product NATURAL JOIN warehouse_stock NATURAL JOIN pricing " +
            "WHERE wid = ?::int AND state = ? AND product_name ILIKE ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Getting state catalog...");

         pStmt.setInt(1, warehouseID);
         pStmt.setString(2, state);
         pStmt.setString(3, "%" + searchQuery +"%");

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            productItems.add(new ProductItem(rs.getString("product_name"),
                  rs.getString("category"), rs.getString("price"),
                  rs.getString("add_info"), rs.getString("description")
            ) );
         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return productItems;
   }

   public ObservableList<ProductItem> getProductItemsByCategory(String state,
                                                      String category) {
      int warehouseID = 0;

      // Find state warehouse
      String selectWID = "SELECT wid FROM warehouse NATURAL JOIN address " +
            "WHERE state = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(selectWID)) {

         System.out.println("Getting warehouse...");

         pStmt.setString(1, state);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            warehouseID = rs.getInt("wid");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      // Get available products in state
      String select = "SELECT product_name, category, price, add_info, description " +
            "FROM product NATURAL JOIN warehouse_stock NATURAL JOIN pricing " +
            "WHERE wid = ?::int AND state = ? AND category = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Getting state catalog...");

         pStmt.setInt(1, warehouseID);
         pStmt.setString(2, state);
         pStmt.setString(3, category);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            productItems.add(new ProductItem(rs.getString("product_name"),
                  rs.getString("category"), rs.getString("price"),
                  rs.getString("add_info"), rs.getString("description")
            ) );
         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return productItems;
   }

   public void addItemToCart(int customerID, String product_name) {
      int quantity = 0;
      String select = "SELECT quantity FROM cart_contents WHERE cid = ? AND " +
            "product_name = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         pStmt.setInt(1, customerID);
         pStmt.setString(2, product_name);

         System.out.println("Checking cart...");

         ResultSet rs = pStmt.executeQuery();

         if (rs.next()) {
            quantity = rs.getInt("quantity");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      if (quantity == 0) {
         String insert = "INSERT INTO cart_contents VALUES (?, ?, ?)";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(insert)) {

            pStmt.setInt(1, customerID);
            pStmt.setString(2, product_name);
            pStmt.setInt(3, 1);

            System.out.println("Executing update to cart...");

            int updateCount = pStmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
      else {
         String insert = "UPDATE cart_contents SET quantity = quantity + 1 " +
               "WHERE cid = ? AND product_name = ?";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(insert)) {

            pStmt.setInt(1, customerID);
            pStmt.setString(2, product_name);

            System.out.println("Executing update to cart...");

            int updateCount = pStmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   /**
    * Choose the amount to insert into cart.
    * @param customerID
    * @param product_name
    * @param quantity
    */
   public void addItemToCart(int customerID, String product_name,
                             int quantity) {
      int existingQuantity = 0;
      String select = "SELECT quantity FROM cart_contents WHERE cid = ? AND " +
            "product_name = ?";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         pStmt.setInt(1, customerID);
         pStmt.setString(2, product_name);

         System.out.println("Checking cart...");

         ResultSet rs = pStmt.executeQuery();

         if (rs.next()) {
            existingQuantity = rs.getInt("quantity");
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      if (existingQuantity == 0) {
         String insert = "INSERT INTO cart_contents VALUES (?, ?, ?)";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(insert)) {

            pStmt.setInt(1, customerID);
            pStmt.setString(2, product_name);
            pStmt.setInt(3, quantity);

            System.out.println("Executing update to cart...");

            int updateCount = pStmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
      else {
         String insert = "UPDATE cart_contents SET quantity = quantity + ? " +
               "WHERE cid = ? AND product_name = ?";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(insert)) {

            pStmt.setInt(1, quantity);
            pStmt.setInt(2, customerID);
            pStmt.setString(3, product_name);

            System.out.println("Executing update to cart...");

            int updateCount = pStmt.executeUpdate();
         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }
}
