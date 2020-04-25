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
		<c:set var="jspEditType" value="${empty employeeinfoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改用户。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
				<form id="mainForm" action="${empty employeeinfoForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="id" id="id" value="${employeeinfoForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					
					<div class="form-group">
						<label class="col-sm-2 control-label" for="department"> 
							<span class="symbol required"></span>入职部门
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="部门名称" id="department" name="department"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="***" >
									<hyjf:validmessage key="department" label=""></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="部门名称" id="department" name="department" class="form-control" value="${employeeinfoForm.departmentid}" maxlength="20"
										datatype="n1-20" errormsg="***">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>角色
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="角色" id="level" name="level"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="level" label="角色"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="角色" id="level" name="level" value="${employeeinfoForm.level}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>岗位名称 
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="岗位名称" id="position" name="position"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="position" label="岗位名称"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="岗位名称" id="position" name="position" value="${employeeinfoForm.positionid}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>岗位工薪
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="岗位工薪" id="payroll" name="payroll"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="payroll" label="payroll"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="岗位工薪" id="payroll" name="payroll" value="${employeeinfoForm.payroll}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="hydname"> 
							<span class="symbol required"></span>汇盈贷账号 
						</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="汇盈贷账号" id="hydname" name="hydname" value="${employeeinfoForm.hyd_name}"  class="form-control"
										datatype="n1-20" errormsg="汇盈贷账号不能超过20个字符！" maxlength="20" >
									<hyjf:validmessage key="username" label="用户名"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text"  value="${employeeinfoForm.hyd_name}"  class="form-control" disabled>
									<input type="hidden" placeholder="汇盈贷账号" id="hydname" name="hydname" value="${employeeinfoForm.hyd_name}" >
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="realname"> 
							<span class="symbol required"></span>姓名
						</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="姓名" id="realname" name="realname" value="${employeeinfoForm.user_realname}"  class="form-control"
										datatype="n1-20" errormsg="汇盈贷账号不能超过20个字符！" maxlength="20" >
									<hyjf:validmessage key="username" label="姓名"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text"  value="${employeeinfoForm.user_realname}"  class="form-control" disabled>
									<input type="hidden" placeholder="姓名" id="realname" name="realname" value="${employeeinfoForm.user_realname}" >
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="hydname"> 
							<span class="symbol required"></span>身份证号
						</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="身份证号" id="idcard" name="idcard" value="${employeeinfoForm.idcard}"  class="form-control"
										datatype="n1-20" errormsg="身份证号不能超过20个字符！" maxlength="20" >
									<hyjf:validmessage key="username" label="用户名"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text"  value="${employeeinfoForm.idcard}"  class="form-control" disabled>
									<input type="hidden" placeholder="身份证号" id="idcard" name="idcard" value="${employeeinfoForm.idcard}" >
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>性别
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="性别" id="sex" name="sex"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="level" label="角色"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="性别" id="sex" name="sex" value="${employeeinfoForm.sex}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>年龄
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="年龄" id="age" name="age"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="age" label="年龄"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="年龄" id="age" name="age" value="${employeeinfoForm.age}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>手机
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="手机" id="mobile" name="mobile"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="mobile" label="手机"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="手机" id="mobile" name="mobile" value="${employeeinfoForm.mobile}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>户口所在地
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="户口所在地" id="acc_address" name="acc_address"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="acc_address" label="户口所在地"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="户口所在地" id="acc_address" name="acc_address" value="${employeeinfoForm.acc_address}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>是否兼职
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="是否兼职" id="temporary" name="temporary"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="temporary" label="是否兼职"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="是否兼职" id="temporary" name="temporary" value="${employeeinfoForm.temporary}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>工资卡开户银行
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="工资卡开户银行" id="bank_address" name="bank_address"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="bank_address" label="工资卡开户银行"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="工资卡开户银行" id="bank_address" name="bank_address" value="${employeeinfoForm.bank_address}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>工资卡开户姓名
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="工资卡开户姓名" id="bank_user" name="bank_user"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="bank_user" label="工资卡开户姓名"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="工资卡开户姓名" id="bank_user" name="bank_user" value="${employeeinfoForm.bank_user}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>工资卡开户账号
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="工资卡开户账号" id="bank_num" name="bank_num"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="bank_num" label="工资卡开户账号"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="工资卡开户账号" id="bank_num" name="bank_num" value="${employeeinfoForm.bank_num}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="user_email"> 
							<span class="symbol required"></span>邮箱
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="邮箱" id="user_email" name="user_email"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="user_email" label="邮箱"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="邮箱" id="user_email" name="user_email" value="${employeeinfoForm.user_email}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="reference"> 
							<span class="symbol required"></span>入职引荐人
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="入职引荐人" id="reference" name="reference"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="reference" label="入职引荐人"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="入职引荐人" id="reference" name="reference" value="${employeeinfoForm.reference}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
		<%-- 		<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>社保归属地
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="社保归属地" id="level" name="level"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="level" label="社保归属地"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="社保归属地" id="level" name="level" value="${employeeinfoForm.level}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>		--%>	
					<div class="form-group">
						<label class="col-sm-2 control-label" for="title"> 
							<span class="symbol required"></span>入职日期
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="入职日期" id="entrydate" name="entrydate"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="entrydate" label="入职日期"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="入职日期" id="entrydate" name="entrydate" value="${employeeinfoForm.entrydate}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="education"> 
							<span class="symbol required"></span>学历
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="学历" id="education" name="education"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="education" label="学历"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="学历" id="education" name="education" value="${employeeinfoForm.education}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="specialty"> 
							<span class="symbol required"></span>专业
						</label>
						<div class="col-sm-3">
							<c:choose>
								<c:when test="${empty employeeinfoForm.id}">
									<input type="text" placeholder="专业" id="specialty" name="specialty"  class="form-control" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！" >
									<hyjf:validmessage key="specialty" label="专业"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="专业" id="specialty" name="specialty" value="${employeeinfoForm.specialty}" maxlength="20"
										datatype="n1-20" errormsg="排序的必须是小于4位的数字！">
								</c:otherwise>
							</c:choose>
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
		<script type='text/javascript' src="${webRoot}/jsp/employee/employeeinfo/employeeinfo_edit.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
