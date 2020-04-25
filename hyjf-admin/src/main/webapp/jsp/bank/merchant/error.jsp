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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>失败 - 汇盈金服官网</title>
</head>
<body>
	<article class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${webRoot}/dist/images/result/register-failure.png" alt="" />
            	</div>
            	<div class="result-mid">
            		<h3>设置失败！</h3>
            		<p></p>
            	</div>
            	<div class="result-add">
            		<p>失败原因</p>
            		<p class="failure-reason">${message}</p>
            	</div>
            	<div class="result-btn">
            		<a href="${webRoot}/bank/merchant/account/init.do" itemid="lt1" class="margin-auto">返回</a>
            	</div>
            </div>
        </div>
    </article>
</body>
</html>
