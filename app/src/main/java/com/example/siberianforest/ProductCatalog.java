package com.example.siberianforest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductCatalog extends AppCompatActivity {

    FragmentProductImage fragmentProductImage=FragmentProductImage.newInstance();
    private List<ProductImage> mItems=new ArrayList<>();
    EditText search;
    SharedPreferences sPref;
    final String FIRST_ENTER = "FIRST_ENTER";
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";
    ArrayList<ArrayList<PriceCatalog>> lstPars;
    PriceCatalog priceCatalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_catalog);
        search = (EditText) findViewById(R.id.search);
        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);
        priceCatalog = new PriceCatalog();

        if (sPref.contains(FIRST_ENTER)) {
            if (sPref.getString(FIRST_ENTER, "").equals("TRUE")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Parsing().start(getApplicationContext());
                    }
                });
                lstPars = priceCatalog.splitString(read());
                String data = read();
                lstPars = priceCatalog.splitString(data);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(FIRST_ENTER, "FALSE");
                ed.commit();
            }
            else
            {
                String data = read();
                lstPars = priceCatalog.splitString(data);
            }
        }else{
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(FIRST_ENTER, "FALSE");
            ed.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Parsing().start(getApplicationContext());
                }
            });
            lstPars = priceCatalog.splitString(read());
            String data = read();
            lstPars = priceCatalog.splitString(data);
        }

        for(int i = 0; i!= lstPars.size();i++)
        {
            Log.d("TAGA", "----------------------");
            for(int j = 0; j!=lstPars.get(i).size(); j++)
            {
                Log.d("TAGA", lstPars.get(i).get(j).toString());
            }
        }

        sortList("");
        renderCatalog();

        renderCatalog();
        if (sPref.contains(SAVE_AUTOREG)) {

            if (sPref.getString(SAVE_AUTOREG, "").equals("Authorized")) {
                findViewById(R.id.fragmentArrow).setVisibility(View.GONE);

            }
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String str = search.getText().toString();
                sortList(str);
                renderCatalog();
            }
        });

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
    private void renderCatalog()
    {
        if(findViewById(R.id.fragmentView) != null)
        {
            getSupportFragmentManager().beginTransaction().remove(fragmentProductImage).commit();
            fragmentProductImage.onDestroy();
            fragmentProductImage=FragmentProductImage.newInstance();
        }
        mItems.clear();
        for(int i = 0; i!=lstPars.size();i++) {
            mItems.add(new ProductImage(null,priceCatalog.splitSort(lstPars.get(i)),
                    priceCatalog.splitSize(lstPars.get(i)),
                    lstPars.get(i).get(0).getName(),
                    "Добавить в корзину",
                    lstPars.get(i).get(0).getLineImage(),
                    priceCatalog.splitPrise(lstPars.get(i), "размер", "сорт"),
                    lstPars.get(i)
                    ));
        }
        fragmentProductImage.setItems(mItems);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentView,fragmentProductImage).commit();
    }

    @Override
    public void onBackPressed() {
        if (sPref.contains(SAVE_AUTOREG)) {

            if(sPref.getString(SAVE_AUTOREG, "").equals("Authorized"))
            {
                moveTaskToBack(true);

                super.onDestroy();

                System.runFinalizersOnExit(true);

                System.exit(0);

            }else{
                super.onBackPressed();
            }
        }
    }
    private static int levenstain(String str1, String str2)
    {
        int[][] str = new int[str1.length()+1][str2.length()+1];
        for(int i = 0;i!=str1.length()+1; i++)
        {
            str[i][0] = i;
        }

        for(int i = 0;i!=str2.length()+1; i++)
        {
            str[0][i] = i;
        }
        for(int i = 1; i!=str1.length()+1; i++)
        {
            for(int j = 1; j!=str2.length()+1;j++)
            {
                int lev = str1.charAt(i-1) == str2.charAt(j-1)?0:1;
                lev = Math.min(str[i-1][j], Math.min(str[i][j-1],str[i-1][j-1])) + lev;
                str[i][j] = lev;
            }
        }



        return str[str1.length()][str2.length()];
    }
    private void sortList(final String str)
    {

        Collections.sort(lstPars, new Comparator<ArrayList<PriceCatalog>>() {
            @Override
            public int compare(ArrayList<PriceCatalog> o1, ArrayList<PriceCatalog> o2) {
                Log.d("TAGZ", o1.get(0).getName().split("\\(")[0] + "");
                int a1 = helpLev(o1.get(0).getName(), str);
                int a2 = helpLev(o2.get(0).getName(), str);
                if(a1>a2)
                    return 1;
                if(a2 == a1)
                    return 0;
                if(a1<a2)
                    return -1;
                return 0;
            }
        });
    }
    private int helpLev(String string1, String string2)
    {
        String[] str = string1.split(" ");
        int min = Integer.MAX_VALUE;
        for(int i = 0;i!=str.length;i++)
        {
            int a = levenstain(str[i], string2);
            if(a<min)
            {
                min = a;
            }
        }
        return min;
    }
    private String read()
    {
        String text = "";
        try {

            FileInputStream fileInput = openFileInput("textparametr.txt");
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
}



