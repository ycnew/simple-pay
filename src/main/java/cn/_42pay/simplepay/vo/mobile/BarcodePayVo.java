package cn._42pay.simplepay.vo.mobile;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;

/**
 * Created by kevin on 2018/7/11.
 */
@Getter
@Setter
public class BarcodePayVo {
	@NotBlank(message="必填")
	private String merchantOrderNo;

	@Range(min=0,max=999999999)
	private Integer payAmount;

	private String payAppId;

	@NotBlank(message="必填")
	private String userId;

	@NotBlank(message="必填")
	private String barcode;

	@NotBlank(message="必填")
	private String description;
}
