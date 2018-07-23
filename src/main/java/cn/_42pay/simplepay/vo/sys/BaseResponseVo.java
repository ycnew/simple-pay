package cn._42pay.simplepay.vo.sys;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.db.entity.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by kevin on 2018/7/16.
 */
@Getter
@Setter
public class BaseResponseVo implements Serializable {
	private String resultCode;
	private String resultMessage;
	private PageInfo pageInfo;

	public BaseResponseVo(){
		this.resultMessage=ErrorCodeEnum.DEFUALT_ERROR.getMessage();
		this.resultCode=ErrorCodeEnum.DEFUALT_ERROR.getCode();
	}

	public void setCodeAndMessage(ErrorCodeEnum errorCodeEnum){
		this.resultCode=errorCodeEnum.getCode();
		this.resultMessage=errorCodeEnum.getMessage();
	}

	public void setCodeAndMessage(String resultCode,String resultMessage){
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
