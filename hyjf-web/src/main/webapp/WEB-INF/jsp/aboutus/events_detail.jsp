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
            <div class="main-title">
                <span class="title-head">
                    <c:set var="title" value="${eventsContent.title}"></c:set>
                    <c:choose>
                        <c:when test="${fn:length(title) > 40}">
                            <c:out value="${fn:substring(title, 0, 40)}" />
                        </c:when>
                        <c:otherwise>
                            <c:out value="${title}" />
                        </c:otherwise>
                    </c:choose>
                </span>
                <span class="title-time">${eventsContent.eventTime}
                <%--<hyjf:datetime value="${eventsContent.createTime}" pattern="yyyy-MM-dd"></hyjf:datetime>--%>
                </span>
            </div>
            <div class="detial-list">
                ${eventsContent.content}
            </div>
            <div class="detial-btn">
                <a href="${ctx }/aboutus/events.do"  itemid="lt">返回列表<i class="iconfont icon-xiayibu63"></i></a>
            </div>
        </section>
    </div>
</article>
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 设置定位  -->
<script>setActById("aboutEvents");</script>
<!-- 导航栏定位  -->
<script>setActById("indexMessage");</script>
</body>
</html>