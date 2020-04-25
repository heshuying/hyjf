<%@page import="com.hyjf.common.util.PropUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String webRoot = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	String themeRoot = PropUtils.getSystem("hyjf.theme.url");
	response.setHeader("Pragma","No-cache");    
	response.setHeader("Cache-Control","no-cache");    
	response.setDateHeader("Expires", -10);
%>
<c:set var="webRoot" value="<%=webRoot%>" />
<c:set var="themeRoot" value="<%=themeRoot%>" />
