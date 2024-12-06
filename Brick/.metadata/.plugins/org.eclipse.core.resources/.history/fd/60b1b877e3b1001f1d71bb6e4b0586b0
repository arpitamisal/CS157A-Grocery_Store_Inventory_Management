package Pages;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ExpiredWarrantyAsset {
    private final VBox root;
    private final HBox buttons;
    private final ListView<String> listView;
    private final Map<String, String> assetDetailsMap = new HashMap<>();

    public ExpiredWarrantyAsset() {
    	// UI variables being initialized 
        root = new VBox(20);
        buttons = new HBox(20);
        listView = new ListView<>();

        buttons.setStyle("-fx-padding: 10px;");
        root.setStyle("-fx-background-color: cornflowerblue;");

        // create label and button
        Label expiredAssetLbl = new Label("Assets with expired warranty");
        expiredAssetLbl.setStyle("-fx-font-size: 24px;");

        // add functionality to the button
        Button homeButton = new Button("Return to Homepage");
        homeButton.setOnAction(e -> {
            Stage primaryStage = (Stage) homeButton.getScene().getWindow();
            HomeScreen homeScreen = new HomeScreen(primaryStage);
            primaryStage.setScene(new Scene(homeScreen.getRoot(), 600, 600));
            primaryStage.setTitle("Home Page");
        });

        // add all of the nodes to the HBox and VBox
        buttons.getChildren().addAll(homeButton);
        root.getChildren().addAll(expiredAssetLbl, listView, buttons);

        setupListViewDoubleClick();
        loadExpiredWarranties();
    }

    // listener function to open the asset details window upon a double click
    private void setupListViewDoubleClick() {
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !listView.getSelectionModel().isEmpty()) {
                String selectedItem = listView.getSelectionModel().getSelectedItem();
                if (assetDetailsMap.containsKey(selectedItem)) {
                    openDetailWindow(assetDetailsMap.get(selectedItem));
                }
            }
        });
    }

    // function that opens the window with all of the assets details
    private void openDetailWindow(String assetDetails) {
        String[] parts = assetDetails.split(",");

        Stage detailStage = new Stage();
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: cornflowerblue; -fx-padding: 20;");

        // create labels that have all of the asset's information 
        Label assetNameLabel = new Label("Asset Name: " + (parts.length > 0 ? parts[0] : ""));
        Label assetCategoryLabel = new Label("Asset Category: " + (parts.length > 1 ? parts[1] : ""));
        Label assetLocationLabel = new Label("Asset Location: " + (parts.length > 2 ? parts[2] : ""));
        Label purchaseDateLabel = new Label("Purchase Date: " + (parts.length > 3 ? parts[3] : ""));
        Label assetDescriptionLabel = new Label("Asset Description: " + (parts.length > 4 ? parts[4] : ""));
        Label assetPurchaseValueLabel = new Label("Purchase Value: " + (parts.length > 5 ? parts[5] : ""));
        Label warrantyDateLabel = new Label("Warranty Date: " + (parts.length > 6 ? parts[6] : ""));

        layout.getChildren().addAll(
            assetNameLabel,
            assetCategoryLabel,
            assetLocationLabel,
            purchaseDateLabel,
            assetDescriptionLabel,
            assetPurchaseValueLabel,
            warrantyDateLabel
        );

        // button that closes the details window
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailStage.close());
        layout.getChildren().add(closeButton);

        Scene scene = new Scene(layout, 400, 300);
        detailStage.setScene(scene);
        detailStage.setTitle("Asset Details");
        detailStage.initModality(Modality.APPLICATION_MODAL);
        detailStage.showAndWait();
    }

    // function that loads the expired warranties onto the list 
    private void loadExpiredWarranties() {
        try (Stream<String> stream = Files.lines(Paths.get("asset_data.csv"))) {
            LocalDate today = LocalDate.now();
            stream.map(line -> line.split(","))
                  .filter(values -> LocalDate.parse(values[6]).isBefore(today))
                  .forEach(values -> {
                      String listViewKey = values[0] + " - Warranty Expired: " + values[6];
                      assetDetailsMap.put(listViewKey, String.join(", ", values));
                      listView.getItems().add(listViewKey);
                  });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // return the root of the class to allow other classes to access the stage
    public Parent getRoot() {
        return root;
    }
}