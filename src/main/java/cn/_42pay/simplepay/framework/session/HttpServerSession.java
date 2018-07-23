package cn._42pay.simplepay.framework.session;

import cn._42pay.simplepay.core.bo.GetUserInfoResponseBo;
import cn._42pay.simplepay.db.entity.PaymentJournal;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.entity.UserSelectPaySetting;
import javax.servlet.http.HttpSession;

/**
 * Created by kevin on 2018/6/26.
 */
public class HttpServerSession {
	private static final String USER_SELECT_PAY_SETTING_SESSION="userSelectPaySetting";

	private static final String USER_MOBILE_AUTH_SESSION="userMobileAuth";

	private static final String USER_PAYMENT_JOURNAL_SESSION="userPaymentJournal";

	private static final String SYS_USER_LOGIN_VALIDATE_CODE="sysUserLoginValidateCode";

	private static final String SYS_USER_REGISTER_VALIDATE_CODE="sysUserRegisterValidateCode";

	private static final String SYS_USER_LOGIN_SESSION="userLoginSession";

	private static final String PERSONAL_WECHAT_TRANSFER_URL_SESSION="persionalWechatTransferUrl";

	/**
	 * 设置用户选择的支付参数
	 * @param session
	 * @param userSelectPaySetting
	 */
	public static void setUserSelectPaySettingSession(HttpSession session, UserSelectPaySetting userSelectPaySetting){
		session.setAttribute(USER_SELECT_PAY_SETTING_SESSION, userSelectPaySetting);
	}

	/**
	 * 获取用户选择的支付参数
	 * @param session
	 * @return
	 */
	public static UserSelectPaySetting getUserSelectPaySettingSession(HttpSession session){
		Object object=session.getAttribute(USER_SELECT_PAY_SETTING_SESSION);
		if(object==null)
		{
			return null;
		}
		return (UserSelectPaySetting)object;
	}

	/**
	 * 移除用户选择的支付参数
	 * @param session
	 */
	public static void removeUserSelectPaySettingSession(HttpSession session){
		session.removeAttribute(USER_SELECT_PAY_SETTING_SESSION);
	}

	/**
	 * 设置授权用户信息
	 * @param session
	 * @param getUserInfoResponseBo
	 */
	public static void setUserMobileAuth(HttpSession session, GetUserInfoResponseBo getUserInfoResponseBo){
		session.setAttribute(USER_MOBILE_AUTH_SESSION, getUserInfoResponseBo);
	}

	/**
	 * 获取授权用户信息
	 * @param session
	 * @return
	 */
	public static GetUserInfoResponseBo getUserMobileAuth(HttpSession session){
		Object object=session.getAttribute(USER_MOBILE_AUTH_SESSION);
		if(object==null)
		{
			return null;
		}
		return (GetUserInfoResponseBo)object;
	}

	/**
	 * 移除授权用户信息
	 * @param session
	 */
	public static void removeUserMobileAuth(HttpSession session){
		session.removeAttribute(USER_MOBILE_AUTH_SESSION);
	}

	/**
	 * 设置用户支付流水
	 * @param session
	 * @param paymentJournal
	 */
	public static void setUserPaymentJournalSession(HttpSession session, PaymentJournal paymentJournal){
		session.setAttribute(USER_PAYMENT_JOURNAL_SESSION, paymentJournal);
	}

	/**
	 * 获取用户支付流水
	 * @param session
	 * @return
	 */
	public static PaymentJournal getUserPaymentJournalSession(HttpSession session){
		Object object=session.getAttribute(USER_PAYMENT_JOURNAL_SESSION);
		if(object==null)
		{
			return null;
		}
		return (PaymentJournal)object;
	}

	/**
	 * 删除用户支付流水
	 * @param session
	 */
	public static void removeUserPaymentJournalgSession(HttpSession session){
		session.removeAttribute(USER_PAYMENT_JOURNAL_SESSION);
	}

	/**
	 * 设置登录的验证码
	 * @param session
	 * @param validateCode
	 */
	public static void setSysUserLoginValidateCode(HttpSession session,String validateCode){
		session.setAttribute(SYS_USER_LOGIN_VALIDATE_CODE,validateCode);
	}

	/**
	 * 获取登录的验证码
	 * @param session
	 * @return
	 */
	public static String getSysUserLoginValidateCode(HttpSession session){
		Object object=session.getAttribute(SYS_USER_LOGIN_VALIDATE_CODE);
		if(object==null){
			return null;
		}
		session.removeAttribute(SYS_USER_LOGIN_VALIDATE_CODE);
		return (String)object;
	}

	/**
	 * 设置注册的验证码
	 * @param session
	 * @param validateCode
	 */
	public static void setSysUserRegisterValidateCode(HttpSession session,String validateCode){
		session.setAttribute(SYS_USER_REGISTER_VALIDATE_CODE,validateCode);
	}

	/**
	 * 获取注册的验证码
	 * @param session
	 * @return
	 */
	public static String getSysUserRegisterValidateCode(HttpSession session){
		Object object=session.getAttribute(SYS_USER_REGISTER_VALIDATE_CODE);
		if(object==null){
			return null;
		}
		session.removeAttribute(SYS_USER_REGISTER_VALIDATE_CODE);
		return (String)object;
	}

	/**
	 * 设置登录用户的信息
	 * @param session
	 * @param user
	 */
	public static void setSysUserLoginSession(HttpSession session,User user){
		session.setAttribute(SYS_USER_LOGIN_SESSION,user);
	}

	/**
	 * 获取用户登录的会话
	 * @param session
	 * @return
	 */
	public static User getSysUserLoginSession(HttpSession session){
		Object object=session.getAttribute(SYS_USER_LOGIN_SESSION);
		if(object==null)
		{
			return null;
		}
		return (User)object;
	}

	/**
	 * 移除用户的登录会话
	 * @param session
	 */
	public static void removeSysUserLoginSession(HttpSession session){
		session.removeAttribute(SYS_USER_LOGIN_SESSION);
	}

	/**
	 * 设置微信转账url
	 * @param session
	 * @param targetUrl
	 */
	public static void setPersonalWechatTransferUrlSession(HttpSession session,String targetUrl){
		session.setAttribute(PERSONAL_WECHAT_TRANSFER_URL_SESSION,targetUrl);
	}

	/**
	 * 获取微信转账url
	 * @param session
	 * @return
	 */
	public static String getPersonalWechatTransferUrlSession(HttpSession session){
		Object object=session.getAttribute(PERSONAL_WECHAT_TRANSFER_URL_SESSION);
		if(object==null){
			return null;
		}
		session.removeAttribute(PERSONAL_WECHAT_TRANSFER_URL_SESSION);
		return (String)object;
	}
}
