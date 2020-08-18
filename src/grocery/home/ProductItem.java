package grocery.home;

import javafx.beans.property.SimpleStringProperty;

public class ProductItem {
   private final SimpleStringProperty prodName;
   private final SimpleStringProperty category;
   private final SimpleStringProperty price;
   private final SimpleStringProperty add_info;
   private final SimpleStringProperty description;

   public ProductItem(String pName, String category, String price,
                      String add_info, String description) {
      this.prodName = new SimpleStringProperty(pName);
      this.price = new SimpleStringProperty(price);
      this.category = new SimpleStringProperty(category);
      this.add_info = new SimpleStringProperty(add_info);
      this.description = new SimpleStringProperty(description);
   }

   public String getProdName()
   {
      return prodName.get();
   }

   public SimpleStringProperty prodNameProperty()
   {
      return prodName;
   }

   public void setProdName(String prodName)
   {
      this.prodName.set(prodName);
   }

   public String getCategory()
   {
      return category.get();
   }

   public SimpleStringProperty categoryProperty()
   {
      return category;
   }

   public void setCategory(String category)
   {
      this.category.set(category);
   }

   public String getPrice()
   {
      return price.get();
   }

   public SimpleStringProperty priceProperty()
   {
      return price;
   }

   public void setPrice(String price)
   {
      this.price.set(price);
   }

   public String getAdd_info()
   {
      return add_info.get();
   }

   public SimpleStringProperty add_infoProperty()
   {
      return add_info;
   }

   public void setAdd_info(String add_info)
   {
      this.add_info.set(add_info);
   }

   public String getDescription()
   {
      return description.get();
   }

   public SimpleStringProperty descriptionProperty()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description.set(description);
   }
}
