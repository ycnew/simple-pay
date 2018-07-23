package cn._42pay.simplepay.report.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/6.
 */
@Getter
@Setter
public class PayReportResponse {
	/**
	 * 第三方支付公司流水ID
	 */
	private String paymentDealId;

	/**
	 * 交易系统产生的支付流水号
	 */
	private String paymentDealNo;

	/**
	 * 商户订单号
	 */
	private String merchantOrderNo;

	/**
	 * 自费金额
	 */
	private Integer payAmount;

	/**
	 * 第三方支付appId
	 */
	private String payAppId;

	/**
	 * 支付的userId 微信 openid 支付宝 alipayUserId
	 */
	private String payUserId;

	/**
	 * 商户号
	 */
	private String merchantId;

	/**
	 * 支付时间 yyyy-MM-dd HH:mm:ss
	 */
	private String payTime;

	/**
	 * 支付的渠道
	 */
	private String payCode;

	/**
	 * 签名
	 */
	private String sign;
}
