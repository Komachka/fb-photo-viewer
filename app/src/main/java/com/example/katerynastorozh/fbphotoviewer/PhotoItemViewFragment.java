package com.example.katerynastorozh.fbphotoviewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PhotoItemViewFragment extends Fragment {

    ImageView imageView;
    Button button;
    String URl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        URl = getArguments().getString("url", "url");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.full_screen_image, container, false);
        imageView = rootView.findViewById(R.id.photo);
        button = rootView.findViewById(R.id.close);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .borderColor(ContextCompat.getColor(imageView.getContext(), R.color.destBackground))
                .borderWidthDp(1)
                .oval(false)
                .build();
        Picasso.with(imageView.getContext()).load(URl).transform(transformation).into(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return rootView;

    }
}
