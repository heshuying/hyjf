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
	<tiles:putAttribute name="pageTitle" value="汇天利管理" />
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
			value="修改"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="updateAction"
						method="post" role="form" class="form-horizontal">
						<%-- 汇天利列表一览 --%><input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name"> <span
								class="symbol required"></span>名称
							</label>
							<div class="col-sm-10">
								<select name="name" id="name"
									class="form-control underline form-select2" datatype="*"
									errormsg="名称必选！">
									<option value="年化收益率"
										<c:if test="${htlconfigForm.name=='年化收益率'}">selected="selected"</c:if>>出借利率率</option>
									<option value="产品总额"
										<c:if test="${htlconfigForm.name=='产品总额'}">selected="selected"</c:if>>产品总额</option>
									<option value="单户上限"
										<c:if test="${htlconfigForm.name=='单户上限'}">selected="selected"</c:if>>单户上限</option>
									<option value="单户下限"
										<c:if test="${htlconfigForm.name=='单户下限'}">selected="selected"</c:if>>单户下限</option>
									<option value="可赎回"
										<c:if test="${htlconfigForm.name=='可赎回'}">selected="selected"</c:if>>可赎回</option>
									<option value="可转让"
										<c:if test="${htlconfigForm.name=='可转让'}">selected="selected"</c:if>>可转让</option>
								</select>
								<hyjf:validmessage key="type" label="汇天利类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="value"> <span
								class="symbol required"></span>数值
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="数值" id="value" name="value"
									value="${htlconfigForm.value}" class="form-control"
									maxlength="20"
									datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/"
									errormsg="请输入正确的数值,只能是数字!">
								<hyjf:validmessage key="type" label="汇天利标题"></hyjf:validmessage>
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
			src="${webRoot}/jsp/manager/config/htlconfig/htlconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
