<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="ture" scope="request"></c:set>
<c:set var="hasMenu" value="ture" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="画面名称" />
	
	<%-- 画面主面板头 (ignore，与下一项二选一) --%>
	<tiles:putAttribute name="pageHeader" type="string">
		
	</tiles:putAttribute>
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">画面功能名称</h1>
		<span class="mainDescription">本功能的说明及提示文本</span>
	</tiles:putAttribute>
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		
	</tiles:putAttribute>
	
	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		
	</tiles:putAttribute>
	<%-- 边界面板 (ignore) --%>
	<tiles:putAttribute name="asidePanels" type="string">
		
	</tiles:putAttribute>
	<%-- 对话框面板 (ignore) --%>
	<tiles:putAttribute name="dialogPanels" type="string">
		
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		
	</tiles:putAttribute>
	
	<%-- JS插件补丁 (ignore) --%>
	<tiles:putAttribute name="pageJsPatch" type="string">
		
	</tiles:putAttribute>
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		
	</tiles:putAttribute>
</tiles:insertTemplate>
