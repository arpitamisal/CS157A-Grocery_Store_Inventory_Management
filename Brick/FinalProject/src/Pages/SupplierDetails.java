package Pages;

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
import models.Supplier;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDetails {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final VBox leftPane;
    private final Stage primaryStage;
    private final TableView<Supplier> supplierTable;

    public SupplierDetails(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize root layout
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title Label
        Label mainLabel = new Label("Supplier Details");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search suppliers...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons
        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button viewSuppliersButton = new Button("View Suppliers");
        Button addSupplierButton = new Button("Add Supplier");
        Button deleteSupplierButton = new Button("Delete Supplier");
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(viewSuppliersButton, addSupplierButton, deleteSupplierButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // Left pane (search and buttons)
        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        // TableView for supplier data
        supplierTable = new TableView<>();
        supplierTable.setPrefWidth(950);
        supplierTable.setPlaceholder(new Label("No supplier data found."));
        setupTableColumns();

        // Main layout: left pane and supplier table
        mainLayout = new HBox(20, leftPane, supplierTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        // Add components to root
        root.getChildren().addAll(mainLabel, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, viewSuppliersButton, addSupplierButton, deleteSupplierButton, returnToHomePageButton);
    }

    private void setupTableColumns() {
        TableColumn<Supplier, Integer> supplierIdColumn = new TableColumn<>("Supplier ID");
        supplierIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSupplierId()).asObject());
        supplierIdColumn.setPrefWidth(100);

        TableColumn<Supplier, String> supplierNameColumn = new TableColumn<>("Supplier Name");
        supplierNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplierName()));
        supplierNameColumn.setPrefWidth(200);

        TableColumn<Supplier, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        phoneColumn.setPrefWidth(150);

        TableColumn<Supplier, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        emailColumn.setPrefWidth(200);

        TableColumn<Supplier, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getStreet() + ", " + 
            data.getValue().getCity() + ", " + 
            data.getValue().getState() + " " + 
            data.getValue().getZip()));
        addressColumn.setPrefWidth(400);

        supplierTable.getColumns().addAll(supplierIdColumn, supplierNameColumn, phoneColumn, emailColumn, addressColumn);
    }


    private void setButtonActions(TextField searchField, Button searchButton, Button viewSuppliersButton,
                                   Button addSupplierButton, Button deleteSupplierButton, Button returnToHomePageButton) {

        viewSuppliersButton.setOnAction(e -> loadSuppliers());

        addSupplierButton.setOnAction(e -> showAddSupplierDialog());

        deleteSupplierButton.setOnAction(e -> {
            Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                deleteSupplier(selectedSupplier);
                loadSuppliers();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a supplier to delete.", ButtonType.OK);
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
                searchSuppliers(searchText);
            } else {
                loadSuppliers(); // Reload all suppliers if search text is empty
            }
        });
    }

    private void loadSuppliers() {
        supplierTable.getItems().clear();
        String sql = "SELECT supplier_id, supplier_name, phone, email, street, city, state, zip FROM Supplier";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip")
                );
                supplierTable.getItems().add(supplier);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchSuppliers(String searchText) {
        supplierTable.getItems().clear();
        String sql = "SELECT supplier_id, supplier_name, phone, email, street, city, state, zip " +
                     "FROM Supplier " +
                     "WHERE supplier_name LIKE ? OR email LIKE ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchText + "%");
            statement.setString(2, searchText + "%");
            
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("phone"),
                        resultSet.getString("email"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip")
                );
                supplierTable.getItems().add(supplier);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAddSupplierDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Supplier");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField streetField = new TextField();
        TextField cityField = new TextField();
        TextField stateField = new TextField();
        TextField zipField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Street:"), 0, 3);
        grid.add(streetField, 1, 3);
        grid.add(new Label("City:"), 0, 4);
        grid.add(cityField, 1, 4);
        grid.add(new Label("State:"), 0, 5);
        grid.add(stateField, 1, 5);
        grid.add(new Label("Zip:"), 0, 6);
        grid.add(zipField, 1, 6);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                addSupplier(nameField.getText(), phoneField.getText(), emailField.getText(),
                        streetField.getText(), cityField.getText(), stateField.getText(), zipField.getText());
                dialog.close();
                loadSuppliers();
            } catch (Exception ex) {
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

        Scene scene = new Scene(dialogLayout, 400, 400);
        dialog.setScene(scene);
        dialog.show();
    }

    private void addSupplier(String name, String phone, String email, String street, String city, String state, String zip) {
        String sql = "INSERT INTO Supplier (supplier_name, phone, email, street, city, state, zip) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, email);
            statement.setString(4, street);
            statement.setString(5, city);
            statement.setString(6, state);
            statement.setString(7, zip);

            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteSupplier(Supplier supplier) {
        String sql = "DELETE FROM Supplier WHERE supplier_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, supplier.getSupplierId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }
}

