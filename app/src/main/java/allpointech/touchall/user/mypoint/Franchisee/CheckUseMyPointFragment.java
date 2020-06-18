package allpointech.touchall.user.mypoint.Franchisee;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;

import allpointech.touchall.R;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.utils.TNConstants;
import allpointech.touchall.webview.WebViewFragment;

/**
 * Created by jay on 2018-10-11.
 */

public class CheckUseMyPointFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener {
    public static final String FRAGMENT_TAG = CheckUseMyPointFragment.class.getSimpleName();

    private String mJunmun;
    private String mStoreId;
    //private String mDeviceSerial;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mJunmun = getArguments().getString("device_serial");
            mStoreId = getArguments().getString("store_id");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_check_use_mypoint;
    }

    @Override
    protected void initLayout(View view) {

    }

    @Override
    protected void initRequest() {
        //CheckFranchiseeFragment fragment = new CheckFranchiseeFragment();

        if (mJunmun != null)
            gotoFranchisee(mJunmun);
        else if (mStoreId != null)
            gotoUse1(1, mStoreId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBack() {
        //replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
        getActivity().finish();
    }

    public void gotoFranchisee(String junmun) {
//        Bundle b = new Bundle();
//        BaseFragment fragment = new UseMyPointFragment();
//        String TAG = UseMyPointFragment.FRAGMENT_TAG;
//        b.putString("device_serial", mDeviceSerial);
//        b.putString("store_type", storeType);
//        b.putInt("action", action);
//        b.putString("obj_message", obj.toString());
//        fragment.setArguments(b);

//        mDeviceSerial = deviceSerial;

        Bundle b = new Bundle();
        BaseFragment fragment = new CheckFranchiseeFragment();
        String TAG = CheckFranchiseeFragment.FRAGMENT_TAG;
        b.putString("device_serial", junmun);
        fragment.setArguments(b);

        replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
    }

    public void gotoUse(int action, String storeType, JSONObject obj) {

        String device_serial = JSONParser.getString(obj, "serial");

        Bundle b = new Bundle();
        BaseFragment fragment = new UseMyPointFragment();
        String TAG = UseMyPointFragment.FRAGMENT_TAG;
        b.putString("device_serial", device_serial);
        b.putString("store_type", storeType);
        b.putInt("action", action);
        b.putString("obj_message", obj.toString());
        fragment.setArguments(b);

        replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
    }

    public void gotoUse1(int action, String storeType) {

        //String device_serial = JSONParser.getString(obj, "serial");

        Bundle b = new Bundle();
        BaseFragment fragment = new UseMyPointFragment();
        String TAG = UseMyPointFragment.FRAGMENT_TAG;
        //b.putString("device_serial", device_serial);
        b.putString("store_type", storeType);
        b.putInt("action", action);
        b.putString("obj_message", "");
        fragment.setArguments(b);

        replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
    }

    public void gotoUseComplete(String storeName, String storeType, int typePoint, int usePoint) {

        //String device_serial = JSONParser.getString(obj, "serial");

        Bundle b = new Bundle();
        BaseFragment fragment = new UseMyPointCompleteFragment();
        String TAG = UseMyPointCompleteFragment.FRAGMENT_TAG;
        b.putString("store_name", storeName);
        b.putString("store_type", storeType);
        b.putInt("type_point", typePoint);
        b.putInt("use_point", usePoint);
        fragment.setArguments(b);

        replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
    }
}
