package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/12.
 */
@Getter
@Setter
public class CancelResponseBo extends PayBaseResponseBo{
	private String retryFlag;//Y需要重试  N 不需要重试
	private String paymentDealId;
	private String paymentDealNo;
}
