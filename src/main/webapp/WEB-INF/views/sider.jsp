<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>首页-集成R语言的数据挖掘平台</title>
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
	<div class="main-container" id="main-container">
		<script type="text/javascript">
			try {
				ace.settings.check('main-container', 'fixed')
			} catch (e) {
			}
		</script>

		<div class="main-container-inner">
			<a class="menu-toggler" id="menu-toggler" href="#"> <span
				class="menu-text"></span> </a>

			<div class="sidebar" id="sidebar">
				<script type="text/javascript">
					try {
						ace.settings.check('sidebar', 'fixed')
					} catch (e) {
					}
				</script>

				<ul class="nav nav-list">
					<li class="active"><a href="index.html"> <i
							class="icon-dashboard"></i> <span class="menu-text"> 控制台 </span>
					</a></li>

					<li><a href="#" class="dropdown-toggle"> <i
							class="icon-desktop"></i> <span class="menu-text"> UI 组件 </span>
							<b class="arrow icon-angle-down"></b> </a>

						<ul class="submenu">
							<li><a href="elements.html"> <i
									class="icon-double-angle-right"></i> 组件 </a>
							</li>

							<li><a href="buttons.html"> <i
									class="icon-double-angle-right"></i> 按钮 &amp; 图表 </a>
							</li>

							<li><a href="jquery-ui.html"> <i
									class="icon-double-angle-right"></i> jQuery UI </a>
							</li>

							<li><a href="nestable-list.html"> <i
									class="icon-double-angle-right"></i> 可拖拽列表 </a>
							</li>

							<li><a href="#" class="dropdown-toggle"> <i
									class="icon-double-angle-right"></i> 三级菜单 <b
									class="arrow icon-angle-down"></b> </a>

								<ul class="submenu">
									<li><a href="#"> <i class="icon-leaf"></i> 第一级 </a>
									</li>

									<li><a href="#" class="dropdown-toggle"> <i
											class="icon-pencil"></i> 第四级 <b class="arrow icon-angle-down"></b>
									</a>

										<ul class="submenu">
											<li><a href="#"> <i class="icon-plus"></i> 添加产品 </a>
											</li>

											<li><a href="#"> <i class="icon-eye-open"></i> 查看商品
											</a></li>
										</ul>
									</li>
								</ul>
							</li>
						</ul>
					</li>

					<li><a href="#" class="dropdown-toggle"> <i
							class="icon-edit"></i> <span class="menu-text"> 表单 </span> <b
							class="arrow icon-angle-down"></b> </a>

						<ul class="submenu">
							<li><a href="form-elements.html"> <i
									class="icon-double-angle-right"></i> 表单组件 </a>
							</li>

							<li><a href="form-wizard.html"> <i
									class="icon-double-angle-right"></i> 向导提示 &amp; 验证 </a>
							</li>

							<li><a href="wysiwyg.html"> <i
									class="icon-double-angle-right"></i> 编辑器 </a>
							</li>

							<li><a href="dropzone.html"> <i
									class="icon-double-angle-right"></i> 文件上传 </a>
							</li>
						</ul>
					</li>

					<li><a href="calendar.html"> <i class="icon-calendar"></i>

							<span class="menu-text"> 日历 <span
								class="badge badge-transparent tooltip-error"
								title="2&nbsp;Important&nbsp;Events"> <i
									class="icon-warning-sign red bigger-130"></i> </span> </span> </a>
					</li>

					<li><a href="#" class="dropdown-toggle"> <i
							class="icon-tag"></i> <span class="menu-text"> 更多页面 </span> <b
							class="arrow icon-angle-down"></b> </a>

						<ul class="submenu">
							<li><a href="profile.html"> <i
									class="icon-double-angle-right"></i> 用户信息 </a>
							</li>

							<li><a href="login.html"> <i
									class="icon-double-angle-right"></i> 登录 &amp; 注册 </a>
							</li>
						</ul>
					</li>

					<li><a href="#" class="dropdown-toggle"> <i
							class="icon-file-alt"></i> <span class="menu-text"> 其他页面 <span
								class="badge badge-primary ">5</span> </span> <b
							class="arrow icon-angle-down"></b> </a>

						<ul class="submenu">
							<li><a href="faq.html"> <i
									class="icon-double-angle-right"></i> 帮助 </a>
							</li>

							<li><a href="error-404.html"> <i
									class="icon-double-angle-right"></i> 404错误页面 </a>
							</li>

							<li><a href="blank.html"> <i
									class="icon-double-angle-right"></i> 空白页面 </a>
							</li>
						</ul>
					</li>
				</ul>

				<div class="sidebar-collapse" id="sidebar-collapse">
					<i class="icon-double-angle-left"
						data-icon1="icon-double-angle-left"
						data-icon2="icon-double-angle-right"></i>
				</div>

				<script type="text/javascript">
					try {
						ace.settings.check('sidebar', 'collapsed')
					} catch (e) {
					}
				</script>
			</div>
		</div>
	</div>
</body>
</html>