package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/17.
 */
@Getter
@Setter
public class UserModifyPwdRequestVo {
	@NotBlank(message="必填")
	private String password;
	@NotBlank(message="必填")
	private String oldPassword;
}
