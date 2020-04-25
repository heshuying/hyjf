<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
            <article class="about-detial content">
                <div class="main-title">
                 	   公司历程
                </div>
                <div class="company-list">
                	<ul>
                		<c:forEach items="${eventsList }" var="record" begin="0" step="1" varStatus="status">
	                		<li>
                			<a href="${ctx}/aboutus/getEventDetail.do?id=${record.id}">
                				<div class="timer"><hyjf:datevent value="${record.actTime}"></hyjf:datevent></div>
                			    <div class="runder">
	                				<span class="circle"><i></i></span>
	                				<span class="vertical"></span>
	                				<span class="cross"></span>
                			    </div>
                			    <div class="events">
									<c:set var="title" value="${record.title}"></c:set>
									<c:choose>
									<c:when test="${fn:length(title) > 45}">
										<c:out value="${fn:substring(title, 0, 45)}..." />
									</c:when>
									<c:otherwise>
										<c:out value="${title}" />
									</c:otherwise>
									</c:choose>
								</div>
                			</a>
                		</li>
                	    </c:forEach>
                	</ul>
			    </div>
            </article>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
  	<!-- 设置定位  -->
  	<script>setActById("aboutEvents");</script>	
  	<!-- 导航栏定位  -->
    <script>setActById("indexMessage");</script>
</body>
</html>