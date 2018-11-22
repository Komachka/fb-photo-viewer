package com.example.katerynastorozh.fbphotoviewer.photoitem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PhotoItemViewFragment extends Fragment {

    ImageView imageView;
    TextView closeTV;
    String URl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO change default value
        URl = getArguments().getString(getResources().getString(R.string.PHOTO_URL), "url");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.full_screen_image, container, false);
        imageView = rootView.findViewById(R.id.photo);
        closeTV = rootView.findViewById(R.id.close);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .borderColor(ContextCompat.getColor(imageView.getContext(), R.color.destBackground))
                .borderWidthDp(1)
                .oval(false)
                .build();
        Picasso.with(imageView.getContext()).load(URl).transform(transformation).into(imageView);
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return rootView;

    }
}
