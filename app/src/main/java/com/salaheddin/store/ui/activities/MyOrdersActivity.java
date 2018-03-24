package com.salaheddin.store.ui.activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.ListPagingHandler;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Order;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.OrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, OrderAdapter.OnItemClickListener {

    private final String TAG = "MyOrdersActivity";
    private final String CANCEL_ORDER_TAG = "MyOrdersActivity";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private OrderAdapter mAdapter;
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

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadData(true, false);
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
        mLayoutManager = new LinearLayoutManager(this);
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

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
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
                    DataLoader.loadJsonDataPost(this, WebUrls.getMyOrdersUrl(),
                            new GetLoadingMoreRequestHandler(),
                            WebURLParams.getWishListParams(session, pageNo, AppConstants.PAGE_SIZE),
                            WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
                }
            } else {
                mAdapter.removeBottomProgress();
            }
        } else {
            DataLoader.loadJsonDataPost(this, WebUrls.getMyOrdersUrl(),
                    new GetListRequestHandler(),
                    WebURLParams.getWishListParams(session, pageNo, AppConstants.PAGE_SIZE),
                    WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
        }
    }

    private void showData(ArrayList<Object> objects) {
        showViews(ViewMode.DATA);

        mAdapter = new OrderAdapter(this, objects, this);

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
    public void onItemClicked(Order order) {

    }

    @Override
    public void onCancelOrderClicked(Order order) {
        loadCancelOrder(order);
    }

    private class GetListRequestHandler implements DataLoader.OnJsonDataLoadedListener {
        @Override
        public void onJsonDataLoadedSuccessfully(JSONObject data) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            ArrayList firstList = null;
            try {
                firstList = JsonParser.json2OrderList(data.getJSONArray("dataArray"));
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
                Utils.showToast(MyOrdersActivity.this, errorMessage);
            } else {
                showError(errorCode, errorMessage, getString(R.string.error_action_retry));
            }
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(MyOrdersActivity.this, errorId);
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
            ArrayList<Order> newLoadedList = new ArrayList<>();
            try {
                newLoadedList = JsonParser.json2OrderList(data.getJSONArray("dataArray"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCancelOrder(final Order order) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getCancelOrderUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Utils.makeToast(MyOrdersActivity.this, getString(R.string.order_cancelation));
                        loadData(true,false);
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(MyOrdersActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(MyOrdersActivity.this, getString(R.string.error_connection));
                    }
                },
                WebURLParams.getCancelOrderParams(session, order.getId(),""),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, CANCEL_ORDER_TAG, new ProgressDialog(this));
    }
}
