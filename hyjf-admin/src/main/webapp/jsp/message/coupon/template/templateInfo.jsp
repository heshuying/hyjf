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
				<form id="mainForm" action="${empty infoForm.id ? 'addAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 文章列表一览 --%>
					<input type="hidden" name="id" id="id" value="${infoForm.id }" />
					<input type="hidden" name="tplCode" id="tplCode" value="${infoForm.tplCode}" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-xs-2 control-label" for="type"> 
							<span class="symbol required"></span>模板名称
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="模板名称" id="tplName" name="tplName"  class="form-control" maxlength="20"
										datatype="s1-20" errormsg="模板名称的长度不能大于20个字符！" >
									<hyjf:validmessage key="tplName" label="模板名称 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="模板名称" id="tplName" name="tplName" class="form-control" value="${infoForm.tplName}" >
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label" for="title"> 
							<span class="symbol required"></span>模板标识
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="模板标识" id="templateCode" name="templateCode"  class="form-control" maxlength="20"
										datatype="s1-20" errormsg="模板标识的长度不能大于20个字符！" >
									<hyjf:validmessage key="templateCode" label="模板标识 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="模板标识" id="templateCode" name="templateCode" value="${infoForm.templateCode}" disabled>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label" for="author"> 
							<span class="symbol required"></span>模板内容
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<textarea rows="15" cols="15" placeholder="模板内容" id="tplContent" name="tplContent"  class="form-control" maxlength="200"></textarea>
									<hyjf:validmessage key="tplContent" label="模板内容 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<textarea rows="15" cols="15" placeholder="模板内容" id="tplContent" name="tplContent"  class="form-control" maxlength="200">${infoForm.tplContent}</textarea>
								</c:otherwise>
							</c:choose>
							可选参数： 姓名: [val_name] ；性别: [val_sex] ；金额: [val_amount] ；日期: [val_date] ；余额: [val_balance] ；出借利率: [val_rate] ；项目名称: [val_title]；借款期限: [val_limit]
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
		<script type='text/javascript' src="${webRoot}/jsp/message/coupon/template/templateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
