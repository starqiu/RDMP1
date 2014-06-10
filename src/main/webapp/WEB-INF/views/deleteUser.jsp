<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="deleteUser" method="post">
	<h4 class="form-signin-heading">即将删除的用户信息：</h4>
	<input id="userName" name="userName" type="text" class="form-control" placeholder="用户名" value="${user.userName }" readonly="readonly" required >
	<input id="email" name="email" type="text" class="form-control" placeholder="邮箱" value="${user.email }" required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">删除</button>
</form>