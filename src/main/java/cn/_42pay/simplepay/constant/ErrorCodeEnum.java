package cn._42pay.simplepay.constant;


import cn._42pay.simplepay.framework.exception.SimplePayException;

/**
 * Created by kevin on 2018/6/27.
 */
public enum ErrorCodeEnum {
    SUCCESS(0,"成功"),
    DEFUALT_ERROR(1,"系统错误"),
    NOT_IMPLEMENT_THIS_INTERFACE(2,"没有实现此接口"),
    NOT_EXTERN_THIS_SERVICE(3,"没有继承该服务"),
    NOT_SUPPORT_THIS_PAY_CHANNEL(4,"不支持该支付渠道"),
    PAY_APP_ID_CAN_NOT_EMPTY(5,"payAppId参数不能为空"),
    PAY_PAYMENT_SETTING_CAN_NOT_EMPTY(6,"支付配置参数不能为空"),
    CALL_METHOD_EXCEPTION(7,"反射调用异常"),
    ORDER_IS_PAYED(8,"订单已经支付了，请勿重复支付"),
    INSRT_PAYMENT_JOURNAL_ERROR(9,"插入支付流水失败"),
    UPDATE_PAYMENT_JOURNAL_ERROR(9,"更新支付流水失败"),
    URL_DECODE_EXCEPTION(10,"UrlDecode异常"),
    BARCODE_PAY_FAIL(11,"条码付支付失败"),

    ALIPAY_EXCETION(1001,"支付宝接口异常"),
    ALIPAY_AUTH_ERROR(1002,"支付宝authCode换取accessToken失败"),
    ALIPAY_BARCODE_PAY_FAIL(1003,"支付宝条码付失败"),
    ALIPAY_TRADE_QUERY_FAIL(1004,"支付宝交易查询失败"),
    ALIPAY_TRADE_CANCEL_FAIL(1005,"支付宝撤销交易失败"),
    ALIPAY_TRADE_REFUND_FAIL(1006,"支付宝退款失败"),

    WECHAT_EXCETION(2001,"微信接口异常"),
    WECHAT_AUTH_ERROR(2002,"微信authCode换取accessToken失败"),
    WECHAT_VERIFY_SIGN_ERROR(2003,"微信验证签名出错"),
    WECHAT_REFUND_FAIL(2004,"微信退款失败"),
    WECHAT_TRADE_QUERY_FAIL(2005,"微信交易查询失败"),
    WECHAT_CANCEL_FAIL(2006,"微信订单撤销失败"),
    WECHAT_BARCODE_PAY_FAIL(2007,"微信刷卡付失败"),

    NOTIFY_EXCETION(3001,"回调通知异常"),
    NOTIFY_FIND_PAYMENT_EXCETION(3002,"回调通知异常:找不到支付配置"),
    NOTIFY_WECHAT_EXCETION(3003,"微信回调通知异常"),
    NOTIFY_WECHAT_XMLTOMAP_EXCETION(3004,"微信回调XML转换MAP异常"),
    NOTIFY_WECHAT_SIGN_VERIFY_EXCETION(3005,"微信回调签名验证失败"),
    NOTIFY_ALIPAY_EXCETION(3006,"支付宝回调通知异常"),
    NOTIFY_ALIPAY_TRADE_NOF_SUCCESS_EXCETION(3007,"支付宝回调交易未完成"),
    NOTIFY_ALIPAY_SIGN_VERIFY_EXCETION(3008,"支付宝回调签名验证失败"),

    SYS_USER_ADD_FAIL(4001,"用户添加异常"),
    SYS_VALIDATE_CODE_VERIFY_FAIL(4002,"验证码校验失败"),
    SYS_USER_NOT_EXIST(4003,"登录名不存在"),
    SYS_USER_LOGIN_ERROR(4004,"登录名或密码不正确"),
    SYS_USER_MODIFY_PASSWORD_FAIL(4005,"用户修改密码异常"),
    SYS_USER_MODIFY_USERINFO_FAIL(4006,"用户修改异常"),
    SYS_PAYMENT_JOURNAL_EMPTY(4007,"查询不到对应的支付流水记录"),
    SYS_PAYMENT_DEAL_NO_EMPTY(4008,"交易流水号不能为空"),
    SYS_PAYMENT_SETTING_EMPTY(4009,"交易配置不存在"),
    SYS_UPLOAD_FILE_EMPTY(4010,"上传文件为空"),
    SYS_UPLOAD_FILE_EXCEPTION(4011,"上传文件IO异常"),
    SYS_USER_EXIST(4012,"用户已经注册了"),
    ;

    private String message;
    private int index;

    private ErrorCodeEnum(int index, String name) {
        this.message = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (ErrorCodeEnum c : ErrorCodeEnum.values()) {
            if (c.getIndex() == index) {
                return c.message;
            }
        }
        return null;
    }

    public static ErrorCodeEnum getEnum(int index) throws SimplePayException {
        for (ErrorCodeEnum c : ErrorCodeEnum.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        throw new SimplePayException(DEFUALT_ERROR);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIndex() {
        return index;
    }

    public String getCode(){return String.valueOf(this.index);}

    public void setIndex(int index) {
        this.index = index;
    }
}
