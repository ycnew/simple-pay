package cn._42pay.simplepay.controller.biz.mobile;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.core.bo.GetAccessTokenRequestBo;
import cn._42pay.simplepay.core.bo.GetAccessTokenResponseBo;
import cn._42pay.simplepay.core.bo.GetUserInfoResponseBo;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.service.mobile.impl.PayChannelServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by kevin on 2018/6/19.
 * @author kevin
 * @link https://docs.alipay.com/fw/api/105942
 * @link https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
 */
@Component
@Controller
@RequestMapping("/mobile/user")
public class UserController extends AbstractController {
	@Autowired
	PayChannelServiceImpl payChannelServiceImpl;

	@RequestMapping(value = "/alipayUserAuth")
	public void alipayUserAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		userAuth(request,response,session);
	}

	@RequestMapping(value = "/wechatUserAuth")
	public void wechatUserAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		userAuth(request,response,session);
	}

	private void userAuth(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException{
		UserSelectPaySetting userSelectPaySetting = HttpServerSession.getUserSelectPaySettingSession(session);
		if(userSelectPaySetting==null){
			response.sendRedirect(getBaseUrl(request)+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("请先进入订单界面",PayConst.UTF8));
			return;
		}

		String targetUrl= new String(Base64.decodeBase64(request.getParameter("state")), PayConst.UTF8);
		String payCode=userSelectPaySetting.getPayCode();
		String authCode;

		if(payCode.startsWith(PayConst.WECHAT)||payCode.startsWith(PayConst.WX)){
			authCode = request.getParameter("code");
		}else if(payCode.startsWith(PayConst.ALIPAY)){
			authCode = request.getParameter("auth_code");
		}else{
			throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}
		Log.i(LogScene.AUTH,"收到回调:"+ HttpUtil.getRequestUrl(request));
		if(StringUtils.isBlank(authCode)){
			response.sendRedirect(getBaseUrl(request)+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("用户拒绝授权",PayConst.UTF8));
			return;
		}

		//换取accessToken和userId
		GetAccessTokenRequestBo getAccessTokenRequestBo =new GetAccessTokenRequestBo();
		getAccessTokenRequestBo.setPayCodeEnum(PayCodeEnum.valueOf(payCode));
		getAccessTokenRequestBo.setAuthCode(authCode);
		getAccessTokenRequestBo.setRequestPayAppId(userSelectPaySetting.getPayAppId());
		getAccessTokenRequestBo.setRequestMerchantId(userSelectPaySetting.getMerchantId());
		getAccessTokenRequestBo.setPaymentSetting(userSelectPaySetting.getPaymentSetting());
		GetAccessTokenResponseBo getAccessTokenResponseBo;
		try {
			getAccessTokenResponseBo= payChannelServiceImpl.getAccessToken(getAccessTokenRequestBo);
		}catch (SimplePayException spe){
			response.sendRedirect(getBaseUrl(request)+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode(spe.getMessage(),PayConst.UTF8));
			return;
		}
		if(!getAccessTokenResponseBo.isSuccess()){
			response.sendRedirect(getBaseUrl(request)+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode(getAccessTokenResponseBo.getResultMessage(),PayConst.UTF8));
			return;
		}

		GetUserInfoResponseBo getUserInfoResponseBo=new GetUserInfoResponseBo();
		getUserInfoResponseBo.setResultCode("0");
		getUserInfoResponseBo.setPayUserId(getAccessTokenResponseBo.getPayUerId());

		//放入会话中
		HttpServerSession.setUserMobileAuth(session,getUserInfoResponseBo);
		//跳转到原始的界面
		response.sendRedirect(targetUrl);
	}
}
