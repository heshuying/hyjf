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
					<a href="#tab_sjzl_1" data-toggle="tab"><i class="fa fa-area-chart"></i>债权分布</a>
				</li>
				<li>
					<a href="#tab_jefb_2" data-toggle="tab"><i class="fa fa-bar-chart"></i>数据总览</a>
				</li>
				<li>
					<a href="#tab_khfb_3" data-toggle="tab"><i class="fa fa-users"></i>出借金额分布</a>
				</li>
			</ul>
			<form id="mainForm"  method="post"  role="form">
			<div class="tab-content">
				<!-- Start:债权分布 -->
				<div class="tab-pane fade in active" id="tab_sjzl_1">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Margins">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 待成交资产-专属资产</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-warning fn-dayView" href="javascript:;"
													data-original-title="按小时显示" data-paramvalue ="1-1" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar-o"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
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
											<a class="btn btn-warning fn-dayView" href="javascript:;"
													data-original-title="按小时显示" data-paramvalue ="2-1" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar-o"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
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
						
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2TotalAmount">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 待成交专属资产-期限分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
													<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab1-chart1-start-date" data-paramvalue ="5-1" class="form-control input-sm underline " placeholder="日期" />
														<i class="ti-calendar"></i> </span>
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
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 待成交债权转让-期限分布</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
														<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab2-chart2-start-date" data-paramvalue ="6-1" class="form-control input-sm underline " placeholder="日期" />
														<i class="ti-calendar"></i> </span>
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
							<div class="panel panel-white" data-reloadfn="tab1Margins2">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 计划持有债权数量-专属资产</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="3-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表1 -->
										<div id="chart-margins2" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Balance2">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 计划持有债权数量-债权转让</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="4-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-balance2" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
						
							<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Margins3">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 计划持有债权待还总额</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="7-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表1 -->
										<div id="chart-margins3" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab1Balance3">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 计划持有债权已还总额</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="8-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-balance3" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
						
					</div>
				</div>
				<!-- End:债权分布  -->
				
				<!-- Start:数据总览 -->
				<div class="tab-pane fade" id="tab_jefb_2">
					<div class="row">
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2Balance1">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 出借人加入总额</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="9-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-tab2Balance1" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2Balance2">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 应还款总额</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="10-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-tab2Balance2" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2Balance3">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 已还款总额</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="11-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-tab2Balance3" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab2Balance4">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 到期公允价值</h4>
									<ul class="panel-heading-tabs border-light">
										<li class="panel-tools">
											<a class="btn btn-success fn-monthView"
													href="javascript:;" data-original-title="按天显示" data-paramvalue ="12-2" data-toggle="tooltip" data-placement="top"><i class="fa fa-calendar"></i></a>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success fn-listView"
													href="${webRoot}/htj/statis/statisDetail" data-original-title="查看明细" data-toggle="tooltip" data-placement="top"><i class="fa fa-list"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab1-图表2 -->
										<div id="chart-tab2Balance4" style="height:360px;width:100%"></div>
										<!-- End: tab1-图表2 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:数据总览 -->
				
				<!-- Start:出借金额分布  -->
				<div class="tab-pane fade" id="tab_khfb_3">
					<div class="row">
							<div class="col-xs-12 col-md-6">
							<div class="panel panel-white" data-reloadfn="tab4TotalAmount">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 加入金额分布-金额分布</h4>
								 	<ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
													<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab14-chart14-start-date" data-paramvalue ="14-1" class="form-control input-sm underline " placeholder="" disabled="disabled" />
														<i class="ti-calendar"></i> </span>
													<input type="text" name="" id="tab14-chart14-end-date" data-paramvalue ="14-2" class="form-control input-sm underline" placeholder=""  disabled="disabled"/>
												</div>
											</div>
										</li>
									</ul> 
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表1 -->
										<div id="chart-total-amount4" style="height:260px;"></div>
										<!-- End: tab2-图表1 -->
									</div>
								</div>
							</div>
						</div>
						<div class="col-xs-12 col-md-6">
							<div class="panel panel-white"  data-reloadfn="tab4Total">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 加入金额分布-人次分布</h4>
									 <ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
														<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab15-chart15-start-date" data-paramvalue ="15-1" class="form-control input-sm underline " placeholder="" disabled="disabled"/>
														<i class="ti-calendar"></i> </span>
													<input type="text" name="" id="tab15-chart15-end-date" data-paramvalue ="15-2" class="form-control input-sm underline" placeholder="" disabled="disabled" />
												</div>
											</div>
										</li>
									</ul> 
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表2 -->
										<div id="chart-total4" style="height:260px;"></div>
										<!-- End: tab2-图表2 -->
									</div>
								</div>
							</div>
						</div>
							<div class="col-xs-12 col-md-6">
							<div class="panel panel-white"  data-reloadfn="tab5Total">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 加入次数分布-人次分布</h4>
									 <ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
														<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab16-chart16-start-date" data-paramvalue ="16-1" class="form-control input-sm underline " placeholder=""  disabled="disabled"/>
														<i class="ti-calendar"></i> </span>
													<input type="text" name="" id="tab16-chart16-end-date" data-paramvalue ="16-2" class="form-control input-sm underline" placeholder="" disabled="disabled" />
												</div>
											</div>
										</li>
									</ul> 
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表2 -->
										<div id="chart-total5" style="height:260px;"></div>
										<!-- End: tab2-图表2 -->
									</div>
								</div>
							</div>
						</div>
							<div class="col-xs-12 col-md-6">
							<div class="panel panel-white"  data-reloadfn="tab6Total">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-加入金额分布</h4>
									 <ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
														<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab17-chart17-start-date" data-paramvalue ="17-1" class="form-control input-sm underline " placeholder="" disabled="disabled" />
														<i class="ti-calendar"></i> </span>
													<input type="text" name="" id="tab17-chart17-end-date" data-paramvalue ="17-2" class="form-control input-sm underline" placeholder="" disabled="disabled"/>
												</div>
											</div>
										</li>
									</ul> 
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表2 -->
										<div id="chart-total6" style="height:260px;"></div>
										<!-- End: tab2-图表2 -->
									</div>
								</div>
							</div>
						</div>
							<div class="col-xs-12 col-md-6">
							<div class="panel panel-white"  data-reloadfn="tab7Total">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="ti-arrow-circle-right"></i> 平台-加入人次分布</h4>
									 <ul class="panel-heading-tabs border-light">
										<li>
											<div class="form-group no-margin margin-top-10">
														<div class="input-group input-daterange datepicker">
													<span class="input-icon">
														<input type="text" name="" id="tab18-chart18-start-date" data-paramvalue ="18-1" class="form-control input-sm underline " placeholder="" disabled="disabled"/>
														<i class="ti-calendar"></i> </span>
													<input type="text" name="" id="tab18-chart18-end-date" data-paramvalue ="18-2" class="form-control input-sm underline" placeholder="" disabled="disabled"/>
												</div>
											</div>
										</li>
									</ul> 
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start: tab2-图表2 -->
										<div id="chart-total7" style="height:260px;"></div>
										<!-- End: tab2-图表2 -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- End:出借金额分布   -->
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
		<script type='text/javascript' src="${webRoot}/jsp/htj/statis/statis.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
</shiro:hasPermission>