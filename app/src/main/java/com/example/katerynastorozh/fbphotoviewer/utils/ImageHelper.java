package com.example.katerynastorozh.fbphotoviewer.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ImageHelper {
    Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    public void loadImageToView(String URL, ImageView view)
    {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .borderColor(ContextCompat.getColor(context, R.color.destBackground))
                .borderWidthDp(1)
                .oval(false)
                .build();
        Picasso.with(context).load(URL).placeholder(R.drawable.progress_animation)
                .transform(transformation).into(view);
    }
}
