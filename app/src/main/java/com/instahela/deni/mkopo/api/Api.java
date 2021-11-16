package com.instahela.deni.mkopo.api;

public class Api {
    //------------------注册------------------------------------
    //发送短信的Api
    public static final String SEND_SMS_URL = "http://api.kenyacash.one/service/v1/account/captcha";
    //验证短信
    public static final String CHECK_SMS_VERIFICATION = "http://api.kenyacash.one/service/v1/account/captcha/check";
    // 注册
    public static final String REGISTER_URL = "http://api.kenyacash.one/service/v1/account/register";
    //登录
    public static final String SIGN_IN_URL = "http://api.kenyacash.one/service/v1/account/login";

    public static final String LOG_OUT_URL = "http://api.kenyacash.one/service/v1/account/logout";

    public static final String CHECK_PHONE_NUM_URL = "http://api.kenyacash.one/service/v1/account/mobile/check";
    //---------------首页--------------------------------------------
    //home详情页
    public static final String LOAN_DETAIL_URL = "http://api.kenyacash.one/service/v1/loan/detail";
    //贷款产品详情
    public static final String PRODUCTS_URL = "http://api.kenyacash.one/service/v1/loan/products";

    //-----------------贷款页-------------------------------------------------------------
    //提交,个人信息修改
    public static final String COMMIT_PERSON_PROFILE_URL = "http://api.kenyacash.one/service/v1/account/profile";

    //查询个人信息
    public static final String REQUEST_PERSON_PROFILE_URL = "http://api.kenyacash.one/service/v1/account/profile/detail";

    //系统字典查询
    public static final String DICT_DETAIL_URL = "http://api.kenyacash.one/service/v1/dict/detail";
    //文案详情查询
    public static final String TEXT_DETAIL_URL = "http://api.kenyacash.one/service/v1/text/detail";

    //添加additional profile 的url
    public static final String ADD_ADDITIONAL_PROFILE_URL = "http://api.kenyacash.one/service/v1/account/profile_other";
    //请求additional profile 详情的url
    public static final String DETAIL_ADDITIONAL_PROFILE_URL = "http://api.kenyacash.one/service/v1/account/profile_other/detail";

    //贷款试结算
    public static final String LOAN_PROFILE_TRIAL_URL = "http://api.kenyacash.one/service/v1/loan/trial";
    //贷款条件检测
    public static final String LOAN_PROFILE_CHECK_URL = "http://api.kenyacash.one/service/v1/loan/check";
    //贷款申请
    public static final String LOAN_PROFILE_APPLY_URL = "http://api.kenyacash.one/service/v1/loan/apply";
    //贷款还款
    public static final String LOAN_REPAY_URL = "http://api.kenyacash.one/service/v1/loan/repay";

    //---------------------------信息采集--------------------------------------------------
    public static final String HARE_WARE_COLLECT_URL = "http://api.kenyacash.one/service/v1/account/hardware";

    public static final String USER_INFO_UPLOAD_URL = "http://api.kenyacash.one/service/v1/account/auth/upload";


    //--------------------------设置-------------------------------------------------
    //未读消息数
    public static final String UNREAD_MESSAGE_URL = "http://api.kenyacash.one/service/v1/station/unread";
    //消息列表
    public static final String MESSAGE_LIST_URL = "http://api.kenyacash.one/service/v1/station/list";

    // webview 链接

    public static final String WEB_VIEW_INDEX = "https://policy.kenyacash.one/index.html";

    public static final String WEB_VIEW_POLICY = "https://policy.kenyacash.one/privacy_policy.html";

    public static final String WEB_VIEW_TERM = "https://policy.kenyacash.one/terms.html";

    // 上报fcm
    public static final String REPORT_FCM_TOKEN = "http://api.kenyacash.one/service/v1/account/fcmtoken";

    //借款成功
    public static final String LOAN_SUCCESS = "http://api.kenyacash.one/service/v1/sms/loansucc";

    //借款失败
    public static final String LOAN_FAILURE = "http://api.kenyacash.one/service/v1/sms/loanfail";

    //还款消息提醒
    public static final String REPAY_SMS = "http://api.kenyacash.one/service/v1/sms/repayment";
}
