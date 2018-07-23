package cn._42pay.simplepay.framework.log.constant;

/**
 * Created by kevin on 2017/12/4.
 * 3位数字表示一种场景，场景最小数字为100，开始递增
 */
public enum LogScene {
    //----------------------------------------------------------------------------
    SYSTEM_ERROR                        ("100","系统错误"),
    //-----------------------------------------------------------------------------
    AUTH                                ("200","授权"),
    AUTH_ALIPAY                         ("200100","授权_支付宝授权"),
    AUTH_WECHAT                         ("200101","授权_微信授权"),
    //------------------------------------------------------------------------------
    PAGE_REQUEST                        ("300","页面请求"),
    //-------------------------------------------------------------------------------
    PAY                                 ("400","支付"),
    PAY_BARCODE                         ("401","条码支付"),
    PAY_ALIPAY_WAP_PAY                  ("400100","支付_支付宝WAP支付"),
    PAY_ALIPAY_WEB_PAY                  ("400101","支付_支付宝WEB支付"),
    PAY_ALIPAY_BARCODE_PAY              ("400102","支付_支付宝条码支付"),
    PAY_ALIPAY_TRADE_QUERY              ("400103","支付_支付宝交易查询"),
    PAY_ALIPAY_REFUND                   ("400104","支付_支付宝退费"),
    PAY_ALIPAY_CANCEL                   ("400105","支付_支付宝订单撤销"),
    PAY_WECHAT_WAP_PAY                  ("400200","支付_微信WAP支付"),
    PAY_WECHAT_TRADE_QUERY              ("400203","支付_微信交易查询"),
    PAY_WECHAT_REFUND                   ("400204","支付_微信退费"),
    PAY_WECHAT_CANCEL                   ("400205","支付_微信订单撤销"),
    PAY_WECHAT_BARCODE_PAY              ("400206","支付_微信刷卡付"),
    //-------------------------------------------------------------------------------
    NOTIFY                              ("500","支付回调"),
    NOTIFY_ALIPAY                       ("500100","支付宝支付回调"),
    NOTIFY_WECHAT                      ("500101","微信支付回调"),
    //--------------------------------------------------------------------------------
    THREAD_POOL                       ("600","线程池"),
    //--------------------------------------------------------------------------------
    MANAGER                              ("700","用户后台管理"),
    //--------------------------------------------------------------------------------
    PERSOANAL_TRANSER                              ("800","个人转账"),
    ;

    private String code;

    private String desc;

    LogScene(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
