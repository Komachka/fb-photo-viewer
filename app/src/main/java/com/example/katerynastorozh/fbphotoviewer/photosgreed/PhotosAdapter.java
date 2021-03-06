package com.example.katerynastorozh.fbphotoviewer.photosgreed;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.example.katerynastorozh.fbphotoviewer.photoitem.PhotoItemViewActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.PHOTOS_DATA;
import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.SLIDER_POSITION;

/**
 * Created by kateryna on 18.11.18.
 */

public class PhotosAdapter extends BaseAdapter {

    private List<String> pictures;


    PhotosAdapter(List<String> pictures) {
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

    public class ViewHolder{
        private ImageView imageView;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewHolder holder = new ViewHolder();
        final View gridView = layoutInflater.inflate(R.layout.grid_item, viewGroup, false);
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
                intent.putStringArrayListExtra(PHOTOS_DATA, (ArrayList<String>) pictures);
                intent.putExtra(SLIDER_POSITION, position);
                view.getContext().startActivity(intent);

            }
        });

        return gridView;
    }
}
