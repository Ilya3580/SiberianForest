package com.example.siberianforest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Trace;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {

    TextView noLogin;
    TextView emailAdress;
    TextView password;
    TextView reg;
    Button enterButton;

    FirebaseUser fUser;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    boolean verefication;

    SharedPreferences sPref;
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";

    String namen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);

        noLogin = (TextView)findViewById(R.id.noLogin);
        emailAdress = (TextView)findViewById(R.id.textMail);
        password = (TextView)findViewById(R.id.textPassword);
        reg = (TextView)findViewById(R.id.registr);
        enterButton = (Button)findViewById(R.id.enterButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference();


        fUser = auth.getCurrentUser();

        final FirebaseUser user = auth.getCurrentUser();



        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(emailAdress.getText().toString().equals("") || password.getText().toString().equals(""))) {
                    auth.signInWithEmailAndPassword(emailAdress.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    SharedPreferences.Editor ed = sPref.edit();
                                    ed.putString("ID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    ed.commit();

                                    if(fUser.isEmailVerified())
                                    {
                                        SharedPreferences.Editor sp = sPref.edit();
                                        sp.putString(SAVE_AUTOREG, "Authorized");
                                        sp.commit();
                                        Intent intent = new Intent(MainActivity.this, ProductCatalog.class);
                                        startActivity(intent);
                                    }else{
                                        Snackbar.make(findViewById(android.R.id.content), "Gодтвердите email по отправленной вам ссылке", Snackbar.LENGTH_LONG).show();

                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e.getMessage().equals("The password is invalid or the user does not have a password."))
                            {
                                Snackbar.make(findViewById(android.R.id.content), "Введен не верный пароль или email", Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });
                }


            }
        });

        noLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVE_AUTOREG, "notAuthorized");
                ed.commit();

                Intent intent = new Intent(MainActivity.this, ProductCatalog.class);
                startActivity(intent);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();

        Thread tread = new Thread(new Runnable() {

            public void run() {
                new Parsing().start(getApplicationContext());

            }

        });
        tread.start();

        if (sPref.contains(SAVE_AUTOREG)) {

            if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
            {
                /*SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVE_AUTOREG, "notAuthorized");
                ed.commit();*/


                Intent intent = new Intent(MainActivity.this, ProductCatalog.class);
                startActivity(intent);

            }
        }
    }
    private void read()
    {
        try {
            FileInputStream fileInput = openFileInput("textparametr.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffered = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffered.readLine())!=null)
            {
                Log.d("TAGZ", lines);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}