package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.core.bo.GetUserInfoResponseBo;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Created by kevin on 2018/7/11.
 */
public class BarcodePayInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String payAppId = request.getParameter("payAppId");
		String userId=request.getParameter("userId");
		String merchantId=request.getParameter("merchantId");
		String barcode=request.getParameter("barcode");
		String payCode;
		String 	basePath= HttpUtil.getBaseUrl(request);

		if(StringUtils.isBlank(payAppId)&&StringUtils.isBlank(userId)){
			response.sendRedirect(basePath+ PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("参数payAppId和userId不能同时为空",PayConst.UTF8));
			return false;
		}

		 /*
         * 通过条码数字判断是微信还是支付宝的当面付条码，规则如下
         * 微信：18位纯数字，以10、11、12、13、14、15开头 https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=5_1
         * 支付宝：25~30开头的长度为16~24位的数字，实际字符串长度以开发者获取的付款码长度为准 https://docs.open.alipay.com/api_1/alipay.trade.pay
         */
		if (barcode.startsWith("10") || barcode.startsWith("11") ||
				barcode.startsWith("12") || barcode.startsWith("13") ||
				barcode.startsWith("14") || barcode.startsWith("15")) {
			payCode=PayCodeEnum.WX_MICROPAY.getCode();
		}else if (barcode.startsWith("25") || barcode.startsWith("26") ||
				barcode.startsWith("27") || barcode.startsWith("28") ||
				barcode.startsWith("29") || barcode.startsWith("30")){
			payCode= PayCodeEnum.ALIPAY_BARPAY.getCode();
		}else{
			response.sendRedirect(basePath+PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("无法识别的authCode",PayConst.UTF8));
			return false;
		}

		//如果payAppId为空，userId不为空，则用userId去找回payAppId进行支付
		PaymentServiceImpl paymentServiceImpl = SpringContextHandler.getBean(PaymentServiceImpl.class);
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

		//保存用户选择的支付信息
		UserSelectPaySetting userSelectPaySetting =new UserSelectPaySetting();
		userSelectPaySetting.setPayAppId(payAppId);
		userSelectPaySetting.setMerchantId(merchantId);
		userSelectPaySetting.setPayCode(payCode);
		userSelectPaySetting.setUserId(paymentSetting.getUserId());
		userSelectPaySetting.setPaymentSetting(paymentSetting);
		HttpServerSession.setUserSelectPaySettingSession(request.getSession(), userSelectPaySetting);

		//构造授权信息
		GetUserInfoResponseBo getUserInfoResponseBo=new GetUserInfoResponseBo();
		getUserInfoResponseBo.setResultCode("0");
		getUserInfoResponseBo.setPayUserId(String.valueOf(paymentSetting.getUserId()));
		HttpServerSession.setUserMobileAuth(request.getSession(),getUserInfoResponseBo);

		return true;
	}
}
