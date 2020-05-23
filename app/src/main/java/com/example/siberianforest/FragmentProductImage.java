package com.example.siberianforest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class FragmentProductImage extends Fragment {

    private FragmentProductImage fragment;
    private List<ProductImage> mItems;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HelpAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_horizontal, container, false);

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



    private void setFragment(FragmentProductImage fragment) {
        this.fragment=fragment;
    }

    class HelpAdapter extends RecyclerView.Adapter<ViewHolder>
    {
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
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.getButton().setText(mItems.get(position).bigParamentrs);
            holder.mImage.setImageBitmap(mItems.get(position).image);
            holder.name.setText(mItems.get(position).name);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,mItems.get(position).spinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner.setAdapter(adapter);


            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,mItems.get(position).leftSpinner);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerLeft.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,mItems.get(position).rightSpinner);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinnerRight.setAdapter(adapter2);


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

        private List<ProductImage> mItems;

        public List<ProductImage> getmItems() {
            return mItems;
        }

        public void setmItems(List<ProductImage> mItems) {
            this.mItems = mItems;
        }



    }
}
