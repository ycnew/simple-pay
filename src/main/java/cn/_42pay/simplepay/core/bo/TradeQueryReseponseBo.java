package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class TradeQueryReseponseBo extends PayBaseResponseBo {
	private String paymentDealId;
	private String payUserId;
	private Integer payAmount;
	private String paymentDealNo;
	private String payTime;
	/**
	 * SUCCESS—支付成功
	 * REFUND—转入退款
	 * NOTPAY 未支付
	 * USERPAYING-支付中
	 * PAYERROR-支付失败
	 * CLOSED-已经关闭
	 * CANCEL-撤销
	 */
	private String tradeState; //
}
