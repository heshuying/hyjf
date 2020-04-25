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
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="http://img.hyjf.com/dist/images/result/failure-img.png">
                        <p class="top-title">抱歉，投资失败，请重试！</p>
                    </div>
                    
                    <div class="bom-attr">
                        <a href="javascript:;" class="open">查看交易明细</a>
                        <a href="javascript:;" class="open">返回项目详情</a>                    
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>