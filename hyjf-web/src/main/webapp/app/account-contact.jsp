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
			<div class="account-set-up-2">
				<p class="title">设置紧急联系人</p>
				<div class="set-content">
		   			<form action="" id="contactForm">
		   				<div class="select-box">
		   					<span class="tit">关系</span>
		   					<div class="new-form-item">
								<h4 class="new-form-input">配偶</h4>
								<div class="new-form-hdselect">
									<ul id="relationUl" for="relationId">
										<li value="1">配偶</li>
										<li value="2">父亲</li>
										<li value="3">母亲</li>
										<li value="4">兄弟</li>
										<li value="5">朋友</li>
										<li value="6">其他</li>
									</ul>
									
								</div>
								<i class="iconfont icon-ananzuiconv265"></i>
								<input type="hidden" name="" id="relationId" value="1" />
							</div>
		   				</div>
		   				<label>
		   					<span class="tit">姓名</span>
		   					<input type="text" name="name" maxlength="20" placeholder="请输入联系人姓名"/>
		   				</label>
		   				<label>
		   					<span class="tit">电话</span>
		   					<input type="text" name="tel" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="11" placeholder="请输入验证码"/>
		   				</label>
		   				<a class="sub">下一步</a>
		   			</form>
				</div>
		    </div>
		</div>
	</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
	<script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/lib/customForm.js"></script>
	<script src="../dist/js/acc-set/account-contact.js"></script>
</body>
</html>