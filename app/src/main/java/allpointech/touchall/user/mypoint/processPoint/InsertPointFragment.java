package allpointech.touchall.user.mypoint.processPoint;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import allpointech.touchall.R;
import allpointech.touchall.custom.glide.GlideLoader;
import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResGame;
import allpointech.touchall.network.http.resource.data.ResJuklib;
import allpointech.touchall.network.http.resource.data.ResReqTelegram;
import allpointech.touchall.network.http.resource.data.ResResponseMessage;
import allpointech.touchall.utils.TNPreference;

/**
 * Created by jay on 2018. 6. 27..
 */

public class InsertPointFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = InsertPointFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private LinearLayout mliInsertPointResult;
    private TextView mTvFranchiseeName;
    private TextView mTvFranchiseeSub;
    private TextView mTvInsertPointType;
    private TextView mTvInsertPointResult;

    private LinearLayout mLiGradeGuide;
    private TextView mTvInsertPointGradeGuide;

    private LinearLayout mLiDuplicateGuide;
    private TextView mTvInsertPointDuplicateGuide;

    private LinearLayout mliInsertPointGame;
    private TextView mTvGameFranchiseeName;
    private TextView mTvGameFranchiseeNameSub;
    private TextView mTvGameInsertPointType;

    private LinearLayout mLiBills;

    static int[] _rsp = {
            R.drawable.rotate_rsp_r,
            R.drawable.rotate_rsp_s,
            R.drawable.rotate_rsp_p};

    private TimerTask mGameTimer;
    Timer t_game = new Timer();
    final Handler handler = new Handler();
    private int mGameCount = 0;
    private int mResultGamePoint = 0;

    String pointUse = "Y";
    String stampUse = "N";
    String rpsUse = "Y";

    private ImageView mIvRspAi;
    private TextView mTvGameGuide;
    private Button mBtnStartGame;
    private ImageView mBtnUserPspS;
    private ImageView mBtnUserPspR;
    private ImageView mBtnUserPspP;
    private TextView mTvInserPointGame;

    private TextView mTvInsertPointCheckStoreName;
    private TextView mTvInsertPointCheckBussinessNumber;
    private TextView mTvInsertPointCheckDate;
    private TextView mTvInsertPointCheckType;
    private TextView mTvInsertPointCheckCost;
    private TextView mTvInsertPointCheckVAT;
    private TextView mTvInsertPointCheckPayCost;
    private TextView mTvInsertPointCheckConfirmCode;

    private LinearLayout mLiLeaflet;
    private ImageView mIvLeaflet;

    private String mJunmun;
    private String mResultRsp;

    private boolean _bTest = false;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mJunmun = getArguments().getString("point_junmun");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_insert_point;
    }

    private TextView setColorInPartitial(String pre_string, String string, String post_string, String color, TextView textView){
        textView.setText("");
        SpannableStringBuilder builder = new SpannableStringBuilder(pre_string + string + post_string);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), pre_string.length(), pre_string.length()+string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(builder);
        return textView;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //mToolbar.setTitle("포인트 적립 승인 완료");

        mliInsertPointResult = (LinearLayout) view.findViewById(R.id.layout_point_result);
        mliInsertPointResult.setVisibility(View.GONE);

        mTvFranchiseeName = (TextView) view.findViewById(R.id.tv_insert_point_franchisee);
        mTvFranchiseeSub = (TextView) view.findViewById(R.id.tv_insert_point_franchisee_sub);
        mTvFranchiseeSub.setText("");
        mTvInsertPointType = (TextView) view.findViewById(R.id.tv_insert_point_type);
        mTvInsertPointResult = (TextView) view.findViewById(R.id.tv_insert_point_result);


        mLiGradeGuide = (LinearLayout) view.findViewById(R.id.li_grade_guide);
        mTvInsertPointGradeGuide = (TextView) view.findViewById(R.id.tv_insert_point_grade_guide);
        mLiGradeGuide.setVisibility(View.GONE);

        mLiDuplicateGuide = (LinearLayout) view.findViewById(R.id.li_duplicate_guide);
        mTvInsertPointDuplicateGuide = (TextView) view.findViewById(R.id.tv_insert_point_duplicate_guide);
        mLiDuplicateGuide.setVisibility(View.GONE);

        mliInsertPointGame = (LinearLayout) view.findViewById(R.id.layout_point_game);
        mliInsertPointGame.setVisibility(View.GONE);
        mTvGameFranchiseeName = (TextView) view.findViewById(R.id.tv_insert_point_game_franchisee);
        mTvGameFranchiseeNameSub = (TextView) view.findViewById(R.id.tv_insert_point_game_franchisee_sub);
        mTvGameInsertPointType = (TextView) view.findViewById(R.id.tv_insert_point_game_type);

        mIvRspAi = (ImageView) view.findViewById(R.id.iv_insert_point_game_ai);
        mTvGameGuide = (TextView) view.findViewById(R.id.tv_insert_point_game_guide);
        mTvGameGuide.setText("가위바위보 게임을 시작합니다.\n시작버튼을 누른 후 3초이내에 아래\n가위바위보를 선택 하십시요.");
        mBtnStartGame = (Button) view.findViewById(R.id.btn_insert_point_game_start);
        mBtnStartGame.setOnClickListener(this);
        mBtnUserPspS = (ImageView) view.findViewById(R.id.iv_insert_point_game_user_s);
        mBtnUserPspR = (ImageView) view.findViewById(R.id.iv_insert_point_game_user_r);
        mBtnUserPspP = (ImageView) view.findViewById(R.id.iv_insert_point_game_user_p);
        mTvInserPointGame = (TextView) view.findViewById(R.id.tv_insert_point_game_result);

        mLiBills = (LinearLayout) view.findViewById(R.id.layout_point_bills);
        mLiBills.setVisibility(View.GONE);
        mTvInsertPointCheckStoreName = (TextView) view.findViewById(R.id.tv_insert_point_check_storename);
        mTvInsertPointCheckBussinessNumber = (TextView) view.findViewById(R.id.tv_insert_point_check_bussiness_number);
        mTvInsertPointCheckDate = (TextView) view.findViewById(R.id.tv_insert_point_check_date);
        mTvInsertPointCheckType = (TextView) view.findViewById(R.id.tv_insert_point_check_type);
        mTvInsertPointCheckCost = (TextView) view.findViewById(R.id.tv_insert_point_check_cost);
        mTvInsertPointCheckVAT = (TextView) view.findViewById(R.id.tv_insert_point_check_vat);
        mTvInsertPointCheckPayCost = (TextView) view.findViewById(R.id.tv_insert_point_check_paycost);
        mTvInsertPointCheckConfirmCode = (TextView) view.findViewById(R.id.tv_insert_point_check_confirm_code);

        mLiLeaflet = (LinearLayout) view.findViewById(R.id.layout_point_leaflet);
        mIvLeaflet = (ImageView) view.findViewById(R.id.imageview_insertpoint_leaflet);
        mLiLeaflet.setVisibility(View.GONE);


        if (_bTest) {
            mTvFranchiseeName.setText("더 카페");
            mTvFranchiseeSub.setText("에서 제공한");
            mTvInsertPointType.setText("터치올 포인트 [통합형]");
            String str = "현금 900원 적립 완료";
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //mTvInsertPointResult.setText("현금 900원 적립 완료");
            mTvInsertPointResult.setText(ssb);

            String checkMsg = "사업자 명 : 더 카페";
            checkMsg += "\n사업자 번호: XXXXX";
            checkMsg += "\n판매일자: 2018-08-29 15:33";
            checkMsg += "\n결제수단: 현금 결제";
            checkMsg += "\n결제금액: 30,000원";
            checkMsg += "\n부가세: 3,000원";
            checkMsg += "\n승인번호: 0123456789";
            //mTvInsertPointCheck.setText(checkMsg);
        }
    }

    private void requestInsertPoint() {

        ResReqTelegram res = new ResReqTelegram();
        res.setToken(TNPreference.getMemToken(getActivity()));
        res.setParameterMobile(TNPreference.getMemphoneNumber(getActivity()));
        res.setParameterTelegram(mJunmun.substring(2));
        res.setParameterRps(mResultRsp);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestGameResult(String rps) {
        ResGame res = new ResGame();
        res.setToken(TNPreference.getMemToken(getActivity()));
        res.setParameter(rps);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
        insertPoint(mJunmun);
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

                JSONObject obj_message = JSONParser.getObject(obj, ResResponseMessage.KEY_RES.message);
                if (obj_message != null) {

                    JSONObject receipt = JSONParser.getObject(obj_message, ResResponseMessage.KEY_RES.receipt);
                    JSONObject mall = JSONParser.getObject(obj_message, ResResponseMessage.KEY_RES.mall);
                    String deuplicate = JSONParser.getString(obj_message, ResResponseMessage.KEY_RES.duplicate);
                    String grade = JSONParser.getString(obj_message, ResResponseMessage.KEY_RES.grade);
                    int rps = JSONParser.getInt(obj_message, ResResponseMessage.KEY_RES.rps, 0);
                    int point = JSONParser.getInt(obj_message, ResResponseMessage.KEY_RES.point, 0);
                    String telegram = JSONParser.getString(obj_message, ResResponseMessage.KEY_RES.telegram);

                    mLiGradeGuide.setVisibility(View.GONE);
                    if (grade != null && grade.length() > 0) {
                        int grade_level = Integer.parseInt(grade);
                        if (grade_level >= 2)
                            mLiGradeGuide.setVisibility(View.VISIBLE);
                    }

                    mLiDuplicateGuide.setVisibility(View.GONE);
                    if (deuplicate != null && deuplicate.compareTo("Y") == 0) {
                        mLiDuplicateGuide.setVisibility(View.VISIBLE);
                    }

                    //if (receipt != null && mall != null)
                    {

                        mToolbar.setTitle("포인트 적립 승인 완료");

                        String approval = "";
                        String type = "";
                        int amount = 0;
                        int supply = 0;
                        int vat = 0;
                        String payType = "";
                        String payDate = "";
                        String payTypeName = "";

                        String name = "";
                        String earn = "";
                        pointUse = "";
                        stampUse = "";
                        rpsUse = "";
                        String ssn = "";
                        String address = "";
                        String owner = "";
                        String leaflet = "";

                        if (receipt != null) {
                            approval = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.approval);
                            type = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.type);
                            amount = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.amount, 0);
                            supply = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.supply, 0);
                            vat = JSONParser.getInt(receipt, ResJuklib.KEY_RES.RECEIPT_RES.vat, 0);
                            payType = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payType);
                            payDate = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payDate);
                            payTypeName = JSONParser.getString(receipt, ResJuklib.KEY_RES.RECEIPT_RES.payTypeName);
                        }

                        if (mall != null) {
                            name = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.name);
                            earn = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.earn);
                            pointUse = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.pointUse);
                            stampUse = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.stampUse);
                            rpsUse = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.rpsUse);
                            ssn = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.ssn);
                            address = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.address);
                            owner = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.owner);
                            leaflet = JSONParser.getString(mall, ResJuklib.KEY_RES.MALL_RES.leaflet);
                        }

                        mTvInsertPointCheckStoreName.setText(name);
                        mTvInsertPointCheckBussinessNumber.setText(ssn);
                        mTvInsertPointCheckDate.setText(payDate);
                        mTvInsertPointCheckType.setText(payTypeName);
                        mTvInsertPointCheckCost.setText(Integer.toString(supply));
                        mTvInsertPointCheckVAT.setText(Integer.toString(vat));
                        mTvInsertPointCheckPayCost.setText(Integer.toString(amount));
                        mTvInsertPointCheckConfirmCode.setText(approval);
                        mLiBills.setVisibility(View.VISIBLE);

                        mTvFranchiseeName.setText(name);
                        mTvFranchiseeSub.setText("에서 제공한");

                        if (pointUse.compareTo("Y") == 0) {
                            mTvInsertPointType.setText("터치올 포인트 [" + earn + "]");
                            setColorInPartitial("현금 ", String.valueOf(point), "원 적립 완료", "#FF0000", mTvInsertPointResult);
                            mliInsertPointResult.setVisibility(View.VISIBLE);
                        }
                        else if (stampUse.compareTo("Y") == 0) {
                            mTvInsertPointType.setText("터치올 스탬프 [" + earn + "]");
                            setColorInPartitial("스탬프 ", String.valueOf(point), "개 적립 완료", "#FF0000", mTvInsertPointResult);
                            mliInsertPointResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            mliInsertPointResult.setVisibility(View.GONE);
                        }

                        //if (rpsUse.compareTo("Y") == 0) {
                        if (rps > 0) {
                            mliInsertPointGame.setVisibility(View.VISIBLE);

                            mTvGameFranchiseeName.setText(name);
                            mTvGameFranchiseeNameSub.setVisibility(View.VISIBLE);
                            mTvGameInsertPointType.setText("터치올 가위바위보 [" + earn + "]");

                            mIvRspAi.setVisibility(View.GONE);
                            mTvGameGuide.setVisibility(View.GONE);
//                            mBtnUserPspS.setVisibility(View.GONE);
//                            mBtnUserPspR.setVisibility(View.GONE);
//                            mBtnUserPspP.setVisibility(View.GONE);
                            mTvInserPointGame.setText("게임승점 " + rps + "점 적립!");
                            mliInsertPointResult.setVisibility(View.VISIBLE);
                        }
                        else {
                            mliInsertPointGame.setVisibility(View.GONE);
                        }

                        if (leaflet != null && leaflet.length() > 0) {
                            Glide
                                    .with(getActivity())
                                    .load(HttpInfo.HOST_URL + leaflet)
                                    .override(600, 200)
                                    .into(mIvLeaflet);
                            mLiLeaflet.setVisibility(View.VISIBLE);
                        }
                        else {
                            mLiLeaflet.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

        if (o[0] instanceof ResGame) {
            ResGame res = (ResGame) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResGame.KEY_RES.message);
                if (obj_message != null) {
                    String result = JSONParser.getString(obj_message, ResGame.KEY_RES.result);
                    String show = JSONParser.getString(obj_message, ResGame.KEY_RES.show);

                    if (show.compareTo("s") == 0)
                        mIvRspAi.setImageResource(_rsp[1]);
                    else if (show.compareTo("r") == 0)
                        mIvRspAi.setImageResource(_rsp[0]);
                    else if (show.compareTo("p") == 0)
                        mIvRspAi.setImageResource(_rsp[2]);
                    mIvRspAi.setVisibility(View.VISIBLE);

                    mTvInserPointGame.setText("경품 게임 " + (result.compareTo("w") == 0 ? "승!" : (result.compareTo("d") == 0 ? "무!" : "패")));

                    mResultRsp = result;
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //여기에 딜레이 후 시작할 작업들을 입력
                            requestInsertPoint();
                        }
                    }, 3000);// 3초 정도 딜레이를 준 후 시작
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
            case R.id.btn_insert_point_game_start:
                mTvGameGuide.setVisibility(View.GONE);
                mBtnStartGame.setVisibility(View.GONE);

                mBtnUserPspS.setOnClickListener(this);
                mBtnUserPspR.setOnClickListener(this);
                mBtnUserPspP.setOnClickListener(this);
                break;
            case R.id.iv_insert_point_game_user_s:  // 찌
                if (mGameTimer != null)
                    mGameTimer.cancel();
                mBtnUserPspS.setOnClickListener(null);
                mBtnUserPspR.setOnClickListener(null);
                mBtnUserPspP.setOnClickListener(null);

                mIvRspAi.setVisibility(View.GONE);
                requestGameResult("s");
                break;
            case R.id.iv_insert_point_game_user_r: // 묵
                if (mGameTimer != null)
                    mGameTimer.cancel();
                mBtnUserPspS.setOnClickListener(null);
                mBtnUserPspR.setOnClickListener(null);
                mBtnUserPspP.setOnClickListener(null);

                mIvRspAi.setVisibility(View.GONE);
                requestGameResult("r");
                break;
            case R.id.iv_insert_point_game_user_p: // 빠
                if (mGameTimer != null)
                    mGameTimer.cancel();
                mBtnUserPspS.setOnClickListener(null);
                mBtnUserPspR.setOnClickListener(null);
                mBtnUserPspP.setOnClickListener(null);

                mIvRspAi.setVisibility(View.GONE);
                requestGameResult("p");
                break;
        }
    }

    @Override
    public void onBack() {
        //replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);

        getActivity().finish();
    }


    public void insertPoint(String point_junmun) {

        mJunmun = point_junmun;

        if (mJunmun != null) {
            if (mJunmun.substring(0,1).compareTo("1") == 0) {
                if (mJunmun.substring(1, 2).compareTo("Y") == 0) {
                    mliInsertPointGame.setVisibility(View.VISIBLE);

                    mTvGameFranchiseeName.setText("");
                    mTvGameFranchiseeNameSub.setVisibility(View.GONE);
                    mTvGameInsertPointType.setText("터치올 가위바위보");


                    mIvRspAi.setVisibility(View.VISIBLE);
                    mTvGameGuide.setVisibility(View.VISIBLE);
                    mBtnUserPspS.setVisibility(View.VISIBLE);
                    mBtnUserPspR.setVisibility(View.VISIBLE);
                    mBtnUserPspP.setVisibility(View.VISIBLE);

                    if (mGameTimer != null)
                        mGameTimer.cancel();

                    mGameCount = 0;
                    mIvRspAi.setImageResource(_rsp[mGameCount]);

                    mGameTimer = new TimerTask() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    mGameCount++;
                                    mGameCount %= 3;
                                    mIvRspAi.setImageResource(_rsp[mGameCount]);
                                }
                            });
                        }
                    };

                    t_game.schedule(mGameTimer, 100, 100);  //
                }
                else {
                    mResultRsp = "N";
                    requestInsertPoint();
                }
            }
        }
    }

}
