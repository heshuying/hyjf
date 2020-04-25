<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,修改密码', ',')}" var="functionPaths"
	scope="request"></c:set>

	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
		flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="网站设置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css"
				rel="stylesheet" media="screen">
			<link
				href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
			<style>
.table-striped .checkbox {
	width: 20px;
	margin-right: 0 !important;
	overflow: hidden
}
.strength-box{
    overflow: hidden;
    margin-left:18%;
    margin-bottom: 10px;
    margin-top: 10px;
}
.strength-box p{
    margin: 0;
    float: left;
    font-size: 14px;
    color: #858585;
    height:20px;
    line-height:20px
}
.strength-box .strip{
    float: left;
    height: 20px;
    line-height:20px;
    background: #F1F1F1;
    WIDTH: 75px;
    text-align:center;
}
.strength-box .strip.light{
	color:#fff
}
.strength-box .strip.s1.light{
	background:#FFC05A;
}
.strength-box .strip.s2.light{
	background:#FF7E46;
}
.strength-box .strip.s3.light{
	background:#FF5B29;
}
.strength-box div+div{
 margin-left:5px;
}
.strength-box span{
	float:left
}
</style>
		</tiles:putAttribute>
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">修改密码</h1>
		<span class="mainDescription">本功能可以修改登录密码。</span>
	</tiles:putAttribute>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<h4>修改密码</h4>
				<hr />
				<div class="panel-scroll height-430">
					<form id="mainForm" method="post"
						role="form" class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="oldPassword">
								<span class="symbol required"></span>旧&nbsp;&nbsp;密&nbsp;&nbsp;码
							</label>
							<div class="col-sm-4">
								<input type="password" placeholder="旧密码" id="oldPassword" datatype="*6-25"
									name="oldPassword" class="form-control" maxlength="25" errormsg="请输入正确的旧密码！">
								<hyjf:validmessage key="oldPassword" label="旧密码"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="newPassword1">
								<span class="symbol required"></span>新&nbsp;&nbsp;密&nbsp;&nbsp;码
							</label>
							<div class="col-sm-4">
								<input type="password" placeholder="新密码" id="newPassword1"
									name="newPassword1" class="form-control" maxlength="16"  ajaxurl="checkPasswordAction" onkeyup="this.value=this.value.replace(/\s+/g,'')"
									errormsg="密码8-16位，必须包含字母、数字、符号至少两种！">
								<hyjf:validmessage key="newPassword1" label="新密码"></hyjf:validmessage>
							</div>
						</div>
						<div class="strength-box" >
							<p>安全强度</p>
	    					<div class="strip s1"  style="margin-left: 10px;">弱</div>
	    					<div class="strip s2">中</div>
	    					<div class="strip s3">强</div>
	    					<span></span>
	    				</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="newPassword2">
								<span class="symbol required"></span>确认新密码
							</label>
							<div class="col-sm-4">
								<input type="password" placeholder="确认新密码 " id="newPassword2"
									name="newPassword2" class="form-control" onkeyup="this.value=this.value.replace(/\s+/g,'')"
									>
								<hyjf:validmessage key="newPassword2" label="确认新密码 "></hyjf:validmessage>
							</div>
						</div>
					</form>
						<div class="col-sm-offset-2 col-sm-10">
							<button class="btn btn-sm btn-primary btn-o fn-Save">
								<i class="fa fa-undo"></i>保存
							</button>
						</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/www/updatepassword/updatepassword.js"></script> 
		</tiles:putAttribute>
</tiles:insertTemplate>
