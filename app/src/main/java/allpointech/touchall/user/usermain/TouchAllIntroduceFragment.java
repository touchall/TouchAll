package allpointech.touchall.user.usermain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;

import allpointech.touchall.R;
import allpointech.touchall.custom.ViewPagerIndicator;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.user.UserMainFragment;
import allpointech.touchall.utils.TNConstants;

/**
 * Created by jay on 2018. 6. 19..
 */

public class TouchAllIntroduceFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = TouchAllIntroduceFragment.class.getSimpleName();

    private Toolbar mToolbar;


    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_touchall_introduce;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }


    @Override
    protected void initRequest() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBack();
                UserMainFragment fragment = (UserMainFragment) getFragmentManager().findFragmentByTag(UserMainFragment.FRAGMENT_TAG);
                if (fragment != null) {
                    fragment.openDrawer();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {

    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBack() {
        replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
    }

}
