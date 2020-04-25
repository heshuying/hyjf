<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
		.hf-withdraw{
			background: #fff;
			font-size: 16px
		}
		.hf-withdraw .banner{
			background:url(/dist/images/hfWithdraw/banner@2x.png);
			background-position: center center;
			height:300px;
			background-size: 3000px 300px;
		}
		.title{
			background:url(/dist/images/hfWithdraw/rule@2x.png) no-repeat;
			background-size: 800px;
			background-position: center center;
			height: 35px;
			line-height: 34px;
			width: 800px;
			text-align: center;
			margin: 0 auto;
			font-size: 32px;
			color: #4078F4;
		}
		.subheading{
			font-size: 16px;
			margin-top: 20px;
		}
		.step-box1{
			padding: 50px 0;
			background: #EFF5FF;
			text-align: center;
		}
		.step-box2{
			padding: 50px 0;
			background: #F9FBFF;
			text-align: center;
		}
		
		.step-box3{
			padding: 50px 0;
			background: #E9F1FF ;
			text-align: center;
		}
		.step-box4{
			padding: 50px 0;
			background: #EFF5FF;
			text-align: center;
		}
		.step a{
			display: inline-block;
			position: relative;
			width: 205px;
			height: 80px;
			background-size: 205px 80px;
			line-height: 80px;
			cursor: pointer;
			margin-bottom: 35px;
		}
		.step .show{
			color: #fff;
		}
		.step .s1{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-1-white@2x.png);
		}
		.step .s1.show{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-1-blue@2x.png);
		}
		.step .s2{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-2-white@2x.png);
		}
		.step .s2.show{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-2-blue@2x.png);
		}
		.step .s3{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-6-white@2x.png);
		}
		.step .s3.show{
			background-image: url(/dist/images/hfWithdraw/buzhou-box-6-blue@2x.png);
		}
		.step span{
			border-radius: 100%;
			border: 1px solid #eee;
			color: #999;
			background: #eee;
			width: 30px;
			height:30px;
			display: inline-block;
			line-height: 30px;
			margin-right: 5px;
			font-size: 16px;
		}
		.step .show span{
			border-color: #fff;
			background: #6BAEF3 ;
			color: #fff;
		}
		.step-img-box{
			list-style: none;
			padding: 0;
			margin: 0;
		}
		.step-img-box li{
			display: none;
		}
		.step-img-box li img{
			width: 800px;
		}
		.step-img-box li p{
			margin-bottom: 20px;
			line-height: 26px;
			
		}
		.step-img-box li p span{
			display: inline-block;
			height: 26px;
			width: 26px;
			color: #fff;
			font-size: 18px;
			background: #488BFF;
			margin-right: 10px;
		}
		.step-img-box li p a{
			color: #488BFF;
			text-decoration: none;
		}
		.step-img-box li.show{
			display: block;
		}
		.hf-withdraw .fixed-tips{
			position: fixed;
			bottom:0;
			width: 100%;
			background: #488BFF;
			color: #fff;
			text-align: center;
			margin: 0;
			z-index: 999;
			height: 64px;
			line-height: 63px;
		}
		.hf-withdraw .fixed-tips a{
			height: 40px;
			width: 126px;
			box-shadow: 0 0 4px 0 #D3E3FF;
			border-radius: 8px;
			background: #fff;
			display: inline-block;
			line-height: 40px;
			color: #488BFF;
			text-decoration: none;
		}
	</style>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content" style="padding: 0;">
	    		<!-- start 内容区域 -->
	    		<div class="hf-withdraw">
	    			<div class="banner"></div>
	    		<div class="step-box1">
	    			<p class="title">提现操作流程</p>
	    			<p class="subheading">点击相应按钮，具体操作流程图如下：</p>
	    			<div class="step">
	    				<a class="s1 show" style="left: 42px;"><span>1</span>点击账户查询</a>
	    				<a class="s2"><span>2</span>输入信息</a>
	    				<a class="s3" style="right: 42px;"><span>3</span>登录后取现</a>
	    			</div>
	    			<ul class="step-img-box">
	    				<li class="show">
	    					<p><span>1</span>登录汇付天下官网<a href="http://www.chinapnr.com">http://www.chinapnr.com</a>，选择右侧P2P账户查询。</p>
	    					<img src="/dist/images/hfWithdraw/step-1-1@2x.png"/>
	    				</li>
	    				<li>
	    					<p><span>2</span>输入账户名及对应密码（注：输入账户号时应为”zsc_ ” 开头账户号）</p>
	    					<img src="/dist/images/hfWithdraw/step-1-2@2x.png"/>
	    				</li>
	    				<li>
	    					<p><span>3</span>登录后即可查看账户余额，然后进行取现。</p>
	    					<img src="/dist/images/hfWithdraw/step-1-3@2x.png"/>
	    				</li>
	    			</ul>
	    		</div>
	    		
	    		
	    		<div class="step-box2">
	    			<p class="title">忘记账户名？</p>
	    			<p class="subheading">点击相应按钮进行找回，具体操作流程图如下：</p>
	    			<div class="step">
	    				<a class="s1 show" style="left: 63px;"><span>1</span>忘记账户名</a>
	    				<a class="s2" style="left: 21px;"><span>2</span>输入信息</a>
	    				<a class="s2" style="right: 21px;"><span>3</span>回答验证问题</a>
	    				<a class="s3" style="right: 63px;"><span>4</span>找回账号成功</a>
	    			</div>
	    			<ul class="step-img-box">
	    				<li class="show">
	    					<img src="/dist/images/hfWithdraw/step-2-1@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-2-2@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-2-3@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-2-4@2x.png"/>
	    				</li>
	    			</ul>
	    		</div>
	    		
	    		<div class="step-box3">
	    			<p class="title">忘记登录密码？</p>
	    			<p class="subheading">点击相应按钮进行找回，具体操作流程图如下：</p>
	    			<div class="step">
	    				<a class="s1 show" style="left: 84px"><span>1</span>忘记登录密码</a>
	    				<a class="s2" style="left:42px"><span>2</span>输入信息</a>
	    				<a class="s2" style=""><span>3</span>获取短信码</a>
	    				<a class="s2" style="right: 42px;"><span>4</span>设置新密码</a>
	    				<a class="s3" style="right: 84px;"><span>5</span>设置成功</a>
	    			</div>
	    			<ul class="step-img-box">
	    				<li class="show">
	    					<img src="/dist/images/hfWithdraw/step-3-1@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-3-2@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-3-3@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-3-4@2x.png"/>
	    				</li>
	    				<li>
	    					<img src="/dist/images/hfWithdraw/step-3-5@2x.png"/>
	    				</li>
	    			</ul>
	    		</div>
	    		
	    		<div class="step-box4">
	    			<p class="title">提现操作流程</p>
	    			<p class="subheading">点击相应按钮进行找回，具体操作流程图如下：</p>
	    			<div class="step">
	    				<a class="s1 show" style="left: 42px;"><span>1</span>弹出提示页面</a>
	    				<a class="s2"><span>2</span>扫描二维码</a>
	    				<a class="s3" style="right: 42px;"><span>3</span>下载汇付APP</a>
	    			</div>
	    			<ul class="step-img-box">
	    				<li class="show">
	    					<p><span>1</span>如果登录时，弹出如下图提示页面。</p>
	    					<img src="/dist/images/hfWithdraw/step-4-1@2x.png"/>
	    				</li>
	    				<li>
	    					<p><span>2</span>请用手机扫描登录界面下方二维码</p>
	    					<img src="/dist/images/hfWithdraw/step-4-2@2x.png"/>
	    				</li>
	    				<li>
	    					<p><span>3</span>下载汇付中心APP解冻当前账号</p>
	    					<img src="/dist/images/hfWithdraw/step-4-3@2x.png"/>
	    				</li>
	    			</ul>
	    		</div>
	    		<p class="fixed-tips">
	    			平台已关闭汇付天下提现功能，请前往“汇付天下官网“进行提现。
	    			<a href="http://www.chinapnr.com">前往汇付天下</a>
	    		</p>
	    		</div>
	    		
	    		<!-- end 内容区域 --> 
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
   		$('.step a').click(function(){
   			var _this=$(this);
   			var _they=_this.parent().children()
   			var _box=_this.parent().next('.step-img-box').children()
   			var i=_this.parent().children().index($(this))
   			if(!_this.hasClass('show')){
    			_they.removeClass('show');
    			_box.removeClass('show');
    			_this.addClass('show');
    			_box.eq(i).addClass('show')
    		}
   		})
   	</script>
</body>
</html>