package cn._42pay.simplepay.service.sys.impl;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.constant.PayStatusEnum;
import cn._42pay.simplepay.db.entity.PageInfo;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.service.AbstractService;
import cn._42pay.simplepay.service.sys.SysPaymentService;
import cn._42pay.simplepay.vo.sys.PaymentJournalListRequestVo;
import cn._42pay.simplepay.vo.sys.PaymentJournalListResponseVo;
import cn._42pay.simplepay.vo.sys.PaymentJournalVo;
import cn._42pay.simplepay.vo.sys.PaymentSettingRequestVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2018/7/17.
 */
@Service
public class SysPaymentServiceImpl extends AbstractService implements SysPaymentService {
	@Override
	public long getPjCountByUserIdAndStatus(Long userId, Integer payStatus) {
		PaymentJournal wheres=new PaymentJournal();
		wheres.setUserId(userId);
		wheres.setPayStatus(payStatus);
		return paymentJournalDao.selectCount(wheres);
	}

	@Override
	public PaymentJournalListResponseVo listPaymentJournal(Long userId,
														   PaymentJournalListRequestVo paymentJournalListRequestVo) throws SimplePayException {
		PaymentJournalListResponseVo paymentJournalListResponseVo=new PaymentJournalListResponseVo();

		PageInfo pageInfo =new PageInfo(paymentJournalListRequestVo.getPageNum(),
				paymentJournalListRequestVo.getPageSize());

		PaymentJournal where=new PaymentJournal();
		if(paymentJournalListRequestVo.getPayStatus()!=null&&
				paymentJournalListRequestVo.getPayStatus()!=-1){
			where.setPayStatus(paymentJournalListRequestVo.getPayStatus());
		}
		if(StringUtils.isNotBlank(paymentJournalListRequestVo.getMerchantOrderNo())){
			where.setMerchantOrderNo(paymentJournalListRequestVo.getMerchantOrderNo());
		}
		if(StringUtils.isNotBlank(paymentJournalListRequestVo.getPayCode())){
			where.setPayCode(paymentJournalListRequestVo.getPayCode());
		}
		where.setBeginCreateTime(paymentJournalListRequestVo.getBeginCreateTime()+" 00:00:00");
		where.setEndCreateTime(paymentJournalListRequestVo.getEndCreateTime()+" 23:59:59");
		where.setUserId(userId);
		long totalCount=paymentJournalDao.selectCount(where);
		if(totalCount==0){
			paymentJournalListResponseVo.setCodeAndMessage(ErrorCodeEnum.SYS_PAYMENT_JOURNAL_EMPTY);
			return paymentJournalListResponseVo;
		}
		List<PaymentJournal> paymentJournals=paymentJournalDao.select(where,pageInfo);

		pageInfo.setTotal(totalCount);
		pageInfo.setSize(paymentJournals.size());
		pageInfo.setPages((pageInfo.getTotal().intValue() + pageInfo.getPageSize() - 1) / pageInfo.getPageSize());
		paymentJournalListResponseVo.setPageInfo(pageInfo);
		paymentJournalListResponseVo.setCodeAndMessage(ErrorCodeEnum.SUCCESS);
		List<PaymentJournalVo> paymentJournalVos=new ArrayList<>();
		for(PaymentJournal paymentJournal:paymentJournals){
			PaymentJournalVo paymentJournalVo=new PaymentJournalVo();
			BeanUtils.copyProperties(paymentJournal,paymentJournalVo);
			paymentJournalVo.setPayCode(PayCodeEnum.valueOf(paymentJournal.getPayCode()).getDesc());
			paymentJournalVo.setPayStatus(PayStatusEnum.getEnum(paymentJournal.getPayStatus()).getMessage());
			paymentJournalVo.setTotalAmount(String.valueOf(new BigDecimal(paymentJournal.getTotalAmount()).divide(new BigDecimal(100))));
			paymentJournalVos.add(paymentJournalVo);
		}
		paymentJournalListResponseVo.setPaymentJournalVoList(paymentJournalVos);

		return paymentJournalListResponseVo;
	}

	@Override
	public PaymentSetting getPaymentSettingByUserIdAndPayMode(Long userId, Integer payMode) {
		PaymentSetting wheres=new PaymentSetting();
		wheres.setUserId(userId);
		wheres.setPayMode(payMode);
		List<PaymentSetting> paymentSettings= paymentSettingDao.select(wheres);
		if(CollectionUtils.isEmpty(paymentSettings)){
			return null;
		}
		return paymentSettings.get(0);
	}

	@Override
	public void updatePaymentSetting(PaymentSettingRequestVo paymentSettingRequestVo) {
		//构造paymentSetting
		PaymentSetting paymentSetting=new PaymentSetting();
		BeanUtils.copyProperties(paymentSettingRequestVo,paymentSetting);
		if(paymentSettingRequestVo.isUpdateFlag()){
			paymentSetting.setUpdateTime(DateUtil.dateToString(new Date()));
			paymentSettingDao.update(paymentSetting);
		}else{
			paymentSetting.setCreateTime(DateUtil.dateToString(new Date()));
			if(paymentSettingRequestVo.getPayMode()==1){
				paymentSetting.setPayCode(PayCodeEnum.WX_JSAPI.getCode()+
						","
						+PayCodeEnum.WX_H5.getCode()
						+","
						+PayCodeEnum.WX_MICROPAY.getCode()
				);
			}else{
				paymentSetting.setPayCode(PayCodeEnum.ALIPAY_WEB.getCode()+
						","
						+PayCodeEnum.ALIPAY_WAP.getCode()
						+","
						+PayCodeEnum.ALIPAY_BARPAY.getCode()
				);
			}
			paymentSettingDao.insert(paymentSetting);
		}
	}
}
