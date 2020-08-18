package grocery;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import grocery.login.LoginController;

import java.io.IOException;

public class MainController
{
   @FXML
   private Button customerButton;
   @FXML
   private Button staffButton;

   /**
    * Passes the user type associated with the button pressed to the
    * corresponding login.
    * @param event
    */
   @FXML
   private void userSelectButtonOnPress(ActionEvent event) throws IOException {
      // Get the window (stage) that was the source of the event
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

      // Get the text of the button pressed
      String buttonPressedText = ((Button) event.getSource()).getText();

      // Get the FXML of the login scene to switch to
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/grocery/login/login.fxml"));
      Parent loginParent = loader.load();

      Scene loginScene = new Scene(loginParent);

      // Access the controller and call a method
      LoginController controller = loader.getController();
      controller.initData(buttonPressedText);
      stage.setScene(loginScene);
   }
}
