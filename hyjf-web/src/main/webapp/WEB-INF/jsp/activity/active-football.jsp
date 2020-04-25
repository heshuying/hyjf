<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
   <style type="text/css">
		.activefoot-content{
			background: #282289;
		}
		.activefoot-tab{
			display: flex;
			width: 1100px;
			margin: 0 auto;
			text-align: center;
		}
		.activefoot-tab a{
			cursor: pointer;
			flex: 1;
			height: 125px;
			line-height: 125px;
			font-size: 36px;
			border-radius:18px 18px 0 0;
			position: relative;
			top:-17px;
			color: #6c72b5;
			background: #1d2574;
		}
		.activefoot-tab a.select{
			color: #fff408;
			background: #282289;
		}
		.activefoot-content img{
			display: block;
			margin: 0 auto;
			
		}
		.activefoot-content .banner-bg{
			height: 500px;
			width: 100%;
			text-align: center;
			float: left;
			overflow: hidden;
			background: url(/dist/images/active/activity2018/activityFootball/banner-bg.jpg) 0 0 repeat-x;
		}
		.activefoot-content p{
			margin-top: 50px;
			padding-top: 50px;
			text-align: center;
			font-size:18px;
			color: #6c72b5;
			padding-bottom:40px;
			background:#1d2574;
			line-height: 28px;
			margin-bottom: 0;
		}
	</style>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="activefoot-content">
		<div class="banner-bg">
				<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-1.jpg" alt="" />
		</div>
		<div class="activefoot-tab">
			<a class="select">活动一：竞猜输赢</a>
			<a>活动二：竞猜冠军</a>
		</div>
		<div class="box">
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-2.jpg" alt="" />
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-3.jpg" alt="" />
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-4.jpg" alt="" />
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-5.jpg" alt="" />
		</div>
		<div class="box" style="display: none;">
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-6.jpg" alt="" />
			<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-7.jpg" alt="" />
		</div>
		<img src="${ctx }/dist/images/active/activity2018/activityFootball/banner-8.jpg" alt="" />
		<p>
           	集团内部员工不得参与 <br>汇盈金服保留在法律规定范围内对上述规则进行解释的权利
        </p>
	</div>
	<script src="${ctx }/dist/js/lib/jquery.min.js"></script>
	<script type="text/javascript">
		$('.activefoot-tab a').each(function(index,el){
			$(el).click(function(){
				$('.activefoot-tab a').removeClass('select')
				$(this).addClass('select')
				$('.box').hide()
				$('.box').eq(index).show()
			})
		})
	</script>
</body>
</html>