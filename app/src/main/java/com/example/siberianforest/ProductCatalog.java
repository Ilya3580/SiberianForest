package com.example.siberianforest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCatalog extends AppCompatActivity {

    FragmentProductImage fragmentProductImage=FragmentProductImage.newInstance();
    private List<ProductImage> mItems=new ArrayList<>();

    EditText search;
    SharedPreferences sPref;
    final String SAVE_AUTOREG = "SAVE_SETTINGS";
    final String NAME_SPREF = "autoris";
    ArrayList<String> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);


        lst = new ArrayList<>();
        InputStream is = this.getResources().openRawResource(R.raw.textparametrs);
        readFile(lst, is);
        for(int i = 0; i < lst.size(); i++)
        {
            Log.d("TAGB", lst.get(i));
        }

        search = (EditText) findViewById(R.id.search);

        sPref = getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);

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

        renderCatalog();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                switch (item.getItemId()) {
                    case R.id.person:
                        Intent intent = new Intent(ProductCatalog.this, PersonActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.contact:
                        break;
                    case R.id.catalog:
                        break;
                    case R.id.basket:
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
        for(int i = 0; i!=lst.size();i++) {
            String[] mas = lst.get(i).split("&");
            String img = mas[0];
            int idImage = getResources().getIdentifier(img, "drawable", getPackageName());
            String[] str = ("Материал," + mas[3]).split(",");
            String[] str1 = ("Сорт," + mas[2]).split(",");
            String[] str2 = ("Размер," + mas[4]).split(",");
            mItems.add(new ProductImage(BitmapFactory.decodeResource(getResources(), idImage), str1, str, str2, mas[1], "Информация"));
        }
        fragmentProductImage.setItems(mItems);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentView,fragmentProductImage).commit();
    }


    public void readFile(ArrayList<String> lst, InputStream is)
    {
        String texttxt = "";
        StringBuffer sbuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {

            while ((texttxt = reader.readLine()) != null) {
                lst.add(texttxt);

            }

            //textView1.setText(sbuffer);
            is.close();


        } catch (Exception e) {
            e.printStackTrace();


        }

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

        Collections.sort(lst, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int a1 = levenstain(o1.split("&")[1],str);
                int a2 = levenstain(o2.split("&")[1],str);
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
}



