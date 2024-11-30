import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class SignIn extends Application {
    private Stage primaryStage;
    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        // Create a Scene
        Scene scene = new Scene(root);

        // Make the stage draggable
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            stage.setOpacity(0.8);
        });

        root.setOnMouseReleased((MouseEvent event) -> {
            stage.setOpacity(1);
        });

        stage.initStyle(StageStyle.TRANSPARENT);

        // Set up the stage
        stage.setScene(scene);
        stage.setTitle("Sign In");
        stage.show();

        this.primaryStage = stage;
    }

    // Method to set a new root for the stage
    void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    // Method to load FXML files
    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
