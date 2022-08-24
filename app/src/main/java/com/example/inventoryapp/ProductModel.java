package com.example.inventoryapp;

import java.io.Serializable;

public class ProductModel implements Serializable{


    public ProductModel(int productid, String productname, String productunit, double productprice, String productExpirydate, int avail_inventory, double productinventorycost, String productimage) {
        this.productid = productid;
        this.productname = productname;
        this.productunit = productunit;
        this.productprice = productprice;
        this.productExpirydate = productExpirydate;
        this.avail_inventory = avail_inventory;
        this.productinventorycost = productinventorycost;
        this.productimage = productimage;
    }

    private int productid;
    private String productname;
    private String productunit;
    private double productprice;
    private String productExpirydate;
    private int avail_inventory;
    private String productimage;
    private double productinventorycost;

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }



    public void setProductExpirydate(String productExpirydate) {
        this.productExpirydate = productExpirydate;
    }

    public double getProductinventorycost() {
        return productinventorycost;
    }

    public void setProductinventorycost(double productinventorycost) {
        this.productinventorycost = productinventorycost;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductunit() {
        return productunit;
    }

    public void setProductunit(String productunit) {
        this.productunit = productunit;
    }

    public double getProductprice() {
        return productprice;
    }

    public void setProductprice(double productprice) {
        this.productprice = productprice;
    }

    public String getProductExpirydate() {
        return productExpirydate;
    }

    public void setProductdate(String productExpirydate) {
        this.productExpirydate = productExpirydate;
    }

    public int getAvail_inventory() {
        return avail_inventory;
    }

    public void setAvail_inventory(int avail_inventory) {
        this.avail_inventory = avail_inventory;
    }


}
