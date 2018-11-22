package com.example.katerynastorozh.fbphotoviewer.alboms;

/**
 * Created by kateryna on 18.11.18.
 */
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(AlbomItem item);
    }

    private final List<AlbomItem> alboms;
    private final OnItemClickListener listener;



    public AlbumAdapter(List<AlbomItem> alboms, OnItemClickListener listener) {
        this.alboms = alboms;
        this.listener = listener;
    }

    public static class AlbomItem {
        final String id;
        final String name;
        final String imageURI;


        public AlbomItem(String id, String name, String imageURI) {
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


        final View mView;
        final TextView mName;
        final ImageView mAlbomPic;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.name);
            mAlbomPic = itemView.findViewById(R.id.albompic);


        }


        public void bind(final AlbomItem item, final OnItemClickListener listener) {
            final AlbomItem albomItem = item;
            mName.setText(albomItem.name);

            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(20)
                    .borderColor(ContextCompat.getColor(mAlbomPic.getContext(), R.color.destBackground))
                    .borderWidthDp(1)
                    .oval(false)
                    .build();
            Picasso.with(mAlbomPic.getContext()).load(albomItem.imageURI).placeholder(R.drawable.progress_animation)
                    .transform(transformation).into(mAlbomPic);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(albomItem);
                }
            });
        }
    }



}
