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
import com.example.katerynastorozh.fbphotoviewer.utils.ImageHelper;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.PHOTO_URL;

public class PhotoItemViewFragment extends Fragment {

    private ImageView imageView;
    private TextView closeTV;
    private String URl;
    private ImageHelper imageHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO change default value
        URl = getArguments().getString(PHOTO_URL, "url");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.full_screen_image, container, false);
        imageView = rootView.findViewById(R.id.photo);
        closeTV = rootView.findViewById(R.id.close);
        imageHelper = new ImageHelper(getContext());
        imageHelper.loadImageToView(URl, imageView);
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imageHelper = null;
    }
}
