package com.example.siberianforest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    SharedPreferences sPref;
    final String EMAIL = "EMAIL";
    final String PASSWORD = "PASSWORD";
    final String NAMEN = "NAMEN";
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";
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
                                        sp.putString(EMAIL, emailAdress.getText().toString());
                                        sp.putString(PASSWORD, password.getText().toString());
                                        sp.putString(NAMEN, user.getDisplayName());
                                        Log.d("TAGA", sPref.getString(NAMEN, ""));
                                        sp.commit();
                                        Intent intent = new Intent(MainActivity.this, ProductCatalog.class);
                                        startActivity(intent);
                                    }else{
                                        Snackbar.make(findViewById(android.R.id.content), "Подтвердите email по отправленной вам ссылке", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(findViewById(android.R.id.content), "Ошибка подключения", Snackbar.LENGTH_LONG).show();
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
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        if (sPref.contains(SAVE_AUTOREG)) {

            if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
            {
                Intent intent = new Intent(MainActivity.this, ProductCatalog.class);
                startActivity(intent);

            }
        }
    }


}