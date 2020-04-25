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
        <div class="container">
           <div class="band-title">
				绑定邮箱
		   </div>
            <!-- start 内容区域 --> 
            <div class="acc-set">
            
            	<div class="acc-set-email acc-set-email-1">
            		<form action="" id="email-form">
            			<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
						<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
 	            		<div class="email-input">
	        				<label for="email">电子邮箱</label>
	            			<input type="email" name="email" id="email" value="" placeholder="请填写常用电子邮箱地址"/>
	            		</div>
	            		<div class="acc-set-btn email-submit-btn">
	            			发送邮箱验证
	            		</div>
            		</form>
            	</div>
            	<div class="acc-set-email acc-set-email-2 hide">
            		<p class="p-1">激活邮件已发送至您的邮箱，请前往验证</p>
            		<p class="p-2">
            			<span class="span-1">接受邮箱：</span><span id="receiveEmail"></span>
            			<span class="span-2">&nbsp;&nbsp;<a href="${ctx}/user/safe/bindingEmail.do" class="underline-style">返回修改</a></span>
            		</p>
            		<div class="acc-set-btn">
            			<a href="" target="_blank" id="toEmailHtml">前往激活</a> 
            		</div>
            	</div>
            </div>       
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script><!-- validate.js一定要引入，影响JS的功能 -->
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn}/dist/js/acc-set/acc-set-email.js"></script>
</body>
</html>