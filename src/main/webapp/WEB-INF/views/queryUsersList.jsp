<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<table class="table table-hover" >
	<thead>
		<tr class="error">
			<th>用户名</th>
			<th>邮箱</th>
			<th>注册时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${users }"  varStatus="s">
			<c:choose>
				<c:when test="${s.index%2 ==1 }">
					<tr class="success">
						<td>${users[s.index].userName }</td>
						<td>${users[s.index].email }</td>
						<td><fmt:formatDate value="${users[s.index].regDate }" pattern="yyyy年MM月dd日"/></td>
						<td>
							<a href="updateUserInput?userName=${users[s.index].userName }" >修改</a>&nbsp;&nbsp;
							<a href="deleteUserInput?userName=${users[s.index].userName }" >删除</a>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr class="info">
						<td>${users[s.index].userName }</td>
						<td>${users[s.index].email }</td>
						<td><fmt:formatDate value="${users[s.index].regDate }" pattern="yyyy年MM月dd日"/></td>
						<td>
							<a href="updateUserInput?userName=${users[s.index].userName }" >修改</a>&nbsp;&nbsp;
							<a href="deleteUserInput?userName=${users[s.index].userName }" >删除</a>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</tbody>
</table>