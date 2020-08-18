package grocery.orders;


import grocery.orders.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Martijn
 */
public class OrdersDatabase {

   private static final String url = "jdbc:postgresql://localhost/amm";
   private static final String user = "postgres";
   private static final String password = "password";//"<add your password>";
   private ObservableList<OrderItem> poData = FXCollections.observableArrayList();

   private ArrayList<String> orderID = new ArrayList<>();
   private String status, cc_number, product_name, orderDate;
   private int quantity;
   private double subtotal = 0, balance;

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

   public void getOrderInfo(int customerID) {

      String select = "SELECT date_issued, status, cc_number, product_name, quantity " +
            "FROM orders NATURAL JOIN order_contents " +
            "WHERE cid = ? ";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Getting order information");

         pStmt.setInt(1, customerID);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            orderDate = rs.getString("date_issued");
            status = rs.getString("status");
            cc_number = rs.getString("cc_number");
            product_name = rs.getString("product_name");
            quantity = rs.getInt("quantity");
         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      select = "SELECT oid " +
            "FROM orders " +
            "WHERE cid = ? ";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Getting order information");

         pStmt.setInt(1, customerID);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            orderID.add(rs.getString("oid"));
         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   public ObservableList<OrderItem> getData(String orderID) {

      // Get order contents with pricing information
      String select = "SELECT product_name, quantity, price, date(date_issued) AS mydate, status, cc_number " +
            "FROM orders NATURAL JOIN order_contents NATURAL JOIN pricing NATURAL JOIN address NATURAL JOIN warehouse " +
            "WHERE oid = ?::int ";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Get order contents with pricing");

         pStmt.setString(1, orderID);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            poData.add(new OrderItem(rs.getString("product_name"),
                  rs.getString("quantity"), rs.getString("price"),
                  rs.getString("mydate"), rs.getString("cc_number"),
                  rs.getString("status")
            ) );

            int q = Integer.parseInt(rs.getString("quantity").trim());
            double p = Double.parseDouble(rs.getString("price").trim());

            subtotal += q*p;

         }

      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return poData;
   }

   public double getSubtotal() {
      return subtotal;
   }

   public ArrayList<String> getOrderID() {
      return orderID;
   }

}