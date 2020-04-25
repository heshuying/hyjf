<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%
	//String path = request.getContextPath();
//String webRoot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	//String themeRoot = PropUtils.getSystem("hyjf.theme.url");
	//response.setHeader("Pragma","No-cache");    
	//response.setHeader("Cache-Control","no-cache");    
	//response.setDateHeader("Expires", -10);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 
<c:choose>
	<%-- #环境类型  0测试   1预生产  99正式 --%>
	<c:when test="${onlineType == '99'}">
		<c:set var="cdn" value="https://img.hyjf.com" />
	</c:when>
	<c:when test="${onlineType == '1'}">
		<c:set var="cdn" value="https://staimg.hyjf.com" />
	</c:when>
	<c:otherwise>
		<c:set var="cdn" value="${pageContext.request.contextPath}" />
	</c:otherwise>
</c:choose>
<c:set var="sensorsDataUrl" value="${sensorsDataUrl}" />
