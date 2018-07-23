package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class InWapPayRequestBo extends PayBaseRequestBo {
	private String description;
	private String subject;
	private Integer payAmount;
	private String paymentDealNo;
}
