package allpointech.touchall.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseDialogFragment;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import allpointech.touchall.R;
import allpointech.touchall.dialog.MsgSingleDialog;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResFind;
import allpointech.touchall.network.http.resource.data.ResSMSCheck;
import allpointech.touchall.network.http.resource.data.ResSMSReq;

/**
 * Created by daze on 2016-11-15.
 */

public class FindPasswordFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = FindPasswordFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvFindEmailMsg;
    private TextInputEditText mEdEmail;

    private TextView mTvFindPasswordMsg;
    private TextInputEditText mEdPassword;

    private TextView mTvFindPasswordConfirmMsg;
    private TextInputEditText mEdPasswordConfirm;

    private TextView mTvFindPhoneNumberMsg;
    private TextInputEditText mEdPhoneNumber;

    private TextView mBtnSendInjeon;

    private Timer mSMSTimer = new Timer();
    private int mRemainTime = 3*60;

    private boolean mBsendMessageOK = false;

    private FindPasswordFragment mFragment;

    private LinearLayout mLiInjeon;
    private TextInputEditText mEdInjeonCode;
    private TextView mBtnCheckInjeon;
    private TextView mTvCheckInjeonTimeout;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_find_password;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mFragment = this;

        mEdEmail = (TextInputEditText) view.findViewById(R.id.edit_find_reset_email);
        mTvFindEmailMsg = (TextView) view.findViewById(R.id.tv_find_reset_email_msg);

        mEdPassword = (TextInputEditText) view.findViewById(R.id.edit_find_reset_password);
        mTvFindPasswordMsg = (TextView) view.findViewById(R.id.tv_find_reset_password_msg);

        mEdPasswordConfirm = (TextInputEditText) view.findViewById(R.id.edit_find_reset_password_confirm);
        mTvFindPasswordConfirmMsg = (TextView) view.findViewById(R.id.tv_find_reset_password_confirm_msg);

        mEdPhoneNumber = (TextInputEditText) view.findViewById(R.id.edit_find_reset_phone_number);
        mTvFindPhoneNumberMsg = (TextView) view.findViewById(R.id.tv_find_reset_phonenumber_msg);

        mBtnSendInjeon = (TextView) view.findViewById(R.id.btn_send_reset_injeon);
        mBtnSendInjeon.setOnClickListener(this);


        mLiInjeon = (LinearLayout) view.findViewById(R.id.li_find_reset_injeon);
        mLiInjeon.setVisibility(View.GONE);
        mEdInjeonCode = (TextInputEditText) view.findViewById(R.id.edit_find_reset_injeon_code);
        mBtnCheckInjeon = (TextView) view.findViewById(R.id.btn_find_reset_injeon_check);
        mBtnCheckInjeon.setOnClickListener(this);
        mTvCheckInjeonTimeout = (TextView) view.findViewById(R.id.tv_find_reset_injeon_timeout);
    }

    @Override
    protected void initRequest() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_ok, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBack();
                break;
//            case R.id.action_ok:
//                if (onCheck()) {
//                    requestFindPassword();
//                }
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    private boolean onCheck() {
//        if (mEdEmail.getText().length() == 0) {
//            showToast(getString(R.string.msg_error_find_email));
//            return false;
//        }
//        return true;
//    }

    private boolean onCheck() {
        if (mEdEmail.getText().length() == 0) {
            //showToast(getString(R.string.msg_error_join_id));
            showToast("사용자 이름을 입력하세요.");
            return false;
        }
        if (mEdPassword.getText().length() == 0) {
            //showToast(getString(R.string.msg_error_join_pw_0));
            showToast("비밀번호를 입력하세요.");
            return false;
        }
        if (mEdPasswordConfirm.getText().length() == 0) {
            //showToast(getString(R.string.msg_error_join_pw_0));
            showToast("비밀번호 확인을 입력하세요.");
            return false;
        }
        if (mEdPhoneNumber.getText().length() == 0) {
            //showToast(getString(R.string.msg_error_join_pw_1));
            showToast("휴대폰 번호를 입력하세요.");
            return false;
        }
//        if (mbInjeonOk == false) {
//            showToast("본인 인증이 필요합니다.");;
//            return false;
//        }

        return true;
    }

    private void requestFindPassword() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFind res = new ResFind();
        res.setParameter(mEdEmail.getText().toString());

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestSMS(String phoneNumber) {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResSMSReq res = new ResSMSReq();
        res.setParameter(phoneNumber);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void checkSMS(String phoneNumber, String regCode) {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResSMSCheck res = new ResSMSCheck(regCode);
        res.setParameter(phoneNumber, regCode);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {

        if (o[0] instanceof ResSMSReq) {
            ResSMSReq res = (ResSMSReq) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                //mEditSMSNumber.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//                mBtnCheckCode.setEnabled(true);
//                mEditSMSLayout.setEnabled(true);
                mRemainTime = 6*30;
                mSMSTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mRemainTime--;
                                if (mRemainTime < 0) {
                                    mSMSTimer.cancel();
                                    mTvCheckInjeonTimeout.setText(String.format("유효시간 00:00:00\n3분 이내에 인증번호를 입력해 주세요\n입력시간이 초과하면 인증번호 재전송을 눌러주세요."));
                                    mBtnCheckInjeon.setOnClickListener(mFragment);
                                    mBsendMessageOK = false;
                                }
                                else {
                                    mTvCheckInjeonTimeout.setText(String.format("유효시간 %02d:%02d:%02d\n3분 이내에 인증번호를 입력해 주세요\n입력시간이 초과하면 인증번호 재전송을 눌러주세요.",
                                            mRemainTime / 3600, (mRemainTime / 60) % 60, mRemainTime % 60));
                                    mBsendMessageOK = true;
                                }
                            }
                        });
                    }
                }, 1000, 1000);
            } else {
            }
        }

        if (o[0] instanceof ResSMSCheck) {
            ResSMSCheck res = (ResSMSCheck) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                //mbInjeonOk = true;

                mSMSTimer.cancel();
                //mEditPhoneNumber.setEnabled(false);

                Bundle b = new Bundle();
                //b.putString("dialog_msg", getString(R.string.msg_success_guide_answer));
                b.putString("dialog_msg", "인증이 완료 되었습니다.");
                MsgSingleDialog dialog = new MsgSingleDialog();
                dialog.setArguments(b);
                dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
                    @Override
                    public void onDialogResult(Object... objects) {
                        if (objects != null && objects[0] instanceof Boolean) {
                            boolean isOk = (boolean) objects[0];
                            if (isOk) {
//                                getActivity().setResult(getActivity().RESULT_OK);
//                                getActivity().finish();
                                onBack();
                            }
                        }
                    }
                });
                dialog.show(getFragmentManager(), "dialog");

                //mBtnNext.setEnabled(true);
                //mBtnNext.setOnClickListener(this);
                //mBtnCheckInjeon.setEnabled(true);
                //mBtnCheckInjeon.setOnClickListener(this);
            } else {
                //mbInjeonOk = false;
            }
        }

//        if (o[0] instanceof ResFind) {
//            ResFind res = (ResFind) o[0];
//            JSONObject obj = res.getParseData();
//            mTvFindMsg.setVisibility(View.VISIBLE);
//            if (JSONParser.isSuccess(obj)) {
//                mTvFindMsg.setText(getString(R.string.msg_success_find));
//            } else {
//                mTvFindMsg.setText(getString(R.string.msg_error_find_fail));
//            }
//        }
    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {

    }

    @Override
    public void onBack() {
        replaceAnimationTagFragment(((BaseAppCompatActivity) getActivity()).getMainViewId(), new LoginFragment(), LoginFragment.FRAGMENT_TAG, 0, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_reset_injeon:

                if (onCheck()) {
                    requestSMS(mEdPhoneNumber.getText().toString());
                    //requestSMS("010-9443-3244");
                    mBtnSendInjeon.setOnClickListener(null);
                    mBtnSendInjeon.setVisibility(View.GONE);
                    mLiInjeon.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_find_reset_injeon_check:
                checkSMS(mEdPhoneNumber.getText().toString(), mEdInjeonCode.getText().toString());
                break;
        }
    }
}
