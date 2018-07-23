package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.config.PayConfig;
import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 *
 * @author kevin
 * @date 2018/6/20
 */
public class WebPayInterceptor implements HandlerInterceptor {
	private static final String ALIPAY_USER_INFO_CONTROLLER="/mobile/user/alipayUserAuth";

	private static final String WECHAT_USER_INFO_CONTROLLER="/mobile/user/wechatUserAuth";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String payAppId = request.getParameter("payAppId");
		String userId=request.getParameter("userId");
		String payCode=request.getParameter("payCode");
		String merchantId=request.getParameter("merchantId");
		String userAgent = request.getHeader("User-Agent");
		String targetUrl= HttpUtil.getRequestUrlRemovePaySetting(request);
		String basePath=HttpUtil.getBaseUrl(request);

		//如果该授权会话session不为空，说明已经授权过了，直接进入页面进行支付
		if(HttpServerSession.getUserMobileAuth(request.getSession())!=null){
			return true;
		}

		if(StringUtils.isBlank(payAppId)&&StringUtils.isBlank(userId)){
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("参数payAppId和userId不能同时为空",PayConst.UTF8));
			return false;
		}

		//对payCode进行处理
		if(StringUtils.isBlank(payCode)){
			if(userAgent.contains(PayConst.ALIPAY_USER_AGENT)){
				payCode=PayCodeEnum.ALIPAY_WAP.getCode();
			}else if(userAgent.contains(PayConst.WECHAT_USER_AGENT)){
				payCode=PayCodeEnum.WX_JSAPI.getCode();
			}else{
				response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
						URLEncoder.encode("外部浏览器模式payCode不能为空，只能传入WX_H5或ALIPAY_WEB等",PayConst.UTF8));
				return false;
			}
		}

		//如果payAppId为空，userId不为空，则用userId去找回payAppId进行支付
		PaymentServiceImpl paymentServiceImpl =SpringContextHandler.getBean(PaymentServiceImpl.class);
		PaymentSetting paymentSetting;
		if(StringUtils.isNotBlank(userId)){
			paymentSetting= paymentServiceImpl.getPaymentSettingByUserIdAndPayCode(Long.valueOf(userId),payCode);
			if(paymentSetting==null){
				response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
						URLEncoder.encode("[userId:"+userId+",payCode:"+payCode+"]不存在该支付配置",PayConst.UTF8));
				return false;
			}
			payAppId=paymentSetting.getPayAppId();
			merchantId=paymentSetting.getMerchantId();
		}else{
			paymentSetting=paymentServiceImpl.getPaymentSettingByAppIdAndMerchantId(payAppId,merchantId);
			if(paymentSetting==null){
				response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
						URLEncoder.encode("[payAppId:"+payAppId+",merchantId:"+merchantId+"]不存在该支付配置",PayConst.UTF8));
				return false;
			}
		}

		//如果是问号结尾，去掉该字符
		if(targetUrl.endsWith("?")){
			targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
		}

		//保存用户选择的支付信息
		UserSelectPaySetting userSelectPaySetting =new UserSelectPaySetting();
		userSelectPaySetting.setPayAppId(payAppId);
		userSelectPaySetting.setMerchantId(merchantId);
		userSelectPaySetting.setPayCode(payCode);
		userSelectPaySetting.setUserId(paymentSetting.getUserId());
		userSelectPaySetting.setPaymentSetting(paymentSetting);
		HttpServerSession.setUserSelectPaySettingSession(request.getSession(), userSelectPaySetting);

		//设置跳转地址
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		if(StringUtils.isBlank(payConfig.getPayReturnUrl())){
			payConfig.setPayReturnUrl(basePath+PayConst.ORDER_PAYED_RETURN_VIEW);
		}

		if(userAgent.contains(PayConst.ALIPAY_USER_AGENT)
				|| PayCodeEnum.ALIPAY_WEB.getCode().equals(payCode)){//支付宝渠道
			String redirectUrl=basePath+ALIPAY_USER_INFO_CONTROLLER;

			PayConfig.AlipayConfig alipayConfig= payConfig.getAlipayConfigInstance();
			if(StringUtils.isBlank(alipayConfig.getNotifyUrl())){
				alipayConfig.setNotifyUrl(basePath+PayConst.NOTIFY_ALIPAY_PATH);
			}
			String authUrl=String.format(alipayConfig.getAuthUrl()+"?app_id=%s&scope=auth_base&redirect_uri=%s&state=%s",
					payAppId,
					URLEncoder.encode(redirectUrl, PayConst.UTF8),
					Base64.encodeBase64String((targetUrl.getBytes(PayConst.UTF8)))
			);
			Log.i(LogScene.AUTH_ALIPAY,"用户请求授权地址:"+authUrl);
			response.sendRedirect(authUrl);
			return false;
		}else if(userAgent.contains(PayConst.WECHAT_USER_AGENT)){//微信渠道
			String redirectUrl=basePath+WECHAT_USER_INFO_CONTROLLER;

			PayConfig.WechatConfig wechatConfig=payConfig.getWechatConfigInstance();
			if(StringUtils.isBlank(wechatConfig.getNotifyUrl())){
				wechatConfig.setNotifyUrl(basePath+PayConst.NOTIFY_WECHAT_PATH);
			}
			String authUrl=String.format(wechatConfig.getAuthUrl()+"?response_type=code"+
							"&scope=snsapi_base&state=%s"+
							"&appid=%s&redirect_uri=%s&#wechat_redirect",
					Base64.encodeBase64String((targetUrl.getBytes(PayConst.UTF8))),
					payAppId,
					URLEncoder.encode(redirectUrl, PayConst.UTF8)
			);
			Log.i(LogScene.AUTH_WECHAT,"用户请求授权地址:"+authUrl);
			response.sendRedirect(authUrl);
			return false;
		}else{
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("无法识别该支付渠道",PayConst.UTF8));
			return false;
		}
	}
}
