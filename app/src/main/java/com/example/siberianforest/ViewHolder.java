package com.example.siberianforest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    Spinner spinner;
    Spinner spinnerLeft;
    Spinner spinnerRight;
    Button button;
    TextView name;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        spinner = (Spinner) itemView.findViewById(R.id.spinner);
        mImage = (ImageView) itemView.findViewById(R.id.imageObject);
        spinnerLeft = (Spinner) itemView.findViewById(R.id.leftSpinner);
        spinnerRight = (Spinner) itemView.findViewById(R.id.rightSpinner);
        button = (Button)itemView.findViewById(R.id.bigParametrs);
        name = (TextView)itemView.findViewById(R.id.parametrs);
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
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
