package allpointech.touchall.network.http.resource.data;


import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.resource.ResBaseHttp;

public class ResStore extends ResBaseHttp {
    /**
     * @Desc : 보내는키
     */
    public class KEY_REQ {

    }

    /**
     * @Desc : 받는키
     */
    public class KEY_RES {
        public static final String message = "message";
    }

    // request
    public void setParameterQuestion(String... params) {
    }


    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET;
    }
}
