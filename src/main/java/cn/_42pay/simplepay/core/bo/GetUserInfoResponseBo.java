package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/26.
 */
@Getter
@Setter
public class GetUserInfoResponseBo extends PayBaseResponseBo{
	private String payUserId;//第三方支付上用户的唯一标识
	private String userName;
	private String userSex;//M-男 F-女
	private String idCardNo;
	private String birth;
	private String mobile;
}
