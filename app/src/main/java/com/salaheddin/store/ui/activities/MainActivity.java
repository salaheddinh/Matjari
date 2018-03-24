package com.salaheddin.store.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salaheddin.store.R;
import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.ui.fragments.CartFragment;
import com.salaheddin.store.ui.fragments.HomeFragment;
import com.salaheddin.store.ui.fragments.AllCategoriesFragment;
import com.salaheddin.store.ui.fragments.SettingsFragment;
import com.salaheddin.store.ui.fragments.WishListFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView mTvTitle;
    private ImageView mIvSearch;
    private ImageView mIvLogo;
    private View toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String size = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.CART_SIZE, "0");
        addBadge(size);
    }

    void init() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvLogo = (ImageView) findViewById(R.id.iv_logo);
        toolBar = findViewById(R.id.tool_bar);

        String size = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.CART_SIZE, "0");

        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        addBadge(size);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                if (!(currentFragment instanceof HomeFragment)) {
                                    toolBar.setVisibility(View.GONE);
                                    mIvSearch.setVisibility(View.VISIBLE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                                    mIvLogo.setVisibility(View.VISIBLE);
                                    mTvTitle.setVisibility(View.GONE);
                                }
                                break;
                            case R.id.action_categories:
                                if (!(currentFragment instanceof AllCategoriesFragment)) {
                                    toolBar.setVisibility(View.VISIBLE);
                                    mIvSearch.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new AllCategoriesFragment()).commit();
                                    mIvLogo.setVisibility(View.GONE);
                                    mTvTitle.setVisibility(View.VISIBLE);
                                    mTvTitle.setText(R.string.categories);
                                }
                                break;
                            case R.id.action_favorite:
                                if (!(currentFragment instanceof WishListFragment)) {
                                    toolBar.setVisibility(View.VISIBLE);
                                    mIvSearch.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new WishListFragment()).commit();
                                    mIvLogo.setVisibility(View.GONE);
                                    mTvTitle.setVisibility(View.VISIBLE);
                                    mTvTitle.setText(R.string.wishlist);
                                }
                                break;
                            case R.id.action_cart:
                                if (!(currentFragment instanceof CartFragment)) {
                                    toolBar.setVisibility(View.VISIBLE);
                                    mIvSearch.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new CartFragment()).commit();
                                    mIvLogo.setVisibility(View.GONE);
                                    mTvTitle.setVisibility(View.VISIBLE);
                                    mTvTitle.setText(R.string.cart);
                                    break;
                                }
                            case R.id.action_settings:
                                if (!(currentFragment instanceof SettingsFragment)) {
                                    toolBar.setVisibility(View.VISIBLE);
                                    mIvSearch.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
                                    mIvLogo.setVisibility(View.GONE);
                                    mTvTitle.setVisibility(View.VISIBLE);
                                    mTvTitle.setText(R.string.user);
                                }
                                break;
                        }
                        return false;
                    }
                });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

    }

    private void addBadge(String size) {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);
        ((TextView) badge.findViewById(R.id.notification_badge)).setText(size);
        if (Integer.parseInt(size) > 0) {
            itemView.addView(badge);
            badge.setVisibility(View.VISIBLE);
        }else {
            badge.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_CANCELED) {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }
}
