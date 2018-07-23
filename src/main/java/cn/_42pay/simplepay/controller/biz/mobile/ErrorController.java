package cn._42pay.simplepay.controller.biz.mobile;

import cn._42pay.simplepay.constant.PayConst;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by kevin on 2018/6/26.
 */
@Component
@Controller
@RequestMapping("/mobile/error")
public class ErrorController extends AbstractController {
	@RequestMapping(value={"/e500"}, method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView e500(String errorMessage){
		ModelAndView modelAndView=new ModelAndView(PayConst.ERROR_500_VIEW_FTL);
		modelAndView.addObject("errorMessage",errorMessage);
		return modelAndView;
	}
}
