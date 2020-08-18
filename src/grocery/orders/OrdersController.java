package grocery.orders;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @author Martijn
 */
public class OrdersController {

   @FXML public TableView tableID;
   @FXML private TableColumn<OrderItem, String> name, quantity, price, dateIssued, ccNum, status;
   @FXML Label totalLabel;
   @FXML private ChoiceBox<String> ordersBox;

   private ObservableList<OrderItem> poData;

   public void initialize(int customerID) {
      OrdersDatabase po = new OrdersDatabase();
      po.getOrderInfo(customerID);
      ordersBox.getItems().setAll(po.getOrderID());
      ordersBox.getSelectionModel().selectLast();
   }

   public void boxAction(ActionEvent actionEvent) {
      // populate TableView
      poData = new OrdersDatabase().getData(ordersBox.getValue());
      name.setCellValueFactory(new PropertyValueFactory<>("prodName"));
      quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
      price.setCellValueFactory(new PropertyValueFactory<>("price"));
      dateIssued.setCellValueFactory(new PropertyValueFactory<>("dateIssued"));
      ccNum.setCellValueFactory(new PropertyValueFactory<>("ccNum"));
      status.setCellValueFactory(new PropertyValueFactory<>("status"));
      tableID.setItems(poData);

      // populate total label
      OrdersDatabase pOrders = new OrdersDatabase();
      pOrders.getData(ordersBox.getValue());
      totalLabel.setText("Total: $ " + String.format("%.2f",(pOrders.getSubtotal())));
   }

   /**
    * Close the window on pressing the home button
    * @param event pressing the home button
    */
   public void handlePressHomeButton(ActionEvent event) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.close();
   }
}