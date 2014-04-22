<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<script type='text/javascript'>
	$(document).ready(function() {
		$('[data-toggle=collapse]').click(function() {
							// toggle icon
							$(this).find("i").toggleClass("glyphicon-chevron-right glyphicon-chevron-down");
						});
	});
</script>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<link href="<c:url value='/resources/bootstrap.css' />" rel="stylesheet">
<link href="<c:url value='/resources/custom.css' />" type="text/css" rel="stylesheet">
<script src="<c:url value='/resources/jquery-1.11.0.min.js' />"></script>
<script src="<c:url value='/resources/bootstrap.min.js' />"></script>
</head>
<body>
	<!-- Header -->
	<div id="top-nav" class="navbar navbar-inverse navbar-static-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="icon-toggle"></span>
				</button>
				<ul class="nav navbar-nav">
					  <li><span class="navbar-brand">大数据挖掘平台</span></li>
					  <li class="active"><a href="index">首页</a></li>
					  <li><a href="about">关于</a></li>
					  <li><a href="contact">联系我们</a></li>
					  <li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown">更多<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="help">帮助</a></li>
						</ul>
				      </li>
			 	 </ul>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li class="active"><a href="login.do">登录</a></li>
			          <li><a href="register.do">注册</a></li>
				      <li class="light-blue"><a data-toggle="dropdown" href="#"
										class="dropdown-toggle"> <span class="glyphicon glyphicon-user">
												StarQiu!</span> <i class="icon-caret-down"></i> </a>
							<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
								<li><a href="#"> <i class="glyphicon glyphicon-cog"></i> 设置 </a></li>
								<li><a href="#"> <i class="glyphicon glyphicon-user"></i> 个人资料 </a></li>
								<li class="divider"></li>
								<li><a href="#"> <i class="glyphicon glyphicon-off"></i> 退出 </a></li>
							</ul>
					</li>
				</ul>
			</div>
		</div>
		<!-- /container -->
	</div>
	<!-- Main -->
	<div class="container">
		<div class="row">
			<div class="col-md-3">
				<!-- Left column -->
				<ul class="list-unstyled">
					<li class="nav-header"><a href="#" data-toggle="collapse"
						data-target="#userMenu">
						<h5>用户管理 <i class="glyphicon glyphicon-chevron-right"></i></h5> </a>
						<ul class="list-unstyled collapse in" id="userMenu">
							<li><a href="user/add"><i class="glyphicon glyphicon-plus"></i> 
									新增用户</a></li>
							<li><a href="user/query"><i class="glyphicon glyphicon-search"></i>
									查询用户</a></li>
							<li><a href="user/update"><i class="glyphicon glyphicon-edit"></i>
									修改用户</a></li>
							<li><a href="user/delete"><i class="glyphicon glyphicon-trash"></i>
									删除用户</a></li>
						</ul>
					</li>
					<li class="nav-header"><a href="#" data-toggle="collapse"
						data-target="#menu2">
						<h5>数据挖掘任务管理 <i class="glyphicon glyphicon-chevron-right"></i></h5> </a>
						<ul class="list-unstyled collapse in" id="menu2">
							<li><a href="task/add"><i class="glyphicon glyphicon-plus"></i> 
									新增任务</a></li>
							<li><a href="task/query"><i class="glyphicon glyphicon-search"></i>
									查询任务</a></li>
							<li><a href="task/update"><i class="glyphicon glyphicon-edit"></i>
									修改任务</a></li>
							<li><a href="task/delete"><i class="glyphicon glyphicon-trash"></i>
									删除任务</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<!-- /col-3 -->
			<div class="col-md-9">
			  <form class="form-signin" role="form" action="welcome.do" method="post">
		        <h2 class="form-signin-heading">请登录</h2>
		        <input id="userName" name="userName" type="text" class="form-control" placeholder="邮箱" required autofocus>
		        <input type="password" class="form-control" placeholder="密码" required>
		        <label class="checkbox">
		          <input type="checkbox" value="remember-me"> 记住我
		        </label>
		        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		      </form>
			</div>
			<!--/col-span-9-->
		</div>
	</div>
	<!-- /Main -->
	<footer class="text-center">
		中国科学技术大学版权所有. Copyright (c) 2006 China Payment and Remittance Service Co.,Ltd. All rights reserved.
	</footer>
</body>
</html>