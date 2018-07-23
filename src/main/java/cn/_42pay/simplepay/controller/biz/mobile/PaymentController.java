package cn._42pay.simplepay.controller.biz.mobile;

import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.core.bo.BarcodePayResponseBo;
import cn._42pay.simplepay.core.bo.GetUserInfoResponseBo;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.entity.UnifiedPayResponse;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.session.HttpServerSession;
import cn._42pay.simplepay.report.ReportMechant;
import cn._42pay.simplepay.report.entity.PayReportResponse;
import cn._42pay.simplepay.service.mobile.impl.PayChannelServiceImpl;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import cn._42pay.simplepay.service.mobile.impl.UserServiceImpl;
import cn._42pay.simplepay.vo.mobile.BarcodePayResponseVo;
import cn._42pay.simplepay.vo.mobile.BarcodePayVo;
import cn._42pay.simplepay.vo.mobile.WebPayVo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2018/6/19.
 */
@Component
@Controller
@RequestMapping("/mobile/payment")
public class PaymentController extends AbstractController {
	@Autowired
	PaymentServiceImpl paymentServiceImpl;

	@Autowired
	PayChannelServiceImpl payChannelService;

	@Autowired
	UserServiceImpl userServiceImpl;

	private static final String WEB_PAY_VIEW="/mobile/biz/payment/wpay";
	private static final String WEB_PAY_LOADING_VIEW="/mobile/biz/payment/payLoading";

	private static final Integer MAX_QUERY_PAY_COUONT=10;

	@RequestMapping(value={"/webPay"}, method = {RequestMethod.POST})
	public ModelAndView webPay(@Validated WebPayVo webPayVo, HttpServletRequest request){
		ModelAndView modelAndView=new ModelAndView(WEB_PAY_VIEW);

		//根据用户的订单号去更新金额，并将状态改成唤起收银台
		PaymentJournal paymentJournal= HttpServerSession.getUserPaymentJournalSession(request.getSession());
		try{
			paymentServiceImpl.updatePaymentJournalMoney(
					paymentJournal.getPaymentJournalId(),
					webPayVo.getPayAmount(),
					PayStatusEnum.WAIT_PAY.getIndex()
			);
			paymentJournal.setPayAmount(webPayVo.getPayAmount());
			paymentJournal.setPayStatus(PayStatusEnum.WAIT_PAY.getIndex());
		}catch (SimplePayException spe){
			modelAndView=new ModelAndView(PayConst.ERROR_500_VIEW_FTL);
			modelAndView.addObject("errorMessage",spe.getMessage());
			return modelAndView;
		}

		UserSelectPaySetting userSelectPaySetting=HttpServerSession.getUserSelectPaySettingSession(request.getSession());
		GetUserInfoResponseBo getUserInfoResponseBo=HttpServerSession.getUserMobileAuth(request.getSession());
		UnifiedPayResponse unifiedPayResponse;
		try{
			unifiedPayResponse=payChannelService.webPayDispatch(userSelectPaySetting,getUserInfoResponseBo,paymentJournal);
		}catch (SimplePayException spe){
			modelAndView=new ModelAndView(PayConst.ERROR_500_VIEW_FTL);
			modelAndView.addObject("errorMessage",spe.getMessage());
			return modelAndView;
		}

		modelAndView.addObject("payCode",unifiedPayResponse.getPayCode());
		modelAndView.addObject("unifiedPayResponse",unifiedPayResponse);
		return modelAndView;
	}

	@RequestMapping(value={"/barcodePay"}, method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseEntity<String> barcodePay(@Validated BarcodePayVo barcodePayVo, HttpServletRequest request) {
		BarcodePayResponseVo barcodePayResponseVo=new BarcodePayResponseVo();
		barcodePayResponseVo.setCodeAndMessage("999","FAIL");

		UserSelectPaySetting userSelectPaySetting =HttpServerSession.getUserSelectPaySettingSession(request.getSession());
		GetUserInfoResponseBo getUserInfoResponseBo=HttpServerSession.getUserMobileAuth(request.getSession());
		User user=userServiceImpl.getUserById(userSelectPaySetting.getUserId());
		userSelectPaySetting.setUser(user);

		//构建支付流水
		PaymentJournal paymentJournal;
		try{
			paymentJournal=paymentServiceImpl.buildPaymentJournal(
					userSelectPaySetting,
					barcodePayVo.getMerchantOrderNo(),
					barcodePayVo.getDescription(),
					barcodePayVo.getPayAmount());
			HttpServerSession.setUserPaymentJournalSession(request.getSession(),paymentJournal);
		}catch (SimplePayException spe){
			barcodePayResponseVo.setCodeAndMessage(spe.getResultCode(),spe.getMessage());
			return new ResponseEntity<>(JSON.toJSONString(barcodePayResponseVo),HttpStatus.OK);
		}

		BarcodePayResponseBo barcodePayResponseBo;
		try{
			barcodePayResponseBo=payChannelService.barcodePay(userSelectPaySetting,getUserInfoResponseBo,paymentJournal,barcodePayVo.getBarcode());
			if(barcodePayResponseBo.isSuccess()){
				PayReportResponse payReportResponse=new PayReportResponse();
				payReportResponse.setMerchantId(userSelectPaySetting.getMerchantId());
				payReportResponse.setMerchantOrderNo(paymentJournal.getMerchantOrderNo());
				payReportResponse.setPayAmount(barcodePayResponseBo.getPayAmount());
				payReportResponse.setPayAppId(userSelectPaySetting.getPayAppId());
				payReportResponse.setPayCode(userSelectPaySetting.getPayCode());
				payReportResponse.setPaymentDealId(barcodePayResponseBo.getPaymentDealId());
				payReportResponse.setPaymentDealNo(barcodePayResponseBo.getPaymentDealNo());
				payReportResponse.setPayUserId(barcodePayResponseBo.getPayUserId());
				ReportMechant.doNotify(payReportResponse);
			}
		}catch (SimplePayException spe){
			barcodePayResponseVo.setCodeAndMessage(spe.getResultCode(),spe.getMessage());
			return new ResponseEntity<>(JSON.toJSONString(barcodePayResponseVo),HttpStatus.OK);
		}

		BeanUtils.copyProperties(barcodePayResponseBo,barcodePayResponseVo);
		return new ResponseEntity<>(JSON.toJSONString(barcodePayResponseVo),HttpStatus.OK);
	}

	@RequestMapping(value={"/payLoading"}, method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView payLoading(HttpServletRequest request){
		return new ModelAndView(WEB_PAY_LOADING_VIEW);
	}

	@RequestMapping(value={"/payConfirm"}, method = {RequestMethod.POST})
	public ResponseEntity<String> payConfirm(HttpServletRequest request){
		Long paymentJournalId= HttpServerSession.getUserPaymentJournalSession(request.getSession()).getPaymentJournalId();
		Map<String,Object> rsultMap=new HashMap<>();
		rsultMap.put("isSuccess",false);
		int count=0;
		while (count<MAX_QUERY_PAY_COUONT){
			PaymentJournal paymentJournal=paymentServiceImpl.getPaymentJournalById(paymentJournalId);
			if(paymentJournal.getPayStatus()==PayStatusEnum.PAY.getIndex()){
				rsultMap.put("isSuccess",true);
				rsultMap.put("msgInfo","支付成功");
				HttpServerSession.setUserPaymentJournalSession(request.getSession(),paymentJournal);
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
		}
		return new ResponseEntity<>(JSON.toJSONString(rsultMap), HttpStatus.OK);
	}
}
