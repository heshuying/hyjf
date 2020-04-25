<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>速度开跑 激情无限 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/may-activity.css" />
		<style>
			footer{
				border-top: 2px solid #dcdcdc;
			}
		</style>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	
   <div class="may-Top">
			<p class="may-text">速度开跑
				<span>激情无限</span>
			</p>
			<p class="may-time">
				2016年5月1日至2016年5月31日
			</p>
		</div>
		<div class="may-Ban">
			<div class="may-ban-speed">
				<c:if test = "${empty activityList}">
				 <div class="may-speed-left">
					<div class="may-list">
						<p class="may-list-bg may-list-title"><span class="may-list-rank">排名</span><span>用户名</span><span>时速</span><span>奖励</span></p>
						<div class="may-list-none">
							<a class="may-touzi-show" href="#">暂无排名，马上投资</a>
						</div>
					</div>
				</div> 
				</c:if>
				<div class="may-speed-left">
					<div class="may-ranking"></div>
					<div class="may-list">
						<p class="may-list-title"><span class="may-list-rank">排名</span><span>用户名</span><span>时速</span><span>奖励</span></p>
						<div class="may-line">
							<ul>
								<c:forEach items="${activityList }" var="record" begin="0" step="1" varStatus="status">
									<li>
										<p><span class="may-list-rank">${status.index +1}</span><span>${record.userName}</span><span>${record.speed}km/h</span><span>${record.returnAmount}元</span></p>
									</li>
								</c:forEach>
							</ul>
						</div>
				    </div>
				</div>
				<!-- 未登录 -->
				<c:if test="${!islogin}">
				 <div class="may-speed-right">
				 	<p>
				 		<a class="may-log-show" href= "${ctx}/user/login/init.do">登录</a>后可见
				 	</p>
				</div>
				</c:if>
				<c:if test = "${islogin}">
					<div class="may-speed-right logined">
						<input type="hidden" name="" id="userDate" data-km="${activityF1.speed == null ? '0':activityF1.speed}">
						<canvas id="canvasBox" width="384" height="331"></canvas>
					</div>
				</c:if>
			</div>
			
		</div>
		<div class="may-Bot">
			<div class="may-active-con">
				<div class="may-active-rule">
				<span class="rule-img"></span>
				<p class="rule-top">
					活动时间：2016年5月1日至2016年5月31日
					<br>
					活动期内，用户成功执行提速任务即可为自己的汇盈战车加油提速！用户可按2016年5月31日24：00时刻其战车速度表所
					<br>
					达到的最终数值获得奖金。此外，速度超过200km/h的选手可参与极速排名，排名前20的用户可额外获得现金大奖。
                </p>
                <p>
					提速任务：
					<br>
					1.投资汇直投项目每满10000元，可提速1km/h
					<br>
					2.活动期内首次使用APP投资，可额外提速1km/h
					<br>
					3.活动期内新注册用户首笔投资金额≥5000元，可额外提速1km/h
                </p>
				</div>
			<div class="may-reward">
				<p>奖励设置：</p>
				<table>
				    <tr>
				    	<td>最终速度</td>
				    	<td>5km/h</td>
				    	<td>10km/h</td>
				    	<td>30km/h</td>
				    	<td>60km/h</td>
				    	<td>80km/h</td>
				    	<td>100km/h</td>
				    	<td>120km/h</td>
				    	<td>150km/h</td>
				    </tr>
				    <tr>
				    	<td>最终奖金</td>
				    	<td>60元</td>
				    	<td>150元</td>
				    	<td>480元</td>
				    	<td>1000元</td>
				    	<td>1400元</td>
				    	<td>1800元</td>
				    	<td>2300元</td>
				    	<td>3000元</td>
				    </tr>
				</table>
			</div>
			<div class="may-rank-table may-reward">
				<p>极速排行榜：</p>
				<table>
				    <tr>
				    	<td>排     名</td>
				    	<td>No.1</td>
				    	<td>No.2</td>
				    	<td>No.3</td>
				    	<td>No.4-10</td>
				    	<td>No.11-20</td>
				    </tr>
				    <tr>
				    	<td>现金大奖</td>
				    	<td>3000元</td>
				    	<td>2000元</td>
				    	<td>1000元</td>
				    	<td>800元</td>
				    	<td>500元</td>
				    </tr>
				</table>
			</div>
			<div class="may-bot-text may-reward">
				<p>
					奖励发放：<br>
				         现金奖励将于活动结束后5个工作日内发放至用户汇盈金服账户。

			</div>
			<div class="may-bot-text may-reward">
				<p>
					注：仅限汇直投项目参与活动。<br>
				          本活动最终解释权归汇盈金服所有。
				</p>
			</div>
		</div>	
	    </div>
	    
	    <jsp:include page="/footer.jsp"></jsp:include>
		<script src="${ctx}/js/easeljs-0.8.2.min.js" type="text/javascript"></script>
	    <script src="${ctx}/js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
		<script>
			jQuery(".may-list").slide({mainCell:".may-line ul",effect:"topLoop",autoPlay:true,vis:5,scroll:1,easing:"swing",delayTime:500,});
		</script>
		<script>
		//数据操作
		var km = $("#userDate").data("km"); //获取参数
		var type = getType(km);
		/* 根据数值获取阶段 */
		function getType(num){
			var type = 1;
			if(num == "" || isNaN(num)){
				type = 1;
			}else if(num > 0 && num<=5){
				type = 2;
			}else if(num > 5 && num<=10){
				type = 3;
			}else if(num > 10 && num<=30){
				type = 4;
			}else if(num > 30 && num<=60){
				type = 5;
			}else if(num > 60 && num<=80){
				type = 6;
			}else if(num > 80 && num<=100){
				type = 7;
			}else if(num > 100 && num<=120){
				type = 8;
			}else if(num > 120 && num<=150){
				type = 9;
			}else if(num > 150 && num<=200){
				type = 10;
			}else if(num > 200 && num<=250){
				type = 11;
			}else if(num > 250){
				type = 12;
			}
			return type;
		}
	</script>
	<script>
	//画布操作
		var canvas = document.getElementById("canvasBox");
	    var stage = new createjs.Stage(canvas);
	    var container = new createjs.Container();//绘制外部容器
	    var kmContainer = new createjs.Container();//刻度容器
	    var txtContainer = new createjs.Container();//文字容器
	    stage.addChild(container);
	    stage.addChild(kmContainer);
	    stage.addChild(txtContainer);
	    bgimage = new Image();
	    bgimage.src = "${ctx}/img/imagemay/act-bg.png"; //刻度背景，无进度条
	    //绘制背景
	    bgimage.onload = function(){
	    	var bitmap = new createjs.Bitmap(bgimage);
		    container.addChild(bitmap);
		    stage.update();
	    }

	    //加载图片
	    var kmimage = [];
	    for(var typei = 1;typei<=type;typei++){
	    	kmimage[typei] = new Image();
	    	kmimage[typei].src = "${ctx}/img/imagemay/act-"+typei+".png";
	    }
	    //最后一张背景图片加载完成执行操作
	    kmimage[type].onload = function(){
	    	var drowi = 1;
	    	var kmi = 0;
	    	var kms = 10; // 进度条加载速度
	    	//加载刻度图片
	    	var doDrowTimer = setInterval(function(){
	    		if(drowi>type){
	    			clearInterval(doDrowTimer);
	    			return false;
	    		}
	    		var bitmap = new createjs.Bitmap(kmimage[drowi]);
			    kmContainer.removeAllChildren();
			    kmContainer.addChild(bitmap);
			    stage.update();
			    drowi++;
	    	},30);
	    	//加载文字
	    	var doKmTimer = setInterval(function(){
	    		if(kmi>km){
	    			clearInterval(doKmTimer);
	    			return false;
	    		}
	    		var text = new createjs.Text(kmi, "italic 48px Impact", "#fff100");
	    		text.textAlign = "center";
	    		text.x=185;
	    		text.y=140;
			    txtContainer.removeAllChildren();
			    txtContainer.addChild(text);
			    stage.update();
			    kmi++;
			    if(kms>0.5){
			    	kms*=0.8;
			    }
	    	},kms);
	    }
	</script>
	</body>
</html>