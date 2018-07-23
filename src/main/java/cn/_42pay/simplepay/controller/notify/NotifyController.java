package cn._42pay.simplepay.controller.notify;

import cn._42pay.simplepay.constant.PayCodeEnum;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.report.ReportMechant;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kevin on 2018/6/29.
 */
@Component
@RestController
@RequestMapping("/notify")
public class NotifyController {

	@RequestMapping(value = {"/alipayNotify"}, method = { RequestMethod.POST})
	public ResponseEntity<Object> alipayNotify(@RequestBody String body)  {
		Log.i(LogScene.NOTIFY_ALIPAY,"收到支付宝回调:"+ body);
		ResponseEntity<Object> responseEntity= ReportMechant.report(PayCodeEnum.ALIPAY.getCode(),body);
		Log.i(LogScene.NOTIFY_ALIPAY,"应答给支付宝:"+ JSON.toJSONString(responseEntity));
		return responseEntity;
	}

	@RequestMapping(value = {"/wechatNotify"}, method = { RequestMethod.POST})
	public ResponseEntity<Object> wechatNotify(@RequestBody String body)  {
		Log.i(LogScene.NOTIFY_WECHAT,"收到微信回调:"+ body);
		ResponseEntity<Object> responseEntity= ReportMechant.report(PayCodeEnum.WECHAT.getCode(),body);
		Log.i(LogScene.NOTIFY_WECHAT,"应答给微信:"+ JSON.toJSONString(responseEntity));
		return responseEntity;
	}

	@RequestMapping(value = {"/test"}, method = { RequestMethod.POST})
	public ResponseEntity<Object> test(@RequestBody String body)  {
		Log.i(LogScene.NOTIFY_WECHAT,"收到测试请求:"+ body);
		return new ResponseEntity<Object>("SUCCESS",HttpStatus.OK);
	}
}
