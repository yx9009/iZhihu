package com.gracecode.iZhihu.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.gracecode.iZhihu.R;
import com.gracecode.iZhihu.Util;

public abstract class BaseActivity extends Activity {
    private static final String FLURRY_KEY = "T25PQCHWSVRS9RGQSR46";
    protected static ActionBar actionBar;
    protected static Context context;
    protected static SharedPreferences sharedPreferences;
    protected static PackageInfo packageInfo;

    public BaseActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // setTheme(android.R.style.Theme_Holo_Light);
    }

    @Override
    public void onStart() {
        super.onStart();

        Boolean openAnalytics = sharedPreferences.getBoolean(getString(R.string.key_analytics), true);
        if (openAnalytics) {
            FlurryAgent.onStartSession(context, FLURRY_KEY);
        }
    }

    public void onStop() {
        FlurryAgent.onEndSession(context);
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_preference:
                intent = new Intent(this, Preference.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_feedback:
                String subject =
                    String.format(getString(R.string.feedback_title), getString(R.string.app_name), packageInfo.versionName);

                Util.sendMail(this, new String[]{getString(R.string.author_email)}, subject, null);
                return true;

            case R.id.menu_about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
