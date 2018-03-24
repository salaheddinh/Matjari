package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.R;
import com.salaheddin.store.models.Offer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM = 0;
    private final int EMPTY = 1;
    private ArrayList<Offer> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnOfferItemClick mOfferItemClick;
    private View.OnClickListener  mOnClickListener;


    OfferAdapter(Context context, ArrayList<Offer> data, OnOfferItemClick onOfferItemClick) {
        mData = data;
        mContext = context;

        mOfferItemClick = onOfferItemClick;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.root){
                    mOfferItemClick.onItemClick((Offer) view.getTag(R.string.offer_tag));
                }else if(id == R.id.empty_root){
                    mOfferItemClick.onEmptyItemClick();
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
            View view = mInflater.inflate(R.layout.item_offer_product , parent, false);
            return new OfferViewHolder(view,mOnClickListener);
        } else {
            View view = mInflater.inflate(R.layout.item_offer_product_empty, parent, false);
            return new EmptyViewHolder(view,mOnClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return EMPTY;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM)
            ((OfferViewHolder) holder).setData(mData.get(position));
    }


    class OfferViewHolder extends RecyclerView.ViewHolder {

        private View mVRoot;
        private TextView mTvTitle;
        private TextView mTvOldPrice;
        private TextView mTvNewPrice;
        private TextView mTvTime;
        private ImageView mIvPhoto;
        private View mVBuy;
        private RequestOptions requestOptions;
        private CountDownTimer timer;

        private static final String FORMAT = "%02d:%02d:%02d";

        private OfferViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            mVRoot = itemView.findViewById(R.id.root);
            mVBuy = itemView.findViewById(R.id.v_buy);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_offer_title);

            mTvOldPrice = (TextView) itemView.findViewById(R.id.tv_offer_old_price);
            mTvOldPrice.setPaintFlags(mTvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            mTvNewPrice = (TextView) itemView.findViewById(R.id.tv_offer_new_price);
            mIvPhoto = (ImageView) itemView.findViewById(R.id.iv_offer_photo);

            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = mIvPhoto.getLayoutParams();
            int itemWidth = (int) Math.round(width / 2.5);
            params.width = itemWidth;
            params.height = (int) Math.round(itemWidth * (1.5));
            mIvPhoto.setLayoutParams(params);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mVRoot.setOnClickListener(onClickListener);
        }

        public void setData(Offer offer) {

            Glide.with(mContext).load(offer.getImage()).apply(requestOptions).into(mIvPhoto);
            mTvTitle.setText(offer.getName());
            mTvOldPrice.setText(offer.getOfferItemsPrice() + " " + mContext.getString(R.string.currency));
            mTvNewPrice.setText(offer.getOfferPrice() + " " + mContext.getString(R.string.currency));

            double remainigTime = offer.getRemainingTime();

            long milliSeconds = (long) (remainigTime * 1000);
            if (offer.getStatus() == 1){
                mVBuy.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle));
                if (timer == null) {
                    timer = new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

                        public void onTick(long millisUntilFinished) {

                            mTvTime.setText("Buy in " + String.format(FORMAT,
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            mVBuy.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_grey));
                            mTvTime.setText(mContext.getResources().getString(R.string.sold));
                        }
                    }.start();
                }
            }else if (offer.getStatus() == 2){
                mVBuy.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_blue));
                mTvTime.setText(mContext.getResources().getString(R.string.comming_soon));
            }else {
                mVBuy.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_grey));
                mTvTime.setText(mContext.getResources().getString(R.string.sold));
            }

            mVRoot.setTag(R.string.offer_tag,offer);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        private CardView mVRoot;
        private ImageView mVPhoto;

        private EmptyViewHolder(View itemView,View.OnClickListener onClickListener) {
            super(itemView);
            mVRoot = (CardView) itemView.findViewById(R.id.empty_root);
            mVPhoto = (ImageView) itemView.findViewById(R.id.iv_offer_photo);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = mVPhoto.getLayoutParams();
            int itemWidth = (int) Math.round(width / 2.5);
            params.width = itemWidth;
            params.height = (int) Math.round(itemWidth * (1.5));
            mVPhoto.setLayoutParams(params);

            mVRoot.setOnClickListener(onClickListener);
        }
    }

    public interface OnOfferItemClick{
        void onItemClick(Offer offer);
        void onEmptyItemClick();
    }
}
