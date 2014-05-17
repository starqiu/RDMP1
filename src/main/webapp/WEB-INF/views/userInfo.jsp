<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="form-signin">
	<h2 class="form-signin-heading">用户信息：</h2>
	<span class="form-control">用户名：${user.userName }</span> <br />
	<span class="form-control">邮箱：${user.email }</span> <br />
	<span class="form-control">注册日期： <fmt:formatDate value="${user.regDate }" pattern="yyyy年MM月dd日"/></span> <br />
</div>