<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>集成R语言的数据挖掘平台</title>
	<link href="<c:url value="/resources/css/bootstrap.css" />"
		rel="stylesheet">
	<link rel="stylesheet"
		href="<c:url value="/resources/css/font-awesome.min.css"  /> " />
	<link rel="stylesheet"
		href="<c:url value="/resources/css/fullcalendar.css" /> " />
	<link rel="stylesheet"
		href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />
	<link rel="stylesheet"
		href="<c:url value="/resources/css/ace.min.css" /> " />
	<link rel="stylesheet"
		href="<c:url value="/resources/css/ace-rtl.min.css" /> " />
	<link rel="stylesheet"
		href="<c:url value="/resources/css/ace-skins.min.css" /> " />
	<script src="<c:url value="/resources/js/ace-extra.min.js" />"></script>
</head>
<body>
	<div class="navbar navbar-default" id="navbar">
		<script type="text/javascript">
			try {
				ace.settings.check('navbar', 'fixed')
			} catch (e) {
			}
		</script>

		<div class="navbar-container" id="navbar-container">
			<div class="navbar-header pull-left">
				<a href="#" class="navbar-brand"> <small>ACE后台管理系统</small> </a>
			</div>
			<div class="navbar-header pull-right" role="navigation">
				<ul class="nav ace-nav">
					<li class="light-blue"><a data-toggle="dropdown" href="#"
						class="dropdown-toggle"> <span class="user-info"> <small>欢迎光临,</small>
								StarQiu!</span> <i class="icon-caret-down"></i> </a>

						<ul
							class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li><a href="#"> <i class="icon-cog"></i> 设置 </a></li>

							<li><a href="#"> <i class="icon-user"></i> 个人资料 </a></li>

							<li class="divider"></li>

							<li><a href="#"> <i class="icon-off"></i> 退出 </a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>