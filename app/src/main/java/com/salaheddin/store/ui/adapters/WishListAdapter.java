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

public class WishListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int FOOTER = 1;
    private static final int PROGRESS = 100;

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public WishListAdapter(Context context, ArrayList<Object> data, final OnItemClickListener onItemClickListener) {
        mData = data;
        mContext = context;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root) {
                    onItemClickListener.onItemClicked((Product) view.getTag(R.string.product_tag));
                } else if (id == R.id.tv_bag) {
                    onItemClickListener.onBagClicked((Product) view.getTag(R.string.product_tag));
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
            View view = mInflater.inflate(R.layout.item_wish_list, parent, false);
            return new ProductViewHolder(view, mOnClickListener);
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
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductViewHolder)
            ((ProductViewHolder) holder).setData((Product) mData.get(position));
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
        View root;
        private TextView title;
        private TextView oldPrice;
        private TextView newPrice;
        private ImageView photo;
        private TextView bag;
        private RequestOptions requestOptions;

        private ProductViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            root = itemView.findViewById(R.id.root);
            title = (TextView) itemView.findViewById(R.id.tv_title);

            oldPrice = (TextView) itemView.findViewById(R.id.tv_old_price);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            newPrice = (TextView) itemView.findViewById(R.id.tv_new_price);
            photo = (ImageView) itemView.findViewById(R.id.iv_product_image);
            bag = (TextView) itemView.findViewById(R.id.tv_bag);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            root.setOnClickListener(onClickListener);
            bag.setOnClickListener(onClickListener);
        }

        public void setData(Product product) {
            title.setText(product.getName());
            if (product.getItems().size() > 0) {
                if (product.getItems().get(0).getImage().size() > 0) {
                    Glide.with(mContext).load(product.getItems().get(0).getImage().get(0).getPhoto()).apply(requestOptions).into(photo);
                }
                String price = product.getItems().get(0).getPrice();
                oldPrice.setText(price + " " + product.getItems().get(0).getCurrency());
                newPrice.setText(product.getItems().get(0).getNewPrice() + " " + product.getItems().get(0).getCurrency());
            }
            root.setTag(R.string.product_tag, product);
            bag.setTag(R.string.product_tag, product);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Product product);
        void onBagClicked(Product product);
    }
}
