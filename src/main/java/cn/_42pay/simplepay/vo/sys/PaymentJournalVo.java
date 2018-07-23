package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/18.
 */
@Getter
@Setter
public class PaymentJournalVo {
	private String paymentDealNo;
	/*支付appid*/
	private String payAppId;
	/*支付商户号*/
	private String merchantId;
	/*用户订单号*/
	private String merchantOrderNo;
	/*第三方交易流水号*/
	private String paymentDealId;
	/*0-创建（待支付）              1-唤起收银台              2-支付              3-退款*/
	private String payStatus;
	/*支付编码*/
	private String payCode;
	private String totalAmount;
	/*描述*/
	private String description;
	/*创建时间*/
	private String createTime;
	/*交易时间*/
	private String payTime;
}
