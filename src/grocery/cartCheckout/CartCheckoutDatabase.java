package grocery.cartCheckout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Martijn
 */
public class CartCheckoutDatabase {

   private static final String url = "jdbc:postgresql://localhost/amm";
   private static final String user = "postgres";
   private static final String password = "password";//"<add your password>";
   private ObservableList<CartCheckoutItem> data = FXCollections.observableArrayList();
   private ArrayList<String> cc_numbers = new ArrayList<>();
   private ArrayList<String> addresses = new ArrayList<>();
   private ArrayList<String> address_id = new ArrayList<>();
   private int oid;
   private int wid;
   private int cid;

   private double subtotal = 0, balance;

   public CartCheckoutDatabase(int cid, int wid) {
      setWid(wid);
      setCid(cid);
   }

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

   public ObservableList<CartCheckoutItem> getItems() {
      // get cart contents with pricing information
      String select = "SELECT * FROM cart_contents " +
            "NATURAL JOIN pricing " +
            "WHERE cid = ? " +
            "AND state = " +
            "(SELECT state FROM address WHERE address_id = " +
            "(SELECT address_id FROM warehouse WHERE wid = ? ))";

      try (Connection conn = connect();
           PreparedStatement pStmt = conn.prepareStatement(select)) {

         System.out.println("Populate TableView");

         pStmt.setInt(1, cid);
         pStmt.setInt(2, wid);

         ResultSet rs = pStmt.executeQuery();

         while (rs.next()) {
            data.add(new CartCheckoutItem(rs.getString("product_name"),
                  rs.getString("quantity"), rs.getString("price")));

            int q = Integer.parseInt(rs.getString("quantity").trim());
            double p = Double.parseDouble(rs.getString("price").trim());

            subtotal += q * p;

         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return data;
   }

   public void getData() {

      try (Connection conn = connect();) {

         // get credit card numbers
         String select = "SELECT cc_number " +
               "FROM credit_card " +
               "WHERE cid = ? ";

         try (PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Get credit card numbers for choice box");

            pStmt.setInt(1, cid);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
               cc_numbers.add(rs.getString("cc_number"));
            }

         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }

         // get addresses
         select = "SELECT street_number, street_name, address_id " +
               "FROM delivery_address " +
               "NATURAL JOIN address " +
               "WHERE cid = ? ";

         try (PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Get addresses for choice box");

            pStmt.setInt(1, cid);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
               addresses.add(rs.getString("street_number") + " " + rs.getString("street_name"));
               address_id.add(rs.getString("address_id"));
            }


         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }

         // get customer balance
         select = "SELECT balance " +
               "FROM customer " +
               "WHERE cid = ? ";

         try (PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Get customer balance");

            pStmt.setInt(1, cid);

            ResultSet rs = pStmt.executeQuery();

            rs.next();
            balance = Double.parseDouble(rs.getString("balance"));

         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }

         // get cart contents with pricing information
         select = "SELECT * FROM cart_contents " +
               "NATURAL JOIN pricing " +
               "WHERE cid = ? " +
               "AND state =  " +
               "(SELECT state FROM address WHERE address_id = " +
               "(SELECT address_id FROM warehouse WHERE wid = ? ))";

         try (PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Get cart contents with prices");

            pStmt.setInt(1, cid);
            pStmt.setInt(2, wid);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
               data.add(new CartCheckoutItem(rs.getString("product_name"),
                     rs.getString("quantity"), rs.getString("price")));

               int q = Integer.parseInt(rs.getString("quantity").trim());
               double p = Double.parseDouble(rs.getString("price").trim());

               subtotal += q * p;

            }

         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

   }

   public void placeOrder(String cc_number) {

      try (Connection conn = connect();) {

         conn.setAutoCommit(false);

         Boolean success = false;
         String insertOrders = "INSERT INTO orders (wid, date_issued, status, cid, cc_number) "
               + "VALUES (?, ?, ?, ?, ?)";

         try (PreparedStatement pStmt = conn.prepareStatement(insertOrders, Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("Insert into orders");

            pStmt.setInt(1, wid);
            pStmt.setTimestamp(2, getCurrentTimeStamp());
            pStmt.setString(3, "Processing");
            pStmt.setInt(4, cid);
            pStmt.setString(5, cc_number);

            pStmt.executeUpdate();
            ResultSet tableKeys = pStmt.getGeneratedKeys();
            tableKeys.next();
            oid = tableKeys.getInt(1);

            System.out.println("Order ID: "+ oid);
            System.out.println("Records created successfully");
            success = true;

         } catch (SQLException e) {
            System.out.println(e.getMessage());
            conn.rollback();
            success = false;
         }

         if (success == true) {
            String insertOrderContents = "INSERT INTO order_contents (oid, product_name, quantity) " +
                  "SELECT ?, product_name, quantity " +
                  "FROM cart_contents NATURAL JOIN pricing " +
                  "WHERE cid = ? " +
                  "AND state = " +
                  "(SELECT state FROM address WHERE address_id = " +
                  "(SELECT address_id FROM warehouse WHERE wid = ? ))";

            try (PreparedStatement pStmt = conn.prepareStatement(insertOrderContents)) {

               System.out.println("Insert into order_contents");

               pStmt.setInt(1, oid);
               pStmt.setInt(2, cid);
               pStmt.setInt(3, wid);

               pStmt.executeUpdate();

               System.out.println("Records created successfully");
               success = true;

            } catch (SQLException e) {
               System.out.println(e.getMessage());
               conn.rollback();
               success = false;
            }
         }
         if (success == true) {
            // Update customer balance
            String updateBalance = "UPDATE customer " +
                  "SET balance = balance + ? " +
                  "WHERE cid = ?";

            try (PreparedStatement pStmt = conn.prepareStatement(updateBalance)) {

               System.out.println("Update customer balance");

               pStmt.setDouble(1, subtotal);
               pStmt.setInt(2, cid);

               pStmt.executeUpdate();

               System.out.println("Records created successfully");
               success = true;

            } catch (SQLException e) {
               System.out.println(e.getMessage());
               conn.rollback();
               success = false;
            }
         }

         if (success == true) {
            // Update warehouse stock
            String updateStock = "UPDATE warehouse_stock AS t2 " +
                  "SET stock = stock - t1.quantity " +
                  "FROM cart_contents AS t1 " +
                  "WHERE t2.product_name = t1.product_name " +
                  "AND cid = ? " +
                  "AND wid = ? ";

            try (PreparedStatement pStmt = conn.prepareStatement(updateStock)) {

               System.out.println("Update warehouse stock");

               pStmt.setInt(1, cid);
               pStmt.setInt(2, wid);

               pStmt.executeUpdate();

               System.out.println("Warehouse stock updated successfully");
               success = true;

            } catch (SQLException e) {
               System.out.println(e.getMessage());
               conn.rollback();
               success = false;
            }
         }

         if (success == true) {
            // Delete cart contents
            String deleteCart = "DELETE FROM cart_contents " +
                  "WHERE cid = ? ";

            try (PreparedStatement pStmt = conn.prepareStatement(deleteCart)) {

               System.out.println("Deleting cart contents");

               pStmt.setInt(1, cid);

               pStmt.executeUpdate();

               System.out.println("Deleted cart successfully");

            } catch (SQLException e) {
               System.out.println(e.getMessage());
               conn.rollback();
            }

         } else {
            System.out.println("FAILED");
         }

         conn.commit();
         conn.setAutoCommit(true);

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void updateQuantity(String quantity, String prodName) {
      if (quantity.equals("0")) {
         // Delete cart item
         String deleteItem = "DELETE FROM cart_contents " +
               "WHERE cid = ? " +
               "AND product_name = ? ";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(deleteItem)) {

            System.out.println("Delete cart item");

            pStmt.setInt(1, cid);
            pStmt.setString(2, prodName);

            pStmt.executeUpdate();

            System.out.println("Records created successfully");

         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      } else {

         // Update cart quantity
         String updateQuantity = "UPDATE cart_contents " +
               "SET quantity = ?::int " +
               "WHERE cid = ? " +
               "AND product_name = ? ";

         try (Connection conn = connect();
              PreparedStatement pStmt = conn.prepareStatement(updateQuantity)) {

            System.out.println("Update cart quantity");

            pStmt.setString(1, quantity);
            pStmt.setInt(2, cid);
            pStmt.setString(3, prodName);

            pStmt.executeUpdate();

            System.out.println("Records created successfully");

         } catch (SQLException e) {
            System.out.println(e.getMessage());
         }
      }
   }

   private static java.sql.Timestamp getCurrentTimeStamp() {

      java.util.Date today = new java.util.Date();
      return new java.sql.Timestamp(today.getTime());

   }

   public double getSubtotal() {
      return subtotal;
   }

   public double getBalance() {
      return subtotal + balance;
   }

   public ArrayList<String> getCC_numbers() {
      return cc_numbers;
   }

   public ArrayList<String> getAddresses() {
      return addresses;
   }

   public int getOid() {
      return oid;
   }

   public void setWid(int wid) {
      this.wid = wid;
   }

   public void setCid(int cid) {
      this.cid = cid;
   }



}