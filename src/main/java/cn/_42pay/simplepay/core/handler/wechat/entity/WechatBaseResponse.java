package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by kevin on 2018/7/16.
 */
@Getter
@Setter
public class WechatBaseResponse implements Serializable {
	private String returnCode;

	private String returnMsg;

	private String appid;

	private String mchId;

	private String deviceInfo;

	private String nonceStr;

	private String sign;

	private String resultCode;

	private String errCode;

	private String errCodeDes;

	private static final String SUCCESS = "SUCCESS";

	private static final String FAIL="FAIL";

	public boolean isReturnCodeSuccess() {
		return SUCCESS.equalsIgnoreCase(returnCode);
	}

	public boolean isResultCodeSuccess() {
		return SUCCESS.equalsIgnoreCase(resultCode);
	}

	public boolean isResultCodeFail(){return FAIL.equalsIgnoreCase(resultCode); }
}
