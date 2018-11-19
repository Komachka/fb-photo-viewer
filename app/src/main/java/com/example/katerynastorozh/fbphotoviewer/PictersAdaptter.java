package com.example.katerynastorozh.fbphotoviewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kateryna on 18.11.18.
 */

public class PictersAdaptter extends BaseAdapter {

    List<String> pictures;


    public PictersAdaptter(List<String> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return  pictures.size();
    }

    @Override
    public Object getItem(int i) {
        return pictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        final View gridView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        holder.imageView = (ImageView) gridView.findViewById(R.id.grid_item);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .borderColor(ContextCompat.getColor(holder.imageView.getContext(), R.color.destBackground))
                .borderWidthDp(1)
                .oval(false)
                .build();
        Picasso.with(holder.imageView.getContext()).load(pictures.get(position)).placeholder(R.drawable.progress_animation)
                .transform(transformation).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PhotoItemViewActivity.class);
                intent.putStringArrayListExtra("data", (ArrayList<String>) pictures);
                intent.putExtra("position", position);
                view.getContext().startActivity(intent);

            }
        });

        return gridView;
    }
}
