package cn._42pay.simplepay.framework.session;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kevin on 2018/6/26.
 *
 */
public class RequestSession {
	private static final String BEGIN_REQUEST_TIME_SESSION="beginRequestTime";

	public static void setBeginRequestTimeSession(HttpServletRequest request,long beginRequestTime){
		request.setAttribute(BEGIN_REQUEST_TIME_SESSION,beginRequestTime);
	}

	public static Long getBeginRequestTimeSession(HttpServletRequest request){
		Object beginTime= request.getAttribute(BEGIN_REQUEST_TIME_SESSION);
		if(beginTime==null){
			return null;
		}
		request.removeAttribute(BEGIN_REQUEST_TIME_SESSION);
		return (long)beginTime;
	}


}
