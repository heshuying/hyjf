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
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="启动状态" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link
			href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
			rel="stylesheet" media="screen">
		<style>
.panel-tiles {
	font-family: "微软雅黑"
}

dl>dd {
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
			<!-- 	<input type="hidden" name="ids" id="ids" /> -->
			<input type="hidden" name="debtPlanNid" id="debtPlanNid" /> <input
				type="hidden" name="debtplanstatus" id="debtplanstatus" /> <input
				type="hidden" name="planlistNid" id="planlistNid" /> <input
				type="hidden" name="PlanlistNidSrch" id="PlanlistNidSrch" /> <input
				type="hidden" name="minInvestCounts" id="minInvestCounts"
				value="${planInfo.minInvestCounts}" /> <input type="hidden"
				name="paginatorPage" id="paginator-page"
				value="${planInfo.paginatorPage}" />


			<div class="row panel panel-white" style="padding-right: 5px;">
				<div class="col-md-12">
					<div style="margin: 20px auto;">
						<!-- <legend> 借款信息 </legend> -->
						<div class="row">
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投编号：</strong> <c:out
												value="${planInfo.debtPlanNid }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投名称：</strong> <c:out
												value="${planInfo.debtPlanName }" />
										</label>
									</div>
								</div>
							</div>

							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>还款方式：</strong> <c:if
												test="${planInfo.borrowStyle eq 'endday' }">
												<c:out value="按天计息，到期还本还息" />
											</c:if> <c:if test="${planInfo.borrowStyle eq 'end' }">
												<c:out value="按月计息，到期还本还息" />
											</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>服务回报期限：</strong> <c:if
												test="${planInfo.isMonth eq '1' }">
												<c:out value="${planInfo.lockPeriod }个月" />
											</c:if> <c:if test="${planInfo.isMonth eq '0' }">
												<c:out value="${planInfo.lockPeriod }天" />
											</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>参考年回报率：</strong> <c:out
												value="${planInfo.expectApr }" />%
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div >
										<label> <strong>添加时间：</strong> <c:out value="${planInfo.addTime}"></c:out>
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
					 	<strong>确定要</strong>
					 	<strong>

						<c:choose>
							<c:when test="${enableOrDisplayFlag == 1}">
								<c:if
										test="${planInfo.debtPlanStatus eq '1' }">
									<c:out value="禁用" />
								</c:if>
								<c:if test="${planInfo.debtPlanStatus ne '1' }">
									<c:out value="启用" />
								</c:if>
							</c:when>
							<c:when test="${enableOrDisplayFlag == 2}">
								<c:if test="${planInfo.planDisplayStatusSrch eq '1' }">
									<c:out value="隐藏" />
								</c:if>
								<c:if test="${planInfo.planDisplayStatusSrch ne '1' }">
									<c:out value="显示" />
								</c:if>
							</c:when>
						</c:choose>

						</strong>
						<strong>智投吗?</strong>
					</label>
				</div>
			</div>
			<div class="form-group text-center" style="padding-bottom: 20px;">
				<a class="btn btn-o btn-default" style="margin-right: 20px;"
					onclick="parent.$.colorbox.close()">取消</a> <a
					class="btn btn-o btn-primary fn-Confirm"
					data-debtplanstatus="${planInfo.debtPlanStatus }"
					data-enableordisplayflag="${enableOrDisplayFlag }"
					data-debtplannid="${planInfo.debtPlanNid}">确定</a>
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/hjhplan/plancommonshow.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
