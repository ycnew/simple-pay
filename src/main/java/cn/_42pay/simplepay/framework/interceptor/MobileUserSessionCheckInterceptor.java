package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.core.bo.GetUserInfoResponseBo;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.util.HttpUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Created by kevin on 2018/6/28.
 */
public class MobileUserSessionCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		GetUserInfoResponseBo getUserInfoResponseBo=HttpServerSession.getUserMobileAuth(request.getSession());
		String basePath = HttpUtil.getBaseUrl(request) ;
		if(getUserInfoResponseBo==null){
			response.sendRedirect(basePath+ PayConst.ERROR_500_VIEW+"?errorMessage="+
					URLEncoder.encode("请先进行授权登录！",PayConst.UTF8));
			return false;
		}
		return true;
	}
}
