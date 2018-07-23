package cn._42pay.simplepay.service.sys;

import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.vo.sys.PaymentJournalListRequestVo;
import cn._42pay.simplepay.vo.sys.PaymentJournalListResponseVo;
import cn._42pay.simplepay.vo.sys.PaymentSettingRequestVo;

/**
 * Created by kevin on 2018/7/17.
 */
public interface SysPaymentService {
	/**
	 * 获取流水记录
	 */
	long  getPjCountByUserIdAndStatus(Long userId,Integer payStatus);

	/**
	 * 获取支付流水
	 * @param userId
	 * @param paymentJournalListRequestVo
	 * @return
	 * @throws SimplePayException
	 */
	PaymentJournalListResponseVo listPaymentJournal(Long userId, PaymentJournalListRequestVo paymentJournalListRequestVo) throws SimplePayException;

	/***
	 * 通过userId+payMode
	 * @param userId
	 * @param payMode
	 * @return
	 */
	PaymentSetting getPaymentSettingByUserIdAndPayMode(Long userId, Integer payMode);

	/**
	 * 更新支付配置
	 * @param paymentSettingRequestVo
	 */
	void updatePaymentSetting(PaymentSettingRequestVo paymentSettingRequestVo);
}
