<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>“礼”财就邀一起玩</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	</head>
    <style>
    	* {
		margin: 0;
		padding: 0
	}
	
	img {
		max-width: 100%;
	}
	
	.invite-bg {
		background: #7ac0fa;
		font-family: "microsoft yahei";
		color: #333333;
	}
	
	.invite-active img {
		display: block;
		width: 100%;
	}
	
	.invite-cont {
		width: 10.51rem;
		position: absolute;
		top: 2.80rem;
		left: 0;
		right: 0;
		margin: auto;
		letter-spacing: 1px;
	}
	
	.invite-cont .copy {
		width: 100%;
		height: 2.23rem;
		background: url(${ctx}/img/act-2017-7/copy.png) no-repeat center;
		background-size: 100%;
		padding: 0.45rem 0.7rem;
		box-sizing: border-box;
		-webkit-border-radius: border-box;
		-moz-border-radius: border-box;
	}
	
	.invite-cont .copy a {
		display: block;
		float: left;
		width: 2.6rem;
		height: 0.7rem;
		line-height: 0.7rem;
		text-decoration: none;
		background: #f9bb00;
		color: #ffffff;
		font-size: 0.30rem;
		text-align: center;
	}
	.activeOutdatePop{display:none;text-align:center;font-size:13px;width:200px;padding:0 4px;line-height:32px;color:white;position:fixed;background:#898989;border-radius:4px;-moz-border-radius:4px;-webkit-border-radius:4px;top:50%;margin-top:-66px;margin-left:-100px;left:50%;z-index:11}
	.invite-cont .copy span {
		display: block;
		float: right;
		font-size: 0.76rem;
		color: #28a7e1;
		line-height: 0.74rem;
	}
	
	.invite-cont .copy p {
		width: 100%;
		font-size: 0.3rem;
		margin-top: 0.20rem;
		float: left;
		letter-spacing: 0;
	}
	
	.down {
		margin-top: 0.20rem;
	}
	
	.down-left {
		display: block;
		float: left;
		width: 2.80rem;
	}
	
	.down-right {
		display: block;
		float: right;
		width: 7.53rem;
	}
	
	.down-btn {
		display: block;
		text-decoration: none;
		color: #fff;
		text-align: center;
		width: 3rem;
		height: 0.87rem;
		line-height: 0.87rem;
		background: #fd6d6d;
		border-radius: 5px;
		font-size: 0.37rem;
		margin: 0 auto;
	}
	
	.down .down-right span {
		display: block;
		width: 100%;
		height: 1.55rem;
		background: #cfecea;
		font-size: 0.25rem;
		padding: 0.42rem 0.4rem;
		box-sizing: border-box;
		-webkit-border-radius: border-box;
		-moz-border-radius: border-box;
		word-wrap: break-word;
		line-height: 0.35rem;
	}
	
	.invite-cont .text {
		font-size: 0.30rem;
		margin-top: 0.25rem;
	}
	
	.invite-cont .text .colorred {
		color: #f6671b;
	}
	.invite-copy-hint {
            z-index: 11;
            background-color: #606060;
            position: absolute;
            font-size: 0.3rem;
            text-align: center;
            color: white;
            width: 5.0rem;
            height: 0.8rem;
            line-height: 0.8rem;
            left: 50%;
            margin-left: -2.5rem;
            top: 1.45rem;
            display: none;
        }
	
	.clear {
		clear: both;
	}
	
	.invite-position {
		position: relative;
		width: 100%;
	}
	
	.unload-cont>a {
		display: block;
		margin: 0 auto;
	}
	
	.unload-cont p {
		font-size: 0.30rem;
		margin-top: 0.15rem;
		text-align: center;
	}
	
	.unload-cont .colorred {
		color: #fd6d6d;
		text-decoration: none;
	}
	
	.button {
		margin-top: 0.3rem
	}
	
	.down-left-div {
		position: relative;
		width: 1.55rem;
		margin: 0 auto;
	}
	
	.invite-record {
		position: relative;
	}
	
	.main-tab {
		position: absolute;
		width: 10.62rem;
		left: 0;
		right: 0;
		margin: auto;
		top: 2.350rem;
		font-size: 0.32rem;
	}
	
	.main-tab .tab-list {
		display: block;
		width: 4.20rem;
		margin: 0 auto;
		padding: 0.14rem 0;
		height: 0.88rem;
	}
	
	.main-tab .tab-list li {
		width: 1.39rem;
		text-align: center;
		list-style: none;
		float: left;
		font-size: 0.36rem;
		color: #FD6D6D;
		height: 0.88rem;
		line-height: 0.88rem;
	}
	
	.main-tab .tab-list li.active {
		color: #fff;
		background: #FD6D6D;
	}
	
	.tab-cont li {
		display: none;
		list-style: none;
	}
	
	.tab-cont li.show {
		display: block;
	}
	
	.tab-cont li table {
		width: 100%;
	}
	
	.tab-cont li table th {
		padding: 0.35rem 0 0.55rem;
	}
	
	.tab-cont li table td {
		padding: 0.10rem 0;
	}
	
	.pages-nav {
		display: block;
		text-align: center;
		height: 0.55rem;
		clear: both;
		background: #fff;
		margin-top: 0.6rem;
		font-size: 0.32rem;
	}
	
	.pages-nav a {
		width: 0.50rem;
		height: 0.50rem;
		text-align: center;
		line-height: 0.50rem;
		border: 1px solid #999999;
		display: inline-block;
		text-decoration: none;
		color: #999999;
		margin-left: 0.12rem;
		margin-right: 0.12rem;
		background: #fff;
	}
	
	.pages-nav .prev,
	.pages-nav .next {
		border: 0;
		display: inline-block;
		width: 1.2rem;
		height: 0.50rem;
		line-height: 0.50rem;
		color: black;
		text-align: center;
		background: #fff;
	}
	.pages-nav .prev-not-used,
	.pages-nav .next-not-used{color:#999999}
	.pages-nav a.noborder {
		border: 0;
	}
	
	.pages-nav a.avtive {
		color: #000;
		border-color: #000
	}
	
	.friend-code-img-2 {
		position: absolute;
		left: 35%;
		top: 35%
	}
    </style>
	<body class="invite-bg">
	    <input type="hidden" value="${userId}">
		<div class="invite-active">
			<img src="${ctx}/img/act-2017-7/active201707-01.jpg" alt="" />
			<img src="${ctx}/img/act-2017-7/active201707-02.jpg" />
			<c:if test="${isLogin eq 0 }">
			<!--未登录-->
			<div class="invite-position">
				<img src="${ctx}/img/act-2017-7/active201707-08.jpg" alt="" />
				<div class="unload-cont invite-cont">
					<a href="#" class="down-btn hy-jumpLogin">立即登录</a>
					<p>登录后获取专属邀请链接</p>
						
				</div>
			</div>
			</c:if>
			<c:if test="${isLogin eq 1 }">
			<!--已登录-->
			<div class="invite-position">
				<img src="${ctx}/img/act-2017-7/active201707-03.jpg" />
				<div class="invite-cont">
				<div class="invite-copy-hint">复制成功，快转发给好友吧!</div>
					<div class="copy">
						<a href="javascript:;" class="clip-btn copy-btn" data-clipboard-text="${userId}" id="copynum">复制邀请码</a>
						<span id="co1">${userId}</span>
						<p>邀请好友注册时在“推荐人”一栏中填写您的邀请码即可。</p>
					</div>
					<div class="down">
						<div class="down-left">
							<div class="down-left-div friend-code-div">
									<input type="hidden" id="qrcodeValue" value="${webCatLink}" />
									<div id="qrcode" class="friend-code-img-1"></div>
										<img src="${ctx}/img/act-2017-7/LOGO.png" class="friend-code-img-2" style="width:28%"/>
								</div>
						</div>
						<div class="down-right">
							<span id="co2">${webCatLink}</span>

						</div>
						<div class="clear"></div>
					</div>
					<div class="button">
						<div class="down-left">
							<a href="${jumpCommand}://jumpH5Encode/?{'url':'${serverUrl}/appUser/getQrCodeAction.do?sign=${sign}&version=${version}&userId=${userId}'}" class="down-btn hy-jumpH5">查看二维码</a>
						</div>
						<div class="down-right">
							<a href="javascript:;" class="down-btn clip-btn hy-partnerShare" data-clipboard-target="#co2" id="copylink">复制链接</a>
						</div>
						<div class="clear"></div>
					</div>
					<div class="text">邀请好友<span class="colorred">点击</span>您的<span class="colorred">邀请链接</span>或<span class="colorred">扫描</span>您的<span class="colorred">二维码</span>进入页面<span class="colorred">注册</span>即可</div>
				</div>
			</div>
			<div class="invite-record">
				<img src="${ctx}/img/act-2017-7/active201707-04.jpg" alt="" />
				<div class="main-tab">
					<ul class="tab-list">
						<li class="active" panel="0">全部</li>
						<li panel="1">已投资</li>
						<li panel="2">未投资</li>
					</ul>
					<ul class="tab-cont">
						<li class="show" panel="0">
							<table border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<th width="23%">手机号</th>
										<th width="14%">用户名</th>
										<th width="20%">注册时间</th>
										<th width="24%">活动投资时间</th>
										<th width="19%">活动投资金额</th>
									</tr>
								</thead>
								<tbody = id="recordInvite">
								</tbody>
							</table>
							<div class="pages-nav" id="new-pagination">
							</div>
						</li>
					</ul>
				</div>
			</div>
			</c:if>
			<img src="${ctx}/img/act-2017-7/active201707-05.jpg" />
			<c:if test="${isLogin eq 0 }">
			    <img src="${ctx}/img/act-2017-7/active201707-09.jpg" />
			</c:if>
			<img src="${ctx}/img/act-2017-7/active201707-06.jpg" />
			<img src="${ctx}/img/act-2017-7/active201707-07.jpg" />

		</div>
	</body>

</html>
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/clipboard.min.js"></script>
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
	/* 跳转二维码页面 */
	//jumpH5()
	/* 复制内容到剪切板 */
	new Clipboard('#copynum');
	new Clipboard('#copylink');
	/* 复制成功提示 */
	hyjfArr.popShow($(".clip-btn").eq(0),"复制成功,快转发给好友吧！")
</script>
<script type="text/javascript">
    var userId = "${userId}";
    var isLogin = "${isLogin}";
    
    $(document).ready(function() {
		if(isLogin == 1){
			getInviteListPage(0, 1, 5);
		}
    });
    
	//产品详情tab切换
	$(".tab-list li").click(function(e) {
		var _self = $(this);
		var idx = _self.attr("panel");
		var panel = $(".tab-cont");
		_self.siblings("li.active").removeClass("active");
		_self.addClass("active");
		//panel.children("li.show").removeClass("show");
		//panel.children("li[panel=" + idx + "]").addClass("show");
		if(isLogin == 1){
			if(idx == 0){
				getInviteListPage(0, 1, 5);
			}else if(idx == 1){
				getInviteListPage(1, 1, 5);
			}else if(idx == 2){
				getInviteListPage(2, 1, 5);
			}
		}
	});
	
	// ajax请求列表
	function getInviteListPage(investType, page, pageSize){
		$.ajax({
				url : "/hyjf-app/activity/inviteseven/getInviteList.do",
				type : 'post',
				dataType : 'json',
				timeout : 60000,
				success : function(data) {
					var inviteListStr = "";
					if(data.data.length == 0){
						
					}else{
						for(var i = 0; i < data.data.length; i++){
							var record = data.data[i];
							inviteListStr += '<tr>' 
								+'<td>' + record.mobileInvited + '</td>'
								+'<td>' + record.usernameInvited + '</td>'
								+'<td>' + getDateTimeFormated(record.registTime) + '</td>'
								+'<td>' + getDateTimeFormated(record.investTime) + '</td>'
								+'<td>' + (record.moneyFirst == 0? "无" : (record.moneyFirst + '元')) + '</td>'
								+'</tr>';
						}
					}
					$("#recordInvite").html(inviteListStr);
					handlePages(data.paginator, "new-pagination", investType, page, pageSize);
				},
				error : function(xhr, textStatus, errorThrown) {
					var err = {
						'result_code' : 1,
						'error_code' : 1,
						'error' : errorThrown
					};
					errorCallback(err);
				},
				data : "investType=" + investType + "&page=" + page + "&pageSize=" + pageSize + "&userId=" + userId
			});

	}
	
	// 分页处理
	function handlePages(paginator, paginationId, investType, page, pageSize) {

		var pageStr = "";
		if (paginator.slider.length > 0) {
			if (paginator.hasPrePage == true) {
				pageStr = pageStr + '<div class="prev" onclick="javascript:getInviteListPage(' + investType + ',' + (page-1) + ',' + pageSize + ')">上一页</div>';
			} else {
				pageStr = pageStr + '<div class="prev prev-not-used">上一页</div>';
			}

			for (var i = 0; i < paginator.slider.length; i++) {
				if (paginator.slider[i] == paginator.page) {
					pageStr = pageStr + '<a href="" class="avtive">' + paginator.page + '</a>';
				} else {
					pageStr = pageStr + '<a href="javascript:getInviteListPage(' + investType + ',' + paginator.slider[i] + ',' + pageSize + ')">'+ paginator.slider[i] + '</a>';
				}
			}
			if (paginator.hasNextPage == true) {
				pageStr = pageStr + '<a class="next" onclick="javascript:getInviteListPage(' + investType + ',' + (page+1) + ',' + pageSize + ')">下一页</a>';
			} else {
				pageStr = pageStr + '<div class="next next-not-used">下一页</div>';
			}
		}
		// 挂载分页
		$("#" + paginationId).html(pageStr);

	}

	
	// 时间格式化
	function getDateTimeFormated(timestamp){
		if(!timestamp){
			return "无";
		}
		
		var time = new Date(parseInt(timestamp)*1000);
		var y = time.getFullYear();//年
		var m = time.getMonth() + 1;//月
		var d = time.getDate();//日
		var h = time.getHours();//时
		var mm = time.getMinutes();//分
		var s = time.getSeconds();//秒
		return time.getFullYear() + "-" + (time.getMonth() + 1) + "-" + time.getDate();
	}

</script>