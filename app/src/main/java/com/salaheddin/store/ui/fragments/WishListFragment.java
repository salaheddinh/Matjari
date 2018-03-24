package com.salaheddin.store.ui.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListPagingHandler;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.VolleySingleton;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.activities.ProductDetailsActivity;
import com.salaheddin.store.ui.adapters.FilterAdapter;
import com.salaheddin.store.ui.adapters.WishListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, WishListAdapter.OnItemClickListener {

    private final String TAG = "WishListFragment";
    private final String ADD_TO_CART_TAG = "ADD_TO_CART_TAG";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private WishListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    //Recycler view loading more params
    private int mNewLoadedListCount = 0;
    private int mPageSize = -1;
    private boolean mCurrentlyLoadingMore = false;
    private boolean mHasMore = true;

    //private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    // Vars to handle loading more scrolls
    private ListPagingHandler mPagingHandler;
    private int previousTotal = 0;
    private boolean loading = true; // The var is for loading upon scrolling
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    AlertDialog dialog;

    public WishListFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        init(view);
        loadData(true, false);
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

        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRvList.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold) && mHasMore) {
                    // End has been reached
                    loading = true;
                    mAdapter.addBottomProgress();
                    loadData(false, true);
                }
            }
        });
        mPagingHandler = new ListPagingHandler();
    }

    private void loadData(boolean showProgress, boolean loadingMore) {
        if (showProgress && !loadingMore) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");
        int pageNo = mPagingHandler.getPageNumber(loadingMore);

        if (loadingMore) {
            if (mHasMore) {
                if (!mCurrentlyLoadingMore) {
                    mCurrentlyLoadingMore = true;
                    DataLoader.loadJsonDataPost(getActivity(), WebUrls.getWishListUrl(),
                            new GetLoadingMoreRequestHandler(),
                            WebURLParams.getWishListParams(session, pageNo, AppConstants.PAGE_SIZE),
                            WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
                }
            } else {
                mAdapter.removeBottomProgress();
            }
        } else {
            DataLoader.loadJsonDataPost(getActivity(), WebUrls.getWishListUrl(),
                    new GetListRequestHandler(),
                    WebURLParams.getWishListParams(session, pageNo, AppConstants.PAGE_SIZE),
                    WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
        }
    }

    private void showData(ArrayList<Object> objects) {
        showViews(ViewMode.DATA);

        mAdapter = new WishListAdapter(getActivity(), objects, this);

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
        loadData(true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true, false);
                break;
            default:
                break;
        }
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
    public void onBagClicked(Product product) {
        if (product.getItems() != null && !product.getItems().isEmpty())
            showDialog(product.getItems().get(0).getSizes(),product);
        //loadAddToCart(product);
    }

    private class GetListRequestHandler implements DataLoader.OnJsonDataLoadedListener {
        @Override
        public void onJsonDataLoadedSuccessfully(JSONObject data) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            ArrayList firstList = null;
            try {
                firstList = JsonParser.json2ProductList(data.getJSONArray("dataArray"), AppConstants.medium);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showData(firstList);

            previousTotal = 0;
            loading = true;
            mCurrentlyLoadingMore = false;
            visibleThreshold = 1;
            firstVisibleItem = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            if (firstList.size() < AppConstants.PAGE_SIZE)
                mHasMore = false;
            else
                mHasMore = true;
            mPagingHandler.resetPaging();
        }

        @Override
        public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(getActivity(), errorMessage);
            } else {
                showError(errorCode, errorMessage, getString(R.string.error_action_retry));
            }
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(getActivity(), errorId);
            } else {
                showError(errorId, getString(errorId), getString(R.string.error_action_retry));
            }
        }
    }

    private class GetLoadingMoreRequestHandler implements DataLoader.OnJsonDataLoadedListener {
        @Override
        public void onJsonDataLoadedSuccessfully(JSONObject data) {
            mCurrentlyLoadingMore = false;
            mAdapter.removeBottomProgress();
            ArrayList<Product> newLoadedList = new ArrayList<>();
            try {
                newLoadedList = JsonParser.json2ProductList(data.getJSONArray("dataArray"), AppConstants.medium);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mNewLoadedListCount = newLoadedList.size();
            if (mNewLoadedListCount < AppConstants.PAGE_SIZE) {
                mHasMore = false;
            } else {
                mHasMore = true;
            }

            mAdapter.addData(newLoadedList);
        }

        @Override
        public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
            mCurrentlyLoadingMore = false;
            mAdapter.removeBottomProgress();
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            mCurrentlyLoadingMore = false;
            mAdapter.removeBottomProgress();
        }
    }

    private void loadAddToCart(final Product product,KeyValue value) {
        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(getActivity(), WebUrls.getAddToCartUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        String size = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.CART_SIZE, "0");
                        int sizeInt = Integer.parseInt(size);
                        sizeInt++;
                        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.CART_SIZE, String.valueOf(sizeInt));
                        Utils.makeToast(getActivity(), product.getName() + " " + getString(R.string.successfully_added_to_the_bag));
                        loadData(true, false);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(getActivity(), errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(getActivity(), getString(R.string.error_connection));
                    }
                },
                WebURLParams.getAddToCartParams(session, product.getItems().get(0).getId(), value.getId()),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_TO_CART_TAG, new ProgressDialog(getActivity()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        VolleySingleton.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true, false);
    }

    void showDialog(ArrayList<KeyValue> values, final Product product) {
        RecyclerView mRvList;
        LinearLayoutManager mLayoutManager;
        FilterAdapter mAdapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_values, null);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvList.setLayoutManager(mLayoutManager);
        mAdapter = new FilterAdapter(getActivity(), values, new FilterAdapter.OnFilterItemClickListener() {
            @Override
            public void onItemClick(KeyValue value) {
                loadAddToCart(product,value);
                dialog.dismiss();
            }
        });
        mRvList.setAdapter(mAdapter);
        builder.setView(view);
        dialog = builder.show();
    }
}
