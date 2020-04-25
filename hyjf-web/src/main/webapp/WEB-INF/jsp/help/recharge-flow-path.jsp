<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
   	#aptitudeShow{
   		width: 580px;
   		margin: 0 auto;
   		text-align: center;
   		padding-bottom: 40px;
   	}
   	#aptitudeShow p{
   		font-size: 18px;
   		margin-top: 45px;
   		margin-bottom: 20px;
   	}
   	.swiper-slideP{
   		position: relative;
   	}
   	.note{
   		width: 100%;
   		position: absolute;
   		left: 0;
   		top: 74px;
   		color: #fb4b35;
   	}
   	.swiper-button-next{background-image:url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M27%2C22L27%2C22L5%2C44l-2.1-2.1L22.8%2C22L2.9%2C2.1L5%2C0L27%2C22L27%2C22z'%20fill%3D'%23ff5b29'%2F%3E%3C%2Fsvg%3E");right: 0;}
   	.swiper-button-prev{background-image:url("data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg'%20viewBox%3D'0%200%2027%2044'%3E%3Cpath%20d%3D'M0%2C22L22%2C0l2.1%2C2.1L4.2%2C22l19.9%2C19.9L22%2C44L0%2C22L0%2C22L0%2C22z'%20fill%3D'%23ff5b29'%2F%3E%3C%2Fsvg%3E");left: 0;}
   </style>
</head>
<body class="help-center">
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
        	<div class="band-title">
					线下充值流程
				</div>
            <!-- start 内容区域 --> 
             <div class="acc-set">
            
            	<div>
            		<div class="swiper-container" id="aptitudeShow">
            			<div class="swiper-wrapper">
	            			<div class="swiper-slide">
	            				<p>第一步，登录个人网银账号（必须是绑定卡）</p>
	            				<img src="${cdn}/dist/images/aboutUs/recharge-flow-path3@2x.png" width="450" alt="" />
	            			</div>
	            			<div class="swiper-slide">
	            				<p>第二步，填写转账信息（收款方信息见下图）</p>
	            				<span class="note">
	            					户名括号为中文括号，开户行江西银行或南昌银行
	            				</span>
	            				<img src="${cdn}/dist/images/aboutUs/recharge-flow-path2@2x.png" width="450" alt="" />
	            			</div>
	            			<div class="swiper-slide">
	            				<p>第三步，输入密码完成</p>
	            				<img src="${cdn}/dist/images/aboutUs/recharge-flow-path1@2x.png" width="450" alt="" />
	            			</div>
	            		</div>
	            		<div class="swiper-button-prev" id="prev"></div>
						<div class="swiper-button-next" id="next"></div>
        			</div>
            	</div>
            </div>       
                      
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/swiper3.jquery.min.js"></script>
    <script>
    	var mySwiper = new Swiper(' .swiper-container', {						
			slidesPerView : 'auto',
			slidesPerGroup:1,
			autoplay : false,
			nextButton: '#next',
    		prevButton: '#prev',
    		loop:false,
		})
    </script>
</body>
</html>
