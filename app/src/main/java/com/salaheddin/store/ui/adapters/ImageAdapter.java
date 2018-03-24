package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.Image;
import com.salaheddin.store.ui.activities.PhotoSliderActivity;

import java.util.ArrayList;
import java.util.Random;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Image> images;
    private boolean clickable;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, ArrayList<Image> images, boolean clickable) {
        this.mContext = context;
        this.images = images;
        this.clickable = clickable;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_pager_image, container, false);
        RequestOptions requestOptions;

        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.dontAnimate();
        requestOptions.placeholder(R.drawable.empty_holder);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(mContext).load(images.get(position).getPhoto()).apply(requestOptions).into(imageView);
        if (clickable) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoSliderActivity.class);
                    intent.putExtra("images", images);
                    intent.putExtra("position",position);
                    mContext.startActivity(intent);
                }
            });
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
