package com.salaheddin.store.ui.fragments;


import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.helpers.FilterTypesIds;
import com.salaheddin.store.models.KeyValue;
import com.salaheddin.store.ui.activities.CategoryProductsActivity;
import com.salaheddin.store.ui.adapters.ColorForFilterAdapter;
import com.salaheddin.store.ui.adapters.FilterAdapter;

import java.util.ArrayList;

public class FilterValuesFragment extends Fragment {

    private RecyclerView mRvList;
    private LinearLayoutManager mLayoutManager;
    private FilterAdapter mAdapter;
    private ColorForFilterAdapter mAdapterColor;
    private ArrayList<KeyValue> values;
    private ImageView mBackArrow;
    private int filterId;
    private TextView mTvSelect;

    public FilterValuesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            values = (ArrayList<KeyValue>) getArguments().getSerializable("values");
            filterId = getArguments().getInt("filterId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_values, container, false);
        init(view);
        switch (filterId) {
            case FilterTypesIds.BRANDS:
                mTvSelect.setText(getActivity().getString(R.string.select) + " " +
                        getActivity().getString(R.string.brand));
                break;
            case FilterTypesIds.SIZES:
                mTvSelect.setText(getActivity().getString(R.string.select) + " " +
                        getActivity().getString(R.string.size));
                break;
            case FilterTypesIds.SEASONS:
                mTvSelect.setText(getActivity().getString(R.string.select) + " " +
                        getActivity().getString(R.string.season));
                break;
        }
        return view;
    }

    void init(View view) {
        mBackArrow = (ImageView) view.findViewById(R.id.iv_back_arrow);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvList.setLayoutManager(mLayoutManager);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        mBackArrow.setImageDrawable(upArrow);

        if (!values.isEmpty())
            if (values.get(0).getId().equals("-1")) {
                values.remove(0);
            }
        values.add(0, new KeyValue("-1", "Any"));

        if (filterId == FilterTypesIds.COLORS) {
            mAdapterColor = new ColorForFilterAdapter(getActivity(), values, new ColorForFilterAdapter.OnColorClickListener() {
                @Override
                public void onItemClick(KeyValue keyValue) {
                    if (keyValue.getId().equals("-1")) {
                        CategoryProductsActivity.filterValues.remove(filterId);
                        FilterDrawerFragment.mTvColor.setVisibility(View.VISIBLE);
                        FilterDrawerFragment.mIvColor.setVisibility(View.GONE);
                    } else {
                        CategoryProductsActivity.filterValues.put(filterId, keyValue.getId());
                        FilterDrawerFragment.mTvColor.setVisibility(View.GONE);
                        FilterDrawerFragment.mIvColor.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(keyValue.getName()).into(FilterDrawerFragment.mIvColor);
                    }

                    getActivity().getSupportFragmentManager().beginTransaction().remove(FilterValuesFragment.this).commit();
                }
            });
            mRvList.setAdapter(mAdapterColor);
        } else {
            mAdapter = new FilterAdapter(getActivity(), values, new FilterAdapter.OnFilterItemClickListener() {
                @Override
                public void onItemClick(KeyValue value) {
                    if (value.getId().equals("-1")) {
                        CategoryProductsActivity.filterValues.remove(filterId);
                    } else {
                        CategoryProductsActivity.filterValues.put(filterId, value.getId());
                    }
                    switch (filterId) {
                        case FilterTypesIds.BRANDS:
                            FilterDrawerFragment.mTvBrand.setText(value.getName());
                            break;
                        case FilterTypesIds.SIZES:
                            FilterDrawerFragment.mTvSize.setText(value.getName());
                            break;
                        case FilterTypesIds.SEASONS:
                            FilterDrawerFragment.mTvSeason.setText(value.getName());
                            break;
                    }
                    getActivity().getSupportFragmentManager().beginTransaction().remove(FilterValuesFragment.this).commit();
                }
            });
            mRvList.setAdapter(mAdapter);
        }


        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FilterValuesFragment.this).commit();
            }
        });

        mTvSelect = (TextView) view.findViewById(R.id.tv_select);

    }
}
