package cn._42pay.simplepay.vo.mobile;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/7/11.
 */
@Getter
@Setter
public class BarcodePayOrderInfoVo {
	private String merchantOrderNo;
	private String payAmount;
	private String description;
	private String barcode;
	private String payAppId;
	private String userId;
}
