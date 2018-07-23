package cn._42pay.simplepay.framework.freemaker;

import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.framework.util.HttpUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by kevin on 2018/6/21.
 * 自定义freemarker视图解析器,增加模板全局变量
 */
public class CustomFreeMarkerView extends FreeMarkerView {
	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String basePath =HttpUtil.getBaseUrl(request)	+ "/";
		// 设置项目路径为全局变量
		model.put("basePath", basePath);
		model.put("copyRight", PayConst.COPY_RIGHT);
		super.exposeHelpers(model, request);
	}
}
