package cn._42pay.simplepay.report.convert;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.report.entity.PayReportResponse;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by kevin on 2018/7/6.
 */
public class AlipayConvert implements IConvert{
	@Override
	public PayReportResponse convert(String body) {
		Map<String, String> params = HttpUtil.requestParamToMap(body);
		if (CollectionUtils.isEmpty(params)){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_ALIPAY_EXCETION);
		}

		String tradeStatus = params.get("trade_status");
		if (!PayConst.ALIPAY_TRADE_SUCCESS.equals(tradeStatus)) {
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_ALIPAY_TRADE_NOF_SUCCESS_EXCETION);
		}

		String signType = params.remove("sign_type");
		String charset = params.get("charset");
		String payAppId =  params.get("app_id");

		PaymentServiceImpl paymentService= SpringContextHandler.getBean(PaymentServiceImpl.class);
		PaymentSetting paymentSetting=paymentService.getPaymentSettingByAppIdAndMerchantId(payAppId,null);
		if(payAppId==null){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_FIND_PAYMENT_EXCETION);
		}

		boolean isValid;
		try {
			isValid = AlipaySignature.rsaCheckV1(
					params, paymentSetting.getPayPublicKey(), charset, signType
			);
		} catch (AlipayApiException e) {
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_ALIPAY_EXCETION.getCode(),"验证签名异常");
		}
		if(!isValid){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_ALIPAY_SIGN_VERIFY_EXCETION);
		}

		String alipayUserId=StringUtils.isEmpty(params.get("buyer_id")) ?  params.get("buyer_user_id") :  params.get("buyer_id");
		String paymentDealNo=params.get("out_trade_no");
		String paymentDealId=params.get("trade_no");
		String payTime=params.get("gmt_payment");
		String payAmount=params.get("buyer_pay_amount") == null ? "0" : String.valueOf((int)(Double.valueOf(params.get("buyer_pay_amount"))*100));

		PaymentJournal paymentJournal=paymentService.getPaymentJournalByNo(paymentDealNo);
		if(paymentJournal==null){
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_ALIPAY_EXCETION.getCode(),"找不到paymentDealNo:"+paymentDealNo+"对应的记录");
		}

		PayReportResponse payReportResponse=new PayReportResponse();
		payReportResponse.setPayUserId(alipayUserId);
		payReportResponse.setPayTime(payTime);
		payReportResponse.setPaymentDealNo(paymentDealNo);
		payReportResponse.setPaymentDealId(paymentDealId);
		payReportResponse.setPayCode(paymentJournal.getPayCode());
		payReportResponse.setPayAppId(payAppId);
		payReportResponse.setPayAmount(Integer.valueOf(payAmount));
		payReportResponse.setMerchantOrderNo(paymentJournal.getMerchantOrderNo());
		payReportResponse.setMerchantId(paymentJournal.getMerchantId());
		return payReportResponse;
	}

	@Override
	public String getNotifySuccessMessage() {
		return PayConst.SUCCESS;
	}

	@Override
	public String getNotifyFailureMessage() {
		return PayConst.FAILURE;
	}
}
