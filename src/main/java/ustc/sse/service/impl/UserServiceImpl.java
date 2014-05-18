/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * UserServiceImpl.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.service.impl;

/**
 * 实现功能： userService的实现类，为controller提供具体服务
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-4	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ustc.sse.dao.UserDao;
import ustc.sse.model.User;
import ustc.sse.service.UserService;

/**
 * service层我用的注解的方式配的，所以没写在Spring配置文件里
 * 
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	@Override
	public List<User> selectAllUsers() {
		return this.getUserDao().selectAllUsers();
	}

	@Override
	public User selectUserByName(User user) {
		return this.getUserDao().selectUserByName(user);
	}

	@Override
	public List<User> selectUsers(User user) {
		return this.getUserDao().selectUsers(user);
	}

	@Override
	public int addUser(User user) {
		return this.getUserDao().addUser(user);
	}

	@Override
	public int deleteUser(User user) {
		return this.getUserDao().deleteUser(user);
	}

	@Override
	public int updateUser(User user) {
		return this.getUserDao().updateUser(user);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
}

