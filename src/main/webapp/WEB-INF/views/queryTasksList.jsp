<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<table class="table table-hover" contenteditable="true">
	<thead>
		<tr class="error">
			<th>数据挖掘任务名称</th>
			<th>表名</th>
			<th>最大数据条数</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${tasks }"  varStatus="s">
			<c:choose>
				<c:when test="${s.index%2 ==1 }">
					<tr class="success">
						<td>${tasks[s.index].taskName }</td>
						<td>${tasks[s.index].tableName }</td>
						<td>${tasks[s.index].maxNum }</td>
						<td><fmt:formatDate value="${tasks[s.index].creatDate }" pattern="yyyy年MM月dd日"/></td>
						<td>
							<a href="updateTaskInput?taskName=${tasks[s.index].taskName }" >修改</a>&nbsp;&nbsp;
							<a href="deleteTaskInput?taskName=${tasks[s.index].taskName }" >删除</a>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr class="info">
						<td>${tasks[s.index].taskName }</td>
						<td>${tasks[s.index].tableName }</td>
						<td>${tasks[s.index].maxNum }</td>
						<td><fmt:formatDate value="${tasks[s.index].creatDate }" pattern="yyyy年MM月dd日"/></td>
						<td>
							<a href="updateTaskInput?taskName=${tasks[s.index].taskName }" >修改</a>&nbsp;&nbsp;
							<a href="deleteTaskInput?taskName=${tasks[s.index].taskName }" >删除</a>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</tbody>
</table>