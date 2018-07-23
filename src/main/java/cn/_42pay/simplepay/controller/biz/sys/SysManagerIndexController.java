package cn._42pay.simplepay.controller.biz.sys;

import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.service.sys.impl.SysPaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by kevin on 2018/7/17.
 */
@Component
@Controller
@RequestMapping("/sys/manager")
public class SysManagerIndexController {
	@Autowired
	SysPaymentServiceImpl sysPaymentServiceImpl;

	@RequestMapping(value = "/index")
	public ModelAndView index() throws IOException {
		return new ModelAndView("/sys/admin/index");
	}

	@RequestMapping(value = "/welcome")
	public ModelAndView welcome(HttpSession session) throws IOException {
		ModelAndView modelAndView=new ModelAndView("/sys/admin/welcome");
		User user= HttpServerSession.getSysUserLoginSession(session);
		Long userId=user.getUserId();
		long payTotalCount;
		long payCreateOrderCount;
		long payWaitBuyCount;
		long paySuccessCount;
		long payRefundCount;

		payCreateOrderCount=sysPaymentServiceImpl.getPjCountByUserIdAndStatus(userId, PayStatusEnum.CREATE_ORDER.getIndex());
		payWaitBuyCount=sysPaymentServiceImpl.getPjCountByUserIdAndStatus(userId, PayStatusEnum.WAIT_PAY.getIndex());
		paySuccessCount=sysPaymentServiceImpl.getPjCountByUserIdAndStatus(userId, PayStatusEnum.PAY.getIndex());
		payRefundCount=sysPaymentServiceImpl.getPjCountByUserIdAndStatus(userId, PayStatusEnum.REFUND.getIndex());
		payTotalCount=payCreateOrderCount+payWaitBuyCount+paySuccessCount+payRefundCount;

		modelAndView.addObject("payCreateOrderCount",payCreateOrderCount);
		modelAndView.addObject("payWaitBuyCount",payWaitBuyCount);
		modelAndView.addObject("paySuccessCount",paySuccessCount);
		modelAndView.addObject("payRefundCount",payRefundCount);
		modelAndView.addObject("payTotalCount",payTotalCount);
		return modelAndView;
	}
}
