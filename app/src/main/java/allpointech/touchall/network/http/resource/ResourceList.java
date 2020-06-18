package allpointech.touchall.network.http.resource;

import java.util.HashMap;

public class ResourceList {
    public static final HashMap<String, String> API_MAP = new HashMap<>();

    static {
        // Common
        API_MAP.put("ResNotice", "notice/board_list"); // notice

        //Login
        //API_MAP.put("ResJoin", "member/member_reg_in"); // join
        API_MAP.put("ResJoin", "user"); // join
        API_MAP.put("ResUserInfoUpdate", "user"); // join
        API_MAP.put("ResFind", "member/member_pwd_reset_send_email"); // find
        API_MAP.put("ResLogin", "login"); // Login
        API_MAP.put("ResConsignment", "login/req_consignment"); // consignment
        API_MAP.put("ResTerms", "login/req_terms"); // terms
        API_MAP.put("ResSMSReq", "user/cert"); // sms_req
        API_MAP.put("ResSMSCheck", "user/cert/"); // sms_req
        API_MAP.put("ResBankReq", "code/BANK"); // sms_req
        API_MAP.put("ResStates", "area"); // sms_req
        API_MAP.put("ResSubStates", "area/"); // sms_req
        API_MAP.put("ResCitys", "area"); // sms_req
        API_MAP.put("ResCode", "code"); // sms_req
        API_MAP.put("ResSubCitys", "area/"); // sms_req
        API_MAP.put("ResSector", "sector"); // sms_req
        API_MAP.put("ResSubSector", "sector/"); // sms_req
        API_MAP.put("ResJuklib", "point"); // sms_req
        API_MAP.put("ResGame", "point"); // sms_req
        API_MAP.put("ResUsePoint", "point/approval"); // sms_req
        API_MAP.put("ResUseMyPointT", "point/amount/"); // sms_req
        API_MAP.put("ResUseMyPointG", "point/amount/"); // sms_req
        API_MAP.put("ResUseMyPointS", "point/amount/"); // sms_req
        API_MAP.put("ResUsePointJunmun", "point"); // sms_req
        API_MAP.put("ResReqPoint", "point/amount"); // sms_req
        API_MAP.put("ResFindMyPoint", "point"); // sms_req
        API_MAP.put("ResReqFranchiseePoint", "point/amount"); // sms_req
        API_MAP.put("ResDeviceSetup", "device/sync"); // sms_req
        API_MAP.put("ResInfo", "user/info"); // sms_req
        API_MAP.put("ResStore", "code"); // sms_req
        API_MAP.put("ResBusinessType", "sector/"); // sms_req
        API_MAP.put("ResBusinessDetail", "sector/"); // sms_req
        API_MAP.put("ResFindStore", "store/pointList"); // sms_req
        API_MAP.put("ResStoreInfo", "store"); // sms_req


        API_MAP.put("ResReqTelegram", "telegram"); // sms_req

        //main
        API_MAP.put("ResMain", "main/main_view"); // main

    }
}
