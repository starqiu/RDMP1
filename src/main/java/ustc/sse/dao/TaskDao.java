/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * TaskDao.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.dao;

import java.util.List;

import ustc.sse.model.Task;


/**
 * 实现功能：对数据挖掘任务的增删改查
* <p>
 * date	        author            email		           notes<br />
 * ----------------------------------------------------------------<br />
 *2014-2-19      邱星       starqiu@mail.ustc.edu.cn	    新建类<br /></p>
 *
 * 
 */
public interface TaskDao {
	
	/**
	 * 查询所有数据挖掘任务
	 * @return
	 */
	List<Task> selectAllTasks();
	/**
	 * 根据数据挖掘任务名查询数据挖掘任务信息
	 * @param taskName
	 * @return
	 */
	Task selectTaskByName(Task task);
	/**
	 * 根据数据挖掘任务名得到数据挖掘任务列表
	 * @param taskName
	 * @return
	 */
	List<Task> selectTasks(Task task);
	/**
	 * 新增数据挖掘任务
	 * @param task
	 * @return
	 */
	int addTask(Task task);
	/**
	 * 删除数据挖掘任务
	 * @param taskName
	 * @return
	 */
	int deleteTask(Task task);
	/**
	 * 更新数据挖掘任务信息
	 * @param task
	 * @return
	 */
	int updateTask(Task task);
}
