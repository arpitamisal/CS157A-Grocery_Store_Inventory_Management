package Pages;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class TrackInventory {

	// creating some variables 
	private final VBox root;
	private final HBox buttons;
	private final HBox buttons4;
	private final Stage primaryStage;
	
	public TrackInventory(Stage primaryStage) {
		
		// create stage and also instantiate variables, HBox and VBox
		this.primaryStage = primaryStage; // assigning the passed stage 
		buttons = new HBox(20);
		root = new VBox(20);
		buttons4 = new HBox(20);
		root.setStyle("-fx-padding: 10px;");
		root.setAlignment(Pos.CENTER); // Align the VBox to the center
	
		root.setStyle("-fx-background-color: cornflowerblue;"); // changes the color of the background 
		
		// Create the home page label and button 
		Label MainLabel = new Label("Track Inventory");
		MainLabel.setStyle("-fx-font-size: 24px;"); // Increase the font size of the MainLabel
		Button trackStockLevelsButton = new Button("Track Stock Levels");
		Button updateStockQuantityButton = new Button("Update Stock Quantity");
		Button setReorderLevelsButton = new Button("Set Reorder Levels");
		Button returnToHomePageButton = new Button("Return to Homepage");
		
		// add the button to the HBox and align the HBox to the center
		buttons.getChildren().addAll(trackStockLevelsButton, updateStockQuantityButton, setReorderLevelsButton);
		buttons.setAlignment(Pos.CENTER);
		
		buttons4.getChildren().addAll(returnToHomePageButton);
		buttons4.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(MainLabel, buttons, buttons4); // add them all to the VBox
		
		
		// Give functionality to the createAssetCategoryButton
		trackStockLevelsButton.setOnAction(e -> {
			System.out.println("Welcome to the Create New Asset Category Page"); // test print 
			
			CreateCategory createAsset = new CreateCategory(); // create instance of CreateAsset class
			primaryStage.setScene(new Scene(createAsset.getRoot(), 600, 600));
			primaryStage.setTitle("Create New Asset Category");
		});
		
		// Give functionality to the createAssetLocationButton
		updateStockQuantityButton.setOnAction(e -> {
			System.out.println("Welcome to the Create New Asset Location Page"); // test print 
			
			CreateLocation createLocation = new CreateLocation(); // create instance of CreateLocation class
			primaryStage.setScene(new Scene(createLocation.getRoot(), 600, 600));
			primaryStage.setTitle("Create New Asset Location");
		});
		
		// Give functionality to the createAssetButton
		setReorderLevelsButton.setOnAction(e -> {
			System.out.println("Welcome to the Create New Asset Page");
			
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