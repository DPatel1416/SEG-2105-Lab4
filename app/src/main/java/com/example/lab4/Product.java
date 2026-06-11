package com.example.lab4;

public class Product {

    private String productId;
    private String productName;
    private double productPrice;

    public Product() {
        // Required empty constructor for Firebase
    }

    public Product(String productId, String productName, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }
}