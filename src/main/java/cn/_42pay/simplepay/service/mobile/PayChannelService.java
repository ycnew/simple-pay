package cn._42pay.simplepay.service.mobile;

import cn._42pay.simplepay.core.bo.*;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.framework.entity.UnifiedPayResponse;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;

/**
 * Created by kevin on 2018/6/27.
 */
public interface PayChannelService {

	/**
	 * 获取accessToken
	 * @param getAccessTokenRequestBo
	 * @return
	 * @throws SimplePayException
	 */
	GetAccessTokenResponseBo getAccessToken(GetAccessTokenRequestBo getAccessTokenRequestBo) throws SimplePayException;

	/**
	 * 获取用户信息
	 * @param getUserInfoRequestBo
	 * @return
	 */
	GetUserInfoResponseBo getUserInfo(GetUserInfoRequestBo getUserInfoRequestBo) throws SimplePayException;

	/**
	 * webPay调度器
	 * @param userSelectPaySetting
	 * @param getUserInfoResponseBo
	 * @return
	 * @throws SimplePayException
	 */
	UnifiedPayResponse webPayDispatch(UserSelectPaySetting userSelectPaySetting,GetUserInfoResponseBo getUserInfoResponseBo,PaymentJournal paymentJournal) throws  SimplePayException;

	/**
	 * 条码付
	 * @param userSelectPaySetting
	 * @param getUserInfoResponseBo
	 * @param paymentJournal
	 * @return
	 */
	BarcodePayResponseBo barcodePay(UserSelectPaySetting userSelectPaySetting,GetUserInfoResponseBo getUserInfoResponseBo,PaymentJournal paymentJournal,String authCode) throws  SimplePayException;

	/**
	 * 退费
	 * @param userId
	 * @param paymentDealNo
	 * @return
	 */
	RefundResponseBo refund(Long userId,String paymentDealNo);
}
