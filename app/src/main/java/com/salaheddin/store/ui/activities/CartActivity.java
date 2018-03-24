package com.salaheddin.store.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Cart;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.CartAdapter;

import org.json.JSONObject;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private final String TAG = "CartActivity";
    private final String INCREASE_TAG = "INCREASE_TAG";
    private final String DECREASE_TAG = "DECREASE_TAG";
    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private CartAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private ImageView close;
    private Cart mCart;

    private TextView mTvCartPrice;
    private TextView mTvCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        loadData(true);
    }

    void init() {
        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        //mPbLoading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mBtnErrorAction.setOnClickListener(this);

        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);

        mTvCheckout = (TextView) findViewById(R.id.btn_cart_check_out);
        mTvCartPrice = (TextView) findViewById(R.id.total_price);

        close = (ImageView) findViewById(R.id.iv_close);
        close.setOnClickListener(this);
        mTvCheckout.setOnClickListener(this);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getCartDetailsUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Cart cart = JsonParser.json2Cart(data);
                        showData(cart);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        if (errorCode == -114){
                            SharedPreferencesManager.saveToPreferences(CartActivity.this,SharedPreferencesManager.CART_SIZE,"0");
                        }
                        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
                    }
                },
                WebURLParams.getCartDetailsParams(session),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    private void increaseQuantity(Product product) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getIncreaseQuantityUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(CartActivity.this,SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
                        showData(cart);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
                    }
                },
                WebURLParams.getAddToCartParams(session, product.getItems().get(0).getId(),product.getItems().get(0).getSizes().get(0).getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, INCREASE_TAG, new ProgressDialog(this));
    }

    private void decreaseQuantity(Product product) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getDecreaseQuantityUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(CartActivity.this,SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
                        showData(cart);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
                    }
                },
                WebURLParams.getAddToCartParams(session, product.getItems().get(0).getId(),product.getItems().get(0).getSizes().get(0).getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, DECREASE_TAG, new ProgressDialog(this));
    }

    private void removeItem(Product product) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getRemoveFromCartUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(CartActivity.this,SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
                        showData(cart);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        if (errorCode == -114){
                            SharedPreferencesManager.saveToPreferences(CartActivity.this,SharedPreferencesManager.CART_SIZE,"0");
                        }
                        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
                    }
                },
                WebURLParams.getAddToCartParams(session, product.getItems().get(0).getId(),product.getItems().get(0).getSizes().get(0).getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, DECREASE_TAG, new ProgressDialog(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
    public void onIncreaseClicked(Product product) {
        increaseQuantity(product);
    }

    @Override
    public void onDecreaseClicked(Product product) {
        decreaseQuantity(product);
    }

    @Override
    public void onRemoveClicked(Product product) {
        removeItem(product);
    }

    @Override
    public void onNoEnoughItems(Boolean isEnough) {
        if (!isEnough){
            mTvCheckout.setAlpha(0.5f);
            mTvCheckout.setOnClickListener(null);
        }else {
            mTvCheckout.setAlpha(1);
            mTvCheckout.setOnClickListener(this);
        }
    }

    private void showData(Cart cart) {
        mCart = cart;
        showViews(ViewMode.DATA);

        SharedPreferencesManager.saveToPreferences(this,SharedPreferencesManager.CART_SIZE,cart.getItemsCount());

        mTvCartPrice.setText(cart.getPrice() + " " + cart.getCurrency());
        mAdapter = new CartAdapter(this, cart.getProducts(), this,cart.getCurrency());
        mRvList.setAdapter(mAdapter);
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
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true);
                break;
            case R.id.iv_close:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
                break;
            case R.id.btn_cart_check_out:
                String session = SharedPreferencesManager.readFromPreferences(CartActivity.this, SharedPreferencesManager.SESSION, "");
                if (session.equals("")) {
                    startActivityForResult(new Intent(CartActivity.this, AuthenticationActivity.class), 100);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                } else {
                    Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                    intent.putExtra("cart", mCart);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
    }
}
