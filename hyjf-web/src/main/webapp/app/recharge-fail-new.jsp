<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	 <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="../dist/images/result/failure-img.png">
                        <p class="top-title">您的充值失败了！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li class="recharge">
                                <div>失败原因：<span class="message">身份证或其他用户证件才能办理</span></div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <a href="javascript:;" class="open">返回重试</a>                 
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>