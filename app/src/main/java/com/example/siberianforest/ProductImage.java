package com.example.siberianforest;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProductImage {
    Bitmap image;
    String[] spinner;
    String[] rightSpinner;
    String[] leftSpinner;
    String bigParamentrs;
    String name;



    public ProductImage(Bitmap image, String[] leftSpinner, String[] spinner, String[] rightSpinner, String name, String bigParamentrs) {

        this.spinner = spinner;
        this.image = image;
        this.rightSpinner = rightSpinner;
        this.leftSpinner = leftSpinner;
        this.bigParamentrs = bigParamentrs;
        this.name = name;
    }

    public String[] getSpinner() {
        return spinner;
    }

    public void setSpinner(String[] spinner) {
        this.spinner = spinner;
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
