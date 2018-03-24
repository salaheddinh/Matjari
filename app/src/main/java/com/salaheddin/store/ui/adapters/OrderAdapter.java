package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.models.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private static final int PROGRESS = 100;

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View.OnClickListener onClickListener;

    public OrderAdapter(Context context, ArrayList<Object> data, final OnItemClickListener onItemClickListener) {
        mData = data;
        mContext = context;
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root) {
                    onItemClickListener.onItemClicked((Order) view.getTag(R.string.product_tag));
                }else if (id == R.id.iv_cancel_order) {
                    onItemClickListener.onCancelOrderClicked((Order) view.getTag(R.string.product_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == ITEM) {
            View view = mInflater.inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view, onClickListener);
        } else {
            View view = mInflater.inflate(R.layout.item_progress, parent, false);
            return new FooterViewHolder(view);
        }
    }

    public void addBottomProgress() {
        if (!mData.contains(PROGRESS)) {
            mData.add(PROGRESS);
            notifyItemInserted(mData.size() - 1);
        }
    }

    public void removeBottomProgress() {
        if (mData.contains(PROGRESS)) {
            mData.remove(mData.size() - 1);
            notifyItemRemoved(mData.size());
        }
    }

    public void addData(ArrayList<Order> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderViewHolder)
            ((OrderViewHolder) holder).setData((Order) mData.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof Order) {
            return ITEM;
        } else {
            return FOOTER;
        }
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private View mVRoot;
        private TextView mTvDate;
        private TextView mTvPrice;
        private ImageView mIvCancelOrder;

        private OrderViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_order_date);
            mTvPrice = (TextView) itemView.findViewById(R.id.tv_order_price);
            mIvCancelOrder = (ImageView) itemView.findViewById(R.id.iv_cancel_order);

            mVRoot.setOnClickListener(onClickListener);
            mIvCancelOrder.setOnClickListener(onClickListener);
        }

        public void setData(Order order) {
            mTvDate.setText(order.getOrderDate());
            mTvPrice.setText(order.getPrice() + order.getCurrency());
            mVRoot.setTag(R.string.product_tag, order);
            mIvCancelOrder.setTag(R.string.product_tag, order);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Order order);
        void onCancelOrderClicked(Order order);
    }
}
