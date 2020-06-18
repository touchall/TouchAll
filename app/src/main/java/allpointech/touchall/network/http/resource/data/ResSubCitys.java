package allpointech.touchall.network.http.resource.data;


import allpointech.touchall.network.http.HttpInfo;
import allpointech.touchall.network.http.resource.ResBaseHttp;

public class ResSubCitys extends ResBaseHttp {

    @Override
    public String makeType() throws Exception {
        return HttpInfo.GET;
        //return super.makeType();
    }

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
        setGetUrlData(params[0]);
    }
}
