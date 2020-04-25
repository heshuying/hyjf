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
				开通银行存管账户
			</div>
			<div class="kaihu-tabs">
				<form action="" method="get" id="khForm">
					<div class="kaihu-cont">
						<div class="kh-item">
							<lable for="khName" class="kh-lable">姓名</lable>
							<input type="text" name="khName" id="khName" class="kaihu-input" placeholder="请输入真实姓名" />
						</div>
						<div class="kh-item">
							<lable for="khIdcard" class="kh-lable">身份证</lable>
							<input type="text" name="khIdcard" id="khIdcard" class="kaihu-input" placeholder="身份证号应与姓名保持一致" oninput="if(value.length>18)value=value.slice(0,18)" />
						</div>
						<div class="kh-item">
							<lable for="khCredit" class="kh-lable">银行卡</lable>
							<input type="text" name="khCredit" id="khCredit" class="kaihu-input" placeholder="银行卡应与姓名保持一致" oninput="if(value.length>19)value=value.slice(0,19)" />
						</div>
						<div class="kh-item">
							<lable for="khPhone" class="kh-lable">手机号</lable>
							<input type="text" name="khPhone" id="khPhone" class="kaihu-input" placeholder="请输入手机号" oninput="if(value.length>11)value=value.slice(0,11)" />
						</div>
						<div class="kh-item kh-yz">
							<lable for="khVerify" class="kh-lable">验证码</lable>
							<input type="text" name="khVerify" id="khVerify" class="yz-input kaihu-input" placeholder="请输入验证码" oninput="if(value.length>4)value=value.slice(0,4)" />
							<input type="button" value="获取" class="get-btn" />
						</div>
						<div class="kh-item kh-agree">
							<input type="checkbox" name="khAgree" id="khVerify" checked="checked" class="check-btn" />
							<label class="kaihu-agree" for="khAgree">我已阅读并同意<a href="">《开户协议》</a></label>
						</div>
						<div class="kh-item">
							<input type="submit" value="开通银行托管账户" class="kaihu-btn" />
						</div>

					</div>
				</form>
			</div>
			<!-- end 内容区域 -->
		</div>
	</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
	<script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/kaihu/kh.js"></script>
</body>
</html>