/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * TaskDaoImpl.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import ustc.sse.dao.TaskDao;
import ustc.sse.model.Task;

/**
 * 实现功能： TaskDao的实现类，与数据库通信，对数据挖掘任务的CRUD
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
public class TaskDaoImpl extends SqlSessionDaoSupport implements TaskDao {

	@Override
	public List<Task> selectAllTasks() {
		return this.getSqlSession().selectList("task.selectAllTasks");
	}

	@Override
	public Task selectTaskByName(Task task) {
		return this.getSqlSession().selectOne("task.selectTaskByName",task);
	}

	@Override
	public List<Task> selectTasks(Task task) {
		return this.getSqlSession().selectList("task.selectTasks",task);
	}

	@Override
	public int addTask(Task task) {
		return this.getSqlSession().insert("task.addTask", task);
	}

	@Override
	public int deleteTask(Task task) {
		return this.getSqlSession().delete("task.deleteTask", task);
	}

	@Override
	public int updateTask(Task task) {
		return this.getSqlSession().update("task.updateTask", task);
	}

}
