<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="table table-hover" contenteditable="true">
	<thead>
		<tr class="error">
			<th>用户名</th>
			<th>邮箱</th>
			<th>注册时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${users }"  varStatus="status">
			<c:choose>
				<c:when test="${status.index%2 ==1 }">
					<tr class="success">
						<td>${users[s.index].userName }</td>
						<td>${users[s.index].email }</td>
						<td><fmt:formatDate value="${users[s.index].regDate }" pattern="yyyy年MM月dd日"/></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr class="info">
						<td>${users[s.index].userName }</td>
						<td>${users[s.index].email }</td>
						<td><fmt:formatDate value="${users[s.index].regDate }" pattern="yyyy年MM月dd日"/></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</tbody>
</table>


<c:forEach  items="${users}"  var="sus" varStatus="s">
${users[s.index].userName }
</c:forEach>