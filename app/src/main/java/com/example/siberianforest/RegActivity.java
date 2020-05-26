package com.example.siberianforest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegActivity extends AppCompatActivity {
    FirebaseAuth auth;
    //FirebaseDatabase db;
    //DatabaseReference users;

    User user;
    Button leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        auth = FirebaseAuth.getInstance();
        //db = FirebaseDatabase.getInstance();
        //users = db.getReference();

        final EditText[] editTexts = new EditText[6];
        editTexts[0] = (EditText) findViewById(R.id.name);
        editTexts[1] = (EditText) findViewById(R.id.surname);
        editTexts[2] = (EditText) findViewById(R.id.middleName);
        editTexts[3] = (EditText) findViewById(R.id.phone);
        editTexts[4] = (EditText) findViewById(R.id.email);
        editTexts[5] = (EditText) findViewById(R.id.password);

        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(editTexts[0].getText().toString(), editTexts[1].getText().toString(), editTexts[2].getText().toString(), editTexts[3].getText().toString(), editTexts[4].getText().toString(), editTexts[5].getText().toString());

                if(!correctPassword())
                {
                    Snackbar.make(findViewById(android.R.id.content), "Введенный пароль слишком простой", Snackbar.LENGTH_LONG).show();

                    return;


                }
                if(!correctData())
                {
                    return;

                }

                auth.createUserWithEmailAndPassword(user.getEmail(), user.getPass())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                        //смотри когда пользователь регестрируется ему отправляется письмо
                                FirebaseAuth.getInstance().getCurrentUser()
                                        .sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                dialogWindow("Вам отправленно ссылка на email. Для подтверждения перейдите по ней");

                                                FirebaseUser userAuth = auth.getCurrentUser();
                                                UserProfileChangeRequest.Builder builder =  new UserProfileChangeRequest.Builder();
                                                String namen =  user.getSurname()+"  "+user.getName()+"  "+user.getMiddleName()+"  "+user.getPhone();
                                                builder.setDisplayName(namen);
                                                UserProfileChangeRequest u = builder.build();
                                                userAuth.updateProfile(u);
                                            }
                                        });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        });

    }

    private void dialogWindow(String str)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(RegActivity.this);

        build.setMessage(str)
                .setCancelable(false)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(RegActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = build.create();
        alertDialog.show();

    }

    private boolean correctData()
    {

        Toast toast;
        if(user.getName().length()<3)
        {
            Snackbar.make(findViewById(android.R.id.content), "Введенные данные не коректны. Проверьте поле \"Имя\"", Snackbar.LENGTH_LONG).show();

            return false;
        }
        if(user.getSurname().length()<3)
        {
            Snackbar.make(findViewById(android.R.id.content), "Введенные данные не коректны. Проверьте поле \"Фамилия\"", Snackbar.LENGTH_LONG).show();

            return false;
        }
        if(user.getMiddleName().length()<3)
        {
            Snackbar.make(findViewById(android.R.id.content), "Введенные данные не коректны. Проверьте поле \"Отчество\"", Snackbar.LENGTH_LONG).show();

            return false;
        }
        if(user.getPhone().length() !=11)
        {
            Snackbar.make(findViewById(android.R.id.content), "Введенные данные не коректны. Проверьте поле \"Телефон\"", Snackbar.LENGTH_LONG).show();

            return false;
        }if(user.getEmail().indexOf('@') == -1 || user.getEmail().length()<4)
        {
            Snackbar.make(findViewById(android.R.id.content), "Введенные данные не коректны. Проверьте поле \"Email\"", Snackbar.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    private boolean correctPassword()
    {
        if(strongPasswordChecker(user.getPass()) == 0)
        {
            return true;
        }else{
            return false;
        }
    }

    private int strongPasswordChecker(String s) {

        if(s.length()<2) return 6-s.length();


        char end = s.charAt(0);
        boolean upper = end>='A'&&end<='Z'||end>='А'&&end<='Я', lower = end>='a'&&end<='z'||end>='а'&&end<='я', digit = end>='0'&&end<='9';


        int end_rep = 1, change = 0;
        int[] delete = new int[3];

        for(int i = 1;i<s.length();++i){
            if(s.charAt(i)==end) ++end_rep;
            else{
                change+=end_rep/3;
                if(end_rep/3>0) ++delete[end_rep%3];
                //updating the states
                end = s.charAt(i);
                upper = upper||end>='A'&&end<='Z'||end>='А'&&end<='Я';
                lower = lower||end>='a'&&end<='z'||end>='а'&&end<='я';
                digit = digit||end>='0'&&end<='9';
                end_rep = 1;
            }
        }
        change+=end_rep/3;
        if(end_rep/3>0) ++delete[end_rep%3];


        int check_req = (upper?0:1)+(lower?0:1)+(digit?0:1);

        if(s.length()>20){
            int del = s.length()-20;


            if(del<=delete[0]) change-=del;
            else if(del-delete[0]<=2*delete[1]) change-=delete[0]+(del-delete[0])/2;
            else change-=delete[0]+delete[1]+(del-delete[0]-2*delete[1])/3;

            return del+Math.max(check_req,change);
        }
        else return Math.max(6-s.length(), Math.max(check_req, change));
    }


}
