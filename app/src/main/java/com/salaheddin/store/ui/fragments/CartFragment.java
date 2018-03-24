package com.salaheddin.store.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.salaheddin.store.ui.activities.AuthenticationActivity;
import com.salaheddin.store.ui.activities.CheckOutActivity;
import com.salaheddin.store.ui.activities.ProductDetailsActivity;
import com.salaheddin.store.ui.adapters.CartAdapter;

import org.json.JSONObject;

public class CartFragment extends Fragment implements CartAdapter.OnCartItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

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
    private Cart mCart;

    private TextView mTvCartPrice;
    private TextView mTvCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        init(view);
        loadData(true);
        return view;
    }

    void init(View view) {
        mPbLoading = (ImageView) view.findViewById(R.id.pb_loading);
        mVData = view.findViewById(R.id.data);
        mVErrorHolder = view.findViewById(R.id.error_holder);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mTvError = (TextView) view.findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) view.findViewById(R.id.btn_error_action);

        mBtnErrorAction.setOnClickListener(this);

        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvList.setLayoutManager(mLayoutManager);

        mTvCheckout = (TextView) view.findViewById(R.id.btn_cart_check_out);
        mTvCartPrice = (TextView) view.findViewById(R.id.total_price);

        mTvCheckout.setOnClickListener(this);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(getActivity(), WebUrls.getCartDetailsUrl(),
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
                            SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,"0");
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
        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(getActivity(), WebUrls.getIncreaseQuantityUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
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
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, INCREASE_TAG, new ProgressDialog(getActivity()));
    }

    private void decreaseQuantity(Product product) {
        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(getActivity(), WebUrls.getDecreaseQuantityUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
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
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, DECREASE_TAG, new ProgressDialog(getActivity()));
    }

    private void removeItem(Product product) {
        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(getActivity(), WebUrls.getRemoveFromCartUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Cart cart = JsonParser.json2Cart(data);
                        SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,cart.getItemsCount());
                        showData(cart);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        if (errorCode == -114){
                            SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,"0");
                        }
                        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
                    }
                },
                WebURLParams.getAddToCartParams(session, product.getItems().get(0).getId(),product.getItems().get(0).getSizes().get(0).getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, DECREASE_TAG, new ProgressDialog(getActivity()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(Product product) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
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

        SharedPreferencesManager.saveToPreferences(getActivity(),SharedPreferencesManager.CART_SIZE,cart.getItemsCount());

        mTvCartPrice.setText(cart.getPrice() + " " + cart.getCurrency());
        mAdapter = new CartAdapter(getActivity(), cart.getProducts(), this,cart.getCurrency());
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
            case R.id.btn_cart_check_out:
                String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");
                if (session.equals("")) {
                    startActivityForResult(new Intent(getActivity(), AuthenticationActivity.class), 100);
                } else {
                    Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                    intent.putExtra("cart", mCart);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
