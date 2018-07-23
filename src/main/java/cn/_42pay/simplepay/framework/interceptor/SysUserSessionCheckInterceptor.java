package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.util.HttpUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kevin on 2018/7/17.
 */
public class SysUserSessionCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		User user= HttpServerSession.getSysUserLoginSession(request.getSession());
		String basePath = HttpUtil.getBaseUrl(request);
		if(user==null){
			response.sendRedirect(basePath+"/sys/user/login");
			return false;
		}
		return true;
	}
}
