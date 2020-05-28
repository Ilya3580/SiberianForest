package com.example.siberianforest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;

public class PersonActivity extends AppCompatActivity {

    Button button;
    FirebaseUser user;
    SharedPreferences sPref;
    FirebaseAuth auth;
    final String EMAIL = "EMAIL";
    final String PASSWORD = "PASSWORD";
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";
    final String NAMEN = "NAMEN";
    String[] mas;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        listView = (ListView)findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        mas = new String[7];

        mas[0] = "Сменить имя пользователя";
        mas[1] = "Сменить фамилию пользователя";
        mas[2] = "Сменить отчество пользователя";
        mas[3] = "Сменить номер телефона";
        mas[4] = "Сменить email";
        mas[5] = "Сменить пароль";
        mas[6] = "выйти";

        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);

        if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
        {
            button.setText(user.getDisplayName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            listViewRender();

        }else{

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            TextView textView = findViewById(R.id.textView);
            textView.setText("Выполните авторизацию");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.person:
                        break;
                    case R.id.contact:
                        intent = new Intent(getApplicationContext(), MapsActivityContact.class);
                        startActivity(intent);
                        break;
                    case R.id.catalog:
                        intent = new Intent(getApplicationContext(), ProductCatalog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        startActivity(intent);
                        break;
                    case R.id.basket:
                        intent = new Intent(getApplicationContext(), BasketActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

    }

    private void listViewRender()
    {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mas);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0 || id == 1 || id == 2 || id == 3)
                {
                    String a = user.getDisplayName();
                    Integer i = (int) (long) id;
                    alertDialogNamen(i);
                }
                if(id == 4) {
                    alertDialogPassword();
                }
                if(id == 5)
                {
                    if (sPref.contains(EMAIL)) {
                        auth.sendPasswordResetEmail(sPref.getString(EMAIL, ""))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialogWindow("Вам отправленно письмо на email для сброса пароля");

                                        }
                                    }
                                });
                    }

                }
                if(id == 6)
                {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(SAVE_AUTOREG, "notAuthorized");
                    ed.commit();
                    Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    clearUser();
                    startActivity(intent);
                }

            }
        });
    }
    private void dialogWindow(String str)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(PersonActivity.this);

        build.setMessage(str)
                .setCancelable(false)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        clearUser();
                        startActivity(intent);

                    }
                });

        AlertDialog alertDialog = build.create();
        alertDialog.show();

    }

    private void alertDialogNamen(final int id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id)
        {
            case 0: builder.setTitle("Введите имя");break;
            case 1: builder.setTitle("Введите фамилию");break;
            case 2: builder.setTitle("Введите отчество");break;
            case 3: builder.setTitle("Введите номер телефона");break;
        }

        final EditText input = new EditText(this);
        if(id != 3) {
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }else{
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sPref.contains(NAMEN)) {
                    String text = input.getText().toString();
                    FirebaseUser userAuth = auth.getCurrentUser();
                    UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                    String namen =user.getDisplayName();

                    String[] mas = namen.split("  ");
                    switch (id)
                    {
                        case 0: mas[1] = text;break;
                        case 1: mas[0] = text;break;
                        case 2: mas[2] = text;break;
                        case 3: mas[3] = text;break;
                    }
                    namen = mas[0] + "  " + mas[1] + "  " + mas[2] + "  " + mas[3];
                    builder.setDisplayName(namen);
                    UserProfileChangeRequest u = builder.build();
                    userAuth.updateProfile(u);
                    button.setText(namen);
                }
            }
        });

        builder.show();
    }

    private void alertDialogPassword()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите пароль от аккаунта");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        if (sPref.contains(PASSWORD)) {
                            if(sPref.getString(PASSWORD, "").equals(text))
                            {
                                alertDialogSet();
                            }
                    }
                }
        });

        builder.show();
    }

    private void alertDialogSet()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите новый email");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();

                if(!sPref.getString(EMAIL, "").equals(text))
                        user.updateEmail(text)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                           user.sendEmailVerification()
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()) {
                                                               dialogWindow("Вам отправленно ссылка на email. Для подтверждения перейдите по ней");
                                                               SharedPreferences.Editor ed = sPref.edit();
                                                               ed.putString(SAVE_AUTOREG, "notAuthorized");
                                                               ed.commit();

                                                           }
                                                       }

                                                   });
                                            //Toast.makeText(PersonActivity.this, "Email address is updated.", Toast.LENGTH_LONG).show();
                                        } else {
                                            //Toast.makeText(PersonActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                }


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void clearUser()
    {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(EMAIL, "");
        ed.putString(PASSWORD, "");
        ed.putString(NAMEN, "");
        ed.commit();
    }


}
