package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.models.DictionaryItem;

import java.util.ArrayList;

public class DictionaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DictionaryItem> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnSearchItemClickListener mOnFilterItemClickListener;
    private View.OnClickListener mOnClickListener;

    public DictionaryAdapter(Context context, ArrayList<DictionaryItem> data, OnSearchItemClickListener onFilterItemClickListener) {
        mData = data;
        mContext = context;

        mOnFilterItemClickListener = onFilterItemClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root) {
                    DictionaryItem item = (DictionaryItem) view.getTag(R.string.item_tag);
                    int pos = (int) view.getTag(R.string.position_tag);
                    mData.get(pos).setSelected(true);
                    mOnFilterItemClickListener.onItemClick(item, pos);
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_new_dictionary, parent, false);
        return new DictionaryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DictionaryViewHolder) holder).setData(mData.get(position), position);
    }

    class DictionaryViewHolder extends RecyclerView.ViewHolder {

        private View mVRoot;
        private TextView mTvTitle;
        private TextView mTvType;

        private DictionaryViewHolder(View itemView) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvType = (TextView) itemView.findViewById(R.id.tv_type);

            mVRoot.setOnClickListener(mOnClickListener);
        }

        public void setData(DictionaryItem item, int position) {
            mVRoot.setTag(R.string.item_tag, item);
            mVRoot.setTag(R.string.position_tag, position);

            mTvTitle.setText(item.getNameEn());
            switch (item.getType()) {
                case 1:
                    mTvType.setText("categories");
                    break;
                case 2:
                    mTvType.setText("products");
                    break;
            }
        }
    }

    public interface OnSearchItemClickListener {
        void onItemClick(DictionaryItem item, int position);
    }
}
