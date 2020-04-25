<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
		<article class="main-content">
			<div class="container">
				<section class="about-detial content">
                <div class="no-border main-title">
                   	 网贷知识
                </div>
                
	                <div class="announce-list"><!-- 新版 -->
	                 	<ul id="strMediaListPage">
	                 	</ul>
	                </div>		
			        <!-- 分页 -->
			        <div class="pages-nav" id="new-pagination"></div> 
				</section>
		    </div>
		</article>
	    <jsp:include page="/footer.jsp"></jsp:include>
	    <script src="${cdn}/js/aboutus/know.js?version=${version}" type="text/javascript"></script>
	    <script>setActById("aboutInformation");</script>
	    <!-- 设置定位  -->
	    <script>setActById("indexMessage");</script>
</body>

</html>