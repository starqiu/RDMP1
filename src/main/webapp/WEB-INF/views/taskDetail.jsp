<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--/tabs-->
<div class="container ">
	<div class="col-md-5 center-block">
		<ul class="nav nav-tabs " id="myTab">
			<li class="active"><a href="#train" data-toggle="tab">训练集</a>
			</li>
			<li><a href="#step2" data-toggle="tab">同现矩阵</a></li>
			<li><a href="#train2" data-toggle="tab">评分矩阵</a></li>
			<li><a href="#result" data-toggle="tab">推荐结果</a></li>
			<li><a href="#validate" data-toggle="tab">校验集</a></li>
		</ul>
		<br />
		<div class="tab-content">
			<div class="tab-pane active" id="train">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<img  src="<c:url value='/images/train.png' />"
							class="img-rounded" />
					</div>
				</div>
			</div>
			<div class="tab-pane" id="step2">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<img  src="<c:url value='/images/step2.png' />"
							class="img-rounded" />
					</div>
				</div>
			</div>
			<div class="tab-pane" id="train2">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<img  src="<c:url value='/images/train2.png' />"
							class="img-rounded" />
					</div>
				</div>
			</div>
			<div class="tab-pane" id="result">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<img  src="<c:url value='/images/result.png' />"
							class="img-rounded" />
					</div>
				</div>
			</div>
			<div class="tab-pane" id="validate">
				<div class="row clearfix">
					<div class="col-md-12 column">
						<img  src="<c:url value='/images/validate.png' />"
							class="img-rounded" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!--/tabs-->
