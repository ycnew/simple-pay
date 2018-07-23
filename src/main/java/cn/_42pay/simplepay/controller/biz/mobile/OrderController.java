package cn._42pay.simplepay.controller.biz.mobile;

import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.framework.util.OrderUtil;
import cn._42pay.simplepay.framework.util.QRCodeUtil;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import cn._42pay.simplepay.service.mobile.impl.UserServiceImpl;
import cn._42pay.simplepay.vo.mobile.BarcodePayOrderInfoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * Created by kevin on 2018/6/21.
		*/
@Component
@Controller
@RequestMapping("/mobile/order")
public class OrderController extends AbstractController {
	private static final String ORDER_INFO_VIEW="/mobile/biz/order/orderInfo";
	private static final String B_ORDER_INFO_VIEW="/mobile/biz/order/bOrderInfo";
	private static final String QUERY_ORDER_INFO_VIEW="/mobile/biz/order/queryOrderInfo";
	private static final String WECHAT_QRCODE_URL="/mobile/biz/order/showWechatQrcode";

	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	PaymentServiceImpl paymentServiceImpl;

	@RequestMapping(value={"/webPayOrderInfo"}, method = {RequestMethod.GET})
	public ModelAndView webPayOrderInfo(String merchantOrderNo, Integer payAmount,String description, HttpServletRequest request){
		ModelAndView modelAndView=new ModelAndView(ORDER_INFO_VIEW);
		UserSelectPaySetting userSelectPaySetting =HttpServerSession.getUserSelectPaySettingSession(request.getSession());
		User user=userServiceImpl.getUserById(userSelectPaySetting.getUserId());
		userSelectPaySetting.setUser(user);
		if(StringUtils.isBlank(merchantOrderNo)){
			merchantOrderNo= OrderUtil.generateOrderNo();
		}
		if(StringUtils.isBlank(description)){
			description="支付到:"+user.getUserName()+"下["+userSelectPaySetting.getPaymentSetting().getDescription()+"]帐号";
		}

		//构建支付流水
		try{
			PaymentJournal paymentJournal=paymentServiceImpl.buildPaymentJournal(userSelectPaySetting,merchantOrderNo,description,payAmount);
			HttpServerSession.setUserPaymentJournalSession(request.getSession(),paymentJournal);
		}catch (SimplePayException spe){
			modelAndView=new ModelAndView(PayConst.ERROR_500_VIEW_FTL);
			modelAndView.addObject("errorMessage",spe.getMessage());
			return modelAndView;
		}

		modelAndView.addObject("merchantOrderNo",merchantOrderNo);
		if(payAmount!=null){
			modelAndView.addObject("payAmount",String.valueOf(new BigDecimal(payAmount).divide(new BigDecimal(100))));
		}
		modelAndView.addObject("description",description);
		return modelAndView;
	}

	@RequestMapping(value={"/barcodePayOrderInfo"}, method = {RequestMethod.GET})
	public ModelAndView barcodePayOrderInfo(BarcodePayOrderInfoVo barcodePayOrderInfoVo, HttpServletRequest request){
		ModelAndView modelAndView=new ModelAndView(B_ORDER_INFO_VIEW);

		if(StringUtils.isBlank(barcodePayOrderInfoVo.getMerchantOrderNo())){
			barcodePayOrderInfoVo.setMerchantOrderNo( OrderUtil.generateOrderNo());
		}

		if(StringUtils.isBlank(barcodePayOrderInfoVo.getDescription())){
			barcodePayOrderInfoVo.setDescription("条码付");
		}

		if(barcodePayOrderInfoVo.getPayAmount()!=null){
			barcodePayOrderInfoVo.setPayAmount(String.valueOf(new BigDecimal(barcodePayOrderInfoVo.getPayAmount()).divide(new BigDecimal(100))));
		}
		modelAndView.addObject("barcodePayOrderInfoVo",barcodePayOrderInfoVo);
		return modelAndView;
	}

	@RequestMapping(value={"/queryOrderInfo"}, method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView queryOrderInfo(HttpServletRequest request){
		ModelAndView modelAndView=new ModelAndView(QUERY_ORDER_INFO_VIEW);
		PaymentJournal paymentJournal=HttpServerSession.getUserPaymentJournalSession(request.getSession());
		modelAndView.addObject("merchantOrderNo",paymentJournal.getMerchantOrderNo());
		modelAndView.addObject("desc",paymentJournal.getDescription());
		modelAndView.addObject("payAmount",String.valueOf(new BigDecimal(paymentJournal.getPayAmount()).divide(new BigDecimal(100))));
		modelAndView.addObject("payStatus", PayStatusEnum.getEnum(paymentJournal.getPayStatus()).getMessage());
		return modelAndView;
	}

	@RequestMapping(value={"/showWapPay"}, method = {RequestMethod.POST,RequestMethod.GET})
	public void showWapPay(HttpServletRequest request,HttpServletResponse response){
		String url=getBaseUrl(request)+"/mobile/order/webPayOrderInfo?userId=1&payAmount=1&timestamp="+System.currentTimeMillis();
		try {
			QRCodeUtil.generateQRCode(url,400,400,"jpg",response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value={"/showPersonalTransfer"}, method = {RequestMethod.POST,RequestMethod.GET})
	public void showPersonalTransfer(HttpServletRequest request,HttpServletResponse response){
		String url=getBaseUrl(request)+"/mobile/payment/personalTransfer?userId=1";
		try {
			QRCodeUtil.generateQRCode(url,400,400,"jpg",response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value={"/showWechatQrcode"}, method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView showWechatQrcode(HttpServletRequest request){
		ModelAndView modelAndView=new ModelAndView(WECHAT_QRCODE_URL);
		String targetUrl=HttpServerSession.getPersonalWechatTransferUrlSession(request.getSession());
		if(StringUtils.isBlank(targetUrl)){
			modelAndView=new ModelAndView(PayConst.ERROR_500_VIEW_FTL);
			modelAndView.addObject("errorMessage","获取不到当前会话的转账URL");
			return modelAndView;
		}
		modelAndView.addObject("targetUrl",targetUrl);
		return modelAndView;
	}
}
