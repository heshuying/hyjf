<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,配置设置,出借人投标申请查询', ',')}" var="functionPaths"
	scope="request"></c:set>

<shiro:hasPermission name="bidapplyquery:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借人投标申请查询" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">出借人投标申请查询</h1>
			<span class="mainDescription">出借人投标申请查询</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="searchAction" method="post"  role="form" class="form-horizontal">
					
						<!-- 检索条件 -->
						<div class="form-group">
							<label class="col-sm-2 control-label"> <span class="symbol required"></span>电子账号(出借人电子账号)：</label>
							<div class="col-sm-4">
							<input type="text" name="accountId" id="accountId" class="form-control input-sm" value="${bidApplyQueryForm.accountId}" datatype="n" nullmsg="未填写电子账号" />
							<hyjf:validmessage key="accountId" label="项目名称"></hyjf:validmessage>
							</div>
						</div>
<%-- 						<div class="form-group">
							<label class="col-sm-2 control-label"> <span class="symbol required"></span>交易日期：</label>
							<div class="col-sm-4">
							<input type="text" name="orgTxDate" id="orgTxDate" class="form-control input-sm" 
								value="${singletradeinfoForm.orgTxDate}" datatype="n8-8" nullmsg="未填写交易日期" errormsg="请输入8位日期（yyyymmdd）"/>
							</div>
						</div> --%>						
<%-- 						<div class="form-group">
							<label class="col-sm-2 control-label"> <span class="symbol required"></span>交易时间：</label>
							<div class="col-sm-4">
							<input type="text" name="orgTxTime" id="orgTxTime" class="form-control input-sm" 
							value="${singletradeinfoForm.orgTxTime}" datatype="n6-6" nullmsg="未填写交易时间" errormsg="请输入6位时间（hhmmss）"/>
							</div>
						</div> --%>
						<div class="form-group">
							<label class="col-sm-2 control-label"> <span class="symbol required"></span>原订单号(原投标订单号)：</label>
							<div class="col-sm-4">
							<input type="text" name="orgOrderId" id="orgOrderId" class="form-control input-sm" value="${bidApplyQueryForm.orgOrderId}" datatype="n" nullmsg="未填写原订单号"/>
							</div>
						</div>
						<!-- 检索结果 -->
						<div class="form-group">
							<label class="col-sm-2 control-label"> <span class="symbol required"></span>结果：</label>
							<div class="col-sm-4" id="result" data-res=${bidApplyQueryForm.result}>
							</div>
						</div>
						
						<div class="col-xs-offset-2 col-xs-10">
							<shiro:hasPermission name="bidapplyquery:SEARCH">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-Confirm"></i> 检索</a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 重 置</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/bidapplyquery/bidapplyquery.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
