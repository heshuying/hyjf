<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="安融反欺诈查询配置" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty mspForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty mspForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%--  --%>
					<input type="hidden" name="id" id="id" value="<c:out value="${mspForm.id }"/>" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					
					<%-- <div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="configureName"> <span class="symbol required"></span>标的名称</label>
						<div class="col-xs-9">
							<input type="text" name=configureName class="form-control input-sm underline" maxlength="20" value="${mspForm.configureName}" />
						</div>
					</div> --%>
					<div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="configureName">
							<span class="symbol required"></span>标的名称
						</label>
						<div class="col-xs-9">
							<input  type="text" placeholder="标的名称" id="configureName" name="configureName"
								value="${mspForm.configureName}" class="form-control"
								datatype="*1-20" errormsg="标的名称不能超过20个字符！"  ajaxurl="configureNameError">
							<hyjf:validmessage key="configureName" label="标的名称"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<h6 class="col-sm-2 control-label padding-top-5">系统字段</h6>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="serviceType"><span
							class="symbol required"></span>业务类型 </label>
						<div class="col-xs-9">
							<select name="serviceType" class="form-control  form-select2" style="width: 100%">
								<option value=""></option>
								<%-- <option value="01" <c:if test="${utmForm.sourceTypeSrch=='01'}">selected</c:if>> 信用额度(不设置该选项)</option> --%>
								<option value="02" <c:if test="${mspForm.serviceType=='02'}">selected</c:if>>一般借贷</option>
								<option value="03" <c:if test="${mspForm.serviceType=='03'}">selected</c:if>>消费信贷</option>
								<option value="04" <c:if test="${mspForm.serviceType=='04'}">selected</c:if>>循环贷</option>
								<option value="05" <c:if test="${mspForm.serviceType=='05'}">selected</c:if>>其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="loanType"> <span class="symbol required"></span>借款用途</label>
						<div class="col-xs-9">
							<select name="loanType" class="form-control form-select2" style="width: 100%">
							<option value=""></option>
							<option value="01" <c:if test="${mspForm.loanType=='01'}">selected</c:if>>经营</option>
							<option value="02" <c:if test="${mspForm.loanType=='02'}">selected</c:if>>消费</option>
							<option value="99" <c:if test="${mspForm.loanType=='99'}">selected</c:if>>其他</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<h6 class="col-sm-2 control-label padding-top-5">审批字段</h6>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="approvalResult"> <span class="symbol required"></span>审批结果</label>
						<div class="col-sm-9 admin-select">
							<select name="approvalResult" disabled="disabled" class="form-control underline form-select2"  style="width: 100%">
								<option value="01" <c:if test="${mspForm.approvalResult == '01'}">selected</c:if>>审批通过</option>
								<%-- <option value="02" <c:if test="${mspForm.approvalResult == 1}">selected="selected"</c:if>> 审批拒绝</option>	 --%>
								<%-- <option value="04" <c:if test="${mspForm.approvalResult == '04'}">selected</c:if>>重新审批</option>
								<option value="05" <c:if test="${mspForm.approvalResult == '05'}">selected</c:if>>客户取消</option> --%>	
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<%-- <label class="col-sm-2 control-label padding-top-5" for="loanMoney"> <span class="symbol required"></span>合同金额（元）：</label>
						<input type="text" name=loanMoney class="form-control input-sm underline" maxlength="20" value="${mspForm.loanMoney}" /> --%>
						<label class="col-sm-2 control-label padding-top-5" for="loanMoney">
							<span class="symbol required"></span>借款金额
						</label>
						<div class="col-xs-9">
							<input  type="text" placeholder="借款金额" id="loanMoney" name="loanMoney"
								value="${mspForm.loanMoney}" class="form-control"
								datatype="/^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/" errormsg="借款金额必须是数字或小数">
							<hyjf:validmessage key="loanMoney" label="借款金额"></hyjf:validmessage>
						</div>
					</div>
					<div   class="form-group">
									<label class="col-sm-2 control-label" for="creditAddress">
										<span class="symbol required"></span>借款城市
									</label>
									<div class="col-xs-9 admin-select">
										<select  id="creditAddress" name="creditAddress"
											class="form-select2"  style="width: 100%">
											
											<c:forEach items="${mspForm.regionList}" var="item">
												<option value="${item.regionId}"
														<c:if test="${item.regionId==mspForm.creditAddress}">selected</c:if>>${item.regionName }</option>
											</c:forEach>
										</select>
									</div>
									<input type="hidden" name="configureId"  id="configureId" value="" >
						</div>
					<%-- <div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="loanTimeLimit"> <span class="symbol required"></span>借款/还款期数（月）：</label>
						
						<input type="text" name=loanTimeLimit class="form-control input-sm underline" maxlength="20" value="${mspForm.loanTimeLimit}" />
					</div> --%>
					<div class="form-group">
						<label  class="col-sm-2 control-label padding-top-5" for="loanTimeLimit">
							<span class="symbol required"></span>借款/还款期数（月）
						</label>
						<div class="col-xs-9">
							<input  type="text" placeholder="借款期数" id="loanTimeLimit" name="loanTimeLimit"
								value="${mspForm.loanTimeLimit=='' ? 1:mspForm.loanTimeLimit}" class="form-control"
								
								datatype="n" errormsg="借款期数必须是数字">
							<hyjf:validmessage key="loanTimeLimit" label="借款期数"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for=guaranteeType> <span class="symbol required"></span>担保类型</label>
						<div class="col-xs-9 admin-select">
							<select name="guaranteeType" class="form-control underline form-select2"  style="width: 100%">
								<option value=""></option>
								<option value="A" <c:if test="${mspForm.guaranteeType=='A'}">selected</c:if>>抵押</option>
								<option value="B" <c:if test="${mspForm.guaranteeType=='B'}">selected</c:if>>质押</option>
								<option value="C" <c:if test="${mspForm.guaranteeType=='C'}">selected</c:if>>担保</option>
								<option value="D" <c:if test="${mspForm.guaranteeType=='D'}">selected</c:if>>信用</option>
								<option value="E" <c:if test="${mspForm.guaranteeType=='E'}">selected</c:if>>保证</option>
								<option value="Y" <c:if test="${mspForm.guaranteeType=='Y'}">selected</c:if>>其他</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<h6 class="col-sm-2 control-label padding-top-5">债权信息字段</h6>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="unredeemedMoney">
							<span class="symbol required"></span>未偿还本金
						</label>
						<div class="col-xs-9">
							<input  readonly="true" type="text" placeholder="未偿还本金" id="unredeemedMoney" name="unredeemedMoney" maxlength="4" 
								value="${mspForm.unredeemedMoney==''?0:mspForm.unredeemedMoney}" class="form-control"
								datatype="/^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/" errormsg="未偿还本金必须是数字或小数">
							<hyjf:validmessage key="unredeemedMoney" label="未偿还本金"></hyjf:validmessage>
						</div>
					</div>
				    <div class="form-group">
						<label class="col-sm-2 control-label padding-top-5" for="repaymentStatus"> <span class="symbol required"></span>当前还款状态</label>
						<div class="col-sm-9 admin-select">
							<select id="repaymentStatus" name="repaymentStatus" class="form-control underline form-select2" style="width: 100%">
								<option value="04" <c:if test="${mspForm.repaymentStatus == '04'}">selected</c:if>>正常结清</option>	
								<option value="01" <c:if test="${mspForm.repaymentStatus == '01'}">selected</c:if>>正常</option>
								<%-- <option value="02" <c:if test="${mspForm.repaymentStatus == 1}">selected="selected"</c:if>> 逾期中</option>	 --%>
								<option value="03" <c:if test="${mspForm.repaymentStatus == '03'}">selected</c:if>>逾期核销</option>
								
							</select>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/mspconfigure/mspconfigureInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
