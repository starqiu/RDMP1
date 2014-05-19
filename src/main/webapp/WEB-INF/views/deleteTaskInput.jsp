<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="deleteTaskInput" method="post">
	<h3 class="form-signin-heading">请输入数据挖掘任务名称：</h3>
	<input id="taskName" name="taskName" type="text" class="form-control" placeholder="数据挖掘任务名称"  required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">确定</button>
</form>