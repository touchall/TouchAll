package allpointech.touchall.network.http.resource.data;


import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.MultiPartData;
import allpointech.touchall.network.http.resource.ResBaseHttp;

public class ResGame extends ResBaseHttp {

    @Override
    public String makeType() throws Exception {
        //return super.makeType();
        return HttpInfo.POST;
    }

    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {
        public static final String rps = "rps";

    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String message = "message";

        public static final String result = "result";
        public static final String show = "show";
    }

    // request
    public void setParameter(String... params) {
        setMultipartData(MultiPartData.FORM, KEY_REQ.rps, params[0]);
    }
}
