package Pages;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.StoreLocation;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreLocations {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final VBox leftPane;
    private final Stage primaryStage;
    private final TableView<StoreLocation> storeTable;

    public StoreLocations(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Root layout
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title Label
        Label mainLabel = new Label("Manage Store Locations");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search store locations...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons
        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button addStoreButton = new Button("Add Store");
        Button editStoreButton = new Button("Edit Store");
        Button removeStoreButton = new Button("Remove Store");
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(addStoreButton, editStoreButton, removeStoreButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // Left pane (search and buttons)
        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        // TableView for store data
        storeTable = new TableView<>();
        storeTable.setPrefWidth(800);
        storeTable.setPlaceholder(new Label("No store locations found."));
        setupTableColumns();

        // Main layout: left pane and store table
        mainLayout = new HBox(20, leftPane, storeTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        // Add components to root
        root.getChildren().addAll(mainLabel, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, addStoreButton, editStoreButton, removeStoreButton, returnToHomePageButton);

        // Load initial data
        loadStoreLocations();
    }

    private void setupTableColumns() {
        TableColumn<StoreLocation, Integer> locationIdColumn = new TableColumn<>("Location ID");
        locationIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLocationId()));
        locationIdColumn.setPrefWidth(100);

        TableColumn<StoreLocation, String> storeNameColumn = new TableColumn<>("Store Name");
        storeNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStoreName()));
        storeNameColumn.setPrefWidth(200);

        TableColumn<StoreLocation, String> streetColumn = new TableColumn<>("Street");
        streetColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStreet()));
        streetColumn.setPrefWidth(200);

        TableColumn<StoreLocation, String> cityColumn = new TableColumn<>("City");
        cityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));
        cityColumn.setPrefWidth(150);

        TableColumn<StoreLocation, String> stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getState()));
        stateColumn.setPrefWidth(100);

        TableColumn<StoreLocation, String> zipColumn = new TableColumn<>("Zip Code");
        zipColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getZipCode()));
        zipColumn.setPrefWidth(100);

        TableColumn<StoreLocation, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        phoneColumn.setPrefWidth(150);

        storeTable.getColumns().addAll(locationIdColumn, storeNameColumn, streetColumn, cityColumn, stateColumn, zipColumn, phoneColumn);
    }

    private void setButtonActions(TextField searchField, Button searchButton, Button addStoreButton, Button editStoreButton,
                                   Button removeStoreButton, Button returnToHomePageButton) {

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                searchStoreLocations(searchText);
            } else {
                loadStoreLocations();
            }
        });

        addStoreButton.setOnAction(e -> showAddStoreDialog());

        editStoreButton.setOnAction(e -> {
            StoreLocation selectedStore = storeTable.getSelectionModel().getSelectedItem();
            if (selectedStore != null) {
                showEditStoreDialog(selectedStore);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a store location to edit.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        removeStoreButton.setOnAction(e -> {
            StoreLocation selectedStore = storeTable.getSelectionModel().getSelectedItem();
            if (selectedStore != null) {
                removeStore(selectedStore);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a store location to remove.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 600, 600));
            primaryStage.setTitle("Homepage");
        });
    }

    private void loadStoreLocations() {
        storeTable.getItems().clear();
        String sql = "SELECT location_id, store_name, street, city, state, zip, phone FROM Store_Location";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                StoreLocation location = new StoreLocation(
                        resultSet.getInt("location_id"),
                        resultSet.getString("store_name"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip"),
                        resultSet.getString("phone")
                );
                storeTable.getItems().add(location);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchStoreLocations(String searchText) {
        storeTable.getItems().clear();
        String sql = "SELECT location_id, store_name, street, city, state, zip, phone " +
                     "FROM Store_Location WHERE store_name LIKE ? OR street LIKE ? OR city LIKE ? OR state LIKE ? OR zip LIKE ? OR phone LIKE ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            statement.setString(3, "%" + searchText + "%");
            statement.setString(4, "%" + searchText + "%");
            statement.setString(5, "%" + searchText + "%");
            statement.setString(6, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StoreLocation store = new StoreLocation(
                        resultSet.getInt("location_id"),
                        resultSet.getString("store_name"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip"),
                        resultSet.getString("phone")
                );
                storeTable.getItems().add(store);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAddStoreDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Store Location");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField storeNameField = new TextField();
        TextField streetField = new TextField();
        TextField cityField = new TextField();
        TextField stateField = new TextField();
        TextField zipField = new TextField();
        TextField phoneField = new TextField();

        grid.add(new Label("Store Name:"), 0, 0);
        grid.add(storeNameField, 1, 0);
        grid.add(new Label("Street:"), 0, 1);
        grid.add(streetField, 1, 1);
        grid.add(new Label("City:"), 0, 2);
        grid.add(cityField, 1, 2);
        grid.add(new Label("State:"), 0, 3);
        grid.add(stateField, 1, 3);
        grid.add(new Label("Zip Code:"), 0, 4);
        grid.add(zipField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            addStore(storeNameField.getText(), streetField.getText(), cityField.getText(), stateField.getText(), zipField.getText(), phoneField.getText());
            loadStoreLocations();
            dialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        VBox dialogLayout = new VBox(20, grid, new HBox(10, saveButton, cancelButton));
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(dialogLayout, 400, 300));
        dialog.show();
    }

    private void addStore(String storeName, String street, String city, String state, String zip, String phone) {
        String sql = "INSERT INTO Store_Location (store_name, street, city, state, zip, phone) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, storeName);
            statement.setString(2, street);
            statement.setString(3, city);
            statement.setString(4, state);
            statement.setString(5, zip);
            statement.setString(6, phone);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void showEditStoreDialog(StoreLocation store) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Store Location");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Pre-fill the fields with the selected store's current details
        TextField storeNameField = new TextField(store.getStoreName());
        TextField streetField = new TextField(store.getStreet());
        TextField cityField = new TextField(store.getCity());
        TextField stateField = new TextField(store.getState());
        TextField zipField = new TextField(store.getZipCode());
        TextField phoneField = new TextField(store.getPhone());

        grid.add(new Label("Store Name:"), 0, 0);
        grid.add(storeNameField, 1, 0);
        grid.add(new Label("Street:"), 0, 1);
        grid.add(streetField, 1, 1);
        grid.add(new Label("City:"), 0, 2);
        grid.add(cityField, 1, 2);
        grid.add(new Label("State:"), 0, 3);
        grid.add(stateField, 1, 3);
        grid.add(new Label("Zip Code:"), 0, 4);
        grid.add(zipField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Update the store object with the new values
            store.setStoreName(storeNameField.getText());
            store.setStreet(streetField.getText());
            store.setCity(cityField.getText());
            store.setState(stateField.getText());
            store.setZipCode(zipField.getText());
            store.setPhone(phoneField.getText());

            // Update the store in the database
            updateStore(store);

            // Reload the table data
            loadStoreLocations();

            // Close the dialog
            dialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        VBox dialogLayout = new VBox(20, grid, new HBox(10, saveButton, cancelButton));
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        dialog.setScene(new Scene(dialogLayout, 400, 300));
        dialog.show();
    }


    private void removeStore(StoreLocation store) {
        String sql = "DELETE FROM Store_Location WHERE location_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, store.getLocationId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        loadStoreLocations(); // Refresh the table after deletion
    }

    private void updateStore(StoreLocation store) {
        String sql = "UPDATE Store_Location SET store_name = ?, street = ?, city = ?, state = ?, zip = ?, phone = ? WHERE location_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, store.getStoreName());
            statement.setString(2, store.getStreet());
            statement.setString(3, store.getCity());
            statement.setString(4, store.getState());
            statement.setString(5, store.getZipCode());
            statement.setString(6, store.getPhone());
            statement.setInt(7, store.getLocationId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }
}
