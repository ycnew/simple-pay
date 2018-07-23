package cn._42pay.simplepay.framework.exception;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 * @author kevin 2018-06-26
 * @version 1.0
 */
@Getter
@Setter
public class SimplePayException extends RuntimeException{
	private String resultCode;
	private Throwable e;

	public SimplePayException(String resultCode, String resultMessage) {
		super(resultMessage);
		this.resultCode = resultCode;
	}

	public SimplePayException(ErrorCodeEnum errorCodeEnum) {
		super(errorCodeEnum.getMessage());
		this.resultCode = errorCodeEnum.getCode();
	}

	public SimplePayException(int index, String resultMessage) {
		super(resultMessage);
		this.resultCode = String.valueOf(index);
	}

	public SimplePayException(String resultCode, String resultMessage, Throwable e) {
		super(resultMessage);
		this.resultCode = resultCode;
		this.e = e;
	}
}
