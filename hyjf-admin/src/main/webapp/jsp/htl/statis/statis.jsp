<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<shiro:hasPermission name="htlstatis:VIEW">
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="统计分析" />
	
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">统计分析</h1>
		<span class="mainDescription">统计分析统计分析统计分析统计分析的说明。</span>
	</tiles:putAttribute>
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul id="mainTabs" class="nav nav-tabs nav-justified">
				<li class="s-ellipsis active">
					<a href="#tab_sjzl_1" data-toggle="tab"><i class="fa fa-area-chart"></i> 数据总览</a>
				</li>
				<li>
					<a href="#tab_jefb_2" data-toggle="tab"><i class="fa fa-bar-chart"></i> 出借金额分布</a>
				</li>
				<li>
					<a href="#tab_khfb_3" data-toggle="tab"><i class="fa fa-users"></i> 新老客户分布</a>
				</li>
				<li>
					<a href="#tab_ptfb_4" data-toggle="tab"><i class="fa fa-sitemap"></i> 平台分布</a>
				</li>
			</ul>
			<form id="mainForm"  method="post"  role="form">
			<div class="tab-content">
				<!-- Start:数据总览 -->
				<div class="tab-pane fade in active" id="tab_sjzl_1">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Margins">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 待成交资产-专属资产</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-warning fn-dayView" href="#"
													data-original-title="按天显示" data-paramvalue ="1-1" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar-o"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="#" data-original-title="按月显示" data-paramvalue ="1-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htl/productinfo/init" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
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
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Balance">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 待成交资产--债权转让</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-warning fn-dayView" href="#"
													data-original-title="按天显示" data-paramvalue ="2-1" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar-o"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="#" data-original-title="按月显示" data-paramvalue ="2-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htl/productinfo/init" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-balance" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:数据总览 -->
				
				<!-- Start:出借金额分布 -->
				<div class="tab-pane fade" id="tab_jefb_2">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2TotalAmount">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 出借人本金-金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
												<div class="input-group">
													<span class="input-icon">
														<input type="text" name="" id="tab2-chart1-date-time" data-paramvalue ="5-1" class="form-control input-sm underline datepicker" placeholder="选择日期" />
														<i class="ti-calendar"></i> </span>
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表1 -->
										<div id="chart-total-amount" style="height:260px;"></div>
										<!-- End: tab2-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white"  data-reloadfn="tab2Total">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 出借人本金-人数分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
												<div class="input-group">
													<span class="input-icon">
														<input type="text" name="" id="tab2-chart2-date-time" data-paramvalue ="6-1" class="form-control input-sm underline datepicker" placeholder="选择日期" />
														<i class="ti-calendar"></i> </span>
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表2 -->
										<div id="chart-total" style="height:260px;"></div>
										<!-- End: tab2-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2OutTotal">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 转出金额-人次分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab2-chart3-start-date" data-paramvalue ="7-1" class="form-control input-sm underline " placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab2-chart3-end-date" data-paramvalue ="7-2" class="form-control btn-sm input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表3 -->
										<div id="chart-out-total" style="height:260px;"></div>
										<!-- End: tab2-图表3 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2IntoTotal">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 转入金额-人次分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab2-chart4-start-date" data-paramvalue ="8-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab2-chart4-end-date" data-paramvalue ="8-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表4 -->
										<div id="chart-into-total" style="height:260px;"></div>
										<!-- End: tab2-图表4 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:出借金额分布 -->
				
				<!-- Start:新老客户分布 -->
				<div class="tab-pane fade" id="tab_khfb_3">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab3CustomerNumber">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 新老客户-出借人数分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
												<div class="input-group">
													<span class="input-icon">
														<input type="text" name="" id="tab3-chart1-date-time" data-paramvalue ="9-1" class="form-control input-sm underline datepicker" placeholder="选择日期" />
														<i class="ti-calendar"></i> </span>
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab3-图表1 -->
										<div id="chart-customer-number" style="height:260px;"></div>
										<!-- End: tab3-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab3CustomerSum">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 新老客户-本金金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
												<div class="input-group">
													<span class="input-icon">
														<input type="text" name="" id="tab3-chart2-date-time" data-paramvalue ="10-1" class="form-control input-sm underline datepicker" placeholder="选择日期" />
														<i class="ti-calendar"></i> </span>
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab3-图表2 -->
										<div id="chart-customer-sum" style="height:260px;"></div>
										<!-- End: tab3-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab3IntoMoney">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 新老客户-转入金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab3-chart3-start-date" data-paramvalue ="11-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab3-chart3-end-date"  data-paramvalue ="11-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab3-图表3 -->
										<div id="chart-into-money" style="height:260px;"></div>
										<!-- End: tab3-图表3 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab3OutMoney">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 新老客户-转出金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab3-chart4-start-date" data-paramvalue ="12-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab3-chart4-end-date" data-paramvalue ="12-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab3-图表4 -->
										<div id="chart-out-money" style="height:260px;"></div>
										<!-- End: tab3-图表4 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:新老客户分布 -->
				
				<!-- Start:平台分布 -->
				<div class="tab-pane fade" id="tab_ptfb_4">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab4MoneyEnter">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-转入金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab4-chart1-start-date" data-paramvalue ="13-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab4-chart1-end-date" data-paramvalue ="13-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab4-图表1 -->
										<div id="chart-money-enter" style="height:260px;"></div>
										<!-- End: tab4-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab4MoneyOut">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-转出金额分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab4-chart2-start-date" data-paramvalue ="14-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab4-chart2-end-date" data-paramvalue ="14-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab4-图表2 -->
										<div id="chart-money-out" style="height:260px;"></div>
										<!-- End: tab4-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab4NumberEnter">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-转入用户数分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab4-chart3-start-date" data-paramvalue ="15-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab4-chart3-end-date" data-paramvalue ="15-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab4-图表3 -->
										<div id="chart-number-enter" style="height:260px;"></div>
										<!-- End: tab4-图表3 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab4NumberOut">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-转出用户数分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-5">
												<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab4-chart4-start-date" data-paramvalue ="16-1" class="form-control input-sm underline" placeholder="开始日期" />
														<i class="ti-calendar"></i> </span>
													<span class="input-group-addon no-border bg-light-orange">~</span>
													<input type="text" name="" id="tab4-chart4-end-date" data-paramvalue ="16-2" class="form-control input-sm underline" placeholder="结束日期" />
												</div>
											</div>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab4-图表4 -->
										<div id="chart-number-out" style="height:260px;"></div>
										<!-- End: tab4-图表4 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:平台分布 -->
			</div>
			</form>
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
		<script type='text/javascript' src="${webRoot}/jsp/htl/statis/statis.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
</shiro:hasPermission>