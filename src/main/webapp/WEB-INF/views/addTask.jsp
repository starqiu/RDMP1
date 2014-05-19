<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="addTask" method="post">
	<h3 class="form-signin-heading">请填写数据挖掘任务信息：</h3>
	<input id="taskName" name="taskName" type="text" class="form-control" placeholder="任务名称" required autofocus>
	<input id="tableName" name="tableName" type="text" class="form-control" placeholder="表名" required >
  	<input id="maxNum" name="maxNum" type="text" class="form-control" placeholder="最大数据条数" required>
	<button class="btn btn-lg btn-primary btn-block" type="submit">新增任务</button>
</form>