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
                   	 风险教育
                </div>
                
	                <div class="announce-list"><!-- 新版 -->
	                 	<ul id="strMediaListPage">
	                 	</ul>
	                </div>		
			        <div class="pages-nav" id="new-pagination"></div> 
				</section>
		    </div>
		</article>
	    <jsp:include page="/footer.jsp"></jsp:include>

	    <script src="${cdn}/js/common/common.js" type="text/javascript"></script>
	    <script src="${cdn}/js/aboutus/fengxian.js?version=${version}" type="text/javascript"></script>
	  	<!-- 设置定位  -->
	    <script>setActById("aboutRiskEducation");</script>
	    <!-- 导航栏定位  -->
  		<script>setActById("indexMessage");</script>
</body>

</html>