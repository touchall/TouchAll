package allpointech.touchall.user.usermain;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseDialogFragment;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import allpointech.touchall.R;
import allpointech.touchall.custom.glide.GlideLoader;
import allpointech.touchall.dialog.ListDialog;
import allpointech.touchall.dialog.MsgDialog;
import allpointech.touchall.intro.IntroActivity;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResLogOut;
import allpointech.touchall.setting.SettingActivity;
import allpointech.touchall.user.UserMainFragment;
import allpointech.touchall.user.info.UserInfoActivity;
import allpointech.touchall.utils.TNConstants;
import allpointech.touchall.utils.TNPreference;

/**
 * Created by daze on 2016-11-15.
 */

public class UserInfoFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = UserInfoFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvUserName;
    private TextView mTvUserBirthDay;
    private TextView mTvUserPhone;

    private TextView mTvUserEmail;
    private TextView mTvUserSex;
    private TextView mTvUserAddr;
    private TextView mTvUserBonusType;

    private Button mBtnModify;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
//            try {
//                //mReceiveObj = new JSONObject(getArguments().getString(TNConstants.DATA.MYPAGE_DATA));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvUserName = (TextView) view.findViewById(R.id.text_userinfo_name);
        mTvUserBirthDay = (TextView) view.findViewById(R.id.text_userinfo_birthday);
        mTvUserPhone = (TextView) view.findViewById(R.id.text_userinfo_phone);

        mTvUserEmail = (TextView) view.findViewById(R.id.text_userinfo_email);
        mTvUserSex = (TextView) view.findViewById(R.id.text_userinfo_sex);
        mTvUserAddr = (TextView) view.findViewById(R.id.text_userinfo_address);
        mTvUserBonusType = (TextView) view.findViewById(R.id.text_userinfo_bonus_type);

        mBtnModify = (Button) view.findViewById(R.id.button_userinfo_modify);
        mBtnModify.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                UserMainFragment fragment = (UserMainFragment) getFragmentManager().findFragmentByTag(UserMainFragment.FRAGMENT_TAG);
                if (fragment != null) {
                    fragment.openDrawer();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initRequest() {

        mTvUserName.setText(TNPreference.getMemName(getActivity()));
        mTvUserBirthDay.setText(TNPreference.getMemBirthDay(getActivity()));
        mTvUserPhone.setText(TNPreference.getMemphoneNumber(getActivity()));

        mTvUserEmail.setText(TNPreference.getMemEMail(getActivity()));
        mTvUserSex.setText(TNPreference.getMemGenderName(getActivity()));
        mTvUserAddr.setText(TNPreference.getMemAddr(getActivity()));
        //mTvUserBonusType.setText(TNPreference.getMem(getActivity()));

        String bank = TNPreference.getMemBankCode(getActivity());
        String account = TNPreference.getMemBankAccount(getActivity());
        String holder = TNPreference.getMemBankHolder(getActivity());

        if (bank != null && bank.length() > 0 && account != null && account.length() > 0 && holder != null && holder.length() > 0) {
            mTvUserBonusType.setText("통장입금");
        }
        else {
            mTvUserBonusType.setText("결제용 터치올 포인트");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.button_userinfo_modify: {
            UserInfoActivity.userMobile = TNPreference.getMemphoneNumber(getActivity());
            UserInfoActivity.userPassword = TNPreference.getMemPw(getActivity());
            UserInfoActivity.userName = TNPreference.getMemName(getActivity());
            UserInfoActivity.userEmail = TNPreference.getMemEMail(getActivity());
            UserInfoActivity.userBirth = TNPreference.getMemBirthDay(getActivity());
            UserInfoActivity.userArea = TNPreference.getMemAddr(getActivity());
            UserInfoActivity.userGender = TNPreference.getMemSex(getActivity());
            UserInfoActivity.userBank = TNPreference.getMemBankCode(getActivity());
            UserInfoActivity.userAccount = TNPreference.getMemBankAccount(getActivity());
            UserInfoActivity.userHolder = TNPreference.getMemBankHolder(getActivity());
        }
            //startActivity(;
            startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), 12123);
            break;
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12123) {
            initRequest();
        }
    }

    @Override
    public void onBack() {
        replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
    }
}
