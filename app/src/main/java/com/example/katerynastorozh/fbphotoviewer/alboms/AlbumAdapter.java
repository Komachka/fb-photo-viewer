package com.example.katerynastorozh.fbphotoviewer.alboms;

/**
 * Created by kateryna on 18.11.18.
 */
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.example.katerynastorozh.fbphotoviewer.utils.ImageHelper;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final List<AlbumItem> alboms;
    private final OnItemClickListener listener;
    private final AlbumActivity activity;


    public interface OnItemClickListener {
        void onItemClick(AlbumItem item);
    }



    public AlbumAdapter(AlbumActivity activity, List<AlbumItem> albums, OnItemClickListener listener ) {
        this.alboms = albums;
        this.listener = listener;
        this.activity = activity;
    }

    public static class AlbumItem {
        final String id;
        final String name;
        final String imageURI;


        public AlbumItem(String id, String name, String imageURI) {
            this.id = id;
            this.name = name;
            this.imageURI = imageURI;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_alboms, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.bind(alboms.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return alboms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ImageHelper imageHelper;
        final View view;
        final TextView textName;
        final ImageView albumPic;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activity.imageHelper = new ImageHelper(itemView.getContext());
            view = itemView;
            textName = itemView.findViewById(R.id.name);
            albumPic = itemView.findViewById(R.id.albompic);
        }


        public void bind(final AlbumItem item, final OnItemClickListener listener) {
            final AlbumItem albumItem = item;
            textName.setText(albumItem.name);
            activity.imageHelper.loadImageToView(albumItem.imageURI, albumPic);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(albumItem);
                }
            });
        }
    }



}
