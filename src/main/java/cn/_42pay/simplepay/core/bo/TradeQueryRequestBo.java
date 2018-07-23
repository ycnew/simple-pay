package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/20.
 */
@Getter
@Setter
public class TradeQueryRequestBo extends PayBaseRequestBo{
	private String paymentDealId;
	private String paymentDealNo;
}
