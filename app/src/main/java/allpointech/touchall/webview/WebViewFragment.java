package allpointech.touchall.webview;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import allpointech.touchall.R;
import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.webclient.BaseWebChromeClient;
import allpointech.touchall.network.webclient.BaseWebViewClient;
import allpointech.touchall.user.UserMainFragment;
import allpointech.touchall.user.usermain.UserMainViewFragment;
import allpointech.touchall.utils.TNConstants;
import allpointech.touchall.utils.TNPreference;

/**
 * Created by Ace on 2016-12-06.
 */

public class WebViewFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener {
    public static final String FRAGMENT_TAG = WebViewFragment.class.getSimpleName();
    private Toolbar mToolbar;
    private WebView mWebView;

    private int mWebViewType;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mWebViewType = getArguments().getInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, 0);
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        String title = "";
        String url = HttpInfo.HTTP_HOST;
        if (TNConstants.WEBVIEW.TYPE_INTRO_COMPANY == mWebViewType) {
            url = TNConstants.WEBVIEW.URL_INTRO_COMPANY;
            title = "회사소개";//getString(R.string.toolbar_help);
        } else if (TNConstants.WEBVIEW.TYPE_INTRO_TOUCHALL == mWebViewType) {
            url = TNConstants.WEBVIEW.URL_INTRO_TOUCHALL;
            title = "터치올소개";//getString(R.string.toolbar_help);
        } else if (TNConstants.WEBVIEW.TYPE_EVENT == mWebViewType) {
            url = TNConstants.WEBVIEW.URL_EVENT;
            title = "이벤트";//getString(R.string.toolbar_notice);
        } else if (TNConstants.WEBVIEW.TYPE_NOTICE == mWebViewType) {
            url = TNConstants.WEBVIEW.URL_NOTICE;
            title = "공지사항";//getString(R.string.toolbar_policy);
        } else if (TNConstants.WEBVIEW.TYPE_FAQ == mWebViewType) {
            url = TNConstants.WEBVIEW.URL_FAQ;
            title = "자주묻는 질문";//getString(R.string.toolbar_tou);
        }
        //url = "http://www.yahoo.com";
        mToolbar.setTitle(title);

        mWebView = (WebView) view.findViewById(R.id.web_view);
        settingWebview(mWebView);
        mWebView.setWebChromeClient(new BaseWebChromeClient(getFragmentManager()));
        mWebView.setWebViewClient(new BaseWebViewClient());

        mWebView.clearCache(true);
        mWebView.loadUrl(url);
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

    }

    private void settingWebview(WebView webview) {
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setUseWideViewPort(true);
        webview.setVerticalScrollbarOverlay(true);
        webview.getSettings().setJavaScriptEnabled(true);

        //WebSettings set = webview.getSettings();
        //set.setUserAgentString(set.getUserAgentString() + "/APP_SWC_Android/Version=");

//        webview.getSettings().setUserAgentString(webview.getSettings().getUserAgentString());
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setGeolocationEnabled(true);
//        webview.getSettings().setUseWideViewPort(true);
//        webview.getSettings().setSaveFormData(false);
//        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
//        webview.getSettings().setSupportMultipleWindows(false);
//        webview.getSettings().setDatabaseEnabled(true);
//        webview.getSettings().setDomStorageEnabled(true);
//
//        webview.getSettings().setLoadsImagesAutomatically(true);
//        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onBack() {
//        if (TNConstants.MemberType.USER.equalsIgnoreCase(TNPreference.getMemberType(getActivity()))) {
//            replaceAnimationTagFragment(R.id.contents, new AskFragment(), AskFragment.FRAGMENT_TAG, 0, 0);
//        } else {
//            replaceAnimationTagFragment(R.id.contents, new AnswerFragment(), AnswerFragment.FRAGMENT_TAG, 0, 0);
//        }

        //replaceAnimationTagFragment(R.id.contents, new UserMainViewFragment(), UserMainViewFragment.FRAGMENT_TAG, 0, 0);
        UserMainFragment fragment = (UserMainFragment) getFragmentManager().findFragmentByTag(UserMainFragment.FRAGMENT_TAG);
        if (fragment != null) {
            fragment.onGoHome();
        }
    }
}
