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

<shiro:hasPermission name="certlog:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="应急中心数据同步" />

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
							<shiro:hasPermission name="certlog:MODIFY">
								<%-- 功能栏 --%>
								<div class="well">
									<a class="btn btn-o btn-primary btn-sm data-syn"
											data-toggle="tooltip" data-type="1" data-placement="bottom" data-original-title="投资人数据同步"> 投资人数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="2" data-placement="bottom" data-original-title="借款人数据同步"> 借款人数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="3" data-placement="bottom" data-original-title="标的数据同步"> 标的数据同步</a>
									<br/>
									<br/>
									<br/>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="4" data-placement="bottom" data-original-title="散标状态数据同步"> 散标状态数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="5" data-placement="bottom" data-original-title="还款计划数据同步"> 还款计划数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="6" data-placement="bottom" data-original-title="债权信息数据同步"> 债权信息数据同步</a>
									<br/>
									<br/>
									<br/>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="7" data-placement="bottom" data-original-title="转让项目数据同步"> 转让项目数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="8" data-placement="bottom" data-original-title="转让状态数据同步"> 转让状态数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="9" data-placement="bottom" data-original-title="承接信息数据同步"> 承接信息数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="10" data-placement="bottom" data-original-title="交易流水数据同步"> 交易流水数据同步</a>

									<a class="btn btn-o btn-primary btn-sm data-syn"
									   data-toggle="tooltip" data-type="11" data-placement="bottom" data-original-title="交易流水mongo上报"> 交易流水mongo上报</a>

								</div>
							</shiro:hasPermission>
							<br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string"></tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/datacenter/datasyn/datasyn.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
