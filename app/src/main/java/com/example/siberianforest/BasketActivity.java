package com.example.siberianforest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {

    SharedPreferences sPref;
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String EMAIL = "EMAIL";
    final String PASSWORD = "PASSWORD";
    final String NAMEN = "NAMEN";
    final String NAME_SPREF = "autoris";
    Button button;
    TextView textView;
    ArrayList<String[]> arrayLists = new ArrayList<>();
    private List<BasketParam> states = new ArrayList();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.basketClear);
        listView = (ListView) findViewById(R.id.listView);

        if (sPref.contains(SAVE_AUTOREG)) {

            if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
            {
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GMailSender.withAccount("afanasevaleksej532@gmail.com", "49154017")
                                .withTitle("Новый заказ")
                                .withBody("Информация о пользователе" + sPref.getString(EMAIL, "")
                                        + ".  Email" + sPref.getString(NAMEN, "") + "Заказ:" + read())
                                .withSender("afanasevaleksej532@gmail.com")
                                .toEmailAddress("ilya.smetanin.2002@mail.ru") // one or multiple addresses separated by a comma
                                .withListenner(new GmailListener() {
                                    @Override
                                    public void sendSuccess() {
                                        dialogWindow("Ваш заказ оформлен. В течении двух дней с вами свяжется менеджер");
                                    }

                                    @Override
                                    public void sendFail(String err) {
                                        Snackbar.make(findViewById(android.R.id.content), "Проверьте подключение к интерненту", Snackbar.LENGTH_LONG).show();
                                    }
                                })
                                .send();


                        writeToFile("");
                        onStart();
                    }
                });

            }else{
                button.setVisibility(View.GONE);
            }
        }



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.person:
                        intent = new Intent(getApplicationContext(), PersonActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.contact:
                        break;
                    case R.id.catalog:
                        intent = new Intent(getApplicationContext(), ProductCatalog.class);
                        startActivity(intent);
                        break;
                    case R.id.basket:

                        break;
                }
                return true;
            }
        });

        String splitStr = read();
        if(splitStr == "")
        {
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.GONE);
            Log.d("Taga", splitStr);
            String[] mas = splitStr.split("####");
            for(int i = 0; i!= mas.length; i++)
            {
                arrayLists.add(mas[i].split("###"));
            }
            setInitialData();
        }

        BasketAdapter stateAdapter = new BasketAdapter(this, R.layout.product_image_basket, states);
        listView.setAdapter(stateAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        };
        listView.setOnItemClickListener(itemListener);
    }
    private void setInitialData(){

        for(int i = 0; i!= arrayLists.size(); i++)
        {
            states.add(new BasketParam(arrayLists.get(i)[1] + "\nКоличество: " + arrayLists.get(i)[0].split(" \\(")[0],
                    "Размер: " + arrayLists.get(i)[2] + "\nЦена:" + arrayLists.get(i)[3] , arrayLists.get(i)[4]));
        }

    }
    private String read()
    {
        String text = "";
        try {

            FileInputStream fileInput = openFileInput("basketParametrs.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader buffered = new BufferedReader(reader);
            StringBuffer strBuffer = new StringBuffer();
            String lines;

            while ((lines = buffered.readLine())!=null)
            {
                text+=lines;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
    private void dialogWindow(String str)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(BasketActivity.this);

        build.setMessage(str)
                .setCancelable(false)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BasketActivity.this, ProductCatalog.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

        AlertDialog alertDialog = build.create();
        alertDialog.show();

    }
    private void writeToFile(String stringPriceCatalog) {
        try {
            FileOutputStream fileOutput = openFileOutput("basketParametrs.txt", MODE_PRIVATE);
            fileOutput.write(stringPriceCatalog.getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
