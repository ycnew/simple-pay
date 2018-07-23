package cn._42pay.simplepay.constant;

/**
 * Created by kevin on 2018/7/4.
 */
public enum  HttpContentType {
	TEXTHTML("text/html; charset=utf-8"),
	FORMURLENCODED("application/x-www-form-urlencoded; charset=utf-8"),
	APPLICATIONJSON("application/json; charset=utf-8"),
			;

	private String type;

	HttpContentType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
