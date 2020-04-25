<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,配置设置,充值手续费收取方式配置', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="openaccountenquiry:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">开户掉单</h1>
			<span class="mainDescription">本功能可以修复开户掉单。</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
			<shiro:hasPermission name="openaccountenquiry:VIEW"> 
		
		<div class="panel panel-white" style="margin: 0">
					<div class="panel-body" style="margin: 0 auto">
						<div class="panel-scroll height-430">
							 <form id="mainForm" action="" method="post" role="form" class="form-horizontal"> 
								<input type="hidden" name="id" id="id" value="${feeFromForm.id }" />
								 <input type="hidden" name="userId" id="userId" value="" />
								 <input type="hidden" name="roleId" id="roleId" value="" />

								<div class="form-group">
									<label class="col-sm-2 control-label" for="title">  </label>
									<div class="col-sm-10"></div>
								</div>

								<div class="form-group">
									<label class="col-sm-2 control-label"> <span class="symbol required"></span>查询方式
									</label>
									<div class="col-sm-10">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="siteStatusOn" name="num" datatype="*" value="1" checked class="event-categories ipt-change" ${feeFromForm.value == '1' ? 'checked' : ''}> 
											<label for="siteStatusOn"> 按手机号查询</label> 
											<input type="radio" id="siteStatusOff" name="num" datatype="*" value="2" class="event-categories ipt-change" ${feeFromForm.value == '2' ? 'checked' : ''}> 
											<label for="siteStatusOff"> 按身份证号查询 </label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label for="lastname" class="col-sm-2 control-label"><span class="symbol required"></span>查询号码</label>
									<div class="col-sm-10">
										<input type="text" name="lastname" class="form-control input-sm" id="lastname" style="width:231px"  value='' placeholder=""  size="22" class="form-control" datatype="*1-20"
											errormsg="请输入正确的号码" maxlength="20" />
										<hyjf:validmessage key="lastname" label="查询号码"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group" id="username-div" style="display:none;">
									<label for="username" class="col-sm-2 control-label"><span class="symbol required"></span>用户名</label>
									<div class="col-sm-10">
										<input type="text" style="width:231px" placeholder="用户名" class="form-control input-sm" id="username" name="username" value="">
										<hyjf:validmessage key="username" label=""></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-labeel">      
									</label>
										<div class="col-sm-10 fn-Modify">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" style="width:231px;text-text-align:center;">查询</a>
										</div>
								</div>
 							</form>
						</div>
					</div>
				</div>

			</shiro:hasPermission>
		</tiles:putAttribute>
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<!-- 弹出层  start -->
			<div class="colobox-dialog-panel" id="queryInfo" tabindex="-1" role="dialog" aria-hidden="true" >
				<div class="modal-dialog" role="document">
					<div class="">
						<div class="modal-body">
							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>查询结果： </label>
								<h4 class="modal-title" id="t_accountStatus"></h4>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>用户名： </label>
								<label class="control-label td-cont" id="t_username"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>姓名： </label>
								<label class="control-label td-cont" id="t_name"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>身份证： </label>
								<label class="control-label td-cont" id="t_idCard"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>手机号： </label>
								<label class="control-label td-cont" id="t_mobile"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>电子账号： </label>
								<label class="control-label td-cont" id="t_accountId"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>开户时间： </label>
								<label class="control-label td-cont" id="t_regTimeEnd"></label>
							</div>

							<div class="form-group">
								<label class="control-label"><span class="symbol"></span>银行角色： </label>
								<label class="control-label td-cont" id="t_roleId"></label>
							</div>

						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default btn-qd">关闭</button>
							<button type="button" class="btn btn-primary btn-qd-tb">提交更改</button>
						</div>
					</div><!-- /.modal-content -->
				</div><!-- /.modal-dialog -->
			</div>
			<!-- 弹出层  end -->
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/openaccountenquiryexception/openaccountenquiryexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
