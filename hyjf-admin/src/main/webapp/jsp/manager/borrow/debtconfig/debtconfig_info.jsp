<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
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
<%-- 画面功能路径 (ignore)
<c:set value="${fn:split('产品中心,会转让,邮件设置', ',')}" var="functionPaths"
	   scope="request"></c:set>--%>

<shiro:hasPermission name="debtconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
						  flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让配置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				  rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				  rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				  rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				  rel="stylesheet" media="screen">
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇转让配置</h1>
			<span class="mainDescription">本功能可以修改汇转让配置信息。</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
			<c:set var="jspEditType" value="${empty debtConfig.id ? '添加' : '修改'}"></c:set>
			<div class="panel panel-white" style="margin:0">
				<div class="panel-body" style="margin:0 auto">
					<div class="panel-scroll height-430">
						<form id="mainForm" action="updateAction"
							  method="post"  role="form" class="form-horizontal">
								<%-- 活动列表一览 --%>
							<input type="hidden" name="id" id="id" value="${debtConfig.id }" />
							<div class="form-group">
								<label class="col-sm-2 control-label" for="attornRate"> <span class="symbol required"></span>转让服务费率 </label>
								<div class="col-sm-10">
									<input type="text"  id="attornRate" name="attornRate" value="${debtConfig.attornRate}"    datatype="/^(?:0|[1-9][0-9]{0,1}|[0-9]{1,2}[\.][0-9]{0,2})$/"   maxlength="5" errormsg="必须小于100，可以有2位小数" class="form-control" >
									<hyjf:validmessage key="attornRate" label="转让服务费率"></hyjf:validmessage>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="concessionRateDown"> <span class="symbol required"></span>折让率下限 </label>
								<div class="col-sm-10">
									<input type="text"  id="concessionRateDown" name="concessionRateDown" value="${debtConfig.concessionRateDown}"  datatype="/^(?:0|[1-9][0-9]{0,1}|[0-9]{1,2}[\.][0-9]{1,1})$/" maxlength="5"  errormsg="必须小于100，可以有1位小数"  class="form-control">
									<hyjf:validmessage key="concessionRateDown" label="折让率下限"></hyjf:validmessage>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="concessionRateUp"> <span class="symbol required"></span>折让率上限 </label>
								<div class="col-sm-10">
									<input type="text"  id="concessionRateUp" name="concessionRateUp" value="${debtConfig.concessionRateUp}"   datatype="/^(?:0|[1-9][0-9]{0,1}|[0-9]{1,2}[\.][0-9]{1,1})$/" maxlength="5"  errormsg="必须小于100，可以有1位小数"  class="form-control">
									<hyjf:validmessage key="concessionRateUp" label="折让率上限"></hyjf:validmessage>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" > <span class="symbol required"></span>散标债转功能开关</label>
								<div class="col-sm-10">
									<div class="radio clip-radio radio-primary ">
										<input type="radio" id="toggleOn" name="toggle" datatype="*" value="1" class="event-categories" ${debtConfig.toggle == '1' ? 'checked' : ''}> <label for="toggleOn">  开
									</label>
										<input type="radio" id="toggleOff" name="toggle" datatype="*" value="0" class="event-categories" ${debtConfig.toggle == '0' ? 'checked' : ''}> <label for="toggleOff">  关
									</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label" for="closeDes"> <span class="symbol required"></span>关闭提示 </label>
								<div class="col-sm-10">
									<input type="text"  id="closeDes" name="closeDes" value="${debtConfig.closeDes}" datatype="*" maxlength="99"  class="form-control"
										    >
									<hyjf:validmessage key="closeDes" label="关闭提示"></hyjf:validmessage>
								</div>
							</div>
							<div class="form-group margin-bottom-0">
								<div class="col-sm-offset-2 col-sm-10">
									<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i>保存</a>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/debtconfig/debtconfig_info.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
