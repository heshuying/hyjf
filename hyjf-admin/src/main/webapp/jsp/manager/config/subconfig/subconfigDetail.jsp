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
	<tiles:putAttribute name="pageTitle" value="分账名单" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty subconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改分账名单配置。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty subconfigForm.id ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="id" id="id" value="${subconfigForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="username"> 
								<span class="symbol required"></span>用户名:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="用户名" id="username" name="username" value="<c:out value="${subconfigForm.username}" />"   class="form-control"
									datatype="*1-20" nullmsg="请输入用户名" errormsg="用户名不能超过20个字符！" maxlength="20"
									<c:if test="${not empty subconfigForm.id}">readonly</c:if>>
								<hyjf:validmessage key="username" label="用户名"></hyjf:validmessage>
								<c:if test="${empty subconfigForm.id}">
									<a class="btn btn-o btn-primary fn-Select"><i class="fa fa-check"></i>查询</a>
								</c:if>
								<c:if test="${!empty subconfigForm.id}">
								</c:if>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="truename"> 
								<span class="symbol required"></span>姓名:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="姓名" id="truename" name="truename" value="<c:out value="${subconfigForm.truename}" />"   class="form-control"
									datatype="*1-20" errormsg="姓名不能超过20个字符！" maxlength="20" readonly="readonly">
								<hyjf:validmessage key="truename" label="姓名"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="roleName"> 
								<span class="symbol required"></span>用户角色:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="用户角色" id="roleName" name="roleName" value="<c:out value="${subconfigForm.roleName}" />"   class="form-control"
									datatype="*1-20" errormsg="用户角色不能超过20个字符！" maxlength="20"  readonly="readonly">
								<hyjf:validmessage key="roleName" label="用户角色"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="userType"> 
								<span class="symbol required"></span>用户类型:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="用户类型" id="userType" name="userType" value="<c:out value="${subconfigForm.userType}" />"   class="form-control"
									datatype="*1-20" errormsg="用户类型不能超过20个字符！" maxlength="20" readonly="readonly">
								<hyjf:validmessage key="userType" label="用户类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group" <c:if test="${'出借人' eq subconfigForm.roleName}"> style="display: none"</c:if>>
							<label class="col-sm-2 control-label" for="cooperateNum">
								<span class="symbol required"></span>合作机构编号:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="合作机构编号" id="cooperateNum" name="cooperateNum" value="<c:out value="${subconfigForm.cooperateNum}" />"   class="form-control"
								    <c:choose>
									   <c:when test="${'出借人' eq subconfigForm.roleName}">datatype="*0-10"</c:when>
									   <c:otherwise>datatype="*1-10" </c:otherwise>
								    </c:choose>
									   errormsg="" maxlength="20" readonly="readonly">
								<hyjf:validmessage key="cooperateNum" label="合作机构编号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="bankOpenAccount"> 
								<span class="symbol required"></span>银行开户状态:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="银行开户状态" id="bankOpenAccount" name="bankOpenAccount" value="<c:out value="${subconfigForm.bankOpenAccount}" />"   class="form-control"
									datatype="*1-20" errormsg="银行开户状态不能超过20个字符！" maxlength="20" readonly="readonly">
								<hyjf:validmessage key="bankOpenAccount" label="银行开户状态"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="account"> 
								<span class="symbol required"></span>江西银行电子账号:
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="江西银行电子账号" id="account" name="account" value="<c:out value="${subconfigForm.account}" />"   class="form-control"
									datatype="*1-20" errormsg="江西银行电子账号不能超过20个字符！" maxlength="20" readonly="readonly">
								<hyjf:validmessage key="account" label="江西银行电子账号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">
								<span class="symbol required"></span>用户状态:
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${ ( subconfigForm.status eq 0 ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${ subconfigForm.status eq 1 ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
									<hyjf:validmessage key="status" label="费率状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span class="symbol"></span>说明: </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="remark" name="remark" class="form-control limited" 
									datatype="*0-50" errormsg="说明不能超过50个字符！" maxlength="50" ignore="ignore"><c:out value="${subconfigForm.remark}"></c:out></textarea>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/subconfig/subconfigDetail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
