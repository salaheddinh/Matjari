package com.salaheddin.store.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.MatjariApplication;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Profile;
import com.salaheddin.store.models.TelephoneNumber;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener {

    private static final String TAG = "AddShippingInfoActivity";
    private static final String ADD_PHONE = "ADD_PHONE";
    private static final String REMOVE_PHONE = "REMOVE_PHONE";
    private static final String ADD_ADDRESS = "ADD_ADDRESS";
    protected Toolbar mToolbar;

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private TextView mTvError;
    private Button mBtnErrorAction;

    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtNotes;

    private TextView mTvUserAddress;
    private View mVAddAddress;
    private View mVAddPhone;
    private View mVUserAddress;
    private ViewGroup mVAddress;
    private ViewGroup mVPhones;
    private TextView mBtnSave;
    private RadioGroup genderGroup;

    private Profile mProfile;
    private String address;
    private ArrayList<String> telephones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String name = getIntent().getStringExtra("name");
        setTitle(name);
        loadData(true);
    }

    void init() {
        telephones = new ArrayList<>();
        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);

        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);
        mBtnErrorAction.setOnClickListener(this);

        mVAddPhone = findViewById(R.id.v_add_phone);
        mVPhones = (ViewGroup) findViewById(R.id.v_phones);
        mVAddAddress = findViewById(R.id.v_add_address);
        mVAddress = (ViewGroup) findViewById(R.id.v_address);
        mTvUserAddress = (TextView) findViewById(R.id.tv_user_address);
        mVUserAddress = findViewById(R.id.v_user_address);

        mEtFirstName = (EditText) findViewById(R.id.et_first_name);
        mEtLastName = (EditText) findViewById(R.id.et_last_name);
        mEtNotes = (EditText) findViewById(R.id.et_user_notes);
        mBtnSave = (TextView) findViewById(R.id.btn_save);

        genderGroup = (RadioGroup) findViewById(R.id.grp_gender);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mVAddPhone.setOnClickListener(this);
        mVAddAddress.setOnClickListener(this);
        mVUserAddress.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }else if (id == R.id.action_log_out){
            showConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.v_add_phone:
                showAddPhoneDialog();
                break;
            case R.id.btn_save:
                int radioButtonID = genderGroup.getCheckedRadioButtonId();
                View radioButton = genderGroup.findViewById(radioButtonID);
                int idx = genderGroup.indexOfChild(radioButton);
                String gender = (idx == 0 ? "M" : "F");
                sendSetProfile(mEtFirstName.getText().toString(), mEtLastName.getText().toString(), gender, address, mEtNotes.getText().toString(),
                        "", "", telephones);
                break;
            case R.id.v_add_address:
            case R.id.v_user_address:
                showAddAddressDialog();
                break;
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true);
                break;
        }
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getViewProfileUrl(),
                this,
                WebURLParams.getCartDetailsParams(session),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);

    }

    private void showData(Profile profile) {
        showViews(ViewMode.DATA);

        mProfile = profile;
        mEtFirstName.setText(profile.getFiretName());
        mEtLastName.setText(profile.getLastName());

        int id = profile.getGender().equals("M") ? 0 : 1;
        if (id == 0) {
            genderGroup.check(R.id.rb_male);
        } else {
            genderGroup.check(R.id.rb_female);
        }

        if (profile.getUserAddress() == null) {
            mVAddAddress.setVisibility(View.VISIBLE);
            mVUserAddress.setVisibility(View.GONE);
            mVPhones.setVisibility(View.GONE);
        } else {
            mVAddAddress.setVisibility(View.GONE);
            mVUserAddress.setVisibility(View.VISIBLE);
            mVPhones.setVisibility(View.VISIBLE);
            address = profile.getUserAddress().getName();
            mTvUserAddress.setText(profile.getUserAddress().getName());
            if (profile.getUserAddress().getTelephoneNumbers().isEmpty()) {

            } else {
                for (TelephoneNumber telephoneNumber : profile.getUserAddress().getTelephoneNumbers()) {
                    telephones.add(telephoneNumber.getNumber());
                    LayoutInflater mInflater = LayoutInflater.from(this);
                    final View view = mInflater.inflate(R.layout.item_phone, mVPhones, false);
                    view.setTag(R.string.phone_tag, telephoneNumber.getNumber());
                    view.setOnClickListener(new removedClicked(mVPhones));
                    ((TextView) view.findViewById(R.id.tv_user_phone)).setText(telephoneNumber.getNumber());
                    mVPhones.addView(view, 1);
                }
            }
        }
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
    public void onJsonDataLoadedSuccessfully(JSONObject data) {
        Profile profile = JsonParser.json2Profile(data);
        showData(profile);
    }

    @Override
    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
    }

    @Override
    public void onJsonDataLoadingFailure(int errorId) {
        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
    }

    public void sendAddAddress(final String name, String notes, final boolean exit) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getAddEditAddressUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        mVAddAddress.setVisibility(View.GONE);
                        mVUserAddress.setVisibility(View.VISIBLE);
                        mTvUserAddress.setText(name);
                        if (exit) {
                            finish();
                        }
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.showToast(UserProfileActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.showToast(UserProfileActivity.this, getString(errorId));
                    }
                },
                WebURLParams.getAddEditAddressParams(session, name, notes, "", ""),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_ADDRESS, new ProgressDialog(this));
    }

    public void sendSetProfile(String firstName, String lastName, String gender, String address, String note, String longitude,
                               String latitude, ArrayList<String> telephones) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getSetProfileUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Profile profile = JsonParser.json2Profile(data);
                        SharedPreferencesManager.saveToPreferences(UserProfileActivity.this, SharedPreferencesManager.USER_ID, profile.getId());
                        SharedPreferencesManager.saveToPreferences(UserProfileActivity.this, SharedPreferencesManager.FIRST_NAME, profile.getFiretName());
                        SharedPreferencesManager.saveToPreferences(UserProfileActivity.this, SharedPreferencesManager.LAST_NAME, profile.getLastName());
                        finish();
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.showToast(UserProfileActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.showToast(UserProfileActivity.this, getString(errorId));
                    }
                },
                WebURLParams.getSetProfileParams(session, firstName, lastName, gender, address, note, longitude, latitude, telephones),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ADD_ADDRESS, new ProgressDialog(this));
    }

    public void showAddAddressDialog() {
        final EditText mEtAddress;
        final TextView mPositive;
        TextView mNegative;
        AlertDialog alertDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_address, null);

        mEtAddress = (EditText) view.findViewById(R.id.et_user_address);
        mPositive = (TextView) view.findViewById(R.id.tv_positive);
        mNegative = (TextView) view.findViewById(R.id.tv_negative);

        mPositive.setText(R.string.save);
        mNegative.setText(R.string.cancel);
        if (mProfile.getUserAddress() != null)
            mEtAddress.setText(mProfile.getUserAddress().getName());

        builder.setView(view);
        alertDialog = builder.create();

        final AlertDialog finalAlertDialog = alertDialog;
        mEtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    mPositive.setEnabled(true);
                else
                    mPositive.setEnabled(false);
            }
        });

        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                address = mEtAddress.getText().toString();
                mVAddAddress.setVisibility(View.GONE);
                mVUserAddress.setVisibility(View.VISIBLE);
                mTvUserAddress.setText(address);
            }
        });

        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });


        alertDialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
        alertDialog.show();
    }

    public void showAddPhoneDialog() {
        final EditText mEtPhone;
        final TextView mPositive;
        TextView mNegative;
        AlertDialog alertDialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_phone, null);

        mEtPhone = (EditText) view.findViewById(R.id.et_user_phone);
        mPositive = (TextView) view.findViewById(R.id.tv_positive);
        mNegative = (TextView) view.findViewById(R.id.tv_negative);

        mPositive.setText(R.string.save);
        mNegative.setText(R.string.cancel);

        builder.setView(view);
        alertDialog = builder.create();

        final AlertDialog finalAlertDialog = alertDialog;
        mEtPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    mPositive.setEnabled(true);
                else
                    mPositive.setEnabled(false);
            }
        });

        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                String phone = mEtPhone.getText().toString();
                LayoutInflater mInflater = LayoutInflater.from(UserProfileActivity.this);
                final View view = mInflater.inflate(R.layout.item_phone, mVPhones, false);
                ((TextView) view.findViewById(R.id.tv_user_phone)).setText(phone);
                view.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVPhones.removeView(view);
                    }
                });
                mVPhones.addView(view, 1);
                telephones.add(phone);
            }
        });

        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    public void showConfirmationDialog() {
        TextView mText;
        TextView mPositive;
        TextView mNegative;
        AlertDialog alertDialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_confirmation_dialog, null);

        mText = (TextView) view.findViewById(R.id.tv_confirmation_text);
        mPositive = (TextView) view.findViewById(R.id.tv_positive);
        mNegative = (TextView) view.findViewById(R.id.tv_negative);

        mText.setText(R.string.are_you_sure_sign_out);

        builder.setView(view);
        alertDialog = builder.create();

        final AlertDialog finalAlertDialog = alertDialog;
        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
                SharedPreferencesManager.clearPreferences(UserProfileActivity.this);
                SharedPreferencesManager.saveToPreferences(UserProfileActivity.this, SharedPreferencesManager.CART_SIZE, String.valueOf(0));
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });


        alertDialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
        alertDialog.show();
    }

    class removedClicked implements View.OnClickListener {
        ViewGroup group;

        removedClicked(ViewGroup group) {
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            String number = (String) v.getTag(R.string.phone_tag);
            telephones.remove(number);
            group.removeView(v);
        }
    }
}
