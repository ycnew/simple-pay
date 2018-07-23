package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class RefundResponseBo extends PayBaseResponseBo {
	private Integer refundAmount;
	private String refundPaymentDealNo; //支付系统退款订单号
	private String refundPaymentDealId; //第三方支付退款流水号
	private String refundTime;
}
