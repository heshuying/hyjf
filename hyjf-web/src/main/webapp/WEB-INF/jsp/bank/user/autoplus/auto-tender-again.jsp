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
                        <img src="${cdn}/dist/images/result/failure-img@2x.png" width="125">
                        <p class="top-title" style="font-size:24px;bottom:60px;">授权期限过短或额度过低，请重新授权！<span style="position: absolute;left: 0;top: 40px;font-weight: normal;font-size: 14px;color: #999999;">为了避免出借过程中出现异常，请勿修改银行授权期限与额度参数。</span></p>
                    </div>
                    <div class="bom-attr">
                        <button type="button" id="url" href="${ctx}/user/safe/init.do" class="submit-btn" onclick='urlClick()'>重新授权</button>
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${ctx}/dist/js/lib/moment.min.js" type="text/javascript" charset="utf-8"></script>
	<script>
		var cookieUrl=utils.getCookie('authUrl');
		if(cookieUrl!=''){
			$('#url').attr('href',cookieUrl)
		}
		function urlClick(){
			location.href=$('#url').attr('href')
		}
	</script>
	

</body>

</html>