package cn._42pay.simplepay.controller.biz.sys;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.util.ValidateCodeUtil;
import cn._42pay.simplepay.service.sys.impl.SysUserServiceImpl;
import cn._42pay.simplepay.vo.sys.BaseResponseVo;
import cn._42pay.simplepay.vo.sys.UserAddRequestVo;
import cn._42pay.simplepay.vo.sys.UserLoginRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by kevin on 2018/7/16.
 */
@Component
@Controller
@RequestMapping("/sys/user")
public class SysUserContoller {
	@Autowired
	SysUserServiceImpl sysUserServiceImpl;

	@RequestMapping(value = "/login")
	public ModelAndView login() throws IOException {
		return new ModelAndView("/sys/login");
	}

	@RequestMapping(value = "/loginOut")
	public ModelAndView loginOut(HttpSession session) throws IOException {
		HttpServerSession.removeSysUserLoginSession(session);
		return new ModelAndView("/sys/login");
	}

	@RequestMapping(value = "/checkLogin")
	public @ResponseBody
	BaseResponseVo checkLogin(@Validated UserLoginRequestVo userLoginRequestVo, HttpServletRequest request) throws IOException {
		BaseResponseVo baseResponseVo =new BaseResponseVo();
		String sessionValidateCode=HttpServerSession.getSysUserLoginValidateCode(request.getSession());
		if(sessionValidateCode==null||!sessionValidateCode.equalsIgnoreCase(userLoginRequestVo.getValidateCode())){
			baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SYS_VALIDATE_CODE_VERIFY_FAIL);
			return baseResponseVo;
		}

		try{
			User user=sysUserServiceImpl.checkUserLogin(userLoginRequestVo.getLoginName(),userLoginRequestVo.getPassword());
			baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
			HttpServerSession.setSysUserLoginSession(request.getSession(),user);
		}catch (SimplePayException spe){
			baseResponseVo.setResultCode(spe.getResultCode());
			baseResponseVo.setResultMessage(spe.getMessage());
		}
		return baseResponseVo;
	}

	@RequestMapping(value = "/register")
	public ModelAndView register() throws IOException {
		return new ModelAndView("/sys/register");
	}

	@RequestMapping(value = "/userRegister",method = {RequestMethod.POST})
	public @ResponseBody
	BaseResponseVo userRegister(@Validated UserAddRequestVo userAddRequestVo, HttpServletRequest request) throws IOException {
		BaseResponseVo baseResponseVo =new BaseResponseVo();

		String sessionValidateCode=HttpServerSession.getSysUserRegisterValidateCode(request.getSession());
		if(sessionValidateCode==null||!sessionValidateCode.equalsIgnoreCase(userAddRequestVo.getValidateCode())){
			baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SYS_VALIDATE_CODE_VERIFY_FAIL);
			return baseResponseVo;
		}

		try{
			if(sysUserServiceImpl.checkUserExist(userAddRequestVo.getLoginName())){
				baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SYS_USER_EXIST);
				return baseResponseVo;
			}

			sysUserServiceImpl.addUser(userAddRequestVo);
			baseResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
		}catch (SimplePayException spe){
			baseResponseVo.setResultCode(spe.getResultCode());
			baseResponseVo.setResultMessage(spe.getMessage());
		}
		return baseResponseVo;
	}

	/**
	 * 生成验证码
	 * */
	@RequestMapping("getValidCode")
	public void getValidCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 设置响应的类型格式为图片格式
		response.setContentType("image/jpeg");
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		ValidateCodeUtil instance = new ValidateCodeUtil();
		HttpServerSession.setSysUserLoginValidateCode(request.getSession(),instance.getCode());
		instance.write(response.getOutputStream());
	}

	/**
	 * 生成验证码
	 * */
	@RequestMapping("getRegisterValidCode")
	public void getRegisterValidCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 设置响应的类型格式为图片格式
		response.setContentType("image/jpeg");
		// 禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		ValidateCodeUtil instance = new ValidateCodeUtil();
		HttpServerSession.setSysUserRegisterValidateCode(request.getSession(),instance.getCode());
		instance.write(response.getOutputStream());
	}
}
