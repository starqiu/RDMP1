<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page isELIgnored="false" %> 
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<tiles:insertAttribute name="header" />
</head>
<body>
	<!-- top -->
	<tiles:insertAttribute name="top" />
	<!-- Main -->
	<div class="container">
		<div class="row">
			<div class="col-md-2">
				<tiles:insertAttribute name="sider" />
			</div>
			<div class="col-md-10">
			  	<tiles:insertAttribute name="content" />
			</div>
		</div>
	</div>
	
	<tiles:insertAttribute name="footer" />
</body>
</html>