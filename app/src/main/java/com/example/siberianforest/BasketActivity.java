package com.example.siberianforest;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {

    ArrayList<String[]> arrayLists = new ArrayList<>();
    private List<BasketParam> states = new ArrayList();
    ListView countriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"A", "B", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"C", "D", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});
        arrayLists.add(new String[]{"E", "F", "http://slp2000.ru/uploads/images/catalog/0105.jpg"});


        setInitialData();
        countriesList = (ListView) findViewById(R.id.listView);
        BasketAdapter stateAdapter = new BasketAdapter(this, R.layout.product_image_basket, states);
        countriesList.setAdapter(stateAdapter);
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        };
        countriesList.setOnItemClickListener(itemListener);
    }
    private void setInitialData(){

        for(int i = 0; i!= arrayLists.size(); i++)
        {
            states.add(new BasketParam(arrayLists.get(i)[0], arrayLists.get(i)[1], arrayLists.get(i)[2]));
        }

    }
}
