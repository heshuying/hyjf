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
	<tiles:putAttribute name="pageTitle" value="用户转账" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="addTransfer" method="post"  role="form" class="form-horizontal" >
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="outUserName"> 
								<span class="symbol required"></span>用户账号:
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="用户账号" class="form-control input-sm" id="outUserName" name="outUserName" value="${transferForm.outUserName}"
										onpaste="return false" datatype="s2-18" errormsg="用户账号错误！" ajaxurl="checkTransfer">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0">
								<span class="symbol"></span>用户余额: 
							</label>
							<div class="col-xs-10">
								<span id ="balance" class="badge badge-inverse"> ${transferForm.balance} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="transferAmount"> 
								<span class="symbol required"></span>转账金额:
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="转账金额" class="form-control input-sm" id="transferAmount" name="transferAmount" value="${transferForm.transferAmount}" maxlength="12"
										datatype="/^((([1-9]{1}[0-9]{0,9}){1})|(((([1-9]{1}[0-9]{0,9}){1})\.([0-9]{1,2}){1})|([0]{1}\.[1-9]{1}[0-9]?)|([0]{1}\.[0-9]{1}[1-9]{1})))$/" errormsg="金额输入错误！">
							</div>
						</div>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-2 control-label text-right padding-right-0" for="remark"> 
								<span class="symbol required"></span>说明
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" id="remark" name="remark" class="form-control input-sm"
										maxlength="60" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为60字符" >${transferForm.remark}</textarea>
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
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/transfer/transfer.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
