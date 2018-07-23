package cn._42pay.simplepay.core.bo;

import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class PayBaseRequestBo {
	private String requestPayAppId;
	private String requestMerchantId;
	private String requestPayUserId;
	private PayCodeEnum payCodeEnum;
	private PaymentSetting paymentSetting;
}
