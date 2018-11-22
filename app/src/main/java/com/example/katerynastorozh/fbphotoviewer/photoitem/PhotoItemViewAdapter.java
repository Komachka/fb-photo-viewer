package com.example.katerynastorozh.fbphotoviewer.photoitem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.example.katerynastorozh.fbphotoviewer.utils.Constants;

import java.util.ArrayList;

import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.PHOTO_URL;

class PhotoItemViewAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> imagePaths;

    public PhotoItemViewAdapter(ArrayList<String> images, FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
        this.imagePaths = images;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }


    @Override
    public Fragment getItem(int i) {
        PhotoItemViewFragment fragment = new PhotoItemViewFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_URL, imagePaths.get(i));
        fragment.setArguments(args);
        return fragment;
    }

}