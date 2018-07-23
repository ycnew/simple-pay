package cn._42pay.simplepay.core.handler.wechat.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/16.
 */
@Getter
@Setter
public class WechatCancelResponse extends WechatBaseResponse {
	private String recall;
}
