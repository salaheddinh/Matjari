package com.salaheddin.store.ui.activities;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.FilterTypesIds;
import com.salaheddin.store.helpers.ListPagingHandler;
import com.salaheddin.store.helpers.ListSpacingItemDecoration;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Product;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.ProductsAdapter;
import com.salaheddin.store.ui.fragments.FilterDrawerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, ProductsAdapter.OnProductItemClicked {

    private final String TAG = "HomeFragment";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private ProductsAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

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

    private String categoryId;
    private String categoryName;
    private String typeId;
    private String typeName;
    private Toolbar mToolbar;
    private boolean isType;

    public static DrawerLayout drawer;

    private TextView title;
    private ImageView mIvFilter;
    public static  int sortBy;
    public static String minPrice;
    public static String maxPrice;
    public static HashMap<Integer, String> filterValues;
    private String key;

    private FilterDrawerFragment filterDrawerFragment;

    private View filterView;
    private View sortView;
    private View sortTypesView;
    private TextView mTvHighToLow;
    private TextView mTvLowToHigh;
    private TextView mTvNew;

    public CategoryProductsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);
        filterValues = new HashMap<>();

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        typeId = getIntent().getStringExtra("typeId");
        typeName = getIntent().getStringExtra("typeName");
        key = getIntent().getStringExtra("key");
        filterValues = (HashMap<Integer, String>) getIntent().getSerializableExtra("filters");
        if (filterValues == null){
            filterValues = new HashMap<>();
        }
        if (key == null || key.toLowerCase().equals("null")){
            key = "";
        }
        if (categoryId == null || categoryId.toLowerCase().equals("null")){
            categoryId = "";
        }
        if (typeId == null || typeId.toLowerCase().equals("null")){
            typeId = "";
        }
        isType = getIntent().getBooleanExtra("isType", false);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!key.equals("")){
            title.setText(key);
        }else {
            if (isType)
                title.setText(typeName);
            else
                title.setText(categoryName);
        }
        filterDrawerFragment = new FilterDrawerFragment();
        Bundle bundle = new Bundle();
        if (isType) {
            bundle.putString("typeId", typeId);
            bundle.putBoolean("isType", true);
        } else {
            bundle.putString("categoryId", categoryId);
            bundle.putBoolean("isType", false);
        }
        filterDrawerFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, filterDrawerFragment).commit();

        loadData(true, false);
    }


    void init() {
        sortBy = FilterTypesIds.NEW;

        mIvFilter = (ImageView) findViewById(R.id.iv_filter);
        title = (TextView) findViewById(R.id.tv_title);

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
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        filterView = findViewById(R.id.v_filter);
        sortView = findViewById(R.id.v_sort);
        sortTypesView = findViewById(R.id.v_sort_types);

        mTvHighToLow = (TextView) findViewById(R.id.tv_high_to_low);
        mTvLowToHigh = (TextView) findViewById(R.id.tv_low_to_high);
        mTvNew = (TextView) findViewById(R.id.tv_new);

        mIvFilter.setOnClickListener(this);
        filterView.setOnClickListener(this);
        sortView.setOnClickListener(this);
        sortTypesView.setOnClickListener(this);
        mTvHighToLow.setOnClickListener(this);
        mTvLowToHigh.setOnClickListener(this);
        mTvNew.setOnClickListener(this);
    }

    public void loadData(boolean showProgress, boolean loadingMore) {
        if (showProgress && !loadingMore) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        int pageNo = mPagingHandler.getPageNumber(loadingMore);

        if (filterDrawerFragment.mEtMinPrice != null &&
                filterDrawerFragment.mEtMaxPrice != null) {
            minPrice = filterDrawerFragment.mEtMinPrice.getText().toString();
            maxPrice = filterDrawerFragment.mEtMaxPrice.getText().toString();
        }
        if (minPrice != null &&minPrice.isEmpty()){
            minPrice = filterDrawerFragment.getFilterByType(FilterTypesIds.PRICE).getMin();
        }
        if (maxPrice != null && maxPrice.isEmpty()){
            maxPrice = filterDrawerFragment.getFilterByType(FilterTypesIds.PRICE).getMax();
        }

        HashMap<String, String> params;
        if (!key.equals("")){
            params = WebURLParams.getCategoryProductsParams(session, "", "", pageNo, AppConstants.PAGE_SIZE, filterValues, sortBy, key,minPrice,maxPrice);
        }else {
            if (isType)
                params = WebURLParams.getCategoryProductsParams(session, "", typeId, pageNo, AppConstants.PAGE_SIZE, filterValues, sortBy, key,minPrice,maxPrice);
            else
                params = WebURLParams.getCategoryProductsParams(session, categoryId, "", pageNo, AppConstants.PAGE_SIZE, filterValues, sortBy, key,minPrice,maxPrice);
        }
        if (loadingMore) {
            if (mHasMore) {
                if (!mCurrentlyLoadingMore) {
                    mCurrentlyLoadingMore = true;
                    DataLoader.loadJsonDataPost(this, WebUrls.getCategoryProductsUrl(),
                            new GetLoadingMoreRequestHandler(),
                            params,
                            WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
                }
            } else {
                mAdapter.removeBottomProgress();
            }
        } else {
            DataLoader.loadJsonDataPost(this, WebUrls.getCategoryProductsUrl(),
                    new GetHomeListRequestHandler(),
                    params,
                    WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
        }
    }

    private void showData(ArrayList<Object> objects) {
        showViews(ViewMode.DATA);

        mAdapter = new ProductsAdapter(this, objects, this);

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
            case R.id.v_filter:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.v_sort:
                if (sortTypesView.getLayoutParams().height == 0 || sortTypesView.getVisibility() == View.GONE) {
                    sortTypesView.setVisibility(View.VISIBLE);
                } else {
                    sortTypesView.setVisibility(View.GONE);
                }

                break;
            case R.id.tv_high_to_low:
                sortTypesView.setVisibility(View.GONE);
                sortBy = FilterTypesIds.HIGH_TO_LOW;
                loadData(true,false);
                break;
            case R.id.tv_low_to_high:
                sortTypesView.setVisibility(View.GONE);
                sortBy = FilterTypesIds.LOW_TO_HIGH;
                loadData(true,false);
                break;
            case R.id.tv_new:
                sortTypesView.setVisibility(View.GONE);
                sortBy = FilterTypesIds.NEW;
                loadData(true,false);
                break;
            default:
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

    private class GetHomeListRequestHandler implements DataLoader.OnJsonDataLoadedListener {
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
                Utils.showToast(CategoryProductsActivity.this, errorMessage);
            } else {
                showError(errorCode, errorMessage, getString(R.string.error_action_retry));
            }
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(CategoryProductsActivity.this, errorId);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            filterValues = (HashMap<Integer, String>) data.getSerializableExtra("filters");
            sortBy = data.getIntExtra("sortBy", FilterTypesIds.NEW);
            minPrice = data.getStringExtra("min");
            maxPrice = data.getStringExtra("max");
            loadData(true, false);
        }
    }
}
