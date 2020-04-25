<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
.panel-title {
	font-family: "微软雅黑"
}
</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty pushmoneyForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty pushmoneyForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${pushmoneyForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="type"> <span
								class="symbol required"></span>产品类型
							</label>
							<div class="col-sm-10">
								<select id="type" name="projectType"
									class="form-control underline form-select2" style="width: 70%"
									datatype="*" data-placeholder="请选择产品类型">
									<option value="1"
										<c:if test="${pushmoneyForm.projectType=='1'}">selected="selected"</c:if>>汇直投</option>
									<option value="2"
										<c:if test="${pushmoneyForm.projectType=='2'}">selected="selected"</c:if>>汇计划</option>
								</select>
								<hyjf:validmessage key="projectType" label="产品类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="type"> <span
								class="symbol required"></span>用户类型
							</label>
							<div class="col-sm-10">
								<select id="type" name="type"
									class="form-control underline form-select2" style="width: 70%"
									datatype="*" data-placeholder="请选择用户类型">
									<option value="线上员工"
										<c:if test="${pushmoneyForm.type=='线上员工'}">selected="selected"</c:if>>线上员工</option>
									<option value="51老用户"
										<c:if test="${pushmoneyForm.type=='51老用户'}">selected="selected"</c:if>>51老用户</option>
								</select>
								<hyjf:validmessage key="type" label="类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="status"> 
								<span class="symbol required"></span>是否发放提成
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="rewardSend" value="1"  datatype="*" <c:if test="${pushmoneyForm.rewardSend eq 1 }" >checked="checked"</c:if> ><label for="stateOn"> 发放 </label> 
									<input type="radio" id="stateOff" name="rewardSend" value="0" datatype="*" <c:if test="${pushmoneyForm.rewardSend eq 0 }" >checked="checked"</c:if> > <label for="stateOff"> 不发放 </label>
									<hyjf:validmessage key="rewardSend" label="是否发放提成"></hyjf:validmessage>
								</div>
							</div>
						</div>
						
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="dayTender"> <span
								class="symbol required"></span>天标
							</label>
							<div class="col-sm-9">
								<input type="text" placeholder="天标" id="dayTender"
									name="dayTender" value="${pushmoneyForm.dayTender}"
									class="form-control" maxlength="20"
									datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/"
									errormsg="请输入正确的天标,只能是数字!">
								<hyjf:validmessage key="type" label="天标"></hyjf:validmessage>
							</div>
							<div class="col-sm-1">
								<span class="symbol required"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="monthTender">
								<span class="symbol required"></span>月标
							</label>
							<div class="col-sm-9">
								<input type="text" placeholder="月标" maxlength="20"
									id="monthTender" name="monthTender"
									value="${pushmoneyForm.monthTender}" class="form-control"
									datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/"
									errormsg="请输入正确的月标,只能是数字！">
								<hyjf:validmessage key="type" label="标题"></hyjf:validmessage>
							</div>
							<div class="col-sm-1">
								<span class="symbol required"></span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span
								class="symbol required"></span>说明
							</label>
							<div class="col-sm-10">
								<textarea datatype="*" errormsg="必填选项" placeholder="说明"
									id="remark" name="remark" class="form-control limited">${pushmoneyForm.remark}</textarea>
							</div>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确 认</a> <a
									class="btn btn-o btn-primary fn-Cancel"><i
									class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/pushmoney/pushmoneyInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
