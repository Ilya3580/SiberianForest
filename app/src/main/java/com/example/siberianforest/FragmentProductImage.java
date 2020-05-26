package com.example.siberianforest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProductImage extends Fragment {
    String strSize = "";
    String strSort = "";
    String saveString;
    SharedPreferences sPref;
    Context context;
    private PriceCatalog priceCatalog = new PriceCatalog();
    private int countElement;
    private ArrayList<ImageView> viewHolders = new ArrayList<>();
    private FragmentProductImage fragment;
    private List<ProductImage> mItems;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HelpAdapter mAdapter;
    final String FIRST_ENTER = "FIRST_ENTER";
    final String NAME_SPREF = "autoris";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_horizontal, container, false);
        context = getActivity().getApplicationContext();
        sPref = context.getSharedPreferences(NAME_SPREF, Context.MODE_PRIVATE);
        return view;
    }

    public void saveImage()
    {
        new Thread(new Runnable() {
        public void run() {
            saveImageHelp();
        }}).start();
    }

    private void saveImageHelp()
    {

        for(int i = 0; i!=viewHolders.size();i++) {
            if(((BitmapDrawable)viewHolders.get(i).getDrawable()) == null)
            {
                continue;
            }
            Bitmap bitmap = ((BitmapDrawable)viewHolders.get(i).getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 1 , stream);
            String str = "";
            byte[] byteArray = stream.toByteArray();
            for (int j = 0; j != byteArray.length; j++) {
                if(j == 0) {
                    str = byteArray[j] + "";
                }
                else{
                    str +="###" + byteArray[j];
                }
            }
            //Log.d("TAGZ", str);
            if(i == 0)
            {
                saveString = str;
            }else{
                saveString += "####" + str;
            }
        }
        //Log.d("TAGZ", "size = " + viewHolders.size());
        if(countElement == viewHolders.size())
        {
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(FIRST_ENTER, "FALSE");
            ed.commit();
        }

        //writeToFile(saveString, context);
    }
    private void writeToFile(String stringPriceCatalog, Context context) {
        try {
            FileOutputStream fileOutput = context.openFileOutput("imageFile.txt", MODE_PRIVATE);
            fileOutput.write(stringPriceCatalog.getBytes());
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mItems.get(positionParent).leftSpinner);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.spinnerLeft.setAdapter(adapter1);
                holder.spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(holder.spinnerLeft.getSelectedItem() != null)
                            setStrSize(holder.spinnerLeft.getSelectedItem().toString());
                        strSort = holder.spinnerLeft.getSelectedItem().toString();
                        holder.price.setText(priceCatalog.splitPrise(mItems.get(positionParent).priceCatalogs, strSize, strSort));
                        Log.d("TAGA", positionParent + "");

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
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mItems.get(positionParent).rightSpinner);
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
            viewHolders.add(holder.mImage);

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
}
