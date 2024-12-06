package Pages;

import javafx.beans.property.SimpleDoubleProperty;
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
import models.Order;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcessOrders {

    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout;
    private final VBox leftPane;
    private final Stage primaryStage;
    private final TableView<Order> ordersTable;

    public ProcessOrders(Stage primaryStage) {
        this.primaryStage = primaryStage;

        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        Label mainLabel = new Label("Process Orders");
        mainLabel.setStyle("-fx-font-size: 24px;");

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search orders...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button viewCustomerOrdersButton = new Button("View Customer Orders");
        Button updateOrderStatusButton = new Button("Update Order Status");
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(viewCustomerOrdersButton, updateOrderStatusButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        ordersTable = new TableView<>();
        ordersTable.setPrefWidth(800);
        ordersTable.setPlaceholder(new Label("No orders found."));
        setupTableColumns();

        mainLayout = new HBox(20, leftPane, ordersTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        root.getChildren().addAll(mainLabel, mainLayout);

        setButtonActions(searchField, searchButton, viewCustomerOrdersButton, updateOrderStatusButton, returnToHomePageButton);
    }

    private void setupTableColumns() {
        TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOrderId()).asObject());
        orderIdColumn.setPrefWidth(150);

        TableColumn<Order, Integer> customerIdColumn = new TableColumn<>("Customer ID");
        customerIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());
        customerIdColumn.setPrefWidth(150);

        TableColumn<Order, String> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderDate().toString()));
        orderDateColumn.setPrefWidth(200);

        TableColumn<Order, Double> totalAmountColumn = new TableColumn<>("Total Amount");
        totalAmountColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalAmount()).asObject());
        totalAmountColumn.setPrefWidth(150);

        TableColumn<Order, String> orderStatusColumn = new TableColumn<>("Order Status");
        orderStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderStatus()));
        orderStatusColumn.setPrefWidth(150);

        ordersTable.getColumns().addAll(orderIdColumn, customerIdColumn, orderDateColumn, totalAmountColumn, orderStatusColumn);
    }

    private void setButtonActions(TextField searchField, Button searchButton, Button viewCustomerOrdersButton,
                                   Button updateOrderStatusButton, Button returnToHomePageButton) {

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                searchOrders(searchText);
            } else {
                loadOrders();
            }
        });

        viewCustomerOrdersButton.setOnAction(e -> loadOrders());

        updateOrderStatusButton.setOnAction(e -> {
            Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showUpdateOrderStatusDialog(selectedOrder);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an order to update.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 800, 800));
            primaryStage.setTitle("Homepage");
        });
    }

    private void loadOrders() {
        ordersTable.getItems().clear();
        String sql = "SELECT o.order_id, o.customer_id, o.order_date, o.order_status, " +
                     "SUM(oi.quantity * oi.price) AS total_amount " +
                     "FROM `Order` o " +
                     "LEFT JOIN Order_Item oi ON o.order_id = oi.order_id " +
                     "GROUP BY o.order_id, o.customer_id, o.order_date, o.order_status";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getDate("order_date"), // Directly pass Date object
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("order_status")
                );
                ordersTable.getItems().add(order);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchOrders(String searchText) {
        ordersTable.getItems().clear();
        String sql = "SELECT o.order_id, o.customer_id, o.order_date, o.order_status, " +
                     "SUM(oi.quantity * oi.price) AS total_amount " +
                     "FROM `Order` o " +
                     "LEFT JOIN Order_Item oi ON o.order_id = oi.order_id " +
                     "WHERE o.order_id LIKE ? OR o.order_status LIKE ? " +
                     "GROUP BY o.order_id, o.customer_id, o.order_date, o.order_status";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getDate("order_date"), // Directly pass Date object
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("order_status")
                );
                ordersTable.getItems().add(order);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void showUpdateOrderStatusDialog(Order order) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Update Order Status");

        VBox dialogLayout = new VBox(20);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "Processing", "Completed", "Cancelled");
        statusComboBox.setValue(order.getOrderStatus());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            order.setOrderStatus(statusComboBox.getValue());
            updateOrderStatus(order);
            ordersTable.refresh();
            dialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBar = new HBox(10, saveButton, cancelButton);
        buttonBar.setAlignment(Pos.CENTER);

        dialogLayout.getChildren().addAll(new Label("Update Status:"), statusComboBox, buttonBar);

        Scene dialogScene = new Scene(dialogLayout, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private boolean isValidOrderStatus(String status) {
        return status != null && (
            status.equals("Pending") ||
            status.equals("Shipped") ||
            status.equals("Delivered") ||
            status.equals("Cancelled")
        );
    }


    private void updateOrderStatus(Order order) {
        String sql = "UPDATE `Order` SET order_status = ? WHERE order_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String status = order.getOrderStatus();
            // Log the status being updated
            System.out.println("Updating order_status to: " + status);

            // Validate status before updating
            if (!isValidOrderStatus(status)) {
                throw new IllegalArgumentException("Invalid order status: " + status);
            }

            statement.setString(1, status);
            statement.setInt(2, order.getOrderId());
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
    }


    public Parent getRoot() {
        return root;
    }
}
