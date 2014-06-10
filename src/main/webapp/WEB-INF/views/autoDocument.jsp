<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="form-signin">
	<h3 class="form-signin-heading">文章自动分类结果为：<span class="red"><c:out value="${classifyName }" /></span></h3>
	<table class="table table-hover" contenteditable="true">
		<thead>
			<tr class="error">
				<th>分类器名称</th>
				<th>可能性</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${classifers }"  varStatus="s">
				<c:choose>
					<c:when test="${s.index%2 ==1 }">
						<tr class="success">
							<td>${classifers[s.index].name }</td>
							<td>${classifers[s.index].probability }</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr class="info">
							<td>${classifers[s.index].name }</td>
							<td>${classifers[s.index].probability }</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tbody>
	</table>
</div>