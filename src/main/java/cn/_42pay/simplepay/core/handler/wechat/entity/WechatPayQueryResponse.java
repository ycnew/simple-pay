package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lullaby on 2017/5/19.
 */
@Getter
@Setter
public class WechatPayQueryResponse extends WechatBaseResponse {

    private String openid;

    private String isSubscribe;

    private String tradeType;

    private String tradeState;

    private String bankType;

    private String totalFee;

    private String feeType;

    private String cashFee;

    private String cashFeeType;

    private String couponFee;

    private String couponCount;

    private String transactionId;

    private String outTradeNo;

    private String attach;

    private String timeEnd;

    private String tradeStateDesc;

}
