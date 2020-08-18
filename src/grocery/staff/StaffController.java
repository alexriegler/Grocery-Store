package grocery.staff;

import grocery.home.ProductItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class StaffController
{
   @FXML
   private Text greetingText;
   @FXML
   private MenuButton accountMenuButton;
   @FXML
   private MenuItem profileMenuItem, orderMenuItem, cartMenuItem,
         switchAccountMenuItem, logOutMenuItem;
   @FXML
   private ComboBox<String> stateComboBox, categoryComboBox;
   @FXML
   private String produceItem, meatItem, eggsAndDairyItem, bakeryItem,
         frozenFoodsItem, snackFoodsItem, alcoholicDrinksItem;
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
   private TextField productTextField, categoryTextField, sizeTextField,
         addInfoTextField;
   @FXML
   private Button addButton, removeButton;
}
