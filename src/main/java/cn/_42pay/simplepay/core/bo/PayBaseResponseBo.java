package cn._42pay.simplepay.core.bo;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class PayBaseResponseBo {
	private String resultCode;
	private String resultMessage;
	public PayBaseResponseBo(){
		this.resultCode= String.valueOf(ErrorCodeEnum.DEFUALT_ERROR.getIndex());
		this.resultMessage= ErrorCodeEnum.DEFUALT_ERROR.getMessage();
	}

	public void buildResultCodeAndResultMessage(String resultCode,String resultMessage){
		this.resultCode=resultCode;
		this.resultMessage=resultMessage;
	}

	public boolean isSuccess(){
		if(ErrorCodeEnum.SUCCESS.getCode().equals(resultCode)){
			return true;
		}
		return false;
	}
}
