package cn._42pay.simplepay.service.mobile.impl;

import cn._42pay.simplepay.constant.EnableFlagEnum;
import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.framework.util.OrderUtil;
import cn._42pay.simplepay.framework.util.SnowflakeUtil;
import cn._42pay.simplepay.service.AbstractService;
import cn._42pay.simplepay.service.mobile.PaymentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kevin on 2018/6/27.
 */
@Service
public class PaymentServiceImpl extends AbstractService implements PaymentService{

	@Override
	public PaymentSetting getPaymentSettingByUserIdAndPayCode(Long userId, String payCode) {
		return paymentSettingExDao.selectPaymentSettingByUserIdAndPayCode(userId,payCode);
	}

	@Override
	public PaymentSetting getPaymentSettingByAppIdAndMerchantId(String payAppId,String merchantId){
		PaymentSetting where=new PaymentSetting();
		where.setPayAppId(payAppId);
		where.setEnableFlag((short)EnableFlagEnum.ENABLE.getIndex());
		if(StringUtils.isNotBlank(merchantId)){
			where.setMerchantId(merchantId);
		}
		List<PaymentSetting> paymentSettingList=paymentSettingDao.select(where);
		if(CollectionUtils.isEmpty(paymentSettingList)){
			return null;
		}
		return paymentSettingList.get(0);
	}

	@Override
	public PaymentJournal buildPaymentJournal(UserSelectPaySetting userSelectPaySetting, String merchantOrderNo, String desc, Integer payAmount) {
		//首先该userId下面这个商户订单号是否已经存在
		PaymentJournal where=new PaymentJournal();
		where.setMerchantOrderNo(merchantOrderNo);
		where.setUserId(userSelectPaySetting.getUserId());
		List<PaymentJournal> paymentJournal=paymentJournalDao.select(where);

		long payCount=paymentJournal.stream().filter(p->p.getPayStatus()==PayStatusEnum.PAY.getIndex()
				||p.getPayStatus()==PayStatusEnum.REFUND.getIndex()
				||p.getPayStatus()==PayStatusEnum.WAIT_PAY.getIndex()).
				count();
		if(payCount>0){
			throw new SimplePayException(ErrorCodeEnum.ORDER_IS_PAYED);
		}

		long notPayCount=paymentJournal.stream().filter(p->p.getPayStatus()==PayStatusEnum.CREATE_ORDER.getIndex()
				||p.getPayStatus()==PayStatusEnum.WAIT_PAY.getIndex()).
				count();
		if(notPayCount>0){
			return paymentJournal.stream().filter(p->p.getPayStatus()==PayStatusEnum.CREATE_ORDER.getIndex()
					||p.getPayStatus()==PayStatusEnum.WAIT_PAY.getIndex()).collect(Collectors.toList()).get(0);
		}

		if(payAmount==null){
			payAmount=0;
		}

		PaymentJournal insertPaymentJournal=new PaymentJournal();
		insertPaymentJournal.setPaymentJournalId(SnowflakeUtil.getInstance().nextId());
		insertPaymentJournal.setPaymentDealNo(OrderUtil.generatePaymentDealNo());
		insertPaymentJournal.setUserId(userSelectPaySetting.getUserId());
		insertPaymentJournal.setUserName(userSelectPaySetting.getUser().getUserName());
		insertPaymentJournal.setPayAppId(userSelectPaySetting.getPayAppId());
		insertPaymentJournal.setMerchantId(userSelectPaySetting.getMerchantId());
		insertPaymentJournal.setMerchantOrderNo(merchantOrderNo);
		insertPaymentJournal.setPaymentDealId(null);
		insertPaymentJournal.setPayStatus(PayStatusEnum.CREATE_ORDER.getIndex());
		insertPaymentJournal.setPayCode(userSelectPaySetting.getPayCode());
		insertPaymentJournal.setPayAmount(payAmount);
		insertPaymentJournal.setAccountAmount(0);
		insertPaymentJournal.setMedicareAmount(0);
		insertPaymentJournal.setInsuranceAmount(0);
		insertPaymentJournal.setTotalAmount(insertPaymentJournal.getPayAmount()+insertPaymentJournal.getInsuranceAmount());
		insertPaymentJournal.setDescription(desc);
		insertPaymentJournal.setExtraParams(null);
		insertPaymentJournal.setCreateTime(DateUtil.dateToString(new Date()));
		insertPaymentJournal.setDataSource("web");

		try{
			paymentJournalDao.insert(insertPaymentJournal);
		}catch (Exception ex){
			Log.e(LogScene.PAY,"插入流水异常",ex);
			throw new SimplePayException(ErrorCodeEnum.INSRT_PAYMENT_JOURNAL_ERROR);
		}
		return insertPaymentJournal;
	}

	@Override
	public void updatePaymentJournalMoney(Long paymentJournalId, Integer payAmount, Integer payStatus) {
		PaymentJournal updatePaymentJournal=new PaymentJournal();
		updatePaymentJournal.setPaymentJournalId(paymentJournalId);
		updatePaymentJournal.setPayStatus(payStatus);
		updatePaymentJournal.setPayAmount(payAmount);
		updatePaymentJournal.setTotalAmount(payAmount);
		try{
			paymentJournalDao.update(updatePaymentJournal);
		}catch (Exception ex){
			Log.e(LogScene.PAY,"更新流水异常",ex);
			throw new SimplePayException(ErrorCodeEnum.UPDATE_PAYMENT_JOURNAL_ERROR);
		}
	}

	@Override
	public PaymentJournal getPaymentJournalById(Long paymentJournalId){
		return paymentJournalDao.selectById(String.valueOf(paymentJournalId));
	}

	@Override
	public PaymentJournal getPaymentJournalByNo(String paymentDealNo) {
		PaymentJournal wheres=new PaymentJournal();
		wheres.setPaymentDealNo(paymentDealNo);
		List<PaymentJournal> paymentJournalList=paymentJournalDao.select(wheres);
		if(CollectionUtils.isEmpty(paymentJournalList)){
			return null;
		}
		return paymentJournalList.get(0);
	}

	@Override
	public void updatePaymentJoural(PaymentJournal paymentJournal) {
		 paymentJournalDao.update(paymentJournal);
	}

	@Override
	public PaymentJournal buildRefundPaymentJournal(PaymentJournal paymentJournal) throws SimplePayException {
		PaymentJournal insertPaymentJournal=new PaymentJournal();
		insertPaymentJournal.setPaymentJournalId(SnowflakeUtil.getInstance().nextId());
		insertPaymentJournal.setPaymentDealNo(OrderUtil.generatePaymentDealNo());
		insertPaymentJournal.setUserId(paymentJournal.getUserId());
		insertPaymentJournal.setUserName(paymentJournal.getUserName());
		insertPaymentJournal.setPayAppId(paymentJournal.getPayAppId());
		insertPaymentJournal.setMerchantId(paymentJournal.getMerchantId());
		insertPaymentJournal.setMerchantOrderNo(paymentJournal.getMerchantOrderNo());
		insertPaymentJournal.setPaymentDealId(null);
		insertPaymentJournal.setPayStatus(PayStatusEnum.WAIT_REFUND.getIndex());
		insertPaymentJournal.setPayCode(paymentJournal.getPayCode());
		insertPaymentJournal.setPayAmount(paymentJournal.getPayAmount());
		insertPaymentJournal.setAccountAmount(paymentJournal.getAccountAmount());
		insertPaymentJournal.setMedicareAmount(paymentJournal.getMedicareAmount());
		insertPaymentJournal.setInsuranceAmount(paymentJournal.getInsuranceAmount());
		insertPaymentJournal.setTotalAmount(paymentJournal.getTotalAmount());
		insertPaymentJournal.setDescription("正常退费");
		insertPaymentJournal.setExtraParams(null);
		insertPaymentJournal.setCreateTime(DateUtil.dateToString(new Date()));
		insertPaymentJournal.setDataSource("refund");

		try{
			paymentJournalDao.insert(insertPaymentJournal);
		}catch (Exception ex){
			Log.e(LogScene.PAY,"插入流水异常",ex);
			throw new SimplePayException(ErrorCodeEnum.INSRT_PAYMENT_JOURNAL_ERROR);
		}
		return insertPaymentJournal;
	}
}
