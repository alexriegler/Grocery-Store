package grocery.cartCheckout;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Martijn
 */
public class CartCheckoutItem
{

   private final SimpleStringProperty prodName;
   private final SimpleStringProperty quantity;
   private final SimpleStringProperty price;

   public CartCheckoutItem(String pName, String qty, String price) {
      this.prodName = new SimpleStringProperty(pName);
      this.quantity = new SimpleStringProperty(qty);
      this.price = new SimpleStringProperty(price);
   }


   public String getProdName() {
      return prodName.get();
   }

   public SimpleStringProperty prodNameProperty() {
      return prodName;
   }

   public void setProdName(String prodName) {
      this.prodName.set(prodName);
   }

   public String getQuantity() {
      return quantity.get();
   }

   public SimpleStringProperty quantityProperty() {
      return quantity;
   }

   public void setQuantity(String quantity) {
      this.quantity.set(quantity);
   }

   public String getPrice() {
      return price.get();
   }

   public SimpleStringProperty priceProperty() {
      return price;
   }

   public void setPrice(String price) {
      this.price.set(price);
   }

}