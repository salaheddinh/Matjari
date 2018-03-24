package com.salaheddin.store.ui.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.ui.fragments.RegisterFragment;
import com.salaheddin.store.ui.fragments.SigninFragment;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener{

    private View mVLogIn;
    private View mVSignUp;
    private TextView mTvLogIn;
    private TextView mTvSignUp;
    private View mVLogInLine;
    private View mVSignUpLine;
    private View mVCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        init();
    }

    void init(){
        mVCancel = findViewById(R.id.v_cancel);
        mVLogIn = findViewById(R.id.v_login);
        mVSignUp = findViewById(R.id.v_signup);
        mVLogInLine = findViewById(R.id.v_login_line);
        mVSignUpLine = findViewById(R.id.v_signup_line);
        mTvLogIn = (TextView) findViewById(R.id.tv_login);
        mTvSignUp = (TextView) findViewById(R.id.tv_signup);
        mVLogIn.setOnClickListener(this);
        mVSignUp.setOnClickListener(this);
        mVCancel.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SigninFragment()).commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.v_login){
            mTvLogIn.setTextColor(getResources().getColor(R.color.pale_red));
            mVLogInLine.setBackgroundColor(getResources().getColor(R.color.pale_red));
            mTvSignUp.setTextColor(getResources().getColor(R.color.black));
            mVLogInLine.setVisibility(View.VISIBLE);
            mVSignUpLine.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SigninFragment()).commit();
        }else if (id == R.id.v_signup){
            mTvLogIn.setTextColor(getResources().getColor(R.color.black));
            mVLogInLine.setVisibility(View.GONE);
            mVSignUpLine.setVisibility(View.VISIBLE);
            mTvSignUp.setTextColor(getResources().getColor(R.color.pale_red));
            mVSignUpLine.setBackgroundColor(getResources().getColor(R.color.pale_red));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
        }else if(id == R.id.v_cancel){
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_up);
    }
}
