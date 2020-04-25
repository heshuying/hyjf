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

<shiro:hasPermission name="couponrepaymonitor:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="投之家日报表" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">投之家日报表</h1>
			<span class="mainDescription">投之家日报表</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
		      		<li><a href="${webRoot}/promotion/tzj/dayreport/init">统计报表</a></li>
		      		<li class="active"><a href="${webRoot}/promotion/tzj/dayreport/chart">统计图表</a></li>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
								<div class="well">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								   
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
									 <span  style="display:block;float:right;margin-right:20px;line-height:30px"> 更新时间 ：${latestUpdateTime} </span>
								</div>
							<br/>
							<%-- 列表一览 --%>
							<div class="tab-pane fade in active" id="tab_sjzl_1">
						<div class="row">
							<div class="col-xs-12 col-md-12">
								<div class="panel panel-white" >
									<div class="panel-wrapper">
										<div class="panel-body">
											<!-- Start: tab1-图表3 -->
											<div id="coupon_repay_statistic" style="height:360px;width:100%"></div>
											<!-- End: tab1-图表3 -->
										</div>
									</div>
								</div>
							</div>
	
						</div>
					</div>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="couponrepaymonitor:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${tzjDayReportForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
					</div>
				</div>
			</div>
		  </div>
		</tiles:putAttribute>
		
				<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${tzjDayReportForm.paginatorPage}" />
			<shiro:hasPermission name="couponrepaymonitor:SEARCH">
				<div class="form-group">
					<label>统计时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${tzjDayReportForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${tzjDayReportForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<!-- Form表单插件 -->
			<%@include file="/jsp/common/pluginBaseForm.jsp"%>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/echarts-all.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/tzjdayreport/reportchart.js"></script>
			<script type='text/javascript'>
			var chartTitle = "${chartTitle}";
			var days = ${days};
			var registCount = ${registCount}; 
			var tenderFirstCount = ${tenderFirstCount}; 
			
			initChart();
			
			</script> 
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
