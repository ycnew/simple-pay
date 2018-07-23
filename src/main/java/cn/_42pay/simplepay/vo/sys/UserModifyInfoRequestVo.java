package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/17.
 */
@Getter
@Setter
public class UserModifyInfoRequestVo {
	@NotBlank(message="必填")
	private String email;
	@NotBlank(message="必填")
	private String mobile;
}
