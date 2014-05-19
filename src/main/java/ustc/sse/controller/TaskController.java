/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * TaskController.java
 * 2014-4-22
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ustc.sse.model.Task;
import ustc.sse.service.TaskService;

/**
 * 实现功能： 对数据挖掘任务的增删改查
 * <p>
 * date	        author            email		           notes<br />
 * ----------------------------------------------------------------<br />
 *2014-2-19      邱星       starqiu@mail.ustc.edu.cn	    新建类<br /></p>
 *
 */
@Controller
public class TaskController {
	private final static Log log = LogFactory.getLog(TaskController.class);
	@Resource
	private TaskService taskService;
	private String taskName;

	public TaskController() {
		super();
	}

	/**
	 * 显示数据挖掘任务资料
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("taskInfo")
	public String taskInfo(HttpServletRequest request,
			HttpServletResponse response) {
		taskName = request.getParameter("taskName");
		//int  id =Integer.valueOf(request.getParameter("id")).intValue() ;
		if (null != taskName) {
			Task param = new Task();
			param.setTaskName(taskName);
			Task task = this.getTaskService().selectTaskByName(param);
			if (null != task) {
				request.setAttribute("task", task);
			} else {
				return "index";
			}
		} else {
			return "index";
		}
		return "taskInfo";
	}

	/**
	 * 跳转到更新数据挖掘任务-数据挖掘任务名输入页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateTaskLink")
	public String updateTaskByAdminLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "updateTaskInput";
	}
	
	/**
	 * 跳转到更新数据挖掘任务-数据挖掘任务信息反显页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateTaskInput")
	public String updateTaskInput(HttpServletRequest request,
			HttpServletResponse response) {
		taskInfo(request, response);
		return "updateTask";
	}

	/**
	 * 更新数据挖掘任务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("updateTask")
	public String updateTask(HttpServletRequest request,
			HttpServletResponse response) {
		taskName = request.getParameter("taskName");
		int  id =Integer.valueOf(request.getParameter("id")).intValue() ;
		String tableName = request.getParameter("tableName");
		int maxNum = Integer.valueOf(request.getParameter("maxNum")).intValue();
		Task task = new Task();
		task.setTaskName(taskName);
		task.setTableName(tableName);
		task.setCreatDate(new Date());
		task.setId(id);
		task.setMaxNum(maxNum);
		int result = this.getTaskService().updateTask(task);
		if (1 == result) {
			log.info("更新数据挖掘任务成功！");
		} else {
			log.error("更新数据挖掘任务失败！");
			return "operateFailed";
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到新增数据挖掘任务页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addTaskLink")
	public String addTaskLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "addTask";
	}

	/**
	 * 新增数据挖掘任务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("addTask")
	public String addTask(HttpServletRequest request,
			HttpServletResponse response) {
		taskName = request.getParameter("taskName");
		String tableName = request.getParameter("tableName");
		int maxNum = (Integer.valueOf(request.getParameter("maxNum"))).intValue();
		Task task = new Task();
		task.setTaskName(taskName);
		task.setTableName(tableName);
		task.setMaxNum(maxNum);
		task.setCreatDate(new Date());
		
		int result = this.getTaskService().addTask(task);
		if (1 == result) {
			log.info("add task success!");
		} else {
			log.info("add task failed!");
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到删除数据挖掘任务-输入页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteTaskLink")
	public String deleteTaskLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "deleteTaskInput";
	}
	
	/**
	 * 跳转到删除数据挖掘任务-反显信息页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteTaskInput")
	public String deleteTaskInput(HttpServletRequest request,
			HttpServletResponse response) {
		taskInfo(request, response);
		return "deleteTask";
	}

	/**
	 * 跳转到更新数据挖掘任务页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("deleteTask")
	public String deleteTask(HttpServletRequest request,
			HttpServletResponse response) {
		taskName = request.getParameter("taskName");
		Task task = new Task();
		task.setTaskName(taskName);
		int result = this.getTaskService().deleteTask(task);
		if (1 == result) {
			log.info("delete task success！");
		} else {
			log.error("delete task failed！");
			return "operateFailed";
		}
		return "operateSuccess";
	}

	/**
	 * 跳转到查询数据挖掘任务页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryTasksLink")
	public String queryTaskLink(HttpServletRequest request,
			HttpServletResponse response) {
		return "queryTasks";
	}

	/**
	 * 查询数据挖掘任务
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("queryTasks")
	public String queryTasks(HttpServletRequest request,
			HttpServletResponse response) {
		taskName = request.getParameter("taskName");
		Task param = new Task();
		param.setTaskName(taskName);
		List<Task> tasks= this.getTaskService().selectTasks(param);
		if (null != tasks && tasks.size() != 0) {
			request.setAttribute("tasks", tasks);
			return "queryTasksList";
		} else {
			request.setAttribute("errMsg", "未查询到满足条件的数据挖掘任务，请更改查询条件后再试！");
			return "queryTasks";
		}
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
