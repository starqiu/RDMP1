<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% response.setContentType("text/html;charset=UTF-8"); %>
<% request.setCharacterEncoding("UTF-8"); %>

<form class="form-signin" role="form" action="autoDocument" method="post">
	<h4 class="form-signin-heading">使用朴素贝叶斯算法将文章自动分类</h4>
	<textarea id="article" cols="300" name="article" type="text" class="form-control" placeholder="请输入文章内容" required autofocus ></textarea><br />
	<button class="btn btn-lg btn-primary btn-block" type="submit">自动分类</button>
</form>