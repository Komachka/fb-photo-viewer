package com.example.katerynastorozh.fbphotoviewer.photoitem;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.katerynastorozh.fbphotoviewer.R;
import com.example.katerynastorozh.fbphotoviewer.utils.ImageHelper;

import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.PHOTOS_DATA;
import static com.example.katerynastorozh.fbphotoviewer.utils.Constants.SLIDER_POSITION;

public class PhotoItemViewActivity extends FragmentActivity {

    PagerAdapter adapter;
    ViewPager viewPager;
    ImageHelper imageHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item_view);

        viewPager = (ViewPager) findViewById(R.id.view_page);
        adapter = new PhotoItemViewAdapter(getIntent(). getStringArrayListExtra(PHOTOS_DATA), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra(SLIDER_POSITION, 0));
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}


