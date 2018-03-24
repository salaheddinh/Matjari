package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.models.KeyValue;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<KeyValue> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnFilterItemClickListener mOnFilterItemClickListener;
    private View.OnClickListener mOnClickListener;

    public FilterAdapter(Context context, ArrayList<KeyValue> data, OnFilterItemClickListener onFilterItemClickListener) {
        mData = data;
        mContext = context;

        mOnFilterItemClickListener = onFilterItemClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root) {
                    mOnFilterItemClickListener.onItemClick((KeyValue) view.getTag(R.string.item_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_filter, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FilterViewHolder) holder).setData(mData.get(position));
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {

        private View mVRoot;
        private TextView mTvTitle;

        private FilterViewHolder(View itemView) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            mVRoot.setOnClickListener(mOnClickListener);
        }

        public void setData(KeyValue keyValue) {
            mVRoot.setTag(R.string.item_tag, keyValue);
            mTvTitle.setText(keyValue.getName());
        }
    }

    public interface OnFilterItemClickListener {
        void onItemClick(KeyValue value);
    }
}
