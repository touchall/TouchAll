package allpointech.touchall.user;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import allpointech.touchall.R;
import allpointech.touchall.custom.glide.GlideLoader;
import allpointech.touchall.network.http.TNHttpMultiPartTask;
import allpointech.touchall.network.http.json.JSONParser;
import allpointech.touchall.network.http.resource.BaseHttpResource;
import allpointech.touchall.network.http.resource.data.ResMain;
import allpointech.touchall.user.mypoint.processPoint.CancelPointActivity;
import allpointech.touchall.user.mypoint.CheckMyPointFragment;
import allpointech.touchall.user.mypoint.Franchisee.CheckFranchiseeActivity;
import allpointech.touchall.user.mypoint.processPoint.InsertPointActivity;
import allpointech.touchall.user.mypoint.Franchisee.UseMyPointFragment;
import allpointech.touchall.user.setup.DeviceSetupResultActivity;
import allpointech.touchall.user.store.FindStoreFragment;
import allpointech.touchall.user.usermain.UserInfoFragment;
import allpointech.touchall.user.usermain.UserMainViewFragment;
import allpointech.touchall.utils.TNConstants;
import allpointech.touchall.utils.TNPreference;
import allpointech.touchall.utils.aria.Aria;
import allpointech.touchall.webview.WebViewFragment;

/**
 * Created by daze on 2016-11-15.
 */

public class UserMainFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, View.OnClickListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = UserMainFragment.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    private ExpandableListView expListView;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    static int[] _icons = {
            R.drawable.ic_home,
            R.drawable.ic_introduce,
            R.drawable.ic_findstore,
            R.drawable.ic_findbonus,
            R.drawable.ic_mypage,
            R.drawable.ic_events,
            R.drawable.ic_faq};
    View view_Group;

    private LinearLayout mLiSideMenu;
    private String mGcmData;

    private ActionBarDrawerToggle mDrawerToggle;

    private boolean _bMyPointTest = false;
    private boolean _bJuklibPointTest = false;
    private boolean _bCancelPointTest = false;
    private boolean _bDeviceSetupTest = false;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mGcmData = getArguments().getString("obj");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_user_main;
    }

    @Override
    protected void initLayout(View view) {
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        setUpDrawer();
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            @Override
            public void onDrawerClosed(View view) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
    }

    // actionbar over flow icon
    private void makeActionOverflowMenuShown() {
        // devices with hardware menu button (e.g. Samsung ) don't show action
        // overflow menu
        try {
            final ViewConfiguration config = ViewConfiguration.get(getActivity());
            final Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    /**
     *
     * Get the names and icons references to build the drawer menu...
     */
    private void setUpDrawer() {
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		/*
		 * mDrawerLayout.setScrimColor(getResources().getColor(
		 * android.R.color.transparent));
		 */
        mDrawerLayout.setDrawerListener(mDrawerListener);
        expListView = (ExpandableListView) getActivity().findViewById(R.id.list_slidermenu);
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        // expandable list view click listener

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public  boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {

				/*
				 * Here add your fragment class name for each case menu (eg.
				 * Layout1, layout2 in screen) you can add n number of classes
				 * to the swithch case Also when you add the class name here,
				 * also add the corresponding name to the array list
				 */
                    case 0:
                        onGoHome();
                        break;
                    case 1:
                        break;
                    case 2:
                    case 3:
                        onGoFindStore();
                        break;
                    case 4:
                        break;
                    case 5:
                        onGoEvent();
                        break;
                    case 6:
                        //onGoMonthlyList();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // setbackground color for list that is selected in child group
//                BaseFragment fragment = null;
                v.setSelected(true);
                if (view_Group != null) {
                    view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                    //view_Group.setBackgroundColor(Color.parseColor("#F21E1E"));
                }
                view_Group = v;
                //view_Group.setBackgroundColor(Color.parseColor("#F21E1E"));


//                getFragmentManager().beginTransaction()
//                        .replace(R.id.contents, fragment).commit();
                expListView.setItemChecked(childPosition, true);
//                mDrawerLayout.closeDrawer(expListView);

                switch (groupPosition) {
                    case 1:
                        if (childPosition == 0)
                            onGoTouchallIntroduce(TNConstants.WEBVIEW.TYPE_INTRO_COMPANY);
                        else if (childPosition == 1)
                            onGoTouchallIntroduce(TNConstants.WEBVIEW.TYPE_INTRO_TOUCHALL);
                        break;
                    case 4:
                        if (childPosition == 0)
                            onGoMyInfo();
                        else if (childPosition == 1)
                            onGoMyBonus();
                        else if (childPosition == 2)
                            onGoMyNews();
                        else if (childPosition == 3)
                            onGoMyNews();
                        break;
                    case 6:
                        if (childPosition == 0)
                            onGoNotice();
                        else if (childPosition == 1)
                            onGoFaq();
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void initRequest() {
//        requestMain();

//        TNPreference.setAutoLogin(getActivity(), true);
        if (mGcmData == null) {
            SLog.LogD("Ace : null");
            replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
        } else {
            SLog.LogD("Ace : null");
            SLog.LogD(mGcmData);
            try {
                JSONObject obj = new JSONObject(mGcmData);
                int idx = JSONParser.getInt(obj, "test", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (_bMyPointTest) {
            //requestFranchiseePoint("20181010", "102000", "0", "KR201808100003");
        }
        if (_bJuklibPointTest) {
            setPayResult("200501011200341KR201808100003T000000000000000000000000300000M00030501010000010090000000000000000");
        }
        if (_bCancelPointTest) {
            requestCancelPoint("200501011200343KR201808100003T000000000000000000000000300000M00030501010000010090000000000000000");
        }
//        if (_bDeviceSetupTest) {
//            requestDeviceSetup("200501011209174KR201808100003T00000000001001110300000005000000000000000100100018000010301");
//        }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Check if no view has focus:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public void onGoTouchallIntroduce(int type) {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, type);
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoFindStore() {

        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new FindStoreFragment();
        fragment.setArguments(b);
        TAG = FindStoreFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }

//        {
//            //Bundle b = new Bundle();
//            //b.putString("dialog_msg", getString(R.string.msg_success_guide_answer));
//            b.putString("dialog_msg", "지원 예정!");
//            MsgSingleDialog dialog = new MsgSingleDialog();
//            dialog.setArguments(b);
//            dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
//                @Override
//                public void onDialogResult(Object... objects) {
//                }
//            });
//            dialog.show(getFragmentManager(), "dialog");
//        }

        mDrawerLayout.closeDrawers();

    }

    public void onGoCheckUsePoint(String obj_message) {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new UseMyPointFragment();
        b.putString("obj_message", obj_message);
        fragment.setArguments(b);
        TAG = UseMyPointFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }


    public void onGoMyInfo() {

        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new UserInfoFragment();
        fragment.setArguments(b);
        TAG = UserInfoFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();

    }

    public void onGoMyBonus() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new CheckMyPointFragment();
        fragment.setArguments(b);
        TAG = CheckMyPointFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoMyNews() {
        mDrawerLayout.closeDrawers();
    }

    public void onGoEvent() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_EVENT);
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoNotice() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_NOTICE);
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoFaq() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FAQ);
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }


    private void onSideMenuClick(View view) {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";
//        switch (view.getId()) {
//            case R.id.btn_menu_intro:
//                break;
//            case R.id.btn_menu_intro_company:
//                onGoTouchallIntroduce(TNConstants.WEBVIEW.TYPE_INTRO_COMPANY);
//                break;
//            case R.id.btn_menu_intro_touchall:
//                onGoTouchallIntroduce(TNConstants.WEBVIEW.TYPE_INTRO_TOUCHALL);
//                break;
//            case R.id.btn_menu_find_store:
//                onGoFindStore();
////            {
////                //Bundle b = new Bundle();
////                //b.putString("dialog_msg", getString(R.string.msg_success_guide_answer));
////                b.putString("dialog_msg", "지원 예정!");
////                MsgSingleDialog dialog = new MsgSingleDialog();
////                dialog.setArguments(b);
////                dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
////                    @Override
////                    public void onDialogResult(Object... objects) {
////                    }
////                });
////                dialog.show(getFragmentManager(), "dialog");
////            }
//                break;
//            case R.id.btn_menu_check_mypoint:
//                //onGoCheckUsePoint();
//                onGoFindStore();
//                break;
//            case R.id.btn_menu_mypage:
//                //onGoMyPage();
//                break;
//            case R.id.btn_menu_my_info:
//                onGoMyInfo();
//                break;
//            case R.id.btn_menu_my_bonus:
//                onGoMyBonus();
//                break;
//            case R.id.btn_menu_my_news:
//                onGoMyNews();
//                break;
//            case R.id.btn_menu_event:
//                onGoEvent();
//                break;
//            case R.id.btn_menu_customer:
//                break;
//            case R.id.btn_menu_notice:
//                onGoNotice();
//                break;
//            case R.id.btn_menu_faq:
//                onGoFaq();
//                break;
////
////            case R.id.btn_menu_find_store:
////                //fragment = new FindStoreFragment();
////                //TAG = FindStoreFragment.FRAGMENT_TAG;
////
////                break;
////            case R.id.btn_menu_check_mypoint:
////                fragment = new CheckMyPointFragment();
////                TAG = CheckMyPointFragment.FRAGMENT_TAG;
////                break;
////            case R.id.btn_menu_use_mypoint:
////                fragment = new UseMyPointFragment();
////                TAG = UseMyPointFragment.FRAGMENT_TAG;
////                break;
////            case R.id.btn_menu_notice:
////                fragment = new UserNoticeFragment();
////                TAG = UserNoticeFragment.FRAGMENT_TAG;
////                break;
////            case R.id.btn_menu_event:
////                fragment = new EventFragment();
////                TAG = EventFragment.FRAGMENT_TAG;
////                break;
////            case R.id.btn_setting:
////            {
////                Intent i = new Intent(getActivity(), SettingActivity.class);
////                i.putExtra(TNConstants.UserSettingType.TYPE, 5);
////                startActivityForResult(i, TNConstants.REQUEST_CODE.SECESSION);
////            }
////                break;
//        }
        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }


    public void onRefresh() {
        requestMain();
    }

    private void requestMain() {
        ResMain res = new ResMain();
//        res.setParameter(TNPreference.getMemIndex(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
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
        switch (requestCode) {
            case TNConstants.REQUEST_CODE.REFRESH:
                if (resultCode == Activity.RESULT_OK) {
                    onRefresh();
                }
                break;
            case TNConstants.REQUEST_CODE.INSERT_POINT:
            case TNConstants.REQUEST_CODE.CANCEL_POINT:
                if (resultCode == Activity.RESULT_OK) {
                    String junmun = data.getStringExtra("junmun");
                    processNFC(junmun);
                }
                break;
            case TNConstants.REQUEST_CODE.CHECK_FRANCHISEE:
                if (resultCode == Activity.RESULT_OK) {
                    String junmun = data.getStringExtra("junmun");
                    if (junmun != null && junmun.length() > 0) {
                        processNFC(junmun);
                    }
                    else {
                        UserMainViewFragment fragment = (UserMainViewFragment) getFragmentManager().findFragmentByTag(UserMainViewFragment.FRAGMENT_TAG);
                        if (fragment != null) {
                            fragment.onReflash();
                        }
                    }
//                    int use_action = data.getIntExtra("use_action", 0);
//                    if (use_action != 0) {
//                        String obj_message = data.getStringExtra("obj_message");
//                        onGoCheckUsePoint(obj_message);
//                    }
                }
                break;
        }
    }

    private boolean mFlag = false;
    private Handler mExitHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mFlag = false;
            }
        }
    };

    public void onGoHome() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawers();
//        } else {
            replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
            mDrawerLayout.closeDrawers();
//        }
    }

    public boolean isDrawOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDraw() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onBack() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            if (!mFlag && getFragmentManager().getBackStackEntryCount() == 0) {
                Toast.makeText(getActivity(), getString(R.string.msg_exit_app), Toast.LENGTH_SHORT).show();
                mFlag = true;
                mExitHandler.sendEmptyMessageDelayed(0, TNConstants.EXIT_DELAY);
            } else {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***
     * pay result : 적립
     */

    public void setPayResult(String junmun) {

        Intent i = new Intent(getActivity(), InsertPointActivity.class);
        i.putExtra("point_junmun", junmun);
        startActivityForResult(i, TNConstants.REQUEST_CODE.INSERT_POINT);

    }

//    public void requestFranchiseePoint(String date, String time, String action, String device_serial) {
//
//        Intent i = new Intent(getActivity(), CheckFranchiseeActivity.class);
//        i.putExtra("device_serial", device_serial);
//        startActivityForResult(i, TNConstants.REQUEST_CODE.CHECK_FRANCHISEE);
//    }
    public void requestFranchiseePoint(String junmun) {

        Intent i = new Intent(getActivity(), CheckFranchiseeActivity.class);
        i.putExtra("device_serial", junmun);
        startActivityForResult(i, TNConstants.REQUEST_CODE.CHECK_FRANCHISEE);
    }

    public void requestCancelPoint(String junmun) {

        Intent i = new Intent(getActivity(), CancelPointActivity.class);
        i.putExtra("cancel_point", junmun);
        //startActivity(i);
        startActivityForResult(i, TNConstants.REQUEST_CODE.CANCEL_POINT);
    }

//    public void requestDeviceSetup(String junmun) {
//        Intent i = new Intent(getActivity(), DeviceSetupResultActivity.class);
//        i.putExtra("device_setup", junmun);
//        //startActivity(i);
//        startActivityForResult(i, TNConstants.REQUEST_CODE.DEVICE_SETUP);
//    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void processNFC(String msg) {

        String[] fields = msg.split("\\?");
        if (fields.length > 1) {
            String[] junmuns = msg.split("\\=");
            if (junmuns.length > 1 && junmuns[0].contains("point")) {
                String junmun = junmuns[1];
                if (junmun.substring(0, 1).compareTo("1") == 0)
                    setPayResult(junmun);//
                else if (junmun.substring(0, 1).compareTo("3") == 0 || junmun.substring(0, 1).compareTo("4") == 0)
                    requestCancelPoint(junmun);
            }
        }
        else {
            String[] junmuns = msg.split("\\=");
            if (junmuns.length > 1) {
                if (junmuns[0].contains("device_serial")) {
                    String junmun = junmuns[1];
                    requestFranchiseePoint(junmun);
                }
//                else if (junmuns[0].contains("cancel_point")) {
//                    String junmun = junmuns[1];
//
//                }
            }
        }
    }


    // Catch the events related to the drawer to arrange views according to this
    // action if necessary...
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
            //getActionBar().setTitle(mDrawerTitle);
            // calling onPrepareOptionsMenu() to hide action bar icons
            //invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            //getActionBar().setTitle(mTitle);
            // calling onPrepareOptionsMenu() to show action bar icons
            //invalidateOptionsMenu();
        }
    };

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        String[] array = getResources().getStringArray(R.array.nav_drawer_items);
        listDataHeader = Arrays.asList(array);

        // Adding child data
        // dash board
        List<String> l0 = new ArrayList<String>();
        String[] home = getResources().getStringArray(R.array.main_menu);
        l0 = Arrays.asList(home);

        // before you file
        List<String> l1 = new ArrayList<String>();
        String[] company_intros = getResources().getStringArray(R.array.menu_company);
        l1 = Arrays.asList(company_intros);

        // profile
        List<String> l2 = new ArrayList<String>();
        String[] store_info = getResources().getStringArray(R.array.menu_find_store);
        l2 = Arrays.asList(store_info);

        // income slip
        List<String> l3 = new ArrayList<String>();
        String[] store_intro = getResources().getStringArray(R.array.menu_my_point);
        l3 = Arrays.asList(store_intro);

        List<String> l4 = new ArrayList<String>();
        String[] users_info = getResources().getStringArray(R.array.menu_my_page);
        l4 = Arrays.asList(users_info);

        List<String> l5 = new ArrayList<String>();
        String[] detail_list = getResources().getStringArray(R.array.menu_event);
        l5 = Arrays.asList(detail_list);

        List<String> l6 = new ArrayList<String>();
        String[] monthly_list = getResources().getStringArray(R.array.menu_customer);
        l6 = Arrays.asList(monthly_list);

        // assigning values to menu and submenu
        listDataChild.put(listDataHeader.get(0), l0); // Header, Child
        // data
        listDataChild.put(listDataHeader.get(1), l1);
        listDataChild.put(listDataHeader.get(2), l2);
        listDataChild.put(listDataHeader.get(3), l3);
        listDataChild.put(listDataHeader.get(4), l4);
        listDataChild.put(listDataHeader.get(5), l5);
        listDataChild.put(listDataHeader.get(6), l6);
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context,
                                     List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition,
                    childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            List<String> childs = this._listDataChild.get(this._listDataHeader.get(groupPosition));
            TextView txt_plusminus = (TextView) convertView.findViewById(R.id.plus_txt);
            if (childs.size() == 0) {
                txt_plusminus.setText("");
            } else {
                if (isExpanded) {
                    txt_plusminus.setText("-");
                } else {
                    txt_plusminus.setText("+");
                }
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            // lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            // nav drawer icons from resources
            // navMenuIcons =
            // getResources().obtainTypedArray(R.array.nav_drawer_icons);
            // imgListGroup.setImageDrawable(navMenuIcons.getDrawable(groupPosition));

            // adding icon to expandable list view
            ImageView imgListGroup = (ImageView) convertView.findViewById(R.id.ic_txt);
            imgListGroup.setImageResource(_icons[groupPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
