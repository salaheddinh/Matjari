package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.ui.widgets.CircleImageView;

import java.util.ArrayList;

public class ColorForFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<KeyValue> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnColorClickListener mOnColorClickListener;
    private View.OnClickListener mOnClickListener;

    public ColorForFilterAdapter(Context context, ArrayList<KeyValue> data, OnColorClickListener onColorClickListener) {
        mData = data;
        mContext = context;

        mOnColorClickListener = onColorClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root){
                    mOnColorClickListener.onItemClick((KeyValue) view.getTag(R.string.item_tag));
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
        private TextView mTvText;
        private CircleImageView mIvPhoto;
        private RequestOptions requestOptions;

        private ColorViewHolder(View itemView) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);

            mIvPhoto = (CircleImageView) itemView.findViewById(R.id.iv_item_color);
            mTvText = (TextView) itemView.findViewById(R.id.tv_title);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mVRoot.setOnClickListener(mOnClickListener);
        }

        public void setData(KeyValue keyValue) {
            if (keyValue.getId().equals("-1")){
                mTvText.setVisibility(View.VISIBLE);
                mIvPhoto.setVisibility(View.GONE);
            }else {
                mTvText.setVisibility(View.GONE);
                mIvPhoto.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(keyValue.getName()).apply(requestOptions).into(mIvPhoto);
            }

            mVRoot.setTag(R.string.item_tag,keyValue);
        }
    }

    public interface OnColorClickListener{
        void onItemClick(KeyValue keyValue);
    }
}
