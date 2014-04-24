<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="updateUser" method="post">
	<h2 class="form-signin-heading">请修改用户信息：</h2>
	<input id="userName" name="userName" type="text" class="form-control" placeholder="用户名" value="${user.userName }" readonly="readonly" required >
  	<input id="password" name="password" type="password" class="form-control" placeholder="密码" required autofocus>
  	<input id="confirmPassword" name="confirmPassword" type="password" class="form-control" placeholder="确认密码" required>
	<input id="email" name="email" type="text" class="form-control" placeholder="邮箱" value="${user.email }" required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">修改</button>
</form>