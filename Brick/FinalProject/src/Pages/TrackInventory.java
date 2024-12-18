package Pages;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Inventory;
import models.Product;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackInventory {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final VBox leftPane;
    private final Stage primaryStage;
    private final TableView<Inventory> inventoryTable;

    public TrackInventory(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Root layout
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title Label
        Label mainLabel = new Label("Track Inventory");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search inventory...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons
        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button trackStockLevelsButton = new Button("Track Stock Levels");
        Button updateStockQuantityButton = new Button("Update Stock Quantity");
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(trackStockLevelsButton, updateStockQuantityButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // Left pane (search and buttons)
        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        // TableView for inventory data
        inventoryTable = new TableView<>();
        inventoryTable.setPrefWidth(800); // Increased width for better visibility
        inventoryTable.setPlaceholder(new Label("No inventory data found."));
        setupTableColumns();

        // Main layout: left pane and inventory table
        mainLayout = new HBox(20, leftPane, inventoryTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        // Add components to root
        root.getChildren().addAll(mainLabel, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, trackStockLevelsButton, updateStockQuantityButton, returnToHomePageButton);
    }

    private void setupTableColumns() {
        TableColumn<Inventory, Integer> inventoryIdColumn = new TableColumn<>("Inventory ID");
        inventoryIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getInventoryId()).asObject());
        inventoryIdColumn.setPrefWidth(100);

        TableColumn<Inventory, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getProductId()).asObject());
        productIdColumn.setPrefWidth(100);

        TableColumn<Inventory, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        productNameColumn.setPrefWidth(200);

        TableColumn<Inventory, Integer> stockLevelColumn = new TableColumn<>("Stock Level");
        stockLevelColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStockLevel()).asObject());
        stockLevelColumn.setPrefWidth(150);

        TableColumn<Inventory, Boolean> reorderLevelColumn = new TableColumn<>("Reorder Needed");
        reorderLevelColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isReorderNeeded()));
        reorderLevelColumn.setPrefWidth(150);

        inventoryTable.getColumns().addAll(inventoryIdColumn, productIdColumn, productNameColumn, stockLevelColumn, reorderLevelColumn);
    }

    private void setButtonActions(TextField searchField, Button searchButton, Button trackStockLevelsButton,
                                   Button updateStockQuantityButton, Button returnToHomePageButton) {

        trackStockLevelsButton.setOnAction(e -> loadInventoryData());

        updateStockQuantityButton.setOnAction(e -> {
            Inventory selectedInventory = inventoryTable.getSelectionModel().getSelectedItem();
            if (selectedInventory != null) {
                showEditInventoryDialog(selectedInventory);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an inventory item to update.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 800, 800));
            primaryStage.setTitle("Homepage");
        });

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                inventoryTable.getItems().clear();
                searchInventory(searchText);
            } else {
                loadInventoryData();
            }
        });
    }

    private void loadInventoryData() {
        inventoryTable.getItems().clear();
        String sql = "SELECT Inventory.inventory_id, Inventory.product_id, Product.product_name, Product.stock_quantity AS stock_level, Inventory.is_reorder_needed " +
                     "FROM Inventory JOIN Product ON Inventory.product_id = Product.product_id";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Inventory inventory = new Inventory(
                        resultSet.getInt("inventory_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("stock_level"),
                        resultSet.getBoolean("is_reorder_needed")
                );
                inventoryTable.getItems().add(inventory);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchInventory(String searchText) {
        inventoryTable.getItems().clear();
        String sql = "SELECT Inventory.inventory_id, Inventory.product_id, Product.product_name, Product.stock_quantity AS stock_level, Inventory.is_reorder_needed " +
                     "FROM Inventory JOIN Product ON Inventory.product_id = Product.product_id " +
                     "WHERE Product.product_name LIKE ? OR Inventory.is_reorder_needed = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");
            // Assume searching for "1" as reorder-needed
            statement.setInt(2, Integer.parseInt(searchText));

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Inventory inventory = new Inventory(
                        resultSet.getInt("inventory_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("stock_level"),
                        resultSet.getBoolean("is_reorder_needed")
                );
                inventoryTable.getItems().add(inventory);
            }

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }



    private void showEditInventoryDialog(Inventory inventory) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Inventory");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField productNameField = new TextField(inventory.getProductName());
        TextField stockLevelField = new TextField(String.valueOf(inventory.getStockLevel()));
        CheckBox reorderNeededCheckbox = new CheckBox();
        reorderNeededCheckbox.setSelected(inventory.isReorderNeeded());

        productNameField.setDisable(true); // Derived from Product, can't edit directly

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(productNameField, 1, 0);
        grid.add(new Label("Stock Level:"), 0, 1);
        grid.add(stockLevelField, 1, 1);
        grid.add(new Label("Reorder Needed:"), 0, 2);
        grid.add(reorderNeededCheckbox, 1, 2);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                int newStockLevel = Integer.parseInt(stockLevelField.getText());
                inventory.setStockLevel(newStockLevel);
                inventory.setReorderNeeded(reorderNeededCheckbox.isSelected());
                updateInventory(inventory);
                inventoryTable.refresh();
                dialog.close();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid stock quantity. Please enter a valid number.", ButtonType.OK);
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating inventory.", ButtonType.OK);
                alert.showAndWait();
                ex.printStackTrace();
            }
        });


        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER);

        VBox dialogLayout = new VBox(20, grid, buttonBar);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(dialogLayout, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    private void updateInventory(Inventory inventory) {
        String updateInventorySql = "UPDATE Inventory SET is_reorder_needed = ? WHERE inventory_id = ?";
        String updateStockSql = "UPDATE Product SET stock_quantity = ? WHERE product_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement inventoryStatement = connection.prepareStatement(updateInventorySql);
             PreparedStatement stockStatement = connection.prepareStatement(updateStockSql)) {

            // Update Inventory Table
            inventoryStatement.setBoolean(1, inventory.isReorderNeeded());
            inventoryStatement.setInt(2, inventory.getInventoryId());
            inventoryStatement.executeUpdate();

            // Update Product Stock Quantity
            stockStatement.setInt(1, inventory.getStockLevel());
            stockStatement.setInt(2, inventory.getProductId());
            stockStatement.executeUpdate();

            System.out.println("Inventory and stock quantity updated successfully.");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public Parent getRoot() {
        return root;
    }

}
