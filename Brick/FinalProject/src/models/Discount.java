package models;

import java.util.Date;

public class Discount {
    private int discountId;
    private Product product; // Reference to the Product object
    private double discountPercentage;
    private Date startDate;
    private Date endDate;

    public Discount(int discountId, Product product, double discountPercentage, Date startDate, Date endDate) {
        this.discountId = discountId;
        this.product = product;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public Product getProduct() {
    	return product;
    }
    public String getProductName() {
        return product != null ? product.getProductName() : null; // Derived attribute
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
   

    public int getProductId() {
        return product.getProductId(); 
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
