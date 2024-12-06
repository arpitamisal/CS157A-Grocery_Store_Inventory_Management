
# Grocery Store Inventory Management - Queries

This file contains all the functionalities mapped to different queries based on the Employee Home Page actions in the JavaFX application.

---

## **1. Manage Products**
- **Purpose**: Navigate to the "Manage Products" page.
- **Action**:
  - Add, update, delete, or view product details.
- **Query Examples**:
  ```sql
  -- Add a new product
  INSERT INTO Product (product_name, category_id, price, stock_quantity, expiration_date)
  VALUES ('Product Name', 1, 15.99, 100, '2024-12-31');

  -- Update a product's stock
  UPDATE Product
  SET stock_quantity = stock_quantity + 50
  WHERE product_id = 101;

  -- Delete a product
  DELETE FROM Product
  WHERE product_id = 101;

  -- View all products
  SELECT * FROM Product;
  ```

---

## **2. Track Inventory**
- **Purpose**: Navigate to the "Track Inventory" page.
- **Action**:
  - Monitor stock levels and reorder levels.
- **Query Examples**:
  ```sql
  -- View inventory levels
  SELECT product_id, stock_level, reorder_level
  FROM Inventory;

  -- Update stock levels
  UPDATE Inventory
  SET stock_level = stock_level - 10
  WHERE product_id = 101;

  -- View low-stock products
  SELECT product_id, stock_level
  FROM Inventory
  WHERE stock_level <= reorder_level;
  ```

---

## **3. Process Orders**
- **Purpose**: Navigate to the "Process Orders" page.
- **Action**:
  - Create, update, or view orders.
- **Query Examples**:
  ```sql
  -- Create a new order
  INSERT INTO Order (customer_id, order_date, total_amount, order_status)
  VALUES (1, CURRENT_DATE, 49.99, 'Pending');

  -- Add items to an order
  INSERT INTO Order_Item (order_id, product_id, quantity, price)
  VALUES (1, 101, 2, 15.99);

  -- Update order status
  UPDATE Order
  SET order_status = 'Shipped'
  WHERE order_id = 1;

  -- View order details
  SELECT * FROM Order
  WHERE order_id = 1;
  ```

---

## **4. Manage Discounts**
- **Purpose**: Navigate to the "Manage Discounts" page.
- **Action**:
  - Add, update, or remove discounts.
- **Query Examples**:
  ```sql
  -- Add a new discount
  INSERT INTO Discount (product_id, discount_percentage, start_date, end_date)
  VALUES (101, 10, '2024-01-01', '2024-01-31');

  -- Update a discount
  UPDATE Discount
  SET discount_percentage = 15
  WHERE discount_id = 1;

  -- Delete a discount
  DELETE FROM Discount
  WHERE discount_id = 1;

  -- View all discounts
  SELECT * FROM Discount;
  ```

---

## **5. View Employee Information**
- **Purpose**: Navigate to the "View Employee Information" page.
- **Action**:
  - View employee details.
- **Query Examples**:
  ```sql
  -- View all employee details
  SELECT * FROM Employee;

  -- View specific employee details
  SELECT first_name, last_name, position, hire_date
  FROM Employee
  WHERE employee_id = 101;
  ```

---

## **6. Store Locations**
- **Purpose**: Navigate to the "Store Locations" page.
- **Action**:
  - View or manage store location details.
- **Query Examples**:
  ```sql
  -- View all store locations
  SELECT * FROM Store_Location;

  -- Update store manager
  UPDATE Store_Location
  SET store_manager_id = 102
  WHERE location_id = 1;
  ```

---

## **7. View Supplier Details**
- **Purpose**: Navigate to the "View Supplier Details" page.
- **Action**:
  - View or manage supplier details.
- **Query Examples**:
  ```sql
  -- View all suppliers
  SELECT * FROM Supplier;

  -- Add a new supplier
  INSERT INTO Supplier (supplier_name, contact_info, address)
  VALUES ('Supplier Name', '123-456-7890', '123 Main St, City, State');

  -- Update supplier details
  UPDATE Supplier
  SET contact_info = '987-654-3210'
  WHERE supplier_id = 101;
  ```

## Additional Employee Queries

### Search Employee by Name
- **Purpose**: Search for employees by their first name.
- **Query**:
```sql
SELECT employee_id, first_name, last_name, position, salary, hire_date
FROM Employee
WHERE first_name LIKE ?;
```
- **Description**: Uses a search pattern to find employees whose first name starts with the input text.

---

### Add New Employee
- **Purpose**: Insert a new employee into the Employee table.
- **Query**:
```sql
INSERT INTO Employee (first_name, last_name, position, salary, hire_date)
VALUES (?, ?, ?, ?, ?);
```
- **Description**: Adds a new employee record with the provided details.

---

### Update Employee Information
- **Purpose**: Update an existing employee's information.
- **Query**:
```sql
UPDATE Employee
SET first_name = ?, last_name = ?, position = ?, salary = ?, hire_date = ?
WHERE employee_id = ?;
```
- **Description**: Updates an employee's details based on their unique `employee_id`.

---

### Delete Employee
- **Purpose**: Remove an employee from the database.
- **Query**:
```sql
DELETE FROM Employee
WHERE employee_id = ?;
```
- **Description**: Deletes an employee based on their `employee_id`.

---

### Load All Employees
- **Purpose**: Retrieve all employee records.
- **Query**:
```sql
SELECT employee_id, first_name, last_name, position, salary, hire_date
FROM Employee;
```
- **Description**: Fetches all employees for display in the TableView.

---

## Store Location Queries

### Load All Store Locations
- **Purpose**: Retrieve all store locations from the database.
- **Query**:
```sql
SELECT location_id, store_name, street, city, state, zip, phone
FROM Store_Location;
```
- **Description**: Retrieves the details of all store locations.

---

### Search Store Locations
- **Purpose**: Search store locations by attributes like name, street, city, state, zip, or phone.
- **Query**:
```sql
SELECT location_id, store_name, street, city, state, zip, phone
FROM Store_Location
WHERE store_name LIKE ? OR street LIKE ? OR city LIKE ? OR state LIKE ? OR zip LIKE ? OR phone LIKE ?;
```
- **Description**: Uses search patterns to find matching store locations.

---

### Add Store Location
- **Purpose**: Add a new store location.
- **Query**:
```sql
INSERT INTO Store_Location (store_name, street, city, state, zip, phone)
VALUES (?, ?, ?, ?, ?, ?);
```
- **Description**: Inserts a new store location into the database.

---

### Edit Store Location
- **Purpose**: Update an existing store location's details.
- **Query**:
```sql
UPDATE Store_Location
SET store_name = ?, street = ?, city = ?, state = ?, zip = ?, phone = ?
WHERE location_id = ?;
```
- **Description**: Updates the details of a store location based on its `location_id`.

---

### Delete Store Location
- **Purpose**: Remove a store location from the database.
- **Query**:
```sql
DELETE FROM Store_Location
WHERE location_id = ?;
```
- **Description**: Deletes a store location based on its `location_id`.

---
