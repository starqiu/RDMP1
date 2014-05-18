/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * UserDaoImpl.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import ustc.sse.dao.UserDao;
import ustc.sse.model.User;

/**
 * 实现功能： UserDao的实现类，与数据库通信，对用户的CRUD
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-4	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
/**
 * 这里要注意，我继承了一个HibernateDaoSupport类，然后在Spring的配置文件中给这个类注入了一个
 * sessionFactory。这是为了能得到Hibernate的Session对象
 * 
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

	@Override
	public List<User> selectAllUsers() {
		return this.getSqlSession().selectList("user.selectAllUsers");
	}

	@Override
	public User selectUserByName(User user) {
		return this.getSqlSession().selectOne("user.selectUserByName",user);
	}

	@Override
	public List<User> selectUsers(User user) {
		return this.getSqlSession().selectList("user.selectUsers",user);
	}

	@Override
	public int addUser(User user) {
		return this.getSqlSession().insert("user.addUser", user);
	}

	@Override
	public int deleteUser(User user) {
		return this.getSqlSession().delete("user.deleteUser", user);
	}

	@Override
	public int updateUser(User user) {
		return this.getSqlSession().update("user.updateUser", user);
	}

}
