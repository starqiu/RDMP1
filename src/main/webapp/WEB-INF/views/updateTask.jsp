<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form class="form-signin" role="form" action="updateTask" method="post">
	<h3 class="form-signin-heading">请修改数据挖掘任务信息：</h3>
	<input id="id" name="id" type="text" class="form-control" placeholder="数据挖掘任务ID" value="${task.id }" readonly="readonly" required >
	<input id="taskName" name="taskName" type="text" class="form-control" placeholder="数据挖掘任务名称" value="${task.taskName }" required autofocus>
	<input id="tableName" name="tableName" type="text" class="form-control" placeholder="表名" value="${task.tableName }" required >
	<input id="maxNum" name="maxNum" type="text" class="form-control" placeholder="最大数据量" value="${task.maxNum }" required >
	<span class="form-control">最近更新日期： <fmt:formatDate value="${task.creatDate }" pattern="yyyy年MM月dd日"/></span> <br />
	<button class="btn btn-lg btn-primary btn-block" type="submit">修改</button>
</form>