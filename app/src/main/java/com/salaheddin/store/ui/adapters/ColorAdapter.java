package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.Item;
import com.salaheddin.store.ui.widgets.CircleImageView;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Item> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnColorClickListener mOnColorClickListener;
    private View.OnClickListener mOnClickListener;

    public ColorAdapter(Context context, ArrayList<Item> data, OnColorClickListener onColorClickListener) {
        mData = data;
        mContext = context;

        mOnColorClickListener = onColorClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root){
                    mOnColorClickListener.onItemClick((Item) view.getTag(R.string.item_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
            View view = mInflater.inflate(R.layout.item_color, parent, false);
            return new ColorViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ColorViewHolder) holder).setData(mData.get(position));
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {

        private View mVRoot;
        private CircleImageView mIvPhoto;
        private CircleImageView mIvPhotoSelected;
        private RequestOptions requestOptions;

        private ColorViewHolder(View itemView) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);

            mIvPhoto = (CircleImageView) itemView.findViewById(R.id.iv_item_color);
            mIvPhotoSelected = (CircleImageView) itemView.findViewById(R.id.iv_item_color_selected);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mVRoot.setOnClickListener(mOnClickListener);
        }

        public void setData(Item item) {
            Glide.with(mContext).load(item.getColor().getPhoto()).apply(requestOptions).into(mIvPhoto);
            if (item.isSelected()){
                mIvPhotoSelected.setVisibility(View.VISIBLE);
                mVRoot.setEnabled(false);
            }else {
                mIvPhotoSelected.setVisibility(View.GONE);
                mVRoot.setEnabled(true);
            }

            mVRoot.setTag(R.string.item_tag,item);
        }
    }

    public interface OnColorClickListener{
        void onItemClick(Item item);
    }
}
