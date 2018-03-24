package com.salaheddin.store.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListSpacingItemDecoration;
import com.salaheddin.store.helpers.ListsTypes;
import com.salaheddin.store.R;
import com.salaheddin.store.models.ListWithType;
import com.salaheddin.store.models.MainType;
import com.salaheddin.store.models.Offer;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.models.SliderItem;
import com.salaheddin.store.models.Type;
import com.salaheddin.store.ui.activities.SearchActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int OFFER = 0;
    private final int TITLE = 1;
    private final int HELP_ITEM = 2;
    private final int ITEM = 3;
    private final int TWO_CATEGORIES = 4;
    private final int THREE_CATEGORY = 5;
    private final int SLIDER = 6;
    private final int FOOTER = 7;
    private final int PROGRESS = 100;

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OfferAdapter.OnOfferItemClick mOfferItemClick;
    private ProductsAdapter.OnProductItemClicked mOnProductItemClicked;
    private OnTypeItemClicked mOnTypeItemClicked;
    private View.OnClickListener mOnClickListener;

    public HomeAdapter(Context context, ArrayList<Object> data, OfferAdapter.OnOfferItemClick onOfferItemClick
            , ProductsAdapter.OnProductItemClicked onProductItemClicked, OnTypeItemClicked onTypeItemClicked) {
        mData = data;
        mContext = context;

        mOfferItemClick = onOfferItemClick;
        mOnProductItemClicked = onProductItemClicked;
        mOnTypeItemClicked = onTypeItemClicked;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.imageView1:
                    case R.id.imageView2:
                    case R.id.imageView3:
                        mOnTypeItemClicked.onTypeClicked((Type) v.getTag(R.string.type_tag));
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == OFFER) {
            View view = mInflater.inflate(R.layout.item_horizental_recyclerview, parent, false);
            return new OffersViewHolder(view);
        } else if (viewType == TITLE) {
            View view = mInflater.inflate(R.layout.item_title, parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == ITEM) {
            View view = mInflater.inflate(R.layout.item_horizental_recyclerview, parent, false);
            return new HomeAdapter.ItemViewHolder(view);
        } else if (viewType == THREE_CATEGORY) {
            View view = mInflater.inflate(R.layout.item_three_categories, parent, false);
            return new ThreeCategoriesViewHolder(view);
        } else if (viewType == TWO_CATEGORIES) {
            View view = mInflater.inflate(R.layout.item_two_categories, parent, false);
            return new TwoCategoriesViewHolder(view);
        } else if (viewType == SLIDER) {
            View view = mInflater.inflate(R.layout.item_slider, parent, false);
            return new HomeAdapter.SliderViewHolder(view);
        } else if (viewType == HELP_ITEM) {
            View view = mInflater.inflate(R.layout.item_help_in_home, parent, false);
            return new HomeAdapter.HelpViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_progress, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof ListWithType) {
            ListWithType list = (ListWithType) mData.get(position);
            if (list.getType() == ListsTypes.OFFERS) {
                return OFFER;
            } else if (list.getType() == ListsTypes.CATEGORIES) {
                if (list.getObjects().size() == 3) {
                    return THREE_CATEGORY;
                } else {
                    return TWO_CATEGORIES;
                }
            } else if (list.getType() == ListsTypes.PRODUCTS) {
                return ITEM;
            } else if (list.getType() == ListsTypes.SLIDER) {
                return SLIDER;
            } else {
                return TITLE;
            }
        } else if (mData.get(position) instanceof MainType) {
            return TITLE;
        } else if (mData.get(position) instanceof String) {
            return HELP_ITEM;
        } else {
            return FOOTER;
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
        ArrayList<Object> objects = (((ListWithType) mData.get(mData.size() - 1)).getObjects());
        objects.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            ((HomeAdapter.TitleViewHolder) holder).setData((MainType) mData.get(position));
        } else if (holder instanceof TwoCategoriesViewHolder) {
            ((TwoCategoriesViewHolder) holder).setData(((ListWithType) mData.get(position)).getObjects());
        } else if (holder instanceof ThreeCategoriesViewHolder) {
            ((ThreeCategoriesViewHolder) holder).setData(((ListWithType) mData.get(position)).getObjects());
        } else if (holder instanceof ItemViewHolder) {
            ((HomeAdapter.ItemViewHolder) holder).setData(((ListWithType) mData.get(position)).getObjects());
        }
    }

    class OffersViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRvList;
        private OfferAdapter mAdapter;
        private LinearLayoutManager mLayoutManager;

        private OffersViewHolder(View itemView) {
            super(itemView);

            mRvList = (RecyclerView) itemView.findViewById(R.id.rv_category_list);
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mRvList.setLayoutManager(mLayoutManager);
            mRvList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view,
                                           RecyclerView parent, RecyclerView.State state) {
                    int position = parent.getChildPosition(view);
                    if (position == 0) {
                        outRect.left = 1;
                    } else {
                        outRect.left = 16;
                    }
                }
            });
            setData(((ListWithType) mData.get(2)).getObjects());
        }

        public void setData(ArrayList<Object> objects) {
            ArrayList<Offer> offers = new ArrayList<>();
            for (int i = 0; i < objects.size(); i++) {
                offers.add((Offer) objects.get(i));
            }
            mAdapter = new OfferAdapter(mContext, offers, mOfferItemClick);
            mRvList.setAdapter(mAdapter);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;
        private ImageView mTvImage;

        private TitleViewHolder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvImage = (ImageView) itemView.findViewById(R.id.iv_title);
        }

        public void setData(MainType type) {
            mTvTitle.setText(type.getTitle());
            mTvImage.setImageDrawable(mContext.getResources().getDrawable(type.getPhoto()));
        }
    }

    class HelpViewHolder extends RecyclerView.ViewHolder {

        private HelpViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView mRvList;
        private ProductsAdapter mAdapter;
        private GridLayoutManager mLayoutManager;

        private ItemViewHolder(View itemView) {
            super(itemView);
            mRvList = (RecyclerView) itemView.findViewById(R.id.rv_category_list);
            mLayoutManager = new GridLayoutManager(mContext, 2);
            mRvList.setLayoutManager(mLayoutManager);

            int spacingInPixels = mContext.getResources().getDimensionPixelSize(R.dimen.home_list_spacing);
            mRvList.addItemDecoration(new ListSpacingItemDecoration(spacingInPixels, mLayoutManager.getSpanCount()));
        }

        public void setData(ArrayList<Object> objects) {
            mAdapter = new ProductsAdapter(mContext, objects, mOnProductItemClicked);
            mRvList.setAdapter(mAdapter);
        }
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private final long DELAY_MS = 500;
        private final long PERIOD_MS = 3000;
        private ViewPager mImagesPager;
        private TabLayout mTlTabLayout;
        private ArrayList<SliderItem> images;
        private SliderImageAdapter mAdapter;
        private int currentPage = 0;
        private Timer timer;
        private View searchView;

        private SliderViewHolder(View itemView) {
            super(itemView);
            mImagesPager = (ViewPager) itemView.findViewById(R.id.vp_images);
            ViewGroup.LayoutParams params = mImagesPager.getLayoutParams();
            Drawable image = mContext.getResources().getDrawable(R.drawable._001_2);
            params.height = (image.getIntrinsicHeight());
            mImagesPager.setLayoutParams(params);

            images = new ArrayList<>();
            ArrayList<Object> items = ((ListWithType) mData.get(0)).getObjects();
            for (Object item : items) {
                images.add((SliderItem) item);
            }
            mAdapter = new SliderImageAdapter(mContext, images, true);
            mImagesPager.setAdapter(mAdapter);

            mTlTabLayout = (TabLayout) itemView.findViewById(R.id.tab_layout);
            mTlTabLayout.setupWithViewPager(mImagesPager, true);

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == images.size()) {
                        currentPage = 0;
                    }
                    mImagesPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer .schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);

            searchView = itemView.findViewById(R.id.search_view);
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, SearchActivity.class));
                }
            });
        }
    }

    class ThreeCategoriesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvFirstImage;
        private ImageView mIvSecondImage;
        private ImageView mIvThirdImage;
        private RequestOptions requestOptions;

        private ThreeCategoriesViewHolder(View itemView) {
            super(itemView);

            mIvFirstImage = (ImageView) itemView.findViewById(R.id.imageView1);
            mIvSecondImage = (ImageView) itemView.findViewById(R.id.imageView2);
            mIvThirdImage = (ImageView) itemView.findViewById(R.id.imageView3);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams params = mIvFirstImage.getLayoutParams();
            params.width =  Math.round(width / 2);
            params.height = (int) Math.round((width / 2) * (0.7));
            mIvFirstImage.setLayoutParams(params);

            params = mIvSecondImage.getLayoutParams();
            params.width =  Math.round(width / 2);
            params.height = (int) Math.round((width / 2) * (0.7));
            mIvSecondImage.setLayoutParams(params);

            params = mIvThirdImage.getLayoutParams();
            params.height = (int) (Math.round((width / 2) * (0.7) * 2));
            mIvThirdImage.setLayoutParams(params);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mIvFirstImage.setOnClickListener(mOnClickListener);
            mIvSecondImage.setOnClickListener(mOnClickListener);
            mIvThirdImage.setOnClickListener(mOnClickListener);
        }

        void setData(ArrayList<Object> categories) {
            if (categories.size() >= 3) {
                String s1 = ((Type) categories.get(0)).getPhoto(AppConstants.medium);
                String s2 = ((Type) categories.get(1)).getPhoto(AppConstants.medium);
                String s3 = ((Type) categories.get(2)).getPhoto(AppConstants.medium);

                Glide.with(mContext).load(s3).apply(requestOptions).into(mIvFirstImage);
                Glide.with(mContext).load(s2).apply(requestOptions).into(mIvSecondImage);
                Glide.with(mContext).load(s1).apply(requestOptions).into(mIvThirdImage);

                mIvFirstImage.setTag(R.string.type_tag, categories.get(2));
                mIvSecondImage.setTag(R.string.type_tag, categories.get(1));
                mIvThirdImage.setTag(R.string.type_tag, categories.get(0));
            }
        }
    }

    class TwoCategoriesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvFirstImage;
        private ImageView mIvSecondImage;
        private RequestOptions requestOptions;

        private TwoCategoriesViewHolder(View itemView) {
            super(itemView);

            mIvFirstImage = (ImageView) itemView.findViewById(R.id.imageView1);
            mIvSecondImage = (ImageView) itemView.findViewById(R.id.imageView2);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams params = mIvFirstImage.getLayoutParams();
            params.height = (int) Math.round((width / 2) * (0.7));
            mIvFirstImage.setLayoutParams(params);

            params = mIvSecondImage.getLayoutParams();
            params.height = (int) Math.round((width / 2) * (0.7));
            mIvSecondImage.setLayoutParams(params);

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.dontAnimate();
            requestOptions.placeholder(R.drawable.empty_holder);

            mIvFirstImage.setOnClickListener(mOnClickListener);
            mIvSecondImage.setOnClickListener(mOnClickListener);
        }

        void setData(ArrayList<Object> categories) {
            if (categories.size() >= 2) {
                String s1 = ((Type) categories.get(0)).getPhoto(AppConstants.medium);
                String s2 = ((Type) categories.get(1)).getPhoto(AppConstants.medium);

                Glide.with(mContext).load(s1).apply(requestOptions).into(mIvFirstImage);
                Glide.with(mContext).load(s2).apply(requestOptions).into(mIvSecondImage);

                mIvFirstImage.setTag(R.string.type_tag, categories.get(0));
                mIvSecondImage.setTag(R.string.type_tag, categories.get(1));
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnTypeItemClicked {
        void onTypeClicked(Type type);

        void onSliderItemClicked(ArrayList<Object> items, String url);
    }
}
