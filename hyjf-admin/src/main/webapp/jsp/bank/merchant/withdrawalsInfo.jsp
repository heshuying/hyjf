<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="商户子账户提现" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
		.panel-title {
			font-family: "微软雅黑"
		}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll">
					<form id="mainForm" action="withdrawals" method="post" role="form"  target="_parent"
						class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input
							type="hidden" name="pageToken"
							value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden"
							id="success" value="${success}" />
						<input type="hidden" name="accountCode" id="account"
							value="<c:out value="${account }"/>" />
						
						<div class="form-group">
							<label class="col-xs-3 control-label">
								可用金额：
							</label>
							<div class="col-xs-9">
								<c:out value="${availableBalance }"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label">
								电子账户：
							</label>
							<div class="col-xs-9">
								<c:out value="${account }"/>
							</div>
						</div>	
						
						<div class="form-group">
							<label class="col-xs-3 control-label">
								预留手机号：
							</label>
							<div class="col-xs-9">
								<c:out value="${info.mobile }"/>
							</div>
						</div>	
						<div class="form-group">
							<label class="col-xs-3 control-label">
								银行卡号：
							</label>
							<div class="col-xs-9">
								<c:out value="${info.bankCard }"/>
							</div>
						</div>	
						<div class="form-group">
							<label class="col-xs-3 control-label">
								姓名：
							</label>
							<div class="col-xs-9">
								<c:out value="${info.accountName }"/>
							</div>
						</div>		
						<div id="chargeTimeDiv" class="form-group">
							<label class="col-xs-3 control-label  padding-top-5" for="amount">
								<span class="symbol required"></span>提现金额：
							</label>
							<div class="col-xs-9">
								<input type="text" id="amount" name="amount" class="form-control input-sm" 
										datatype="/^([1-9]{1,7})$/|/^([1-9]\d{1,7}|0)(\.\d{1,2})?$/"
										errormsg="请输入面值格式" ajaxurl="checkAction?accountCode=${account}"/>
								<hyjf:validmessage key="amount" label="名称"></hyjf:validmessage>
							</div>
						</div>	
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-3 col-xs-9">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 提现</a> <a
									class="btn btn-o btn-primary fn-Cancel"><i
									class="fa fa-close"></i> 返回</a>
							</div>
						</div>
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/bank/merchant/withdrawalsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
