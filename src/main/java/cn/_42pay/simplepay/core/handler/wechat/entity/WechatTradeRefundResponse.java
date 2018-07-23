package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by kevin on 2018/7/6.
 */
@Getter
@Setter
public class WechatTradeRefundResponse extends WechatBaseResponse {
    private String transactionId;

    private String outTradeNo;

    private String outRefundNo;

    private String refundId;

    private String refundFee;

    private String totalFee;

    private String cashFee;
}
