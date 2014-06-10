<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-signin" role="form" action="kMeans" method="post">
	<h3 class="form-signin-heading">K-Means聚类分析</h3>
	<input id="k" name="k" type="text" class="form-control" placeholder="聚类簇的个数" required autofocus>
	<input id="numOfNode" name="numOfNode" type="text" class="form-control" placeholder="结点个数" required >
	<button class="btn btn-lg btn-primary btn-block" type="submit">聚类分析</button>
</form>