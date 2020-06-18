package allpointech.touchall.user.mypoint.Franchisee;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import allpointech.touchall.R;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResJuklib;
import allpointech.touchall.network.http.resource.data.ResReqPoint;
import allpointech.touchall.utils.TNPreference;

/**
 * Created by jay on 2018-07-11.
 */

public class UseMyPointCompleteFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = UseMyPointCompleteFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvStoreName;
    private TextView mTvStoreType;
    private TextView mTvUsePoint;
    private TextView mTvUsePoint1;

    CheckFranchiseeActivity mActivity;

    String mStoreName;
    String mStoreType;
    int mTypePoint;
    int mUsePoint;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mStoreName = getArguments().getString("store_name");
            mStoreType = getArguments().getString("store_type");
            mTypePoint = getArguments().getInt("type_point");
            mUsePoint = getArguments().getInt("use_point");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_use_mypoint_complete;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvStoreName = (TextView)view.findViewById(R.id.tv_use_mypoint_complete_store_name);
        mTvStoreType = (TextView)view.findViewById(R.id.tv_use_mypoint_complete_store_type);
        mTvUsePoint = (TextView) view.findViewById(R.id.tv_use_mypoint_complete_point);
        mTvUsePoint1 = (TextView) view.findViewById(R.id.tv_use_mypoint_complete_point1);

        mTvStoreName.setText(mStoreName + "에서");
        if (mTypePoint == 1) {
            mTvStoreType.setText("터치올 포인트(" + mStoreType + ")");
            mTvUsePoint.setText("포인트 " + String.valueOf(mUsePoint) + "원이");
            mTvUsePoint1.setText("정상 결제 되었습니다.");
        }
        else if (mTypePoint == 2) {
            mTvStoreType.setText("터치올 스탬프(" + mStoreType + ")");
            mTvUsePoint.setText("스탬프 " + String.valueOf(mUsePoint) + "개가");
            mTvUsePoint1.setText("정상 사용 되었습니다.");
        }
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
                onBack();
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
        //replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
        Intent returnIntent = new Intent();
        //returnIntent.putExtra("junmun",msgs.get(0).toString());
        getActivity().setResult(Activity.RESULT_OK,returnIntent);
        getActivity().finish();
    }
}
