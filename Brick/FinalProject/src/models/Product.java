package models;

import java.util.Date;

public class Product {
    private int productId;
    private String productName;
    private int categoryId;
    private double price;
    private int stockQuantity;
    private Date expirationDate;

    // Constructor
    public Product(int productId, String productName, int categoryId, double price, int stockQuantity, Date expirationDate) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
}
