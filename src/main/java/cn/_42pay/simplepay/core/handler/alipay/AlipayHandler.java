package cn._42pay.simplepay.core.handler.alipay;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.constant.TradeStateEnum;
import cn._42pay.simplepay.core.bo.*;
import cn._42pay.simplepay.core.handler.AbstractHandler;
import cn._42pay.simplepay.core.iface.IPayHandler;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 开发Demo @link https://docs.open.alipay.com/54
 * Created by kevin on 2018/6/20.
 */
public class AlipayHandler extends AbstractHandler implements IPayHandler {
	private static final String GRANT_TYPE = "authorization_code";
	private static final String WAP_PAY_PRODUCT_CODE = "QUICK_WAP_WAY";
	private static final String BARCODE_PAY_SCENE = "bar_code";
	private static final String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";

	private static final String SUCCESS = "10000"; // 成功
	private static final String PAYING = "10003";  // 用户支付中
	private static final String FAILED = "40004";  // 失败
	private static final String ERROR = "20000"; // 系统异常

	private static final Integer MAX_BARCODE_PAY_QUERY=10;

	@Override
	public GetAccessTokenResponseBo getAccessToken(GetAccessTokenRequestBo getAccessTokenRequestBo) {
		GetAccessTokenResponseBo responseBo = new GetAccessTokenResponseBo();

		AlipayClient alipayClient = getAlipayClient(getAccessTokenRequestBo);
		AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
		oauthTokenRequest.setCode(getAccessTokenRequestBo.getAuthCode());
		oauthTokenRequest.setGrantType(GRANT_TYPE);
		Log.i(LogScene.AUTH_ALIPAY, getAlipayGateWay(), "请求:" + JSON.toJSONString(oauthTokenRequest));
		AlipaySystemOauthTokenResponse oauthTokenResponse;
		try {
			oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
			Log.i(LogScene.AUTH_ALIPAY, getAlipayGateWay(), "返回:" + JSON.toJSONString(oauthTokenResponse));

			if(oauthTokenResponse!=null&&oauthTokenResponse.isSuccess()){
				setCodeAndMessage(responseBo, ErrorCodeEnum.SUCCESS);
				responseBo.setAccessToken(oauthTokenResponse.getAccessToken());
				responseBo.setPayUerId(oauthTokenResponse.getUserId());
				return responseBo;
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.AUTH_ALIPAY, getAlipayGateWay(), "支付宝授权异常", e);
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return responseBo;
		}

		if(oauthTokenResponse!=null){
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_AUTH_ERROR,
					oauthTokenResponse.getSubCode(),
					oauthTokenResponse.getSubMsg());
		}else{
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_AUTH_ERROR);
		}
		return responseBo;
	}

	@Override
	public GetUserInfoResponseBo getUserInfo(GetUserInfoRequestBo getUserInfoRequestBo) {
		GetUserInfoResponseBo responseBo = new GetUserInfoResponseBo();

		AlipayClient alipayClient = getAlipayClient(getUserInfoRequestBo);
		AlipayUserUserinfoShareResponse userUserinfoShareResponse;
		Log.i(LogScene.AUTH_ALIPAY, getAlipayGateWay(), "请求:" + JSON.toJSONString(getUserInfoRequestBo));
		try {
			userUserinfoShareResponse = alipayClient.execute(new AlipayUserUserinfoShareRequest(), getUserInfoRequestBo.getAccessToken());
			Log.i(LogScene.AUTH_ALIPAY, getAlipayGateWay(), "返回:" + JSON.toJSONString(userUserinfoShareResponse));
			if(userUserinfoShareResponse!=null&&userUserinfoShareResponse.isSuccess()){
				setCodeAndMessage(responseBo, ErrorCodeEnum.SUCCESS);
				responseBo.setBirth(userUserinfoShareResponse.getBirthday());
				responseBo.setIdCardNo(userUserinfoShareResponse.getCertNo());
				responseBo.setMobile(userUserinfoShareResponse.getMobile());
				responseBo.setPayUserId(userUserinfoShareResponse.getUserId());
				responseBo.setUserName(userUserinfoShareResponse.getRealName());
				responseBo.setUserSex(userUserinfoShareResponse.getGender());
				return responseBo;
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.AUTH_ALIPAY, "支付宝授权获取用户信息失败", e);
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return responseBo;
		}

		if(userUserinfoShareResponse!=null){
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_AUTH_ERROR,
					userUserinfoShareResponse.getSubCode(),
					userUserinfoShareResponse.getSubMsg());
		}else{
			setCodeAndMessage(responseBo,
					ErrorCodeEnum.ALIPAY_AUTH_ERROR);
		}
		return responseBo;
	}

	/**
	 * @param inWapPayRequestBo
	 * @return
	 * @link https://docs.open.alipay.com/203
	 */
	@Override
	public InWapPayResponseBo inWapPay(InWapPayRequestBo inWapPayRequestBo) {
		InWapPayResponseBo inWapPayResponseBo = new InWapPayResponseBo();

		AlipayClient alipayClient = getAlipayClient(inWapPayRequestBo);
		AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(inWapPayRequestBo.getPaymentDealNo());
		model.setSubject(inWapPayRequestBo.getSubject());
		model.setTotalAmount(String.valueOf(new BigDecimal(inWapPayRequestBo.getPayAmount()).divide(new BigDecimal(100))));
		model.setBody(inWapPayRequestBo.getDescription());
		model.setTimeoutExpress(getAlipayWebPayTimeout());
		model.setProductCode(WAP_PAY_PRODUCT_CODE);
		alipayTradeWapPayRequest.setBizModel(model);
		// 设置异步通知地址
		alipayTradeWapPayRequest.setNotifyUrl(getAlipayNotifyUrl());
		// 设置同步地址
		alipayTradeWapPayRequest.setReturnUrl(getReturnUrl());

		// form表单生产
		String form;
		try {
			// 调用SDK生成表单
			form = alipayClient.pageExecute(alipayTradeWapPayRequest).getBody();
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_WAP_PAY, "支付宝WAP支付失败", e);
			setCodeAndMessage(inWapPayResponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return inWapPayResponseBo;
		}
		setCodeAndMessage(inWapPayResponseBo, ErrorCodeEnum.SUCCESS);
		inWapPayResponseBo.setFrontSubmitForm(form);
		return inWapPayResponseBo;
	}

	/**
	 * @param outWapPayRequestBo
	 * @return
	 * @link https://docs.open.alipay.com/270
	 */
	@Override
	public OutWapPayResponseBo outWapPay(OutWapPayRequestBo outWapPayRequestBo) {
		OutWapPayResponseBo outWapPayResponseBo = new OutWapPayResponseBo();

		AlipayClient alipayClient = getAlipayClient(outWapPayRequestBo);
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(getReturnUrl());
		alipayRequest.setNotifyUrl(getAlipayNotifyUrl());
		alipayRequest.setBizContent(
				"{\"out_trade_no\":\"" + outWapPayRequestBo.getPaymentDealNo() + "\","
						+ "\"total_amount\":\"" + String.valueOf(new BigDecimal(outWapPayRequestBo.getPayAmount()).divide(new BigDecimal(100))) + "\","
						+ "\"subject\":\"" + outWapPayRequestBo.getSubject() + "\","
						+ "\"body\":\"" + outWapPayRequestBo.getDescription() + "\","
						+ "\"timeout_express\":\"" + getAlipayWebPayTimeout() + "\","
						+ "\"product_code\":\"" + FAST_INSTANT_TRADE_PAY + "\"}"
		);

		// form表单生产
		String form;
		try {
			// 调用SDK生成表单
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_WEB_PAY, "支付宝WEB支付失败", e);
			setCodeAndMessage(outWapPayResponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return outWapPayResponseBo;
		}
		setCodeAndMessage(outWapPayResponseBo, ErrorCodeEnum.SUCCESS);
		outWapPayResponseBo.setFrontSubmitForm(form);
		return outWapPayResponseBo;
	}

	@Override
	public BarcodePayResponseBo barcodePay(BarcodePayRequestBo barcodePayRequestBo) {
		BarcodePayResponseBo barcodePayResponseBo = new BarcodePayResponseBo();

		AlipayClient alipayClient = getAlipayClient(barcodePayRequestBo);
		Map<String, Object> bizParams = new HashMap<>();
		bizParams.put("out_trade_no", barcodePayRequestBo.getPaymentDealNo());
		bizParams.put("scene", BARCODE_PAY_SCENE);
		bizParams.put("auth_code", barcodePayRequestBo.getAuthCode());
		bizParams.put("subject", barcodePayRequestBo.getSubject());
		bizParams.put("body", barcodePayRequestBo.getDescription());
		bizParams.put("total_amount",
				new BigDecimal(barcodePayRequestBo.getPayAmount()).divide(
						new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());


		AlipayTradePayRequest payRequest = new AlipayTradePayRequest();
		payRequest.setBizContent(JSON.toJSONString(bizParams));
		Log.i(LogScene.PAY_ALIPAY_BARCODE_PAY, "请求->" + JSON.toJSONString(payRequest));
		AlipayTradePayResponse payResponse;
		try {
			payResponse = alipayClient.execute(payRequest);
			Log.i(LogScene.PAY_ALIPAY_BARCODE_PAY, "返回->" + JSON.toJSONString(payResponse));
			if (payResponse == null || !SUCCESS.equals(payResponse.getCode())) {
				if (payResponse != null && PAYING.equals(payResponse.getCode())) {
					// 返回用户处理中，则轮询查询交易是否成功，如果查询超时，则调用撤销
					barcodePayResponseBo=loopBarcodePayQuery(barcodePayRequestBo,MAX_BARCODE_PAY_QUERY);
				} else if (payResponse == null || ERROR.equals(payResponse.getCode())) {
					// 系统错误，则查询一次交易，如果交易没有支付成功，则调用撤销
					barcodePayResponseBo=loopBarcodePayQuery(barcodePayRequestBo,1);
				} else {
					// 其他情况表明该订单支付明确失败
					setCodeAndMessage(barcodePayResponseBo,
							ErrorCodeEnum.ALIPAY_BARCODE_PAY_FAIL,
							payResponse.getCode(), payResponse.getMsg()
					);
				}
				return barcodePayResponseBo;
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_WEB_PAY, "支付宝条码支付失败", e);
			setCodeAndMessage(barcodePayResponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return barcodePayResponseBo;
		}

		barcodePayResponseBo.setPayAmount((int) (Double.valueOf(payResponse.getTotalAmount()) * 100));
		barcodePayResponseBo.setPaymentDealId(payResponse.getTradeNo());
		barcodePayResponseBo.setPayUserId(payResponse.getBuyerUserId());
		barcodePayResponseBo.setPaymentDealNo(payResponse.getOutTradeNo());
		barcodePayResponseBo.setPayTime(DateUtil.dateToString(payResponse.getGmtPayment()));
		setCodeAndMessage(barcodePayResponseBo, ErrorCodeEnum.SUCCESS);
		return barcodePayResponseBo;
	}

	@Override
	public RefundResponseBo refund(RefundRequestBo refundRequestBo) {
		RefundResponseBo refundResponseBo=new RefundResponseBo();

		AlipayClient alipayClient = getAlipayClient(refundRequestBo);

		Map<String, Object> bizParams = new HashMap<>();
		if (!StringUtils.isEmpty(refundRequestBo.getPaymentDealNo())) {
			bizParams.put("out_trade_no", refundRequestBo.getPaymentDealNo());
		}
		if (!StringUtils.isEmpty(refundRequestBo.getPaymentDealId())) {
			bizParams.put("trade_no", refundRequestBo.getPaymentDealId());
		}
		bizParams.put("refund_amount",
				new BigDecimal(refundRequestBo.getRefundAmount()).divide(
						new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		bizParams.put("out_request_no", refundRequestBo.getRefundPaymentDealNo());

		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent(JSON.toJSONString(bizParams));
		Log.i(LogScene.PAY_ALIPAY_REFUND, "请求->" + JSON.toJSONString(request));
		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			Log.i(LogScene.PAY_ALIPAY_REFUND, "返回->" + JSON.toJSONString(response));
			if (response != null && SUCCESS.equals(response.getCode())) {
				setCodeAndMessage(refundResponseBo, ErrorCodeEnum.SUCCESS);
				refundResponseBo.setRefundAmount((int)(Double.valueOf(response.getRefundFee())*100));
				refundResponseBo.setRefundPaymentDealId(response.getTradeNo());
				refundResponseBo.setRefundPaymentDealNo(response.getOutTradeNo());
				refundResponseBo.setRefundTime(DateUtil.dateToString(response.getGmtRefundPay()));
				return refundResponseBo;
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_REFUND, "支付宝退款异常", e);
			setCodeAndMessage(refundResponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return refundResponseBo;
		}

		if(response!=null){
			setCodeAndMessage(refundResponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_REFUND_FAIL,
					response.getSubCode(),
					response.getSubMsg());
		}else{
			setCodeAndMessage(refundResponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_REFUND_FAIL
			);
		}
		return refundResponseBo;
	}

	@Override
	public CancelResponseBo cancel(CancelRequestBo cancelRequestBo) {
		CancelResponseBo cancelResponseBo =new CancelResponseBo();

		AlipayClient alipayClient = getAlipayClient(cancelRequestBo);
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		Map<String, Object> bizParams = new HashMap<>();
		if (!StringUtils.isEmpty(cancelRequestBo.getPaymentDealNo())) {
			bizParams.put("out_trade_no", cancelRequestBo.getPaymentDealNo());
		}
		if (!StringUtils.isEmpty(cancelRequestBo.getPaymentDealId())) {
			bizParams.put("trade_no", cancelRequestBo.getPaymentDealId());
		}
		request.setBizContent(JSON.toJSONString(bizParams));

		Log.i(LogScene.PAY_ALIPAY_CANCEL,"请求:"+JSON.toJSONString(request));
		AlipayTradeCancelResponse alipayTradeCancelResponse;
		try {
			alipayTradeCancelResponse = alipayClient.execute(request);
			Log.i(LogScene.PAY_ALIPAY_CANCEL, "返回->" + JSON.toJSONString(alipayTradeCancelResponse));
			if(alipayTradeCancelResponse!=null&&SUCCESS.equals(alipayTradeCancelResponse.getCode())){
				cancelResponseBo.setPaymentDealId(alipayTradeCancelResponse.getTradeNo());
				cancelResponseBo.setPaymentDealNo(alipayTradeCancelResponse.getOutTradeNo());
				cancelResponseBo.setRetryFlag(alipayTradeCancelResponse.getRetryFlag());
				setCodeAndMessage(cancelResponseBo, ErrorCodeEnum.SUCCESS);
				return cancelResponseBo;
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_TRADE_QUERY, "支付宝交易查询失败", e);
			setCodeAndMessage(cancelResponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return cancelResponseBo;
		}

		if(alipayTradeCancelResponse!=null){
			setCodeAndMessage(cancelResponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_CANCEL_FAIL,
					alipayTradeCancelResponse.getSubCode(),
					alipayTradeCancelResponse.getSubMsg());
		}else{
			setCodeAndMessage(cancelResponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_CANCEL_FAIL
			);
		}
		return cancelResponseBo;
	}

	@Override
	public TradeQueryReseponseBo tradeQuery(TradeQueryRequestBo tradeQueryRequestBo) {
		TradeQueryReseponseBo tradeQueryReseponseBo = new TradeQueryReseponseBo();

		AlipayClient alipayClient = getAlipayClient(tradeQueryRequestBo);
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		Map<String, Object> bizParams = new HashMap<>();
		if (!StringUtils.isEmpty(tradeQueryRequestBo.getPaymentDealNo())) {
			bizParams.put("out_trade_no", tradeQueryRequestBo.getPaymentDealNo());
		}
		if (!StringUtils.isEmpty(tradeQueryRequestBo.getPaymentDealId())) {
			bizParams.put("trade_no", tradeQueryRequestBo.getPaymentDealId());
		}
		request.setBizContent(JSON.toJSONString(bizParams));

		Log.i(LogScene.PAY_ALIPAY_TRADE_QUERY, "请求->" + JSON.toJSONString(request));
		AlipayTradeQueryResponse alipayTradeQueryResponse;
		try {
			alipayTradeQueryResponse = alipayClient.execute(request);
			Log.i(LogScene.PAY_ALIPAY_TRADE_QUERY, "返回->" + JSON.toJSONString(alipayTradeQueryResponse));
			if (alipayTradeQueryResponse != null && SUCCESS.equals(alipayTradeQueryResponse.getCode())) {
				if (PayConst.ALIPAY_TRADE_FINISHED.equals(alipayTradeQueryResponse.getTradeStatus()) ||
						PayConst.ALIPAY_TRADE_SUCCESS.equals(alipayTradeQueryResponse.getTradeStatus())) {
					// 如果查询到交易成功、交易结束，则返回对应结果
					setCodeAndMessage(tradeQueryReseponseBo, ErrorCodeEnum.SUCCESS);
					tradeQueryReseponseBo.setPayAmount((int) (Double.valueOf(alipayTradeQueryResponse.getTotalAmount()) * 100));
					tradeQueryReseponseBo.setPaymentDealId(alipayTradeQueryResponse.getTradeNo());
					tradeQueryReseponseBo.setPayUserId(alipayTradeQueryResponse.getBuyerUserId());
					tradeQueryReseponseBo.setPaymentDealNo(alipayTradeQueryResponse.getOutTradeNo());
					tradeQueryReseponseBo.setPayTime(DateUtil.dateToString(alipayTradeQueryResponse.getSendPayDate()));
					tradeQueryReseponseBo.setTradeState(TradeStateEnum.SUCCESS.getCode());
				} else{
					tradeQueryReseponseBo.setTradeState(TradeStateEnum.PAYERROR.getCode());
				}
			}
		} catch (AlipayApiException e) {
			Log.e(LogScene.PAY_ALIPAY_TRADE_QUERY, "支付宝交易查询失败", e);
			setCodeAndMessage(tradeQueryReseponseBo,
					ErrorCodeEnum.ALIPAY_EXCETION,
					e.getErrCode(),
					e.getErrMsg());
			return tradeQueryReseponseBo;
		}

		if(alipayTradeQueryResponse!=null){
			setCodeAndMessage(tradeQueryReseponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_QUERY_FAIL,
					alipayTradeQueryResponse.getSubCode(),
					alipayTradeQueryResponse.getSubMsg());
		}else{
			setCodeAndMessage(tradeQueryReseponseBo,
					ErrorCodeEnum.ALIPAY_TRADE_QUERY_FAIL);
		}
		return tradeQueryReseponseBo;
	}
}
