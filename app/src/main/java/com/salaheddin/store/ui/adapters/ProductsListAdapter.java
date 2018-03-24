package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
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

public class ProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Product> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnProductItemClicked mOnProductItemClicked;
    private View.OnClickListener mOnClickListener;

    public ProductsListAdapter(Context context, ArrayList<Product> data, OnProductItemClicked onProductItemClicked) {
        mData = data;
        mContext = context;
        mOnProductItemClicked = onProductItemClicked;

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.root) {
                    mOnProductItemClicked.onItemClicked((Product) v.getTag(R.string.product_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mInflater.inflate(R.layout.item_product_offer_details, parent, false);
        return new ProductViewHolder(view, mOnClickListener);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductViewHolder)
            ((ProductViewHolder) holder).setData(mData.get(position));
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private View mVRoot;
        private TextView mTvTitle;
        private TextView mTvOldPrice;
        private TextView mTvNewPrice;
        private ImageView mIvPhoto;
        private RequestOptions requestOptions;

        private ProductViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);

            mTvOldPrice = (TextView) itemView.findViewById(R.id.tv_old_price);
            mTvOldPrice.setPaintFlags(mTvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            mTvNewPrice = (TextView) itemView.findViewById(R.id.tv_new_price);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_product_image);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);
            mVRoot.setOnClickListener(onClickListener);
        }

        public void setData(Product product) {
            mTvTitle.setText(product.getName());
            if (product.getItems().size() > 0) {
                if (product.getItems().get(0).getImage().size() > 0) {
                    Glide.with(mContext).load(product.getItems().get(0).getImage().get(0).getPhoto()).apply(requestOptions).into(mIvPhoto);
                }
                String price = product.getItems().get(0).getPrice() ;
                mTvOldPrice.setText(price + " " + product.getItems().get(0).getCurrency());
                mTvNewPrice.setText(product.getItems().get(0).getNewPrice() + " " + product.getItems().get(0).getCurrency());
            }
            mVRoot.setTag(R.string.product_tag, product);
        }
    }

    public interface OnProductItemClicked {
        void onItemClicked(Product product);
    }
}
