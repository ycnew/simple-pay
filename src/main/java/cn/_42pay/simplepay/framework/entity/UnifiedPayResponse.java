package cn._42pay.simplepay.framework.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/29.
 */
@Getter
@Setter
public class UnifiedPayResponse {
	private String payCode;
	private String frontSubmitForm;
	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String prepayId;
	private String signType;
	private String paySign;
}
