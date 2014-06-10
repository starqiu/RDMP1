<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<ul class="list-unstyled nav nav-list">
	<li class="nav-header"><a href="#" data-toggle="collapse"
		data-target="#userMenu">
		<h5>用户管理 <i class="glyphicon glyphicon-chevron-right"></i></h5> </a>
		<ul class="list-unstyled collapse in" id="userMenu">
			<li><a href="addUserLink"><i class="glyphicon glyphicon-plus"></i> 
					新增用户</a></li>
			<li><a href="queryUsersLink"><i class="glyphicon glyphicon-search"></i>
					查询用户</a></li>
			<li><a href="updateUserByAdminLink"><i class="glyphicon glyphicon-edit"></i>
					修改用户</a></li>
			<li><a href="deleteUserLink"><i class="glyphicon glyphicon-trash"></i>
					删除用户</a></li>
		</ul>
	</li>
	<li class="nav-header"><a href="#" data-toggle="collapse"
		data-target="#taskMenu">
		<h5>关联规则任务管理 <i class="glyphicon glyphicon-chevron-right"></i></h5> </a>
		<ul class="list-unstyled collapse in" id="taskMenu">
			<li><a href="addTaskLink"><i class="glyphicon glyphicon-plus"></i> 
					新增任务</a></li>
			<li><a href="queryTasksLink"><i class="glyphicon glyphicon-search"></i>
					查询任务</a></li>
			<li><a href="updateTaskLink"><i class="glyphicon glyphicon-edit"></i>
					修改任务</a></li>
			<li><a href="deleteTaskLink"><i class="glyphicon glyphicon-trash"></i>
					删除任务</a></li>
		</ul>
	</li>
	<li class="nav-header"><a href="#" data-toggle="collapse"
		data-target="#moreMenu">
		<h5>更多数据挖掘功能 <i class="glyphicon glyphicon-chevron-right"></i></h5> </a>
		<ul class="list-unstyled collapse in" id="moreMenu">
			<li><a href="autoDocumentInput"><i class="glyphicon glyphicon-book"></i> 
					分类算法</a></li>
			<li><a href="kMeansInput"><i class="glyphicon glyphicon-resize-small"></i> 
					聚类算法</a></li>
		</ul>
	</li>
</ul>