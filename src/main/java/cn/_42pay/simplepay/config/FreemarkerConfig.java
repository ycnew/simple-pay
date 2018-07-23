package cn._42pay.simplepay.config;

import cn._42pay.simplepay.framework.freemaker.CustomFreeMarkerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Created by kevin on 2018/6/21.
 */

@Configuration
@AutoConfigureAfter({FreeMarkerViewResolver.class})
public class FreemarkerConfig {
	@Autowired
	FreeMarkerViewResolver freemarkerViewResolver;

	@Bean
	public FreeMarkerViewResolver freemarkerViewResolverBean() {
		freemarkerViewResolver.setViewClass(CustomFreeMarkerView.class);
		return freemarkerViewResolver;
	}

}
