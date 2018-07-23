package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/16.
 */
@Getter
@Setter
public class WechatMicroPayResponse  extends WechatBaseResponse{
	private String openid;

	private String isSubscribe;

	private String tradeType;

	private String bankType;

	private String feeType;

	private String totalFee;

	private String settlementTotalFee;

	private String couponFee;

	private String cashFeeType;

	private String cashFee;

	private String transactionId;

	private String outTradeNo;

	private String attach;

	private String timeEnd;

	private String promotionDetail;
}
