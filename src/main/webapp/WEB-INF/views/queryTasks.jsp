<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="queryTasks" method="post">
	<h4 class="form-signin-heading">请填写查询条件（支持模糊查询）：</h4>
	<input id="taskName" name="taskName" type="text" class="form-control" placeholder="数据挖掘任务名称" value="${userName }" autofocus>
	<button class="btn btn-lg btn-primary btn-block" type="submit">查询</button><br />
	<c:if test="${!empty errMsg }"><span class="red">${errMsg }</span></c:if>
</form>

