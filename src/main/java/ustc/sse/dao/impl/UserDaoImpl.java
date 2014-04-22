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
 * 实现功能： 
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
 * @author 百木森森
 * 
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

	@Override
	public List<User> selectAllUsers() {
		return this.getSqlSession().selectList("user.selectAllUsers");
	}

	@Override
	public User selectUserByName(String userName) {
		return this.getSqlSession().selectOne("user.selectUserByName",userName);
	}

}
