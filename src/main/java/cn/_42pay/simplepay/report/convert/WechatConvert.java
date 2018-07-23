package cn._42pay.simplepay.report.convert;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.framework.util.WXPayUtil;
import cn._42pay.simplepay.report.entity.PayReportResponse;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import java.util.Map;

/**
 * Created by kevin on 2018/7/6.
 */
public class WechatConvert implements IConvert{
	@Override
	public PayReportResponse convert(String body) {
		Map<String, String> params;
		try {
			params = WXPayUtil.xmlToMap(body);
		} catch (Exception e) {
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_WECHAT_XMLTOMAP_EXCETION);
		}

		String payAppId=params.get("appid");
		PaymentServiceImpl paymentService= SpringContextHandler.getBean(PaymentServiceImpl.class);
		PaymentSetting paymentSetting=paymentService.getPaymentSettingByAppIdAndMerchantId(payAppId,null);
		if(payAppId==null){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_FIND_PAYMENT_EXCETION);
		}

		boolean isSignValid;
		try {
			isSignValid=WXPayUtil.isSignatureValid(params,paymentSetting.getApiSecret());
			if(!isSignValid){
				throw new SimplePayException(ErrorCodeEnum.NOTIFY_WECHAT_SIGN_VERIFY_EXCETION);
			}
		} catch (Exception e) {
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_WECHAT_SIGN_VERIFY_EXCETION);
		}

		String paymentDealNo=params.get("out_trade_no");
		String payAmount=params.get("total_fee");
		String paymentDealId=params.get("transaction_id");
		String openId= params.get("openid");
		String payTime= DateUtil.parseDatetime(params.get("time_end"), DateUtil.YYYY_MM_DD_HH_MM_SS_NO_DIVIDE);

		PaymentJournal paymentJournal=paymentService.getPaymentJournalByNo(paymentDealNo);
		if(paymentJournal==null){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_WECHAT_EXCETION.getCode(),"找不到paymentDealNo:"+paymentDealNo+"对应的记录");
		}

		PayReportResponse payReportResponse=new PayReportResponse();
		payReportResponse.setMerchantId(paymentJournal.getMerchantId());
		payReportResponse.setMerchantOrderNo(paymentJournal.getMerchantOrderNo());
		payReportResponse.setPayAmount(Integer.valueOf(payAmount));
		payReportResponse.setPayAppId(payAppId);
		payReportResponse.setPayCode(paymentJournal.getPayCode());
		payReportResponse.setPaymentDealId(paymentDealId);
		payReportResponse.setPaymentDealNo(paymentDealNo);
		payReportResponse.setPayTime(payTime);
		payReportResponse.setPayUserId(openId);
		return payReportResponse;
	}

	@Override
	public String getNotifySuccessMessage() {
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	@Override
	public String getNotifyFailureMessage() {
		return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[NOTOK]]></return_msg></xml>";
	}
}
