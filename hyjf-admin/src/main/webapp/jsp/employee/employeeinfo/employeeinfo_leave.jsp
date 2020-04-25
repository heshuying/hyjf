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
	<tiles:putAttribute name="pageTitle" value="用户编辑" />
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
		<c:set var="jspEditType" value=""></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
				</p>
				<hr/>
				<div class="panel-scroll height-535">
				<form id="mainForm" action="employeeinfo_list"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="id" id="id" value="${employeeinfoForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					
					<div class="form-group">
						<label class="col-sm-2 control-label" for="type" name="leaveType" id="leaveType"> 
							<span class="symbol required"></span>离职类型
						</label>
						<div class="col-sm-3">
							<input type="text" placeholder="离职类型" id="department" name="department" class="form-control" value="" maxlength="20"
										datatype="s1-20" errormsg="***">		
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title" name="leaveReason" id="leaveReason"> 
							<span class="symbol required"></span>离职原因
						</label>
						<div class="col-sm-3">
							<input type="text" placeholder="离职原因" id="level" name="level" value="" maxlength="20"
										datatype="n1-4" errormsg="排序的必须是小于4位的数字！">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title" name="leaveDay" id="leaveDay"> 
							<span class="symbol required"></span>离职日期
						</label>
						<div class="col-sm-3">
							<input type="text" placeholder="离职日期" id="position" name="position" value="" maxlength="20"
										datatype="n1-4" errormsg="排序的必须是小于4位的数字！">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title" name="leaveSalaryDay" id="leaveSalaryDay"> 
							<span class="symbol required"></span>工薪截止日
						</label>
						<div class="col-sm-3">
							<input type="text" placeholder="工薪截止日" id="quit" name="quit" value="" maxlength="20"
										datatype="n1-4" errormsg="排序的必须是小于4位的数字！">
						</div>
					</div>
				
					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm" href="leaveStaffOK?id=${employeeinfoForm.id  }"><i class="fa fa-check"></i> 提交</a>
							<a class="btn btn-o btn-primary fn-Cancel" href="employeeinfo_list"><i class="fa fa-close"></i> 取 消</a>
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
		<script type='text/javascript' src="${webRoot}/jsp/employee/employeeinfo/employeeinfo_leave.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
