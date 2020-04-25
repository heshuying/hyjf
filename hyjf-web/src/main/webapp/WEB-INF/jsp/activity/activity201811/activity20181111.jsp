<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>双十一狂欢大作战 - 汇盈金服</title>
<jsp:include page="/head.jsp"></jsp:include>
	<style>
		body {
			padding: 0;
			margin: 0;
			min-width: 1200px;
			overflow-x:auto;
		}
		.overlayer{
			filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=80);
			opacity: .8;
		}

		.activity20181111 {
			width: 100%;
			float: left;
			padding: 568px 0 80px;
			background: url(${ctx}/dist/images/active/activity2018/activity201811/activity20181111-bg.jpg) center top no-repeat #060743;

		}

		.container-activity20181111 {
			width: 1095px;
			margin: 0 auto;
			overflow-x:auto;
		}

		.activity-tab {
			height: 103px;
			font-size: 0;
		}

		.activity-tab>.tab-item {
			height: 103px;
			background: #FF834D;
			margin-right: 3px;
			text-align: center;
			padding: 0 50px;
			display: inline-block;
			color: #ffffff;
			cursor: pointer;
		}

		.activity-tab>.tab-item.active {
			color: #FF834D;
			background: #fff;
		}

		.activity-tab>.tab-item:first-child {
			-webkit-border-radius: 10px 0 0 10px;
			-moz-border-radius: 10px 0 0 10px;
			border-radius: 10px 0 0 10px;
		}

		.activity-tab>.tab-item:last-child {
			-webkit-border-radius: 0 10px 10px 0;
			-moz-border-radius: 0 10px 10px 0;
			border-radius: 0 10px 10px 0;
		}

		.activity-tab>.tab-item>.date {
			font-size: 30px;
			margin-top: 13px;
			line-height: 42px;
		}

		.activity-tab>.tab-item>.name {
			font-size: 20px;
			line-height: 28px;
		}

		.activity-tab-panel {
			padding-top: 30px;
		}

		.activity-tab-panel-item {
			display: none;
		}

		.activity-tab-panel-item.active {
			display: block
		}

		.time-area-tab {
			height: 40px;
			box-sizing: border-box;
			border: 1px solid #FFFFFF;
			border-radius: 100px;
			display: inline-block;
			clear: both;
			margin-bottom: 34px;
		}

		.time-area-tab>.time-area-tab-item {
			width: 196px;
			height: 38px;
			border-radius: 18px;
			font-size: 24px;
			color: #ffffff;
			display: inline-block;
			line-height: 38px;
			text-align: center;
			cursor: pointer;
            position: relative;
		}

		.time-area-tab>.time-area-tab-item.active {
			background: #ffffff;
			color: #070700;
		}
        .time-area-tab>.time-area-tab-item.active:before{
			position: absolute;
			content:' ';
			width: 10px;
			height: 8px;
			left:50%;
			top: -8px;
			margin-left: -5px;
			background:url(${ctx}/dist/images/active/activity2018/activity201811/arrow.png) 0 0 no-repeat;
			background-size: 10px 8px;

        }
		.time-area-panel {
			background: #23096C;
			border: 2px solid #5F33D9;
			border-radius: 36px;
			box-sizing: border-box;
			padding: 50px;
		}

		.time-area-panel-item {
			display: none;
		}

		.time-area-panel-item.active {
			display: block;
		}

		.time-area-panel-item>.status-desc {
			font-size: 40px;
			color: #88FBFF;
			letter-spacing: 0;
			text-align: center;
		}
		.time-area-panel-item>.status-desc[ongoing='2'],
		.time-area-panel-item>.status-desc[ongoing='4']{
			color: #5F33D9;
		}

		.time-area-panel-item>.awards {
			margin-top: 35px;
			font-size: 0;
		}

		.time-area-panel-item>.awards>.awards-item {
			margin-right: 38px;
			width: 305px;
			display: inline-block;
			box-sizing: border-box;
		}

		.time-area-panel-item>.awards>.awards-item:last-child {
			margin-right: 0;
		}
		.time-area-panel-item>.awards>.awards-item .top-bg{
			height: 40px;
			background:url(${ctx}/dist/images/active/activity2018/activity201811/award-item-bg.png) top left no-repeat;
			background-size: 100%;
		}
		.award-main {
			border-radius: 0 0 15px 15px;
			background: #fff;
			padding-bottom: 45px;
			padding-top: 40px;
		}

		.award-main .name {
			font-size: 42px;
			color: #23096C;
			text-align: center;
		}

		.award-main .limit {
			font-size: 16px;
			color: #23096C;
			text-align: center;
			width: 210px;
			margin:40px auto 36px;
			text-align: left;
			line-height: 22px;
		}

		.award-main .progress {
			background: #E9E9E9;
			width: 210px;
			margin: 0 auto 5px;
			height: 10px;
			-webkit-border-radius: 10px;
			-moz-border-radius: 10px;
			border-radius: 10px;
		}

		.award-main .progress .done {
			background: #499DFC;
			height: 10px;
			-webkit-border-radius: 10px;
			-moz-border-radius: 10px;
			border-radius: 10px;
		}

		.award-main .rest {
			font-size: 18px;
			width: 210px;
			margin: 0 auto 0;
			color: #499DFC;
		}

		.award-main .award-btn {
			background: #BABABA;
			width: 230px;
			height: 54px;
			text-align: center;
			line-height: 54px;
			font-size: 26px;
			color: #FFFFFF;
			border-radius: 54px;
			margin: 58px auto 0;
			cursor: pointer;
			display: block;
			text-decoration: none;
		}

		.award-main .award-btn.ongoing {
			background-image: linear-gradient(-218deg, #FF268E 0%, #FFE066 100%);
		}
		.award-main .award-btn.ongoing:hover{
			background-image: linear-gradient(-218deg, #FF268E 100%, #FFE066 100%);;
		}
		.time-area-panel-item>.info {
			margin-top: 55px;
			text-align: center;
			font-size: 22px;
			color: #FF834D;
		}

		.time-area-panel-item>.info  .info-tip {
			color: #88FBFF;
		}
		.activity3-head{
			height:40px;
		}
		.activity3-total{
			font-size: 24px;
			color: #88FBFF;
			width: 800px;
			float: left;
		}
		.activity3-total a{
			color: #FF834D;
			text-decoration: none;
		}
		.activity3-btn{
			float:right;
			background: #FF834D;
			border-radius: 10px;
			width: 160px;
			height: 40px;
			color:#ffffff;
			line-height: 40px;
			text-align: center;
			cursor: pointer;
			text-decoration: none;
		}
		.activity3-btn:hover{
			background-color:#FFA84D;
		}
		.activity3-tip{
			font-size: 14px;
			color: #88FBFF;
			margin-top: 15px;
		}
		.activity3-awards,
		.activity3-example{
			width: 100%;
		}
		.activity3-awards{
			font-size: 0;
			margin-top:35px;
		}
		.activity3-awards .award-item{
			width: 305px;
			height: 406px;
			display: inline-block;
			margin-right:38px;
			box-sizing: border-box;
			background: #060743;
			border: 2px solid #4526AD;
			border-radius: 13px;
			color: #88FBFF;
			text-align: center;
		}

		.activity3-awards .award-item.got{
			border-color:#FF8579;
			color:#ffffff;
			background:url(${ctx}/dist/images/active/activity2018/activity201811/got-award.png) -2px -2px no-repeat;
			background-size: 91px 114px;
		}

		.activity3-awards .award-item .award-img{
			width: 200px;
			height: 200px;
			margin: 50px auto;
		}
		.activity3-awards .award-item .award-img img{
			max-height: 100%;
		}
		.activity3-awards .award-item .name{
			font-size: 24px;
			margin-top: 30px;
		}
		.activity3-awards .award-item .name span{
			font-size: 0.7em;
		}
		.activity3-awards .award-item .limit{
			font-size: 14px;
			margin-top: 10px;
		}
		.activity3-awards .award-item:last-child{
			margin-right:0;
		}
		.activity3-example{
			margin-top:40px;
			font-size: 16px;
			color: #CAB7FF;
			line-height: 38px;
		}
		.activity-rule {
			font-size: 16px;
			line-height: 38px;
			margin: 70px auto 0;
			width: 1008px;
			color:#fff;
		}

		.activity-rule ol {
			margin: 0;
			padding-left: 20px;

		}
		.login-btn{
			font-size: 26px;
			color: #FFFFFF;
			width: 370px;
			height: 54px;
			line-height: 54px;
			background-image: linear-gradient(-218deg, #FFE066 0%, #FF268E 100%);
			margin: 57px auto 0;
			text-align: center;
			border-radius: 54px;
			cursor: pointer;
			display: none;
			text-decoration: none;
		}
		.login-btn:hover{
			background: #FF268E;
		}
		.activity3-login-btn{
			display: none;
		}
		.need-login{
			display: block;
		}
		.need-login-inline{
			display: inline;
		}
		.unlogin .award-btn{
			display: none;
		}
		.unlogin .login-btn{
			display: block
		}
		.unlogin .need-login-inline,
		.unlogin .need-login{
			display: none;
		}
		.unlogin .activity3-login-btn{
			display: inline-block;
		}
		.unlogin .time-area-panel-item .info{
			display: none;
		}

		.activity-popup{
			position: fixed;
			left: 50%;
			top: 40%;
			width: 440px;
			height: 290px;
			background: url(${ctx}/dist/images/active/activity2018/activity201811/failed-bg.png) left top no-repeat;
			background-size: 380px 290px;
			margin-left: -220px;
			margin-top:-145px;
			z-index: 99;
			display: none;
			padding-top:80px;
			box-sizing: border-box;
		}

		.activity-popup-main{
			width:380px;
			margin:0 auto 0;
			text-align: center;
			color:#ffffff;
		}
		.activity-popup-main .result-title{
			font-size: 35px;
			line-height: 48px;
			width: 260px;
			margin: 0 auto;
		}
		.activity-popup-main .result-desc{
			font-size: 12px;
			line-height: 15px;
			margin-top:10px;
		}
		.activity-popup-main .activity-popup-btn{
			width: 130px;
			height: 32px;
			line-height: 32px;
			cursor: pointer;
			text-decoration: none;
			display: block;
			margin:auto;
			background: #fff;
			color: #D0021B;
			border-radius: 100px;
			margin-top:24px;
		}
		.activity-popup-main .activity-popup-close{
			background: url(${ctx}/dist/images/active/activity2018/activity201811/alert-close.png) center center no-repeat;
			width: 28px;
			height: 28px;
			background-size: 28px;
			position: absolute;
			top: 56px;
			right: 40px;
			cursor: pointer;
		}
		.activity-popup[status='000']{
			background-image: url(${ctx}/dist/images/active/activity2018/activity201811/success-bg.png);
		}
		.activity-popup[status='800'],
		.activity-popup[status='801'],
		.activity-popup[status='803'],
		.activity-popup[status='999'],
		.activity-popup[status='804']{
			padding-top:100px;
		}
		/* .activity-popup[status='803'] {
			padding-top:100px;
		} */

	</style>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
	<div class="activity20181111 <c:if test="${cookie['hyjfUsername'].value == null || cookie['hyjfUsername'].value == ''}">unlogin</c:if>">
		<div class="container-activity20181111">
			<div class="activity-tab" id="activityTab">
				<div class="tab-item active" data-tab="0">
					<div class="date">11月9日</div>
					<div class="name">活动一：加息先行</div>
				</div>
				<div class="tab-item" data-tab="1">
					<div class="date">11月10日</div>
					<div class="name">活动二：红包到达战场</div>
				</div>
				<div class="tab-item" data-tab="2">
					<div class="date">11月9日～11月15日</div>
					<div class="name">活动三：iPhone疯狂抢</div>
				</div>
			</div>
			<div class="activity-tab-panel" id="activityPanel">
				<!-- 活动一 -->
				<div class="activity-tab-panel-item" data-panel="0">
					<div class="time-area-tab" id="areaTab-0">
						<div class="time-area-tab-item active" data-tab="0">
							11:11～13:11
						</div>
						<div class="time-area-tab-item" data-tab="1">
							13:11～15:11
						</div>
						<div class="time-area-tab-item" data-tab="2">
							17:11～19:11
						</div>
					</div>
					<div class="time-area-panel" id="areaPanel-0">
						<!-- 活动一  时段1 -->
						<div class="time-area-panel-item active" data-panel="0">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.8%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.9%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">1.0%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>
						<!-- 活动一  时段2 -->
						<div class="time-area-panel-item" data-panel="1">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.8%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.9%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">1.0%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>
						<!-- 活动一  时段3 -->
						<div class="time-area-panel-item" data-panel="2">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.8%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">0.9%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">1.0%加息券</div>
										<div class="limit">使用条件：债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>
					</div>
				</div>
				<!-- 活动二 -->
				<div class="activity-tab-panel-item" data-panel="1">
					<div class="time-area-tab" id="areaTab-1">
						<div class="time-area-tab-item active" data-tab="0">
							11:11～13:11
						</div>
						<div class="time-area-tab-item" data-tab="1">
							13:11～15:11
						</div>
						<div class="time-area-tab-item" data-tab="2">
							17:11～19:11
						</div>
					</div>
					<div class="time-area-panel" id="areaPanel-1">
						<div class="time-area-panel-item active" data-panel="0">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">11元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 3个月
											单笔投资金额 ≥ 5000元 <br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">88元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 6个月
											单笔投资金额 ≥ 3.5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">111元代金券</div>
										<div class="limit">使用条件：投资期限 ≥6个月
											单笔投资金额 ≥5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>
						<div class="time-area-panel-item" data-panel="1">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">11元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 3个月
											单笔投资金额 ≥ 5000元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">88元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 6个月
											单笔投资金额 ≥ 3.5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">111元代金券</div>
										<div class="limit">使用条件：投资期限 ≥6个月
											单笔投资金额 ≥5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>
						<div class="time-area-panel-item" data-panel="2">
							<div class="status-desc">秒杀已结束</div>
							<div class="awards">
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">11元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 3个月
											单笔投资金额 ≥ 5000元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">88元代金券</div>
										<div class="limit">使用条件：投资期限 ≥ 6个月
											单笔投资金额 ≥ 3.5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
								<div class="awards-item">
									<div class="top-bg"></div>
									<div class="award-main">
										<div class="name">111元代金券</div>
										<div class="limit">使用条件：投资期限 ≥6个月
											单笔投资金额 ≥5万元<br/>
											债转除外</div>
										<div class="progress">
											<div class="done"></div>
										</div>
										<div class="rest">剩余：0张</div>
										<a href="javascript:;" class="award-btn">立即秒杀</a>
									</div>
								</div>
							</div>
							<a href="${ctx}/user/login/init.do" class="login-btn">立即登录</a>
							<div class="need-login info">剩余<span class="total-rest">2</span>次秒杀机会 <span class="info-tip">（1 次秒杀成功即消耗 1次秒杀机会）</span></div>
						</div>

					</div>
				</div>
				<!-- 活动三 -->
				<div class="activity-tab-panel-item" data-panel="2">
					<div class="time-area-panel">
						<div class="activity3-head">
							<div class="activity3-total">活动期间 我的累计年化投资金额：<span class="need-login-inline" id="totalRest"></span>
								<a href="${ctx}/user/login/init.do" class="activity3-login-btn">登录可见</a></div>
                            <c:if test="${cookie['hyjfUsername'].value != null && cookie['hyjfUsername'].value != ''}"><a class="activity3-btn" href="${ctx}/hjhplan/initPlanList.do">立即投资</a></c:if>

						</div>
						<div class="clearfloat"></div>
						<div class="activity3-tip">累计年化投资金额=SUM（m1*n1+m2*n2+...）/12，m为单笔投资金额，n为单笔投资期限。</div>
						<div class="activity3-awards" id="activity3Awards">
							<div class="award-item" data-id="3">
								<div class="award-img">
									<img src="${ctx}/dist/images/active/activity2018/activity201811/award3.png" style="max-height:110%;margin-top:-5%;" alt="" />
								</div>
								<div class="name">iPhone XS Max <span> (64G)</span></div>
								<div class="limit">累计年化投资金额 ≥ 100万元</div>
							</div>
							<div class="award-item" data-id="2">
								<div class="award-img">
									<img src="${ctx}/dist/images/active/activity2018/activity201811/award2.png" alt="" />
								</div>
								<div class="name">iPhone XS <span> (64G)</span></div>
								<div class="limit">90万元 ≤ 累计年化投资金额 ＜ 100万元</div>
							</div>
							<div class="award-item" data-id="1">
								<div class="award-img">
									<img src="${ctx}/dist/images/active/activity2018/activity201811/award1.png" alt="" />
								</div>
								<div class="name">iPhone XR <span> (64G)</span></div>
								<div class="limit">70万元 ≤ 累计年化投资金额 ＜ 90万元</div>
							</div>
						</div>
						<div class="activity3-example">
							例如：活动期间，赵某分别投资1200元（30天期限的新手标）、50万（12个月期限的散标）、50万（12个月期限的智投标）、
							10万（3个月期限的债转标），那么赵某的累计年化投资金额＝1200*30/360+50万*12/12+50万*12/12＝100.01万（债转除外），
							则赵某获得一部iPhone XS Max手机。
						</div>
					</div>
				</div>
			</div>

			<div class="activity-rule">
				活动规则：<br />
				<ol>
					<li>活动对象为汇盈金服全部注册用户。</li>
					<li>秒杀到的加息券和代金券奖励，由系统实时发放至用户账户，有效期均为7天（自获得之日起开始计算），逾期不补，且不可叠加使用。</li>
					<li>累计年化投资金额计算期间为活动期间（2018.11.09 00:00:00~2018.11.15 23:59:59），投资期限不限，债转除外。</li>
					<li>新手标和散标按投资成功时间、智投按加入时间来判定是否为活动期间内的投资。</li>
					<li>新款iPhone手机由工作人员在活动结束后，15个工作日内电话联系获奖用户进行发放。<br/>提示：用户汇盈金服账号所绑定的手机号，即为获奖联系方式，请获奖用户注意接听来电，以免耽误领奖；如未能通过该手机号联系到您，
						则视为主动放弃领奖，不予后补。</li>
					<li>用户获得奖励以后，奖励不可更换。</li>
					<li>本次活动奖励，不与汇盈金服其他活动奖励同享。</li>
					<li>如有疑问，请致电汇盈金服全国免费热线400-900-7878咨询。</li>
					<li>汇盈金服保留在法律规定范围内对上述规则进行解释的权利。</li>
				</ol>
			</div>
		</div>
	</div>


	<div class="overlayer"></div>
	<div class="activity-popup">
		<div class="activity-popup-main">
			<div class="result-title"></div>
			<div class="result-desc">
			</div>
			<a href="${ctx}/hjhplan/initPlanList.do" class="activity-popup-btn" onclick="closeActivityAlert()">立即投资</a>
			<div class="activity-popup-close" onclick="closeActivityAlert()">&nbsp;</div>

		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
        var areaStatusDirc = ['开始秒杀', '秒杀进行中', '秒杀已结束', '倒计时', '已抢完，秒杀已结束']; // 时段活动状态查询
        // 页面初始化数据
        var pageData = {};

        // 初始化tab
        initTab('activityTab', 'activityPanel');
        initTab('areaTab-0', 'areaPanel-0');
        initTab('areaTab-1', 'areaPanel-1');
        // 设置进行中默认活动3
        changeActivity(3);

        // 请求数据
        getInitData();

        $(".award-btn").click(function () {
            var self = $(this);
            if (self.hasClass('ongoing')) {
                var data = {
                    id: self.data('id'),
                    activity: self.data('activity'),
                    activityToken: pageData.otherData.activityToken
                }
                // 进行秒杀
                postAwards(data);
            }
            return false
        })
        /*
         * 设置登录
         */
        function setLogined() {
            $(".activity20181111").removeClass('unlogin');
        }

        function getInitData() {
            $.ajax({
                url: '${ctx}/twoeleven/init.do',
                type: 'get'
			}).done(function(res){
                // 渲染页面  请求数据后
                if(res.status != '999' && res.status != '800'){
                    // 设置登录
                    setLogined();
                }
                pageData = res.data;
                renderView();
			}).fail(function () {
                activityAlert('800', '服务异常<br/>请稍后再试');
            })
        }
        /*
         * 秒杀
         */
        function postAwards(data) {
            $.ajax({
                url: '${ctx}/twoeleven/seckill.do',
                type: 'post',
                data: data
            })
                .done(function (res) {
                    if(res.status == '000'){
                        activityAlert(res.status, res.awardVal+res.awardType);
					}else{
                        activityAlert(res.status, res.statusDesc);
                    }
                    getInitData();//重新请求数据
                })
                .fail(function () {
                    activityAlert('800', '服务异常<br/>请稍后再试');
                })
        }


        /*
         * 活动弹窗
         */
        function activityAlert(status, statusDesc) {
            // activityAlert('000','0.8%加息券') // 获奖
            // activityAlert('999','0.8%加息券') // 获奖
            // activityAlert('800','刷新太频繁<br/>秒杀无效') // token验证非法，提示'刷新太频繁了，秒杀无效'
            // activityAlert('801','再接再厉，继续加油哦～') // 未抢到
            // activityAlert('802','今日秒杀机会<br/>已用完') // 活动1机会已用完
            // activityAlert('803','秒杀机会<br/>已用完') // 活动2机会已用完
            // activityAlert('804','秒杀奖品不存在') // 异常
            // activityAlert('804','当前进行没有进行中的活动') // 异常
            // activityAlert('804','活动未开始') // 异常
            var pop = $(".activity-popup");
            pop.attr('status', status);
            if (status == '000') {
                // 已中奖
                pop.find('.activity-popup-btn').show();
                pop.find('.result-title').text('秒杀成功')
                pop.find('.result-desc').html('恭喜您获得 1 张 ' + statusDesc + '<br>已发放至您的账户');
            } else if (status == '801') {
                // '801': 未抢到 显示‘再接再厉，继续加油哦～’
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-title').text('秒杀失败')
                pop.find('.result-desc').html('再接再厉，继续加油哦～');
            } else if (status == '802') {
                // '802': 活动1机会已用完 （提示带明日再来：11:11 ~ 13:11）
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-title').html('今日秒杀机会<br/>已用完')
                pop.find('.result-desc').html('明日再来：11:11 ~ 13:11');
            } else if (status == '803') {
                // '803': 活动2机会已用完
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-title').html('秒杀机会<br/>已用完')
                pop.find('.result-desc').html('');

            }else if (status == '999') {
                // '803': 活动2机会已用完
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-title').html('用户未登录<br/>请先登录')
                pop.find('.result-desc').html('');

            } else {
                // 其他情况
                // '800': token验证非法，提示'刷新太频繁了，秒杀无效'
                // '804': 秒杀奖品ID 为空或者不存在；当前进行活动为空；活动未开始；或其他失败状态
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-desc').html('');
                pop.find('.result-title').html(statusDesc);
            }
            $(".overlayer").fadeIn();
            pop.fadeIn();
        }
        /*
         * 关闭弹窗
         */
        function closeActivityAlert() {
            var pop = $(".activity-popup");
            setTimeout(function(){
                pop.find('.activity-popup-btn').hide();
                pop.find('.result-title').html('')
                pop.find('.result-desc').html('');
            },500);
            $(".overlayer").fadeOut();
            pop.fadeOut();
        }
        /*
         *	渲染页面数据
         *
         */
        function renderView() {
            var totalRest = pageData.otherData.restTimes; // 剩余次数

            renderActive(1, totalRest); // 渲染活动1
            renderActive(2, totalRest); //渲染活动2
            renderActive3(); // 渲染活动3
            changeActivityTab();  // 切换tab
        }
        /*
         *	切换tab
         *
         */
        function changeActivityTab() {
            // 切换到当前活动 倒序遍历 有效状态即切换
            var activityIndex = 3;  // 当前活动
            var activity12Index = 0; // 当前时段
            var now = pageData.otherData.now;
            if (now < pageData.activity1.data[2].endTime) {        //小于2018-11-09 19:11  默认活动1高亮
                activityIndex = 1;
                if (now < pageData.activity1.data[0].endTime) {      //小于2018-11-09 13:11  默认活动1》第一时间段高亮
                    activity12Index = 1;
                } else if (now < pageData.activity1.data[1].endTime) {  //小于2018-11-09 15:11  默认活动1》第二时间段高亮
                    activity12Index = 2;
                } else {                        //默认活动1》第三时间段高亮
                    activity12Index = 3;
                }
            } else if (now < pageData.activity2.data[2].endTime) {  //小于2018-11-10 19:11  默认活动2高亮
                activityIndex = 2;
                if (now < pageData.activity2.data[0].endTime) {      //小于2018-11-10 13:11  默认活动2》第一时间段高亮
                    activity12Index = 1;
                } else if (now < pageData.activity2.data[1].endTime) {  //小于2018-11-10 15:11  默认活动2》第二时间段高亮
                    activity12Index = 2;
                } else {                        //默认活动2》第三时间段高亮
                    activity12Index = 3;
                }
            } else {
                activityIndex = 3        //否则2018-11-10 19:11  默认活动3高亮
            }
            changeActivity(activityIndex, activity12Index);
        }
        // 渲染活动1，2
        function renderActive(activeNum, totalRest) {
            // 设置活动一
            var actData = pageData['activity' + activeNum]; // 活动数据
            var actAreaData = actData.data; // 活动时段数据
            var actTab = $('#activityTab').children('[data-tab=' + (activeNum - 1) + ']');
            var actPanel = $('#activityPanel').children('[data-panel=' + (activeNum - 1) + ']');
            // actTab.children('.name').text(actData.name); // 活动名暂时写死
            actTab.children('.date').text(fmtDate(actData.startDate)); // 活动日期
            // 活动时段
            actAreaData.forEach(function (item, index) {
                var actArea = actPanel.children('.time-area-panel').children('[data-panel=' + index + ']'); // 当前时段
                var actState = actArea.children('.status-desc');
                var areaTab = actPanel.children('.time-area-tab').children('[data-tab=' + index + ']'); // 当前时段
                areaTab.text(fmtTime(item.startTime) + "~" + fmtTime(item.endTime)); // 活动时段起止时间
				// 设置活动状态样式
                actState.attr('ongoing',item.status);
                // 设置活动状态
                if (item.status == '1' || item.status == '2' || item.status == '4') { // 进行中|已结束|已抢完且已结束
                    actState.text(areaStatusDirc[item.status])
                } else if (item.status == '0') { //未开始
                    actState.text(fmtTime(item.startTime) + ' ' + areaStatusDirc[item.status])
                } else if (item.status == '3') { // 倒计时
                    actState.html(areaStatusDirc[item.status] + '  <span class="timer"></span>');
                    var actStateTimer = actState.children('.timer');
                    actStateTimer.data({
                        start: pageData.otherData.now,
                        end: item.startTime
                    })
                    timeCountdown(actStateTimer,renderView);
                }
                actArea.find('.total-rest').text(totalRest);
                // 渲染奖品
                item.listAwards.forEach(function (award, awardId) {
                    var awardEl = actArea.children('.awards');
                    var currentAward = awardEl.children('.awards-item').eq(awardId);
                    var btn = currentAward.find('.award-btn'); // 秒杀按钮
                    currentAward.find('.name').text(award.name + ['', '%加息券', '元代金券'][activeNum]);
                    currentAward.find('.progress').children().css('width', 100 - award.progress + '%');
                    currentAward.find('.rest').text('剩余：' + award.rest + '张');
                    // 设置按钮状态 进行中
                    if (item.status == 1 && award.rest > 0) {
                        btn.addClass('ongoing');
                        btn.data({
                            id: award.id,
                            activity: activeNum
                        }); // 设置id
                    }else{
                        // 移除按钮样式
                        btn.removeClass('ongoing');
					}
                })
            })
        }
        // 渲染活动3
        function renderActive3(totalRest) {
            var act3 = pageData.activity3;
            var act3Tab = $('#activityTab').children('[data-tab=2]');
            $("#totalRest").text(act3.total + '元'); // 剩余次数
            act3Tab.children('.date').text(fmtDate(act3.startDate) + '~' + fmtDate(act3.endDate));
            // act3Tab.children('.name').text(act3.name); // 活动名 暂时写死
            if (act3.award > 0) { // 设置获奖
                $("#activity3Awards").children('[data-id=' + act3.award + ']').addClass('got').siblings().removeClass('got');
            }
        }

        /*
         *  倒计时
         * @params ele 倒计时元素ele
         * @params ondone 倒计时结束回调
         */
        function timeCountdown(ele, ondone) {
            var ele = ele,
                s = parseInt(ele.data("end")/1000) - parseInt(ele.data("start")/1000);
            utils.timer(ele, s, ondone);
        }

        /*
         *  切换到对应tab
         * @param activity 活动一：1 活动二：2 活动3 ：3
         * @param area 时段1，2，3 活动3可不传
         */
        function changeActivity(activity, area) {
            var activityIndex = activity - 1;
            var areaIndex = area - 1;
            // 切换到活动对应tab
            updateTab('activityTab', 'activityPanel', activityIndex);
            // 切换到时段对应tab
            updateTab('areaTab-' + activityIndex, 'areaPanel-' + activityIndex, areaIndex);
        }
        /*
         * 初始化tab事件
         * @params tabId 标签id
         * @params panelId 面板id
         * @params index 可选 手动设置选中序号
         */
        function updateTab(tabId, panelId, index) {
            // 标签容器
            var tabEl = $("#" + tabId);
            // 面板容器
            var panelEl = $("#" + panelId);
            // 当前选中标签元素
            var tab = tabEl.children('[data-tab=' + index + ']');
            // 当前选中标签对应面板
            var panel = panelEl.children('[data-panel=' + index + ']');
            // 清除原状态
            tab.siblings('.active').removeClass('active');
            panel.siblings('.active').removeClass('active');
            // 设置新状态
            tab.addClass('active');
            panel.addClass('active');
        }
        /*
         * 初始化tab事件
         * @params tabId 标签id
         * @params panelId 面板id
         * @params index 可选 手动设置选中序号
         */
        function initTab(tabId, panelId) {
            // 标签容器
            var tabEl = $("#" + tabId);
            // 标签序列
            var tabs = tabEl.children('[data-tab]');
            // 面板容器
            var panelEl = $("#" + panelId);
            // 面板序列
            var panels = panelEl.children('[data-panel]');
            // 标签选择
            tabs.click(function () {
                // 当前选中序号
                var id = $(this).data('tab');
                updateTab(tabId, panelId, id);
            })
        }
        // 格式化日期
        function fmtDate(date) {
            var d = new Date(date);
            return d.getMonth() + 1 + '月' + d.getDate() + '日';
        }
        // 格式化事件
        function fmtTime(date) {
            var d = new Date(date);
            return d.getHours() + ':' + d.getMinutes();
        }
	</script>
</body>