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
	<article class="main-content">
        <div class="container">
            <section class="about-detial content">
                <div class="main-title">
                    <span class="title-head">${companyNotice.title}</span>
                    <span class="title-time"><fmt:formatDate value="${companyNotice.createTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                </div>
                <div class="detial-list">
                	${companyNotice.content}
			    </div>
			    <div class="detial-btn">
			    	<a href="javascript:;" onClick="javascript:history.go(-1)" itemid="lt">返回列表<i class="iconfont icon-xiayibu63"></i></a>
			    </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>