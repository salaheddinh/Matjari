package com.salaheddin.store.ui.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListSpacingItemDecoration;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewAnimationUtils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Color;
import com.salaheddin.store.models.Image;
import com.salaheddin.store.models.Item;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.ColorAdapter;
import com.salaheddin.store.ui.adapters.FilterAdapter;
import com.salaheddin.store.ui.adapters.ImageAdapter;
import com.salaheddin.store.ui.adapters.ProductsAdapter;
import com.salaheddin.store.ui.widgets.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener,
        ProductsAdapter.OnProductItemClicked, ColorAdapter.OnColorClickListener {

    private final String TAG = "ProductDetailsActivity";
    private final String ADD_TO_CART_TAG = "ADD_TO_CART_TAG";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private View mAllDescription;

    private View mAllDescriptionDetails;
    private int mAllDescriptionDetailsHeight;
    private ImageView mIvExpandCollapseDescIcon;
    private TextView mTvSeason;
    private TextView mTvBrand;
    private ImageView mIvBrand;

    private TextView mTvCategory;
    private ImageView mIvCategory;

    private TextView mTvStore;
    private ImageView mIvStore;

    private View mAllShippingInfo;
    private View mAllShippingInfoDetails;
    private int mAllShippingInfoDetailsHeight;
    private ImageView mIvExpandCollapseShippingIcon;
    private TextView mTvCanBeShipped;
    private TextView mTvShippingFees;
    private TextView mTvPhoneNumber;

    private ViewPager mImagesPager;
    private CircleImageView mItemColor;
    private View mVColor;
    private View mVSize;
    private TextView mTvItemDescription;
    private TextView mTvItemShortDescription;
    private TextView mTvItemTitle;

    private TextView mTvPrice;
    private TextView mTvNewPrice;
    private TextView mTvAddToCart;
    private TextView mTvAlsoLike;
    private TextView mTvSize;
    private TextView mTvSale;

    private Toolbar mToolbar;
    private RequestOptions requestOptions;

    private RecyclerView mRvList;
    private ArrayList<Color> colors;
    private ColorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProductsAdapter mProductAdapter;
    private GridLayoutManager mGridLayoutManager;

    private Product mProduct;
    private boolean reload;
    private Item mItem;
    private AlertDialog colorDialog;
    private ImageView mIvAddToWishList;
    private ImageView mIvAddShare;
    private ImageView mIvCart;
    private TextView mTvBadge;

    private AlertDialog dialog;
    private KeyValue size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_new);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(R.drawable.empty_holder);

        mProduct = (Product) getIntent().getSerializableExtra("product");
        reload = getIntent().getBooleanExtra("reload", true);
        mItem = (Item) getIntent().getSerializableExtra("item");

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        init();
        if (reload)
            loadData(true);
        else
            showData(mProduct, mItem);
    }

    void init() {
        mTvBadge = (TextView) findViewById(R.id.notification_badge);
        String size = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.CART_SIZE, "0");
        if (Integer.parseInt(size) > 0) {
            mTvBadge.setVisibility(View.VISIBLE);
            mTvBadge.setText(size);
        }
        else {
            mTvBadge.setVisibility(View.GONE);
        }

        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mAllDescription = findViewById(R.id.v_all_description);
        mAllDescriptionDetails = findViewById(R.id.v_all_description_details);
        mAllDescriptionDetailsHeight = mAllDescriptionDetails.getLayoutParams().height;
        mIvExpandCollapseDescIcon = (ImageView) findViewById(R.id.tv_desc_collapse);
        mTvSeason = (TextView) findViewById(R.id.tv_season);
        mTvBrand = (TextView) findViewById(R.id.tv_brand);
        mIvBrand = (ImageView) findViewById(R.id.iv_brand);

        mTvCategory = (TextView) findViewById(R.id.tv_category);
        mIvCategory = (ImageView) findViewById(R.id.iv_category);
        mTvStore = (TextView) findViewById(R.id.tv_store);
        mIvStore = (ImageView) findViewById(R.id.iv_store);

        mImagesPager = (ViewPager) findViewById(R.id.vp_images);
        mItemColor = (CircleImageView) findViewById(R.id.iv_item_color);
        mVColor = findViewById(R.id.v_color);
        mVSize = findViewById(R.id.v_size);
        mTvItemDescription = (TextView) findViewById(R.id.tv_item_description);
        mTvItemShortDescription = (TextView) findViewById(R.id.tv_short_desc);
        mTvItemTitle = (TextView) findViewById(R.id.tv_item_title);
        mTvCanBeShipped = (TextView) findViewById(R.id.tv_item_shipped);
        mTvShippingFees = (TextView) findViewById(R.id.tv_item_shipping_fee);
        mTvPhoneNumber = (TextView) findViewById(R.id.tv_store_phone_number);

        mAllShippingInfo = findViewById(R.id.v_all_shipping_info);
        mAllShippingInfoDetails = findViewById(R.id.v_all_shipping_details);
        mAllShippingInfoDetailsHeight = mAllShippingInfoDetails.getLayoutParams().height;
        mIvExpandCollapseShippingIcon = (ImageView) findViewById(R.id.tv_shipping_collapse);

        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvPrice.setPaintFlags(mTvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mTvNewPrice = (TextView) findViewById(R.id.tv_new_price);
        mTvAddToCart = (TextView) findViewById(R.id.tv_add_to_cart);
        mTvAlsoLike = (TextView) findViewById(R.id.tv_also_like);
        mTvSize = (TextView) findViewById(R.id.tv_size);
        mTvSale = (TextView) findViewById(R.id.tv_sale);

        mIvAddToWishList = (ImageView) findViewById(R.id.iv_add_to_wishlist);
        mIvAddShare = (ImageView) findViewById(R.id.iv_share);
        mIvCart = (ImageView) findViewById(R.id.cart);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRvList.setLayoutManager(mGridLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_list_spacing);
        mRvList.addItemDecoration(new ListSpacingItemDecoration(spacingInPixels, mGridLayoutManager.getSpanCount()));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        ViewGroup.LayoutParams params = mImagesPager.getLayoutParams();
        params.width = width;
        params.height = (int) (height * 0.8);
        mImagesPager.setLayoutParams(params);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mImagesPager, true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        mBtnErrorAction.setOnClickListener(this);
        mAllDescription.setOnClickListener(this);
        mAllShippingInfo.setOnClickListener(this);
        mItemColor.setOnClickListener(this);
        mVColor.setOnClickListener(this);
        mTvAddToCart.setOnClickListener(this);
        mIvAddToWishList.setOnClickListener(this);
        mVSize.setOnClickListener(this);
        mIvCart.setOnClickListener(this);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getProductDetailsUrl(),
                this,
                WebURLParams.getProductDetailsParams(session, mProduct.getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    private void loadAddToCart() {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getAddToCartUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        String size = SharedPreferencesManager.readFromPreferences(ProductDetailsActivity.this, SharedPreferencesManager.CART_SIZE, "0");
                        int sizeInt = Integer.parseInt(size);
                        sizeInt++;
                        SharedPreferencesManager.saveToPreferences(ProductDetailsActivity.this, SharedPreferencesManager.CART_SIZE, String.valueOf(sizeInt));
                        Utils.makeToast(ProductDetailsActivity.this, mProduct.getName() + " " + getString(R.string.successfully_added_to_the_bag));
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(ProductDetailsActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(ProductDetailsActivity.this, getString(R.string.error_connection));
                    }
                },
                WebURLParams.getAddToCartParams(session, mItem.getId(), size != null ? size.getId() : ""),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_TO_CART_TAG, new ProgressDialog(this));
    }

    private void showData(Product product, Item item) {
        mProduct = product;
        mItem = getItemById(product.getItems(), item.getId());
        showViews(ViewMode.DATA);

        mTvSeason.setText(product.getSeason().getName());
        mTvBrand.setText(product.getBrand().getName());
        if (mItem.isWished())
            mIvAddToWishList.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like_filled));
        else
            mIvAddToWishList.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like));
        Glide.with(this).load(product.getBrand().getIcon()).apply(requestOptions).into(mIvBrand);
        if (product.getCategories().size() > 0) {
            mTvCategory.setText(product.getCategories().get(0).getName());
            Glide.with(this).load(product.getCategories().get(0).getImage()).apply(requestOptions).into(mIvCategory);
        }
        mTvStore.setText(product.getStore().getName());
        Glide.with(this).load(product.getStore().getLogo()).apply(requestOptions).into(mIvStore);

        ArrayList<Image> images = mItem.getImage();
        ImageAdapter adapter = new ImageAdapter(this, images, true);
        mImagesPager.setAdapter(adapter);
        mTvItemDescription.setText(product.getDesc());
        mTvItemShortDescription.setText(product.getDesc());
        mTvCanBeShipped.setText(product.getCanBeShipped() == 1 ? getString(R.string.yes) : getString(R.string.no));
        mTvShippingFees.setText(product.getShippingFee() + " " + mItem.getCurrency());
        mTvPhoneNumber.setText(product.getStore().getPhone());
        mTvPrice.setText(mItem.getPrice() + " " + mItem.getCurrency());
        mTvNewPrice.setText(mItem.getNewPrice() + " " + mItem.getCurrency());
        mTvItemTitle.setText(mProduct.getName());
        if (!mItem.getSizes().isEmpty()) {
            size = mItem.getSizes().get(0);
            mTvSize.setText(size.getName());
        }

        mTvSale.setText(mItem.getDiscount().intValue() + "% " + getString(R.string.off));

        Glide.with(this).load(mItem.getColor().getPhoto()).apply(requestOptions).into(mItemColor);

        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(mProduct.getRelatedProducts());

        mProductAdapter = new ProductsAdapter(this, objects, this);
        mRvList.setAdapter(mProductAdapter);

        mTvAlsoLike.setVisibility(mProduct.getRelatedProducts().size() > 0 ? View.VISIBLE : View.GONE);
    }

    private void showViews(int viewMode) {
        if (mPbLoading != null && mVData != null && mVErrorHolder != null) {
            switch (viewMode) {
                case ViewMode.PROGRESS:
                    mPbLoading.setVisibility(View.VISIBLE);
                    mVData.setVisibility(View.GONE);
                    mVErrorHolder.setVisibility(View.GONE);
                    break;
                case ViewMode.DATA:
                    mPbLoading.setVisibility(View.GONE);
                    mVData.setVisibility(View.VISIBLE);
                    mVErrorHolder.setVisibility(View.GONE);
                    break;
                case ViewMode.ERROR:
                    mPbLoading.setVisibility(View.GONE);
                    mVData.setVisibility(View.GONE);
                    mVErrorHolder.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void showError(int errorCode, String errorMessage, String errorActionName) {
        mTvError.setText(errorMessage);
        mBtnErrorAction.setText(errorActionName);

        mBtnErrorAction.setTag(errorCode);

        showViews(ViewMode.ERROR);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back_arrow:
                finish();
                break;
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true);
                break;
            case R.id.v_all_description:
                if (mAllDescriptionDetails.getLayoutParams().height == 0 || mAllDescriptionDetails.getVisibility() == View.GONE) {
                    mIvExpandCollapseDescIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_up_arrow));
                    ViewAnimationUtils.expand(mAllDescriptionDetails, 300, mAllDescriptionDetailsHeight);
                } else {
                    mIvExpandCollapseDescIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_arrow_down));
                    ViewAnimationUtils.collapse(mAllDescriptionDetails, 300, 0);
                }
                break;
            case R.id.v_all_shipping_info:
                if (mAllShippingInfoDetails.getLayoutParams().height == 0 || mAllShippingInfoDetails.getVisibility() == View.GONE) {
                    mIvExpandCollapseShippingIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_up_arrow));
                    ViewAnimationUtils.expand(mAllShippingInfoDetails, 300, mAllShippingInfoDetailsHeight);
                } else {
                    mIvExpandCollapseShippingIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_arrow_down));
                    ViewAnimationUtils.collapse(mAllShippingInfoDetails, 300, 0);
                }
                break;
            case R.id.iv_item_color:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_color_picker, null);
                mRvList = (RecyclerView) view.findViewById(R.id.rv_color_list);
                mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mRvList.setLayoutManager(mLayoutManager);
                mAdapter = new ColorAdapter(this, getProductItemsWithSelection(), this);
                mRvList.setAdapter(mAdapter);
                builder.setView(view);
                colorDialog = builder.show();
                break;
            case R.id.v_color:
                builder = new AlertDialog.Builder(this);
                inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.dialog_color_picker, null);
                mRvList = (RecyclerView) view.findViewById(R.id.rv_color_list);
                mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mRvList.setLayoutManager(mLayoutManager);
                mAdapter = new ColorAdapter(this, getProductItemsWithSelection(), this);
                mRvList.setAdapter(mAdapter);
                builder.setView(view);
                colorDialog = builder.show();
                break;
            case R.id.iv_add_to_wishlist:
                if (mItem.isWished()) {
                    removeAddToWishList();
                } else {
                    loadAddToWishList();
                }
                break;
            case R.id.tv_add_to_cart:
                loadAddToCart();
                break;
            case R.id.v_size:
                showDialog(mItem.getSizes());
                break;
            case R.id.cart:
                Intent i2 = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(i2);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                break;
        }
    }

    @Override
    public void onItemClicked(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("reload", true);
        intent.putExtra("item", product.getItems().get(0));
        startActivity(intent);
    }

    @Override
    public void onJsonDataLoadedSuccessfully(JSONObject data) {
        Product product = JsonParser.json2Product(data, AppConstants.large);
        showData(product, mItem);
    }

    @Override
    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
    }

    @Override
    public void onJsonDataLoadingFailure(int errorId) {
        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
    }

    public ArrayList<Color> getProductColors() {
        ArrayList<Color> colors = new ArrayList<>();
        for (Item item : mProduct.getItems()) {
            colors.add(item.getColor());
        }
        return colors;
    }

    @Override
    public void onItemClick(Item item) {
        colorDialog.dismiss();
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", mProduct);
        intent.putExtra("reload", false);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    private void loadAddToWishList() {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getAddToWishListUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        mIvAddToWishList.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like_filled));
                        mItem.setWished(true);
                        Utils.makeToast(ProductDetailsActivity.this, mProduct.getName() + " " + getString(R.string.has_been_added_to_wishlist));
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(ProductDetailsActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(ProductDetailsActivity.this, getString(R.string.error_connection));
                    }
                },
                WebURLParams.getAddToWishListParams(session, mItem.getId(), size.getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_TO_CART_TAG, new ProgressDialog(this));
    }

    private void removeAddToWishList() {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getRemoveFromWishListUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        mIvAddToWishList.setImageDrawable(getResources().getDrawable(R.mipmap.ic_like));
                        mItem.setWished(false);
                        Utils.makeToast(ProductDetailsActivity.this, mProduct.getName() + " " + getString(R.string.has_been_removed_from_wishlist));
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(ProductDetailsActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(ProductDetailsActivity.this, getString(R.string.error_connection));
                    }
                },
                WebURLParams.getAddToWishListParams(session, mItem.getId(), size.getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_TO_CART_TAG, new ProgressDialog(this));
    }

    public Item getItemById(ArrayList<Item> items, String id) {
        for (Item item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }


    ArrayList<Item> getProductItemsWithSelection() {
        for (Item item : mProduct.getItems()) {
            if (item.getId().equals(mItem.getId())) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        return mProduct.getItems();
    }

    void showDialog(ArrayList<KeyValue> values) {
        RecyclerView mRvList;
        LinearLayoutManager mLayoutManager;
        FilterAdapter mAdapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_values, null);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);
        mAdapter = new FilterAdapter(this, values, new FilterAdapter.OnFilterItemClickListener() {
            @Override
            public void onItemClick(KeyValue value) {
                size = value;
                mTvSize.setText(value.getName());
                dialog.dismiss();
            }
        });
        mRvList.setAdapter(mAdapter);
        builder.setView(view);
        dialog = builder.show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        collapsingToolbarLayout.setTitle(mProduct.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
