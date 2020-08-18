package grocery.orders;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Martijn
 */
public class OrderItem
{

   private final SimpleStringProperty prodName;
   private final SimpleStringProperty quantity;
   private final SimpleStringProperty price;
   private final SimpleStringProperty dateIssued;
   private final SimpleStringProperty ccNum;
   private final SimpleStringProperty status;

   public OrderItem(String pName, String qty, String price, String dateIssued, String ccNum, String status) {
      this.prodName = new SimpleStringProperty(pName);
      this.quantity = new SimpleStringProperty(qty);
      this.price = new SimpleStringProperty(price);
      this.dateIssued = new SimpleStringProperty(dateIssued);
      this.ccNum = new SimpleStringProperty(ccNum);
      this.status = new SimpleStringProperty(status);
   }

   public String getDateIssued() {
      return dateIssued.get();
   }

   public SimpleStringProperty dateIssuedProperty() {
      return dateIssued;
   }

   public void setDateIssued(String dateIssued) {
      this.dateIssued.set(dateIssued);
   }

   public String getCcNum() {
      return ccNum.get();
   }

   public SimpleStringProperty ccNumProperty() {
      return ccNum;
   }

   public void setCcNum(String ccNum) {
      this.ccNum.set(ccNum);
   }

   public String getStatus() {
      return status.get();
   }

   public SimpleStringProperty statusProperty() {
      return status;
   }

   public void setStatus(String status) {
      this.status.set(status);
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