package cn._42pay.simplepay.service.mobile.impl;

import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.service.AbstractService;
import cn._42pay.simplepay.service.mobile.UserService;
import org.springframework.stereotype.Service;

/**
 * 获取用户信息
 * Created by kevin on 2018/6/26.
 */
@Service
public class UserServiceImpl extends AbstractService implements UserService {

	@Override
	public User getUserById(Long userId) {
		return userDao.selectById(userId.toString());
	}
}
