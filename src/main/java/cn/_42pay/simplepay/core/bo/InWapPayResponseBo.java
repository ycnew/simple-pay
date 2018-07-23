package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class InWapPayResponseBo extends PayBaseResponseBo {
	private String frontSubmitForm;
	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String prepayId;
	private String signType;
	private String paySign;
}
