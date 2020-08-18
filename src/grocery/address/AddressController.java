package grocery.address;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * @author Martijn
 */
public class AddressController {

   @FXML
   private TextField streetNum;
   @FXML
   private TextField streetName;
   @FXML
   private TextField aptNum;
   @FXML
   private TextField city;
   @FXML
   private TextField zip;
   @FXML
   private TextField country;

   @FXML
   private ChoiceBox<String> stateBox;

   private int customerID;

   public void initialize(int customerID) {
      this.customerID = customerID;
      stateBox.getItems().addAll("Choose State", "AL", "AK", "AZ", "AR", "CA", "CO", "CT",
            "DC", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME",
            "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY",
            "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT",
            "VA", "WA", "WV", "WI", "WY");

      AddressDatabaseM cc = new AddressDatabaseM();
      stateBox.setValue("Choose State");
   }

   @FXML
   protected void handleSubmitButtonAction(ActionEvent actionEvent) {

      AddressDatabaseM cc = new AddressDatabaseM(streetNum.getText(),
            streetName.getText(), aptNum.getText(), city.getText(), stateBox.getValue(),
            zip.getText(), country.getText());
      cc.addAddress(customerID);


      if (cc.isSuccess()) {
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } else {
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setTitle("Error");
         alert.setHeaderText(null);
         alert.setContentText("There was an error updating your address.");

         alert.showAndWait();
      }
   }


}