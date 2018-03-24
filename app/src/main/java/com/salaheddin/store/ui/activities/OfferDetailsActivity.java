package com.salaheddin.store.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.ViewAnimationUtils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Offer;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.ProductsListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class OfferDetailsActivity extends AppCompatActivity implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener,
        ProductsListAdapter.OnProductItemClicked {
    private final String TAG = "ProductDetailsActivity";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private ImageView mBackArrow;
    private TextView mTvToolbarText;
    private View mAllDescription;

    private View mAllDescriptionDetails;
    private int mAllDescriptionDetailsHeight;
    private ImageView mIvExpandCollapseDescIcon;

    private TextView mTvPrice;
    private TextView mTvNewPrice;
    private TextView mTvBuyNow;
    private TextView mTvOfferDescription;
    private TextView mTvOfferTitle;

    private Toolbar mToolbar;
    private ImageView mIvOfferImage;

    private RecyclerView mRvList;
    private ProductsListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private TextView quantity;
    private ImageView increase;
    private ImageView decrease;
    private View quantityView;

    private View timeView;
    private TextView tvHours;
    private TextView tvMins;
    private TextView tvScnds;

    private Offer mOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        mOffer = (Offer) getIntent().getSerializableExtra("offer");
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mOffer.getName());
        init();
        loadData(true);
    }

    void init() {
        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mBackArrow = (ImageView) findViewById(R.id.iv_back_arrow);
        mTvToolbarText = (TextView) findViewById(R.id.tv_toolbar_text);
        mAllDescription = findViewById(R.id.v_all_description);
        mAllDescriptionDetails = findViewById(R.id.v_all_description_details);
        mAllDescriptionDetailsHeight = mAllDescriptionDetails.getLayoutParams().height;
        mIvExpandCollapseDescIcon = (ImageView) findViewById(R.id.tv_desc_collapse);

        mIvOfferImage = (ImageView) findViewById(R.id.iv_offer_image);

        mTvPrice = (TextView) findViewById(R.id.tv_items_price);
        mTvPrice.setPaintFlags(mTvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mTvNewPrice = (TextView) findViewById(R.id.tv_offer_price);
        mTvBuyNow = (TextView) findViewById(R.id.tv_buy_now);
        mTvOfferDescription = (TextView) findViewById(R.id.tv_item_description);
        mTvOfferTitle = (TextView) findViewById(R.id.tv_offer_title);

        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);

        quantityView = findViewById(R.id.v_count);
        quantity = (TextView) findViewById(R.id.tv_product_quantity);
        increase = (ImageView) findViewById(R.id.iv_increase);
        decrease = (ImageView) findViewById(R.id.iv_decrease);

        timeView = findViewById(R.id.v_time);
        tvHours = (TextView) findViewById(R.id.tv_hours);
        tvMins = (TextView) findViewById(R.id.tv_mins);
        tvScnds = (TextView) findViewById(R.id.tv_scnds);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        mBackArrow.setImageDrawable(upArrow);

        mTvToolbarText.setText(mOffer.getName());

        mBackArrow.setOnClickListener(this);
        mBtnErrorAction.setOnClickListener(this);
        mAllDescription.setOnClickListener(this);
        mTvBuyNow.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getOfferDetailsUrl(),
                this,
                WebURLParams.getOfferDetailsParams(session, mOffer.getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    private void showData(Offer offer) {
        mOffer = offer;
        showViews(ViewMode.DATA);

        mTvPrice.setText(offer.getOfferItemsPrice() + " " + getString(R.string.currency));
        mTvNewPrice.setText(offer.getOfferPrice() + " " + getString(R.string.currency));
        mTvOfferDescription.setText(offer.getDesc());
        mTvOfferTitle.setText(offer.getName());
        RequestOptions requestOptions;
        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(R.drawable.empty_holder);
        Glide.with(this).load(offer.getImage()).apply(requestOptions).into(mIvOfferImage);

        mAdapter = new ProductsListAdapter(this, offer.getOfferProducts(), this);
        mRvList.setAdapter(mAdapter);

        final int time = (int) offer.getRemainingTime();
        final int[] seconds = {time};
        final int[] min = {(int) (time / 60.0)};
        final int[] hours = {(int) (min[0] / 60.0)};
        double remaining;

        if (hours[0] > 0) {
            tvHours.setText(hours[0] + "");
            min[0] = min[0] - (hours[0] * 60);
            tvMins.setText(min[0] + "");
            remaining = seconds[0] - (min[0] * 60.0) - (hours[0] * 3600.0) ;
            seconds[0] = (int) remaining;
            tvScnds.setText(seconds[0] + "");
        } else {
            tvHours.setText(0 + "");
            tvMins.setText(min[0] + "");
            remaining = seconds[0] - (min[0] * 60.0) - (hours[0] * 3600.0) ;
            seconds[0] = (int) remaining;
            tvScnds.setText(seconds[0] + "");
        }
        long milliSeconds = (long) (time * 1000);
        new CountDownTimer(milliSeconds, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                seconds[0]--;
                tvScnds.setText(seconds[0] + "");
                tvMins.setText(min[0] + "");
                tvHours.setText(hours[0] + "");
                if (seconds[0] == 0) {
                    min[0]--;
                    seconds[0] = 60;
                }
                if (min[0] == 0) {
                    hours[0]--;
                    min[0] = 60;
                }
            }

            public void onFinish() {
            }
        }.start();

        //ongoing
        if (offer.getStatus() == 1) {
            quantityView.setVisibility(View.VISIBLE);
            mTvBuyNow.setEnabled(true);
            mTvBuyNow.setText(R.string.buy_now);
            mTvBuyNow.setBackgroundColor(getResources().getColor(R.color.pale_red));
            timeView.setVisibility(View.VISIBLE);
        }
        //coming soon
        else if (offer.getStatus() == 2) {
            quantityView.setVisibility(View.GONE);
            mTvBuyNow.setEnabled(false);
            mTvBuyNow.setText(getString(R.string.comming_soon));
            mTvBuyNow.setBackgroundColor(getResources().getColor(R.color.blue));
            timeView.setVisibility(View.VISIBLE);
        }
        //sold
        else {
            quantityView.setVisibility(View.GONE);
            mTvBuyNow.setEnabled(false);
            mTvBuyNow.setText(getString(R.string.sold));
            timeView.setVisibility(View.GONE);
        }
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
                    mIvExpandCollapseDescIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_remove));
                    ViewAnimationUtils.expand(mAllDescriptionDetails, 300, mAllDescriptionDetailsHeight);
                } else {
                    mIvExpandCollapseDescIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add));
                    ViewAnimationUtils.collapse(mAllDescriptionDetails, 300, 0);
                }
                break;
            case R.id.tv_buy_now:
                String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
                if (session.equals("")) {
                    startActivity(new Intent(OfferDetailsActivity.this, AuthenticationActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                } else {
                    buyOffer();
                }
                break;
            case R.id.iv_increase:
                int qty = Integer.parseInt(quantity.getText().toString());
                qty++;
                quantity.setText(qty + "");
                break;
            case R.id.iv_decrease:
                qty = Integer.parseInt(quantity.getText().toString());
                if (qty > 1) {
                    qty--;
                    quantity.setText(qty + "");
                }
                break;
        }
    }

    private void buyOffer() {
        Intent intent = new Intent(OfferDetailsActivity.this, CheckOutActivity.class);
        intent.putExtra("offer", mOffer);
        intent.putExtra("offerQuantity", Integer.parseInt(quantity.getText().toString()));
        startActivity(intent);
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
        Offer offer = JsonParser.json2Offer(data);
        showData(offer);
    }

    @Override
    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
    }

    @Override
    public void onJsonDataLoadingFailure(int errorId) {
        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
