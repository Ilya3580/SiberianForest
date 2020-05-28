package com.example.siberianforest;

import androidx.fragment.app.FragmentActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivityContact extends FragmentActivity implements OnMapReadyCallback {

    String mail = "2000slp@mail.ru", phone = "89850355720";
    TextView textPhone;
    TextView textMail;
    Button buttonPhone;
    Button buttonMail;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_contact);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textPhone = (TextView)findViewById(R.id.phoneNumber);
        textMail = (TextView)findViewById(R.id.email);
        buttonPhone = (Button)findViewById(R.id.copyPhoneNumber);
        buttonMail = (Button)findViewById(R.id.copyEmail);

        textPhone.setText(phone);
        textMail.setText(mail);

        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toCall = "tel:" + phone;

                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(toCall)));
            }
        });

        buttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", phone);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(findViewById(android.R.id.content), "email скопирован в буфер обмена", Snackbar.LENGTH_LONG).show();
            }
        });
        

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng base = new LatLng(55.616205, 37.793385);
        mMap.addMarker(new MarkerOptions().position(base).title(""));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(base));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(base,11f));
    }
}
