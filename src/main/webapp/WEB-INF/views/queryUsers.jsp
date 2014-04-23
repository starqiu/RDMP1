<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="user/queryUsers" method="post">
	<h2 class="form-signin-heading">请填写查询条件（支持模糊查询）：</h2>
	<input id="userName" name="userName" type="text" class="form-control" placeholder="用户名" required autofocus>
	<input id="email" name="email" type="text" class="form-control" placeholder="邮箱" required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">查询</button>
</form>

<div style="display:none">
</div>