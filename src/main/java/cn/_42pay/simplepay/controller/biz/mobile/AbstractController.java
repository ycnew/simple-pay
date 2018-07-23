package cn._42pay.simplepay.controller.biz.mobile;

import cn._42pay.simplepay.framework.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kevin on 2018/6/26.
 */
public abstract  class AbstractController {
	protected String getBaseUrl(HttpServletRequest request){
		return HttpUtil.getBaseUrl(request);
	}
}
