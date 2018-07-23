package cn._42pay.simplepay.framework.util;

import java.util.Date;

/**
 * Created by kevin on 2018/6/21.
 */
public class OrderUtil {
	private static final String ORDER_NO_PREFIX="S";

	private static final String PAYMENT_DEAL_NO_PREFIX="P";

	private static final Object MERCHANT_ORDER_NO_LOCK=new Object();

	private static final Object PAYMENT_NO_LOCK=new Object();

	public static String generateOrderNo(){
		synchronized (MERCHANT_ORDER_NO_LOCK){
			return gennerateNo(ORDER_NO_PREFIX);
		}
	}

	public static String generatePaymentDealNo(){
		synchronized (PAYMENT_NO_LOCK){
			return gennerateNo(PAYMENT_DEAL_NO_PREFIX);
		}
	}

	private static String gennerateNo(String prefix){
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(DateUtil.dateToStringMillonSecondNoDivide(new Date()));
		sb.append(RandomUtil.getRandomNumberChar(3));
		return sb.toString();
	}
}
