package cn._42pay.simplepay.framework.entity;

import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.db.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户选择的支付参数
 * Created by kevin on 2018/6/26.
 */
@Getter
@Setter
public class UserSelectPaySetting {
	private String payAppId;
	private String merchantId;
	private String payCode;
	private Long userId;
	private PaymentSetting paymentSetting;
	private User user;
}
