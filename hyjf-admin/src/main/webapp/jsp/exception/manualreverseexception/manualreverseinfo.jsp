<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="申请调账" />
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
		<c:set var="jspEditType" value="添加"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="insertAction" method="post" role="form" class="form-horizontal">

						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<div class="widget">
							<div class="widgetcontent nopadding">
								<div class="form-group">
									<label class="col-sm-2 control-label" for="seqNo"> <span
										class="symbol required"></span>原交易系统跟踪号
									</label>
									<div class="col-sm-10">
										<input type="text" id="seqNo" name="seqNo"
											value="${manualreverseForm.seqNo}" class="form-control"
											datatype="*1-6" errormsg="原交易系统跟踪号长度1-6位！">
										<hyjf:validmessage key="seqNo" label="原交易系统跟踪号"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="bankSeqNo"> <span
										class="symbol required"></span>原交易流水号
									</label>
									<div class="col-sm-10">
										<input type="text" id="seqNo" name="bankSeqNo"
											value="${manualreverseForm.bankSeqNo}" class="form-control"
											datatype="n1-50" errormsg="原交易流水号必须是纯数字！">
										<hyjf:validmessage key="bankSeqNo" label="原交易流水号"></hyjf:validmessage>
									</div>
								</div><%--
								<div class="form-group">
									<label class="col-sm-2 control-label" for="bankSeqNo"> <span
										class="symbol required"></span>原交易订单号
									</label>
									<div class="col-sm-10">
										<input type="text" id="bankSeqNo" name="bankSeqNo"
											value="${manualreverseForm.bankSeqNo}" class="form-control"
											datatype="*1-50" errormsg="原交易订单号长度1-50位！">
										<hyjf:validmessage key="bankSeqNo" label="原交易订单号"></hyjf:validmessage>
									</div>
								</div> --%>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="userName"> <span
										class="symbol"></span>用户名
									</label>
									<div class="col-sm-10">
										<input type="text" name="userName" id="userName" class="form-control input-sm" value='<c:out value="${manualreverseForm.userName}"></c:out>'
											maxlength="30" datatype="s0-50" nullmsg="未填写借款人用户名"  errormsg="借款人用户名长度不能超过30位字符" ajaxurl="isExistsUser"/>
										<hyjf:validmessage key="userName" label="用户名"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="accountId"> <span
										class="symbol"></span>电子账号
									</label>
									<div class="col-sm-10">
										<input type="text" id="accountId"
											name="accountId" value="${manualreverseForm.accountId}"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label" for="amount"> <span
										class="symbol required"></span>操作金额
									</label>
									<div class="col-sm-10">
										<input type="text" id="amount" name="amount"
											datatype="/^\d{1,8}(\.\d{1,2})?$/"  errormsg="操作金额必须为数字，整数部分不超过8位，小数部分不能超过2位！"
											value="${manualreverseForm.amount}" class="form-control">
										<hyjf:validmessage key="amount" label="操作金额"></hyjf:validmessage>
									</div>
								</div>

								<div class="form-group">
									<label class="col-sm-2 control-label"> <span
										class="symbol required"></span>操作类型
									</label>
									<div class="col-sm-10">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="statusOn" name="type" datatype="*"
												value="0" class="event-categories"
												${manualreverseForm.type == '0' ? 'checked' : ''}> <label
												for="statusOn"> 调账调增 </label> <input type="radio"
												id="statusOff" name="type" datatype="*" value="1"
												class="event-categories"
												${manualreverseForm.type == '1' ? 'checked' : ''}> <label
												for="statusOff"> 调账调减 </label>
										</div>
									</div>
									<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-sm-offset-2 col-sm-10">
										<a class="btn btn-o btn-primary fn-Confirm"><i
											class="fa fa-check"></i> 提 交</a> 
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript" src="${webRoot}/jsp/exception/manualreverseexception/manualreverseinfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
