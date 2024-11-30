import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLController {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button closeBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    private void initialize() {
        // Initialize any default values or setup if needed
    }

    @FXML
    private void loginAdmin() {
        String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
        connect = Database.connectDb(); // Assuming database.connectDb() connects to your DB

        try {
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                showAlert(AlertType.ERROR, "Error message", "Please complete all fields");
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();

            if (result.next()) {
                showAlert(AlertType.INFORMATION, "Information Message", "Successfully Logged In");

                // Open the Dash window
                Parent root = FXMLLoader.load(getClass().getResource("Dash.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();

                // Close the current login window
                closeCurrentStage();

            } else {
                showAlert(AlertType.ERROR, "Error Message", "Incorrect Username/Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while connecting to the database");
        }
    }

    @FXML
    private void close() {
        // Handle close action (exit the application)
        System.exit(0);
    }

    // Helper method to show an alert
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to close the current stage
    private void closeCurrentStage() {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.close();
    }
}
