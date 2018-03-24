package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Product> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private OnCartItemClickListener mOnCartItemClickListener;
    private String currency;

    public CartAdapter(Context context, ArrayList<Product> data,
                       final OnCartItemClickListener onCartItemClickListener, String currency) {
        this.currency = currency;
        mData = data;
        mContext = context;
        mOnCartItemClickListener = onCartItemClickListener;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root) {
                    onCartItemClickListener.onItemClicked((Product) view.getTag(R.string.product_tag));
                } else if (id == R.id.iv_increase) {
                    onCartItemClickListener.onIncreaseClicked((Product) view.getTag(R.string.product_tag));
                } else if (id == R.id.iv_decrease) {
                    onCartItemClickListener.onDecreaseClicked((Product) view.getTag(R.string.product_tag));
                } else if (id == R.id.iv_remove) {
                    onCartItemClickListener.onRemoveClicked((Product) view.getTag(R.string.product_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CartViewHolder) holder).setData(mData.get(position), mOnCartItemClickListener);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTitle;
        private TextView mTvPrice;
        private TextView mTvQuantity;
        private ImageView mIvIncrease;
        private ImageView mIvDecrease;
        private ImageView mIvPhoto;
        private ImageView mIvRemove;
        private View mVRoot;
        private View mVNoEnoughItems;
        private RequestOptions requestOptions;

        private CartViewHolder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_product_name);
            mTvPrice = (TextView) itemView.findViewById(R.id.tv_product_price);
            mTvQuantity = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_product_image);
            mIvRemove = (ImageView) itemView.findViewById(R.id.iv_remove);
            mIvIncrease = (ImageView) itemView.findViewById(R.id.iv_increase);
            mIvDecrease = (ImageView) itemView.findViewById(R.id.iv_decrease);
            mVRoot = itemView.findViewById(R.id.root);
            mVNoEnoughItems = itemView.findViewById(R.id.v_no_enough_items);

            requestOptions = new RequestOptions();
            requestOptions.fitCenter();

            mVRoot.setOnClickListener(mOnClickListener);
            mIvIncrease.setOnClickListener(mOnClickListener);
            mIvDecrease.setOnClickListener(mOnClickListener);
            mIvRemove.setOnClickListener(mOnClickListener);
        }

        public void setData(Product product, OnCartItemClickListener mOnCartItemClickListener) {
            if (product.getItems().get(0).getQuantity() < Integer.parseInt(product.getItemsCount())) {
                mVNoEnoughItems.setVisibility(View.VISIBLE);
                mOnCartItemClickListener.onNoEnoughItems(false);
            } else {
                mVNoEnoughItems.setVisibility(View.GONE);
                mOnCartItemClickListener.onNoEnoughItems(true);
            }
            mTvTitle.setText(product.getName());
            mTvPrice.setText(product.getItems().get(0).getNewPrice() + " " + currency);
            mTvQuantity.setText(product.getItemsCount() + "");
            String imageUrl = product.getItems().get(0).getImage().get(0).getPhoto();
            Glide.with(mContext).load(imageUrl).apply(requestOptions).into(mIvPhoto);

            int itemCount = Integer.parseInt(product.getItemsCount());
            if (itemCount > 1) {
                mIvDecrease.setEnabled(true);
            } else {
                mIvDecrease.setEnabled(false);
            }

            mVRoot.setTag(R.string.product_tag, product);
            mIvIncrease.setTag(R.string.product_tag, product);
            mIvDecrease.setTag(R.string.product_tag, product);
            mIvRemove.setTag(R.string.product_tag, product);
        }
    }

    public interface OnCartItemClickListener {
        void onItemClicked(Product product);

        void onIncreaseClicked(Product product);

        void onDecreaseClicked(Product product);

        void onRemoveClicked(Product product);

        void onNoEnoughItems(Boolean isEnough);
    }
}
