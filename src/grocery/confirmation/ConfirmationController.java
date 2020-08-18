package grocery.confirmation;

import grocery.cartCheckout.CartCheckoutItem;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Random;

/**
 * @author Martijn
 */
public class ConfirmationController
{

    @FXML
    public Label deliveryDate;
    @FXML
    public TableView tableID;
    @FXML
    private TableColumn<CartCheckoutItem, String> name, quantity, price;
    @FXML Label totalLabel;

    private int orderID;

    LocalDate d1 = LocalDate.now().plusDays(new Random().nextInt(14));

    private ObservableList<CartCheckoutItem> data;

    public void initialize(int orderID) {
        this.orderID = orderID;

        // populate TableView
        data = new ConfirmationDatabase().getData(orderID);
        name.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableID.setItems(data);

        // populate total label
        ConfirmationDatabase confirmationDatabase = new ConfirmationDatabase();
        confirmationDatabase.getData(orderID);
        totalLabel.setText("Total: $ " + String.format("%.2f",(confirmationDatabase.getSubtotal())));

        String formattedDate = d1.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        deliveryDate.setText("Estimated Delivery: " + formattedDate);

    }

    public void handlePressHomeButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
