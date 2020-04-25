<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                    	网站公告
                </div>
                <div class="announce-list">
                <ul id="announce-list"></ul>
                </div>
 			<div class="pages-nav" id="new-pagination"></div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>

	<script src="${cdn}/js/contentArticle/contentArticle.js?version=${version}" type="text/javascript"></script>
	<!-- 设置定位  -->
 	<script>setActById("indexMessage");</script>	
	<!-- 设置定位  -->
 	<script>setActById("aboutNotice");</script>	
</body>
</html>