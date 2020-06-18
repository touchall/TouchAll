package allpointech.touchall.intro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import allpointech.touchall.AppInfo;
import allpointech.touchall.R;
import allpointech.touchall.network.fcm.FcmPreferenceUtil;

/**
 * Created by daze on 2016-11-14.
 */

public class IntroActivity extends BaseAppCompatActivity {

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseInstanceId.getInstance().getToken();

        if (FirebaseInstanceId.getInstance().getToken() != null) {
            Log.d("IntroActivity", "token = " + FirebaseInstanceId.getInstance().getToken());

            int appVersion = getAppVersion(this);
            SLog.LogD("|" + "Saving regId on app version " + appVersion + "|");
            FcmPreferenceUtil.instance(this).putRedId(FirebaseInstanceId.getInstance().getToken());
            FcmPreferenceUtil.instance(this).putAppVersion(appVersion);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SLog.LogD("Ace : new intent");
//        if (AppInfo.mMemberIdx != null) {
//            AppInfo.setPendingNotificationsCount(0);
//        }
        SLog.LogD("Ace : " + intent.getStringExtra("obj"));
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        IntroFragment fragment = new IntroFragment();
        fragment.setArguments(intent.getExtras());
        ft.replace(getMainViewId(), fragment, IntroFragment.FRAGMENT_TAG);
        ft.commit();
        fm.executePendingTransactions();
    }


    @Override
    protected void initIntentData(Bundle bundle) {

    }

    @Override
    protected BaseFragment initFragment() {
        IntroFragment fragment = new IntroFragment();
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                fragment.setArguments(getIntent().getExtras());
            }
        }
        return fragment;
    }

    @Override
    protected void initRequest() {
    }

    @Override
    protected void initDefaultSet() {

    }

}
