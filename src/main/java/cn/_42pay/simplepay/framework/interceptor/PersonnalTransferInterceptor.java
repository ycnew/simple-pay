package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.service.sys.impl.SysPaymentServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * 个人转账拦截器
 * Created by kevin on 2018/7/22.
 */
public class PersonnalTransferInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String userId=request.getParameter("userId");
		String userAgent = request.getHeader("User-Agent");
		String 	basePath= HttpUtil.getBaseUrl(request);
		Integer payMode=0;
		if(StringUtils.isBlank(userId)){
			response.sendRedirect(basePath+ PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("参数userId不能为空",PayConst.UTF8));
			return false;
		}

		if(userAgent.contains(PayConst.ALIPAY_USER_AGENT)) {//支付宝渠道
			payMode=2;
		}else if(userAgent.contains(PayConst.WECHAT_USER_AGENT)) {//微信渠道
			payMode=1;
		}else{
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("无法识别该支付渠道",PayConst.UTF8));
			return false;
		}
		SysPaymentServiceImpl sysPaymentServiceImpl= SpringContextHandler.getBean(SysPaymentServiceImpl.class);
		PaymentSetting paymentSetting=	sysPaymentServiceImpl.getPaymentSettingByUserIdAndPayMode(Long.valueOf(userId),payMode);
		if(paymentSetting==null){
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("找不到该支付配置",PayConst.UTF8));
			return false;
		}

		String targetUrl=paymentSetting.getPersonalTransferStr();
		if(StringUtils.isBlank(targetUrl)){
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("未上传转账二维码",PayConst.UTF8));
			return false;
		}
		Log.i(LogScene.PERSOANAL_TRANSER,"用户ID:"+userId,"转账的URL为:"+targetUrl);
		if(userAgent.contains(PayConst.ALIPAY_USER_AGENT)) {//支付宝渠道
			response.sendRedirect(targetUrl);
		}else if(userAgent.contains(PayConst.WECHAT_USER_AGENT)) {//微信渠道
			HttpServerSession.setPersonalWechatTransferUrlSession(request.getSession(),targetUrl);
			response.sendRedirect(basePath+"/mobile/order/showWechatQrcode");
		}

		return false;
	}
}
