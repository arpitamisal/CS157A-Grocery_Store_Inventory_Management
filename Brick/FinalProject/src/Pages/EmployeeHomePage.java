package Pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class EmployeeHomePage {

	// creating some variables 
	private final VBox root;
	private final HBox buttons;
	private final HBox buttons4;
	private final Stage primaryStage;
	
	public EmployeeHomePage(Stage primaryStage) {
		
		// create stage and also instantiate variables, HBox and VBox
		this.primaryStage = primaryStage; // assigning the passed stage 
		buttons = new HBox(20);
		root = new VBox(20);
		buttons4 = new HBox(20);
		root.setStyle("-fx-padding: 10px;");
		root.setAlignment(Pos.CENTER); // Align the VBox to the center
	
		root.setStyle("-fx-background-color: cornflowerblue;"); // changes the color of the background 
		
		// Create the home page label and button 
		Label MainLabel = new Label("Welcome to the Employee Home Page");
		MainLabel.setStyle("-fx-font-size: 24px;"); // Increase the font size of the MainLabel
		Button manageProductButton = new Button("Manage Products");
		Button trackInventoryButton = new Button("Track Inventory");
		Button processOrdersButton = new Button("Process Orders");
		Button manageDiscountsButton = new Button("Manage Discounts");
		Button viewCustomerInfoButton = new Button("View Employee Information");
		Button storeLocationsButton = new Button("Store Locations");
		Button viewSupplierDetailsButton = new Button("View Supplier Details");
		
		// add the button to the HBox and align the HBox to the center
		buttons.getChildren().addAll(manageProductButton, trackInventoryButton, processOrdersButton, manageDiscountsButton);
		buttons.setAlignment(Pos.CENTER);
		
		buttons4.getChildren().addAll(viewCustomerInfoButton, storeLocationsButton, viewSupplierDetailsButton);
		buttons4.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(MainLabel, buttons, buttons4); // add them all to the VBox
		
		
		// Give functionality to the manageProductButton
		manageProductButton.setOnAction(e -> {			
			ManageProduct manageP = new ManageProduct(primaryStage); // create instance of CreateAsset class
			primaryStage.setScene(new Scene(manageP.getRoot(), 1100, 600));
			primaryStage.setTitle("Mange Products");
		});
		
		// Give functionality to the trackInventoryButton
		trackInventoryButton.setOnAction(e -> {			
			TrackInventory trackInv = new TrackInventory(primaryStage); // create instance of CreateLocation class
			primaryStage.setScene(new Scene(trackInv.getRoot(), 1100, 550));
			primaryStage.setTitle("Track Inventory");
		});
		
		// Give functionality to the processOrdersButton
		processOrdersButton.setOnAction(e -> {			
			ProcessOrders procOrders = new ProcessOrders(primaryStage); // create instance of CreateAsset class
			primaryStage.setScene(new Scene(procOrders.getRoot(), 1100, 600));
			primaryStage.setTitle("Process Orders");
		});
	
		// Give functionality to the manageDiscountsButton
		manageDiscountsButton.setOnAction(e -> {			
			ManageDiscounts manageDisc = new ManageDiscounts(primaryStage);
			primaryStage.setScene(new Scene(manageDisc.getRoot(), 1000, 600)); // create instance of searchAsset class
			primaryStage.setTitle("Manage Discounts");
		});
		
		// Give functionality to the viewCustomerInfoButton
		viewCustomerInfoButton.setOnAction(e -> {					
			ViewEmployeeInformation viewCustInfo = new ViewEmployeeInformation(primaryStage);
			primaryStage.setScene(new Scene(viewCustInfo.getRoot(), 1100, 500)); // create instance of expiredWarrantyAsset class
			primaryStage.setTitle("View Customer Information");
		});
		
		// Give functionality to the storeLocationButton
		storeLocationsButton.setOnAction(e -> {
			StoreLocations reportsButton = new StoreLocations(primaryStage);
			primaryStage.setScene(new Scene(reportsButton.getRoot(), 1100, 600)); // create instance of searchByCategory class
			primaryStage.setTitle("Store Locations");
		});
		
		// Give functionality to the viewSupplierDetailsButton
		viewSupplierDetailsButton.setOnAction(e -> {
					
			SupplierDetails supplierDetailsButton = new SupplierDetails(primaryStage);
			primaryStage.setScene(new Scene(supplierDetailsButton.getRoot(), 1300, 600)); // create instance of searchByLocation class
			primaryStage.setTitle("View Supplier Details");
		});
		
	}
	
	// function to return the VBox/stage 
	public Parent getRoot() {
        return root;
    }
	
}
