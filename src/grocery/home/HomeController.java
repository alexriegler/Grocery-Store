package grocery.home;

import grocery.cartCheckout.CartCheckoutController;
import grocery.details.DetailsController;
import grocery.orders.OrdersController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import grocery.database.Address;
import grocery.database.Customer;
import grocery.login.LoginController;

import java.io.IOException;
import java.util.List;

public class HomeController
{
   @FXML
   private Text balanceAmountText;
   @FXML
   private Text greetingText;
   @FXML
   private MenuButton accountMenuButton;
   @FXML
   private MenuItem profileMenuItem;
   @FXML
   private MenuItem orderMenuItem;
   @FXML
   private MenuItem cartMenuItem;
   @FXML
   private MenuItem switchAccountMenuItem;
   @FXML
   private MenuItem logOutMenuItem;
   @FXML
   private ComboBox<String> stateComboBox;
   @FXML
   private ComboBox categoryComboBox;
   @FXML
   private String produceItem;
   @FXML
   private String meatItem;
   @FXML
   private String eggsAndDairyItem;
   @FXML
   private String bakeryItem;
   @FXML
   private String frozenFoodsItem;
   @FXML
   private String snackFoodsItem;
   @FXML
   private String alcoholicDrinksItem;
   @FXML
   private TextField searchTextField;
   @FXML
   private Button searchButton;
   @FXML
   private TableView<ProductItem> catalogTableView;
   @FXML
   private TableColumn<ProductItem, String> productTableColumn,
         categoryTableColumn, priceTableColumn, addInfoTableColumn;
   @FXML
   private Button detailsButton;
   @FXML
   private Button addToCartButton;
   @FXML
   private Button cartButton;

   private Customer customer;
   private ObservableList<ProductItem> productItems;

   /**
    * Initializes the customer variable of the HomeController with the
    * current customer.
    *
    * @param customer the customer using the application
    */
   public void initData(Customer customer) {
      this.customer = customer;
      setBalanceAmountText();
      setGreetingText();
      setStateComboBoxOptions();
      productTableColumn.setCellValueFactory(new PropertyValueFactory<>("prodName"));
      categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
      priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
      addInfoTableColumn.setCellValueFactory(new PropertyValueFactory<>("add_info"));
      onStateSelectAction(null);
   }

   /**
    * Sets the balanceAmountText to the customer's current balance.
    */
   private void setBalanceAmountText() {
      double balance = customer.getBalance();
      String balanceString = String.format("%.2f", balance);
      balanceAmountText.setText(balanceString);
   }

   /**
    * Sets the greeting text to "Hello, " + firstName.
    */
   private void setGreetingText() {
      String[] splitName = customer.getCustomerName().split(" ");
      String firstName = splitName[0];
      greetingText.setText("Hello, " + firstName);
   }

   /**
    * Sets the available state options in the state combo box.
    */
   private void setStateComboBoxOptions() {
      List<Address> addresses = customer.getDeliveryAddresses();
      for (Address address : addresses) {
         String state = address.getState();
         stateComboBox.getItems().add(state);
      }
      stateComboBox.getSelectionModel().selectFirst();
   }

   @FXML
    private void profileMenuItemOnPress(ActionEvent event) {
       // TODO Implement profileMenuItemOnPress()
   }

   /**
    * Goes to the customer's orders screen.
    * @param event on pressing the order menu item
    */
    @FXML
    private void orderMenuItemOnPress(ActionEvent event) {
       try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("/grocery/orders" +
                "/orders.fxml"));
          Parent ordersParent = loader.load();

          OrdersController controller = loader.getController();
          controller.initialize(customer.getCustomerID());

          Scene scene = new Scene(ordersParent);
          Stage stage = new Stage();
          stage.setTitle("Orders");
          stage.setScene(scene);
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.show();
       } catch (IOException e) {
          e.printStackTrace();
       }
    }

    private void goToCartCheckout() {
       try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("/grocery/cartCheckout" +
                "/cartCheckout.fxml"));
          Parent cartCheckoutParent = loader.load();

          CartCheckoutController controller = loader.getController();
          controller.initialize(customer.getCustomerID(),
                new HomeDatabase().getWarehouseID(stateComboBox.getValue()));

          Scene scene = new Scene(cartCheckoutParent);
          Stage stage = new Stage();
          stage.setTitle("Cart");
          stage.setScene(scene);
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.show();
       } catch (IOException e) {
          e.printStackTrace();
       }
    }

   /**
    * Go to cart after pressing button.
    * @param event cart menu item on press
    */
    @FXML
    private void cartMenuItemOnPress(ActionEvent event) {
       goToCartCheckout();
    }

   /**
    * Logs the user out and changes the scene to the customer login.
    * @param event pressing the switch account button
    */
    @FXML
    private void switchAccountMenuItemOnPress(ActionEvent event) {
       try {
          // Use greetingText to get current window
          Stage stage = (Stage) greetingText.getScene().getWindow();
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().getResource("/grocery/login/login.fxml"));
          Parent loginParent = loader.load();
          Scene loginScene = new Scene(loginParent);
          LoginController controller = loader.getController();
          controller.initData("Customer");
          stage.setScene(loginScene);
       } catch (IOException e) {
          e.printStackTrace();
       }
    }

   /**
    * Logs the user out and switches the scene to the main scene.
    * @param event pressing the log out button
    */
    @FXML
    private void logOutMenuItemOnPress(ActionEvent event) {
       try {
          // Use greetingText to get current window
          Stage stage = (Stage) greetingText.getScene().getWindow();
          Parent root = FXMLLoader.load(
                getClass().getResource("/grocery/main.fxml"));
          Scene mainScene = new Scene(root);
          stage.setScene(mainScene);
          stage.show();
       } catch (IOException e) {
          e.printStackTrace();
       }
    }

   /**
    * Searches for products that are similar to the text entered in the
    * search text field.
    * @param event on search button press
    */
    @FXML
    private void searchButtonOnPress(ActionEvent event) {
       productItems =
             new HomeDatabase().getProductItemsSearch((String) stateComboBox.getValue(), searchTextField.getText());
       catalogTableView.setItems(productItems);
    }

    @FXML
    private void detailsButtonOnPress(ActionEvent event) {
       try {
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(getClass().
                getResource("/grocery/details/details.fxml"));
          Parent detailsParent = loader.load();

          DetailsController controller = loader.getController();
          ProductItem selectedProduct =
                catalogTableView.getSelectionModel().getSelectedItem();
          controller.initData(customer.getCustomerID(), selectedProduct.getProdName(),
                selectedProduct.getCategory(),
                selectedProduct.getAdd_info(), selectedProduct.getPrice(),
                selectedProduct.getDescription());

          Scene scene = new Scene(detailsParent);
          Stage stage = new Stage();
          stage.setTitle(selectedProduct.getProdName());
          stage.setScene(scene);
          stage.initModality(Modality.APPLICATION_MODAL);
          stage.show();
       } catch (IOException e) {
          e.printStackTrace();
       }
    }

   /**
    * Adds an item to the customer's cart.
    * @param event on press add to cart button
    */
    @FXML
    private void addToCartButtonOnPress(ActionEvent event) {
       ProductItem selectedProduct =
             catalogTableView.getSelectionModel().getSelectedItem();
       HomeDatabase homeDatabase = new HomeDatabase();
       homeDatabase.addItemToCart(customer.getCustomerID(),
             selectedProduct.getProdName());
    }

   /**
    * Goes to cart after pressing button.
    * @param event cart button on press
    */
    @FXML
    private void cartButtonOnPress(ActionEvent event) {
       goToCartCheckout();
    }

   /**
    * Populates the product catalog with items from the customer's state.
    * @param event select state
    */
    @FXML
   private void onStateSelectAction(ActionEvent event) {
       productItems =
             new HomeDatabase().getProductItems((String) stateComboBox.getValue());
       catalogTableView.setItems(productItems);
    }

   /**
    * Populates the product catalog by category.
    * @param event on select category
    */
    @FXML
   private void onCategorySelectAction(ActionEvent event) {
       productItems =
             new HomeDatabase().getProductItemsByCategory((String) stateComboBox.getValue(), (String) categoryComboBox.getValue());
       catalogTableView.setItems(productItems);
    }
}
