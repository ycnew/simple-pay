package cn._42pay.simplepay.core.iface;

import cn._42pay.simplepay.core.bo.*;

/**
 * Created by kevin on 2018/6/20.
 */
public interface IPayHandler {

	/**
	 * 获取AccessToken
	 * @param getAccessTokenRequestBo
	 * @return
	 */
	GetAccessTokenResponseBo getAccessToken(GetAccessTokenRequestBo getAccessTokenRequestBo);


	/**
	 * 获取用户信息
	 * @param getUserInfoRequestBo
	 * @return
	 */
	GetUserInfoResponseBo getUserInfo(GetUserInfoRequestBo getUserInfoRequestBo);


	/**
	 * 内置浏览器支付
	 * 支付宝手机网站支付
	 * 微信公众号支付（WX_JSAPI）
	 * @param inWapPayRequestBo
	 * @return
	 */
	InWapPayResponseBo inWapPay(InWapPayRequestBo inWapPayRequestBo);

	/**
	 * 外置浏览器支付
	 * 微信手机网站H5支付
	 * @param outWapPayRequestBo
	 * @return
	 */
	OutWapPayResponseBo outWapPay(OutWapPayRequestBo outWapPayRequestBo);

	/**
	 * 条码付
	 * @param barcodePayRequestBo
	 * @return
	 */
	BarcodePayResponseBo barcodePay(BarcodePayRequestBo barcodePayRequestBo);


	/**
	 * 支付退款
	 * @param refundRequestBo
	 * @return
	 */
	RefundResponseBo refund(RefundRequestBo refundRequestBo);

	/**
	 * 撤销交易
	 * @param cancelRequestBo
	 * @return
	 */
	CancelResponseBo cancel(CancelRequestBo cancelRequestBo);

	/**
	 * 交易订单查询
	 * @param tradeQueryRequestBo
	 * @return
	 */
	TradeQueryReseponseBo tradeQuery(TradeQueryRequestBo tradeQueryRequestBo);
}
