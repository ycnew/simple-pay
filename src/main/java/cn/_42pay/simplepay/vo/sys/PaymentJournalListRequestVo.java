package cn._42pay.simplepay.vo.sys;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/18.
 */
@Getter
@Setter
public class PaymentJournalListRequestVo {
	@NotBlank(message="必填")
	private String beginCreateTime;
	@NotBlank(message="必填")
	private String endCreateTime;
	private String payCode;
	private Integer payStatus;
	private String merchantOrderNo;
	private Integer pageSize;
	private Integer pageNum;
}
