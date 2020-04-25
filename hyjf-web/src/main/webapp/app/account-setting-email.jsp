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
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            <!-- start 内容区域 --> 
            <div class="acc-set">
            	<div class="acc-set-email acc-set-email-1">
            		<form action="" id="email-form">
            		<div class="email-input">
        				<label for="email">电子邮箱</label>
            			<input type="email" name="email" id="email" value="" placeholder="请填写常用电子邮箱地址"/>
            		</div>
            		<span class="error-container"></span>
            		<div class="acc-set-btn email-submit-btn">
            			发送邮箱验证
            		</div>
            		</form>
            	</div>
            	<div class="acc-set-email acc-set-email-2 hide">
            		<p class="p-1">激活邮件已发送至您的邮箱，请前往验证</p>
            		<p class="p-2"><span class="span-1">接受邮箱：</span>22222@gmail.com<span class="span-2 underline-style">返回修改</span></p>
            		<div class="acc-set-btn">
            			前往激活
            		</div>
            	</div>
            	<div class="acc-set-email acc-set-email-3 hide">
            		<div class="email-outcome">
            			<div class="email-icon">
            				<img src="http://img.hyjf.com/dist/images/acc-set/email-icon.png"/>
            				<span class="icon iconfont icon-duihao"></span>
            			</div>
            		</div>
            		<p class="p-2">邮箱认证成功！</p>
            		<p class="p-3">您可以继续<a class="underline-style a-1">账户设置</a>|<a class="underline-style a-2">浏览投资项目</a></p>
            	</div>
            </div>
                      
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${ctx}/dist/js/acc-set/acc-set-email.js"></script>
</body>
</html>