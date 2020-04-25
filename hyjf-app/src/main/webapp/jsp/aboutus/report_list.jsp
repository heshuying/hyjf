<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="UTF-8">
		<title>运营报告列表</title>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
</head>
<style>
a{display:inline-block;width:100%}
</style>
<body >
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" id="version" name="version" value="${version}" />
		<section class="report-list">
			<a href="" class="report-link-201803">
				<img src="${ctx}/img/report201803/report-list-201803.jpg"/ >
			</a>
			<a href="" class="report-link-201802">
				<img src="${ctx}/img/report201802/report-list-201802.jpg"/ >
			</a>
		    <a href="" class="report-link-201801">
				<img src="${ctx}/img/report-list-201801.jpg"/ >
			</a>
		    <a href="" class="report-link-201712">
				<img src="${ctx}/img/report-list-201712.jpg"/ >
			</a>
		    <a href="" class="report-link-201711">
				<img src="${ctx}/img/report-list-201711.jpg"/ >
			</a>
		    <a href="" class="report-link-201710">
				<img src="${ctx}/img/report-list-201710.jpg?v=20171123"/ >
			</a>
		    <a href="" class="report-link-201709">
				<img src="${ctx}/img/report-list-201709.jpg?v=20171123"/ >
			</a>
		    <a href="" class="report-link-201708">
				<img src="${ctx}/img/report-list-201708.jpg?v=20171123"/ >
			</a>
		    <a href="" class="report-link-201707">
				<img src="${ctx}/img/report-list-201707.jpg?v=20171123"/ >
			</a>
		   <a href="" class="report-link-201706">
				<img src="${ctx}/img/report-list-201706.jpg?v=20171123"/ >
			</a>
		    <a href="" class="report-link-201705">
				<img src="${ctx}/img/report-list-201705.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-201704">
				<img src="${ctx}/img/report-list-201704.png?v=20171123"/ >
			</a>
			<a href="" class="report-link-201703">
				<img src="${ctx}/img/report-list-2017-1th.png?v=20171123"/ >
			</a>
		    <a href="" class="report-link-201702">
				<img src="${ctx}/img/report-list-201702.jpg?v=20171122"/ >
			</a>
			<a href="" class="report-link-201701">
				<img src="${ctx}/img/report-list-201701.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-dec">
				<img src="${ctx}/img/report-list-dec.jpg?v=20171123"/ >
			</a>
		    <a href="" class="report-link-nov">
				<img src="${ctx}/img/report-list-nov.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-oct">
				<img src="${ctx}/img/report-list-1.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-3rd">
				<img src="${ctx}/img/report-list-2.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-aug">
				<img src="${ctx}/img/report-list-3.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-july">
				<img src="${ctx}/img/report-list-4.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-half">
				<img src="${ctx}/img/report-list-5.jpg?v=20171123"/ >
			</a>
			<a href="" class="report-link-may">
				<img src="${ctx}/img/report-list-6.jpg?v=20171123"/ >
			</a>
			<p class="tac">汇盈金服一直秉承诚信规范、公开透明、平等协作的精神，</p>
			<p class="tac">© 汇盈金服 All rights reserved | 惠众商务顾问（北京）有限公司 | 京ICP备13050958号</p>
			<p class="tac">| 公安安全备案证：37021313127</p>
		</section>
	</body>
	<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/jq22.js" type="text/javascript" charset="utf-8"></script>
</html>