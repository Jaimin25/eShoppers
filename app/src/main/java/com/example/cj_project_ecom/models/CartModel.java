package com.example.cj_project_ecom.models;

public class CartModel {

    private String puid;
    // and imgid for storing image id.
    private String product_name;
    private String imgid;

    private String pinfo, pprice, pamount;


    public CartModel(String puid, String amount) {
        this.product_name = product_name;
        this.imgid = imgid;
        this.puid = puid;
        this.pinfo = pinfo;
        this.pprice = pprice;
        this.pamount = amount;
    }

    public String getPinfo() {
        return pinfo;
    }
    public String getPprice() {
        return pprice;
    }
    public String getProduct_name() {
        return product_name;
    }

    public void setProducts_name(String Products_name) {
        this.product_name = Products_name;
    }

    public String getAmount() {
        return pamount;
    }

    public String getImgid() {
        return imgid;
    }

    public void setPrice(String price) {
        this.pprice = price;
    }
    public void setAmount(String amt) {
        this.pamount = amt;
    }

    public String getPuid() {
        return puid;
    }
    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

}
