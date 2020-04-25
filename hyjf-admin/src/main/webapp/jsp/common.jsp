<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%
	String path = request.getContextPath();
//String webRoot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	//String themeRoot = PropUtils.getSystem("hyjf.theme.url");
	//response.setHeader("Pragma","No-cache");    
	//response.setHeader("Cache-Control","no-cache");    
	//response.setDateHeader("Expires", -10);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 