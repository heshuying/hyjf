<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>绑卡失败 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>

	<section class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${cdn}/dist/images/result/failure-img@2x.png" width="125">
            	</div>
				<div class="result-mid">
					<p>${message}</p>
				</div>
            	<div class="result-btn">
            		
            		<c:if test="${urlstatus==null}">
	            		<div class="result-left">
	            			<a href="${ctx}/" class="import" itemid="lt1">返回首页</a>
	            		</div>
	            	</c:if>
	            	
	            	<c:if test="${urlstatus!=null}">
		            	<div class="result-left">
	            			<a href="${url}" class="import" itemid="lt1">${buttonName}</a>
	            		</div>
	            	</c:if>
            		<div class="result-right">
            			<a target="_blank" href="http://q.url.cn/s/09hAjRm?_type=wpa&qidian=true" itemid="lt2">联系在线客服</a>
            		</div>
            		 <div class="clearboth"></div>
            	</div>
            </div>
        </div>
    </section>
	
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>