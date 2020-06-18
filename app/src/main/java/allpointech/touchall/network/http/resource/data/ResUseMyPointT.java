package allpointech.touchall.network.http.resource.data;


import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.MultiPartData;
import allpointech.touchall.network.http.resource.ResBaseHttp;

public class ResUseMyPointT extends ResBaseHttp {

    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET;
    }

    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
        public static final String device_serial = "device_serial";
        public static final String earn = "earn";
    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String result = "result";
        public static final String message = "message";
    }

    // request
    public void setParameterQuestion(String... params) {
        setGetUrlData(params[0]);
    }
}
