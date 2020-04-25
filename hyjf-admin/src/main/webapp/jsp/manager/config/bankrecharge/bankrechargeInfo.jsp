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
	<tiles:putAttribute name="pageTitle" value="快捷充值限额配置" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty bankrechargeForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty bankrechargeForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id" value="${bankrechargeForm.id }" /> 
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span class="symbol required"></span>银行</label>
							<div class="col-xs-9">
								<select id="bankId" name="bankId" class="form-control" datatype="*" errormsg="请选择银行！" ajaxurl="checkAction?id=${bankrechargeForm.id }" >
									<option value="" >请选择银行</option>
									<c:if test="${!empty bankList}">
										<c:forEach items="${bankList }" var="bank" begin="0" step="1" varStatus="status">
											<option value="${bank.id }"  data-class="fa" <c:if test="${bankrechargeForm.bankId == bank.id }">selected="selected"</c:if> >${bank.name }</option>
										</c:forEach>
									</c:if>
								</select>
								<hyjf:validmessage key="bankId" label="银行"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="personalCredit">
								<span class="symbol required"></span>接入方式
							</label>
							<div class="col-xs-9">
								<select id="accessCode" name="accessCode" class="form-control" datatype="*" errormsg="请选择接入方式！" >
									<option value="" disabled>接入方式</option>
									<option value="0"  data-class="fa" <c:if test="${bankrechargeForm.accessCode == 0 }">selected="selected"</c:if> >全国</option>
								</select>
								<hyjf:validmessage key="accessCode" label="接入方式"></hyjf:validmessage>
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="personalCredit">
								<span class="symbol required"></span>银行卡类型
							</label>
							<div class="col-xs-9">
								<select id="bankType" name="bankType" class="form-control" datatype="*" errormsg="请选择银行卡类型！" >
									<option value="" disabled>银行卡类型</option>
									<option value="0"  data-class="fa" <c:if test="${bankrechargeForm.bankType == 0 }">selected="selected"</c:if> >借记卡</option>
								</select>
								<hyjf:validmessage key="bankType" label="银行卡类型"></hyjf:validmessage>
							</div>
						</div>
	
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="singleQuota"> <span
								class="symbol required"></span>单笔充值限额（元）
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="单笔充值限额" id="singleQuota" name="singleQuota"
									value="${bankrechargeForm.singleQuota}" class="form-control"
									datatype="/^((([1-9]{1}[0-9]{1,12}){1})|(((([1-9]{1}[0-9]{1,10}){1})\.([0-9]{1,2}){1})|([0]{1}\.[1-9]{1}[0-9]?)|([0]{1}\.[0-9]{1}[1-9]{1})))$/" errormsg="单笔充值限额 只能是数字类型，长度1~13位！" maxlength="13">
								<hyjf:validmessage key="singleQuota" label="单笔充值限额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="singleCardQuota"> 
							<span class="symbol required"></span>单卡单日限额（元）
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="单卡单日限额" id="singleCardQuota" name="singleCardQuota"
									value="${bankrechargeForm.singleCardQuota}" class="form-control"
									datatype="/^((([1-9]{1}[0-9]{1,12}){1})|(((([1-9]{1}[0-9]{1,10}){1})\.([0-9]{1,2}){1})|([0]{1}\.[1-9]{1}[0-9]?)|([0]{1}\.[0-9]{1}[1-9]{1})))$/" errormsg="单卡单日限额 只能是数字类型，长度1~13位！" maxlength="13">
								<hyjf:validmessage key="singleCardQuota" label="单卡单日限额"></hyjf:validmessage>
							</div>
						</div>	
						
						<div class="form-group">
							<label class="col-xs-3 control-label" > <span class="symbol required"></span>状态 </label>
							<div class="col-xs-9 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="statusOn" name="status" value="0"  datatype="*" ${bankrechargeForm.status == '0' ? 'checked' : ''}> <label for="statusOn"> 启用 </label> 
									<input type="radio" id="statusOff" name="status" value="1" datatype="*" ${bankrechargeForm.status == '1' ? 'checked' : ''}> <label for="statusOff"> 禁用 </label>
									<hyjf:validmessage key="status" label="用户状态"></hyjf:validmessage>
								</div>
							</div>
						</div>

						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a> 
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
			span.select2{width:492px !important;}
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

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/bankrecharge/bankrechargeInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
