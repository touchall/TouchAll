package allpointech.touchall.user.mypoint.Franchisee;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import allpointech.touchall.R;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResReqPoint;
import allpointech.touchall.network.http.resource.data.ResUseMyPointG;
import allpointech.touchall.network.http.resource.data.ResUseMyPointS;
import allpointech.touchall.network.http.resource.data.ResUseMyPointT;
import allpointech.touchall.network.http.resource.data.ResUsePoint;
import allpointech.touchall.network.http.resource.data.ResUsePointJunmun;
import allpointech.touchall.user.UserMainActivity;
import allpointech.touchall.user.UserMainFragment;
import allpointech.touchall.user.usermain.UserMainViewFragment;
import allpointech.touchall.utils.TNPreference;
import allpointech.touchall.utils.aria.Aria;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by jay on 2018-07-11.
 */

public class UseMyPointFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, CheckFranchiseeActivity.OnNfcCompleteListener {
    public static final String FRAGMENT_TAG = UseMyPointFragment.class.getSimpleName();

    private Toolbar mToolbar;

    //private TextView mTvStoreKind1;
    //private TextView mTvStoreKind2;
    private LinearLayout mLiTotalPoint;
    private LinearLayout mLiGroupPoint;
    private LinearLayout mLiSinglePoint;

    private TextView mTvStoreName;
//    private TextView mTvTempPoint;


    private TextView mTvValidPointTitle;
    private TextView mTvValidPoint;
    private TextView mTvUsePointTitle;
    private EditText mEtUseMyPoint;

    private TextView mTvValidGroupPointTitle;
    private TextView mTvValidGroupPoint;
    private TextView mTvUseGroupPointTitle;
    private EditText mEtUseGroupMyPoint;


    private TextView mTvValidSinglePointTitle;
    private TextView mTvValidSinglePoint;
    private TextView mTvUseSinglePointTitle;
    private EditText mEtUseSingleMyPoint;
    //private TextView mBtnUseMyPointNFC;
    private ToggleButton mBtnUseMyPointNFC;

    private ImageView mIvNfcTag;

    CheckFranchiseeActivity mActivity;

    private int action;
    private String mStoreType;
    private JSONObject obj_message;

 //   int pointTotalTotal = 0;
 //   int pointTotalCanuse = 0;
    int pointTotalCanuse = 0;

//    int stempTotalTotal = 0;
//    int stempTotalCanuse = 0;
    int stempTotalCanuse = 0;

//    int gameTotalTotal = 0;
//    int gameTotalCanuse = 0;
    int gameTotalCanuse = 0;

    int pointGroupCanuse = 0;
    int stempGroupCanuse = 0;
    int gameGroupCanuse = 0;

    int pointSingleCanuse = 0;
    int stempSingleCanuse = 0;
    int gameSingleCanuse = 0;

    private String mDeviceSerial;
    private String mUsePointType;
    private String mUsePoint;
    private String mCertification;
    private String mTelegram;

    private boolean _bTest = false;
    private boolean _bTestAria = true;

    private final int SEND_NFC_ON = 1;
    private final int SEND_NFC_OFF = 2;

    // Handler 클래스
    Handler mMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SEND_NFC_ON:
                {
                    mEtUseMyPoint.setEnabled(false);

                    mActivity._enableNdefExchangeMode("user_point_use=" + mTelegram);
                    mActivity._enableTagWriteMode();

                    mIvNfcTag.setVisibility(View.VISIBLE);
                }
                    break;

                case SEND_NFC_OFF:
                {
                    mEtUseMyPoint.setEnabled(true);
                    mActivity._disableNdef();
                    mIvNfcTag.setVisibility(View.GONE);
                }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            try {
                mDeviceSerial = getArguments().getString("device_serial");
                mStoreType = getArguments().getString("store_type");
                action = getArguments().getInt("action");
                obj_message = new JSONObject(getArguments().getString("obj_message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUID() {

        String serial_id;

        final String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        serial_id = androidId;
        serial_id += System.currentTimeMillis();
        serial_id = serial_id.replaceAll("[^0-9]", "");

//        serial_id = "235623434";

        if (serial_id.length() > 17)
            serial_id = serial_id.substring(0, 17);
        else {
            int DIGITS = 17;
            StringBuilder sb = new StringBuilder(DIGITS);
            for (int i = 0; i < DIGITS; i++) {
                //sb.append((char) (Math.random() * 10 + '0'));
                if (i >= DIGITS - serial_id.length()) {
                    //    sb.append(serial_id.charAt(i-serial_id.length()));
                    break;
                } else
                    sb.append('0');
            }
            serial_id = sb.toString() + serial_id;
        }

        return serial_id;
//
//        if (serial_id.length() > 17)
//            return serial_id.substring(0, 17);
//        else {
//            int DIGITS = 17;
//            StringBuilder sb = new StringBuilder(DIGITS);
//            for(int i = 0;i < DIGITS;i++) {
//                //sb.append((char) (Math.random() * 10 + '0'));
//
//            }
//            return sb.toString();
//        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_use_mypoint;
    }

    @Override
    protected void initLayout(View view) {

        mActivity = (CheckFranchiseeActivity) getActivity();
        mActivity.setOnNfcCompleteListener(this);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvStoreName = (TextView) view.findViewById(R.id.tv_use_mypoint_store);

        //mTvStoreKind1 = (TextView) view.findViewById(R.id.tv_use_mypoint_kind1);
        //mTvStoreKind2 = (TextView) view.findViewById(R.id.tv_use_mypoint_kind2);
        mLiTotalPoint = (LinearLayout) view.findViewById(R.id.li_usepoint_total_point);
        mLiGroupPoint = (LinearLayout) view.findViewById(R.id.li_usepoint_group_point);
        mLiSinglePoint = (LinearLayout) view.findViewById(R.id.li_usepoint_single_point);

        //mTvTempPoint = (TextView)view.findViewById(R.id.tv_use_mypoint_valid);
        mTvValidPointTitle = (TextView) view.findViewById(R.id.tv_use_mypoint_valid_title);
        mTvValidPoint = (TextView) view.findViewById(R.id.tv_use_mypoint_valid);
        mTvUsePointTitle = (TextView) view.findViewById(R.id.et_use_mypoint_use_title);
        mEtUseMyPoint = (EditText) view.findViewById(R.id.et_use_mypoint_use);
        //mBtnUseMyPointNFC = (TextView)view.findViewById(R.id.btn_use_mypoint_nfc);
        //mBtnUseMyPointNFC.setOnClickListener(this);

        mTvValidGroupPointTitle = (TextView) view.findViewById(R.id.tv_use_group_mypoint_valid_title);
        mTvValidGroupPoint = (TextView) view.findViewById(R.id.tv_use_group_mypoint_valid);
        mTvUseGroupPointTitle = (TextView) view.findViewById(R.id.et_use_group_mypoint_use_title);
        mEtUseGroupMyPoint = (EditText) view.findViewById(R.id.et_use_group_mypoint_use);

        mTvValidSinglePointTitle = (TextView) view.findViewById(R.id.tv_use_single_mypoint_valid_title);
        mTvValidSinglePoint = (TextView) view.findViewById(R.id.tv_use_single_mypoint_valid);
        mTvUseSinglePointTitle = (TextView) view.findViewById(R.id.et_use_single_mypoint_use_title);
        mEtUseSingleMyPoint = (EditText) view.findViewById(R.id.et_use_single_mypoint_use);

        mIvNfcTag = (ImageView) view.findViewById(R.id.iv_nfc_tag);
        mIvNfcTag.setVisibility(View.GONE);

        mBtnUseMyPointNFC = (ToggleButton) view.findViewById(R.id.btn_use_mypoint_nfc);
        mBtnUseMyPointNFC.setOnClickListener(this);

        if (action == 1) {
            mTvValidPointTitle.setText("가용 포인트");
            mTvUsePointTitle.setText("사용 포인트");

            mTvValidGroupPointTitle.setText("가용 포인트");
            mTvUseGroupPointTitle.setText("사용 포인트");

            mTvValidSinglePointTitle.setText("가용 포인트");
            mTvUseSinglePointTitle.setText("사용 포인트");
        }
        else if (action == 2) {
            mTvValidPointTitle.setText("가용 스템프");
            mTvUsePointTitle.setText("사용 스탬프");

            mTvValidGroupPointTitle.setText("가용 스템프");
            mTvUseGroupPointTitle.setText("사용 스템프");

            mTvValidSinglePointTitle.setText("가용 스템프");
            mTvUseSinglePointTitle.setText("사용 스템프");
        }
        else if (action == 3) {
            mTvValidPointTitle.setText("가용 경품");
            mTvUsePointTitle.setText("사용 경품");

            mTvValidGroupPointTitle.setText("가용 경품");
            mTvUseGroupPointTitle.setText("사용 경품");

            mTvValidSinglePointTitle.setText("가용 경품");
            mTvUseSinglePointTitle.setText("사용 경품");
        }

        if (obj_message != null) {
            String storeName = JSONParser.getString(obj_message, "storeName");

            String storeType = "";
            if (mStoreType.equals("T"))
                storeType = "통합형";
            else if (mStoreType.equals("G"))
                storeType = "그룹형";
            else if (mStoreType.equals("S"))
                storeType = "단독형";

            mTvStoreName.setText(storeName + "(" + storeType + ")");
        }
    }

    private void requestMyPointT() {
        ResUseMyPointT res = new ResUseMyPointT();
        res.setToken(TNPreference.getMemToken(getActivity()));
        //res.setCertification(mCertification);
        res.setParameterQuestion(mDeviceSerial + "/" + "T");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestMyPointG() {
        ResUseMyPointG res = new ResUseMyPointG();
        res.setToken(TNPreference.getMemToken(getActivity()));
        //res.setCertification(mCertification);
        res.setParameterQuestion(mDeviceSerial + "/" + "G");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestMyPointS() {
        ResUseMyPointS res = new ResUseMyPointS();
        res.setToken(TNPreference.getMemToken(getActivity()));
        //res.setCertification(mCertification);
        res.setParameterQuestion(mDeviceSerial + "/" + "S");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestUsePoint() {
//
//        String date_time="0000-00-00 00:00:00";
//        String from = pay_date + pay_time;//"2013-04-08 10:10:10";
//        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        try {
//            Date to = transFormat.parse(from);
//            SimpleDateFormat transFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            date_time = transFormat1.format(to);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        mUsePointType = "P";
//        if (action == 1)
//            mUsePointType = "P";
//        else if (action == 2)
//            mUsePointType = "S";
//        else if (action == 3)
//            mUsePointType = "R";

        ResUsePoint res = new ResUsePoint();
        res.setToken(TNPreference.getMemToken(getActivity()));
        //res.setCertification(mCertification);
        res.setParameter(TNPreference.getMemphoneNumber(getActivity())
                ,mCertification);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);

        if (_bTest) {
            CheckUseMyPointFragment mainFragment = (CheckUseMyPointFragment) getFragmentManager().findFragmentByTag(CheckUseMyPointFragment.FRAGMENT_TAG);
            if (mainFragment != null) {
                String storeName = JSONParser.getString(obj_message, "storeName");
                //String storeName = mTvStoreName.getText().toString();
                String storeType = "통합형";
                if (mStoreType.equals("T"))
                    storeType = "통합형";
                else if (mStoreType.equals("G"))
                    storeType = "그룹형";
                else if (mStoreType.equals("S"))
                    storeType = "단독형";
                int point_type = 0;
                int use_point = Integer.valueOf(mEtUseMyPoint.getText().toString());
                mainFragment.gotoUseComplete(storeName, mStoreType, point_type, use_point);
            }
        }
    }

    void requestUsePointJunmun() {
        ResUsePointJunmun res = new ResUsePointJunmun();
        res.setToken(TNPreference.getMemToken(getActivity()));
        res.setParameterMobile(TNPreference.getMemphoneNumber(getActivity()));
        res.setParameterSerial(mDeviceSerial);
        res.setParameterKind(mUsePointType);
        res.setParameterAmount(mUsePoint);
        res.setParameterEarn(mStoreType);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }


    @Override
    protected void initRequest() {
        //requestMyPoint();


        requestMyPointT();
        requestMyPointG();
        requestMyPointS();

        if (obj_message != null) {

            JSONArray array_total = JSONParser.getArray(obj_message, "total");
            JSONArray array_mall = JSONParser.getArray(obj_message, "mall");

            for (int i = 0; i < array_total.length(); i++) {
                JSONObject obj_row = JSONParser.getArrayItem(array_total, i);
                if (obj_row != null) {
                    String total = JSONParser.getString(obj_row, "total");
                    String kind = JSONParser.getString(obj_row, "kind");
                    String available = JSONParser.getString(obj_row, "available");

                    if (kind.equals("P")) {
                        //if (total != null && total.length() > 0)
                        //    pointTotalTotal = Integer.valueOf(total);
                        if (available != null && available.length() > 0)
                            pointTotalCanuse = Integer.valueOf(available);
                    } else if (kind.equals("S")) {
                        //if (total != null && total.length() > 0)
                        //    stempTotalTotal = Integer.valueOf(total);
                        if (available != null && available.length() > 0)
                            stempTotalCanuse = Integer.valueOf(available);
                    } else if (kind.equals("R")) {
                        //if (total != null && total.length() > 0)
                        //    gameTotalTotal = Integer.valueOf(total);
                        if (available != null && available.length() > 0)
                            gameTotalCanuse = Integer.valueOf(available);
                    }
                }
            }

            for (int i = 0; i < array_mall.length(); i++) {
                JSONObject obj_row = JSONParser.getArrayItem(array_mall, i);
                if (obj_row != null) {
                    String kind = JSONParser.getString(obj_row, "kind");
                    String point = JSONParser.getString(obj_row, "point");

                    if (kind.equals("P")) {
                        pointTotalCanuse = Integer.valueOf(point);
                    } else if (kind.equals("S")) {
                        stempTotalCanuse = Integer.valueOf(point);
                    } else if (kind.equals("R")) {
                        gameTotalCanuse = Integer.valueOf(point);
                    }
                }
            }
        }

//        if (_bTest)
//            pointCanuse = 5000;
//        if (action == 1) {
//            mTvValidPoint.setText(String.valueOf(pointTotalCanuse) + "P");
//
////            if ((mStoreType.compareTo("T") == 0 || mStoreType.compareTo("G") == 0)
////                && pointCanuse > 0) {
////                mTvValidSinglePoint.setT
////            }
//        }
//        if (action == 2) {
//            mTvValidPoint.setText(String.valueOf(stempTotalCanuse) + "개");
//        }
//        if (action == 3) {
//            mTvValidPoint.setText(String.valueOf(gameTotalCanuse) + "점");
//        }
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

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {

        if (o[0] instanceof ResUseMyPointT) {
            ResUseMyPointT res = (ResUseMyPointT) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                if (pointTotalCanuse > 0 || stempTotalCanuse > 0 || gameTotalCanuse > 0)
                    mLiTotalPoint.setVisibility(View.VISIBLE);
                else
                    mLiTotalPoint.setVisibility(View.GONE);

                if (action == 1) {
                    mTvValidPoint.setText(String.valueOf(pointTotalCanuse) + "P");
                }
                if (action == 2) {
                    mTvValidPoint.setText(String.valueOf(stempTotalCanuse) + "개");
                }
                if (action == 3) {
                    mTvValidPoint.setText(String.valueOf(gameTotalCanuse) + "점");
                }

            }
        }

        if (o[0] instanceof ResUseMyPointG) {
            ResUseMyPointG res = (ResUseMyPointG) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                if (pointGroupCanuse > 0 || stempGroupCanuse > 0 || gameGroupCanuse > 0)
                    mLiGroupPoint.setVisibility(View.VISIBLE);
                else
                    mLiGroupPoint.setVisibility(View.GONE);

                if (action == 1) {
                    mTvValidGroupPoint.setText(String.valueOf(pointGroupCanuse) + "P");
                }
                if (action == 2) {
                    mTvValidGroupPoint.setText(String.valueOf(stempGroupCanuse) + "개");
                }
                if (action == 3) {
                    mTvValidGroupPoint.setText(String.valueOf(gameGroupCanuse) + "점");
                }
            }
        }

        if (o[0] instanceof ResUseMyPointS) {
            ResUseMyPointS res = (ResUseMyPointS) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                if (pointSingleCanuse > 0 || stempSingleCanuse > 0 || gameSingleCanuse > 0)
                    mLiSinglePoint.setVisibility(View.VISIBLE);
                else
                    mLiSinglePoint.setVisibility(View.GONE);

                if (action == 1) {
                    mTvValidSinglePoint.setText(String.valueOf(pointSingleCanuse) + "P");
                }
                if (action == 2) {
                    mTvValidSinglePoint.setText(String.valueOf(stempSingleCanuse) + "개");
                }
                if (action == 3) {
                    mTvValidSinglePoint.setText(String.valueOf(gameSingleCanuse) + "점");
                }
            }
        }

        if (o[0] instanceof ResReqPoint) {
            ResReqPoint res = (ResReqPoint) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

            }
        }

        if (o[0] instanceof ResUsePoint) {
            ResUsePoint res = (ResUsePoint) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj) && JSONParser.getBoolean(obj, ResUsePoint.KEY_RES.message)) {

//                    Message msg = mMainHandler.obtainMessage();
//                    msg.what = SEND_NFC_OFF;
//                    mMainHandler.handleMessage(msg);

                CheckUseMyPointFragment mainFragment = (CheckUseMyPointFragment) getFragmentManager().findFragmentByTag(CheckUseMyPointFragment.FRAGMENT_TAG);
                if (mainFragment != null) {
                    //String storeName = mTvStoreName.getText().toString();
                    String storeName = JSONParser.getString(obj_message, "storeName");
                    String storeType = "통합형";
                    if (mStoreType.equals("T"))
                        storeType = "통합형";
                    else if (mStoreType.equals("G"))
                        storeType = "그룹형";
                    else if (mStoreType.equals("S"))
                        storeType = "단독형";
                    //int point_type = 0;
                    int use_point = Integer.valueOf(mEtUseMyPoint.getText().toString());
                    mainFragment.gotoUseComplete(storeName, storeType, action, use_point);
                }
            }
        }

        if (o[0] instanceof ResUsePointJunmun) {
            ResUsePointJunmun res = (ResUsePointJunmun) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                JSONObject obj_message = JSONParser.getObject(obj, ResUsePoint.KEY_RES.message);
                if (obj_message != null) {
                    JSONObject receipt = JSONParser.getObject(obj_message, ResUsePoint.KEY_RES.receipt);
                    JSONObject mall = JSONParser.getObject(obj_message, ResUsePoint.KEY_RES.mall);
                    //int rps = JSONParser.getInt(obj_message, ResUsePoint.KEY_RES.rps, 0);
                    int point = JSONParser.getInt(obj_message, ResUsePoint.KEY_RES.point, 0);
                    mCertification = JSONParser.getString(obj_message, "certification");
                    mTelegram = JSONParser.getString(obj_message, "telegram");

                    if (receipt != null) {
                        String approval = JSONParser.getString(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.approval);
                        String type = JSONParser.getString(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.type);
                        int amount = JSONParser.getInt(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.amount, 0);
                        int supply = JSONParser.getInt(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.supply, 0);
                        int vat = JSONParser.getInt(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.vat, 0);
                        String payType = JSONParser.getString(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.payType);
                        String payDate = JSONParser.getString(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.payDate);
                        String payTypeName = JSONParser.getString(receipt, ResUsePoint.KEY_RES.RECEIPT_RES.payTypeName);
//
//                        String checkMsg = "사업자 명 : " + JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.name);
////               checkMsg += "\n사업자 번호: XXXXX";
//                        checkMsg += ("\n판매일자: " + payDate);
//                        checkMsg += ("\n결제수단: " + payTypeName);
//                        checkMsg += ("\n결제금액: " + amount);
//                        checkMsg += ("\n부가세: " + vat);
//                        checkMsg += ("\n승인번호: " + approval);
//                        mTvInsertPointCheck.setText(checkMsg);
                    }

                    if (mall != null) {
                        String name = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.name);
                        String earn = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.earn);
                        String rpsUse = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.rpsUse);
                        String ssn = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.ssn);
                        String address = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.address);
                        String owner = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.owner);
                        String leaflet = JSONParser.getString(mall, ResUsePoint.KEY_RES.MALL_RES.leaflet);
//
//                        mTvFranchiseeName.setText(name);
//                        mTvInsertPointType.setText("터치올 포인트 " + earn);
                    }

                    if (mTelegram != null && mTelegram.length() > 0) {
                        SLog.LogD("use point telegram -> " + mTelegram);
                        Message msg = mMainHandler.obtainMessage();
                        msg.what = SEND_NFC_ON;
                        mMainHandler.handleMessage(msg);
                    }

                    if (_bTestAria)
                    {
                        mTelegram = "4CCF0885CABFB513C46FB83382F01496F15EE8A0B8FCD20A289B761CD5FCE5F68744223EB44B04E0B29AE7940CDB0611C40A99035A1CA5896E2A4143628F026D";
                        byte[] bytes = hexStringToByteArray(mTelegram);

                        Aria aria = new Aria("KopYecatainUTa%4el82ro#$llcL&HAI");
                        //String decryt_junmun = aria.Decrypt(aria_junmun);
                        String decryt_junmun = aria.Decrypt(bytes);

                        SLog.LogD("Use ARIA dec message -> " + decryt_junmun);
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
        switch (view.getId()) {
            case R.id.btn_use_mypoint_nfc:
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mEtUseMyPoint.getWindowToken(), 0);
                }

                if (mBtnUseMyPointNFC.isChecked()) {

                    // 통합형.
                    String useTotalPoint = mEtUseMyPoint.getText().toString();
                    String useGroupPoint = mEtUseGroupMyPoint.getText().toString();
                    String useSinglePoint = mEtUseSingleMyPoint.getText().toString();

                    if (useTotalPoint.length() <= 0 && useGroupPoint.length() <= 0 && useSinglePoint.length() <= 0) {
                        showToast("사용 할 가용 보너스를 입력하세요.");
                        mBtnUseMyPointNFC.setChecked(false);
                        return;
                    }

                    if (mLiTotalPoint.getVisibility() == View.VISIBLE && useTotalPoint.length() > 0) {

                        int iUsePoint = Integer.valueOf(useTotalPoint);
                        if (iUsePoint <= 0) {
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                        if (iUsePoint > pointTotalCanuse) {
                            showToast("[통합형] 사용 할 수 있는 가용 보너스가 부족합니다.");
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                    }

                    if (mLiGroupPoint.getVisibility() == View.VISIBLE && useGroupPoint.length() > 0) {

                        int iUsePoint = Integer.valueOf(useGroupPoint);
                        if (iUsePoint <= 0) {
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                        if (iUsePoint > pointGroupCanuse) {
                            showToast("[그룹형] 사용 할 수 있는 가용 보너스가 부족합니다.");
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                    }

                    if (mLiSinglePoint.getVisibility() == View.VISIBLE && useSinglePoint.length() > 0) {

                        int iUsePoint = Integer.valueOf(useSinglePoint);
                        if (iUsePoint <= 0) {
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                        if (iUsePoint > pointSingleCanuse) {
                            showToast("[단독형] 사용 할 수 있는 가용 보너스가 부족합니다.");
                            mBtnUseMyPointNFC.setChecked(false);
                            return;
                        }

                    }

                    mUsePointType = "P";
                    mUsePoint = "0";

                    if (action == 1) {
                        mUsePointType = "P";
                        if (mLiTotalPoint.getVisibility() == View.VISIBLE && useTotalPoint.length() > 0) {
                            mUsePoint = String.format("%05d", Integer.valueOf(useTotalPoint));
                        }
                        else if (mLiGroupPoint.getVisibility() == View.VISIBLE && useGroupPoint.length() > 0) {
                            mUsePoint = String.format("%05d", Integer.valueOf(useGroupPoint));
                        }
                        else if (mLiSinglePoint.getVisibility() == View.VISIBLE && useSinglePoint.length() > 0) {
                            mUsePoint = String.format("%05d", Integer.valueOf(useSinglePoint));
                        }
                    } else if (action == 2) {
                        mUsePointType = "S";
                        if (mLiTotalPoint.getVisibility() == View.VISIBLE && useTotalPoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useTotalPoint));
                        }
                        else if (mLiGroupPoint.getVisibility() == View.VISIBLE && useGroupPoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useGroupPoint));
                        }
                        else if (mLiSinglePoint.getVisibility() == View.VISIBLE && useSinglePoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useSinglePoint));
                        }
                    } else if (action == 3) {
                        mUsePointType = "R";
                        if (mLiTotalPoint.getVisibility() == View.VISIBLE && useTotalPoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useTotalPoint));
                        }
                        else if (mLiGroupPoint.getVisibility() == View.VISIBLE && useGroupPoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useGroupPoint));
                        }
                        else if (mLiSinglePoint.getVisibility() == View.VISIBLE && useSinglePoint.length() > 0) {
                            mUsePoint = String.format("%02d", Integer.valueOf(useSinglePoint));
                        }
                    }

                    requestUsePointJunmun();
                }
                else {
                    Message msg = mMainHandler.obtainMessage();
                    msg.what = SEND_NFC_OFF;
                    mMainHandler.handleMessage(msg);
                }
            }
                break;
        }
    }

    @Override
    public void onBack() {
        //replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);

        Intent returnIntent = new Intent();
        //returnIntent.putExtra("junmun",msgs.get(0).toString());
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }


    @Override
    public void onComplete() {
        requestUsePoint();
    }
}
