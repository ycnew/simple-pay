package cn._42pay.simplepay.service.mobile;

import cn._42pay.simplepay.db.entity.User;

/**
 * Created by kevin on 2018/6/26.
 */
public interface UserService {
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	User getUserById(Long userId);
}
