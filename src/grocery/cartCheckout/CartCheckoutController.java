package grocery.cartCheckout;

import grocery.creditcard.CreditCardController;
import grocery.confirmation.ConfirmationController;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Martijn
 */
public class CartCheckoutController {

   @FXML
   private TableView<CartCheckoutItem> tableID;

   @FXML
   private TableColumn<CartCheckoutItem, String> name, quantity, price;

   @FXML
   private Label balanceLabel, totalLabel;

   @FXML
   private Button placeOrderButton;

   @FXML
   private ChoiceBox<String> choiceBoxCC, choiceBoxAddress;

   private int cid, wid;

   private ObservableList<CartCheckoutItem> data;
   CartCheckoutDatabase co;

   //@Override
   public void initialize(int customerID, int warehouseID) {
      cid = customerID;
      wid = warehouseID;

      // populate table with cart contents
      data = new CartCheckoutDatabase(cid, wid).getItems();
      tableID.setEditable(true);
      quantity.setCellFactory(TextFieldTableCell.forTableColumn());
      name.setCellValueFactory(new PropertyValueFactory<>("prodName"));
      quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
      price.setCellValueFactory(new PropertyValueFactory<>("price"));
      tableID.setItems(data);

      co = new CartCheckoutDatabase(cid, wid);
      co.getData();

      // populate total and balance labels
      totalLabel.setText(String.format("%.2f", (co.getSubtotal())));
      balanceLabel.setText(String.format("%.2f", (co.getBalance())));

      // populate delivery address and credit card choice boxes
      choiceBoxAddress.getItems().setAll(co.getAddresses());
      choiceBoxCC.getItems().setAll(co.getCC_numbers());

      choiceBoxAddress.getSelectionModel().selectFirst();
      choiceBoxCC.getSelectionModel().selectFirst();
   }

   public void handleNewCCButton(ActionEvent actionEvent) {
      try {
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource("/grocery/creditcard" +
               "/creditcard.fxml"));
         Parent ccParent = loader.load();

         CreditCardController controller = loader.getController();
         controller.initialize(cid);

         Scene scene = new Scene(ccParent);
         Stage ccStage = new Stage();
         ccStage.setTitle("Credit Card");
         ccStage.setScene(scene);
         ccStage.initModality(Modality.APPLICATION_MODAL);
         ccStage.show();

         //((Node)(actionEvent.getSource())).getScene().getWindow().hide();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void handlePlaceOrderButton(ActionEvent actionEvent) {
      if (choiceBoxCC.getValue() == null || choiceBoxAddress.getValue() == null) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Uh oh!");
         alert.setHeaderText(null);
         alert.setContentText("Please enter address and credit card information.");

         alert.showAndWait();

      }
      else if (tableID.getItems().isEmpty()) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Uh oh!");
         alert.setHeaderText(null);
         alert.setContentText("Your cart is empty. Please place some items " +
               "into your cart before placing an order.");
         alert.showAndWait();
      }
      else {
         co.placeOrder(choiceBoxCC.getValue());

         try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/grocery/confirmation" +
                  "/confirmation.fxml"));
            Parent confirmParent = loader.load();

            ConfirmationController controller = loader.getController();
            controller.initialize(co.getOid());

            Scene scene = new Scene(confirmParent);
            Stage confirmStage = new Stage();
            confirmStage.setTitle("Confirmation");
            confirmStage.setScene(scene);
            confirmStage.show();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

   }

   public void handleQuantityColumnEdit(TableColumn.CellEditEvent<CartCheckoutItem, String> event) {
      CartCheckoutItem tmpCartCheckoutItem = tableID.getSelectionModel().getSelectedItem();
      System.out.println(tmpCartCheckoutItem.getProdName());
      co.updateQuantity(event.getNewValue(), tmpCartCheckoutItem.getProdName());
      data = new CartCheckoutDatabase(cid, wid).getItems();
      co = new CartCheckoutDatabase(cid, wid);
      co.getData();

      // populate table with cart contents
      quantity.setCellFactory(TextFieldTableCell.forTableColumn());
      name.setCellValueFactory(new PropertyValueFactory<>("prodName"));
      quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
      price.setCellValueFactory(new PropertyValueFactory<>("price"));
      tableID.setItems(data);

      // populate total and balance labels
      totalLabel.setText(String.format("%.2f", (co.getSubtotal())));
      balanceLabel.setText(String.format("%.2f", (co.getBalance())));

   }

   public void handleChoiceBoxCCEvent(Event event) {
      co = new CartCheckoutDatabase(cid, wid);
      co.getData();
      choiceBoxCC.getItems().setAll(co.getCC_numbers());
   }
}