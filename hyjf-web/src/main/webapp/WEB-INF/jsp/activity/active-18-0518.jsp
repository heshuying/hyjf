<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<link rel="stylesheet" href="${ctx}/css/active/active-20180518.css" type="text/css" />
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<article class="active-18-0518">
	<img src="${ctx}/dist/images/active/activity2018/activity20180518/banner.png"></img>
  <section class='ccenter'>
		<div class="active-content content-bg ccenter">
			<h1 class='atitle ccenter'>活动内容</h1>
			<div class="calendar-content ccenter">
				<div class="calendar-info cbox text">
					<div class='info-main'>
						<p class='doubleline'>汇盈金服宠出新花样</p>
						<p class='doubleline'>只想让你们<span class='text-large'>收益</span></p>
						<img src="${ctx}/dist/images/active/activity2018/activity20180518/upupup.png"/>
						<h3>红包大小你说了算！</h3>
					</div>
					<p class='cleft'>活动期间，用户可在活动页上自定义代金券面额，定制成功后即可投资使用。</p>
					<img class='float-img' src="${ctx}/dist/images/active/activity2018/activity20180518/02.png" style='position:absolute;left:0;bottom:-40px;'/>
				</div>
				<div>
					<img style="position:absolute;margin-left:-26px;margin-top:-118px;" src="${ctx}/dist/images/active/activity2018/activity20180518/cline.png"/>
					<img style="position:absolute;margin-left:-26px;margin-top:-25px;" src="${ctx}/dist/images/active/activity2018/activity20180518/cline.png"/>
					<img style="position:absolute;margin-left:-26px;margin-top:70px;" src="${ctx}/dist/images/active/activity2018/activity20180518/cline.png"/>
				</div>
				<div class="calendar-operation cbox">
					<p class="top-line"></p>
					<div class='operation-content'>
						<div class='edit' id='operation-edit'>
							<div class='operation-bg'>
							<img class='float-img' src="${ctx}/dist/images/active/activity2018/activity20180518/01.png" style='position:absolute;right:0;top:20%;'/>
							<div class='edit'>
                <div class='coupon-box'>
									<input class="coupon-amount text" id="coupon-amount"
										placeholder="请输入代金券金额" type='text'></input>
									<label class='text coupon-unit'>元</label>
								</div>
								<div class='amount-error doubleline' id='amount-tips'>
									<p class="cleft error">请输入介于10-200之间的整数，且需为10的整倍数</p>
									<p class="cleft tips">单笔投资达<span class="text text-large coupon-for-loan">3000元</span>可用</p>
								</div>
							</div>
							<div class='success'>
								<p class='text-large'>恭喜！</p>
								<p class='text-large'>您已成功定制<span class="text coupon-num">10</span>代金券</p>
							</div>
							<p class="cleft use-conditions">使用条件：</p>
							<ol class="cleft">
								<li>仅限用于散标投资项目和计划专区</li>
								<li>投资期限>=3个月</li>
								<li class='success'>单笔投资达<span class="coupon-for-loan">3000元</span>可用</li>
							</dl>
							<p class="edit button ccenter" id='submit'>立即定制</p>
							<p class="success button ccenter" id='go-used'>去使用</p>
							<p class='success ccenter'>本券有效期<span class='validity-date'>3</span>天，请及时使用！</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="active-introduction content-bg ccenter">
			<h1 class='atitle ccenter'>活动须知</h1>
			<div class='cbox introduction-box'>
				<div class="cleft">
					<h4>奖励发放</h4>
					<p>代金券定制成功后，由系统自动发放至用户的汇盈金服账户中，用户登录后于“我的奖励”中查看。</p>
				</div>
				<div class="step-box">
					<div class="step">
						<div class="cleft">
							<h1>01-</h1>
							<p>打开汇盈金服官网，点击右上角登录</p>
						</div>
						<p>
							<img src="${ctx}/dist/images/active/activity2018/activity20180518/step01.png"/>
							<img class='float-img' src="${ctx}/dist/images/active/activity2018/activity20180518/03.png" style='position:absolute;right:0;top:20%;height:75px;'/>
						</p>
					</div>
					<div class="step">
						<p>
						<img src="${ctx}/dist/images/active/activity2018/activity20180518/step02.png"/>
						<img class='float-img' src="${ctx}/dist/images/active/activity2018/activity20180518/04.png" style='position:absolute;left:0;top:50%;height:90px;'/></p>
						<div class="cleft">
							<h1>02-</h1>
							<p>进入个人中心</p>
							</div>
					</div>
					<div class="step">
						<div class="cleft">
							<h1>03-</h1>
							<p>点击我的奖励，查看我的优惠券</p>
							</div>
						<p>
						<img src="${ctx}/dist/images/active/activity2018/activity20180518/step03.png"/>
						<img class='float-img' src="${ctx}/dist/images/active/activity2018/activity20180518/05.png" style='position:absolute;right:0;top:70%;height:90px;'/></p>
					</div>
				</div>
			</div>
		</div>
		<div class='introduction-tips cleft'>
			<h4>注：</h4>
			<ol>
				<li>活动期间，每位用户最多可定制3张代金券。</li>
				<li>定制的代金券使用或过期后，方可定制下一张。</li>
				<li>定制的代金券自获得之日起3日内有效，过期作废。</li>
			</ol>
		</div>
	</section>
	<div class='footer ccenter doubleline'>
		<p>集团内部员工不得参与</p>
		<p>汇盈金服保留在法律规定范围内对上述规则进行解释的权利</p>
	</div>
</article>
<div class='model hide'>
	<div class='modelbox'>
		<div class='mtitle'></div>
		<div class='mcontent'></div>
		<div class='confirm' id='modelbtn'>
			<p class='choose-tips'>注意：定制的代金券使用或过期后，方可定制下一张。</p>
			<p class='mbtn submbtn' id='submbtn'></p><p class='mbtn cancel' id='mbtn'></p>
		</div>
	</div>
</div>
<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
<script src="${ctx}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
<script src="${ctx}/dist/js/lib/echarts.common.min.js"></script>
<script src="${ctx}/dist/js/lib/jquery.placeholder.min.js"></script>
<script src="${ctx}/dist/js/lib/nprogress.js"></script>

<script src="${ctx}/dist/js/utils.js"></script>
<script src="${ctx}/js/common/common.js?version=${version}" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/activity/active-18-0518/active-18-0518.js"></script>
</body>
</html>
