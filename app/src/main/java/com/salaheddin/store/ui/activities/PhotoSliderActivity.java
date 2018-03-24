package com.salaheddin.store.ui.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.salaheddin.store.R;
import com.salaheddin.store.models.Image;
import com.salaheddin.store.ui.adapters.ImageAdapter;

import java.util.ArrayList;

public class PhotoSliderActivity extends AppCompatActivity {

    private ViewPager mImagesPager;
    private ArrayList<Image> images;
    private ImageView mBackArrow;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slider);
        images = (ArrayList<Image>) getIntent().getSerializableExtra("images");
        position = getIntent().getIntExtra("position",position);
        init();
    }

    void init() {
        mImagesPager = (ViewPager) findViewById(R.id.vp_images);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mImagesPager, true);

        mBackArrow = (ImageView) findViewById(R.id.iv_back_arrow);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        mBackArrow.setImageDrawable(upArrow);

        ImageAdapter adapter = new ImageAdapter(this, images, false);
        mImagesPager.setAdapter(adapter);
        mImagesPager.setCurrentItem(position);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
