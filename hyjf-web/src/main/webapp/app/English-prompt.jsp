<%@ include  file="/common.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
    	<div class="container result">
    		<!-- start 内容区域 -->
    		<div class="English-prompt">
	    		<div class="imageBox"><img src="${ctx}/dist/images/prompt.jpg" alt="" /></div>
	    		<p class="cn-title">根据相关检查法规，汇盈金服平台暂不向地处美国的用户提供交易服务。</p>
	    		<p class="en-title">Due to regulatory restrictions，HYJF does not facilitate transactions by users located in the United States.</p>
	    		<p class="time"><span>10</span>秒后将跳转到首页</p>
	    		<div class="btn-box"><a href="https://www.hyjf.com/" class="btn-1">返回首页</a><a href="http://q.url.cn/s/09hAjRm?_type=wpa&qidian=true" class="btn">联系在线客服</a></div>
    		</div>
    		<!-- end 内容区域 --> 
    	</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script>
		var s=9
		var interval = window.setInterval(function() {
            if (s == 0) {
                clearInterval(interval);
                location.href='https://www.hyjf.com/'
            }
            $('.English-prompt .time span').html(s)
            s--;
        }, 1000);
	</script>
</body>
</html>