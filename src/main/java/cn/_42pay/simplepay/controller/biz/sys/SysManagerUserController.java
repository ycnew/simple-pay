package cn._42pay.simplepay.controller.biz.sys;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.service.sys.impl.SysUserServiceImpl;
import cn._42pay.simplepay.vo.sys.BaseResponseVo;
import cn._42pay.simplepay.vo.sys.UserModifyInfoRequestVo;
import cn._42pay.simplepay.vo.sys.UserModifyPwdRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by kevin on 2018/7/17.
 */
@Component
@Controller
@RequestMapping("/sys/manager/user")
public class SysManagerUserController {
	@Autowired
	SysUserServiceImpl sysUserServiceImpl;

	@RequestMapping(value = "/modifyPwd")
	public ModelAndView modifyPwd() throws IOException {
		return new ModelAndView("/sys/admin/modifyPwd");
	}

	@RequestMapping(value = "/userModifyPwd")
	public @ResponseBody
	BaseResponseVo userModifyPwd(@Validated UserModifyPwdRequestVo userModifyPwdRequestVo, HttpSession session) throws IOException {
		BaseResponseVo baseResponseVo = new BaseResponseVo();
		User user = HttpServerSession.getSysUserLoginSession(session);
		//判断旧密码是否正确
		try {
			sysUserServiceImpl.checkUserLogin(user.getLoginName(), userModifyPwdRequestVo.getOldPassword());
		} catch (SimplePayException spe) {
			baseResponseVo.setResultCode(spe.getResultCode());
			baseResponseVo.setResultMessage(spe.getMessage());
			return baseResponseVo;
		}
		try {
			sysUserServiceImpl.modifyUserPassword(user.getUserId(), userModifyPwdRequestVo.getPassword());
		} catch (SimplePayException spe) {
			baseResponseVo.setResultCode(spe.getResultCode());
			baseResponseVo.setResultMessage(spe.getMessage());
			return baseResponseVo;
		}

		baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
		return baseResponseVo;
	}

	@RequestMapping(value = "/userInfo")
	public ModelAndView userInfo(HttpSession session) throws IOException {
		return new ModelAndView("/sys/admin/userInfo");
	}

	@RequestMapping(value = "/userInfoModify")
	public @ResponseBody
	BaseResponseVo userInfoModify(@Validated UserModifyInfoRequestVo userModifyInfoRequestVo, HttpSession session) throws IOException {
		BaseResponseVo baseResponseVo = new BaseResponseVo();
		User user = HttpServerSession.getSysUserLoginSession(session);
		try {
			sysUserServiceImpl.modifyUserInfoById(user.getUserId(),
					userModifyInfoRequestVo.getEmail(),
					userModifyInfoRequestVo.getMobile());
		} catch (SimplePayException spe) {
			baseResponseVo.setResultCode(spe.getResultCode());
			baseResponseVo.setResultMessage(spe.getMessage());
			return baseResponseVo;
		}

		User updateUser=sysUserServiceImpl.getUserById(user.getUserId());
		HttpServerSession.setSysUserLoginSession(session,updateUser);
		baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
		return baseResponseVo;
	}
}
