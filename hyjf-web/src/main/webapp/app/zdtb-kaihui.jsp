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
				<div class="fxcp-title">
					自动投标授权
				</div>
				<div class="kaihu-tabs">
					<form action="" method="get" id="khForm">
						<div class="kaihu-cont">
							<div class="kh-item kh-yz">
								<p class="zdtb-tel">手机号<span>134****6269</span></p>
								<input type="hidden" name="" value="13465896269" id="sqPhone"/>
							</div>
							<div class="kh-item kh-yz">
								<lable for="khVerify" class="kh-lable">验证码</lable>
								<input type="text" name="khVerify" id="khVerify" class="yz-input kaihu-input" placeholder="请输入验证码" oninput="if(value.length>4)value=value.slice(0,4)" />
								<input type="button" value="获取" class="get-btn" />
							</div>
							<div class="kh-item kh-agree">
								<input type="checkbox" name="khAgree" id="khVerify" checked="checked" class="check-btn" />
								<label class="kaihu-agree" for="khAgree">我已阅读并同意<a href="">《自动投标授权协议》</a></label>
							</div>
							<div class="kh-item">
								<input type="submit" value="开通自动投标授权" class="kaihu-btn" />
							</div>

						</div>
					</form>
				</div>
				<!-- end 内容区域 -->
			</div>
		</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/kaihu/sq.js"></script>
</body>
</html>