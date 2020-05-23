package com.example.siberianforest;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.IOException;

public class PriceCatalog {
    private String requestCreator = "a";
    private String name = null;
    private String material = null;
    private String size = null;
    private String priceM2 = null;
    private String priceM3 = null;

    public PriceCatalog(RequestCreator requestCreator, String name, String material, String size, String priceM2, String priceM3) {

        if(requestCreator != null) {
            inBitmap(requestCreator);
        }
        if(name != null)
            this.name = name.trim();
        if(material != null)
            this.material = material.trim();
        if(size != null)
            this.size = size.trim();
        if(priceM2 != null)
            this.priceM2 = priceM2.trim();
        if(priceM3 != null)
            this.priceM3 = priceM3.trim();
    }

    private void inBitmap(final RequestCreator requestCreator)
    {
        requestCreator.into(
                new Target(){

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //requestCreator = bitmap.getNinePatchChunk();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                }
        );
    }

    private String byteToString(byte[] masByte)
    {

        if(masByte == null)
            return "RRRRRRRRRRRRRRRRRRRRR";
        String str = "";
        Log.d("BYTE", masByte[0] + "");
        for(int i = 0; i!=masByte.length;i++)
        {
            if(i != 0)
            {
                str+="###" + masByte[i];
            }else{
                str = masByte[i] + "";
            }
        }
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPriceM2() {
        return priceM2;
    }

    public void setPriceM2(String priceM2) {
        this.priceM2 = priceM2;
    }

    public String getPriceM3() {
        return priceM3;
    }

    public void setPriceM3(String priceM3) {
        this.priceM3 = priceM3;
    }

    @NonNull
    @Override
    public String toString() {
        return requestCreator + ";" + name + ";" + material + ";" +size+ ";" +priceM2+ ";" +priceM3;
    }
}
