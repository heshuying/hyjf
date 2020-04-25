<%@page import="com.hyjf.common.util.GetDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<%-- <shiro:hasPermission name="wkcdBorrow:VIEW" > --%>
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="微可车贷资产审核 "/>
    
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">微可车贷资产审核</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
			<ul id="mainTabs" class="nav nav-tabs nav-justified">
				<li class="s-ellipsis active">
					<a href="javascript:void(0)" onclick="changeTab('baseView')" data-toggle="tab"><i class="fa fa-edit"></i>基本信息</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('carView')" data-toggle="tab"><i class="fa fa-cubes"></i>车辆信息</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('phoneAuditLogView')" data-toggle="tab"><i class="fa fa-folder-open"></i>电审信息</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('callbackLogView')" data-toggle="tab"><i class="fa fa-server"></i>回访记录</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('auditLogView')" data-toggle="tab"><i class="fa fa-user-secret"></i>审核记录</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('cashPledgeView')" data-toggle="tab"><i class="fa fa-briefcase"></i>押金信息</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('rcView')" data-toggle="tab"><i class="fa fa-legal"></i>风控报告</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="changeTab('enclosureView')" data-toggle="tab"><i class="fa fa-user"></i>附件信息</a>
				</li>
			</ul>
						    <input type="hidden" id="i_wkcdId" value="${wkcdId}"/>
							<iframe src="gotoWkcdPage?wkcdId=${wkcdId}&type=baseView" id="i_detail" style="width: 100%;height:800px;border: none"></iframe>			
							<br/><br/>
						</div>
						<c:choose>
						    <c:when test="${wkcdBorrowForm.hyjfStatus == 0}">
						        <h6>审核:</h6>
								<form id="form" method="post">
								     <input type="hidden" name="id"  value="${wkcdBorrowForm.id}"/>
									 <input type="radio" name="verify"  value="1" checked="checked" />通过    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									 <input type="radio" name="verify"  value="2" />不通过<br>
							    <h6>审核意见:</h6>
									 <textarea rows="10" cols="50" name="yijian" id="yijian"></textarea>
								</form>
						    </c:when>
						    <c:otherwise>
						        <h6>审核意见:</h6>
									 ${wkcdBorrowForm.checkDesc}
						    </c:otherwise>
						</c:choose>
					</div>
				</div>
				<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Next" href="javascript:history.back()">
				                <i class="fa fa-error"></i> 返回
				</a>
				<c:if test="${wkcdBorrowForm.hyjfStatus == 0}">
				    <a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存
				    </a>
				</c:if>
				
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/wkcd/wkcdborrow/detail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
<%-- </shiro:hasPermission> --%>
