<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>成功 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	 <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="auto-tender-success">
                    <div class="top-attr">
                        <img src="${ctx}/dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">授权成功!</p>
                    </div>
                    <div class="bom-attr">
						<button id="url"   class="submit-btn" href="${ctx}/user/pandect/pandect.do" onclick='urlClick()'>完成</button>
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
	var cookieUrl=utils.getCookie('authrePayUrl');
	if(cookieUrl!=''){
		$('#url').attr('href',cookieUrl)
	}
	function urlClick(){
		location.href=$('#url').attr('href')
	}
	</script>		
</body>
</html>