package com.salaheddin.store.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.VolleySingleton;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.activities.CategoryProductsActivity;
import com.salaheddin.store.ui.adapters.CategoryAdapter;
import com.salaheddin.store.ui.adapters.SubCategoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllCategoriesFragment extends Fragment implements BaseSliderView.OnSliderClickListener, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,SubCategoryAdapter.OnSubCategoryClickListener {
    private final String TAG = "AllCategoriesFragment";

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private ArrayList<Object> objects;
    private CategoryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_categories, container, false);
        init(view);
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

        objects = new ArrayList<>();

        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvList.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData(true, false);
    }

    private void loadData(boolean showProgress, boolean loadingMore) {
        if (showProgress && !loadingMore) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(getActivity(),SharedPreferencesManager.SESSION,"");
        DataLoader.loadJsonDataPost(getActivity(), WebUrls.getAllCategoriesUrl(),
                new GetListRequestHandler(),
                WebURLParams.getAllCategoriesParams(session, 1, AppConstants.PAGE_SIZE),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        VolleySingleton.getInstance().cancelPendingRequests(TAG);
    }

    private void showData(ArrayList<Object> objects) {
        showViews(ViewMode.DATA);

        mAdapter = new CategoryAdapter(getActivity(), objects,this);
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
    public void onItemClick(String categoryId,String categoryName) {
        Intent intent = new Intent(getActivity(),CategoryProductsActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("categoryName",categoryName);
        intent.putExtra("isType",false);
        startActivity(intent);
    }

    private class GetListRequestHandler implements DataLoader.OnJsonDataLoadedListener {
        @Override
        public void onJsonDataLoadedSuccessfully(JSONObject data) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            ArrayList<Object> firstList = null;
            try {
                firstList = JsonParser.json2MainCategoryList(data.getJSONArray("dataArray"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showData(firstList);
        }

        @Override
        public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(getActivity().getApplicationContext(), errorMessage);
            } else {
                showError(errorCode, errorMessage, getString(R.string.error_action_retry));
            }
        }

        @Override
        public void onJsonDataLoadingFailure(int errorId) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Utils.showToast(getActivity().getApplicationContext(), errorId);
            } else {
                showError(errorId, getString(errorId), getString(R.string.error_action_retry));
            }
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }
}
