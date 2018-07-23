package cn._42pay.simplepay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 2018/6/25.
 */
@Configuration
@Setter
@Getter
public class PayConfig {
	@Value("${pay.alipay.sanbox-flag}")
	private Integer alipaySanboxFlag;

	@Value("${pay.alipay.sanbox.auth-url}")
	private String alipaySanboxAuthUrl;

	@Value("${pay.alipay.sanbox.gateway-url}")
	private String alipaySanboxGatewayUrl;

	@Value("${pay.alipay.release.auth-url}")
	private String alipayReleaseAuthUrl;

	@Value("${pay.alipay.release.gateway-url}")
	private String alipayReleaseGatewayUrl;

	@Value("${pay.alipay.web-pay-timeout}")
	private String alipayWebPayTimeout;

	@Value("${pay.wechat.release.auth-url}")
	private String wechatRelaseAuthUrl;

	@Value("${pay.wechat.release.pay-url}")
	private String wechatReleasePayUrl;

	@Value("${pay.wechat.release.accesstoken-url}")
	private String wechatReleaseAccessTokenUrl;

	@Value("${pay.wechat.release.refund-url}")
	private String wechatReleaseRefundUrl;

	@Value("${pay.wechat.release.pay-query-url}")
	private String wechatReleasPayQueryUrl;

	@Value("${pay.wechat.release.cancel-url}")
	private String wechatReleaseCancelUrl;

	@Value("${pay.wechat.release.micro-pay-url}")
	private String wechatReleaseMicroPayUrl;

	@Value("${pay.upload-folder}")
	private String uploadFolder;

	private String payReturnUrl;

	private static volatile AlipayConfig alipayConfig;
	public  AlipayConfig getAlipayConfigInstance(){
		if(alipayConfig==null){
			alipayConfig=new AlipayConfig();
			if(alipaySanboxFlag==1){
				alipayConfig.setAuthUrl(alipaySanboxAuthUrl);
				alipayConfig.setGateWayUrl(alipaySanboxGatewayUrl);
			}else{
				alipayConfig.setAuthUrl(alipayReleaseAuthUrl);
				alipayConfig.setGateWayUrl(alipayReleaseGatewayUrl);
			}
		}
		return alipayConfig;
	}

	@Getter
	@Setter
	public static final class AlipayConfig{
		private String authUrl;
		private String gateWayUrl;
		private String notifyUrl;
	}

	private static volatile WechatConfig wechatConfig;
	public  WechatConfig getWechatConfigInstance(){
		if(wechatConfig==null){
			wechatConfig=new WechatConfig();
			wechatConfig.setAuthUrl(wechatRelaseAuthUrl);
			wechatConfig.setAccessTokenUrl(wechatReleaseAccessTokenUrl);
			wechatConfig.setPayUrl(wechatReleasePayUrl);
			wechatConfig.setRefundUrl(wechatReleaseRefundUrl);
			wechatConfig.setPayQueryUrl(wechatReleasPayQueryUrl);
			wechatConfig.setCancelUrl(wechatReleaseCancelUrl);
			wechatConfig.setMicroPayUrl(wechatReleaseMicroPayUrl);
		}
		return wechatConfig;
	}

	@Getter
	@Setter
	public static final class WechatConfig{
		private String authUrl;
		private String accessTokenUrl;
		private String payUrl;
		private String notifyUrl;
		private String refundUrl;
		private String payQueryUrl;
		private String cancelUrl;
		private String microPayUrl;
	}
}


