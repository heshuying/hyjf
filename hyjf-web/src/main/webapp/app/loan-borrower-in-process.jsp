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
                <div class="in-process">
                    <div class="top-attr">
                        <img src="../dist/images/in-process.png">
                        <p class="top-title value">批量还款处理中...</p>
                        <p>批量还款可能需要几分钟时间，后台将自动进行处理，最终结果请以短信为准</p>
                    </div>

                    <div class="bom-attr">
                        <a href="javascript:;" class="open">返回</a>
                        <a href="javascript:;" class="open">查看垫付详情</a>
                    </div>
                </div>

            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>