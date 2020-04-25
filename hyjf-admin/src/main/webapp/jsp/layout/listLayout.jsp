<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<!DOCTYPE html>
<!--[if IE 8]><html class="ie8" lang="en"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="en"><![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<head>
	<%-- 画面的共通MATE --%>
	<%@include file="/jsp/common/headMetas.jsp"%>
	<%-- 浏览器左上角显示的文字 --%>
	<title><tiles:getAsString name="pageTitle" /> - 汇盈金服后台管理系统</title>
	<%-- 画面的共通CSS --%>
	<%@include file="/jsp/common/headCss.jsp"%>
	<%-- 各个画面的CSS --%>
	<tiles:insertAttribute name="pageCss" ignore="true" />
	<%-- 上部分共通的JS --%>
	<%@include file="/jsp/common/headJavascript.jsp"%>
	<%-- 消息信息提示通知插件 --%>
	<%@include file="/jsp/common/pluginMessageInfo.jsp"%>
	<%-- Form表单插件 --%>
	<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	<%-- 上部分的JS --%>
	<tiles:insertAttribute name="pageGlobalImport" ignore="true" />
</head>
<body>

<!--lockscreen-->
<div class="lockscreen" style="display:none">
	<div class="lock-overlay"></div>
	<div class="logwindow">
		<div class="logwindow-inner" align="center">
			<h3>Locked</h3>
			<div class="iconWrap"><i class="ti-user"></i></div>
			<h5>登录者: ${sessionScope.LOGIN_USER_INFO.username}</h5>
			<div class="input-group" style="width:260px;">
				<input type="text" size="9" placeholder="请输入解锁口令..." class="form-control unlockPWD">
				<span class="input-group-btn">
					<button class="btn btn-primary" id="unlockBtn">
						<i class="fa fa-chevron-right"></i>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>

<div id="app" class="${hasMenu ne 'false' ? '' : 'no-menu' }">
	<%@include file="/jsp/common/pageLeft.jsp"%>
	<div class="app-content">
	<%@include file="/jsp/common/pageTop.jsp"%>
	<%@include file="/jsp/common/pageContent.jsp"%>
	</div>
	<%@include file="/jsp/common/pageFooter.jsp"%>
	<%@include file="/jsp/common/pageSettings.jsp"%>
</div>

<%-- 检索面板 --%>
<div id="searchable-panel" class="perfect-scrollbar">
	<form id="mainForm" method="post">
		<div class="modal-body">
			<!-- <h4>
				<a class="btn btn-xs btn-danger btn-o pull-right" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-close"></i></a>
				<i class="ti-layout-sidebar-right"></i> 检索条件
			</h4> -->
			<!-- <hr/> -->
			<tiles:insertAttribute name="searchPanels" ignore="true" />
			<div class="modal-footer">
				<button class="btn btn-sm btn-primary btn-o fn-ClearForm" type="button"><i class="fa fa-undo"></i> 清 空</button>
				<button class="btn btn-sm btn-primary btn-o fn-Reset1" type="reset"><i class="fa fa-undo"></i> 重 置</button>
				<button class="btn btn-sm btn-primary btn-o fn-Search" type="button"><i class="fa fa-search"></i> 检 索</button>
			</div>
		</div>
	</form>
	<br/><br/><br/>
</div>
<%-- 对话框面板 --%>
<tiles:insertAttribute name="dialogPanels" ignore="true" />
<style>
/*内部搜索修改后的样式*/
@media (min-width:480px){
	#searchable-panel-clone .form-group{
		width:100%;
		display:block;
		float:left;
		margin:1% 0;
	}
}
@media (min-width:768px){
	#searchable-panel-clone .form-group{
		width:46%;
		display:block;
		float:left;
		margin:1%;
	}
}
@media (min-width:992px){
	#searchable-panel-clone .form-group{
		width:30%;
		display:block;
		float:left;
		margin:1%;
	}
}
@media (min-width:1200px){
	#searchable-panel-clone .form-group{
		width:22%;
		display:block;
		float:left;
		margin:1%;
	}
}


#searchable-panel-clone{
	background:#e8e8e8;
	margin-top:10px;
	width:100%;
	float:left;
	margin-bottom:10px;
}
#searchable-panel-clone .form-group .select2-container{
	/* width:100%!important; */
}
#searchable-panel-clone .modal-footer{
	width:100%;
	float:left;
}

#searchable-panel-clone .input-daterange{
	margin-top:-4px;
}

/* ----------------------------------------- */
/* 搜索框由纵向改为横向 开始*/

#searchable-panel-clone input[type="text"].underline{
	border:1px solid #c8c7cc;
	border-radius:3px !important;
}
#searchable-panel-clone{
	background:#fff !important;
}
#searchable-panel-clone .form-group{
	width:auto;/* 235 */
	margin:5px 0;
}
#searchable-panel-clone .form-group .dropdown-menu{
	float:left;
}
#searchable-panel-clone .form-group span.input-icon{
	float:left;
}
#searchable-panel-clone .form-group label{
	display:inline-block !important;
	padding:0 5px;
	float:left;
	line-height:30px;
	letter-spacing:1px;
}
#searchable-panel-clone .form-group input,#searchable-panel-clone .form-group .select2{
	width:auto !important;
	min-width:140px;/* 60 */
	display:block;
	float:left;
	height:32px;
	height:32px;
}
.select2-container .select2-selection--single{
	height:32px !important;
}
.select2-container--default .select2-selection--single .select2-selection__rendered{
	line-height:32px !important;
}

#searchable-panel-clone .form-group .datepicker{
	width:auto;
	float:left;
}
#searchable-panel-clone .form-group .datepicker input{
	width:110px !important;
}
.select2-container--default .select2-selection--single{
	border-radius:3px !important;
}
.form-group .input-group .input-group-addon{
	width:32px;
}
#searchable-panel-clone .form-group .input-group{
	float:left;
}
.main-content .perfect-scrollbar{
	overflow-y: visible !important;
}
select.form-control{
	float:left;
	display:block;
	width:auto;
	min-width:120px;
	border-radius:3px !important;
}
/* 搜索框由纵向改为横向 结束 */
/* -------------------------------------------------------------- */
</style>
<script>
	//检索框由右边移动到内部
	var searchPanel = $("#searchable-panel").clone(true).hide().attr("id","searchable-panel-clone");
	searchPanel.find("br").remove();
	/* searchPanel.find("h4").find(".pull-right").click(function(){
		$("#searchable-panel-clone").slideUp(300)
	}) */
	
	$("#searchable-panel").remove();
	searchPanel.appendTo($(".well"));
	//重新绑定事件
	/* $(":text,.form-select2", "#searchable-panel-clone").val("").filter(".form-select2").trigger('change'); */
	
	$(".fn-searchPanel").click(function(){
		$("#searchable-panel-clone").toggle();
	})
</script>
</body>

<!-- Javascripts required for this page only -->
<script type="text/javascript" src="${themeRoot}/assets/js/page.admin.js"></script>
<tiles:insertAttribute name="pageJavaScript" ignore="true" />



</html>
