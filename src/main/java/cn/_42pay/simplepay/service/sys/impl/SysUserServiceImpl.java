package cn._42pay.simplepay.service.sys.impl;

import cn._42pay.simplepay.constant.ErrorCodeEnum;
import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.util.DateUtil;
import cn._42pay.simplepay.framework.util.MD5Util;
import cn._42pay.simplepay.framework.util.RandomUtil;
import cn._42pay.simplepay.framework.util.SnowflakeUtil;
import cn._42pay.simplepay.service.AbstractService;
import cn._42pay.simplepay.service.sys.SysUserService;
import cn._42pay.simplepay.vo.sys.UserAddRequestVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2018/7/16.
 */
@Service
public class SysUserServiceImpl extends AbstractService implements SysUserService {
	@Override
	public boolean checkUserExist(String loginName) throws SimplePayException {
		User where = new User();
		where.setLoginName(loginName);
		long count=userDao.selectCount(where);
		if(count>0){
			return true;
		}
		return false;
	}

	@Override
	public void addUser(UserAddRequestVo userAddRequestVo) throws SimplePayException {
		String salt = RandomUtil.getRandomAllChar(16);
		String md5 = MD5Util.string2MD5(userAddRequestVo.getPassword() + salt);

		try {
			User user = new User();
			user.setUserId(SnowflakeUtil.getInstance().nextId());
			user.setCreateTime(DateUtil.dateToString(new Date()));
			user.setEmail(userAddRequestVo.getEmail());
			user.setLoginName(userAddRequestVo.getLoginName());
			user.setMobile(userAddRequestVo.getMobile());
			user.setPassword(md5);
			user.setSalt(salt);
			user.setUserName(userAddRequestVo.getLoginName());
			userDao.insert(user);
		} catch (Exception ex) {
			Log.e(LogScene.MANAGER, "添加用户异常", ex);
			throw new SimplePayException(ErrorCodeEnum.SYS_USER_ADD_FAIL);
		}
	}

	@Override
	public User checkUserLogin(String loginName, String password) throws SimplePayException {
		User where = new User();
		where.setLoginName(loginName);
		List<User> users = userDao.select(where);
		if (CollectionUtils.isEmpty(users)) {
			throw new SimplePayException(ErrorCodeEnum.SYS_USER_NOT_EXIST);
		}

		User user = users.get(0);
		String checkMd5 = MD5Util.string2MD5(password + user.getSalt());
		if (!checkMd5.equalsIgnoreCase(user.getPassword())) {
			throw new SimplePayException(ErrorCodeEnum.SYS_USER_LOGIN_ERROR);
		}

		return user;
	}

	@Override
	public void modifyUserPassword(Long userId, String password) throws SimplePayException {
		String salt = RandomUtil.getRandomAllChar(16);
		String md5 = MD5Util.string2MD5(password + salt);

		try {
			User user = new User();
			user.setUserId(userId);
			user.setUpdateTime(DateUtil.dateToString(new Date()));
			user.setPassword(md5);
			user.setSalt(salt);
			userDao.update(user);
		} catch (Exception ex) {
			Log.e(LogScene.MANAGER, "修改用户密码异常", ex);
			throw new SimplePayException(ErrorCodeEnum.SYS_USER_MODIFY_PASSWORD_FAIL);
		}
	}

	@Override
	public User getUserById(Long userId) {
		return userDao.selectById(String.valueOf(userId));
	}

	@Override
	public void modifyUserInfoById(Long userId, String email, String mobile) throws SimplePayException {
		try {
			User user = new User();
			user.setUserId(userId);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setUpdateTime(DateUtil.dateToString(new Date()));
			userDao.update(user);
		} catch (Exception ex) {
			Log.e(LogScene.MANAGER, "修改用户信息异常", ex);
			throw new SimplePayException(ErrorCodeEnum.SYS_USER_MODIFY_USERINFO_FAIL);
		}
	}
}
