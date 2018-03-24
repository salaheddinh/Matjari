package com.salaheddin.store.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.helpers.Utils;
import com.salaheddin.store.helpers.ViewMode;
import com.salaheddin.store.models.Cart;
import com.salaheddin.store.models.Offer;
import com.salaheddin.store.models.Profile;
import com.salaheddin.store.models.TelephoneNumber;
import com.salaheddin.store.network.DataLoader;
import com.salaheddin.store.network.JsonParser;
import com.salaheddin.store.network.VolleySingleton;
import com.salaheddin.store.network.WebURLParams;
import com.salaheddin.store.network.WebUrls;

import org.json.JSONObject;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, DataLoader.OnJsonDataLoadedListener {

    private final String TAG = "CheckOutActivity";
    private final String SUBMIT_ORDER = "SUBMIT_ORDER";
    private final String ORDER_OFFER = "ORDER_OFFER";

    private Toolbar mToolbar;
    private View mVAddShipping;
    private View mVEditShipping;

    private ImageView mPbLoading;
    private View mVData;
    private View mVErrorHolder;

    private TextView mTvError;
    private Button mBtnErrorAction;

    private TextView mTvName;
    private TextView mTvAddress;
    private TextView mTvNotes;
    private TextView mTvQty;
    private TextView mTvPrice;
    private ViewGroup mVPhones;
    private TextView mBtnOrder;

    private AppCompatRadioButton mRbMyAddress;
    private AppCompatRadioButton mRbNearBy;

    private Profile mProfile;
    private Cart cart;
    private Offer offer;
    private int offerQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        cart = (Cart) getIntent().getSerializableExtra("cart");
        offer = (Offer) getIntent().getSerializableExtra("offer");
        offerQuantity = getIntent().getIntExtra("offerQuantity", 0);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.checkout1));
        loadData(true);
    }

    void init() {
        mPbLoading = (ImageView) findViewById(R.id.pb_loading);
        mVData = findViewById(R.id.data);
        mVErrorHolder = findViewById(R.id.error_holder);
        Glide.with(this).load(R.raw.loading).into(mPbLoading);
        mTvError = (TextView) findViewById(R.id.tv_error);
        mBtnErrorAction = (Button) findViewById(R.id.btn_error_action);
        mBtnErrorAction.setOnClickListener(this);

        mTvName = (TextView) findViewById(R.id.tv_user_full_name);
        mTvAddress = (TextView) findViewById(R.id.tv_user_address);
        mTvNotes = (TextView) findViewById(R.id.tv_user_notes);
        mTvQty = (TextView) findViewById(R.id.tv_cart_qty);
        mTvPrice = (TextView) findViewById(R.id.tv_cart_price);
        mVPhones = (ViewGroup) findViewById(R.id.v_phones);
        mVAddShipping = findViewById(R.id.v_add_shipping);
        mVEditShipping = findViewById(R.id.v_edit_shipping);
        mBtnOrder = (TextView) findViewById(R.id.btn_order);

        mRbMyAddress = (AppCompatRadioButton) findViewById(R.id.rb_my_address);
        mRbNearBy = (AppCompatRadioButton) findViewById(R.id.rb_nearby);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mVAddShipping.setOnClickListener(this);
        mVEditShipping.setOnClickListener(this);
        mBtnOrder.setOnClickListener(this);

        mRbNearBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mRbMyAddress.setChecked(false);
                else
                    mRbMyAddress.setChecked(true);
            }
        });

        mRbMyAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mRbNearBy.setChecked(false);
                else
                    mRbNearBy.setChecked(true);
            }
        });
    }

    private void loadData(boolean showProgress) {
        if (showProgress) {
            showViews(ViewMode.PROGRESS);
        }

        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPost(this, WebUrls.getCheckOutUrl(),
                this,
                WebURLParams.getCartDetailsParams(session),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, TAG);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.v_add_shipping:
            case R.id.v_edit_shipping:
                startActivity(new Intent(this, AddShippingInfoActivity.class));
                break;
            case R.id.btn_error_action:
                showViews(ViewMode.PROGRESS);
                loadData(true);
                break;
            case R.id.btn_order:
                if (cart != null)
                    sendSubmitOrder();
                else if (offer != null)
                    buyOffer(offer,offerQuantity);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
    }

    private void showData(Profile profile) {
        showViews(ViewMode.DATA);

        if (cart != null) {
            mTvQty.setText(cart.getItemsCount());
            mTvPrice.setText(cart.getPrice() + cart.getCurrency());
        }else {
            mTvQty.setText(offerQuantity + "");
            mTvPrice.setText(offer.getOfferPrice() + cart.getCurrency());
        }

        mProfile = profile;
        mTvName.setText(profile.getFiretName() + " " + profile.getLastName());

        if (profile.getUserAddress() == null) {
            mVAddShipping.setVisibility(View.VISIBLE);
            mVEditShipping.setVisibility(View.GONE);
            mBtnOrder.setEnabled(false);
        } else {
            mTvAddress.setText(profile.getUserAddress().getName());
            if (profile.getUserAddress().getNote() != null && !profile.getUserAddress().getNote().equals("null"))
                mTvNotes.setText(profile.getUserAddress().getNote());
            if (profile.getUserAddress().getTelephoneNumbers().isEmpty()) {
                mVAddShipping.setVisibility(View.VISIBLE);
                mVEditShipping.setVisibility(View.GONE);
                mBtnOrder.setEnabled(false);
            } else {
                mBtnOrder.setEnabled(true);
                mVAddShipping.setVisibility(View.GONE);
                mVEditShipping.setVisibility(View.VISIBLE);

                mVPhones.removeAllViews();

                for (TelephoneNumber telephoneNumber : profile.getUserAddress().getTelephoneNumbers()) {
                    LayoutInflater mInflater = LayoutInflater.from(this);
                    final View view = mInflater.inflate(R.layout.item_phone_not_editable, mVPhones, false);
                    ((TextView) view.findViewById(R.id.tv_user_phone)).setText(telephoneNumber.getNumber());
                    mVPhones.addView(view, 0);
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
        VolleySingleton.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
        showError(errorCode, errorMessage, getString(R.string.error_action_retry));
    }

    @Override
    public void onJsonDataLoadingFailure(int errorId) {
        showError(errorId, getString(errorId), getString(R.string.error_action_retry));
    }

    public void sendSubmitOrder() {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");

        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getSubmitOrderUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        SharedPreferencesManager.saveToPreferences(CheckOutActivity.this, SharedPreferencesManager.CART_SIZE, String.valueOf(0));
                        Utils.showToast(CheckOutActivity.this, getString(R.string.order_submitted));
                        finish();
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
                WebURLParams.getSubmitOrderParams(session),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, SUBMIT_ORDER, new ProgressDialog(this));
    }

    private void buyOffer(final Offer offer, int quantity) {
        String session = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.SESSION, "");
        DataLoader.loadJsonDataPostWithProgress(this, WebUrls.getOrderOfferUrl(),
                new DataLoader.OnJsonDataLoadedListener() {
                    @Override
                    public void onJsonDataLoadedSuccessfully(JSONObject data) {
                        Utils.makeToast(CheckOutActivity.this, offer.getName() + " " + getString(R.string.has_been_order));
                        finish();
                    }

                    @Override
                    public void onJsonDataLoadedWithError(int errorCode, String errorMessage) {
                        Utils.makeToast(CheckOutActivity.this, errorMessage);
                    }

                    @Override
                    public void onJsonDataLoadingFailure(int errorId) {
                        Utils.makeToast(CheckOutActivity.this, getString(R.string.error_connection));
                    }
                },
                WebURLParams.getOrderOfferParams(session, offer.getId(), quantity),
                WebURLParams.getHeaders(), Request.Priority.IMMEDIATE, ORDER_OFFER, new ProgressDialog(this));
    }
}
