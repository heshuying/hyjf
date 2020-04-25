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

<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="我的工作台" />
	
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
	</tiles:putAttribute>
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="row">
			<div class="col-sm-12">
				<!-- start:平台数据总览 -->
				<div class="panel panel-white no-radius" id="visits">
					<div class="panel-heading border-light">
						<h4 class="panel-title"> 平台数据总览 </h4>
						<ul class="panel-heading-tabs border-light">
							<li class="panel-tools">
								<i style="font-size:14px">数据截止至：2015-12-24-11:37:05</i>
							</li>
						</ul>
					</div>
					<div class="panel-wrapper">
						<div class="panel-body">
							<div class="row padding-top-15 padding-bottom-15">
								<div class="col-sm-3 center border-right">
									<h3>4,484,812,351.00</h3><br/>
									<small>用户出借总额(元)</small>
								</div>
								<div class="col-sm-3 center border-right">
									<h3>157,116,026.83</h3><br/>
									<small>用户赚取收益(元)</small>
								</div>
								<div class="col-sm-3 center border-right">
									<h3>3,303,885,106.61</h3><br/>
									<small>已支付用户本金(元)</small>
								</div>
								<div class="col-sm-3 center">
									<h3>94,262,134.00</h3><br/>
									<small>已支付用户收益(元)</small>
								</div>
							</div>
							<hr/>
							<!-- start:图表 -->
							<div id="chart1" class="height-270"></div>
							<!-- end:图表 -->
						</div>
					</div>
				</div>
				<!-- end:平台数据总览 -->
				
				<!-- start:7日平台数据变化 -->
				<div class="panel panel-white no-radius" id="visits">
					<div class="panel-heading border-light">
						<h4 class="panel-title"> 7日平台数据变化 </h4>
						<ul class="panel-heading-tabs border-light">
							<li class="panel-tools">
								<i style="font-size:14px">数据截止至：2015-12-24-11:37:05</i>
							</li>
						</ul>
					</div>
					<div class="panel-wrapper">
						<div class="panel-body">
							<div class="row padding-top-15 padding-bottom-15">
								<div class="col-sm-3 center border-right">
									<h3>86,521,561.00</h3><br/>
									<small>用户出借总额 (元)</small>
								</div>
								<div class="col-sm-3 center border-right">
									<h3>2,807,498.91</h3><br/>
									<small>用户赚取收益 (元)</small>
								</div>
								<div class="col-sm-3 center border-right">
									<h3>102,080,000.00</h3><br/>
									<small>已支付用户本金 (元)</small>
								</div>
								<div class="col-sm-3 center">
									<h3>3,520,667.73</h3><br/>
									<small>已支付用户收益 (元)</small>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- end:7日平台数据变化 -->
				
				<!-- start:平台风险承受能力 -->
				<div class="panel panel-white no-radius" id="visits">
					<div class="panel-heading border-light">
						<h4 class="panel-title"> 平台风险承受能力 </h4>
						<ul class="panel-heading-tabs border-light">
							<li class="panel-tools">
								<i style="font-size:14px">数据截止至：2015-12-24-11:37:05</i>
							</li>
						</ul>
					</div>
					<div class="panel-wrapper">
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-5 border-right">
									<h4><b>平台逾期、坏账数据</b></h4>
									<small>自 2013 年 12 月 22 日以来无坏账</small>
									<table class="table margin-top-20">
										<colgroup>
											<col></col>
											<col style="width:60px;"></col>
										</colgroup>
										<tbody>
											<tr>
												<td style="line-height:24px;">逾期金额</td>
												<td>0.00</td>
											</tr><tr>
												<td style="line-height:24px;">逾期比例</td>
												<td>0.00 %</td>
											</tr><tr>
												<td style="line-height:24px;">坏账金额</td>
												<td>0.00</td>
											</tr><tr>
												<td style="line-height:24px;">坏账比例</td>
												<td>0.00 %</td>
											</tr><tr>
												<td style="line-height:24px;">代偿金额</td>
												<td>0.00</td>
											</tr><tr>
												<td style="line-height:24px;">项目展期</td>
												<td>0.00</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="col-sm-7">
									<h4><b>平台逾期、坏账数据</b></h4>
									<div id="chart2" class="height-250"></div>
								</div>
							</div>
						</div>
					</div>
					</div>
				<!-- end:平台风险承受能力 -->
			</div>
		</div>
	</div>	
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/echarts-all.js"></script>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/desktop/desktop.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
