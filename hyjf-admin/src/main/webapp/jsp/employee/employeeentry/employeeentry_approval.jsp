
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,员工信息,离职记录', ',')}" var="functionPaths" scope="request"></c:set>

<%-- <shiro:hasPermission name="accountdetail:VIEW"> --%>
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="离职员工" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox { width:20px;margin-right:0!important;overflow:hidden }
			</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">离职审批</h1>
			<span class="mainDescription">  </span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								
								<a class="btn btn-o btn-primary btn-sm fn-Refresh"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表"><i class="fa fa-refresh"></i> 刷新</a>
								<shiro:hasPermission name="accountdetail:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel"><i class="fa fa-Export"></i> 导出Excel</a>
								</shiro:hasPermission>		
										
								<a class="btn btn-primary btn-sm pull-right fn-searchPanel" data-toggle="tooltip" tooltip-placement="top" data-original-title="查询条件" data-toggle-class="active"
									data-toggle-target="#searchable-panel"><i class="fa fa-search"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
			<c:forEach items="${employeeentry1}" var="list">
				<tr>
					<td> 入职部门 </td>
					<td> ${list.firstdepart}
						 ${list.seconddepart}
						 ${list.thirddepart} 
					</td>
				</tr>
				<tr>
					<td> 角色 </td>
					<td> <c:if test="${list.level == 1}"> 主管 </c:if>
						 <c:if test="${list.level == 2}"> 员工 </c:if>
					</td>
				</tr>
				<tr>
					<td> 岗位名称 </td>
					<td> ${list.pname} </td>
				</tr>
				<tr>
					<td> 岗位工薪 </td>
					<td> ${list.payroll} </td>
				</tr>
				<tr>
					<td> 姓名 </td>
					<td> ${list.user_realname} </td>
				</tr>
				<tr>
					<td> 性别 </td>
					<td> <c:if test="${list.sex == 0}"> 保密 </c:if>
						 <c:if test="${list.sex == 1}"> 男 </c:if>  
						 <c:if test="${list.sex == 2}"> 女 </c:if>
					</td>
				</tr>
				<tr>
					<td> 年龄 </td>
					<td> ${list.age} </td>
				</tr>
				<tr>
					<td> 手机 </td>
					<td> ${list.mobile} </td>
				</tr>
				<tr>
					<td> 身份证号码 </td>
					<td> ${list.idcard} </td>
				</tr>
				<tr>
					<td> 户口所在地 </td>
					<td> ${list.acc_address} </td>
				</tr>
				<tr>
					<td> 是否兼职 </td>
					<td> <c:if test="${list.temporary == 1}"> 兼职 </c:if>
						 <c:if test="${list.temporary == 2}"> 正式员工 </c:if> 
					</td>
				</tr>			
				<tr>
					<td> 汇盈贷账号 </td>
					<td> ${list.hyd_name} </td>
				</tr>
				<tr>
					<td> 工资卡开户银行 </td>
					<td> ${list.bank_address} </td>
				</tr>
				<tr>
					<td> 工资卡开户姓名  </td>
					<td> ${list.bank_user} </td>
				</tr>
				<tr>
					<td> 工资卡开户账号 </td>
					<td> ${list.bank_num} </td>
				</tr>
				<tr>
					<td> 邮箱 </td>
					<td> ${list.user_email} </td>
				</tr>
				<tr>
					<td> 推荐人 </td>
					<td> ${list.reference} </td>
				</tr>
				<tr>
					<td> 社保归属地 </td>
					<td> ${list.cityid} </td>
				</tr>
				<tr>
					<td> 入职日期 </td>
					<td><fmt:formatDate value="${list.entrydate}" pattern="yyyy-MM-dd"/></td>
				</tr>
				<tr>
					<td> 学历 </td>
					<td> ${list.education} </td>
				</tr>
				<tr>
					<td> 专业 </td>
					<td> ${list.specialty} </td>
				</tr>
				<tr>
					<td> 备注 </td>
					<td> <input type="text" id="remark"/> </td>
				</tr>																						
				</c:forEach>
			</table>
							<h1> 
								<a href="entrypass?id=${list.id }"> 通过 </a>
								<a href="employeeentry_list"> 驳回(暂时设置的返回) </a>
							</h1>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/employee/employeedimission/employeedimission_detail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
<%-- </shiro:hasPermission> --%>