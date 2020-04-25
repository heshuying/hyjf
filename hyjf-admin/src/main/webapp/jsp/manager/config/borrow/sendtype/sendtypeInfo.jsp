<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="银行管理" />
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
		<c:set var="jspEditType" value="${empty sendtypeForm.sendCd ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty sendtypeForm.sendCd ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 银行列表一览 --%>
					<input type="hidden" name="modifyFlag" id="modifyFlag" value="<c:out value="${ modifyFlag }" />" />
					<input type="hidden" id="success" value="<c:out value="${success}" />"  />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="sendName"> <span class="symbol required"></span>编号 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="编号" id="sendCd" name="sendCd" value="<c:out value="${sendtypeForm.sendCd}" />"  <c:if test="${ modifyFlag eq 'A' }"> ajaxurl="checkAction" </c:if><c:if test="${ modifyFlag eq 'M' }"> readonly="readonly" </c:if>  class="form-control"
								maxlength="50" datatype="/^\w+$/" errormsg="编号 只能由数字、26个英文字母或者下划线组成，长度1~50个字符！" >
							<hyjf:validmessage key="sendCd" label="编号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="sendName"> <span class="symbol required"></span>名称 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="名称" id="sendName" name="sendName" value="<c:out value="${sendtypeForm.sendName}" />"   class="form-control"
								maxlength="50" datatype="s1-50" errormsg="名称 只能是字符汉字，长度1~50个字符！" >
							<hyjf:validmessage key="sendName" label="名称"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="afterTime"> <span class="symbol required"></span>时间（分钟） </label>
						<div class="col-sm-10">
							<div class="input-group">
								<input type="text" placeholder="时间（分钟）" id="afterTime" name="afterTime" value="<c:out value="${sendtypeForm.afterTime}" />"   class="form-control"
									maxlength="4" datatype="n1-4" errormsg="时间（分钟） 只能是字符数字，长度1~4个字符！" >
								<span class="input-group-addon">分钟</span>
							</div>
							<hyjf:validmessage key="afterTime" label="时间（分钟）"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="remark"> 说明 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="说明" id="remark" name="remark" value="<c:out value="${sendtypeForm.remark}" />"   class="form-control"
								class="form-control" datatype="*1-255" maxlength="255" errormsg="说明 只能是字符汉字，长度1~50个字符！" ignore="ignore" >
							<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/sendtype/sendtypeInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
