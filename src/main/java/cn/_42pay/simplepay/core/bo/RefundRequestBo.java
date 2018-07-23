package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class RefundRequestBo extends PayBaseRequestBo {
	private Integer refundAmount;
	private String refundPaymentDealNo;
	private String paymentDealNo;
	private String paymentDealId;
}
