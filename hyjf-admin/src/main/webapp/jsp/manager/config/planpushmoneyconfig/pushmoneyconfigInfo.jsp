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
		<c:set var="jspEditType" value="${empty pushMoneyConfigForm.ids ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty pushMoneyConfigForm.ids ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="ids" id="ids" value="${pushMoneyConfigForm.ids }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						
						<div class="form-group">
							<label class="col-sm-3 control-label" for="debtPlanTypeName">用户类型:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="debtPlanTypeName" name="debtPlanTypeName" value="线上员工" class="form-control" readonly="readonly" />
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="rate"> <span class="symbol required"></span>月标:</label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" placeholder="月标" id="rate" name="rate" value="<c:out value="${pushMoneyConfigForm.rate}"></c:out>" class="form-control"
										datatype="/^\d{1,2}(\.\d{1,10})?$/"  errormsg="月标必须为数字，整数部分不能超过2位，小数部分不能超过10位！" maxlength="13" />
								</div>
								<hyjf:validmessage key="rate" label="月标"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="status"> 
								<span class="symbol required"></span>状态
							</label>
							<div class="col-sm-5">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="1"  datatype="*" ${ ( pushMoneyConfigForm.status eq '1' ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="status" value="0" datatype="*" ${( empty pushMoneyConfigForm.status || pushMoneyConfigForm.status eq 0 ) ? 'checked' : ''}> <label for="stateOff"> 关闭 </label>
									<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="remark">说明 </label>
							<div class="col-sm-5">
								<textarea placeholder="" id="remark" name="remark" cols="10" rows="4" class="form-control limited" 
									datatype="*" maxlength="50" ignore="ignore" ><c:out value="${pushMoneyConfigForm.remark}"></c:out></textarea>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/planpushmoneyconfig/pushmoneyconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
