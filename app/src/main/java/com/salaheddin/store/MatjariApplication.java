package com.salaheddin.store;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.salaheddin.store.data.SharedPreferencesManager;
import com.salaheddin.store.services.LifCyclerHandler;

import java.util.UUID;

public class MatjariApplication extends Application {
    public static Intent intent;
    public static String APP_VERSION;
    public static String DEVICE_ID;
    public static int floatingViewX;
    public static int floatingViewY;

    private static MatjariApplication sInstance;

    public static synchronized MatjariApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        APP_VERSION = getAppVersion();
        DEVICE_ID = getUniqueDeviceID();
        registerActivityLifecycleCallbacks(new LifCyclerHandler());
        floatingViewX = 0;
        floatingViewY = 50;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    private String getAppVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info;
        String version;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "1";
        }
        return version;
    }

    private static String getUniqueDeviceID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) +
                (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Return the serial for api >= 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // Default value
            serial = Settings.Secure.ANDROID_ID;
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public String getLangString() {
        String lang = SharedPreferencesManager.readFromPreferences(this, SharedPreferencesManager.KEY_LANGUAGE, "en");
        return lang;
    }
}
