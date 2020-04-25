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
	<tiles:putAttribute name="pageTitle" value="通知配置管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty infoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty infoForm.id ? 'addAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 文章列表一览 --%>
					<input type="hidden" name="id" id="id" value="${infoForm.id }" />
					<input type="hidden" name="name" id="name" value="${infoForm.name}" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-xs-2 control-label padding-top-5" for="type"> 
							<span class="symbol required"></span>标识
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="  不能重复" id="configName" name="configName"  class="form-control" maxlength="50"
										datatype="*1-50" errormsg="标识只能包含英文字母和下划线且不能大于50个字符！" ajaxurl="checkAction" >
									<hyjf:validmessage key="configName" label="标识 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="标识" id="name" name="name" class="form-control" maxlength="50"
										datatype="*1-50" errormsg="标识的长度不能为空且不能大于50个字符！" value="${infoForm.name}" disabled>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label padding-top-5" for="title"> 
							<span class="symbol required"></span>名称
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="  用于后台显示的配置名称" id="title" name="title"  class="form-control" maxlength="20"
										datatype="*1-20" errormsg="名称的长度不能大于20个字符！" >
									<hyjf:validmessage key="title" label="名称"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="名称" id="title" name="title" class="form-control" maxlength="20"
										datatype="*1-20" errormsg="名称的长度不能大于20个字符！" value="${infoForm.title}">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label padding-top-5" for="author"> 
							<span class="symbol required"></span>配置值
						</label>
						<div class="col-xs-10">
									<textarea rows="5" cols="5" placeholder="多个号码请用英文半角逗号','隔开" id="value" name="value"  class="form-control" maxlength="200"
										datatype="*1-200" errormsg="配置值的长度不能大于200个字符！">${infoForm.value}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label padding-top-5" for="author"> 
							<span class="symbol required"></span>通知内容
						</label>
						<div class="col-xs-10">
									<textarea rows="5" cols="5" placeholder="需要替换的内容，请用特殊字符*、#等代替" id="content" name="content"  class="form-control" maxlength="200" datatype="*1-200" errormsg="配置内容的长度不能大于200个字符！">${infoForm.content}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label padding-top-5" for="author"> 
							备注说明
						</label>
						<div class="col-xs-10">
									<textarea rows="5" cols="5" placeholder="备注说明" id="remark" name="remark"  class="form-control" maxlength="200">${infoForm.remark}</textarea>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
			.panel-title { font-family: "微软雅黑" }
		</style>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/message/template/templateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
