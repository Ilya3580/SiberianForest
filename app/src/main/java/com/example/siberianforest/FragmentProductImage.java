package com.example.siberianforest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProductImage extends Fragment {
    ArrayList<String> arrayListBasket = new ArrayList<>();
    String[] masLf;
    String[] masRg;
    String strSize = "";
    String strSort = "";
    SharedPreferences sPref;
    Context context;
    private PriceCatalog priceCatalog = new PriceCatalog();
    private int countElement;
    private FragmentProductImage fragment;
    private List<ProductImage> mItems;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HelpAdapter mAdapter;
    final String NAME_SPREF = "autoris";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_horizontal, container, false);
        context = getActivity().getApplicationContext();
        sPref = context.getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);
        return view;
    }


    public static FragmentProductImage newInstance() {
        FragmentProductImage fragment = new FragmentProductImage();
        fragment.setFragment(fragment);
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

    public void setCountElement(int countElement) {
        this.countElement = countElement;
    }

    private void setFragment(FragmentProductImage fragment) {
        this.fragment=fragment;
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
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, masLf = mItems.get(positionParent).leftSpinner);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerLeft.setAdapter(adapter1);
                holder.spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(holder.spinnerLeft.getSelectedItem() != null)
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
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, masRg = mItems.get(positionParent).rightSpinner);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerRight.setAdapter(adapter2);

                holder.spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(holder.spinnerLeft.getSelectedItem() != null)
                            setStrSort(holder.spinnerLeft.getSelectedItem().toString());
                        strSize = holder.spinnerRight.getSelectedItem().toString();
                        holder.price.setText(priceCatalog.splitPrise(mItems.get(positionParent).priceCatalogs, strSize, strSort));
                        Log.d("TAGA", positionParent + "");

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

            /*holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name;
                    String link;
                    String spinL = null,spinR = null;
                    String[] masL = null, masR = null;
                    if(strSize == null)
                    {
                        masL = masLf;
                        spinL = holder.spinnerLeft.getSelectedItem().toString();
                    }
                    if(strSort == null)
                    {
                        masR = masRg;
                        spinR = holder.spinnerRight.getSelectedItem().toString();
                    }
                    if(strSize != null && strSort != null && holder.spinnerLeft.getSelectedItem() != null && holder.spinnerRight.getSelectedItem() != null)
                    {
                        masL = masLf;
                        masR = masRg;
                        spinL = holder.spinnerLeft.getSelectedItem().toString();
                        spinR = holder.spinnerRight.getSelectedItem().toString();
                    }
                    link = mItems.get(positionParent).getLink();
                    name = mItems.get(positionParent).name;

                    alertDialogCount(holder.price.getText().toString(), name, link, spinL, spinR, masL, masR);

                }
            });*/

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

        private void writeToFile(String stringPriceCatalog) {
            try {
                FileOutputStream fileOutput = context.openFileOutput("image.txt", MODE_PRIVATE);
                fileOutput.write(stringPriceCatalog.getBytes());
                fileOutput.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    /*private void alertDialogCount(final String strPrice, final String name, final String link,
                                  final String spinL, final String spinR, final String[] masL, final String[] masR)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Введите количество " + strPrice.trim().substring(strPrice.trim().charAt('M')));
        final EditText input = new EditText(getActivity());

        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int countProduct = Integer.parseInt(input.getText().toString());
                String strMasL = "", strMasR = "";
                if(masL != null)
                    for(int i = 0; i!= masL.length; i++)
                    {
                        if(i == 0) {
                            strMasL = masL[i];
                        }
                        else
                        {
                            strMasL =strMasL + "###" + masL[i];
                        }
                    }
                if(masR != null)
                    for(int i = 0; i!= masR.length; i++)
                    {
                        if(i == 0) {
                            strMasR = masR[i];
                        }
                        else
                        {
                            strMasR =strMasR + "###" + masR[i];
                        }
                    }

                String conc = name + "####" + spinL + "####" + spinR;
                Log.d("TAGA", conc);
                arrayListBasket.add(conc);
            }
        });

        builder.show();
    }*/

    public ArrayList<String> getArrayListBasket() {
        return arrayListBasket;
    }
}
