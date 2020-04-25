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
<jsp:include page="/header.jsp"></jsp:include>
<style>
    	p,ul,li{
    		margin: 0;
    	}
    	*{
    		box-sizing: content-box;
    	}
    	body{
    		overflow-x: auto;
    	}
    	.active08-content{
    		float:left;
    		background:#6d41ac;
    		width: 100%;
    		text-align: center;
    		min-width: 1200px;
    	}
    	.active08-content .block{
    		display: block;
    		margin: 0 auto;
    	}
    	.active08-content .banner1{
    		max-width: 1920px;
    		min-width: 1100px;
    		margin: 0 auto;
    		display: block;
    	}
    	.active08-content .banner2{
    		max-width: 1920px;
    		background: url('/dist/images/active/activity2018/activity201808/banner2.png');
    		height:295px ;
    		margin: 0 auto;
    		position: relative;
    		min-width:1100px;
    		margin-top:-3px;
    	}
    	.active08-content .banner2 img{
    		display: block;
    		margin: 0 auto;
    	}
    	.active08-content .banner3{
    		max-width: 1920px;
    		min-width:1100px;
    		background: url('/dist/images/active/activity2018/activity201808/banner3.png');
    		height:132px ;
    		margin: 0 auto;
    	}
    	.active08-content .banner4{
    		max-width: 1920px;
    		min-width:1100px;
    		background: url('/dist/images/active/activity2018/activity201808/banner4.jpg');
    		background-size: 100% 100%;
    		margin: 0 auto;
    	}
    	.active08-content .banner5{
    		max-width: 1920px;
    		min-width:1100px;
    		margin: 0 auto;
    	}
    	.prize-content{
    		width: 1100px;
    		margin: 0 auto;
    		background: url('/dist/images/active/activity2018/activity201808/content2.png');
    		background-size: 100% 100% ;
    		padding-bottom: 80px;
    		padding-top:50px;
    	}
    	
    	.prize-content ul{
    		list-style: none;
    	}
    	.prize-content p{
    		padding-top:15px;
    		padding-bottom:15px;
    		height: 50px;
    		line-height: 50px;
    		font-size: 28px;
    		margin: 0;
    		color:#fff
    	}
    	.prize-content p span{
    		color: #f2e400;
    	}
    	.prize-content p em{
    		font-style: normal;
    		display: inline-block;
    		text-align: center;
    		width: 39px;
    		height: 50px;
    		background: url('/dist/images/active/activity2018/activity201808/number.png');
    		margin:0 5px;
    	}
    	.prize-content .prizes li{
			float: left;
			margin-right: 20px;
			margin-top:35px;
			position:relative;
    	}
    	.prize-content .prizes li .getPrize{
    		position: absolute;
    		top:80px;
			left: 91px;
    	}
    	.prize-content ul{
    		margin-left: 25px;
    		padding: 0;
    		overflow: hidden;
    	}
    	.prize-content .jixi{
    		text-align: left;
    		padding-left: 27px;
    		margin-top: 40px;
    	}
    	.prize-content .jixi div{
    		display: inline-block;
    		position: relative;
    	}
    	.prize-content .jixi div .getPrize{
    		position: absolute;
    		left: 60px;
    		top: -5px;
    		height: 130px;
    	}
    	.prize-content .jixi div+div{
    		margin-left: 10px;
    	}
    	.prize-content .jixi h6{
    		margin-bottom: 70px;
    		color: #fff;
    		font-size: 14px;
    		margin-top: 10px;
    		font-weight: normal;
    	}
    	.alertBox{
    		position: fixed;
    		top:0;
    		left: 0;
    		width: 100%;
    		height: 100%;
    		background: rgba(0,0,0,0.7);
    	}
    	.alertBox .alertPrompt{
    		background: url('/dist/images/active/activity2018/activity201808/alert.png');
    		left: 50%;
		    margin-left: -150px;
		    margin-top: -100px;
		    top: 50%;
		    width: 550px;
		    height: 300px;
		    position: absolute;
		    color: #FFF;
		    line-height: 60PX;
		    text-align: center;
		    padding-top: 80px;
		    box-sizing: border-box;
		    font-size: 28PX;
		    
    	}
    	.alertBox .alertPrompt .close{
    		position: absolute;
    		background: url('/dist/images/active/activity2018/activity201808/close.png');
    		height: 40px;
    		width: 40px;
    		right: -20px;
    		top: -20px;
    	}
    </style>
    <!-- END if -->
	</head>
	<body>

		<div class="active08-content">
			<div class="banner1"><img src="/dist/images/active/activity2018/activity201808/banner1.jpg" alt="" /></div>
			<div class="banner2">
				<img style="position: absolute;left:50%;top:-100px;margin-left:-239px" src="/dist/images/active/activity2018/activity201808/tit1.png" alt="" />
				<img style="padding-top: 30px;" src="/dist/images/active/activity2018/activity201808/content1.png" alt="" />
			</div>
			<div class="banner3">
				<img style="" src="/dist/images/active/activity2018/activity201808/tit2.png" alt="" />
			</div>
			<div class="banner4">
				<div class="prize-content">
					<input type="hidden" value="${money}" id="method1">
					<p id="investsum"></p>
					<ul class="prizes">
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize1.png" alt="" />
							<c:if test="${awartType == 1}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize2.png" alt="" />
							<c:if test="${awartType == 2}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize3.png" alt="" />
							<c:if test="${awartType == 3}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize4.png" alt="" />
							<c:if test="${awartType == 4}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize5.png" alt="" />
							<c:if test="${awartType == 5}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
						<li>
							<img src="/dist/images/active/activity2018/activity201808/prize6.png" alt="" />
							<c:if test="${awartType == 6}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</li>
					</ul>
					<div class="jixi">
						<div>
							<img src="/dist/images/active/activity2018/activity201808/jiaxi1.png" alt="" />
							<c:if test="${awartType == 7}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</div>
						<div>
							<img src="/dist/images/active/activity2018/activity201808/jiaxi2.png" alt="" />
							<c:if test="${awartType == 8}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</div>
						<div>
							<img src="/dist/images/active/activity2018/activity201808/jiaxi3.png" alt="" />
							<c:if test="${awartType == 9}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</div>
						<div>
							<img src="/dist/images/active/activity2018/activity201808/jiaxi4.png" alt="" />
							<c:if test="${awartType == 10}">
								<img class="getPrize" src="/dist/images/active/activity2018/activity201808/get.png" alt="" />
							</c:if>
						</div>
						<h6>注：图片仅供参考，奖品以实物为准</h6>
					</div>

				<input type="hidden" value="${nowTime}" id="method2">
				<input type="hidden" value="${startTime}" id="method3">
				<input type="hidden" value="${endTime}" id="method4">
				<input type="hidden" value="${isLogin}" id="method5">
					<div id="investimg">
						<img id="startimg"/>
					</div>



							<!-- 活动结束按钮 -->
							<!--<img src="/dist/images/active/activity2018/activity201808/btn2.png" alt="" />-->

				</div>
				<img class="block" style="margin-top: 50px;" src="/dist/images/active/activity2018/activity201808/tit3.png" alt="" />
				<img class="block" style="margin-top: 30px;" src="/dist/images/active/activity2018/activity201808/content3.png" alt="" />
			</div>
			<div class="banner5">
				<img alt="" src="/dist/images/active/activity2018/activity201808/banner5.png">
			</div>
		</div>
		<c:choose>
			<c:when test="${nowTime lt startTime}">
				<div class="alertBox">
					<div class="alertPrompt">
						活动将于8月10号开始，<br />
						请耐心等待~
						<span class="close"></span>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="alertBox" style="display: none;">
					<div class="alertPrompt">
						活动将于8月10号开始，<br />
						请耐心等待~
						<span class="close"></span>
					</div>
				</div>
			</c:otherwise>
		</c:choose>


	</body>
	<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
	<script type="text/javascript">
		$('.alertPrompt .close').click(function(){
			$('.alertBox').hide()
		})
	</script>
<script type="text/javascript">
    var money = $("#method1").val();
    if (money != null && money != "") {
        arr = money.split('');
        var aa = "<span>我的累计投资:</span><em>¥</em>";
        for(var i=0;i<arr.length;i++) {
            if (arr[i].toString()==(".")|| arr[i].toString()==(","))  {
                aa += arr[i].toString();
            } else {
                aa += "<em>" + arr[i].toString() + "</em>";
            }
        }
        $("#investsum").html(aa);
	}

    $(function( ){
        var startTime = $("#method3").val();
        var nowTime = $("#method2").val();
        var endTime = $("#method4").val();
        var isLogin = $("#method5").val();
        if (nowTime < startTime) {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn.png"});
            $("#startimg").attr({onclick:"return turnto()"})
		}
        else if (startTime <= nowTime && nowTime < endTime) {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn.png"});
            $("#startimg").attr({onclick:"return turnto()"})
        } else if (nowTime >= endTime && isLogin == 0) {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn2.png"});
		}
        else if (nowTime >= endTime && isLogin == 1) {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn.png"});
            $("#startimg").attr({onclick:"return turnto()"})
        }
	});

    function change() {
        var nowTime = $("#method2").val();
        var endTime = $("#method4").val();
        var isLogin = $("#method5").val();
        alert(isLogin);
        if (nowTime >= endTime && isLogin == 0) {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn2.png"})
		} else {
            $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn.png"})
		}

    }
    
    function turnto() {
        window.location.href = webPath + "/activity/2018qixi/invest_list.do"
    }

    function turnto2() {
        $("#startimg").attr({src:"/dist/images/active/activity2018/activity201808/btn2.png"})
    }


</script>
</body>
</html>
