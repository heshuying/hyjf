<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="发提成" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
	<style>
	.panel-title { font-family: "微软雅黑" }
	dl > dd {
		    height: 30px;
	}
	.panel-table .tlabel {
		height: 30px;
		font-weight: 700;
		width: 35%;
	}
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<form id="mainForm" method="post" role="form">
			<input type="hidden" name="ids" id="ids" />

			<div class="row panel panel-white" style="padding-right: 5px;">
				<div class="col-md-12">
					<div style="margin:20px auto;">
						<!-- <legend> 借款信息 </legend> -->
						<div class="row">
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投订单号：</strong> <c:out value="${showObject.planOrderId }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投编号：</strong> <c:out value="${showObject.planNid }" />
										</label>
									</div>
								</div>
							</div>

							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>服务回报期限：</strong>
										<c:if test="${showObject.borrowStyle eq 1 }">
											<c:out value="${showObject.lockPeriod }个月" />
										</c:if>
										<c:if test="${showObject.borrowStyle eq 0 }">
											<c:out value="${showObject.lockPeriod }天" />
										</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>参考年回报率：</strong> <c:out value="${(showObject.expectApr)[0] }" />%
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>提成人：</strong> <c:out value="${showObject.username }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>提成金额：</strong> <fmt:formatNumber value="${showObject.commission }" pattern="#,##0.00#"/>元
										</label>
									</div>
								</div>
							</div>
							
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投订单锁定时间：</strong> <c:out value="${showObject.countInterestTime }" />
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-12">
				<div class="text-center" style="margin-bottom: 20px;">
					<label>
					 	<strong>确定要发放提成吗?</strong>
					</label>
				</div>
			</div>
			<div class="form-group text-center" style="padding-bottom:20px;">
				<a class="btn btn-o btn-default" style="margin-right:20px;" onclick="parent.$.colorbox.close()">取消</a>
				<a class="btn btn-o btn-primary fn-Confirm" data-id="${showObject.ids}">确定</a>
			</div>
		</form>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/pushmoneyhjh/pushMoneyShow.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
