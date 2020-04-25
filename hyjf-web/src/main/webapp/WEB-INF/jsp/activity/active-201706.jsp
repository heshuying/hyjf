<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>理财就“邀”一起玩</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-201706.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div  class="colorblue">
		<img src="${ctx}/img/active/active_201706/invite-ban.jpg?version=2" alt="" />
		<div class="invite-active">
			<img src="${ctx}/img/active/active_201706/invite-bg01.png" />
			<!--未登录-->
			<c:if test="${isLogin eq 0 }">
				<div class="invite-position">
					<img src="${ctx}/img/active/active_201706/invite-unload.png" alt="" />
					<div class="unload-cont invite-cont">
						<a href="${ctx}/user/login/init.do?retUrl=/activity/invitesix/init.do" class="down-btn">立即登录</a>
						<p><span class="colorred">登录后</span>获取专属邀请链接</p>
					</div>
				</div>
			</c:if>
			<!--已登录-->
			<c:if test="${isLogin eq 1 }">
				<div class="invite-position">
					<img src="${ctx}/img/active/active_201706/invite-bg02.png" />
					<div class="invite-cont">
						<div class="invite-copy-hint">复制成功，快转发给好友吧!</div>
						<div class="copy">
							<a href="javascript:;" rel="#co1" id="copyCode">复制邀请码</a>
							<span id="co1">${userId}</span>
							<p>请好友注册时在“推荐人”一栏中填写您的邀请码即可。</p>
						</div>
						<div class="down">
							<div class="qrcode down-top">
								<input type="hidden" id="qrcodeValue" value="${webCatLink }" />
								<div id="qrcode" class="invite-ewm">
									<img src="${ctx}/img/LOGO_CODE.png" />
								</div>
								<p id="co2" class="invite-link">${inviteLink}</p>
								<div class="clear"></div>
							</div>
							<div class="down-mid">
								<a href="${ctx}/user/invite/download.do" class="down-btn">下载二维码</a>
								<a href="javascript:;" class="down-btn" rel="#co2" id="copyLink">复制链接</a>
								<p>邀请好友点击您的邀请链接或扫描您的二维码进入页面注册即可。</p>
							</div>
							
						</div>
					</div>
				</div>
			</c:if>
			
			<img src="${ctx}/img/active/active_201706/invite-bg03.png" />
			<img src="${ctx}/img/active/active_201706/invite-bg04.png" />
			<img src="${ctx}/img/active/active_201706/invite-bg05.png" />

		</div>
		<div class="invite-bot">
			本活动最终解释权归汇盈金服所有
		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/jquery.qrcode.js"></script>
	<script type="text/javascript" src="${ctx}/js/qrcode.js"></script>
	<script type="text/javascript" src="${ctx}/js/code.js"></script>
	<script src="${ctx}/js/jquery.zclip.min.js"></script>
	<script src="${ctx}/js/fill.js" type="text/javascript"></script>
	
    <script>
	$(document).ready(function() {
		/*复制内容*/
		$("#copyCode").zclip({   
			path: "${ctx}/js/ZeroClipboard.swf",
			copy: $("#co1").html(),
			afterCopy:function(){/* 复制成功后的操作 */
				$(".invite-copy-hint").show().delay(1500).fadeOut(300)
				/*var $copysuc = $("<div class='copy-tips'><div class='copy-tips-wrap'>☺ 复制成功，快转发给好友吧！</div></div>");
				$("body").find(".copy-tips").remove().end().append($copysuc);
				$(".copy-tips").fadeOut(3000);*/
	        }
		});
		
		$("#copyLink").zclip({   
			path: "${ctx}/js/ZeroClipboard.swf",
			copy: $("#co2").html(),
			afterCopy:function(){/* 复制成功后的操作 */
				$(".invite-copy-hint").show().delay(1500).fadeOut(300)
				/*var $copysuc = $("<div class='copy-tips'><div class='copy-tips-wrap'>☺ 复制成功，快转发给好友吧！</div></div>");
				$("body").find(".copy-tips").remove().end().append($copysuc);
				$(".copy-tips").fadeOut(3000);*/
	        }
		});


	});
    </script>
	</body>
</html>