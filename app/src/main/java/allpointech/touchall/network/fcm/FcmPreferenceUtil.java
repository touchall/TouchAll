package allpointech.touchall.network.fcm;

import android.content.Context;

/**
 * Created by jay on 09/04/2019.
 */

public class FcmPreferenceUtil extends allpointech.touchall.network.fcm.BasePreferenceUtil {

    private static FcmPreferenceUtil _instance = null;
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static synchronized FcmPreferenceUtil instance(Context $context) {
        if (_instance == null)
            _instance = new FcmPreferenceUtil($context);
        return _instance;
    }

    protected FcmPreferenceUtil(Context $context) {
        super($context);
    }

    public void putRedId(String $regId) {
        put(PROPERTY_REG_ID, $regId);
    }

    public String regId() {
        return get(PROPERTY_REG_ID);
    }

    public void putAppVersion(int $appVersion) {
        put(PROPERTY_APP_VERSION, $appVersion);
    }

    public int appVersion() {
        return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
    }

}