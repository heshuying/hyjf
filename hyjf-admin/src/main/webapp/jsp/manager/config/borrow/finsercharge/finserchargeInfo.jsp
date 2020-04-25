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
	<tiles:putAttribute name="pageTitle" value="添加项目类型" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	.admin-select .select2-container { 
		width: 100% !important;
	}
	.admin-select .select2-container--default .select2-selection--single { 
		border-radius: 0px;
		border: 1px solid #BBBAC0 !important;
	}
	.admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single { 
		height: 34px;
		line-height:34px;
	}
	.admin-select .select2-container .select2-selection--single .select2-selection__rendered {
		padding-left: 4px;
	}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty finserchargeForm.chargeCd ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">				
					<form id="mainForm" class="form-horizontal" action="${empty finserchargeForm.chargeCd ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="chargeCd" id="chargeCd" value="${finserchargeForm.chargeCd }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
                        
                        
                        <div class="form-group">
							<label class="col-sm-2 control-label" >
								项目类型 <span class="symbol required"></span>
							</label>
							<div class="col-sm-9">
								<select id="i_projectType" name="projectType" nullmsg="未指定项目类型！" class="form-control underline form-select2" style="width:70% " datatype="*" data-placeholder="请选择类型" >
									<option value=""></option>
										<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
											<option value="${borrowProjectType.borrowCd }" data-borrowclass="${borrowProjectType.borrowClass }"  data-increasemoney="${borrowProjectType.increaseMoney}" data-interestcoupon="${borrowProjectType.interestCoupon }"  data-tastemoney="${borrowProjectType.tasteMoney }"  data-min="${ borrowProjectType.investStart}" data-max="${ borrowProjectType.investEnd}"
												<c:if test="${borrowProjectType.borrowCd eq finserchargeForm.projectType}">selected="selected"</c:if>>
												<c:out value="${borrowProjectType.borrowName }"></c:out></option>
										</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" >
								类型 <span class="symbol required"></span>
							</label>
							<div class="col-sm-9">
								<select id="chargeTimeType" name="chargeTimeType" class="form-control underline form-select2" style="width:70% " datatype="*" onchange="change(this)" data-placeholder="请选择类型" flagurl="checkAction?chargeCd=<c:out value="${finserchargeForm.chargeCd}"></c:out>" ajaxurl="checkAction?chargeCd=<c:out value="${finserchargeForm.chargeCd}"></c:out>&projectType=${finserchargeForm.projectType}">
									<option value=""></option>
									<c:forEach items="${enddayMonthList }" var="enddayMonth" begin="0" step="1" varStatus="status">
										<option value="${enddayMonth.nameCd }" <c:if test="${enddayMonth.nameCd eq finserchargeForm.chargeTimeType}">selected="selected"</c:if> >
											<c:out value="${enddayMonth.name }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="chargeTimeType" label="类型"></hyjf:validmessage>
								<hyjf:validmessage key="chargeTimeType-repeat" ></hyjf:validmessage>
							</div>
						</div>
						<div id="chargeTimeDiv" class="form-group" >
							<label class="col-sm-2 control-label" for="borrowClass"> 
								<span class="symbol required"></span>期限:
							</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" id="chargeTime" name="chargeTime" value="<c:out value="${finserchargeForm.chargeTime}"></c:out>"  
										class="form-control" datatype="n1-10" errormsg="期限应该输入1~10个数字！" maxlength="10" onkeyup="change(this)" flagurl="checkAction?chargeCd=<c:out value="${finserchargeForm.chargeCd}"></c:out>" ajaxurl="checkAction?chargeCd=<c:out value="${finserchargeForm.chargeCd}"></c:out>&projectType=${finserchargeForm.projectType}&borrowStyle=${finserchargeForm.chargeCd}"/>
									<span class="input-group-addon">天/月</span>
								</div>
								<hyjf:validmessage key="chargeTime" label="期限"></hyjf:validmessage>
								<hyjf:validmessage key="onlyOneMonth" ></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="borrowClass"> 
								<span class="symbol required"></span>费率:
							</label>
							<div class="col-sm-9">
								<div class="input-group">
									<input type="text" placeholder="费率" id="chargeRate" name="chargeRate" value="<c:out value="${finserchargeForm.chargeRate}"></c:out>"  class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="费率必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
									<span class="input-group-addon">率</span>
								</div>
								<hyjf:validmessage key="chargeRate" label="费率"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span class="symbol"></span>说明 </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="remark" name="remark" class="form-control limited" 
									datatype="*1-255" errormsg="说明不能超过255个字符！" maxlength="255" ignore="ignore"><c:out value="${finserchargeForm.remark}"></c:out></textarea>
								<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="status"> 
								<span class="symbol required"></span>费率状态
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${ ( empty finserchargeForm.status || finserchargeForm.status eq 0 ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${ finserchargeForm.status eq 1 ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
									<hyjf:validmessage key="status" label="费率状态"></hyjf:validmessage>
								</div>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/finsercharge/finserchargeInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
