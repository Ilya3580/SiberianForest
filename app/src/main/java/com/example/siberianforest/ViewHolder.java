package com.example.siberianforest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    Spinner spinnerLeft;
    Spinner spinnerRight;
    Button button;
    TextView name;
    TextView price;
    ArrayList<PriceCatalog> parsArray;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        price = (TextView)itemView.findViewById(R.id.price);
        mImage = (ImageView) itemView.findViewById(R.id.imageObject);
        spinnerLeft = (Spinner) itemView.findViewById(R.id.leftSpinner);
        spinnerRight = (Spinner) itemView.findViewById(R.id.rightSpinner);
        button = (Button)itemView.findViewById(R.id.bigParametrs);
        name = (TextView)itemView.findViewById(R.id.parametrs);
    }

    public ArrayList<PriceCatalog> getParsArray() {
        return parsArray;
    }

    public void setParsArray(ArrayList<PriceCatalog> parsArray) {
        this.parsArray = parsArray;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }

    public ImageView getmImage() {
        return mImage;
    }

    public void setmImage(ImageView mImage) {
        this.mImage = mImage;
    }

    public Spinner getSpinnerLeft() {
        return spinnerLeft;
    }

    public void setSpinnerLeft(Spinner spinnerLeft) {
        this.spinnerLeft = spinnerLeft;
    }

    public Spinner getSpinnerRight() {
        return spinnerRight;
    }

    public void setSpinnerRight(Spinner spinnerRight) {
        this.spinnerRight = spinnerRight;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
