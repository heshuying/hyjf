<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="融资管理费" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty finmanchargeForm.manChargeCd ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty finmanchargeForm.manChargeCd ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 银行列表一览 --%>
					<input type="hidden" name="manChargeCd" id="manChargeCd" value="<c:out value="${finmanchargeForm.manChargeCd }"/>" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<c:if test="${ empty finmanchargeForm.manChargeCd }">
						<div class="form-group">
							<label class="col-sm-2 control-label" >
								类型 <span class="symbol required"></span>
							</label>
							<div class="col-sm-9">
								<select id="chargeTimeType" name="chargeTimeType" class="form-control underline form-select2" style="width:70% " ajaxurl="checkAction" datatype="*" data-placeholder="请选择类型">
									<c:forEach items="${enddayMonthList }" var="enddayMonth" begin="0" step="1" varStatus="status">
										<option value="${enddayMonth.nameCd }" <c:if test="${enddayMonth.nameCd eq finmanchargeForm.chargeTimeType}">selected="selected"</c:if> >
											<c:out value="${enddayMonth.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="chargeTimeType" label="类型"></hyjf:validmessage>
							</div>
						</div>
					</c:if>
					<c:if test="${ !empty finmanchargeForm.manChargeCd }">
						<div class="form-group">
							<label class="col-sm-2 control-label" >
								类型 <span class="symbol required"></span>
							</label>
							<div class="col-sm-9">
								<select class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择类型" disabled="disabled">
									<c:forEach items="${enddayMonthList }" var="enddayMonth" begin="0" step="1" varStatus="status">
										<option value="${enddayMonth.nameCd }" <c:if test="${enddayMonth.nameCd eq finserchargeForm.chargeTimeType}">selected="selected"</c:if> >
											<c:out value="${enddayMonth.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="chargeTimeType" label="类型"></hyjf:validmessage>
							</div>
						</div>
						<input type="hidden" name="chargeTimeType" id="chargeTimeType" value="<c:out value="${finmanchargeForm.chargeTimeType }"/>" />
					</c:if>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="manChargePer"> <span class="symbol required"></span>服务费率 </label>
						<div class="col-sm-10">
							<div class="input-group">
								<input type="text" placeholder="服务费率" id="manChargePer" name="manChargePer" value="<c:out value="${finmanchargeForm.manChargePer}"/>"  class="form-control"
									datatype="/^\d{1,2}(\.\d{1,4})?$/"  errormsg="服务费率必须为数字，整数部分不能超过2位，小数部分不能超过4位！" maxlength="7" />
								<span class="input-group-addon">率</span>
							</div>
							<hyjf:validmessage key="manChargePer" label="服务费率"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>状态 </label>
						<div class="col-sm-10">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="statusOn" name="status" datatype="*" value="0" class="event-categories" ${ ( ( finmanchargeForm.status eq '0' ) or ( empty finmanchargeForm.status ) ) ? 'checked' : ''}> <label for="statusOn">  启用
								</label>
								<input type="radio" id="statusOff" name="status" datatype="*" value="1" class="event-categories" ${finmanchargeForm.status eq '1' ? 'checked' : ''}> <label for="statusOff">  禁用
								</label>
							</div>
						</div>
						<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="remark"> 说明 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="说明" id="remark" name="remark" value="<c:out value="${finmanchargeForm.remark}"/>"  class="form-control" class="form-control" datatype="*1-255" maxlength="255" errormsg="说明 只能是字符汉字，长度1~50个字符！" ignore="ignore" >
							<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
						</div>
					</div>
					
					
					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/finmancharge/finmanchargeInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
