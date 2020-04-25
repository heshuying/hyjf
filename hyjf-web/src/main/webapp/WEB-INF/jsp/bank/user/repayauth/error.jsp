<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>失败 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	 <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="auto-tender-success">
                    <div class="top-attr">
                        <img src="${ ctx}/dist/images/result/failure-img@2x.png" width="125">
                        <p class="top-title">授权失败</p>
                    </div>
                    <div class="bom-attr">
                        <button type="button" id="url" href="${ctx}/user/safe/init.do" class="submit-btn" onclick='urlClick()'>返回重试</button>
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
	function urlClick(){
		location.href=$('#url').attr('href')
	}
	</script>
</body>
</html>
