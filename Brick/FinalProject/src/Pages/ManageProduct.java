package Pages;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Product;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageProduct {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final Stage primaryStage;
    private final TableView<Product> productTable;

    public ManageProduct(Stage primaryStage) {
        this.primaryStage = primaryStage;
        buttons = new VBox(20);
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title
        Label mainLabel = new Label("Manage Products");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons for actions
        Button searchProductButton = new Button("View Products");
        Button addProductButton = new Button("Add Product");
        Button updateProductButton = new Button("Update Product");
        Button deleteProductButton = new Button("Delete Product");
        Button returnToHomePageButton = new Button("Return to Homepage");

        buttons.getChildren().addAll(searchProductButton, addProductButton, updateProductButton, deleteProductButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // TableView for displaying products
        productTable = new TableView<>();
        productTable.setPrefWidth(800);
        productTable.setPlaceholder(new Label("No products found."));
        setupTableColumns();

        // Layout: Buttons on the left, TableView on the right
        mainLayout = new HBox(20, buttons, productTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);

        // Add components to the root
        root.getChildren().addAll(mainLabel, searchBar, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, searchProductButton, addProductButton, updateProductButton, deleteProductButton, returnToHomePageButton);
    }

    private void setupTableColumns() {
        TableColumn<Product, Integer> idColumn = new TableColumn<>("Product ID");
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getProductId()).asObject());
        idColumn.setPrefWidth(100);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        nameColumn.setPrefWidth(150);

        TableColumn<Product, Integer> categoryIdColumn = new TableColumn<>("Category ID");
        categoryIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCategoryId()).asObject());
        categoryIdColumn.setPrefWidth(100);

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        priceColumn.setPrefWidth(100);

        TableColumn<Product, String> expirationDateColumn = new TableColumn<>("Expiration Date");
        expirationDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getExpirationDate().toString()));
        expirationDateColumn.setPrefWidth(150);

        TableColumn<Product, Integer> stockQuantityColumn = new TableColumn<>("Stock Quantity");
        stockQuantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStockQuantity()).asObject());
        stockQuantityColumn.setPrefWidth(150);

        productTable.getColumns().addAll(idColumn, nameColumn, categoryIdColumn, priceColumn, expirationDateColumn, stockQuantityColumn);
    }

    private void setButtonActions(TextField searchField, Button searchButton, Button searchProductButton,
                                   Button addProductButton, Button updateProductButton, Button deleteProductButton,
                                   Button returnToHomePageButton) {

    	searchButton.setOnAction(e -> {
    	    String searchText = searchField.getText().trim();
    	    if (!searchText.isEmpty()) {
    	        System.out.println("Searching for: " + searchText);
    	        productTable.getItems().clear(); // Clear the table before adding results
    	        searchProducts(searchText);
    	    } else {
    	        System.out.println("Search field is empty.");
    	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a search term.", ButtonType.OK);
    	        alert.showAndWait();
    	    }
    	});

    	searchProductButton.setOnAction(e -> {
    	    System.out.println("Displaying all products.");
    	    productTable.getItems().clear(); // Clear the table before adding results
    	    loadAllProducts();
    	});

        addProductButton.setOnAction(e -> showAddProductDialog());

        updateProductButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                showEditProductDialog(selectedProduct);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product to update.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        deleteProductButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                deleteProduct(selectedProduct);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a product to delete.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 600, 600));
            primaryStage.setTitle("Homepage");
        });
    }

    private void searchProducts(String searchText) {
        String sqlQuery = "SELECT product_id, product_name, category_id, price, stock_quantity, expiration_date " +
                          "FROM Product " +
                          "WHERE product_name LIKE ?"; 
        
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

        	String searchPattern = "%" + searchText + "%"; // Match products containing searchText
            preparedStatement.setString(1, searchPattern);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getInt("category_id"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getDate("expiration_date")
                );
                productTable.getItems().add(product);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void showAddProductDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Product");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField categoryIdField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();
        DatePicker expirationPicker = new DatePicker();

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category ID:"), 0, 1);
        grid.add(categoryIdField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Expiration Date:"), 0, 4);
        grid.add(expirationPicker, 1, 4);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int categoryId = Integer.parseInt(categoryIdField.getText());
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                Date expirationDate = Date.valueOf(expirationPicker.getValue());

                insertProduct(name, categoryId, price, stock, expirationDate);
                dialog.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBar, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditProductDialog(Product product) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Product");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(product.getProductName());
        TextField categoryIdField = new TextField(String.valueOf(product.getCategoryId()));
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField stockField = new TextField(String.valueOf(product.getStockQuantity()));
        DatePicker expirationPicker = new DatePicker(((Date) product.getExpirationDate()).toLocalDate());

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category ID:"), 0, 1);
        grid.add(categoryIdField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Expiration Date:"), 0, 4);
        grid.add(expirationPicker, 1, 4);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try {
                product.setProductName(nameField.getText());
                product.setCategoryId(Integer.parseInt(categoryIdField.getText()));
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setStockQuantity(Integer.parseInt(stockField.getText()));
                product.setExpirationDate(Date.valueOf(expirationPicker.getValue()));

                updateProduct(product);
                productTable.refresh();
                dialog.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        grid.add(buttonBar, 1, 5);

        Scene scene = new Scene(grid, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }

    private void insertProduct(String name, int categoryId, double price, int stock, Date expirationDate) {
        String sql = "INSERT INTO Product (product_name, category_id, price, stock_quantity, expiration_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, categoryId);
            statement.setDouble(3, price);
            statement.setInt(4, stock);
            statement.setDate(5, expirationDate);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(Product product) {
        String sql = "UPDATE Product SET product_name = ?, category_id = ?, price = ?, stock_quantity = ?, expiration_date = ? WHERE product_id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setInt(2, product.getCategoryId());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setDate(5, (Date) product.getExpirationDate());
            statement.setInt(6, product.getProductId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(Product product) {
        String sql = "DELETE FROM Product WHERE product_id = ?";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product.getProductId());
            statement.executeUpdate();
            productTable.getItems().remove(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllProducts() {
        String sql = "SELECT * FROM Product";
        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("category_id"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getDate("expiration_date")
                );
                productTable.getItems().add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public Parent getRoot() {
        return root;
    }
}
