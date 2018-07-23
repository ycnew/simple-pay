package cn._42pay.simplepay.vo.mobile;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * Created by kevin on 2018/7/11.
 */
@Getter
@Setter
public class BarcodePayResponseVo implements Serializable{
	private String resultCode;
	private String resultMessage;
	private String paymentDealId;
	private String payUserId;
	private Integer payAmount;
	private String paymentDealNo;

	public void setCodeAndMessage(String resultCode,String resultMessage){
		this.resultCode=resultCode;
		this.resultMessage=resultMessage;
	}
}
