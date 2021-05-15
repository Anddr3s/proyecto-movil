package com.example.gatobar.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.gatobar.R;
import com.example.gatobar.models.Images;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class Adapter extends ArrayAdapter {

    Activity context;
    ArrayList<Images> datos;

    public Adapter(Activity context,ArrayList<Images> datos) {
        super(context, R.layout.list_imgs,datos);
        this.context = context;
        this.datos = datos;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_imgs, null);

        ImageView photo= view.findViewById(R.id.imgPhoto);
        TextView author = view.findViewById(R.id.txtAutor);
        TextView width = view.findViewById(R.id.txtWidth);
        TextView height = view.findViewById(R.id.txtHeight);
        TextView URL = view.findViewById(R.id.txtURL);

        Images i = (Images) datos.get(position);

        Picasso.with(context.getApplicationContext()).load(i.getDownload_url()).error(R.mipmap.ic_launcher).fit().centerInside().into(photo);
        author.setText(i.getAuthor());
        width.setText((i.getWidth()) + " x");
        height.setText(String.valueOf(i.getHeight()));
        URL.setText(i.getUrl());

        return view;
    }
}
