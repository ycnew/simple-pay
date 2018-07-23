package cn._42pay.simplepay.constant;

/**
 * Created by kevin on 2018/6/27.
 */
public enum PayStatusEnum {
	CREATE_ORDER(0,"创建(待支付)"),
	WAIT_PAY(1,"唤起收银台"),
	PAY(2,"支付成功"),
	WAIT_REFUND(3,"待退款"),
	REFUND(4,"已退款"),
			;

	private String message;
	private int index;

	PayStatusEnum(int index, String message) {
		this.message = message;
		this.index = index;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static String getName(int index) {
		for (PayStatusEnum c : PayStatusEnum.values()) {
			if (c.getIndex() == index) {
				return c.message;
			}
		}
		return null;
	}

	public static PayStatusEnum getEnum(int index) {
		for (PayStatusEnum c : PayStatusEnum.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}
}
