<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
                <div class="auto-tender-success">
                    <div class="top-attr">
                        <img src="../dist/images/result/failure-img.png">
                        <p class="top-title">抱歉，自动投标授权失败，请重试！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div><span>失败原因：</span><span>具体原因</span></div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <button type="button" class="submit-btn">返回重试</button>
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>