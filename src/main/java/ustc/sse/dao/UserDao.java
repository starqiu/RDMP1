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
 * 实现功能：
 * <p>
 * date author email notes<br />
 * -------- --------------------------- ---------------<br />
 * 2014-4-4 邱星 starqiu@mail.ustc.edu.cn 新建类<br />
 * </p>
 * 
 */
public interface UserDao {
	
	List<User> selectAllUsers();
	User selectUserByName(String userName);
}
