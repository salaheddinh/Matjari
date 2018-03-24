package com.salaheddin.store.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.DictionaryItem;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.DictionaryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, DictionaryAdapter.OnSearchItemClickListener {

    private ImageView mIvBack;
    private ImageView mIvClear;
    private TextView mEtKeyword;
    private static final String TAG = "SearchActivity";
    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private TextView mTvError;
    private Button mBtnErrorAction;

    private RecyclerView mRvList;
    private DictionaryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<DictionaryItem> dictionaryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        loadData(true);
    }

    void init() {
        dictionaryItems = new ArrayList<>();
        mEtKeyword = (TextView) findViewById(R.id.et_search_keyword);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvClear = (ImageView) findViewById(R.id.iv_cancel_search);
        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtKeyword.setText("");
            }
        });
        mEtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(SearchActivity.this, CategoryProductsActivity.class);
                    intent.putExtra("isType", false);
                    intent.putExtra("key", v.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);

        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mBtnErrorAction.setOnClickListener(this);

        mRvList = (RecyclerView) findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getDictionaryUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        ArrayList<DictionaryItem> categories = new ArrayList<>();
                        ArrayList<DictionaryItem> products = new ArrayList<>();
                        try {
                            categories = JsonParser.json2DictionaryItemList(data.getJSONArray("categories"), 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            products = JsonParser.json2DictionaryItemList(data.getJSONArray("products"), 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dictionaryItems.addAll(categories);
                        dictionaryItems.addAll(products);
                        showData();
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
                WebURLParams.getCartDetailsParams(session),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    private void showData() {
        mEtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<DictionaryItem> items = search(s);
                mAdapter = new DictionaryAdapter(SearchActivity.this, items, SearchActivity.this);
                mRvList.setAdapter(mAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showViews(ViewMode.DATA);

        mAdapter = new DictionaryAdapter(this, dictionaryItems, this);
        mRvList.setAdapter(mAdapter);
    }

    private ArrayList<DictionaryItem> search(CharSequence key) {
        ArrayList<DictionaryItem> items = new ArrayList<>();
        for (DictionaryItem item:dictionaryItems){
            if (item.getNameEn().toLowerCase().contains(key.toString().toLowerCase()) || item.getNameAr().toLowerCase().contains(key.toString().toLowerCase())){
                items.add(item);
            }
        }
        return items;
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
        switch (v.getId()) {
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(DictionaryItem item, int position) {
        if (item.getType() == 1){
            Intent intent = new Intent(SearchActivity.this, CategoryProductsActivity.class);
            intent.putExtra("categoryId", item.getId());
            intent.putExtra("categoryName", item.getNameEn());
            intent.putExtra("isType", false);
            startActivity(intent);
        }else {
            Intent intent = new Intent(SearchActivity.this, CategoryProductsActivity.class);
            intent.putExtra("isType", false);
            intent.putExtra("key", item.getNameEn());
            startActivity(intent);
        }
    }
}
