/*
 * ============================================================
 * The SSE USTC Software License
 * 
 * Task.java
 * 2014-5-17
 * 
 * Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd        
 * All rights reserved.
 * ============================================================
 */
package ustc.sse.model;

import java.util.Date;

/**
 * 实现功能： 数据挖掘任务类
 * <p>
 * date	    author            email		           notes<br />
 * --------	---------------------------	---------------<br />
 *2014-3-17	 邱星            starqiu@mail.ustc.edu.cn	      新建类<br /></p>
 *
 */
/**
 * @author 邱星
 */
public class Task {
	/** 任务ID*/
	private int id ;
	/** 任务名称*/
	private String taskName ;
	/** 表名*/
	private String tableName ;
	/** 最大数据条数*/
	private int maxNum ;
	/** 注册日期*/
	private Date creatDate ;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public Date getCreatDate() {
		return creatDate;
	}
	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	
}

