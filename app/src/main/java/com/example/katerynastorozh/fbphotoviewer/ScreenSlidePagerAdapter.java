package com.example.katerynastorozh.fbphotoviewer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> _imagePaths;

    public ScreenSlidePagerAdapter(ArrayList<String> images, FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        this._imagePaths =images;
    }

    @Override
    public int getCount() {
        return _imagePaths.size();
    }


    @Override
    public Fragment getItem(int i) {
        PhotoItemViewFragment fragment = new PhotoItemViewFragment();
        Bundle args = new Bundle();
        args.putString("url", _imagePaths.get(i));
        fragment.setArguments(args);
        return fragment;
    }

}