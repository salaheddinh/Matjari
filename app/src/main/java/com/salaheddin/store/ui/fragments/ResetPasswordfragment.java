package com.salaheddin.store.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.models.User;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;
import com.salaheddin.store.ui.activities.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class ResetPasswordfragment extends Fragment implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener {

    private EditText mEtPassword;
    private TextView mBtnResetPassword;
    private String code;
    private String email;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        email = getArguments().getString("email");
        code = getArguments().getString("code");
        init(view);
        return view;
    }

    private void init(View view) {
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mBtnResetPassword = (TextView) view.findViewById(R.id.btn_reset_password);
        mBtnResetPassword.setOnClickListener(null);
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0){
                    mBtnResetPassword.setOnClickListener(ResetPasswordfragment.this);
                    mBtnResetPassword.setAlpha(1);
                }else {
                    mBtnResetPassword.setOnClickListener(null);
                    mBtnResetPassword.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reset_password) {
            password = mEtPassword.getText().toString();

            String url = WebUrls.getResetPasswordUrl();
            HashMap<String, String> params = WebURLParams.getResetPasswordParams(email, password, code);
            DataLoader.loadJsonDataPostWithProgress(getActivity(), url, this, params, WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, "LOGIN", new ProgressDialog(getActivity()));
        }
    }

    @Override
    public void onJsonDataLoadedSuccessfully(JSONObject data) {
        User user = JsonParser.json2User(data);
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.HASH_KEY, user.getHashKey());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.SESSION, user.getSession());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.USER_ID, user.getId());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.FIRST_NAME, user.getFirstName());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.LAST_NAME, user.getLastName());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.RELATED_STORE_ID, user.getRelatedStoreId());
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.IS_ADMIN, String.valueOf(user.isAdmin()));
        SharedPreferencesManager.saveToPreferences(getActivity(), SharedPreferencesManager.CART_SIZE, String.valueOf(user.getCartItems()));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
        Utils.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onJsonDataLoadingFailure(int errorId) {
        Utils.showToast(getActivity(), getResources().getString(R.string.error_connection));
    }
}
