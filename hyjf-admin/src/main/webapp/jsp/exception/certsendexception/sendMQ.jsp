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

<shiro:hasPermission name="certerrorlog:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="应急中心MQ补录" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">记录</h1>
			<span class="mainDescription">本功能可以修改查询相应的记录。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="certerrorlog:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<div class="form-group">
										<label>请求类型</label>
										<select name="infType" class="form-control underline form-select2">
											<option value=""></option>
											<option value="1" <c:if test="${'1' eq form.infType}">selected="selected"</c:if>>用户数据</option>
											<option value="2" <c:if test="${'2' eq form.infType}">selected="selected"</c:if>>散标</option>
											<option value="6" <c:if test="${'6' eq form.infType}">selected="selected"</c:if>>散标状态</option>
											<option value="81" <c:if test="${'81' eq form.infType}">selected="selected"</c:if>>还款计划</option>
											<option value="82" <c:if test="${'82' eq form.infType}">selected="selected"</c:if>>债权信息</option>
											<option value="83" <c:if test="${'83' eq form.infType}">selected="selected"</c:if>>转让项目</option>
											<option value="84" <c:if test="${'84' eq form.infType}">selected="selected"</c:if>>转让状态</option>
											<option value="85" <c:if test="${'85' eq form.infType}">selected="selected"</c:if>>承接信息</option>
											<option value="4" <c:if test="${'4' eq form.infType}">selected="selected"</c:if>>交易流水</option>
										</select>
									</div>
									<br/><br/>
									<div class="form-group">
										<label>请求参数</label>
										<input type="text" name="mqValue" id="mqValue" class="form-control underline" value="" />
									</div>
									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="11" data-placement="bottom" data-original-title="发送MQ"> 发送MQ</a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<%-- 分页栏 --%>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
		</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/certsendexception/certerrorlog.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
