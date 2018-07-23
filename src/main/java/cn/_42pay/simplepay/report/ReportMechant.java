package cn._42pay.simplepay.report;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.constant.PayConst;
import cn._42pay.simplepay.db.entity.PaymentSetting;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.spring.SpringContextHandler;
import cn._42pay.simplepay.framework.threadpool.WorkerThreadPool;
import cn._42pay.simplepay.framework.util.HttpUtil;
import cn._42pay.simplepay.framework.util.MD5Util;
import cn._42pay.simplepay.framework.util.MapUtil;
import cn._42pay.simplepay.report.convert.AlipayConvert;
import cn._42pay.simplepay.report.convert.IConvert;
import cn._42pay.simplepay.report.convert.WechatConvert;
import cn._42pay.simplepay.report.entity.PayReportResponse;
import cn._42pay.simplepay.report.thread.NotifyExecuteThread;
import cn._42pay.simplepay.service.mobile.impl.PaymentServiceImpl;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.Map;

/**
 * Created by kevin on 2018/7/6.
 */
public class ReportMechant {
	public static ResponseEntity<Object> report(String payMode,String body){
		//转换成统一参数
		IConvert iConvert;
		if(payMode.startsWith(PayConst.WECHAT)||payMode.startsWith(PayConst.WX)){
			iConvert=new WechatConvert();
		}else if(payMode.startsWith(PayConst.ALIPAY)){
			iConvert=new AlipayConvert();
		}else{
			throw new SimplePayException(ErrorCodeEnum.NOTIFY_EXCETION);
		}

		PayReportResponse payReportResponse;
		try{
			payReportResponse=iConvert.convert(body);
		}catch (SimplePayException spe){
			Log.e(LogScene.NOTIFY,"处理转换发生异常[code:"+spe.getResultCode()+",message:"+spe.getMessage()+"]",spe);
			return new ResponseEntity<>(iConvert.getNotifyFailureMessage(), HttpStatus.OK);
		}
		Log.i(LogScene.NOTIFY,"数据转换完成:[payMode:"+payMode+",body:"+body+",转换后的数据:"+JSON.toJSONString(payReportResponse)+"]");

		String notifyResult=doNotify(payReportResponse);
		if(PayConst.SUCCESS.equals(notifyResult)) {
			return new ResponseEntity<>(iConvert.getNotifySuccessMessage(), HttpStatus.OK);
		}else if(PayConst.EXCEPTION.equals(notifyResult)){
			return new ResponseEntity<>(iConvert.getNotifyFailureMessage(), HttpStatus.BAD_REQUEST);
		}
		else{
			return new ResponseEntity<>(iConvert.getNotifyFailureMessage(), HttpStatus.OK);
		}
	}

	public static String doNotify(PayReportResponse payReportResponse){
		//调用线程处理本地逻辑
		WorkerThreadPool workerThreadPool= SpringContextHandler.getBean("workerThreadPool");
		workerThreadPool.execute(new NotifyExecuteThread(payReportResponse));

		//如果有notifyUrl则向notifyUrl报告参数，没有则直接返回成功应答给第三方支付
		PaymentServiceImpl paymentService= SpringContextHandler.getBean(PaymentServiceImpl.class);
		PaymentSetting paymentSetting=paymentService.getPaymentSettingByAppIdAndMerchantId(payReportResponse.getPayAppId(),null);
		if(paymentSetting!=null){
			String notifyUrl=paymentSetting.getNotifyUrl();
			String notifyKey=paymentSetting.getNotifyKey();
			if(!StringUtils.isEmpty(notifyUrl)&&!StringUtils.isEmpty(notifyKey)){
				String sign=buildNotifySign(payReportResponse,notifyKey);
				payReportResponse.setSign(sign);
				String postData= JSON.toJSONString(payReportResponse);
				try {
					String postResult=HttpUtil.post(postData,notifyUrl);
					Log.i(LogScene.NOTIFY,"通知完成:[url:"+notifyUrl+",postData:"+postData+",result:"+postResult+"]");
					if(PayConst.SUCCESS.equals(postResult)){
						return PayConst.SUCCESS;
					}else{
						return PayConst.FAILURE;
					}
				} catch (IOException e) {
					Log.e(LogScene.NOTIFY,"通知异常:[url:"+notifyUrl+",postData:"+postData+"]",e);
					return PayConst.EXCEPTION;
				}
			}else{
				return PayConst.SUCCESS;
			}
		}else{
			return PayConst.SUCCESS;
		}
	}

	private static String buildNotifySign(PayReportResponse payReportResponse,String notifyKey){
		Map<String,String> signMap=JSON.parseObject(JSON.toJSONString(payReportResponse),Map.class);
		String signStr=MapUtil.coverMap2SignString(signMap);
		Log.i(LogScene.NOTIFY,"待签名string:"+signStr);
		signStr=signStr+notifyKey;
		Log.i(LogScene.NOTIFY,"待签名string+key:"+signStr);
		String sign=MD5Util.string2MD5(signStr);
		Log.i(LogScene.NOTIFY,"签名后md5:"+sign);
		return sign;
	}
}
