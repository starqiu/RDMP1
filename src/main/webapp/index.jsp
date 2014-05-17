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
	<tiles:insertTemplate template="/WEB-INF/views/header.jsp" />
</head>
<body>
	<!-- top -->
	<tiles:insertTemplate template="/WEB-INF/views/top.jsp" />
	<!-- Main -->
	<div class="container">
		<div class="row">
			<div class="col-md-2">
				<tiles:insertTemplate template="/WEB-INF/views/sider.jsp" />
			</div>
			<div class="col-md-10">
			  	<tiles:insertTemplate template="/WEB-INF/views/index.jsp" />
			</div>
		</div>
	</div>
	
	<tiles:insertTemplate template="/WEB-INF/views/footer.jsp" />
</body>
</html>