package models;

public class Inventory {
    private int inventoryId;
    private int productId; // Product ID from Product table
    private String productName; // Derived from Product table
    private int stockLevel; // Derived from Product table
    private boolean reorderNeeded;

    public Inventory(int inventoryId, int productId, String productName, int stockLevel, boolean reorderNeeded) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.productName = productName;
        this.stockLevel = stockLevel;
        this.reorderNeeded = reorderNeeded;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public boolean isReorderNeeded() {
        return reorderNeeded;
    }

    public void setReorderNeeded(boolean reorderNeeded) {
        this.reorderNeeded = reorderNeeded;
    }
}
