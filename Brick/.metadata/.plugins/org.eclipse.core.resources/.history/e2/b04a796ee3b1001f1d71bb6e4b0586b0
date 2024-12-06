package Pages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateCategory {
	// create some variables
    private final VBox categoryInputs;
    private final HBox buttons;
    private final Stage primaryStage = new Stage();

    public CreateCategory() {
    	// instantiate the VBox 
        categoryInputs = new VBox(20);
        categoryInputs.setStyle("-fx-padding: 10px;");

        buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        // Create a label and button nodes 
        Label categoryLabel = new Label("Create a new Asset Category");
		categoryLabel.setStyle("-fx-font-size: 24px;"); // Change the font size of categoryLabel
        Button homePageButton = new Button("Return to Homepage");
        Button finalizeCategoryButton = new Button("Create Asset Category");
        
        // create text field for the name of the asset
        Label assetCategoryNameLbl = new Label("Name of Asset Category *Required*");
        TextField assetCategoryName = new TextField();
        
		categoryInputs.setStyle("-fx-background-color: cornflowerblue;"); // changes the color of the background 

        // Give functionality to button to return to the home page 
        homePageButton.setOnAction(e -> {
            System.out.println("Back to the Homepage"); // test to prove functionality
            
            // Return to the home page through the button click
            Stage primaryStage = (Stage) homePageButton.getScene().getWindow();
            HomeScreen homeScreen = new HomeScreen(primaryStage);
            primaryStage.setScene(new Scene(homeScreen.getRoot(), 600, 600));
            primaryStage.setTitle("Home Page");
        });		

		// Inside the qCreateAsset class
		finalizeCategoryButton.setOnAction(e -> {
		    // Get the text from the text fields
		    String assetCategoryText = assetCategoryName.getText();
		    
		    if(assetCategoryText.isEmpty()) {
		    	// Create an alert and do not allow the user to create an entry if the text field is empty
		    	Alert alert1 = new Alert(Alert.AlertType.ERROR);
		    	alert1.setTitle("Error");
		    	alert1.setHeaderText(null);
		    	alert1.setContentText("Please fill in the asset category.");
		    	alert1.showAndWait();
		    } else {
			    // Write the data to the flat file category_data.txt
			    try (BufferedWriter writer = new BufferedWriter(new FileWriter("category_data.txt", true))) {
			        writer.write(assetCategoryText); // Write the data to the file
			        writer.newLine(); // Add a new line for the next entry
			        writer.flush(); // Flush the writer to ensure data is written immediately
				    System.out.println("Asset Category Successfully Created");
				    
				    assetCategoryName.clear(); // Clear out the text field upon creating a new asset category
				
				    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Asset Category Created Successfully and data has been written to the file.");
                    alert.showAndWait();
				    
				    // after creating the category, return to the home page
				    Stage primaryStage = (Stage) homePageButton.getScene().getWindow();
		            HomeScreen homeScreen = new HomeScreen(primaryStage);
		            primaryStage.setScene(new Scene(homeScreen.getRoot(), 800, 800));
		            primaryStage.setTitle("Home Page");
			    } catch (IOException ex) {
					ex.printStackTrace(); // Handle exception
				} // end of try-catch
			    
		    } // end of else 
		      
		}); // end of finalizeLocationButton 


        buttons.getChildren().addAll(homePageButton, finalizeCategoryButton);
        
        // add all of the nodes to the VBox
        categoryInputs.getChildren().addAll(categoryLabel,assetCategoryNameLbl, assetCategoryName, buttons);
    }

    // return the VBox/stage 
    public Parent getRoot() {
        return categoryInputs;
    }
}