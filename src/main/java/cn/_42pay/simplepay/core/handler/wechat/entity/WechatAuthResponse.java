package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by kevin on 2018/7/4.
 */
@Getter
@Setter
public class WechatAuthResponse implements Serializable {

	private String accessToken;

	private String expiresIn;

	private String refreshToken;

	private String openid;

	private String scope;

	private String errcode;

	private String errmsg;

}
