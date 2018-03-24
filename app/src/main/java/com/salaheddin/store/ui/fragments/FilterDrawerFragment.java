package com.salaheddin.store.ui.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.FilterTypesIds;
import com.salaheddin.store.models.FilterTypes;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.activities.CategoryProductsActivity;
import com.salaheddin.store.ui.adapters.ColorForFilterAdapter;
import com.salaheddin.store.ui.adapters.FilterAdapter;
import com.salaheddin.store.ui.widgets.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterDrawerFragment extends Fragment implements View.OnClickListener{

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

    private TextView mTvNew;
    private TextView mTvPriceHigh;
    private TextView mTvPriceLow;
    private TextView mTvPrice;
    public static TextView mTvBrand;
    public static TextView mTvColor;
    public static TextView mTvSize;
    public static TextView mTvSeason;
    public static CircleImageView mIvColor;

    private TextView mTvDone;
    private TextView mTvReset;

    public EditText mEtMinPrice;
    public EditText mEtMaxPrice;

    private String categoryId;
    private String typeId;
    private boolean isType;
    private ArrayList<FilterTypes> mFilterTypes;
    private AlertDialog dialog;
    private String key;

    public FilterDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            typeId = getArguments().getString("typeId");
            key = getArguments().getString("key");
            if (key == null || key.toLowerCase().equals("null")) {
                key = "";
            }
            isType = getArguments().getBoolean("isType", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_drawer, container, false);
        init(view);
        loadData();
        return view;
    }

    void init(View view) {
        CategoryProductsActivity.filterValues = new HashMap<>();

        mVNew = view.findViewById(R.id.v_new);
        mVPriceHigh = view.findViewById(R.id.v_price_high);
        mVPriceLow = view.findViewById(R.id.v_price_low);
        mVPrice = view.findViewById(R.id.v_price);
        mVBrand = view.findViewById(R.id.v_brand);
        mVSize = view.findViewById(R.id.v_size);
        mVColor = view.findViewById(R.id.v_color);
        mVSeason = view.findViewById(R.id.v_season);

        mIvNew = (ImageView) view.findViewById(R.id.iv_new);
        mIvPriceHigh = (ImageView) view.findViewById(R.id.iv_price_high);
        mIvPriceLow = (ImageView) view.findViewById(R.id.iv_price_low);

        mTvNew = (TextView) view.findViewById(R.id.tv_new);
        mTvPriceHigh = (TextView) view.findViewById(R.id.tv_price_high);
        mTvPriceLow = (TextView) view.findViewById(R.id.tv_price_low);
        mTvPrice = (TextView) view.findViewById(R.id.tv_price);
        mTvBrand = (TextView) view.findViewById(R.id.tv_brand);
        mTvSize = (TextView) view.findViewById(R.id.tv_size);
        mTvColor = (TextView) view.findViewById(R.id.tv_color);
        mTvSeason = (TextView) view.findViewById(R.id.tv_season);
        mIvColor = (CircleImageView) view.findViewById(R.id.iv_color);

        mTvDone = (TextView) view.findViewById(R.id.tv_done);
        mTvReset = (TextView) view.findViewById(R.id.tv_reset);

        mEtMinPrice = (EditText) view.findViewById(R.id.et_min_price);
        mEtMaxPrice = (EditText) view.findViewById(R.id.et_max_price);

        mVNew.setOnClickListener(this);
        mVPriceHigh.setOnClickListener(this);
        mVPriceLow.setOnClickListener(this);
        mVBrand.setOnClickListener(this);
        mVSize.setOnClickListener(this);
        mVColor.setOnClickListener(this);
        mVSeason.setOnClickListener(this);

        mVBrand.setEnabled(false);
        mVSize.setEnabled(false);
        mVColor.setEnabled(false);
        mVSeason.setEnabled(false);

        mTvDone.setOnClickListener(this);
        mTvReset.setOnClickListener(this);

        mVNew.setTag(R.string.filter_tag, 1);
        mVPriceHigh.setTag(R.string.filter_tag, 0);
        mVPriceLow.setTag(R.string.filter_tag, 0);
    }

    private void loadData() {
        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, "");

        HashMap<String, String> params;
        if (!key.equals("")) {
            params = WebURLParams.getFilterParams(session, "", "", key);
        } else {
            if (isType) {
                params = WebURLParams.getFilterParams(session, "", typeId, "");
            } else {
                params = WebURLParams.getFilterParams(session, categoryId, "", "");
            }
        }
        DataLoader.loadJsonDataPost(getActivity(), WebUrls.getFiltersUrl(),
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
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                    }
                },
                params,
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, "");
    }

    private void showData(ArrayList<FilterTypes> filterTypes) {
        mVBrand.setEnabled(true);
        mVSize.setEnabled(true);
        mVColor.setEnabled(true);
        mVSeason.setEnabled(true);

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.v_new:
                mVNew.setTag(R.string.filter_tag, 1);
                mVPriceHigh.setTag(R.string.filter_tag, 0);
                mVPriceLow.setTag(R.string.filter_tag, 0);
                CategoryProductsActivity.sortBy = FilterTypesIds.NEW;
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
                CategoryProductsActivity.sortBy = FilterTypesIds.HIGH_TO_LOW;
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
                CategoryProductsActivity.sortBy = FilterTypesIds.LOW_TO_HIGH;
                mIvNew.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceHigh.setColorFilter(getResources().getColor(R.color.grey));
                mIvPriceLow.setColorFilter(getResources().getColor(R.color.black));
                mTvNew.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceHigh.setTextColor(getResources().getColor(R.color.grey));
                mTvPriceLow.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.iv_close:
                Intent intent = new Intent();
                intent.putExtra("filters", CategoryProductsActivity.filterValues);
                intent.putExtra("sortBy", getSortBy());
                intent.putExtra("min", CategoryProductsActivity.minPrice);
                intent.putExtra("max", CategoryProductsActivity.maxPrice);
                break;
            case R.id.v_size:
                ArrayList<KeyValue> values = getFilterByType(FilterTypesIds.SIZES).getValues();
                FilterValuesFragment fragment = new FilterValuesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("values",values);
                bundle.putInt("filterId",FilterTypesIds.SIZES);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,
                        fragment).commit();
                break;
            case R.id.v_brand:
                values = getFilterByType(FilterTypesIds.BRANDS).getValues();
                fragment = new FilterValuesFragment();
                bundle = new Bundle();
                bundle.putSerializable("values",values);
                bundle.putInt("filterId",FilterTypesIds.BRANDS);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,
                        fragment).commit();
                break;
            case R.id.v_season:
                values = getFilterByType(FilterTypesIds.SEASONS).getValues();
                fragment = new FilterValuesFragment();
                bundle = new Bundle();
                bundle.putSerializable("values",values);
                bundle.putInt("filterId",FilterTypesIds.SEASONS);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,
                        fragment).commit();
                break;
            case R.id.v_color:
                values = getFilterByType(FilterTypesIds.COLORS).getColors();
                fragment = new FilterValuesFragment();
                bundle = new Bundle();
                bundle.putSerializable("values",values);
                bundle.putInt("filterId",FilterTypesIds.COLORS);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,
                        fragment).commit();
                break;
            case R.id.v_price:
                String min = getFilterByType(FilterTypesIds.PRICE).getMin();
                String max = getFilterByType(FilterTypesIds.PRICE).getMax();
                showRangeSelector(min, max, mTvPrice);
                break;
            case R.id.tv_done:
                if (!mEtMaxPrice.getText().toString().isEmpty() ||
                        !mEtMinPrice.getText().toString().isEmpty())
                CategoryProductsActivity.filterValues.put(FilterTypesIds.PRICE, "");
                ((CategoryProductsActivity)getActivity()).loadData(true,false);
                CategoryProductsActivity.drawer.closeDrawer(GravityCompat.END);
                break;
            case R.id.tv_reset:

                break;
        }
    }

    public FilterTypes getFilterByType(int type) {
        if (mFilterTypes == null){
            mFilterTypes = new ArrayList<>();
        }
        for (FilterTypes filterType : mFilterTypes) {
            if (filterType.getId().equals(String.valueOf(type))) {
                return filterType;
            }
        }
        return null;
    }

    void showRangeSelector(String minValue, String maxValue, final TextView textView) {
        final EditText mEtMin;
        final EditText mEtMax;
        Button mBtnGo;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ranges, null);
        mEtMax = (EditText) view.findViewById(R.id.et_max);
        mEtMin = (EditText) view.findViewById(R.id.et_min);
        mBtnGo = (Button) view.findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryProductsActivity.minPrice = mEtMin.getText().toString();
                CategoryProductsActivity.maxPrice = mEtMax.getText().toString();
                CategoryProductsActivity.filterValues.put(FilterTypesIds.PRICE, CategoryProductsActivity.minPrice);
                textView.setText("From " + CategoryProductsActivity.minPrice + " To " + CategoryProductsActivity.maxPrice);
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
