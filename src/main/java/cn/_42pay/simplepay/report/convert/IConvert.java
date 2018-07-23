package cn._42pay.simplepay.report.convert;

import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.report.entity.PayReportResponse;

/**
 * Created by kevin on 2018/7/6.
 */
public interface IConvert {
	/**
	 * 参数转换
	 * @param body
	 * @return
	 * @throws SimplePayException
	 */
	PayReportResponse convert(String body) throws SimplePayException;

	/**
	 * 获取成功通知参数
	 * @return
	 */
	String getNotifySuccessMessage();


	/**
	 * 获取失败通知参数
	 * @return
	 */
	String getNotifyFailureMessage();
}
