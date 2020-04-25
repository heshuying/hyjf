<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>充值失败 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn}/dist/images/result/failure-img@2x.png" width="125">
                        <p class="top-title">您的充值失败了！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li class="recharge">
                                <div>失败原因：<span class="message">${message}</span></div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="open">返回重试</a>                 
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>	
	</body>
</html>