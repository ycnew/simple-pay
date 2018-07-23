package cn._42pay.simplepay.constant;

/**
 * Created by kevin on 2018/7/16.
 */
public enum TradeStateEnum {
	SUCCESS("SUCCESS","支付成功"),
	REFUND("REFUND","转入退款"),

	NOTPAY("NOTPAY","未支付"),
	USERPAYING("USERPAYING","支付中"),
	PAYERROR("PAYERROR","支付失败"),

	CLOSED("CLOSED","已经关闭"),
	REVOKED("REVOKED","撤销"),
			;
	private String code;
	private String desc;

	TradeStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
