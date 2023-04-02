package com.example.cj_project_ecom;

public class ProductModel {

    // and imgid for storing image id.
    private String product_name;
    private double product_rating;
    private int imgid;

    public ProductModel(String product_name, double product_rating, int imgid) {
        this.product_name = product_name;
        this.product_rating = product_rating;
        this.imgid = imgid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProducts_name(String Products_name) {
        this.product_name = Products_name;
    }

    public double getProduct_rating() {return product_rating; }

    public void setProduct_rating(double ratings) {this.product_rating = ratings; }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
}
