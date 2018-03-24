package com.salaheddin.store.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.FilterTypesIds;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.FilterTypes;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.adapters.ColorForFilterAdapter;
import com.salaheddin.store.ui.adapters.FilterAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "FilterActivity";
    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;
    private TextView mTvError;
    private Button mBtnErrorAction;

    private View mVNew;
    private View mVPriceHigh;
    private View mVPriceLow;
    private View mVPrice;
    private View mVBrand;
    private View mVColor;
    private View mVSize;
    private View mVSeason;

    private ImageView mIvNew;
    private ImageView mIvPriceHigh;
    private ImageView mIvPriceLow;
    private ImageView mIvClose;

    private TextView mTvNew;
    private TextView mTvPriceHigh;
    private TextView mTvPriceLow;
    private TextView mTvPrice;
    private TextView mTvBrand;
    private TextView mTvColor;
    private TextView mTvSize;
    private TextView mTvSeason;

    private String categoryId;
    private String typeId;
    private boolean isType;
    ArrayList<FilterTypes> mFilterTypes;
    HashMap<Integer, String> filterValues;
    AlertDialog dialog;
    String key;
    String min;
    String max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        categoryId = getIntent().getStringExtra("categoryId");
        typeId = getIntent().getStringExtra("typeId");
        key = getIntent().getStringExtra("key");
        if (key == null || key.toLowerCase().equals("null")) {
            key = "";
        }
        isType = getIntent().getBooleanExtra("isType", false);
        init();

        loadData(true);
    }

    void init() {
        filterValues = new HashMap<>();

        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);
        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);

        mVNew = findViewById(R.id.v_new);
        mVPriceHigh = findViewById(R.id.v_price_high);
        mVPriceLow = findViewById(R.id.v_price_low);
        mVPrice = findViewById(R.id.v_price);
        mVBrand = findViewById(R.id.v_brand);
        mVSize = findViewById(R.id.v_size);
        mVColor = findViewById(R.id.v_color);
        mVSeason = findViewById(R.id.v_season);

        mIvNew = (ImageView) findViewById(R.id.iv_new);
        mIvPriceHigh = (ImageView) findViewById(R.id.iv_price_high);
        mIvPriceLow = (ImageView) findViewById(R.id.iv_price_low);
        mIvClose = (ImageView) findViewById(R.id.iv_close);

        mTvNew = (TextView) findViewById(R.id.tv_new);
        mTvPriceHigh = (TextView) findViewById(R.id.tv_price_high);
        mTvPriceLow = (TextView) findViewById(R.id.tv_price_low);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvBrand = (TextView) findViewById(R.id.tv_brand);
        mTvSize = (TextView) findViewById(R.id.tv_size);
        mTvColor = (TextView) findViewById(R.id.tv_color);
        mTvSeason = (TextView) findViewById(R.id.tv_season);

        mVNew.setOnClickListener(this);
        mVPriceHigh.setOnClickListener(this);
        mVPriceLow.setOnClickListener(this);
        mVPrice.setOnClickListener(this);
        mVBrand.setOnClickListener(this);
        mVSize.setOnClickListener(this);
        mVColor.setOnClickListener(this);
        mVSeason.setOnClickListener(this);

        mIvClose.setOnClickListener(this);
        mBtnErrorAction.setOnClickListener(this);

        mVNew.setTag(R.string.filter_tag, 1);
        mVPriceHigh.setTag(R.string.filter_tag, 0);
        mVPriceLow.setTag(R.string.filter_tag, 0);
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        HashMap<String, String> params = new HashMap<>();
        if (!key.equals("")) {
            params = WebURLParams.getFilterParams(session, "", "", key);
        } else {
            if (isType) {
                params = WebURLParams.getFilterParams(session, "", typeId, "");
            } else {
                params = WebURLParams.getFilterParams(session, categoryId, "", "");
            }
        }
        DataLoader.loadJsonDataPost(this, WebUrls.getFiltersUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        ArrayList<FilterTypes> filterTypes = new ArrayList<FilterTypes>();
                        try {
                            filterTypes = JsonParser.json2FilterItemList(data.getJSONArray("dataArray"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showData(filterTypes);
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
                params,
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }

    private void showData(ArrayList<FilterTypes> filterTypes) {
        showViews(ViewMode.DATA);
        mFilterTypes = filterTypes;
        boolean isPrice = false;
        boolean isSize = false;
        boolean isColor = false;
        boolean isSeason = false;
        boolean isBrand = false;
        for (FilterTypes type : filterTypes) {
            if (type.getId().equals(String.valueOf(FilterTypesIds.PRICE))) {
                isPrice = true;
            } else if (type.getId().equals(String.valueOf(FilterTypesIds.SIZES))) {
                isSize = true;
            } else if (type.getId().equals(String.valueOf(FilterTypesIds.COLORS))) {
                isColor = true;
            } else if (type.getId().equals(String.valueOf(FilterTypesIds.SEASONS))) {
                isSeason = true;
            } else if (type.getId().equals(String.valueOf(FilterTypesIds.BRANDS))) {
                isBrand = true;
            }
        }
        mVPrice.setVisibility(isPrice ? View.VISIBLE : View.GONE);
        mVSize.setVisibility(isSize ? View.VISIBLE : View.GONE);
        mVColor.setVisibility(isColor ? View.VISIBLE : View.GONE);
        mVSeason.setVisibility(isSeason ? View.VISIBLE : View.GONE);
        mVBrand.setVisibility(isBrand ? View.VISIBLE : View.GONE);
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
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("filters", filterValues);
        intent.putExtra("sortBy", getSortBy());
        intent.putExtra("min", min);
        intent.putExtra("max", max);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.v_new:
                mVNew.setTag(R.string.filter_tag, 1);
                mVPriceHigh.setTag(R.string.filter_tag, 0);
                mVPriceLow.setTag(R.string.filter_tag, 0);
                mIvNew.setColorFilter(getResources().getColor(R.color.black));
                mIvPriceHigh.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceLow.setColorFilter(getResources().getColor(R.color.grey));
                mTvNew.setTextColor(getResources().getColor(R.color.black));
                mTvPriceHigh.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceLow.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.v_price_high:
                mVNew.setTag(R.string.filter_tag, 0);
                mVPriceHigh.setTag(R.string.filter_tag, 1);
                mVPriceLow.setTag(R.string.filter_tag, 0);
                mIvNew.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceHigh.setColorFilter(getResources().getColor(R.color.black));
                mIvPriceLow.setColorFilter(getResources().getColor(R.color.grey));
                mTvNew.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceHigh.setTextColor(getResources().getColor(R.color.black));
                mTvPriceLow.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.v_price_low:
                mVNew.setTag(R.string.filter_tag, 0);
                mVPriceHigh.setTag(R.string.filter_tag, 0);
                mVPriceLow.setTag(R.string.filter_tag, 1);
                mIvNew.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceHigh.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceLow.setColorFilter(getResources().getColor(R.color.black));
                mTvNew.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceHigh.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceLow.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.iv_close:
                Intent intent = new Intent();
                intent.putExtra("filters", filterValues);
                intent.putExtra("sortBy", getSortBy());
                intent.putExtra("min", min);
                intent.putExtra("max", max);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
                break;
            case R.id.v_size:
                ArrayList<KeyValue> values = getFilterByType(FilterTypesIds.SIZES).getValues();
                showDialog(values, FilterTypesIds.SIZES, mTvSize);
                break;
            case R.id.v_brand:
                values = getFilterByType(FilterTypesIds.BRANDS).getValues();
                showDialog(values, FilterTypesIds.BRANDS, mTvBrand);
                break;
            case R.id.v_season:
                values = getFilterByType(FilterTypesIds.SEASONS).getValues();
                showDialog(values, FilterTypesIds.SEASONS, mTvSeason);
                break;
            case R.id.v_color:
                values = getFilterByType(FilterTypesIds.COLORS).getColors();
                showColorDialog(values, FilterTypesIds.COLORS, mTvColor);
                break;
            case R.id.v_price:
                String min = getFilterByType(FilterTypesIds.PRICE).getMin();
                String max = getFilterByType(FilterTypesIds.PRICE).getMax();
                showRangeSelector(min, max, mTvPrice);
                break;
        }
    }

    FilterTypes getFilterByType(int type) {
        for (FilterTypes filterType : mFilterTypes) {
            if (filterType.getId().equals(String.valueOf(type))) {
                return filterType;
            }
        }
        return null;
    }

    void showDialog(ArrayList<KeyValue> values, final int filterId, final TextView textView) {
        RecyclerView mRvList;
        LinearLayoutManager mLayoutManager;
        FilterAdapter mAdapter;

        if (!values.isEmpty())
            if (values.get(0).getId().equals("-1")) {
                values.remove(0);
            }
        values.add(0, new KeyValue("-1", getString(R.string.all)));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_values, null);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);
        mAdapter = new FilterAdapter(this, values, new FilterAdapter.OnFilterItemClickListener() {
            @Override
            public void onItemClick(KeyValue value) {
                if (value.getId().equals("-1")) {
                    filterValues.remove(filterId);
                } else {
                    filterValues.put(filterId, value.getId());
                }
                textView.setText(value.getName());
                textView.setTextColor(getResources().getColor(R.color.black));
                dialog.dismiss();
            }
        });
        mRvList.setAdapter(mAdapter);
        builder.setView(view);
        dialog = builder.show();
    }

    void showColorDialog(ArrayList<KeyValue> values, final int filterId, final TextView textView) {
        RecyclerView mRvList;
        LinearLayoutManager mLayoutManager;
        ColorForFilterAdapter mAdapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_picker, null);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_color_list);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvList.setLayoutManager(mLayoutManager);
        mAdapter = new ColorForFilterAdapter(this, values, new ColorForFilterAdapter.OnColorClickListener() {
            @Override
            public void onItemClick(KeyValue keyValue) {
                filterValues.put(filterId, keyValue.getId());
                textView.setText("Color Selected");
                textView.setTextColor(getResources().getColor(R.color.black));
                dialog.dismiss();
            }
        });
        mRvList.setAdapter(mAdapter);
        builder.setView(view);
        dialog = builder.show();
    }

    void showRangeSelector(String minValue, String maxValue, final TextView textView) {
        final EditText mEtMin;
        final EditText mEtMax;
        Button mBtnGo;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ranges, null);
        mEtMax = (EditText) view.findViewById(R.id.et_max);
        mEtMin = (EditText) view.findViewById(R.id.et_min);
        mBtnGo = (Button) view.findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = mEtMin.getText().toString();
                max = mEtMax.getText().toString();
                filterValues.put(FilterTypesIds.PRICE, min);
                textView.setText("From " + min + " To " + max);
                dialog.dismiss();
            }
        });
        mEtMax.setHint(maxValue);
        mEtMin.setHint(minValue);
        builder.setView(view);
        dialog = builder.show();
    }

    int getSortBy() {
        if ((int) mVNew.getTag(R.string.filter_tag) == 1) {
            return FilterTypesIds.NEW;
        } else if ((int) mVPriceHigh.getTag(R.string.filter_tag) == 1) {
            return FilterTypesIds.HIGH_TO_LOW;
        } else if ((int) mVPriceLow.getTag(R.string.filter_tag) == 1) {
            return FilterTypesIds.LOW_TO_HIGH;
        } else {
            return FilterTypesIds.NEW;
        }
    }


}
