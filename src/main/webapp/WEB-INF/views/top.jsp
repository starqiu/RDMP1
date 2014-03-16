<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">RDMP</a>
		</div>
		<div class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="#">首页</a></li>
				<li><a href="about.do">关于</a></li>
				<li><a href="contact.do">联系我们</a></li>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown">功能导航 <b class="caret"></b> </a>
					<ul class="dropdown-menu">
						<li><a href="#">分类</a></li>
						<li><a href="#">聚类</a></li>
						<li><a href="#">预测</a></li>
						<li class="divider"></li>
						<li class="dropdown-header">其它</li>
						<li><a href="#">时间序列</a></li>
					</ul>
				</li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="active"><a href="login.do">登录</a></li>
				<li><a href="register.do">注册</a></li>
			</ul>
		</div>
	</div>
</div>
