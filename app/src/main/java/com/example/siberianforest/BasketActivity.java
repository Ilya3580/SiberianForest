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

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

    String splitStr;
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
                        alertDialogPassword();
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

                        break;
                }
                return true;
            }
        });

        splitStr = read();
        if(splitStr == "")
        {
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.GONE);
            stringToArray();
            setInitialData();
        }

        BasketAdapter stateAdapter = new BasketAdapter(this, R.layout.product_image_basket, states);
        listView.setAdapter(stateAdapter);
        final AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String str = states.get(safeLongToInt(id)).getName();

                alertDialogSet(str);

            }
        };
        listView.setOnItemClickListener(itemListener);
    }
    private void setInitialData(){

        for(int i = 0; i!= arrayLists.size(); i++)
        {
            String size = arrayLists.get(i)[0];
            if(size.length() > 1) {
                if (size.charAt(0) == '0' && size.charAt(1) != '.') {
                    size = size.substring(1);
                }
            }
            //String[] mas = arrayLists.get(i)[3].split(" ");
            //String prise = mas[0] + " " + mas[1];
            if(!arrayLists.get(i)[0].equals(""))
                states.add(new BasketParam(arrayLists.get(i)[1] + "\nКоличество: " + size,
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

    private int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void destroy(String name)
    {
        String str = "";
        arrayLists.clear();
        String[] mas = splitStr.split("####");
        for(int i = 0; i!= mas.length; i++)
        {
            String splitName = name.split("Кол")[0];
            splitName = splitName.substring(0, splitName.length()-1);

            if(!mas[i].contains(splitName)) {
                if(str.equals(""))
                {
                    str = mas[i];
                }else{
                    str = str + "####" + mas[i];
                }
                arrayLists.add(mas[i].split("###"));
            }
        }
        splitStr = str;
        writeToFile(str);
    }

    private void stringToArray()
    {
        Log.d("TAGA", splitStr);
        arrayLists.clear();
        String[] mas = splitStr.split("####");
        for(int i = 0; i!= mas.length; i++)
        {
            arrayLists.add(mas[i].split("###"));
        }
    }

    private void alertDialogSet(final String name)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Удалить элемент");

        builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destroy(name);
                        Intent intent = new Intent(BasketActivity.this, BasketActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.cancel();
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
                        GMailSender.withAccount("afanasevaleksej532@gmail.com", "49154017")
                                .withTitle("Новый заказ")
                                .withBody("Информация о пользователе" + sPref.getString(EMAIL, "")
                                        + ".  Email" + sPref.getString(NAMEN, "") + "Заказ:" + read())
                                .withSender("afanasevaleksej532@gmail.com")
                                .toEmailAddress("ilya.smetanin.2002@mail.ru")
                                .withListenner(new GmailListener() {
                                    @Override
                                    public void sendSuccess() {
                                        dialogWindow("Ваш заказ оформлен. В течении двух дней с вами свяжется менеджер");
                                    }

                                    @Override
                                    public void sendFail(String err) {
                                        Snackbar.make(findViewById(android.R.id.content), "Проверьте подключение к интернету", Snackbar.LENGTH_LONG).show();
                                    }
                                })
                                .send();


                        writeToFile("");
                        onStart();
                    }
                }
            }
        });

        builder.show();
    }
}
