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
	<%-- 上部分的JS --%>
	<tiles:insertAttribute name="pageGlobalImport" ignore="true" />
</head>
<body>
	<div class="app-content">
	<%@include file="/jsp/common/pageDialogContent.jsp"%>
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
