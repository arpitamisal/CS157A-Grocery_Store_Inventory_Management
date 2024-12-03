package Pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeAlertPage {
    private final VBox root;
    private final HBox buttons;
    private Stage primaryStage;

    public HomeAlertPage(Stage primaryStage) {
        this.primaryStage = primaryStage; // Use the same primary stage passed from main

        root = new VBox(20);
        root.setStyle("-fx-padding: 10px; -fx-background-color: cornflowerblue;");

        buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        Label warningLbl = new Label("Welcome to Grocery Store Inventory Management!");
        warningLbl.setStyle("-fx-font-size: 20px;");

        Button customerButton = new Button("Are you a customer?");
        Button employeeButton = new Button("Are you an employee?");
//
//        customerButton.setOnAction(e -> {
//        	CustomerHomePage cHomePage = new CustomerHomePage(primaryStage);
//            primaryStage.setScene(new Scene(cHomePage.getRoot(), 650, 650));
//            primaryStage.setTitle("Customer Home Page");
//        });

        employeeButton.setOnAction(e -> {
            EmployeeHomePage eHomePage = new EmployeeHomePage(primaryStage); // Assume ExpiredWarrantyAsset is correctly implemented
            primaryStage.setScene(new Scene(eHomePage.getRoot(), 600, 600));
            primaryStage.setTitle("Expired Warranty Assets");
        });

        // Add all of the buttons to the HBox then VBox
        buttons.getChildren().addAll(customerButton, employeeButton);
        root.getChildren().addAll(warningLbl, buttons);
        root.setAlignment(Pos.CENTER);
    } // end of WarrantyAlert()

    // Return the root of the class to allow other classes to access the stage
    public Parent getRoot() {
        return root;
    }
    
}