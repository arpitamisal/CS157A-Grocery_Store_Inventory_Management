package Pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Discount;
import models.Product;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageDiscounts {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final VBox leftPane;
    private final Stage primaryStage;
    private final TableView<Discount> discountsTable;

    public ManageDiscounts(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Root Layout
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title
        Label mainLabel = new Label("Manage Discounts");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search discounts...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons
        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button viewDiscountsButton = new Button("View Discounts");
        Button createDiscountsButton = new Button("Create Discount");
        Button updateDiscountsButton = new Button("Update Discount");
        Button deleteDiscountsButton = new Button("Delete Discount");
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(viewDiscountsButton, createDiscountsButton, updateDiscountsButton, deleteDiscountsButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // Combine Buttons and Search Bar
        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        // TableView for discounts
        discountsTable = new TableView<>();
        discountsTable.setPrefWidth(800);
        discountsTable.setPlaceholder(new Label("No discounts found."));
        setupTableColumns();

        // Main Layout
        mainLayout = new HBox(20, leftPane, discountsTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        // Add components to root
        root.getChildren().addAll(mainLabel, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, viewDiscountsButton, createDiscountsButton, updateDiscountsButton, deleteDiscountsButton, returnToHomePageButton);
    }

    private void setupTableColumns() {
        TableColumn<Discount, Integer> discountIdColumn = new TableColumn<>("Discount ID");
        discountIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDiscountId()).asObject());
        discountIdColumn.setPrefWidth(100);

        TableColumn<Discount, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProductName()));
        productNameColumn.setPrefWidth(150);

        TableColumn<Discount, Double> percentageColumn = new TableColumn<>("Discount %");
        percentageColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getDiscountPercentage()).asObject());
        percentageColumn.setPrefWidth(150);

        TableColumn<Discount, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate().toString()));
        startDateColumn.setPrefWidth(150);

        TableColumn<Discount, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEndDate().toString()));
        endDateColumn.setPrefWidth(150);

        discountsTable.getColumns().addAll(discountIdColumn, productNameColumn, percentageColumn, startDateColumn, endDateColumn);
    }

    private void setButtonActions(TextField searchField, Button searchButton, Button viewDiscountsButton,
                                   Button createDiscountsButton, Button updateDiscountsButton,
                                   Button deleteDiscountsButton, Button returnToHomePageButton) {

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            if (!searchText.isEmpty()) {
                discountsTable.getItems().clear();
                searchDiscounts(searchText);
            } else {
                System.out.println("Search field is empty.");
            }
        });

        viewDiscountsButton.setOnAction(e -> {
            discountsTable.getItems().clear();
            loadAllDiscounts();
        });

        createDiscountsButton.setOnAction(e -> showCreateDiscountDialog());

        updateDiscountsButton.setOnAction(e -> {
            Discount selectedDiscount = discountsTable.getSelectionModel().getSelectedItem();
            if (selectedDiscount != null) {
                showUpdateDiscountDialog(selectedDiscount);
            } else {
                showAlert("Warning", "Please select a discount to update.");
            }
        });

        deleteDiscountsButton.setOnAction(e -> {
            Discount selectedDiscount = discountsTable.getSelectionModel().getSelectedItem();
            if (selectedDiscount != null) {
                deleteDiscount(selectedDiscount);
            } else {
                showAlert("Warning", "Please select a discount to delete.");
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 800, 800));
            primaryStage.setTitle("Homepage");
        });
    }

    private void showCreateDiscountDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create Discount");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField productNameField = new TextField();
        TextField percentageField = new TextField();
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(productNameField, 1, 0);
        grid.add(new Label("Discount Percentage:"), 0, 1);
        grid.add(percentageField, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("End Date:"), 0, 3);
        grid.add(endDatePicker, 1, 3);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, 4);

        saveButton.setOnAction(e -> {
            try {
                String productName = productNameField.getText();
                double discountPercentage = Double.parseDouble(percentageField.getText());
                Date startDate = Date.valueOf(startDatePicker.getValue());
                Date endDate = Date.valueOf(endDatePicker.getValue());

                createDiscount(productName, discountPercentage, startDate, endDate);
                loadAllDiscounts();
                dialogStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Error creating discount: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        Scene scene = new Scene(grid, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void showUpdateDiscountDialog(Discount discount) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Update Discount");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField percentageField = new TextField(String.valueOf(discount.getDiscountPercentage()));
        DatePicker startDatePicker = new DatePicker(((Date) discount.getStartDate()).toLocalDate());
        DatePicker endDatePicker = new DatePicker(((Date) discount.getEndDate()).toLocalDate());

        grid.add(new Label("Discount Percentage:"), 0, 0);
        grid.add(percentageField, 1, 0);
        grid.add(new Label("Start Date:"), 0, 1);
        grid.add(startDatePicker, 1, 1);
        grid.add(new Label("End Date:"), 0, 2);
        grid.add(endDatePicker, 1, 2);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, 3);

        saveButton.setOnAction(e -> {
            try {
                double discountPercentage = Double.parseDouble(percentageField.getText());
                Date startDate = Date.valueOf(startDatePicker.getValue());
                Date endDate = Date.valueOf(endDatePicker.getValue());

                updateDiscount(discount.getDiscountId(), discount.getProduct().getProductId(), discountPercentage, startDate, endDate);
                dialogStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Error updating discount: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        Scene scene = new Scene(grid, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void createDiscount(String productName, double discountPercentage, Date startDate, Date endDate) {
        String sql = "INSERT INTO Discount (product_id, discount_percentage, start_date, end_date) " +
                     "SELECT product_id, ?, ?, ? FROM Product WHERE product_name = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDouble(1, discountPercentage);
            preparedStatement.setDate(2, startDate);
            preparedStatement.setDate(3, endDate);
            preparedStatement.setString(4, productName);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated discount ID
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int discountId = generatedKeys.getInt(1);

                        // Fetch the product ID and create a new Discount object for the TableView
                        String productQuery = "SELECT product_id FROM Product WHERE product_name = ?";
                        try (PreparedStatement productStmt = connection.prepareStatement(productQuery)) {
                            productStmt.setString(1, productName);
                            ResultSet productResultSet = productStmt.executeQuery();
                            if (productResultSet.next()) {
                                int productId = productResultSet.getInt("product_id");
                                Product product = new Product(productId, productName, 0, 0, 0, null);

                                // Create the new discount object
                                Discount newDiscount = new Discount(discountId, product, discountPercentage, startDate, endDate);

                                // Add it to the TableView
                                discountsTable.getItems().add(newDiscount);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void updateDiscount(int discountId, int productId, double discountPercentage, Date startDate, Date endDate) {
        String sql = "UPDATE Discount " +
                     "SET product_id = ?, discount_percentage = ?, start_date = ?, end_date = ? " +
                     "WHERE discount_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, productId);
            preparedStatement.setDouble(2, discountPercentage);
            preparedStatement.setDate(3, startDate);
            preparedStatement.setDate(4, endDate);
            preparedStatement.setInt(5, discountId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Discount updated successfully.");
                for (Discount d : discountsTable.getItems()) {
                    if (d.getDiscountId() == discountId) {
                        d.setDiscountPercentage(discountPercentage);
                        d.setStartDate(startDate);
                        d.setEndDate(endDate);
                        d.setProduct(new Product(productId, d.getProductName(), 0, 0, 0, null));
                        break;
                    }
                }
                discountsTable.refresh();
            } else {
                System.out.println("No discount found with ID: " + discountId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteDiscount(Discount discount) {
        String sql = "DELETE FROM Discount WHERE discount_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, discount.getDiscountId());
            preparedStatement.executeUpdate();

            discountsTable.getItems().remove(discount);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadAllDiscounts() {
        String sqlQuery = "SELECT d.discount_id, p.product_id, p.product_name, d.discount_percentage, d.start_date, d.end_date " +
                          "FROM Discount d " +
                          "JOIN Product p ON d.product_id = p.product_id";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"), 0, 0, 0, null
                );

                Discount discount = new Discount(
                    resultSet.getInt("discount_id"),
                    product,
                    resultSet.getDouble("discount_percentage"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date")
                );
                discountsTable.getItems().add(discount);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchDiscounts(String searchText) {
        String sqlQuery = "SELECT d.discount_id, p.product_id, p.product_name, d.discount_percentage, d.start_date, d.end_date " +
                          "FROM Discount d " +
                          "JOIN Product p ON d.product_id = p.product_id " +
                          "WHERE p.product_name LIKE ? OR d.discount_id LIKE ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            String searchPattern = "%" + searchText + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"), 0, 0, 0, null
                );

                Discount discount = new Discount(
                    resultSet.getInt("discount_id"),
                    product,
                    resultSet.getDouble("discount_percentage"),
                    resultSet.getDate("start_date"),
                    resultSet.getDate("end_date")
                );
                discountsTable.getItems().add(discount);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}

