<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="deleteUser" method="post">
	<h2 class="form-signin-heading">请输入用户名：</h2>
	<input id="userName" name="userName" type="text" class="form-control" placeholder="用户名"  required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">确定</button>
</form>