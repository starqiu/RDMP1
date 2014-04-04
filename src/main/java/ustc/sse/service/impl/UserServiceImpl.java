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
 * 实现功能： 
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
import ustc.sse.jdbc.User;
import ustc.sse.service.UserService;

/**
 * service层我用的注解的方式配的，所以没写在Spring配置文件里
 * @author 百木森森
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	public List<User> findAll() {
		return this.userDao.findAll();
	}

	public void modify(User user) {
		this.userDao.modify(user);
	}

}

