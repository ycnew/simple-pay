package cn._42pay.simplepay.config;

import cn._42pay.simplepay.framework.interceptor.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kevin on 2018/6/20.
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/notify/**");
		registry.addInterceptor(new WebPayInterceptor())
				.addPathPatterns("/mobile/order/webPayOrderInfo");
		registry.addInterceptor(new BarcodePayInterceptor())
				.addPathPatterns("/mobile/payment/barcodePay");
		registry.addInterceptor(new PersonnalTransferInterceptor())
				.addPathPatterns("/mobile/payment/personalTransfer");

		List<String> userSessionCheckList=new ArrayList<>();
		userSessionCheckList.add("/mobile/payment/**");
		userSessionCheckList.add("/mobile/order/queryOrderInfo");
		registry.addInterceptor(new MobileUserSessionCheckInterceptor())
				.addPathPatterns(userSessionCheckList);

		registry.addInterceptor(new SysUserSessionCheckInterceptor())
				.addPathPatterns("/sys/manager/**");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("redirect:/sys/user/login");
	}
}
