package com.example.siberianforest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BasketAdapter extends ArrayAdapter<BasketParam> {

    private LayoutInflater inflater;
    private int layout;
    private List<BasketParam> states;

    public BasketAdapter(Context context, int resource, List<BasketParam> states) {
        super(context, resource, states);
        this.states = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageObject);
        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView parametrs = (TextView) view.findViewById(R.id.parametrs);

        BasketParam state = states.get(position);
        Picasso.with(getContext())
                .load(state.getImageView())
                .into(imageView);
        nameView.setText(state.getName());
        parametrs.setText(state.getParametrs());

        return view;
    }
}