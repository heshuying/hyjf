<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	    <article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up">
	    			<p class="title">修改紧急联系人</p>
	    			<div class="set-content">
	    				<img src="${cdn }/dist/images/acc-set/set-pw.png" class="success-img"/>
	    				<p class="success-word">紧急联系人修改成功！</p>
	    				<div class="continue">
	    					<span>您可以继续</span><a href="${ctx}/user/safe/init.do">账户设置</a><em class="rule"></em><a href="${ctx}/bank/web/borrow/initBorrowList.do">浏览投资项目</a>
	    				</div>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
</body>
</html>