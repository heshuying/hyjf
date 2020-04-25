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
	<header class="active"></header>
	<article class="active">
		<div class="active-content">
			<!--活动内容-->
			<div class="content">
				<p>活动期间，用户邀请好友注册开户，若好友的首笔投资金额满足一定条件，则邀请人可获得丰厚奖励。</p>
			</div>
			<!--邀请好友登陆前-->
			<div class="invite login">
				<a href="" class="login-btn">立即登录</a>
				<p><span>登录后</span>获取专属邀请链接</p>
			</div>
			<!--邀请好友登陆后-->
			<div class="invite">
				<div class="invite-cont">
					<div class="invite-copy-hint">复制成功，快转发给好友吧!</div>
					<div class="copy">
						<a href="javascript:;" rel="#co1" id="copyCode">复制邀请码</a>
						<span id="co1">182214</span>
						<p>请好友注册时在“推荐人”一栏中填写您的邀请码即可。</p>
					</div>
					<div class="down">
						<div class="down-top">
							<img src="${ctx}/dist/images/active/ewm.png?=20171123" alt="" />
							<p id="co2">https://www.hyjf.com/landingpage/init.do?from-id+182214</p>
						</div>
						<div class="down-mid">
							<a href="javascript:;" class="down-btn">下载二维码</a>
							<a href="javascript:;" class="down-btn" rel="#co2" id="copyLink">复制链接</a>
							<p>邀请好友点击您的邀请链接或扫描您的二维码进入页面注册即可。</p>
						</div>
					</div>
				</div>
			</div>
			<!--奖励设置-->
			<div class="set-up">
				<p>
					<span>被邀请人</span>首投<span>≥2000元</span>，<span>邀请人</span>即可获得<span>28元</span>代金券，单笔投资<span>≥1000元</span>即可使用；<br/>
					<span>被邀请人</span>首投<span>≥5000元</span>，<span>邀请人</span>即可获得<span>58元</span>代金券，单笔投资<span>≥2000元</span>即可使用；<br/>
					此外，邀请人<span>累计</span>邀请人数<span>≥10人</span>，且被邀请人首投均<span>≥5000元</span>，邀请人可额外获得<span>100元</span>代金券，单笔投资<span>≥2000元</span>即可使用。<br/>
				</p>
			</div>
			
			<!--邀请记录-->
			<div class="record">
				<div class="table">
					<!--tab切换-->
					<div class="tab">
						<a class="choice" data-for='all'>全部</a>
						<a data-for='already'>已投资</a>
						<a data-for='unfinished'>未投资</a>
					</div>
					<div class="table-box">
						<!--全部-->
						<div id="all" class="table-div">
							<table border="0" cellspacing="0" cellpadding="0" >
								<thead>
									<tr>
										<th width="6%">手机号</th>
										<th width="8%">用户名</th>
										<th width="10%">注册时间</th>
										<th width="13%">首次投资时间</th>
										<th width="13%">首次投资金额</th>
									</tr>
								</thead>
								<tbody>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>无</td>
									<td>无</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								</tbody>
							</table>
							<div class="pages-nav-active">
									<a class="select page-link">1</a>
									<a class="page-link">2</a>
									<a class="page-link">3</a>
									<a class="page-link">4</a>
									<a class="more">...</a>
									<a class="next">下一页</a>
							</div>
						</div>
						<!--已投资-->
						<div id="already" class="table-div">
							<table border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<th width="6%">手机号</th>
										<th width="8%">用户名</th>
										<th width="10%">注册时间</th>
										<th width="13%">首次投资时间</th>
										<th width="13%">首次投资金额</th>
									</tr>
								</thead>
								<tbody>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>无</td>
									<td>无</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								<tr>
									<td>131****3423</td>
									<td>张＊＊</td>
									<td>2017-7-11</td>
									<td>2017-7-11</td>
									<td>5000元</td>
								</tr>
								</tbody>
							</table>
							<div class="pages-nav-active">
									<a class="select page-link">1</a>
									<a class="page-link">2</a>
									<a class="page-link">3</a>
									<a class="page-link">4</a>
									<a class="more">...</a>
									<a class="next">下一页</a>
							</div>
						</div>
						<!--未投资-->
						<div id="unfinished" class="table-div">
							<table border="0" cellspacing="0" cellpadding="0">
								<thead>
									<tr>
										<th width="6%">手机号</th>
										<th width="8%">用户名</th>
										<th width="10%">注册时间</th>
										<th width="13%">首次投资时间</th>
										<th width="13%">首次投资金额</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>131****3423</td>
										<td>张＊＊</td>
										<td>2017-7-11</td>
										<td>无</td>
										<td>无</td>
									</tr>
									<tr>
										<td>131****3423</td>
										<td>张＊＊</td>
										<td>2017-7-11</td>
										<td>无</td>
										<td>无</td>
									</tr>
									<tr>
										<td>131****3423</td>
										<td>张＊＊</td>
										<td>2017-7-11</td>
										<td>无</td>
										<td>无</td>
									</tr>
									<tr>
										<td>131****3423</td>
										<td>张＊＊</td>
										<td>2017-7-11</td>
										<td>无</td>
										<td>无</td>
									</tr>
								</tbody>
							</table>
							<div class="pages-nav-active">
									<a class="select page-link">1</a>
									<a class="page-link">2<a>
									<a class="page-link">3</a>
									<a class="page-link">4</a>
									<a class="more">...</a>
									<a class="next">下一页</a>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!--奖励发放-->
			<div class="grant">
				<p>所有奖励将于活动<span>结束后3个工作日内</span>发放至推荐人汇盈金服账户，用户登陆后于<a>“优惠券”</a>中查看。</p>
			</div>
			<div class="notes">
				<p class="tit">注:</p>
				<p class="word">
					1.仅限投资<a>新手专区</a>、<a>债权投资</a>项目的金额参与活动统计。<br />
					2.本活动所发优惠券均自获得之日起30日内有效，过期作废。<br />
					3.对于恶意刷取代金券者，一经查实，汇盈金服有权没收其所有奖励。<br />
					4.公司及集团内员工不得参与。<br />
				</p>
			</div>
		</div>
		<div class="explain">
			本活动最终解释权归汇盈金服所有
		</div>
		<img src="${ctx}/dist/images/active/yingbi.png" class="yingbi"/>
	</article>
</body>
<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
<script src="${ctx}/dist/js/lib/jquery.qrcode.js"></script>
<script src="${ctx}/dist/js/lib/qrcode.js"></script>
<script src="${ctx}/dist/js/active/code.js"></script>
<script src="${ctx}/dist/js/lib/jquery.zclip.min.js"></script>
   <script>
$(document).ready(function() {
	/*复制内容*/
	$("#copyCode").zclip({   
		path: "${ctx}/dist/js/lib/ZeroClipboard.swf",
		copy: $("#co1").html(),
		afterCopy:function(){/* 复制成功后的操作 */
			$(".invite-copy-hint").show().delay(1500).fadeOut(300)
        }
	});
	$("#copyLink").zclip({   
		path: "${ctx}/dist/js/lib/ZeroClipboard.swf",
		copy: $("#co2").html(),
		afterCopy:function(){/* 复制成功后的操作 */
			$(".invite-copy-hint").show().delay(1500).fadeOut(300)
        }
	});


});
   </script>
</html>