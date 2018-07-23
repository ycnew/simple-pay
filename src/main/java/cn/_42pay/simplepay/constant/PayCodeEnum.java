package cn._42pay.simplepay.constant;

/**
 * Created by kevin on 2018/6/19.
 */
public enum PayCodeEnum {
    WECHAT("WECHAT","微信支付"),
	ALIPAY("ALIPAY","支付宝支付"),

	WX_H5("WX_H5","微信手机网站H5支付"),
	WX_JSAPI("WX_JSAPI","微信公众号支付"),
	WX_MICROPAY("WX_MICROPAY","微信刷卡付"),

	ALIPAY_WEB("ALIPAY_WEB","支付宝电脑网站支付"),
	ALIPAY_WAP("ALIPAY_WAP","支付宝手机网站支付"),
	ALIPAY_BARPAY("ALIPAY_BARPAY","支付宝条码支付"),
	;

	private String code;
	private String desc;

	PayCodeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
