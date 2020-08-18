package grocery.creditcard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * @author Martijn
 */
public class CreditCardController
{

    @FXML private TextField nameField;
    @FXML private TextField numberField;
    @FXML private TextField cvvField;
    @FXML private TextField exp_dateMonth;
    @FXML private TextField exp_dateYear;
    @FXML private TextField streetNum;
    @FXML private TextField streetName;
    @FXML private TextField aptNum;
    @FXML private TextField city;
    @FXML private TextField zip;
    @FXML private TextField country;

    @FXML private ChoiceBox<String> stateBox;
    @FXML private ChoiceBox<String> addressBox;

    private int customerID;

    public void initialize(int customerID) {
        this.customerID = customerID;
        System.out.println(customerID);
        stateBox.getItems().addAll( "Choose State", "AL", "AK", "AZ", "AR", "CA", "CO", "CT",
                "DC", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME",
                "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY",
                "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT",
                "VA", "WA", "WV", "WI", "WY");

        CreditCard creditCard = new CreditCard();
        creditCard.getAddress(customerID);
        addressBox.getItems().setAll(creditCard.getAddress_id());
        addressBox.setOnAction(this::boxAction);
        addressBox.setValue("Choose Address");
        stateBox.setValue("Choose State");
    }


    @FXML
    protected void handleSubmitButtonAction(ActionEvent actionEvent) {

        if (nameField.getText() == null || nameField.getText().trim().isEmpty() ||
                numberField.getText() == null || numberField.getText().trim().isEmpty() ||
                cvvField.getText() == null || cvvField.getText().trim().isEmpty() ||
                exp_dateMonth.getText() == null || exp_dateMonth.getText().trim().isEmpty() ||
                exp_dateYear.getText() == null || exp_dateYear.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please complete all fields.");

            alert.showAndWait();
        }
        else
        {
            CreditCard creditCard;
            if (addressBox.getValue().equals("Choose Address") || addressBox.getValue() == null) {
                creditCard = new CreditCard(nameField.getText(), numberField.getText(), cvvField.getText(),
                        exp_dateMonth.getText(), exp_dateYear.getText(), streetNum.getText(),
                        streetName.getText(), aptNum.getText(), city.getText(), stateBox.getValue(),
                        zip.getText(), country.getText());
                creditCard.addAddress();
            }
            else {
                creditCard = new CreditCard(nameField.getText(), numberField.getText(), cvvField.getText(),
                        exp_dateMonth.getText(), exp_dateYear.getText(), Integer.parseInt(addressBox.getValue()));
            }
            creditCard.addCC(customerID);

            if (creditCard.isSuccess()) {
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("There was an error updating your credit card information.");

                alert.showAndWait();
            }
        }

    }

    public void boxAction(ActionEvent actionEvent) {
        if (addressBox.getValue().equals("Choose Address")) {
            streetNum.setDisable(false);
            streetName.setDisable(false);
            aptNum.setDisable(false);
            city.setDisable(false);
            zip.setDisable(false);
            country.setDisable(false);
            stateBox.setDisable(false);
            streetNum.setText(null);
            streetName.setText(null);
            aptNum.setText(null);
            city.setText(null);
            stateBox.setValue(null);
            zip.setText(null);
            country.setText(null);
        }
        else {
            CreditCard creditCard = new CreditCard();
            creditCard.getAddressDropDown(Integer.parseInt(addressBox.getValue()));
            streetNum.setDisable(true);
            streetName.setDisable(true);
            aptNum.setDisable(true);
            city.setDisable(true);
            zip.setDisable(true);
            country.setDisable(true);
            stateBox.setDisable(true);
            streetNum.setText(creditCard.getStreetNum());
            streetName.setText(creditCard.getStreetName());
            aptNum.setText(creditCard.getAptNum());
            city.setText(creditCard.getCity());
            stateBox.setValue(creditCard.getState());
            zip.setText(creditCard.getZip());
            country.setText(creditCard.getCountry());
        }
    }

}

