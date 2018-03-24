package com.salaheddin.store.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.Request;
import com.salaheddin.store.R;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Salaheddin on 2/2/2018.
 */

public class ForgetPaswordFragment extends Fragment implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener {
    private EditText mEtEmail;
    private String email;
    private TextView mBtnSubmit;
    private TextView mBtnCheck;
    private View mVCodes;
    private String code;
    private PinEntryEditText pin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        init(view);
        return view;
    }

    void init(View view) {
        code = "";
        mEtEmail = (EditText) view.findViewById(R.id.et_user_name);
        mBtnSubmit = (TextView) view.findViewById(R.id.btn_send_code);
        mBtnCheck = (TextView) view.findViewById(R.id.btn_check_code);
        mVCodes = view.findViewById(R.id.v_codes);
        pin = (PinEntryEditText) view.findViewById(R.id.et_verification_code);

        mBtnSubmit.setOnClickListener(null);
        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.getText().toString().equals(code))
                {
                    ResetPasswordfragment resetPasswordfragment = new ResetPasswordfragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("email",email);
                    bundle.putString("code",code);
                    resetPasswordfragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, resetPasswordfragment).commit();
                }
                else{
                    Toast.makeText(getActivity(),R.string.wrong_code,Toast.LENGTH_LONG).show();
                }
            }
        });

        mEtEmail.setText(email);
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0){
                    mBtnSubmit.setOnClickListener(ForgetPaswordFragment.this);
                    mBtnSubmit.setAlpha(1);
                }else {
                    mBtnSubmit.setOnClickListener(null);
                    mBtnSubmit.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_code) {
            email = mEtEmail.getText().toString();
            String validateError = validateData(email);
            if (validateError == null) {
                // TODO pass correct notification id
                String url = WebUrls.getForgetPasswordUrl();
                HashMap<String, String> params = WebURLParams.getForgetPasswordParams(email);
                DataLoader.loadJsonDataPostWithProgress(getActivity(), url, this, params, WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, "LOGIN", new ProgressDialog(getActivity()));
            } else {
                Utils.showDialog(getActivity(), validateError);
            }
        }
    }

    public String validateData(String gsm) {
        if (gsm.length() == 0)
            return getResources().getString(R.string.error_empty_field);

        return null;
    }

    @Override
    public void onJsonDataLoadedSuccessfully(JSONObject data) {
        try {
            code = data.getString("verificationCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVCodes.setVisibility(View.VISIBLE);
        mEtEmail.setEnabled(false);
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
