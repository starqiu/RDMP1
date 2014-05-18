/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * TaskServiceImpl.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.service.impl;

/**
 * 实现功能： taskService的实现类，为controller提供具体服务
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-4	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ustc.sse.dao.TaskDao;
import ustc.sse.model.Task;
import ustc.sse.service.TaskService;

/**
 * service层我用的注解的方式配的，所以没写在Spring配置文件里
 * 
 *
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Resource
	private TaskDao taskDao;

	@Override
	public List<Task> selectAllTasks() {
		return this.getTaskDao().selectAllTasks();
	}

	@Override
	public Task selectTaskByName(Task task) {
		return this.getTaskDao().selectTaskByName(task);
	}

	@Override
	public List<Task> selectTasks(Task task) {
		return this.getTaskDao().selectTasks(task);
	}

	@Override
	public int addTask(Task task) {
		return this.getTaskDao().addTask(task);
	}

	@Override
	public int deleteTask(Task task) {
		return this.getTaskDao().deleteTask(task);
	}

	@Override
	public int updateTask(Task task) {
		return this.getTaskDao().updateTask(task);
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
	
}

