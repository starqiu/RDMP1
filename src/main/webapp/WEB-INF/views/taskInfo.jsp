<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${null != addTask }">
		<form class="form-signin" role="form" action="dataMining" method="post">
			<h2 class="form-signin-heading">数据挖掘任务信息：</h2>
			<span class="form-control">数据挖掘任务ID：${task.id }</span> <br />
			<span class="form-control">数据挖掘任务名称：${task.taskName }</span> <br /> 
			<span class="form-control">数据挖掘任务表名：${task.tableName }</span> <br /> 
			<span class="form-control">数据挖掘任务数据量：${task.maxNum }</span> <br /> 
			<span class="form-control">最近更新日期： <fmt:formatDate value="${task.creatDate }" pattern="yyyy年MM月dd日" />
			</span><br />
			<input id="taskName" name="taskName" type="hidden" class="form-control" placeholder="用户名" value="${task.taskName }" >
			<button class="btn btn-lg btn-primary btn-block" type="submit">点此进行数据挖掘</button>
		</form>	
	</c:when>
	<c:otherwise>
		<form class="form-signin" role="form" action="taskDetail" method="post">
			<h2 class="form-signin-heading">数据挖掘任务信息：</h2>
			<span class="form-control">数据挖掘任务ID：${task.id }</span> <br />
			<span class="form-control">数据挖掘任务名称：${task.taskName }</span> <br /> 
			<span class="form-control">数据挖掘任务表名：${task.tableName }</span> <br /> 
			<span class="form-control">数据挖掘任务数据量：${task.maxNum }</span> <br /> 
			<span class="form-control">最近更新日期： <fmt:formatDate value="${task.creatDate }" pattern="yyyy年MM月dd日" />
			</span><br />
			<input id="taskName" name="taskName" type="hidden" class="form-control" placeholder="用户名" value="${task.taskName }" >
			<button class="btn btn-lg btn-primary btn-block" type="submit">查看任务详情</button>
		</form>
	</c:otherwise>
</c:choose>

