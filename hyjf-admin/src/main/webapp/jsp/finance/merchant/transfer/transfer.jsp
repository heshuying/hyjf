<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="子账户转账" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="addTransfer" method="post"  role="form" class="form-horizontal" >
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-430 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="outAccountId"> 
								<span class="symbol required"></span>转出子账户:
							</label>
							<div class="col-xs-10">
								<select id="outAccountId" name="outAccountId" class="form-control col-xs-10 underline form-select2" value="${merchanttransferForm.outAccountId}" datatype="*" nullmsg="请选择转出账户！">
									<option value=""></option>
									<c:forEach items="${merchantAccountListOut }" var="merchantAccountOut" begin="0" step="1">
										<option value="${merchantAccountOut.id }"
											<c:if test="${merchantAccountOut.id eq merchanttransferForm.outAccountId}">selected="selected"</c:if>>
											<c:out value="${merchantAccountOut.subAccountName }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="outAccountId" label="转出子账户"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="inAccountId">
								<span class="symbol required"></span>转入子账户: 
							</label>
							<div class="col-xs-10">
								<select id="inAccountId" name="inAccountId" class="form-control col-xs-10 underline form-select2" value="${merchanttransferForm.inAccountId}"  datatype="*" nullmsg="请选择转入账户！">
									<option value=""></option>
									<c:forEach items="${merchantAccountListIn }" var="merchantAccountIn" begin="0" step="1">
										<option value="${merchantAccountIn.id }"
											<c:if test="${merchantAccountIn.id eq merchanttransferForm.inAccountId}">selected="selected"</c:if>>
											<c:out value="${merchantAccountIn.subAccountName }"></c:out>
										</option>
									</c:forEach>
								</select>
								<hyjf:validmessage key="inAccountId" label="转入子账户"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="transferAmount"> 
								<span class="symbol required"></span>转账金额:
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="转账金额" class="form-control input-sm" id="transferAmount" name="transferAmount" value="${merchanttransferForm.transferAmount}" maxlength="10"
										datatype="/^((([1-9]{1}[0-9]{0,9}){1})|(((([1-9]{1}[0-9]{0,9}){1})\.([0-9]{1,2}){1})|([0]{1}\.[1-9]{1}[0-9]?)|([0]{1}\.[0-9]{1}[1-9]{1})))$/" nullmsg="请输入转账金额！" errormsg="转账金额错误，请重新填写！" >
								<hyjf:validmessage key="transferAmount" label="转账金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-2 control-label text-right padding-right-0" for="remark"> 
								<span class="symbol required"></span>说明:
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" id="remark" name="remark" class="form-control input-sm"
										maxlength="50" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为50字符" >${merchanttransferForm.remark}</textarea>
								<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
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
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.select2-container{min-width:100% !important;}
	</style>
	
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/merchant/transfer/transfer.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
