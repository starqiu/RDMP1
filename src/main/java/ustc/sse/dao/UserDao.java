/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * UserDao.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.dao;

import java.util.List;

import ustc.sse.model.User;


/**
 * 实现功能：对用户的增删改查
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2014-4-4 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 * 
 */
public interface UserDao {
	
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
	User selectUserByName(String userName);
	/**
	 * 根据用户名得到用户列表
	 * @param userName
	 * @return
	 */
	List<User> selectUsers(String userName);
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
	int deleteUser(String userName);
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	int updateUser(User user);
}
