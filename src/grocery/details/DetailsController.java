package grocery.details;

import grocery.home.HomeDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailsController {
   @FXML
   private ImageView descriptionImageView;
   @FXML
   private Text productNameText, categoryText, addInfoText, priceText,
         quantityText;
   @FXML
   private Button increaseQuantityButton, decreaseQuantityButton, addToCartButton;

   private int customerID;
   private String productName, category, addInfo, price, description;

   public void initData(int customerID, String productName, String category,
                        String addInfo,
                        String price, String description) {
      this.customerID = customerID;
      this.productName = productName;
      this.category = category;
      this.addInfo = addInfo;
      this.price = price;
      this.description = description;

      productNameText.setText(productName);
      categoryText.setText(category);
      addInfoText.setText(addInfo);
      priceText.setText(price);
      DetailsDatabase img = new DetailsDatabase(productName);
      img.getImg();
      File file = new File("./img/" + img.getURL());
      Image image = new Image(file.toURI().toString());
      descriptionImageView.setImage(image);
   }

   /**
    * Increases the quantity text.
    * @param event on + button press
    */
   @FXML
   private void increaseQuantityButtonOnPress(ActionEvent event) {
      int quantity = Integer.parseInt(quantityText.getText());
      if (quantity < 100) {
         quantity++;
         String newQuantity = "" + quantity;
         quantityText.setText(newQuantity);
      }
   }

   /**
    * Decrease the quantity text.
    * @param event on - button press
    */
   @FXML
   private void decreaseQuantityButtonOnPress(ActionEvent event) {
      int quantity = Integer.parseInt(quantityText.getText());
      if (quantity > 1) {
         quantity--;
         String newQuantity = "" + quantity;
         quantityText.setText(newQuantity);
      }
   }

   /**
    * Add item to cart.
    * @param event on add item to cart button press
    */
   @FXML
   private void addToCartButtonOnPress(ActionEvent event) {
      int quantity = Integer.parseInt(quantityText.getText());
      HomeDatabase homeDatabase = new HomeDatabase();
      homeDatabase.addItemToCart(customerID, productName, quantity);
   }
}

