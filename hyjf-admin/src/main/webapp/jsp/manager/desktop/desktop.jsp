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



<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="我的工作台" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<img class = "img" src="/hyjf-theme/images/main.png" alt="" />
		<div class = "content">
		</div>
	</div>	
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
		#app{background:#f4f6f9;}
		#app > footer{display:none;}
		img.img{
			 /* Set rules to fill background */
			 min-height:100%;min-width:1024px;/* Set up proportionate scaling */
			 width:100%;height:auto !important;height:100%;/* Set up positioning */
			 position:fixed;left:0;z-index:1;
		}
		.container-fullw{margin-left:-30px;padding:0;}
		.main-content > .container{padding:0;}
	</style>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
