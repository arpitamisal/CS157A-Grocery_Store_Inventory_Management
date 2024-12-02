package Pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WarrantyAlert {
    private final VBox root;
    private final HBox buttons;
    private Stage primaryStage;

    public WarrantyAlert(Stage primaryStage) {
        this.primaryStage = primaryStage; // Use the same primary stage passed from main

        root = new VBox(20);
        root.setStyle("-fx-padding: 10px; -fx-background-color: cornflowerblue;");

        buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Label warningLbl = new Label("Warning! Some of your asset's have expired warranties!");
        warningLbl.setStyle("-fx-font-size: 20px;");

        Button homeButton = new Button("OK");
        Button expiredWarrantyAssetButton = new Button("Show Me");

        homeButton.setOnAction(e -> {
            HomeScreen homeScreen = new HomeScreen(primaryStage); // Assume HomeScreen is correctly implemented
            primaryStage.setScene(new Scene(homeScreen.getRoot(), 650, 650));
            primaryStage.setTitle("Home Page");
        });

        expiredWarrantyAssetButton.setOnAction(e -> {
            ExpiredWarrantyAsset expiredWarrantyAsset = new ExpiredWarrantyAsset(); // Assume ExpiredWarrantyAsset is correctly implemented
            primaryStage.setScene(new Scene(expiredWarrantyAsset.getRoot(), 600, 600));
            primaryStage.setTitle("Expired Warranty Assets");
        });

        // Add all of the buttons to the HBox then VBox
        buttons.getChildren().addAll(homeButton, expiredWarrantyAssetButton);
        root.getChildren().addAll(warningLbl, buttons);
        root.setAlignment(Pos.CENTER);
    } // end of WarrantyAlert()

    // Return the root of the class to allow other classes to access the stage
    public Parent getRoot() {
        return root;
    }
    
}
