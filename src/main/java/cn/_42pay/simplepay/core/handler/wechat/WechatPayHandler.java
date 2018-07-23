package cn._42pay.simplepay.core.handler.wechat;

import cn._42pay.simplepay.constant.*;
import cn._42pay.simplepay.core.bo.*;
import cn._42pay.simplepay.core.handler.AbstractHandler;
import cn._42pay.simplepay.core.handler.wechat.entity.*;
import cn._42pay.simplepay.core.iface.IPayHandler;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.framework.util.WXPayUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2018/6/20.
 */
public class WechatPayHandler extends AbstractHandler implements IPayHandler {

	private static final Integer MAX_BARCODE_PAY_QUERY=10;

	@Override
	public GetAccessTokenResponseBo getAccessToken(GetAccessTokenRequestBo getAccessTokenRequestBo) {
		GetAccessTokenResponseBo responseBo=new GetAccessTokenResponseBo();

		String getAccessTokenUrl = getWechatAccessTokenUrl() +
				"?appid=" + getAccessTokenRequestBo.getRequestPayAppId() +
				"&secret=" + getAccessTokenRequestBo.getPaymentSetting().getAppSecret() +
				"&code=" + getAccessTokenRequestBo.getAuthCode() +
				"&grant_type=authorization_code";

		Log.i(LogScene.AUTH_WECHAT,"请求URL:"+getAccessTokenUrl);
		String result;
		try {
			result = HttpUtil.get(getAccessTokenUrl);
		} catch (IOException e) {
			Log.e(LogScene.AUTH_WECHAT,"微信获取access token异常",e);
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return responseBo;
		}
		Log.i(LogScene.AUTH_WECHAT,"返回:"+result);

		WechatAuthResponse wechatAuthResponse = JSON.parseObject(result,WechatAuthResponse.class, JsonSetting.getParserConfig());
		if (wechatAuthResponse != null
				&& !StringUtils.isEmpty(wechatAuthResponse.getAccessToken())
				&& !StringUtils.isEmpty(wechatAuthResponse.getOpenid())) {
			setCodeAndMessage(responseBo,ErrorCodeEnum.SUCCESS);
			responseBo.setAccessToken(wechatAuthResponse.getAccessToken());
			responseBo.setPayUerId(wechatAuthResponse.getOpenid());
		} else {
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.WECHAT_AUTH_ERROR,
					wechatAuthResponse.getErrcode(),
					wechatAuthResponse.getErrmsg());
		}

		return responseBo;
	}

	@Override
	public GetUserInfoResponseBo getUserInfo(GetUserInfoRequestBo getUserInfoRequestBo) {
		return null;
	}

	@Override
	public InWapPayResponseBo inWapPay(InWapPayRequestBo inWapPayRequestBo) {
		InWapPayResponseBo responseBo=new InWapPayResponseBo();

		Map<String, String> params = new HashMap<>(20);
		params.put("appid", inWapPayRequestBo.getPaymentSetting().getPayAppId());
		params.put("mch_id", inWapPayRequestBo.getPaymentSetting().getMerchantId());
		params.put("openid", inWapPayRequestBo.getRequestPayUserId());
		params.put("nonce_str", WXPayUtil.generateNonceStr());
		params.put("body", inWapPayRequestBo.getSubject());
		params.put("out_trade_no", inWapPayRequestBo.getPaymentDealNo());
		params.put("total_fee",String.valueOf(inWapPayRequestBo.getPayAmount()));
		params.put("spbill_create_ip", PayConst.LOCALHOST_IP);
		params.put("notify_url", getWechatPayNotifyUrl());
		params.put("trade_type", WXPayConstants.JSAPI);
		Map<String,String> attachParams=new HashMap<>(5);
		attachParams.put("appId",inWapPayRequestBo.getRequestPayAppId());
		attachParams.put("merchantId",inWapPayRequestBo.getRequestMerchantId());
		attachParams.put("payCode",inWapPayRequestBo.getPayCodeEnum().getCode());
		params.put("attach", JSON.toJSONString(attachParams));

		try {
			String xml= WXPayUtil.generateSignedXml(params,inWapPayRequestBo.getPaymentSetting().getApiSecret());
			Log.i(LogScene.PAY_WECHAT_WAP_PAY,"请求:"+xml);
			String result = HttpUtil.post(xml, getWechatPayUrl());
			Log.i(LogScene.PAY_WECHAT_WAP_PAY,"返回:"+result);

			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			WechatJsapiPayResponse jsapiPayResponse = JSON.parseObject(
					JSON.toJSONString(resultMap),
					WechatJsapiPayResponse.class,
					JsonSetting.getParserConfig());
			//判断returnCode
			if (!jsapiPayResponse.isReturnCodeSuccess()) {
				setCodeAndMessage(responseBo,
						ErrorCodeEnum.WECHAT_EXCETION,
						jsapiPayResponse.getResultCode(),
						jsapiPayResponse.getReturnMsg());
				return responseBo;
			}

			//判断签名是否正确
			if(!WXPayUtil.isSignatureValid(resultMap,inWapPayRequestBo.getPaymentSetting().getApiSecret())){
				setCodeAndMessage(responseBo,
						ErrorCodeEnum.WECHAT_VERIFY_SIGN_ERROR);
				return responseBo;
			}

			//判断resultCode
			if (!jsapiPayResponse.isResultCodeSuccess()) {
				setCodeAndMessage(responseBo,
						ErrorCodeEnum.WECHAT_EXCETION,
						jsapiPayResponse.getErrCode(),
						jsapiPayResponse.getErrCodeDes());
				return responseBo;
			}

			setCodeAndMessage(responseBo,ErrorCodeEnum.SUCCESS);
			responseBo.setAppId(jsapiPayResponse.getAppid());
			responseBo.setNonceStr(WXPayUtil.generateNonceStr());
			responseBo.setPrepayId("prepay_id="+jsapiPayResponse.getPrepayId());
			responseBo.setSignType(PayConst.MD5);
			responseBo.setTimeStamp(String.valueOf(WXPayUtil.getCurrentTimestamp()));
			responseBo.setPaySign(buildWechatJsapiSign(responseBo,inWapPayRequestBo.getPaymentSetting().getApiSecret()));

			Log.i(LogScene.PAY_WECHAT_WAP_PAY,"返回给serviceImpl层:"+JSON.toJSONString(responseBo));
			return responseBo;
		} catch (Exception e) {
			Log.e(LogScene.PAY_WECHAT_WAP_PAY,"统一下单异常",e);
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return responseBo;
		}
	}

	@Override
	public OutWapPayResponseBo outWapPay(OutWapPayRequestBo outWapPayRequestBo) {

		return null;
	}

	@Override
	public BarcodePayResponseBo barcodePay(BarcodePayRequestBo barcodePayRequestBo) {
		BarcodePayResponseBo barcodePayResponseBo = new BarcodePayResponseBo();

		Map<String, String> params = new HashMap<>(20);
		params.put("appid", barcodePayRequestBo.getPaymentSetting().getPayAppId());
		params.put("mch_id", barcodePayRequestBo.getPaymentSetting().getMerchantId());
		params.put("nonce_str", WXPayUtil.generateNonceStr());
		params.put("body", barcodePayRequestBo.getSubject());
		params.put("out_trade_no", barcodePayRequestBo.getPaymentDealNo());
		params.put("total_fee",String.valueOf(barcodePayRequestBo.getPayAmount()));
		params.put("spbill_create_ip", PayConst.LOCALHOST_IP);
		params.put("auth_code", barcodePayRequestBo.getAuthCode());

		try {
			String xml = WXPayUtil.generateSignedXml(params, barcodePayRequestBo.getPaymentSetting().getApiSecret());
			Log.i(LogScene.PAY_WECHAT_BARCODE_PAY, "请求:" + xml);
			String result = HttpUtil.post(xml, getWechatMicroPayUrl());
			Log.i(LogScene.PAY_WECHAT_BARCODE_PAY, "返回:" + result);

			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			WechatMicroPayResponse wechatMicroPayResponse = JSON.parseObject(
					JSON.toJSONString(resultMap),
					WechatMicroPayResponse.class,
					JsonSetting.getParserConfig());

			//判断returnCode
			if (!wechatMicroPayResponse.isReturnCodeSuccess()) {
				setCodeAndMessage(barcodePayResponseBo,
						ErrorCodeEnum.WECHAT_BARCODE_PAY_FAIL,
						wechatMicroPayResponse.getResultCode(),
						wechatMicroPayResponse.getReturnMsg());
				return barcodePayResponseBo;
			}

			//判断签名是否正确
			if(!WXPayUtil.isSignatureValid(resultMap,barcodePayRequestBo.getPaymentSetting().getApiSecret())){
				setCodeAndMessage(barcodePayResponseBo,
						ErrorCodeEnum.WECHAT_VERIFY_SIGN_ERROR);
				return barcodePayResponseBo;
			}

			//判断resultCode
			if (!wechatMicroPayResponse.isResultCodeSuccess()) {
				//明确失败
				if(wechatMicroPayResponse.isResultCodeFail()){
					setCodeAndMessage(barcodePayResponseBo,
							ErrorCodeEnum.WECHAT_BARCODE_PAY_FAIL,
							wechatMicroPayResponse.getErrCode(),
							wechatMicroPayResponse.getErrCodeDes()
					);
					return barcodePayResponseBo;
				}
				//循环查询订单是否成功
				return loopBarcodePayQuery(barcodePayRequestBo,MAX_BARCODE_PAY_QUERY);
			}

			barcodePayResponseBo.setPayAmount(Integer.valueOf(wechatMicroPayResponse.getTotalFee()));
			barcodePayResponseBo.setPaymentDealId(wechatMicroPayResponse.getTransactionId());
			barcodePayResponseBo.setPayUserId(wechatMicroPayResponse.getOpenid());
			barcodePayResponseBo.setPaymentDealNo(wechatMicroPayResponse.getOutTradeNo());
			barcodePayResponseBo.setPayTime(DateUtil.parseDatetime(wechatMicroPayResponse.getTimeEnd(),
					DateUtil.YYYY_MM_DD_HH_MM_SS_NO_DIVIDE));
			setCodeAndMessage(barcodePayResponseBo, ErrorCodeEnum.SUCCESS);
			return barcodePayResponseBo;
		}catch (Exception e){
			Log.e(LogScene.PAY_WECHAT_BARCODE_PAY,"刷卡付异常",e);
			setCodeAndMessage(barcodePayResponseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return barcodePayResponseBo;
		}
	}

	@Override
	public RefundResponseBo refund(RefundRequestBo refundRequestBo) {
		RefundResponseBo refundResponseBo=new RefundResponseBo();

		Map<String, String> params = new HashMap<>(20);
		params.put("appid",refundRequestBo.getPaymentSetting().getPayAppId());
		params.put("mch_id", refundRequestBo.getPaymentSetting().getMerchantId());
		params.put("nonce_str", WXPayUtil.generateNonceStr());
		if (!StringUtils.isEmpty(refundRequestBo.getPaymentDealNo())) {
			params.put("out_trade_no", refundRequestBo.getPaymentDealNo());
		}
		if (!StringUtils.isEmpty(refundRequestBo.getPaymentDealId())) {
			params.put("transaction_id", refundRequestBo.getPaymentDealId());
		}
		params.put("out_refund_no", refundRequestBo.getRefundPaymentDealNo());
		params.put("total_fee", String.valueOf(refundRequestBo.getRefundAmount()));
		params.put("refund_fee", String.valueOf(refundRequestBo.getRefundAmount()));
		try {
			String xml= WXPayUtil.generateSignedXml(params,refundRequestBo.getPaymentSetting().getApiSecret());
			Log.i(LogScene.PAY_WECHAT_REFUND,"请求:"+xml);
			String certPath = refundRequestBo.getPaymentSetting().getCertificatePath();
			String certPassword=refundRequestBo.getPaymentSetting().getCertificatePwd();
			if(StringUtils.isEmpty(certPath)||StringUtils.isEmpty(certPassword)){
				throw new SimplePayException(ErrorCodeEnum.WECHAT_EXCETION.getIndex(),"商户证书或证书密码未配置");
			}
			String result = HttpUtil.doWechatSslPost(getWechatRefundUrl(), xml, certPassword, certPath);
			Log.i(LogScene.PAY_WECHAT_REFUND,"返回:"+result);
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			WechatTradeRefundResponse wechatTradeRefundResponse = JSON.parseObject(
					JSON.toJSONString(resultMap),
					WechatTradeRefundResponse.class,
					JsonSetting.getParserConfig());
			if (!wechatTradeRefundResponse.isReturnCodeSuccess()) {
				setCodeAndMessage(refundResponseBo,
						ErrorCodeEnum.WECHAT_REFUND_FAIL,
						wechatTradeRefundResponse.getReturnCode(),
						wechatTradeRefundResponse.getReturnMsg());
				return refundResponseBo;
			}

			if (!WXPayUtil.isSignatureValid(resultMap, refundRequestBo.getPaymentSetting().getApiSecret())){
				setCodeAndMessage(refundResponseBo,
						ErrorCodeEnum.WECHAT_VERIFY_SIGN_ERROR);
				return refundResponseBo;
			}

			if (!wechatTradeRefundResponse.isResultCodeSuccess()) {
				setCodeAndMessage(refundResponseBo,
						ErrorCodeEnum.WECHAT_REFUND_FAIL,
						wechatTradeRefundResponse.getErrCode(),
						wechatTradeRefundResponse.getErrCodeDes());
				return refundResponseBo;
			}

			setCodeAndMessage(refundResponseBo,ErrorCodeEnum.SUCCESS);
			refundResponseBo.setRefundAmount(Integer.valueOf(wechatTradeRefundResponse.getRefundFee()));
			refundResponseBo.setRefundPaymentDealId(wechatTradeRefundResponse.getTransactionId());
			refundResponseBo.setRefundPaymentDealNo(wechatTradeRefundResponse.getOutRefundNo());
			refundResponseBo.setRefundTime(DateUtil.dateToString(new Date()));
			return refundResponseBo;
		} catch (Exception e) {
			Log.e(LogScene.PAY_WECHAT_REFUND,"微信退款异常",e);
			setCodeAndMessage(refundResponseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return refundResponseBo;
		}
	}

	@Override
	public CancelResponseBo cancel(CancelRequestBo cancelRequestBo) {
		CancelResponseBo cancelResponseBo =new CancelResponseBo();

		Map<String, String> params = new HashMap<>(5);
		params.put("appid",cancelRequestBo.getPaymentSetting().getPayAppId());
		params.put("mch_id",cancelRequestBo.getPaymentSetting().getMerchantId());
		if(!StringUtils.isEmpty(cancelRequestBo.getPaymentDealId())){
			params.put("transaction_id",cancelRequestBo.getPaymentDealId());
		}
		if(!StringUtils.isEmpty(cancelRequestBo.getPaymentDealNo())){
			params.put("out_trade_no",cancelRequestBo.getPaymentDealNo());
		}
		params.put("nonce_str",WXPayUtil.generateNonceStr());
		try {
			String xml= WXPayUtil.generateSignedXml(params,cancelRequestBo.getPaymentSetting().getApiSecret());
			Log.i(LogScene.PAY_WECHAT_CANCEL,"请求:"+xml);
			String certPath = cancelRequestBo.getPaymentSetting().getCertificatePath();
			String certPassword=cancelRequestBo.getPaymentSetting().getCertificatePwd();
			if(StringUtils.isEmpty(certPath)||StringUtils.isEmpty(certPassword)){
				throw new SimplePayException(ErrorCodeEnum.WECHAT_EXCETION.getIndex(),"商户证书或证书密码未配置");
			}
			String result = HttpUtil.doWechatSslPost(getWechatCancelUrl(), xml, certPassword, certPath);
			Log.i(LogScene.PAY_WECHAT_CANCEL,"返回:"+result);
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);

			WechatCancelResponse wechatCancelResponse = JSON.parseObject(
					JSON.toJSONString(resultMap),
					WechatCancelResponse.class,
					JsonSetting.getParserConfig());
			if (!wechatCancelResponse.isReturnCodeSuccess()) {
				setCodeAndMessage(cancelResponseBo,
						ErrorCodeEnum.WECHAT_CANCEL_FAIL,
						wechatCancelResponse.getReturnCode(),
						wechatCancelResponse.getReturnMsg());
				return cancelResponseBo;
			}

			if (!WXPayUtil.isSignatureValid(resultMap, cancelRequestBo.getPaymentSetting().getApiSecret())){
				setCodeAndMessage(cancelResponseBo,
						ErrorCodeEnum.WECHAT_VERIFY_SIGN_ERROR);
				return cancelResponseBo;
			}

			if (!wechatCancelResponse.isResultCodeSuccess()) {
				setCodeAndMessage(cancelResponseBo,
						ErrorCodeEnum.WECHAT_CANCEL_FAIL,
						wechatCancelResponse.getErrCode(),
						wechatCancelResponse.getErrCodeDes());
				return cancelResponseBo;
			}

			setCodeAndMessage(cancelResponseBo,ErrorCodeEnum.SUCCESS);
			cancelResponseBo.setPaymentDealId("");
			cancelResponseBo.setPaymentDealNo("");
			cancelResponseBo.setRetryFlag(wechatCancelResponse.getRecall());
			return cancelResponseBo;
		} catch (Exception e) {
			Log.e(LogScene.PAY_WECHAT_CANCEL,"微信撤销异常",e);
			setCodeAndMessage(cancelResponseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return cancelResponseBo;
		}
	}

	@Override
	public TradeQueryReseponseBo tradeQuery(TradeQueryRequestBo tradeQueryRequestBo) {
		TradeQueryReseponseBo tradeQueryReseponseBo=new TradeQueryReseponseBo();

		Map<String, String> params = new HashMap<>(5);
		params.put("appid",tradeQueryRequestBo.getPaymentSetting().getPayAppId());
		params.put("mch_id",tradeQueryRequestBo.getPaymentSetting().getMerchantId());
		if(!StringUtils.isEmpty(tradeQueryRequestBo.getPaymentDealId())){
			params.put("transaction_id",tradeQueryRequestBo.getPaymentDealId());
		}
		if(!StringUtils.isEmpty(tradeQueryRequestBo.getPaymentDealNo())){
			params.put("out_trade_no",tradeQueryRequestBo.getPaymentDealNo());
		}
		params.put("nonce_str",WXPayUtil.generateNonceStr());

		try {
			String xml= WXPayUtil.generateSignedXml(params,tradeQueryRequestBo.getPaymentSetting().getApiSecret());
			Log.i(LogScene.PAY_WECHAT_TRADE_QUERY,"请求:"+xml);
			String result = HttpUtil.post(xml, getWechatPayQueryUrl());
			Log.i(LogScene.PAY_WECHAT_TRADE_QUERY,"返回:"+result);

			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
			WechatPayQueryResponse wechatPayQueryResponse = JSON.parseObject(
					JSON.toJSONString(resultMap),
					WechatPayQueryResponse.class,
					JsonSetting.getParserConfig());
			if (!wechatPayQueryResponse.isReturnCodeSuccess()) {
				setCodeAndMessage(tradeQueryReseponseBo,
						ErrorCodeEnum.WECHAT_TRADE_QUERY_FAIL,
						wechatPayQueryResponse.getReturnCode(),
						wechatPayQueryResponse.getReturnMsg());
				return tradeQueryReseponseBo;
			}

			if (!WXPayUtil.isSignatureValid(resultMap, tradeQueryRequestBo.getPaymentSetting().getApiSecret())){
				setCodeAndMessage(tradeQueryReseponseBo,
						ErrorCodeEnum.WECHAT_TRADE_QUERY_FAIL);
				return tradeQueryReseponseBo;
			}

			if (!wechatPayQueryResponse.isResultCodeSuccess()) {
				setCodeAndMessage(tradeQueryReseponseBo,
						ErrorCodeEnum.WECHAT_TRADE_QUERY_FAIL,
						wechatPayQueryResponse.getErrCode(),
						wechatPayQueryResponse.getErrCodeDes());
				return tradeQueryReseponseBo;
			}

			if(!TradeStateEnum.SUCCESS.getCode().equals(wechatPayQueryResponse.getTradeState())){
				setCodeAndMessage(tradeQueryReseponseBo,
						ErrorCodeEnum.WECHAT_TRADE_QUERY_FAIL,
						"999",
						"当前订单状态:"+wechatPayQueryResponse.getTradeState());
				return tradeQueryReseponseBo;
			}

			setCodeAndMessage(tradeQueryReseponseBo, ErrorCodeEnum.SUCCESS);
			tradeQueryReseponseBo.setPayAmount(Integer.valueOf(wechatPayQueryResponse.getTotalFee()));
			tradeQueryReseponseBo.setPaymentDealId(wechatPayQueryResponse.getTransactionId());
			tradeQueryReseponseBo.setPayUserId(wechatPayQueryResponse.getOpenid());
			tradeQueryReseponseBo.setPaymentDealNo(wechatPayQueryResponse.getOutTradeNo());
			tradeQueryReseponseBo.setPayTime(DateUtil.parseDatetime(wechatPayQueryResponse.getTimeEnd(), DateUtil.YYYY_MM_DD_HH_MM_SS_NO_DIVIDE));
			tradeQueryReseponseBo.setTradeState(TradeStateEnum.SUCCESS.getCode());
			return tradeQueryReseponseBo;
		} catch (Exception e) {
			Log.e(LogScene.PAY_WECHAT_TRADE_QUERY,"微信交易查询异常",e);
			setCodeAndMessage(tradeQueryReseponseBo,
					ErrorCodeEnum.WECHAT_EXCETION);
			return tradeQueryReseponseBo;
		}
	}
}
