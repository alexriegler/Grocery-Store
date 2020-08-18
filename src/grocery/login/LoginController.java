package grocery.login;

import grocery.staff.StaffController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import grocery.database.Customer;
import grocery.home.HomeController;

import java.io.IOException;
import java.sql.SQLException;

import static grocery.database.LoginDatabase.checkCredentials;

public class LoginController
{
   private String userType;

   @FXML
   private Text welcomeText;
   @FXML
   private TextField usernameTextField;
   @FXML
   private PasswordField passwordField;
   @FXML
   private Text errorText;

   /**
    * Accepts a user type of "Staff" or "Customer" to initialize a login
    * @param userType the type of user
    */
   public void initData(String userType) {
      this.userType = userType;
      welcomeText.setText(userType + " Login");
   }

   /**
    * Checks the credentials entered by the user.
    * @param event
    */
   @FXML
   private void signIn(ActionEvent event) throws IOException {
      String username = usernameTextField.getText();
      String password = passwordField.getText();

      if (username.isEmpty() || password.isEmpty()) {
         errorText.setText("Enter a username and password.");
      }
      else if (checkCredentials(userType, username, password)) {
         // Get the window (stage) that was the source of the event
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

         // Get the text of the button pressed
         FXMLLoader loader = new FXMLLoader();

         if (userType.equals("Customer")) {
            loader.setLocation(
                  getClass().getResource("/grocery/home/home.fxml"));
            Parent homeParent = loader.load();

            Scene homeScene = new Scene(homeParent);

            // Access the controller and call a method
            HomeController controller = loader.getController();
            try {
               controller.initData(new Customer(username));
            } catch (SQLException e) {
               e.printStackTrace();
            }
            stage.setScene(homeScene);
         }
         else if (userType.equals("Staff")) {
            // TODO implement link to staff view
            loader.setLocation(
                  getClass().getResource("/grocery/staff/staff.fxml"));
            Parent staffParent = loader.load();

            Scene staffScene = new Scene(staffParent);

            // Access the controller and call a method
            StaffController controller = loader.getController();
            // TODO initData staff
//            try {
//               controller.initData(new Customer(username));
//            } catch (SQLException e) {
//               e.printStackTrace();
//            }
            stage.setScene(staffScene);
         }
         else {
            // If the button pressed was not Customer or Staff, then throw
            // exception.
            throw new IOException();
         }
      }
      else {
         errorText.setText("Invalid credentials.");
      }
   }
}
