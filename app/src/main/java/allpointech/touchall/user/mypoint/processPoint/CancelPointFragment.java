package allpointech.touchall.user.mypoint.processPoint;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import allpointech.touchall.R;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResJuklib;
import allpointech.touchall.network.http.resource.data.ResReqTelegram;
import allpointech.touchall.utils.TNPreference;

/**
 * Created by jay on 2018. 6. 27..
 */

public class CancelPointFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = CancelPointFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvCancelPointGuide;
    private TextView mTvCancelPointResult;

    private String mJunmun;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mJunmun = getArguments().getString("cancel_point");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_cancel_point;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvCancelPointGuide = (TextView) view.findViewById(R.id.tv_cancel_point_franchisee);
        mTvCancelPointResult = (TextView) view.findViewById(R.id.tv_cancel_point_result);
    }

    private void requestCancelPoint() {

        ResReqTelegram res = new ResReqTelegram();
        res.setToken(TNPreference.getMemToken(getActivity()));
        res.setParameterMobile(TNPreference.getMemphoneNumber(getActivity()));
        res.setParameterTelegram(mJunmun.substring(1));
        res.setParameterRps("N");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
        requestCancelPoint();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {

        if (o[0] instanceof ResReqTelegram) {
            ResReqTelegram res = (ResReqTelegram) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                JSONObject obj_message = JSONParser.getObject(obj, ResJuklib.KEY_RES.message);
                if (obj_message != null) {
                    JSONObject receipt = JSONParser.getObject(obj_message, ResJuklib.KEY_RES.receipt);
                    JSONObject mall = JSONParser.getObject(obj_message, ResJuklib.KEY_RES.mall);
                    int rps = JSONParser.getInt(obj_message, ResJuklib.KEY_RES.rps, 0);
                    int point = JSONParser.getInt(obj_message, ResJuklib.KEY_RES.point, 0);

                    if (receipt != null) {
                        String approval = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.approval);
                        String type = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.type);
                        int amount = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.amount, 0);
                        int supply = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.supply, 0);
                        int vat = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.vat, 0);
                        String payType = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payType);
                        String payDate = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payDate);
                        String payTypeName = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payTypeName);
                    }

                    if (mall != null) {
                        String name = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.name);
                        String earn = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.earn);
                        String rpsUse = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.rpsUse);
                        String ssn = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.ssn);
                        String address = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.address);
                        String owner = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.owner);
                        String leaflet = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.leaflet);

                        mTvCancelPointGuide.setText("감사합니다.   " +  name + "\n에서 제공한 터치올 포인트(" + earn +")");
                        mTvCancelPointResult.setText(point + " 포인트 취소 완료\n");
                    }
                }
            }
        }
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
        getActivity().finish();
    }

    public void cancelPoint(String cancel_junmun) {
        mJunmun = cancel_junmun;
        requestCancelPoint();
    }
}
