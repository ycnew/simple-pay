package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/16.
 */
@Getter
@Setter
public class UserAddRequestVo {
	@NotBlank(message="必填")
	private String loginName;
	@NotBlank(message="必填")
	private String password;
	private String email;
	private String mobile;
	@NotBlank(message="必填")
	private String validateCode;
}
