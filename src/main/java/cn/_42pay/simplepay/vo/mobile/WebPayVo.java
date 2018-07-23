package cn._42pay.simplepay.vo.mobile;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by kevin on 2018/6/28.
 */
@Getter
@Setter
public class WebPayVo implements Serializable{
	@NotBlank(message="必填")
	private String merchantOrderNo;

	@Range(min=0,max=999999999)
	private Integer payAmount;
}
