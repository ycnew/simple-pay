package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/21.
 */
@Getter
@Setter
public class PaymentSettingRequestVo {
	/*主键id*/
	private Long paymentSettingId;

	/*支付appid*/
	@NotBlank(message="必填")
	private String payAppId;

	/*商户号*/
	@NotBlank(message="必填")
	private String merchantId;

	/*用户id*/
	private Long userId;

	/*用户姓名*/
	private String userName;

	/*授权密钥*/
	private String appSecret;

	/*接口密钥*/
	private String apiSecret;

	/*授权证书*/
	private String certificatePath;

	/**/
	private String certificatePwd;


	/*公钥*/
	private String publicKey;

	/*私钥*/
	private String privateKey;

	/*第三方支付公钥*/
	private String payPublicKey;

	/*合作者id*/
	private String partnerId;

	/*支付编码*/
	private String payCode;

	/*支付方式              */
	@Range(min=1,max=10)
	private Integer payMode;

	/**/
	private String notifyUrl;

	/*异步通知到其它客户端的key*/
	private String notifyKey;

	/*是否启用 0-不可用  1-启用*/
	private String enableFlagStr;

	private Short enableFlag;

	/*描述*/
	@NotBlank(message="必填")
	private String description;

	/**/
	private String personalTransferStr;

	private boolean updateFlag;

}
