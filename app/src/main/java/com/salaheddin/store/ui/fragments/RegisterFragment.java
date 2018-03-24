package com.salaheddin.store.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class RegisterFragment extends Fragment implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener {

    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private EditText mEtFirstName;
    private EditText mEtLastName;
    private TextView mBtnSignIn;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private RadioGroup genderGroup;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_register, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        mEtEmail = (EditText) view.findViewById(R.id.et_user_name);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);
        mEtConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);
        mEtFirstName = (EditText) view.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) view.findViewById(R.id.et_last_name);
        mBtnSignIn = (TextView) view.findViewById(R.id.btn_sign_in);
        genderGroup = (RadioGroup) view.findViewById(R.id.grp_gender);

        mBtnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sign_in) {
            email = mEtEmail.getText().toString();
            password = mEtPassword.getText().toString();
            confirmPassword = mEtConfirmPassword.getText().toString();
            firstName = mEtFirstName.getText().toString();
            lastName = mEtLastName.getText().toString();

            int radioButtonID = genderGroup.getCheckedRadioButtonId();
            View radioButton = genderGroup.findViewById(radioButtonID);
            int idx = genderGroup.indexOfChild(radioButton);
            String gender = (idx == 0 ? "M" : "F");

            String validateError = validateData(email, password);
            if (validateError == null) {
                String url = WebUrls.getRegistrationUrl();
                HashMap<String, String> params = WebURLParams.getRegistrationParams(email, password, firstName, lastName, gender);
                DataLoader.loadJsonDataPostWithProgress(getActivity(), url, this, params, WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, "LOGIN", new ProgressDialog(getActivity()));
            } else {
                Utils.showDialog(getActivity(), validateError);
            }
        }
    }

    public String validateData(String gsm, String password) {
        if (gsm.length() == 0)
            return getResources().getString(R.string.error_empty_field);

        if (password.length() == 0)
            return getResources().getString(R.string.error_empty_field);

        return null;
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
