package cn._42pay.simplepay.service.mobile;

import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;

/**
 * Created by kevin on 2018/6/27.
 */
public interface PaymentService {
	/***
	 * 通过userId+payCode去找出支付配置
	 * @param userId
	 * @param payCode
	 * @return
	 */
	PaymentSetting getPaymentSettingByUserIdAndPayCode(Long userId, String payCode);

	/**
	 * 通过payAppId+merchantId去找出支付配置
	 * @param payAppId
	 * @param merchantId
	 * @return
	 */
	PaymentSetting getPaymentSettingByAppIdAndMerchantId(String payAppId,String merchantId);

	/**
	 * 新增支付流水
	 * @param userSelectPaySettingr
	 * @param merchantOrderNo
	 * @param desc
	 * @param payAmount
	 * @return PaymentJournal
	 */
	PaymentJournal buildPaymentJournal(UserSelectPaySetting userSelectPaySettingr, String merchantOrderNo, String desc, Integer payAmount) throws SimplePayException;

	/**
	 * 更新支付流水的金额
	 * @param paymentJournalId
	 * @param payAmount
	 */
	void updatePaymentJournalMoney(Long paymentJournalId,Integer payAmount,Integer payStatus);

	/**
	 * 根据Id查询支付流水
	 * @param paymentJournalId
	 * @return
	 */
	PaymentJournal getPaymentJournalById(Long paymentJournalId);

	/**
	 * 支付流水No
	 * @param paymentDealNo
	 * @return
	 */
	PaymentJournal getPaymentJournalByNo(String paymentDealNo);

	/**
	 * 更新paymentjournal
	 * @param paymentJournal
	 */
	void updatePaymentJoural(PaymentJournal paymentJournal);


	PaymentJournal buildRefundPaymentJournal(PaymentJournal paymentJournal) throws SimplePayException;
}
