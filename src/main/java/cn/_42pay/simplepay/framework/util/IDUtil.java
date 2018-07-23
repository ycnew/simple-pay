package cn._42pay.simplepay.framework.util;

import com.eaio.uuid.UUID;


/**
 * ID工具
 */
public class IDUtil {
	/**
	 * 生成32位具有时间顺序的UUID
	 * @return
	 */
	public static String getUUID32() {
		return new UUID().toString().replaceAll("-", "");
	}
	/**
	 * 生成36位具有时间顺序的UUID
	 * @return
	 */
	public static String getUUID36() {
		return new UUID().toString();
	}

	public static String getRandomUUID32(){
		String uuid=java.util.UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
}
