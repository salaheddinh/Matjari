package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.models.Category;
import com.salaheddin.store.models.SubCategory;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int CATEGORY = 0;
    private final int SUB_CATEGORIES = 1;

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private SubCategoryAdapter.OnSubCategoryClickListener mOnSubCategoryClickListener;

    public CategoryAdapter(Context context,ArrayList<Object> data,SubCategoryAdapter.OnSubCategoryClickListener onSubCategoryClickListener) {
        mData = data;
        mContext = context;
        mOnSubCategoryClickListener = onSubCategoryClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == SUB_CATEGORIES) {
            View view = mInflater.inflate(R.layout.item_horizental_recyclerview, parent, false);
            return new SubCategoryViewHolder(view);
        }else{
            View view = mInflater.inflate(R.layout.item_categories_header, parent, false);
            return new CategoryViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof Category){
            return CATEGORY;
        }else {
            return SUB_CATEGORIES;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == SUB_CATEGORIES)
            ((SubCategoryViewHolder) holder).setData((ArrayList<SubCategory>) mData.get(position));
        else
            ((CategoryViewHolder) holder).setData((Category) mData.get(position));
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRvList;
        private SubCategoryAdapter mAdapter;
        private GridLayoutManager mLayoutManager;

        private SubCategoryViewHolder(View itemView) {
            super(itemView);

            mRvList = (RecyclerView) itemView.findViewById(R.id.rv_category_list);
            mLayoutManager = new GridLayoutManager(mContext,3);
            mRvList.setLayoutManager(mLayoutManager);
        }

        public void setData(ArrayList<SubCategory> subCategories) {
            mAdapter = new SubCategoryAdapter(mContext, subCategories,mOnSubCategoryClickListener);
            mRvList.setAdapter(mAdapter);
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;

        private CategoryViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_category_title);
        }

        public void setData(Category category) {
            mTvTitle.setText(category.getName());
        }
    }
}
