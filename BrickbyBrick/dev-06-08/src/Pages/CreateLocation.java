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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateLocation {
	// create some variables 
	private final VBox locationInput;
	private final HBox buttons1;
	private final Stage primaryStage = new Stage();
	
	public CreateLocation() {
		locationInput = new VBox(20);
		locationInput.setStyle("-fx-padding: 10px;");
		
		// instantiate buttons1 and also align it to the center of the screen
		buttons1 = new HBox(20);
		buttons1.setAlignment(Pos.CENTER); 
		
		locationInput.setStyle("-fx-background-color: cornflowerblue;"); // changes the color of the background
		
		// Create a label and button nodes 
		Label locationLabel = new Label("Create a new Asset Location");
		locationLabel.setStyle("-fx-font-size: 24px;"); // Change the font size of locationLabel
		Button homePageButton1 = new Button("Return to the Homepage");
		Button finalizeLocationButton = new Button("Create Asset Location");
		
		// Create a text field for the name of the asset location
        Label assetLocationLbl = new Label("Name of Asset Location *Required*");
        TextField assetLocation = new TextField();
        
        // Create a text field for the description of the asset location 
        Label assetLocationDescLbl = new Label("Description of Asset Location *Optional*");
        TextArea assetLocationDesc = new TextArea();
        
		
		homePageButton1.setOnAction( e -> {
			System.out.println("Back to the home page."); // Print message to the user 
			
			// Return to the home page through a button click
			Stage primaryStage = (Stage) homePageButton1.getScene().getWindow();
			HomeScreen homeScreen1 = new HomeScreen(primaryStage);
			primaryStage.setScene(new Scene(homeScreen1.getRoot(), 600, 600));
			primaryStage.setTitle("Home Page");
			
		});
		
		finalizeLocationButton.setOnAction(e -> {			
			String assetLocationText = assetLocation.getText(); // Retrieve data from text field
			String assetLocationDescText = assetLocationDesc.getText();	
			
			// 
		    if(assetLocationText.isEmpty()) {	
		    	// Create an alert and do not allow the user to create an entry if the text field is empty
		    	Alert alert2 = new Alert(Alert.AlertType.ERROR);
		    	alert2.setTitle("Error");
		    	alert2.setHeaderText(null);
		    	alert2.setContentText("Please fill in the asset location.");
		    	alert2.showAndWait();
		    } else {
			    // Write the data to the flat file location_data.txt
			    try (BufferedWriter writer = new BufferedWriter(new FileWriter("location_data.txt", true))) {
			        writer.write(assetLocationText); // write the asset location to the file
			        writer.write("\t"); // separate name and description by a tab 
			        writer.write(assetLocationDescText); // write the asset location description to the file
			        writer.newLine(); // Add a new line for the next entry
			        writer.flush(); // Flush the writer to ensure data is written immediately
				    System.out.println("Asset Location Successfully Created");
				    
				    // Clear the text fields upon creating a new asset location 
				    assetLocation.clear();
				    assetLocationDesc.clear();
				    
				    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Asset Location Created Successfully and data has been written to the file.");
                    alert.showAndWait();
				
				    // after creating the location, return to the home page
				    Stage primaryStage = (Stage) homePageButton1.getScene().getWindow();
					HomeScreen homeScreen1 = new HomeScreen(primaryStage);
					primaryStage.setScene(new Scene(homeScreen1.getRoot(), 800, 800));
					primaryStage.setTitle("Home Page");
			    } catch (IOException ex) {
					ex.printStackTrace(); // Handle exception appropriately
				} // end of try-catch
		    } // end of else 
			
		}); // end of finalizeLocationButton
		
		
        
        // Add the button nodes to the HBox
     	buttons1.getChildren().addAll(homePageButton1, finalizeLocationButton);
		
		// Add all of the nodes into the HBox
		locationInput.getChildren().addAll(locationLabel, assetLocationLbl, assetLocation, assetLocationDescLbl, assetLocationDesc, buttons1);
	}
	
	// return the VBox/stage 
    public Parent getRoot() {
        return locationInput;
    }
	
}