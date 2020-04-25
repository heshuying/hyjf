<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
		<meta name="format-detection" content="telephone=no" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
        <link rel="stylesheet" type="text/css" href="${ctx}/css/invite.css"/>
        <style type="text/css">
        	.friend-code-img-2 {
        		left: 35%;
        		top: 35%
        	}
        </style>
		<title>理财就“邀”一起玩</title>
	</head>
	<body class="invite-bg">
		<div class="invite-active">
			<img src="${ctx}/images/invite/invite-banner.jpg?version=2" alt="" />
			<img src="${ctx}/images/invite/invite-img01.png" />
			<!--未登录-->
			<c:if test="${isLogin eq 0 }">
				<div class="invite-position">
					<img src="${ctx}/images/invite/invite-img06.png" alt="" />
					<div class="unload-cont invite-cont">
						<a href="${loginUrl}" class="down-btn">立即登录</a>
						<p>
							<span class="colorred">登录后</span>获取专属邀请链接
						</p>
					</div>
				</div>
			</c:if>
			<!--已登录-->
			<c:if test="${isLogin eq 1 }">
				<div class="invite-position">
					<img src="${ctx}/images/invite/invite-img02.png" />
					<div class="invite-cont">
						<div class="copy">
							<a href="${jumpCommand}://activityToShare/?{'title':'汇盈金服高收益专业金融服务平台','content':'灵活投资，稳健安全，尽享投资乐趣，快来体验专属于你的指尖财富','image':'https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png','url':'https://wx.hyjf.com/index.php?s=/Weixin/Activites/traffic/page_id/2119/id/${userId==null?'':userId}.html'}" class="clip-btn copy-btn" data-clipboard-target="#co1" id="copynum">复制邀请码</a>
							<span id="co1" class="copy-num">${userId}</span>
							<p>请好友注册时在“推荐人”一栏中填写您的邀请码即可。</p>
						</div>
						<div class="down">
							<div class="down-left">
								<div class="down-left-div friend-code-div">
									<input type="hidden" id="qrcodeValue" value="${webCatLink}" />
									<div id="qrcode" class="friend-code-img-1"></div>
										<c:choose>
										<c:when test="${ empty iconUrl}">
										<img src="${ctx}/img/LOGO.png" class="friend-code-img-2" style="width:28%"/>
										</c:when>
										<c:otherwise>
										<img src="${iconUrl }" class="friend-code-img-2"/>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="down-right">
								<span id="co2">${webCatLink}</span>
							</div>
							<div class="clear"></div>
						</div>
						<div class="button">
							<div class="down-left">
								<a href="${jumpCommand}://jumpH5Encode/?{'url':'${serverUrl}/appUser/getQrCodeAction.do?sign=${sign}&version=${version}&userId=${userId}'}" class="down-btn">打开二维码</a>
							</div>
							<div class="down-right">
								<a href="${jumpCommand}://activityToShare/?{'title':'汇盈金服高收益专业金融服务平台','content':'灵活投资，稳健安全，尽享投资乐趣，快来体验专属于你的指尖财富','image':'https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png','url':'https://wx.hyjf.com/index.php?s=/Weixin/Activites/traffic/page_id/2119/id/${userId==null?'':userId}.html'}" class="down-btn clip-btn" data-clipboard-target="#co2" id="copylink">复制链接</a>
							</div>
							<div class="clear"></div>
						</div>
						<div class="text">邀请好友<span class="colorred">点击</span>您的<span class="colorred">邀请链接</span>或<span class="colorred">扫描</span>您的<span class="colorred">二维码</span>进入页面<span class="colorred">注册</span>即可</div>
					</div>
				</div>
			</c:if>
			<img src="${ctx}/images/invite/invite-img03.png" />
			<img src="${ctx}/images/invite/invite-img04.png" />
			<img src="${ctx}/images/invite/invite-img05.png" />
		</div>
	</body>
</html>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/invite/clipboard.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.qrcode.js"></script>
<script type="text/javascript" src="${ctx}/js/qrcode.js"></script>
<script type="text/javascript" src="${ctx}/js/code.js"></script>
<script type="text/javascript">
	document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	$(window).on('resize', function() {
	document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	});
</script>
<script>
/* 复制内容到剪切板 */
	new Clipboard('#copynum');
	new Clipboard('#copylink');
</script>