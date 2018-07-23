package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WechatJsapiPayResponse extends WechatBaseResponse {

    private String tradeType;

    private String prepayId;

}
