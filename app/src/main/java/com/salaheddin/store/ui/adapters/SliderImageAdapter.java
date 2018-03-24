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
import com.salaheddin.store.models.SliderItem;
import com.salaheddin.store.ui.activities.CategoryProductsActivity;
import com.salaheddin.store.ui.activities.PhotoSliderActivity;

import java.util.ArrayList;
import java.util.Random;

public class SliderImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<SliderItem> mImages;
    private boolean clickable;
    private OnSliderItemClicked onSliderItemClicked;

    private LayoutInflater mLayoutInflater;

    SliderImageAdapter(Context context, ArrayList<SliderItem> images, boolean clickable) {
        this.context = context;
        this.mImages = images;
        this.clickable = clickable;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_slider_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        RequestOptions requestOptions;
        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.dontAnimate();
        requestOptions.placeholder(R.drawable.empty_holder);
        Glide.with(context).load(mImages.get(position).getPhoto()).apply(requestOptions).into(imageView);
        if (clickable) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = mImages.get(position).getId();
                    Intent intent = new Intent(context, CategoryProductsActivity.class);
                    intent.putExtra("categoryId", id);
                    intent.putExtra("categoryName", "Offer");
                    intent.putExtra("isType", false);
                    context.startActivity(intent);
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

    public interface OnSliderItemClicked {
        void onSliderImageClicked(ArrayList<Object> items, String url);
    }
}
