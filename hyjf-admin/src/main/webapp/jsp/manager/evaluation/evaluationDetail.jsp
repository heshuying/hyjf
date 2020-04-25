<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="消息模板管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty infoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" method="post"  role="form" class="form-horizontal" >
					<c:if test="${ifEvaluation==1}">
					<div class="form-group">
						
						<label class="col-xs-6"> 
							用户名:&nbsp;&nbsp;<c:out value="${user.username}"></c:out>
						</label>
						<div class="col-xs-10">
							
						</div>
						
					</div>
					
					<c:forEach items="${list }" var="item" begin="0" step="1">
					
					<div class="form-group">
						
						<label class="col-xs-12"> 
							<c:out value="${item.question}"></c:out>&nbsp;<c:out value="${item.answer}"></c:out>&nbsp;&nbsp;(<c:out value="${item.score}"></c:out>分)
						</label>
						
					</div>
					</c:forEach>
					
					
					
					<div class="form-group">
						
						<label class="col-xs-6"> 
							风险测评得分:&nbsp;&nbsp;<c:out value="${userEvalationResult.scoreCount}"></c:out>
						</label>
						
					</div>
					<div class="form-group">
						
						<label class="col-xs-6"> 
							风险测评等级:&nbsp;&nbsp;<c:out value="${userEvalationResult.type}"></c:out>
						</label>
					</div>
					</c:if>
					<c:if test="${ifEvaluation==0}">
					<div class="form-group">
						<div class="col-xs-10">
							用户未测评
						</div>
					</div>
					</c:if>
<!-- 					<br>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 返回</a>
						</div>
					</div> -->
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
			.purMargin{
				margin:8px 0;
			}
			.purMargin input{
				width:200px;
			}
		</style>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/message/template/templateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
