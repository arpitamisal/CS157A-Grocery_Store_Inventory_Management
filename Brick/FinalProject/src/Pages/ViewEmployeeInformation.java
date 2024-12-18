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
import models.Employee;
import application.DatabaseUtility;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewEmployeeInformation {

    // Declaring variables
    private final VBox root;
    private final VBox buttons;
    private final HBox mainLayout; // For splitting buttons and TableView
    private final VBox leftPane; // For buttons and search bar
    private final Stage primaryStage;
    private final TableView<Employee> employeeTable;

    public ViewEmployeeInformation(Stage primaryStage) {

        // Initialize variables
        this.primaryStage = primaryStage;
        root = new VBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: cornflowerblue;");

        // Title Label
        Label mainLabel = new Label("View Employee Information");
        mainLabel.setStyle("-fx-font-size: 24px;");

        // Search Bar
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search employees...");
        Button searchButton = new Button("Search");
        searchBar.getChildren().addAll(searchField, searchButton);

        // Buttons
        buttons = new VBox(20);
        buttons.setPadding(new Insets(10));
        Button viewEmployeeProfilesButton = new Button("View Employee Profiles");
        Button editEmployeeButton = new Button("Edit Selected Employee");
        Button addEmployeeButton = new Button("Add Employee");
        Button deleteEmployeeButton = new Button("Delete Selected Employee"); // New delete button
        Button returnToHomePageButton = new Button("Return to Homepage");
        buttons.getChildren().addAll(viewEmployeeProfilesButton, editEmployeeButton, addEmployeeButton, deleteEmployeeButton, returnToHomePageButton);
        buttons.setAlignment(Pos.TOP_LEFT);

        // Combine Search Bar and Buttons into Left Pane
        leftPane = new VBox(20);
        leftPane.getChildren().addAll(searchBar, buttons);
        leftPane.setAlignment(Pos.TOP_LEFT);

        // TableView for employee data
        employeeTable = new TableView<>();
        employeeTable.setPrefWidth(840); // Increased width for better visibility
        employeeTable.setPlaceholder(new Label("No employee data found."));
        setupTableColumns();

        // Main Layout: Buttons on the left, TableView on the right
        mainLayout = new HBox(20, leftPane, employeeTable);
        mainLayout.setAlignment(Pos.TOP_LEFT);
        mainLayout.setPadding(new Insets(10));

        // Add components to the root
        root.getChildren().addAll(mainLabel, mainLayout);

        // Set button actions
        setButtonActions(searchField, searchButton, viewEmployeeProfilesButton, editEmployeeButton, addEmployeeButton, deleteEmployeeButton, returnToHomePageButton);
    }

    // Setup columns for the TableView
    private void setupTableColumns() {
        TableColumn<Employee, Number> employeeIdColumn = new TableColumn<>("Employee ID");
        employeeIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEmployeeId()));
        employeeIdColumn.setPrefWidth(100);

        TableColumn<Employee, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        firstNameColumn.setPrefWidth(150);

        TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
        lastNameColumn.setPrefWidth(150);

        TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPosition()));
        positionColumn.setPrefWidth(150);

        TableColumn<Employee, Number> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getSalary()));
        salaryColumn.setPrefWidth(150);

        TableColumn<Employee, String> hireDateColumn = new TableColumn<>("Hire Date");
        hireDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHireDate().toString()));
        hireDateColumn.setPrefWidth(150);

        employeeTable.getColumns().addAll(employeeIdColumn, firstNameColumn, lastNameColumn, positionColumn, salaryColumn, hireDateColumn);
    }

    // Set button actions and fetch data from Employee table
    private void setButtonActions(TextField searchField, Button searchButton, Button viewEmployeeProfilesButton,
                                   Button editEmployeeButton, Button addEmployeeButton, Button deleteEmployeeButton,
                                   Button returnToHomePageButton) {

        searchButton.setOnAction(e -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                System.out.println("Searching for: " + searchText);
                employeeTable.getItems().clear(); // Clear the table before adding results
                searchEmployees(searchText);
            } else {
                System.out.println("Search field is empty.");
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a search term.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        viewEmployeeProfilesButton.setOnAction(e -> {
            System.out.println("View Employee Profiles button clicked.");
            employeeTable.getItems().clear();
            loadAllEmployees();
        });

        editEmployeeButton.setOnAction(e -> {
            Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                showEditEmployeeDialog(selectedEmployee);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an employee to edit.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        addEmployeeButton.setOnAction(e -> showAddEmployeeDialog());

        deleteEmployeeButton.setOnAction(e -> {
            Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            if (selectedEmployee != null) {
                deleteEmployee(selectedEmployee);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an employee to delete.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        returnToHomePageButton.setOnAction(e -> {
            System.out.println("Return to Homepage button clicked.");
            EmployeeHomePage empHomePage = new EmployeeHomePage(primaryStage);
            primaryStage.setScene(new Scene(empHomePage.getRoot(), 800, 800));
            primaryStage.setTitle("Homepage");
        });
    }

    private void loadAllEmployees() {
        String sqlQuery = "SELECT employee_id, first_name, last_name, position, salary, hire_date FROM Employee";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("position"),
                    resultSet.getDouble("salary"),
                    resultSet.getDate("hire_date")
                );
                employeeTable.getItems().add(employee);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteEmployee(Employee employee) {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, employee.getEmployeeId());
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully.");
                employeeTable.getItems().remove(employee); // Remove the employee from the TableView
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error deleting employee: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void showEditEmployeeDialog(Employee employee) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Employee");
        dialogStage.initOwner(primaryStage);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField(employee.getFirstName());
        TextField lastNameField = new TextField(employee.getLastName());
        TextField positionField = new TextField(employee.getPosition());
        TextField salaryField = new TextField(String.valueOf(employee.getSalary()));
        DatePicker hireDatePicker = new DatePicker();

        if (employee.getHireDate() != null) {
            hireDatePicker.setValue(((Date) employee.getHireDate()).toLocalDate());
        }

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);
        grid.add(new Label("Salary:"), 0, 3);
        grid.add(salaryField, 1, 3);
        grid.add(new Label("Hire Date:"), 0, 4);
        grid.add(hireDatePicker, 1, 4);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, 5);

        saveButton.setOnAction(e -> {
            try {
                employee.setFirstName(firstNameField.getText());
                employee.setLastName(lastNameField.getText());
                employee.setPosition(positionField.getText());
                employee.setSalary(Double.parseDouble(salaryField.getText()));

                if (hireDatePicker.getValue() != null) {
                    employee.setHireDate(Date.valueOf(hireDatePicker.getValue()));
                } else {
                    employee.setHireDate(null);
                }

                updateEmployeeInDatabase(employee);
                employeeTable.refresh();
                dialogStage.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating employee: " + ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        Scene scene = new Scene(grid, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void showAddEmployeeDialog() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add New Employee");
        dialogStage.initOwner(primaryStage);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField positionField = new TextField();
        TextField salaryField = new TextField();
        DatePicker hireDatePicker = new DatePicker();

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);
        grid.add(new Label("Salary:"), 0, 3);
        grid.add(salaryField, 1, 3);
        grid.add(new Label("Hire Date:"), 0, 4);
        grid.add(hireDatePicker, 1, 4);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttons, 1, 5);

        saveButton.setOnAction(e -> {
            try {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String position = positionField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                Date hireDate = Date.valueOf(hireDatePicker.getValue());

                insertEmployeeIntoDatabase(firstName, lastName, position, salary, hireDate);
                employeeTable.getItems().clear();
                loadAllEmployees();
                dialogStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding employee: " + ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(e -> dialogStage.close());

        Scene scene = new Scene(grid, 400, 300);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void insertEmployeeIntoDatabase(String firstName, String lastName, String position, double salary, Date hireDate) {
        String sql = "INSERT INTO Employee (first_name, last_name, position, salary, hire_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, position);
            preparedStatement.setDouble(4, salary);
            preparedStatement.setDate(5, hireDate);

            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateEmployeeInDatabase(Employee employee) {
        String sql = "UPDATE Employee SET first_name = ?, last_name = ?, position = ?, salary = ?, hire_date = ? WHERE employee_id = ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setDouble(4, employee.getSalary());

            if (employee.getHireDate() != null) {
                preparedStatement.setDate(5, (Date) employee.getHireDate());
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }

            preparedStatement.setInt(6, employee.getEmployeeId());
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchEmployees(String searchText) {
        String sqlQuery = "SELECT employee_id, first_name, last_name, position, salary, hire_date " +
                          "FROM Employee " +
                          "WHERE first_name LIKE ?";

        try (Connection connection = DatabaseUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            String searchPattern = searchText + "%"; // Optimized for index usage with prefix search
            preparedStatement.setString(1, searchPattern);

            ResultSet resultSet = preparedStatement.executeQuery();
            employeeTable.getItems().clear(); // Clear the table before adding results

            while (resultSet.next()) {
                Employee employee = new Employee(
                    resultSet.getInt("employee_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("position"),
                    resultSet.getDouble("salary"),
                    resultSet.getDate("hire_date")
                );
                employeeTable.getItems().add(employee);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching employee data: " + ex.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }


    public Parent getRoot() {
        return root;
    }
}
