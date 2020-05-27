package com.example.siberianforest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProductImage extends Fragment {

    String[] masL;
    String[] masR;
    String strSize = "";
    String strSort = "";
    Context context;
    private PriceCatalog priceCatalog = new PriceCatalog();
    private List<ProductImage> mItems;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HelpAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_horizontal, container, false);
        context = getActivity().getApplicationContext();
        return view;
    }


    public static FragmentProductImage newInstance() {
        FragmentProductImage fragment = new FragmentProductImage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setItems(List<ProductImage> items){
        this.mItems=items;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=(RecyclerView)view.findViewById(R.id.listView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HelpAdapter();
        mAdapter.setmItems(mItems);
        recyclerView.setAdapter(mAdapter);

    }

    class HelpAdapter extends RecyclerView.Adapter<ViewHolder>
    {
        private List<ProductImage> mItems;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.product_image_vertical, parent, false));
            }else{
                return new ViewHolder(getLayoutInflater().inflate(R.layout.product_image_horizontal, parent, false));
            }

        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, final int positionParent) {
            Picasso.with(getActivity().getApplicationContext())
                    .load(mItems.get(positionParent).getLink())
                    .into(holder.mImage);

            holder.price.setText(mItems.get(positionParent).price);
            holder.getButton().setText(mItems.get(positionParent).bigParamentrs);
            holder.name.setText(mItems.get(positionParent).name);


            if(mItems.get(positionParent).leftSpinner != null) {
                holder.spinnerLeft.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, masL = mItems.get(positionParent).leftSpinner);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerLeft.setAdapter(adapter1);
                holder.spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(holder.spinnerRight.getSelectedItem() != null)
                            setStrSize(holder.spinnerRight.getSelectedItem().toString());
                        strSort = holder.spinnerLeft.getSelectedItem().toString();
                        holder.price.setText(priceCatalog.splitPrise(mItems.get(positionParent).priceCatalogs, strSize, strSort));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else{
                strSize = null;
                holder.spinnerLeft.setVisibility(View.GONE);
                holder.spinnerRight.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                holder.spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            if(mItems.get(positionParent).rightSpinner != null) {
                holder.spinnerRight.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, masR = mItems.get(positionParent).rightSpinner);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerRight.setAdapter(adapter2);

                holder.spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(holder.spinnerLeft.getSelectedItem() != null)
                            setStrSort(holder.spinnerLeft.getSelectedItem().toString());
                        strSize = holder.spinnerRight.getSelectedItem().toString();
                        holder.price.setText(priceCatalog.splitPrise(mItems.get(positionParent).priceCatalogs, strSize, strSort));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else {
                strSort = null;
                holder.spinnerRight.setVisibility(View.GONE);
                holder.spinnerLeft.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                holder.spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = true;

                    String sortSize = "";
                    if(holder.spinnerLeft.getSelectedItem()!= null)
                    {
                        if(holder.spinnerLeft.getSelectedItem().toString().equals("сорт"))
                        {
                            flag = false;
                        }else{
                            sortSize = holder.spinnerLeft.getSelectedItem().toString() + " ";
                        }
                    }

                    if(holder.spinnerRight.getSelectedItem()!= null)
                    {
                        if(holder.spinnerRight.getSelectedItem().toString().equals("размер"))
                        {
                            flag = false;
                        }else{
                            sortSize = holder.spinnerRight.getSelectedItem().toString();
                        }
                    }


                    if(flag)
                    {
                        dialogParam(holder.name.getText().toString(),
                                sortSize,
                                holder.price.getText().toString(),
                                mItems.get(positionParent).getLink()
                                );
                    }else{
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Не все параметры установленны", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if(getmItems() != null)
                return getmItems().size();
            return 0;
        }

        public int getItemViewType(int position) {
            return position % 4;
        }

        public List<ProductImage> getmItems() {
            return mItems;
        }

        public void setmItems(List<ProductImage> mItems) {
            this.mItems = mItems;
        }

        public int safeLongToInt(long l) {
            if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
                throw new IllegalArgumentException
                        (l + " cannot be cast to int without changing its value.");
            }
            return (int) l;
        }

    }
    public void setStrSize(String strSize) {
        this.strSize = strSize;
    }

    public void setStrSort(String strSort) {
        this.strSort = strSort;
    }

    private void dialogParam(final String name, final String sortSize, final String price, final String link)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Введите количество");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enterText = input.getText().toString();
                enterText = "0"+enterText;
                enterText = enterText.replaceAll(",", ".");
                double enterTextDouble = Double.parseDouble(enterText);
                if(enterTextDouble < 0.1)
                {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Введенное значение недоступно! Введите большее значение", Snackbar.LENGTH_LONG).show();
                    return;
                }

                String str = read();
                boolean flag = true;
                String[] mas = str.split("####");
                str = "";
                for(int i = 0; i!=mas.length;i++)
                {
                    if(mas[i].contains(name))
                    {
                        flag = false;
                        String[] masI = mas[i].split("###");
                        masI[0] = String.valueOf(Double.valueOf(masI[0]) + enterTextDouble);
                        mas[i] = masI[0] + "###" + masI[1] + "###" +  masI[2] + "###" + masI[3] + "###" + masI[4];
                    }
                    if(i == 0)
                    {
                        str = mas[i];
                    }else{
                        str = "####" + mas[i];
                    }
                }
                if(flag)
                {
                    if(str.equals(""))
                    {
                        str = enterText + "###" + name + "###" + sortSize + "###" + price + "###" + link;
                    }else{
                        str = str + "####" + enterText + "###" + name + "###" + sortSize + "###" + price + "###" + link;
                    }
                }
                Log.d("TAGA", str);
                writeToFile(str);
                //writeToFile("");
        }});

        builder.show();
    }
    private void writeToFile(String stringPriceCatalog) {
        try {
            FileOutputStream fileOutput = context.openFileOutput("basketParametrs.txt", MODE_PRIVATE);
            fileOutput.write(stringPriceCatalog.getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read()
    {
        String text = "";
        try {

            FileInputStream fileInput = getActivity().openFileInput("basketParametrs.txt");
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
