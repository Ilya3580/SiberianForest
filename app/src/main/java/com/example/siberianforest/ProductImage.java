package com.example.siberianforest;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductImage {
    String link;
    Bitmap image;
    String[] rightSpinner;
    String[] leftSpinner;
    String bigParamentrs;
    String name;
    String price;
    ArrayList<PriceCatalog> priceCatalogs;

    public ProductImage(Bitmap image, String[] leftSpinner, String[] rightSpinner, String name,
                        String bigParamentrs, String link, String price, ArrayList<PriceCatalog> priceCatalogs) {
        this.link = link;
        this.image = image;
        this.rightSpinner = rightSpinner;
        this.leftSpinner = leftSpinner;
        this.bigParamentrs = bigParamentrs;
        this.name = name;
        this.price = price;
        this.priceCatalogs = priceCatalogs;
    }

    public ArrayList<PriceCatalog> getPriceCatalogs() {
        return priceCatalogs;
    }

    public void setPriceCatalogs(ArrayList<PriceCatalog> priceCatalogs) {
        this.priceCatalogs = priceCatalogs;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String[] getRightSpinner() {
        return rightSpinner;
    }

    public void setRightSpinner(String[] rightSpinner) {
        this.rightSpinner = rightSpinner;
    }

    public String[] getLeftSpinner() {
        return leftSpinner;
    }

    public void setLeftSpinner(String[] leftSpinner) {
        this.leftSpinner = leftSpinner;
    }

    public String getBigParamentrs() {
        return bigParamentrs;
    }

    public void setBigParamentrs(String bigParamentrs) {
        this.bigParamentrs = bigParamentrs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
