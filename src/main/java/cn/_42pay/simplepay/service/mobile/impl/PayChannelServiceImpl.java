package cn._42pay.simplepay.service.mobile.impl;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.core.bo.*;
import cn._42pay.simplepay.core.handler.alipay.AlipayHandler;
import cn._42pay.simplepay.core.handler.wechat.WechatPayHandler;
import cn._42pay.simplepay.core.iface.IPayHandler;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.entity.UnifiedPayResponse;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.framework.util.ReflectUtil;
import cn._42pay.simplepay.service.AbstractService;
import cn._42pay.simplepay.service.mobile.PayChannelService;
import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * 支付相关的
 * Created by kevin on 2018/6/27.
 */
@Service
public class PayChannelServiceImpl extends AbstractService implements PayChannelService {
	@Autowired
	PaymentServiceImpl paymentServiceImpl;

	@Override
	public GetAccessTokenResponseBo getAccessToken(GetAccessTokenRequestBo getAccessTokenRequestBo) throws SimplePayException {
		return doExecute(getAccessTokenRequestBo);
	}

	@Override
	public GetUserInfoResponseBo getUserInfo(GetUserInfoRequestBo getUserInfoRequestBo) throws SimplePayException {
		return doExecute(getUserInfoRequestBo);
	}

	@Override
	public UnifiedPayResponse webPayDispatch(UserSelectPaySetting userSelectPaySetting, GetUserInfoResponseBo getUserInfoResponseBo, PaymentJournal paymentJournal) throws SimplePayException {
		UnifiedPayResponse unifiedPayResponse=new UnifiedPayResponse();
		unifiedPayResponse.setPayCode(userSelectPaySetting.getPayCode());

		PayCodeEnum payCodeEnum=PayCodeEnum.valueOf(userSelectPaySetting.getPayCode());
		String methodName=getPayMethodName(payCodeEnum);
		PayBaseRequestBo payBaseRequestBo =getPayBaseRequestBo(payCodeEnum,userSelectPaySetting);

		//差异化处理
		switch (payCodeEnum){
			case WX_JSAPI:
			case ALIPAY_WAP:
				InWapPayRequestBo inWapPayRequestBo=(InWapPayRequestBo)payBaseRequestBo;
				inWapPayRequestBo.setDescription(paymentJournal.getDescription());
				inWapPayRequestBo.setPayAmount(paymentJournal.getPayAmount());
				inWapPayRequestBo.setPaymentDealNo(paymentJournal.getPaymentDealNo());
				inWapPayRequestBo.setSubject(paymentJournal.getDescription());
				inWapPayRequestBo.setRequestPayUserId(getUserInfoResponseBo.getPayUserId());
				break;
			case WX_H5:
			case ALIPAY_WEB:
				OutWapPayRequestBo outWapPayRequestBo=(OutWapPayRequestBo)payBaseRequestBo;
				outWapPayRequestBo.setDescription(paymentJournal.getDescription());
				outWapPayRequestBo.setPayAmount(paymentJournal.getPayAmount());
				outWapPayRequestBo.setPaymentDealNo(paymentJournal.getPaymentDealNo());
				outWapPayRequestBo.setSubject(paymentJournal.getDescription());
				outWapPayRequestBo.setRequestPayUserId(getUserInfoResponseBo.getPayUserId());
				break;
			default:
				throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}

		PayBaseResponseBo payBaseResponseBo=doExecute(payBaseRequestBo,methodName);
		if(!payBaseResponseBo.isSuccess()){
			throw new SimplePayException(payBaseResponseBo.getResultCode(),
					payBaseResponseBo.getResultMessage());
		}

		//差异化处理
		switch (payCodeEnum){
			case WX_JSAPI:
			case ALIPAY_WAP:
				InWapPayResponseBo inWapPayRequestBo=(InWapPayResponseBo)payBaseResponseBo;
				BeanUtils.copyProperties(inWapPayRequestBo,unifiedPayResponse);
				break;
			case WX_H5:
			case ALIPAY_WEB:
				OutWapPayResponseBo outWapPayRequestBo=(OutWapPayResponseBo)payBaseResponseBo;
				BeanUtils.copyProperties(outWapPayRequestBo,unifiedPayResponse);
				break;
			default:
				throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}

		Log.i(LogScene.PAY,"返回给view层:"+ JSON.toJSONString(unifiedPayResponse));
		return unifiedPayResponse;
	}

	@Override
	public BarcodePayResponseBo barcodePay(UserSelectPaySetting userSelectPaySetting, GetUserInfoResponseBo getUserInfoResponseBo, PaymentJournal paymentJournal,String authCode) throws SimplePayException {
		BarcodePayRequestBo barcodePayRequestBo=new BarcodePayRequestBo();
		barcodePayRequestBo.setPayCodeEnum(PayCodeEnum.valueOf(userSelectPaySetting.getPayCode()));
		barcodePayRequestBo.setRequestPayAppId(userSelectPaySetting.getPayAppId());
		barcodePayRequestBo.setRequestMerchantId(userSelectPaySetting.getMerchantId());
		barcodePayRequestBo.setPaymentSetting(userSelectPaySetting.getPaymentSetting());
		barcodePayRequestBo.setAuthCode(authCode);
		barcodePayRequestBo.setDescription(paymentJournal.getDescription());
		barcodePayRequestBo.setPayAmount(paymentJournal.getPayAmount());
		barcodePayRequestBo.setPaymentDealNo(paymentJournal.getPaymentDealNo());
		barcodePayRequestBo.setSubject("条码付:"+paymentJournal.getDescription());
		return doExecute(barcodePayRequestBo);
	}

	@Override
	public RefundResponseBo refund(Long userId, String paymentDealNo) {
		RefundResponseBo refundResponseBo=new RefundResponseBo();
		//根据userId和paymentDealNo和payStatus 去数据库查询
		PaymentJournal where=new PaymentJournal();
		where.setPaymentDealNo(paymentDealNo);
		where.setUserId(userId);
		where.setPayStatus(PayStatusEnum.PAY.getIndex());
		List<PaymentJournal> paymentJournalList=paymentJournalDao.select(where);
		if(CollectionUtils.isEmpty(paymentJournalList)){
			refundResponseBo.buildResultCodeAndResultMessage(ErrorCodeEnum.SYS_PAYMENT_JOURNAL_EMPTY.getCode(),
					ErrorCodeEnum.SYS_PAYMENT_JOURNAL_EMPTY.getMessage());
			return refundResponseBo;
		}
		PaymentJournal paymentJournal=paymentJournalList.get(0);

		PaymentSetting paymentSetting=paymentServiceImpl.getPaymentSettingByAppIdAndMerchantId(
				paymentJournal.getPayAppId(),
				paymentJournal.getMerchantId()
		);
		if(paymentSetting==null){
			refundResponseBo.buildResultCodeAndResultMessage(ErrorCodeEnum.SYS_PAYMENT_SETTING_EMPTY.getCode(),
					ErrorCodeEnum.SYS_PAYMENT_SETTING_EMPTY.getMessage());
			return refundResponseBo;
		}

		//构造请求负流水
		PaymentJournal refundPaymentJournal=paymentServiceImpl.buildRefundPaymentJournal(paymentJournal);

		//构造请求退款的参数
		RefundRequestBo refundRequestBo=new RefundRequestBo();
		refundRequestBo.setPaymentSetting(paymentSetting);
		refundRequestBo.setRequestPayAppId(paymentJournal.getPayAppId());
		refundRequestBo.setRequestMerchantId(paymentJournal.getMerchantId());
		refundRequestBo.setPayCodeEnum(PayCodeEnum.valueOf(paymentJournal.getPayCode()));
		refundRequestBo.setPaymentDealNo(paymentJournal.getPaymentDealNo());
		refundRequestBo.setRefundAmount(paymentJournal.getTotalAmount());
		refundRequestBo.setRefundPaymentDealNo(refundPaymentJournal.getPaymentDealNo());
		refundResponseBo=doExecute(refundRequestBo);
		if(refundResponseBo.isSuccess()){
			PaymentJournal updateRefundPaymentJournal=new PaymentJournal();
			updateRefundPaymentJournal.setPaymentJournalId(refundPaymentJournal.getPaymentJournalId());
			updateRefundPaymentJournal.setPayStatus(PayStatusEnum.REFUND.getIndex());
			updateRefundPaymentJournal.setUpdateTime(DateUtil.dateToString(new Date()));
			updateRefundPaymentJournal.setPayTime(refundResponseBo.getRefundTime());
			updateRefundPaymentJournal.setPaymentDealId(refundResponseBo.getRefundPaymentDealId());
			paymentServiceImpl.updatePaymentJoural(updateRefundPaymentJournal);
		}
		return refundResponseBo;
	}


	@SuppressWarnings("unchecked")
	private <OUT extends PayBaseResponseBo> OUT doExecute(PayBaseRequestBo payBaseRequestBo){
		return doExecute(payBaseRequestBo,"");
	}

	/**
	 * 统一调用支付通道
	 * @param payBaseRequestBo
	 * @param <OUT>
	 * @return
	 */
	@SuppressWarnings("unchecked")
  	private <OUT extends PayBaseResponseBo> OUT doExecute(PayBaseRequestBo payBaseRequestBo,String methodName){
		IPayHandler payHandler=getPayHandler(payBaseRequestBo.getPayCodeEnum().getCode());
		if(StringUtil.isBlank(methodName)){
			methodName=Thread.currentThread().getStackTrace()[3].getMethodName();
		}
		Method m = ReflectUtil.findMethod(methodName,payHandler.getClass());
		try {
			return  (OUT)(m.invoke(payHandler, new Object[]{payBaseRequestBo}));
		} catch (IllegalAccessException e) {
			Log.e(LogScene.SYSTEM_ERROR,"支付反射调用异常",e);
			throw new SimplePayException(ErrorCodeEnum.CALL_METHOD_EXCEPTION);
		} catch (InvocationTargetException ie) {
			Log.e(LogScene.SYSTEM_ERROR,"支付反射调用异常",ie);
			throw new SimplePayException(ErrorCodeEnum.CALL_METHOD_EXCEPTION);
		}
	}

	/**
	 * 获取对应支付通道的实现
	 * @param payCode
	 * @return
	 */
	private IPayHandler getPayHandler(String  payCode){
		IPayHandler payHandler;
		Assert.notNull(payCode,"payCode不能为空");
		if(payCode.startsWith(PayConst.WECHAT)||payCode.startsWith(PayConst.WX)){
			payHandler=new WechatPayHandler();
		}else if(payCode.startsWith(PayConst.ALIPAY)){
			payHandler=new AlipayHandler();
		}else{
			throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}
		Assert.notNull(payHandler,"payHandler不能为空");
		return payHandler;
	}

	/**
	 * 找出对应需要支付方法名
	 * @param payCodeEnum
	 * @return
	 */
	private String getPayMethodName(PayCodeEnum payCodeEnum){
		String methodName;
		switch (payCodeEnum){
			case WX_JSAPI:
			case ALIPAY_WAP:
				methodName="inWapPay";
				break;
			case WX_H5:
			case ALIPAY_WEB:
				methodName="outWapPay";
				break;
			default:
				throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}
		Assert.notNull(methodName,"methodName不能为空");
		return methodName;
	}

	/**
	 * 找出对应的payBaseRequestBo
	 * @param payCodeEnum
	 * @param userSelectPaySetting
	 * @return
	 */
	private PayBaseRequestBo getPayBaseRequestBo(PayCodeEnum payCodeEnum,UserSelectPaySetting userSelectPaySetting){
		PayBaseRequestBo payBaseRequestBo;
		switch (payCodeEnum){
			case WX_JSAPI:
			case ALIPAY_WAP:
				payBaseRequestBo=new InWapPayRequestBo();
				break;
			case WX_H5:
			case ALIPAY_WEB:
				payBaseRequestBo=new OutWapPayRequestBo();
				break;
			default:
				throw new SimplePayException(ErrorCodeEnum.NOT_SUPPORT_THIS_PAY_CHANNEL);
		}

		payBaseRequestBo.setPayCodeEnum(payCodeEnum);
		payBaseRequestBo.setPaymentSetting(userSelectPaySetting.getPaymentSetting());
		payBaseRequestBo.setRequestMerchantId(userSelectPaySetting.getMerchantId());
		payBaseRequestBo.setRequestPayAppId(userSelectPaySetting.getPayAppId());
		return payBaseRequestBo;
	}
}
