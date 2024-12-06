package Pages;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ManageDiscounts {

	// creating some variables 
	private final VBox root;
	private final HBox buttons;
	private final HBox buttons4;
	private final Stage primaryStage;
	
	public ManageDiscounts(Stage primaryStage) {
		
		// create stage and also instantiate variables, HBox and VBox
		this.primaryStage = primaryStage; // assigning the passed stage 
		buttons = new HBox(20);
		root = new VBox(20);
		buttons4 = new HBox(20);
		root.setStyle("-fx-padding: 10px;");
		root.setAlignment(Pos.CENTER); // Align the VBox to the center
	
		root.setStyle("-fx-background-color: cornflowerblue;"); // changes the color of the background 
		
		// Create the home page label and button 
		Label MainLabel = new Label("Manage Discounts");
		MainLabel.setStyle("-fx-font-size: 24px;"); // Increase the font size of the MainLabel
		Button createDiscountsButton = new Button("Create Discounts");
		Button updateDiscountsButton = new Button("Update Discounts");
		Button deleteDiscountsButton = new Button("Delete Discounts");
		Button returnToHomePageButton = new Button("Return to Homepage");
		
		// add the button to the HBox and align the HBox to the center
		buttons.getChildren().addAll(createDiscountsButton, updateDiscountsButton, deleteDiscountsButton);
		buttons.setAlignment(Pos.CENTER);
		
		buttons4.getChildren().addAll(returnToHomePageButton);
		buttons4.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(MainLabel, buttons, buttons4); // add them all to the VBox
		
		createDiscountsButton.setOnAction(e -> {			
			CreateCategory createAsset = new CreateCategory(); // create instance of CreateAsset class
			primaryStage.setScene(new Scene(createAsset.getRoot(), 600, 600));
			primaryStage.setTitle("Create New Asset Category");
		});
		
		updateDiscountsButton.setOnAction(e -> {
			
			CreateLocation createLocation = new CreateLocation(); // create instance of CreateLocation class
			primaryStage.setScene(new Scene(createLocation.getRoot(), 600, 600));
			primaryStage.setTitle("Create New Asset Location");
		});
		
		deleteDiscountsButton.setOnAction(e -> {
			
			CreateAsset createAsset = new CreateAsset(); // create instance of CreateAsset class
			primaryStage.setScene(new Scene(createAsset.getRoot(), 800, 800));
			primaryStage.setTitle("Create New Asset");
		});
		
		// Give functionality to the searchAssetButton
		returnToHomePageButton.setOnAction(e -> {
			System.out.println("Welcome back to the home page");
			
			EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
			primaryStage.setScene(new Scene(empHomePage.getRoot(), 600, 600)); // create instance of searchAsset class
			primaryStage.setTitle("Search Asset(s)");
		});

		
	}
	
	// function to return the VBox/stage 
	public Parent getRoot() {
        return root;
    }
	
}