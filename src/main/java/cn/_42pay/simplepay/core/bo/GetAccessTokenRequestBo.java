package cn._42pay.simplepay.core.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kevin on 2018/6/26.
 */
@Setter
@Getter
public class GetAccessTokenRequestBo extends PayBaseRequestBo{
	private String authCode;
}
