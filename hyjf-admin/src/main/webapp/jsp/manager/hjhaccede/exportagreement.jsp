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
	<tiles:putAttribute name="pageTitle" value="发送协议" />
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

<%--    			<input type="hidden" id="planOrderId" value="${accedeOrderId}" />
			<input type="hidden" id="debtPlanNid" value="${planNid}" />
			<input type="hidden" id="success" value="${success}" />
			email地址:<br/><input type="text" id="email" /><br/>
			<a class="btn btn-o btn-primary fn-ExportAgreement"><i class="fa fa-download"></i>发送协议</a> --%>

			<input type="hidden" id="userid" value="${userid}" />
			<input type="hidden" id="planOrderId" value="${accedeOrderId}" />
			<input type="hidden" id="debtPlanNid" value="${planNid}" />
			<input type="hidden" id="success" value="${success}" />

			<div class="row panel panel-white" style="padding-right: 5px;">
				<div class="col-md-12">
					<div style="margin:20px auto;">
						<!-- <legend> 借款信息 </legend> -->
						<div class="row">
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投订单号：</strong> <c:out value="${accedeOrderId }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>智投编号：</strong> <c:out value="${planNid }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>还款方式：</strong>
										<c:if test="${borrowStyle eq 'end' }">
											<c:out value="按月计息，到期还本还息" />
										</c:if>
										<c:if test="${borrowStyle eq 'endday' }">
											<c:out value="按天计息，到期还本还息" />
										</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>服务回报期限：</strong>
										<c:if test="${borrowStyle eq 'end' }">
											<c:out value="${lockPeriod }个月" />
										</c:if>
										<c:if test="${borrowStyle eq 'endday' }">
											<c:out value="${lockPeriod }天" />
										</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>参考年回报率：</strong> <c:out value="${expectApr }" />%
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>授权服务金额：</strong> <fmt:formatNumber value="${accedeAmount }" pattern="#,##0.00#"/>元
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>订单状态：</strong>
										<c:if test="${orderStatus eq '0' }">
											<c:out value="自动投标中" />
										</c:if>
										<c:if test="${orderStatus eq '2' }">
											<c:out value="自动投标成功" />
										</c:if>
										<c:if test="${orderStatus eq '3' }">
											<c:out value="锁定中" />
										</c:if>
										<c:if test="${orderStatus eq '5' }">
											<c:out value="退出中" />
										</c:if>
										<c:if test="${orderStatus eq '7' }">
											<c:out value="已退出" />
										</c:if>
										<c:if test="${orderStatus eq '80' }">
											<c:out value="80:出借失败(异常中心)" />
										</c:if>
										<c:if test="${orderStatus eq '82' }">
											<c:out value="82:复投失败(异常中心)" />
										</c:if>
										<c:if test="${orderStatus eq '83' }">
											<c:out value="83:复投失败(异常中心)" />
										</c:if>
										<c:if test="${orderStatus eq '90' }">
											<c:out value="90:出借失败(联系管理员)" />
										</c:if>
										<c:if test="${record.orderStatus eq '92' }">
											<c:out value="92:复投失败(联系管理员)" />
										</c:if>
										<c:if test="${record.orderStatus eq '93' }">
											<c:out value="93:复投失败(联系管理员)" />
										</c:if>
										<c:if test="${record.orderStatus eq '99' }">
											<c:out value="99:失败(联系管理员)" />
										</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="">
										<label> <strong>授权服务时间：</strong> <c:out value="${createTime }" />
										</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label for="email" class="control-label"><span style="color:red">*</span>邮箱:</label>
				<input type="text" id="email" value="${email}" class="" pattern="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$"/>
				<p id="email-check" style="color:red;margin-left:40px;margin-top:5px;display:none;">***请检查邮箱格式***</p>
			</div>
			<div class="form-group text-center" style="padding-bottom:20px;">
				<a class="btn btn-o btn-default" style="margin-right:20px;width:82px;" onclick="parent.$.colorbox.close()">取消</a>
				<a class="btn btn-o btn-primary fn-ExportAgreement">确定发送</a>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/hjhaccede/exportagreement.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
