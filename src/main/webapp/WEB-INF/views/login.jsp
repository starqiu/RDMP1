<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="welcome.do" method="post">
	<c:choose>
		<c:when test="${loginFailed =='1'}">
			<h5 class="form-signin-heading red">登录失败,用户名与密码不匹配！请重新登录！</h5>
		</c:when>
		<c:otherwise>
			<c:out value="${loginFailed}"></c:out>
		  	<h2 class="form-signin-heading">请登录</h2>
		</c:otherwise>
	</c:choose>
	<input id="userName" name="userName" type="text" class="form-control" placeholder="邮箱" required autofocus>
  	<input id="password" name="password" type="password" class="form-control" placeholder="密码" required>
  	<label class="checkbox">
    	<input type="checkbox" value="remember-me"> 记住我
  	</label>
	<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
</form>