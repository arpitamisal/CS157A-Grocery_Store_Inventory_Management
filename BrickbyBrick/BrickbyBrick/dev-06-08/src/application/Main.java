package application;

import Pages.WarrantyAlert;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create an instance of WarrantyAlert by passing the primaryStage
            WarrantyAlert warrantyAlert = new WarrantyAlert(primaryStage);

            // Set the scene with the root of the WarrantyAlert
            primaryStage.setScene(new Scene(warrantyAlert.getRoot(), 600, 400));
            primaryStage.setTitle("Brick by Brick"); // Title of the stage
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
