package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.SubCategory;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<SubCategory> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnSubCategoryClickListener mOnSubCategoryClickListener;
    private View.OnClickListener mOnClickListener;

    SubCategoryAdapter(Context context, ArrayList<SubCategory> data,OnSubCategoryClickListener onSubCategoryClickListener) {
        mData = data;
        mContext = context;

        mOnSubCategoryClickListener = onSubCategoryClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root){
                    mOnSubCategoryClickListener.onItemClick((String) view.getTag(R.string.subcategory_id_tag),(String) view.getTag(R.string.subcategory_name_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
            View view = mInflater.inflate(R.layout.item_sub_categories, parent, false);
            return new SubCategoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SubCategoryViewHolder) holder).setData(mData.get(position));
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        private View mVRoot;
        private TextView mTvTitle;
        private ImageView mIvPhoto;
        private RequestOptions requestOptions;

        private SubCategoryViewHolder(View itemView) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_sub_category_title);

            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_sub_category_image);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = mIvPhoto.getLayoutParams();
            int itemWidth = Math.round(width / 3);
            params.width = itemWidth;
            params.height = itemWidth;
            mIvPhoto.setLayoutParams(params);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mVRoot.setOnClickListener(mOnClickListener);
        }

        public void setData(SubCategory subCategory) {
            mTvTitle.setText(subCategory.getName());
            Glide.with(mContext).load(subCategory.getImage()).apply(requestOptions).into(mIvPhoto);

            mVRoot.setTag(R.string.subcategory_id_tag,subCategory.getId());
            mVRoot.setTag(R.string.subcategory_name_tag,subCategory.getName());
        }
    }

    public interface OnSubCategoryClickListener{
        void onItemClick(String categoryId,String categoryName);
    }
}
