package grocery.confirmation;

import grocery.cartCheckout.CartCheckoutItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Martijn
 */
public class ConfirmationDatabase
{

    private static final String url = "jdbc:postgresql://localhost/amm";
    private static final String user = "postgres";
    private static final String password = "password";//"<add your password>";
    private ObservableList<CartCheckoutItem> data = FXCollections.observableArrayList();
    private ArrayList<String> cc_numbers = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<String> address_id = new ArrayList<>();

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

    public ObservableList<CartCheckoutItem> getData(int orderID) {
        // Get order contents with pricing information
        String select = "SELECT product_name, quantity, price " +
                 "FROM orders NATURAL JOIN order_contents NATURAL JOIN pricing NATURAL JOIN warehouse NATURAL JOIN address " +
                 "WHERE oid = ? ";

        try (Connection conn = connect();
             PreparedStatement pStmt = conn.prepareStatement(select)) {

            System.out.println("Get order contents with pricing");

            pStmt.setInt(1, orderID);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                data.add(new CartCheckoutItem(rs.getString("product_name"),
                        rs.getString("quantity"), rs.getString("price")));

                int q = Integer.parseInt(rs.getString("quantity").trim());
                double p = Double.parseDouble(rs.getString("price").trim());

                subtotal += q*p;

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public double getSubtotal() {
        return subtotal;
    }

}