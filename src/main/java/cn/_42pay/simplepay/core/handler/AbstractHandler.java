package cn._42pay.simplepay.core.handler;

import cn._42pay.simplepay.config.PayConfig;
import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.core.bo.*;
import cn._42pay.simplepay.core.iface.IPayHandler;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.WXPayUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2018/6/26.
 */
public abstract class AbstractHandler implements IPayHandler {

	private static final Integer MAX_BARCODE_PAY_CANCEL=5;
	private static final Long BARCODE_PAY_SLEEP_TIME=4L; //最先单位秒
	private static final Long BARCODE_PAY_CANCEL_SLEEP_TIME=1L; //最先单位秒

	private String getRsaSignType(String alipayPublicKey){
		String signType = "RSA";
		if (alipayPublicKey.length() > 256) {
			signType = "RSA2";
		}
		return signType;
	}

	protected String getAlipayGateWay(){
		PayConfig.AlipayConfig alipayConfig= SpringContextHandler.getBean(PayConfig.class).getAlipayConfigInstance();
		return alipayConfig.getGateWayUrl();
	}

	protected String getReturnUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getPayReturnUrl();
	}

	protected String getAlipayNotifyUrl(){
		PayConfig.AlipayConfig alipayConfig= SpringContextHandler.getBean(PayConfig.class).getAlipayConfigInstance();
		return alipayConfig.getNotifyUrl();
	}

	protected String getAlipayWebPayTimeout(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getAlipayWebPayTimeout();
	}

	public AlipayClient getAlipayClient(PayBaseRequestBo payBaseRequestBo){
		return new DefaultAlipayClient(
				getAlipayGateWay(),
				payBaseRequestBo.getRequestPayAppId(),
				payBaseRequestBo.getPaymentSetting().getPrivateKey(),
				PayConst.JSON_FORMAT,
				PayConst.UTF8,
				payBaseRequestBo.getPaymentSetting().getPayPublicKey(),
				getRsaSignType(payBaseRequestBo.getPaymentSetting().getPayPublicKey())
		);
	}

	/**
	 * 设置返回的参数
	 * @param payBaseResponseBo
	 * @param errorCodeEnum
	 */
	public void setCodeAndMessage(PayBaseResponseBo payBaseResponseBo, ErrorCodeEnum errorCodeEnum){
		setCodeAndMessage(payBaseResponseBo,errorCodeEnum,"","");
	}

	/**
	 * 设置返回的参数
	 * @param payBaseResponseBo
	 * @param errorCodeEnum
	 * @param payErrorCode
	 * @param payErrorMessage
	 */
	public void setCodeAndMessage(PayBaseResponseBo payBaseResponseBo, ErrorCodeEnum errorCodeEnum,String payErrorCode,String payErrorMessage){
		String errorMessage=errorCodeEnum.getMessage();
		if(StringUtils.isNotBlank(payErrorCode)&&
				StringUtils.isNotBlank(payErrorMessage)){
			errorMessage=errorMessage+"->["+payErrorCode+","+payErrorMessage+"]";
		}
		payBaseResponseBo.buildResultCodeAndResultMessage(
				errorCodeEnum.getCode()
				,errorMessage);
	}

	/**
	 * 获取微信的换取accessToken Url
	 * @return
	 */
	protected String getWechatAccessTokenUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getAccessTokenUrl();
	}

	/**
	 * 获取微信统一下单的url
	 * @return
	 */
	protected String getWechatPayUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getPayUrl();
	}

	/**
	 * 获取微信支付异步通知的地址
	 * @return
	 */
	protected String getWechatPayNotifyUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getNotifyUrl();
	}

	/**
	 * 获取统一支付的签名
	 * @param response
	 * @param apiSecret
	 * @return
	 * @throws Exception
	 */
	protected String buildWechatJsapiSign(InWapPayResponseBo response, String apiSecret) throws Exception {
		Map<String, String> result = new HashMap<>(10);
		result.put("appId", response.getAppId());
		result.put("timeStamp", response.getTimeStamp());
		result.put("nonceStr", response.getNonceStr());
		result.put("package", response.getPrepayId());
		result.put("signType",response.getSignType());
		return WXPayUtil.generateSignature(result,apiSecret);
	}

	/**
	 * 获取微信退款url
	 * @return
	 */
	protected String getWechatRefundUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getRefundUrl();
	}

	protected String getWechatPayQueryUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getPayQueryUrl();
	}

	protected String getWechatCancelUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getCancelUrl();
	}

	protected String getWechatMicroPayUrl(){
		PayConfig payConfig= SpringContextHandler.getBean(PayConfig.class);
		return payConfig.getWechatConfigInstance().getMicroPayUrl();
	}

	/**
	 * 循环查询支付宝，查看交易是否成功
	 * @param barcodePayRequestBo
	 * @return
	 */
	protected BarcodePayResponseBo loopBarcodePayQuery(BarcodePayRequestBo barcodePayRequestBo, int queryCount){
		BarcodePayResponseBo barcodePayResponseBo=new BarcodePayResponseBo();

		TradeQueryRequestBo tradeQueryRequestBo=new TradeQueryRequestBo();
		tradeQueryRequestBo.setPaymentSetting(barcodePayRequestBo.getPaymentSetting());
		tradeQueryRequestBo.setRequestPayAppId(barcodePayRequestBo.getRequestPayAppId());
		tradeQueryRequestBo.setPaymentDealNo(barcodePayRequestBo.getPaymentDealNo());

		for (int i = 0; i < queryCount; i++) {
			try {
				Thread.sleep(BARCODE_PAY_SLEEP_TIME*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			TradeQueryReseponseBo tradeQueryReseponseBo = tradeQuery(tradeQueryRequestBo);
			if(tradeQueryReseponseBo.isSuccess()){
				barcodePayResponseBo.setPayAmount(tradeQueryReseponseBo.getPayAmount());
				barcodePayResponseBo.setPaymentDealId(tradeQueryReseponseBo.getPaymentDealId());
				barcodePayResponseBo.setPayUserId(tradeQueryReseponseBo.getPayUserId());
				barcodePayResponseBo.setPaymentDealNo(tradeQueryReseponseBo.getPaymentDealNo());
				barcodePayResponseBo.setPayTime(tradeQueryReseponseBo.getPayTime());
				setCodeAndMessage(barcodePayResponseBo, ErrorCodeEnum.SUCCESS);
				return barcodePayResponseBo;
			}
		}

		Log.i(LogScene.PAY_BARCODE, "查询最大次数后，条码付还是失败，即将发起撤销" + JSON.toJSONString(barcodePayRequestBo));
		CancelResponseBo cancelResponseBo=loopBarcodePayCancel(barcodePayRequestBo,MAX_BARCODE_PAY_CANCEL);	//发起撤销
		Log.i(LogScene.PAY_BARCODE, "撤销操作完成后，最终结果为:" + JSON.toJSONString(cancelResponseBo));

		setCodeAndMessage(barcodePayResponseBo,
				ErrorCodeEnum.BARCODE_PAY_FAIL);
		return barcodePayResponseBo;
	}

	/**
	 * 循环撤销交易
	 * @param barcodePayRequestBo
	 * @param cancelCount
	 * @return
	 */
	protected CancelResponseBo loopBarcodePayCancel(BarcodePayRequestBo barcodePayRequestBo,int cancelCount){
		CancelResponseBo cancelResponseBo=new CancelResponseBo();

		CancelRequestBo cancelRequestBo=new CancelRequestBo();
		cancelRequestBo.setPaymentSetting(barcodePayRequestBo.getPaymentSetting());
		cancelRequestBo.setRequestPayAppId(barcodePayRequestBo.getRequestPayAppId());
		cancelRequestBo.setPaymentDealNo(barcodePayRequestBo.getPaymentDealNo());

		for (int i = 0; i < cancelCount; i++) {
			try {
				Thread.sleep(BARCODE_PAY_CANCEL_SLEEP_TIME * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cancelResponseBo=cancel(cancelRequestBo);
			if(cancelResponseBo.isSuccess()){
				return cancelResponseBo;
			}
		}

		setCodeAndMessage(cancelResponseBo,
				ErrorCodeEnum.ALIPAY_TRADE_CANCEL_FAIL
		);
		return cancelResponseBo;
	}

}
