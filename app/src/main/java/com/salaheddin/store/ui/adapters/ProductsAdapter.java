package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.ListWithType;
import com.salaheddin.store.models.Product;

import java.util.ArrayList;
import java.util.Random;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private static final int PROGRESS = 100;

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    private OnProductItemClicked mOnProductItemClicked;
    private View.OnClickListener mOnClickListener;

    public ProductsAdapter(Context context, ArrayList<Object> data,OnProductItemClicked onProductItemClicked) {
        mData = data;
        mContext = context;
        mOnProductItemClicked = onProductItemClicked;

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.root){
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
        if (viewType == ITEM) {
            View view = mInflater.inflate(R.layout.item_product3, parent, false);
            return new ProductViewHolder(view,mOnClickListener);
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

    public void addData(ArrayList<Product> data) {
        if (mData.get(mData.size() - 1) instanceof ListWithType) {
            ArrayList<Object> objects = (((ListWithType) mData.get(mData.size() - 1)).getObjects());
            objects.addAll(data);
        }else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
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

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof Product) {
            return ITEM;
        } else {
            return FOOTER;
        }
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

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = mIvPhoto.getLayoutParams();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(params);
            mIvPhoto.setLayoutParams(lp);
            int itemWidth = Math.round(width / 2);
            params.width = itemWidth;
            params.height = (int) Math.round(itemWidth * 1.5);
            mIvPhoto.setLayoutParams(params);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mVRoot.setOnClickListener(onClickListener);
        }

        public void setData(Object object) {
            Product product = (Product) object;
            mTvTitle.setText(product.getName());
            if (product.getItems().size() > 0) {
                if (product.getItems().get(0).getImage().size() > 0) {
                    Glide.with(mContext).load(product.getItems().get(0).getImage().get(0).getPhoto()).apply(requestOptions).into(mIvPhoto);
                }
                String price = product.getItems().get(0).getPrice();
                mTvOldPrice.setText(price + " " + product.getItems().get(0).getCurrency());
                mTvNewPrice.setText(product.getItems().get(0).getNewPrice() + " " + product.getItems().get(0).getCurrency());
            }
            mVRoot.setTag(R.string.product_tag,product);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnProductItemClicked{
        void onItemClicked(Product product);
    }
}
