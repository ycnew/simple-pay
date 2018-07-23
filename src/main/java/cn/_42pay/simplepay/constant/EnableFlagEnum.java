package cn._42pay.simplepay.constant;


/**
 * Created by kevin on 2018/6/27.
 */
public enum  EnableFlagEnum {
	DISABLE(0,"禁用"),
	ENABLE(1,"启用"),
	;

	//成员变量
	private String message;

	private int index;

	//构造方法
	private EnableFlagEnum(int index, String name) {
		this.message = name;
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
}
