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
	<tiles:putAttribute name="pageTitle" value="出借（直投类产品）" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430" style="left:10px">
					<form id="mainForm" action="creditTenderAction" method="post" role="form" class="form-horizontal">
						<input type="hidden" id="creditNid" name="creditNid" value="${creditProjectInfo.creditNid}" />
						<input type="hidden" id="accedeOrderId" name="accedeOrderId" value="${accedeOrderId}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImage"> 
								智投编号:
							</label>
							<div class="col-xs-9">
								<c:out value="${planLockCustomize.debtPlanNid}"></c:out>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImageTitle"> 
								智投名称:
							</label>
							<div class="col-xs-9">
							<c:out value="${planLockCustomize.debtPlanName}"></c:out>	
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImageTitle"> 
								订单号:
							</label>
							<div class="col-xs-9">
								<c:out value="${accedeOrderId}"></c:out>	
							</div>
						</div>
						<div class="form-group">
								<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
									用户名:
								</label>
								<div class="col-xs-9">
									<c:out value="${planLockCustomize.userName}"></c:out>
								</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								可用余额:
							</label>
							<div class="col-xs-9">
								<c:out value="${planLockCustomize.accedeBalance}"></c:out>
							</div>
						</div>
						
						<hr/>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								债转编号:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.creditNid}"></c:out>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								项目编号:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.borrowNid}"></c:out>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								项目类型:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.projectTypeName}"></c:out>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								参考年回报率:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.borrowApr}"></c:out>%
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								实际年化:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.actualApr}"></c:out>%
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								还款方式:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.borrowStyle}"></c:out>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								剩余期限:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.remainDays}"></c:out>天
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								剩余本金:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.creditCapitalWait}"></c:out>元
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								剩余利息:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.creditInterestAdvanceWait}"></c:out>元
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
								实际支付:
							</label>
							<div class="col-xs-9">
								<c:out value="${creditProjectInfo.creditCapitalWait + creditProjectInfo.creditInterestAdvanceWait}"></c:out>元
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="money">
								授权服务金额 <span class="symbol required"></span>
							</label>
							<div class="input-group" style="width:40%">
								<input input type="text" maxlength="20" placeholder="授权服务金额" id="money" name="money" class="form-control" datatype="/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/"
									errormsg="授权服务金额 只能是数字长度1~20个字符！" nullmsg="请填写授权服务金额" />
								<span class="input-group-addon">元</span>
							</div>
							<hyjf:validmessage key="money" label="授权服务金额"></hyjf:validmessage>
						</div>
						
					</form>
					<div class="form-group margin-bottom-0" align="center">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认出借</a> <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
		.purMargin {margin: 8px 0;}
		.purMargin input {width: 200px;}
		.margin-bottom-0{float:left;width:100%;margin-top:15px}
		.form-style2{width:100%;margin-left:145px}
		.col-xs-style{margin-bottom:15px}
		</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
		<!-- The Canvas to Blob plugin is included for image resizing functionality -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
		<!-- blueimp Gallery script -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
		<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>


	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/planlock/planInvest.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
