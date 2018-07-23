package cn._42pay.simplepay.service;

import cn._42pay.simplepay.db.dao.PaymentJournalDao;
import cn._42pay.simplepay.db.dao.PaymentSettingDao;
import cn._42pay.simplepay.db.dao.PaymentSettingExDao;
import cn._42pay.simplepay.db.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础类
 * Created by kevin on 2018/6/26.
 */
public abstract class AbstractService {
	@Autowired
	public PaymentSettingDao paymentSettingDao;

	@Autowired
	public PaymentJournalDao paymentJournalDao;

	@Autowired
	public PaymentSettingExDao paymentSettingExDao;

	@Autowired
	public UserDao userDao;
}
