<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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