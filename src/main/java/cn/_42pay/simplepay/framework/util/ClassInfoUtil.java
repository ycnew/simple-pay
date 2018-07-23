package cn._42pay.simplepay.framework.util;

/**
 * Created by kevin on 2018/6/27.
 */
public class ClassInfoUtil {
	private static final int originStackIndex = 2;

	public static String getFileName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getFileName();
	}

	public static String getClassName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getClassName();
	}

	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getMethodName();
	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getLineNumber();
	}
}
