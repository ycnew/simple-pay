package cn._42pay.simplepay.report.thread;

import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.report.entity.PayReportResponse;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import java.util.Date;

/**
 * Created by kevin on 2018/7/6.
 */
public class NotifyExecuteThread implements Runnable{

	private PayReportResponse payReportResponse;

	public NotifyExecuteThread(PayReportResponse payReportResponse){
		this.payReportResponse=payReportResponse;
	}

	@Override
	public void run() {
		PaymentServiceImpl paymentService=SpringContextHandler.getBean(PaymentServiceImpl.class);
		PaymentJournal paymentJournal=	paymentService.getPaymentJournalByNo(payReportResponse.getPaymentDealNo());
		if(paymentJournal==null){
			Log.i(LogScene.NOTIFY,"找不到："+payReportResponse.getPaymentDealNo()+" 对应的支付流水");
			return;
		}

		PaymentJournal updatePaymentJournal=new PaymentJournal();
		updatePaymentJournal.setPaymentJournalId(paymentJournal.getPaymentJournalId());
		updatePaymentJournal.setPayStatus(PayStatusEnum.PAY.getIndex());
		updatePaymentJournal.setPayTime(payReportResponse.getPayTime());
		updatePaymentJournal.setPaymentDealId(payReportResponse.getPaymentDealId());
		updatePaymentJournal.setPayAmount(payReportResponse.getPayAmount());
		updatePaymentJournal.setTotalAmount(payReportResponse.getPayAmount());
		updatePaymentJournal.setUpdateTime(DateUtil.dateToString(new Date()));
		paymentService.updatePaymentJoural(updatePaymentJournal);
	}
}
