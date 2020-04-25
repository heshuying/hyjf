<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="refeestatistic:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="充值手续费统计" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">充值手续费垫付统计图表</h1>
			<span class="mainDescription">这里添加充值手续费垫付统计描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		
		 <div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/account/accountList">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li><a href="${webRoot}/finance/web/web_list">网站收支</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/transfer/transferList">子账户间转账</a></li>
			      	</shiro:hasPermission>
	   				<shiro:hasPermission name="refeestatistic:VIEW">
						<li class="active" ><a href="${webRoot}/finance/statistics/rechargefee/statistics">充值手续费统计</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="couponrepaymonitor:VIEW">
						<li><a href="${webRoot}/finance/couponrepaymonitor/chart">加息券还款统计</a></li>
					</shiro:hasPermission>
			    </ul>
		
				<div class="tab-content">
					<div class="tab-pane fade in active">

								<div class="tab-pane fade in active" id="tab_sjzl_1">
									<div class="row">
									
										<div class="col-xs-12 col-md-12">
											<div class="panel panel-white" >
												<div class="panel-heading border-light">
													<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 充值手续费垫付金额</h4>
													<ul class="panel-heading-tabs border-light">
													
													<li class="panel-tools">
														更新时间：${rechargeFeeStatisticsForm.updateTimeView }
														</li>
														<li class="panel-tools">
															<a class="btn btn-success fn-listView" href="${webRoot}/finance/statistics/rechargefee/init" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
														</li>
													</ul>
												</div>
												<div class="panel-wrapper">
													<div class="panel-body">
														<!-- Start: tab1-图表3 -->
														<div id="chart-turn-money" style="height:360px;width:100%"></div>
														<!-- End: tab1-图表3 -->
													</div>
												</div>
											</div>
										</div>
										<div class="col-xs-12 col-md-12">
											<div class="panel panel-white">
												<div class="panel-heading border-light">
													<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 充值手续费垫付账户余额</h4>
													<ul class="panel-heading-tabs border-light">
														<li class="panel-tools">
															<a class="btn btn-warning fn-todayView" href="#"
																	data-original-title="今日" data-paramvalue ="1" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar-o"></i></a>
														</li>
														<li class="panel-tools">
															<a class="btn btn-success fn-yestodayView"
																	href="#" data-original-title="昨日" data-paramvalue ="2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
														</li>
													</ul>
												</div>
												<div class="panel-wrapper">
													<div class="panel-body">
														<!-- Start: tab1-图表1 -->
														<div id="chart-margins" style="height:360px;width:100%"></div>
														<!-- End: tab1-图表1 -->
													</div>
												</div>
											</div>
										</div>
				
									</div>
								</div>
								<!-- End:数据总览 -->
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<!-- Form表单插件 -->
			<%@include file="/jsp/common/pluginBaseForm.jsp"%>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/echarts-all.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/finance/statistics/rechargefee/rechargefee_statistics.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
