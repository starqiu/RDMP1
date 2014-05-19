<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form class="form-signin" role="form" action="deleteTask" method="post">
	<h4 class="form-signin-heading">即将删除的数据挖掘任务信息：</h4>
	<input id="taskName" name="taskName" type="hidden" class="form-control" placeholder="数据挖掘任务名称" value="${task.taskName }" readonly="readonly" required >
	<span class="form-control">数据挖掘任务名称：${task.taskName }</span> <br />
	<span class="form-control">表名：${task.tableName }</span> <br />
	<span class="form-control">最大数据量：${task.maxNum }</span> <br />
	<span class="form-control">最近更新日期： <fmt:formatDate value="${task.creatDate }" pattern="yyyy年MM月dd日"/></span> <br />
	<button class="btn btn-lg btn-primary btn-block" type="submit">删除</button>
</form>