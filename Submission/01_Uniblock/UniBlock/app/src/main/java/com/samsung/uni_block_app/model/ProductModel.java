package com.samsung.uni_block_app.model;

public class ProductModel {
    private int productId;
    private String productName;
    private String productDetails;
    private int productImage;
    private double productPrice;
    private String sellerAddress;

    public ProductModel(int productId, String productName, String productDetails, int productImage, double productPrice, String sellerAddress) {
        this.productId = productId;
        this.productName = productName;
        this.productDetails = productDetails;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.sellerAddress = sellerAddress;
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

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }
}
