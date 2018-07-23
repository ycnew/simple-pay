package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/12.
 */
@Getter
@Setter
public class CancelRequestBo extends PayBaseRequestBo{
	private String paymentDealId;
	private String paymentDealNo;
}
