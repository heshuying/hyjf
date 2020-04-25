<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,消息中心,发送短信', ',')}" var="functionPaths" scope="request"></c:set>
<shiro:hasPermission name="smsLog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="发送短信" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox { width:20px;margin-right:0!important;overflow:hidden }
			</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">发送短信</h1>
			<span class="mainDescription">发送消息发送消息发送消息发送消息</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div>
				<form id="sendMessageForm" action="${pageContext.request.contextPath }/message/message/sendMessageAction" method="post">
					<div class="modal-body">
					<input type="hidden" id="pagecontext" name="pagecontext" value ="${pageContext.request.contextPath }">
					
					手机号码：<textarea rows="1" cols="15" name="user_phones" id="user_phones" maxlength="11"></textarea>
					<br />
					短信内容：<textarea rows="10" cols="30" name="message" id="message" maxlength="494"></textarea><font color="red">发送的内容无需添加短信签名，系统会自动加上,70个字符以内短信0.04分一条,之后每67个字符算一条计费</font>
					<br />
					短信渠道：<input type="radio" id="channelTypeNormal" name="channelType" value="normal" checked="checked"> <label for="channelTypeNormal"> 普通渠道 </label> 
						    <input type="radio" id="channelTypeMaketing" name="channelType" value="marketing" > <label for="channelTypeMaketing"> 营销渠道 </label>
					<br />
					发送类型：<input type="radio" id="sendTypeNow" name="sendType" value="now" checked="checked" onclick="javascript:$('#timetime').css('display','none');"> <label for="sendTypeNow"> 立即发送 </label> 
						    <input type="radio" id="sendTypeOntime" name="sendType" value="ontime" onclick="javascript:$('#timetime').css('display','block');" > <label for="sendTypeOntime"> 定时发送 </label>
					<br />
					是否脱敏：<input type="radio" id="noDisplay" name="isDisplay" value=1> <label
							for="noDisplay">是</label>
						<input type="radio" id="display" name="isDisplay" value=0 checked="checked"> <label
							for="display">否</label>
			    	<div class="form-group" id="timetime" style="display:none">
						<label>定时发送时间</label>
						<div class="input-group" style="width:30%">
											<span class="input-icon input-icon-right">
												<input type="text" name="on_time" id="on_time" class="form-control input-sm"
														value="<c:out value="${smsCode.on_time}"></c:out>" maxlength="19" ignore="ignore"  datatype="*" 
														onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})"/>
												<i class="fa fa-calendar"></i>
											</span>
						</div>
					</div>
					</div>
					<div class="modal-body">		
						<input class="btn btn-sm btn-primary btn-o" type="button" value="提交" onclick="tijiao();"/>
						<input class="btn btn-sm btn-primary btn-o" type="reset" value="重置"/>
					</div>
				</form>
						
				
			</div>
			
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/moment.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/i18n/zh-CN.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
				<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/message/sendMessageSingle/sendMessageSingle.js">
			</script> 
			<script type='text/javascript' >
			function tijiao() {
				var mobile=$('#user_phones').val();
				var ontime=$('#on_time').val();
				ontime="发送时间为"+ontime;
				if (ontime=='发送时间为'||$('#sendTypeNow').prop("checked")) {
					ontime="";
				}
				if(confirm("发送手机号为"+mobile+"，请确认发送。 "+ontime)) {
					$('#sendMessageForm').submit();
				}
			}
			</script> 
		</tiles:putAttribute>
	</tiles:insertTemplate>
 </shiro:hasPermission>
