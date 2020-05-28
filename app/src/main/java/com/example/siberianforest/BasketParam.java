package com.example.siberianforest;

public class BasketParam {

    private String name;
    private String parametrs;
    private String imageView;

    public BasketParam(String name, String parametrs, String imageView){

        this.name=name;
        this.parametrs =parametrs;
        this.imageView =imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParametrs() {
        return parametrs;
    }

    public void setParametrs(String parametrs) {
        this.parametrs = parametrs;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }
}