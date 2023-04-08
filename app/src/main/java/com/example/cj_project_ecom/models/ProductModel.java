package com.example.cj_project_ecom.models;

public class ProductModel {

    private final String puid;
    // and imgid for storing image id.
    private String product_name;
    private double product_rating;
    private String imgid;

    private String pinfo, pprice, in_stock;


    public ProductModel(String product_name, double product_rating, String puid, String imgid, String pinfo, String pprice, String in_stock) {
        this.product_name = product_name;
        this.product_rating = product_rating;
        this.imgid = imgid;
        this.puid = puid;
        this.pinfo = pinfo;
        this.pprice = pprice;
        this.in_stock = in_stock;
    }

    public String getPinfo() {
        return pinfo;
    }
    public String getPprice() {
        return pprice;
    }
    public String getIn_stock() {
        return in_stock;
    }
    public String getProduct_name() {
        return product_name;
    }

    public void setProducts_name(String Products_name) {
        this.product_name = Products_name;
    }

    public double getProduct_rating() {return product_rating; }

    public void setProduct_rating(double ratings) {this.product_rating = ratings; }

    public String getImgid() {
        return imgid;
    }

    public String getPuid() {
        return puid;
    }
    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

}
