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
	<tiles:putAttribute name="pageTitle" value="确定已交保证金" />
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
			<input type="hidden" name=borrowNid id="borrowNid" value="${ fireInfo.borrowNid}"/>
			<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
			<input type="hidden" id="success" value="${success}" />
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
							<table class="center" style="width:95%">
								<tr>
									<td align="right" class="tlabel"><span class="symbol required"></span>发标方式：</td>
									<td align="left"> 									
										<div class="radio clip-radio radio-primary radio-inline">
											<input type="radio" id="verifyStatus2" class="fn-Radio" name="verifyStatus" <c:if test="${ ( fireInfo.verifyStatus eq '2' ) or ( fireInfo.verifyStatus eq '1' ) }">checked="checked"</c:if> <c:if test="${canUpdate}">disabled="disabled"</c:if> value="2">
											<label for="verifyStatus2"> 暂不发标 </label>
											<%-- <c:if test="${ engineFlag eq '0' }">  --%>
												<input type="radio" id="verifyStatus3" class="fn-Radio" name="verifyStatus" <c:if test="${ ( fireInfo.verifyStatus eq '3' ) }">checked="checked"</c:if> <c:if test="${canUpdate}">disabled="disabled"</c:if> value="3">
												<label for="verifyStatus3"> 定时发标 </label>
											<%-- </c:if> --%>
											<input type="radio" id="verifyStatus4" class="fn-Radio" name="verifyStatus" <c:if test="${ ( fireInfo.verifyStatus eq '4' ) }">checked="checked"</c:if> <c:if test="${canUpdate}">disabled="disabled"</c:if> value="4">
											<label for="verifyStatus4"> 立即发标 </label>
										</div>
									</td>
								</tr>
								<tr id="ontimeCtrl1">
									<td align="right" class="tlabel"><span class="symbol required"></span>设定发标时间：</td>
									<td align="left">
										<span class="input-icon input-icon-right">
											<input type="text" name="ontime" id="ontime" class="form-control input-sm" <c:if test="${canUpdateTime}">disabled="disabled"</c:if>
												value="<c:out value="${ ontime }" />"  maxlength="16" datatype="*1-16" nullmsg="未填写发标时间"
												onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm', errDealMode: 1})"/>
											<i class="fa fa-calendar"></i>
										</span>
									<hyjf:validmessage key="ontime"></hyjf:validmessage>
									</td>
								</tr>
							</table>
							<hr>
							<table class="center" style="width:95%">
								<tr>
									<td align="right" class="tlabel">项目编号：</td>
									<td align="left"> <c:out value="${ fireInfo.borrowNid}" /></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">项目名称：</td>
									<td align="left"> <c:out value="${ fireInfo.name}" /></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">借款金额：</td>
									<td align="left"> <c:out value="${ fireInfo.account}" />元</td>
								</tr>
								<tr>
									<td align="right" class="tlabel">出借利率：</td>
									<td align="left"> <c:out value="${ fireInfo.borrowApr}" />%</td>
								</tr>
								<tr>
									<td align="right" class="tlabel">借款期限：</td>
									<td align="left"> <c:out value="${ fireInfo.borrowPeriod}" /><c:if test="${ fireInfo.borrowStyle eq 'endday' }">天</c:if><c:if test="${ fireInfo.borrowStyle ne 'endday' }">个月</c:if></td>
								</tr>
								<tr>
									<td align="right" class="tlabel">添加时间：</td>
									<td align="left"> <c:out value="${ addtime}" /></td>
								</tr>
							</table>
							<br />
							<div class="form-group margin-bottom-0" align="center">
								<div class="col-sm-offset-2 col-sm-10">
									<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 
									<span id="confirm">
										<%-- <c:if test="${ engineFlag eq '0' }"> --%>
											<c:if test="${ ( fireInfo.verifyStatus eq '1' ) }">定时发标</c:if>
										<%-- </c:if> --%>
										<c:if test="${ ( fireInfo.verifyStatus eq '2' ) or ( empty fireInfo.verifyStatus )}">立即发标</c:if>
										<c:if test="${ ( fireInfo.verifyStatus eq '3' ) }">暂不发标</c:if>
									</span></a>
									<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消</a>
								</div>
							</div>
						</div>
					</div>
				</div>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowfirst/borrowfire.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
