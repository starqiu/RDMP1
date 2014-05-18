/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * UserService.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ustc.sse.model.User;



/**
 * 实现功能： 用户服务类，为controller提供服务
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-4	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Service
public interface UserService {
	/**
	 * 查询所有用户
	 * @return
	 */
	List<User> selectAllUsers();
	/**
	 * 根据用户名查询用户信息
	 * @param userName
	 * @return
	 */
	User selectUserByName(User user);
	/**
	 * 根据用户名得到用户列表
	 * @param userName
	 * @return
	 */
	List<User> selectUsers(User user);
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	int addUser(User user);
	/**
	 * 删除用户
	 * @param userName
	 * @return
	 */
	int deleteUser(User user);
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	int updateUser(User user);
}
