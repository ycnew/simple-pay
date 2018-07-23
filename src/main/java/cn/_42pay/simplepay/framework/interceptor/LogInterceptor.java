package cn._42pay.simplepay.framework.interceptor;

import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogParamType;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.session.RequestSession;
import cn._42pay.simplepay.framework.spring.TraceContextHandler;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.framework.util.IDUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by kevin on 2018/6/25.
 */
public class LogInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//重新生成traceId
		TraceContextHandler.clear();
		String id= IDUtil.getRandomUUID32();
		TraceContextHandler.setTraceId(id);

		if (handler instanceof HandlerMethod) {
			long beginRequestTime = System.currentTimeMillis();
			String uri = request.getRequestURI();
			Method method = ((HandlerMethod) handler).getMethod();
			String params=HttpUtil.getRequestParams(request);
			Log.i(LogScene.PAGE_REQUEST,uri, LogParamType.REQUEST,method.getName(), StringUtils.isBlank(params)?"参数为空":params);
			RequestSession.setBeginRequestTimeSession(request,beginRequestTime);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		Long beginRequestTime=RequestSession.getBeginRequestTimeSession(request);
		if(beginRequestTime==null)
		{
			return;
		}
		if(handler instanceof HandlerMethod){
			String uri = request.getRequestURI();
			Method method = ((HandlerMethod) handler).getMethod();
			long endResponseTime=System.currentTimeMillis();
			Log.i(LogScene.PAGE_REQUEST,uri,
					LogParamType.RESPONSE,method.getName(),
					(endResponseTime-beginRequestTime),
					modelAndView==null?"[结束]":modelAndView.toString());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

	}
}
