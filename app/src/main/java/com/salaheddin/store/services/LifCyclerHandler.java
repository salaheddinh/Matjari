package com.salaheddin.store.services;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public class LifCyclerHandler implements Application.ActivityLifecycleCallbacks{
        // I use four separate variables here. You can, of course, just use two and
        // increment/decrement them instead of using four and incrementing them all.
        private static int resumed;
        private static int paused;
        private static int started;
        private static int stopped;

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            ++resumed;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            ++paused;
            android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;
            android.util.Log.w("test", "application is visible: " + (started > stopped));
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        public static boolean isApplicationVisible() {
                boolean b = started > stopped;
                return b;
        }

        public static boolean isApplicationInForeground() {
                boolean b =resumed > paused;
                return b;
        }

}
