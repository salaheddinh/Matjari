package com.salaheddin.store.ui.activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListPagingHandler;
import com.salaheddin.store.helpers.ListSpacingItemDecoration;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Offer;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.OfferAdapter;
import com.salaheddin.store.ui.adapters.OffersListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllOffersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener ,OfferAdapter.OnOfferItemClick{
    private final String TAG = "AllOffersActivity";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private OffersListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private int mNewLoadedListCount = 0;
    private boolean mCurrentlyLoadingMore = false;
    private boolean mHasMore = true;

    private ListPagingHandler mPagingHandler;
    private int previousTotal = 0;
    private boolean loading = true; // The var is for loading upon scrolling
    private int visibleThreshold = 1;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private Toolbar mToolbar;

    public AllOffersActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category_products);
        init();
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        loadData(true, false);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_offers);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else {
            return false;
        }
    }

    void init() {
        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mBtnErrorAction.setOnClickListener(this);

        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRvList.setLayoutManager(mLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_list_spacing);
        mRvList.addItemDecoration(new ListSpacingItemDecoration(spacingInPixels, mLayoutManager.getSpanCount()));

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

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        int pageNo = mPagingHandler.getPageNumber(loadingMore);

        if (loadingMore) {
            if (mHasMore) {
                if (!mCurrentlyLoadingMore) {
                    mCurrentlyLoadingMore = true;
                    DataLoader.loadJsonDataPost(this, WebUrls.getAllOffersUrl(),
                            new GetLoadingMoreRequestHandler(),
                            WebURLParams.getAllOffersParams(session, pageNo, AppConstants.PAGE_SIZE),
                            WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
                }
            } else {
                mAdapter.removeBottomProgress();
            }
        } else {
            DataLoader.loadJsonDataPost(this, WebUrls.getAllOffersUrl(),
                    new GetListRequestHandler(),
                    WebURLParams.getAllOffersParams(session, pageNo, AppConstants.PAGE_SIZE),
                    WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
        }
    }

    private void showData(ArrayList<Object> objects) {
        showViews(ViewMode.DATA);

        mAdapter = new OffersListAdapter(this, objects, this);

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
    public void onItemClick(Offer offer) {
        Intent intent = new Intent(this, OfferDetailsActivity.class);
        intent.putExtra("offer", offer);
        startActivity(intent);
    }

    @Override
    public void onEmptyItemClick() {

    }

    private class GetListRequestHandler implements DataLoader.OnJsonDataLoadedListener {
        @Override
        public void onJsonDataLoadedSuccessfully(JSONObject data) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            ArrayList firstList = null;
            try {
                firstList = JsonParser.json2OfferList(data.getJSONArray("dataArray"));
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
                Utils.showToast(AllOffersActivity.this, errorMessage);
            } else {
                showError(errorCode, errorMessage, getString(R.string.error_action_retry));
            }
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(AllOffersActivity.this, errorId);
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
            ArrayList<Offer> newLoadedList = new ArrayList<>();
            try {
                newLoadedList = JsonParser.json2OfferList(data.getJSONArray("dataArray"));
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
}
