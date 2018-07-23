package cn._42pay.simplepay.service.sys;

import cn._42pay.simplepay.db.entity.User;
import cn._42pay.simplepay.framework.exception.SimplePayException;
import cn._42pay.simplepay.vo.sys.UserAddRequestVo;

/**
 * Created by kevin on 2018/7/16.
 */
public interface SysUserService {

	/**
	 * 判断用户是否存在
	 * @param loginName
	 * @return
	 * @throws SimplePayException
	 */
	boolean checkUserExist(String loginName) throws SimplePayException;

	/**
	 * 新增用户
	 * @param userAddRequestVo
	 */
	void addUser(UserAddRequestVo userAddRequestVo) throws SimplePayException;

	/**
	 * 验证用户名和密码是否正确
	 * @param loginName
	 * @param password
	 * @return
	 */
	User checkUserLogin(String loginName,String password) throws SimplePayException;

	/**
	 * 修改用户密码
	 * @param userId
	 * @param password
	 * @throws SimplePayException
	 */
	void modifyUserPassword(Long userId,String password) throws SimplePayException;

	/**
	 * 根据Id查找用户信息
	 * @param userId
	 * @return
	 */
	User getUserById(Long userId);

	/**
	 * 根据用户Id修改用户信息
	 * @param userId
	 * @param email
	 * @param mobile
	 * @return
	 * @throws SimplePayException
	 */
	void modifyUserInfoById(Long userId,String email,String mobile) throws SimplePayException;
}
