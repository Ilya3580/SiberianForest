package com.example.siberianforest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static android.view.View.GONE;

public class PersonActivity extends AppCompatActivity {

    Button button;

    SharedPreferences sPref;
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";
    String[] mas;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        listView = (ListView)findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);

        mas = new String[6];

        mas[0] = "Сменить имя пользователя";
        mas[1] = "Сменить фамилию пользователя";
        mas[2] = "Сменить отчество пользователя";
        mas[3] = "Сменить номер телефона";
        mas[4] = "Сменить email";
        mas[5] = "выйти";

        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);

        if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
        {
            button.setVisibility(GONE);
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



    }

    private void listViewRender()
    {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mas);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id == 5)
                {
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(SAVE_AUTOREG, "notAuthorized");
                    ed.commit();


                    Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }

            }
        });
    }


}
