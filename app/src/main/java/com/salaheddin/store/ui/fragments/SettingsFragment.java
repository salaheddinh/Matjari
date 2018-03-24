package com.salaheddin.store.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.ui.activities.AuthenticationActivity;
import com.salaheddin.store.ui.activities.MyOrdersActivity;
import com.salaheddin.store.ui.activities.UserProfileActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private TextView mBtnSignIn;
    private View mVProfile;
    private View mVMyOrders;
    private final int REQUEST_CODE = 100;
    private View view;

    private String firstName;
    private String lastName;
    private View mVRatingBar;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        init(view);
        return view;
    }

    void init(final View view) {
        mBtnSignIn = (TextView) view.findViewById(R.id.btn_sign_in);
        mVProfile = view.findViewById(R.id.v_profile);
        mVMyOrders = view.findViewById(R.id.v_my_orders);
        mVRatingBar = view.findViewById(R.id.rate_bar);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AuthenticationActivity.class), REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        String session = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.SESSION, null);
        if (session == null) {
            mVProfile.setVisibility(View.GONE);
            mVMyOrders.setVisibility(View.GONE);
            mBtnSignIn.setVisibility(View.VISIBLE);
        } else {
            mVProfile.setVisibility(View.VISIBLE);
            mVMyOrders.setVisibility(View.VISIBLE);
            mBtnSignIn.setVisibility(View.GONE);
        }

        showData();

        mVMyOrders.setOnClickListener(this);
        mVProfile.setOnClickListener(this);
        mVRatingBar.setOnClickListener(this);
    }

    void showData() {
        firstName = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.FIRST_NAME, "");
        lastName = SharedPreferencesManager.readFromPreferences(getActivity(), SharedPreferencesManager.LAST_NAME, "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            init(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.v_my_orders:
                startActivity(new Intent(getActivity(), MyOrdersActivity.class));
                break;
            case R.id.v_profile:
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("name", firstName + " " + lastName);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.rate_bar:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                }catch (Exception e){
                    Toast.makeText(getActivity(), R.string.dont_have_google_play_store,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }
}
