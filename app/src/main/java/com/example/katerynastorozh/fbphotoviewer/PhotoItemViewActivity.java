package com.example.katerynastorozh.fbphotoviewer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PhotoItemViewActivity extends AppCompatActivity {

    PagerAdapter adapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item_view);

        viewPager = (ViewPager) findViewById(R.id.view_page);
        adapter = new ScreenSlidePagerAdapter(this, getIntent(). getStringArrayListExtra("data"));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getIntent().getIntExtra("name", 0));


    }


        class ScreenSlidePagerAdapter extends PagerAdapter {

        private Activity _activity;
        private ArrayList<String> _imagePaths;

        public ScreenSlidePagerAdapter(Activity activity, ArrayList<String> images) {
                this._activity = activity;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          _activity = activity;
                this._imagePaths =images;
        }

            @Override
            public int getCount() {
                return _imagePaths.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

                return view == ((FrameLayout) o);

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView((FrameLayout) object);

            }
                @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView;
                Button button;


                final View pageView = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.full_screen_image, container, false);

                if (pageView == null)
                    Toast.makeText(_activity, "NULL", Toast.LENGTH_LONG).show();

                imageView = (ImageView) pageView.findViewById(R.id.photo);
                button = (Button) pageView.findViewById(R.id.close);

                Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(5)
                        .borderColor(ContextCompat.getColor(imageView.getContext(), R.color.destBackground))
                        .borderWidthDp(1)
                        .oval(false)
                        .build();

                Toast.makeText(_activity, "_imagePaths.get(position)" + _imagePaths.get(position).toString(), Toast.LENGTH_LONG).show();
                Picasso.with(_activity).load(_imagePaths.get(position)).transform(transformation).into(imageView);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _activity.finish();
                    }
                });

                //container.addView(viewPager);
                return pageView;
            }
        }


    }


