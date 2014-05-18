/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * TaskService.java
 * 2014-4-4
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ustc.sse.model.Task;



/**
 * 实现功能： 数据挖掘任务服务类，为controller提供服务
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-4-4	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
@Service
public interface TaskService {
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
