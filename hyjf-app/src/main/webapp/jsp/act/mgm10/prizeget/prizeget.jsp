<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/css/mgm.css"/>
<title>汇盈金服</title>
</head>
<body style="background:url(${ctx}/images/mgm10/active-bg.jpg);">
    <input type="hidden" name="sign" id="sign" value="${sign }">
	<!--pop-->
	<div class="pop hide">
		<div class="pop-1"></div>
		<div class="pop-commit hide pop-div">
			<div class="pop-close"></div>
			<div class="pop-2-main">
				<input type="hidden" id="groupCodeConfirm" name="groupCodeConfirm" value="">
				<div class="pop-2-title text-left">您选择兑换&nbsp;<span class="color-yellow">20元代金券</span></div>
				<div class="pop-2-text color-yellow text-left">--</div>
				<p class="text-left pop-2-main-p">数量：<label id="forChangeCount">0 </label><span>将使用推荐星：0个</span></p>
			</div>
			<div class="active-detail-btn pop-main-btn">确定</div>
		</div>
		<div id="popForChangeSuccess" class="pop-2 hide pop-div">
			<div class="pop-close"></div>
			<img id="prizeUrlForChangeSuccess" src="${ctx}/images/active-goods.png"/>
			<div class="pop-2-main">
				<div class="pop-2-title text-left">您已成功兑换&nbsp;<span class="color-yellow">20元代金券</span></div>
				<div class="pop-2-text color-yellow text-left">--</div>
				<p class="text-left pop-2-main-p">数量：<label>0 </label> <span>使用推荐星：0个</span></p>
				<p id="successMsgForChange" class="pop-2-main-notice">优惠券已发放至您的汇盈金服账户，登录后于“优惠券”中查看。</p>
			</div>
			<div class="active-detail-btn pop-main-btn">确定</div>
		</div>
		<div class="pop-error hide pop-div">
			<div class="pop-close"></div>
			<div class="active-detail-btn pop-main-btn"><a id="inviteButtonClick" ref="">邀请好友</a></div>
		</div>
		<div id="popForDrawSuccess" class="pop-2 pop-div lottery-pop hide">
			<div class="pop-close"><a href=""></a></div>
			<img id="prizeUrlForDrawSuccess" src="images/active-goods.png"/>
			<div class="pop-2-main">
				<div class="pop-2-title text-left">您已成功抽取&nbsp;<span id="prizeNameForDraw" class="color-yellow">20元代金券</span></div>
				<div id="remarkForDraw" class="pop-2-text color-yellow text-left">--</div>
				<p class="text-left pop-2-main-p">数量：<label id="prizeCountForDraw">0 </label> <span id="recommendUsedForDraw">使用推荐星：0个</span></p>
				<p id="successMsgForDraw" class="pop-2-main-notice">优惠券已发放至您的汇盈金服账户，登录后于“优惠券”中查看。</p>
			</div>
			<div class="active-detail-btn pop-main-btn">确定</div>
		</div>
		<div id="wxtsPop" class="pop-outnumber hide pop-div">
			<div class="pop-close"></div>
			<div class="pop-update-div">
				<div id="wxtsMsg">很抱歉，可兑换数量不足</div>
			</div>
			<div class="active-detail-btn pop-main-btn pop-ok">确定</div>
		</div>
		<div class="pop-main1">
		</div>
	</div>
	<!--pop end-->
	<!--奖品图片-->
	<c:forEach items="${prizeDrawListObj }" var="prizeDraw" varStatus="status">
		<img src="${prizeDraw.prizePicUrl}" id="drawimg${status.index }" style="display:none;" />
	</c:forEach>
    <ul class="nav">
    	<li>兑换好礼</li>
    	<li>幸运抽奖</li>
    </ul>
    <div class="active-items">
	    <div class="active-0 hide">
	    	<img src="${ctx}/images/mgm10/active-get.png" class="luck-lottery"/>
		    <p class="active-tip">每月设固定数量奖品，先兑先得，兑完即止。</p>
		    <p class="active-tip">您当前拥有<span id="starsNumNow">${recommendCount }</span>颗推荐星可用</p>
		    <div class="active-goods">
		        <c:forEach items="${prizeChangeList }" var="prizeChange">
		        <c:if test="${prizeChange.prizeReminderQuantity > 0}">
		    	<div class="goods-item">
		    		<img src="${prizeChange.prizePicUrl }"/>
		    		<div>
		    			<input type="hidden" name="groupCode" value="${prizeChange.prizeGroupCode }">
		    			<div class="goods-item-top">
		    				<i>${prizeChange.prizeName}</i>
		    				<span class="goods-rest">剩余<span class="color-yellow">${prizeChange.prizeReminderQuantity}</span>个</span>
		    			</div>
		    			<p>${prizeChange.remark}</p>
		    			<div class="tb-stock">
			    			<i class="tb-reduce reduce"></i>
			    			<input type="tel" name="" class="num"  value="1"  onafterpaste="this.value=this.value.replace(/\D/g,'')"  />
			    			<i class="tb-increase increase"></i>
		    			</div>
		    			<div class="active-detail-btn goods-item-btn" style="background:#898989;">活动已结束</div>
		    			<span class="hide btn-num"></span>
		    		</div>
		    	</div>
		    	</c:if>
		    	</c:forEach>
		    	<!--不可用-->
		    	<c:forEach items="${prizeChangeList }" var="prizeChange">
		    	<c:if test="${prizeChange.prizeReminderQuantity <= 0}">
		    	<div class="goods-item goods-item-none">
		    		<img src="${prizeChange.prizePicUrl }"/>
		    		<div>
		    			<input type="hidden" name="groupCode" value="${prizeChange.prizeGroupCode }">
		    			<div class="goods-item-top">
		    				<i>${prizeChange.prizeName}</i>
		    				<span class="goods-rest">剩余<span class="color-yellow">${prizeChange.prizeReminderQuantity}</span>个</span>
		    			</div>
		    			<p>${prizeChange.remark}</p>
		    			<div class="tb-stock">
			    			<i class="tb-reduce"></i>
			    			<div type="tel" name="" class="num" ></div>
			    			<i class="tb-increase"></i>
		    			</div>
		    			<div class="active-detail-btn" style="background:#898989;">活动已结束</div>
		    			<span class="hide btn-num"></span>
		    		</div>
		    	</div>
		    	</c:if>
		    	<!--不可用 结束-->
		    	</c:forEach>
		    	
		    </div>
		    <div class="active-detail">
				<img src="${ctx}/images/mgm10/active-detail.png" alt="活动详情" />
				<p class="active-tip1"><b>活动时间:</b></p>
				<p class="active-tip1">2016年10月21日起，2016年11月30日止。</p>
				<p class="active-tip1"><b>活动规则:</b></p>
				<p class="active-tip1">凡于汇盈金服有过任意投资的用户，均可在活动期内，通过推荐好友注册投资等方式获得“推荐星”，以兑换好礼或参与幸运抽奖。</p>
				<div class="active-detail-btn"><a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/mgm10/recommend/hd-detial.jsp'}">更多详情</a></div>
			</div>
			<img src="${ctx}/images/mgm10/active-tri.png"/>
			<img src="${ctx}/images/mgm10/active-rights.png" class="active-rights"/>
	    </div>
	    <div class="active-1 hide">
	    	<img src="${ctx}/images/mgm10/luck-lottery.jpg" class="luck-lottery"/>
		    <p class="active-tip">每次抽奖消耗${needCount }个“推荐星”，抽奖次数不限。</p>
		    <p class="active-tip">您当前拥有<span>${canDrawCount }</span>次抽奖机会</p>
			<div class="banner" style="margin-top: 3%">
				<div class="turnplate" style="background-image:url(${ctx}/images/mgm10/zp-bg.png);background-size:100% 100%;">
					<canvas class="item" id="wheelcanvas" width="422px" height="422px"></canvas>
					<img class="pointer" src="${ctx}/images/mgm10/mgm10-end-active.png"/>
				</div>
			</div>
			<div class="earnUl">
				<ul class="news_li">
				   <c:forEach items="${prizeWinList }" var="prizeWin">
					<li class="active-tip">恭喜用户${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))} <b>获得${prizeWin.prizeName }</b></li>
				   </c:forEach>
				</ul>
				<ul class="swap"></ul>
			</div>
			<div class="active-detail">
				<img src="${ctx}/images/mgm10/active-detail.png" alt="活动详情" />
				<p class="active-tip1"><b>活动时间:</b></p>
				<p class="active-tip1">2016年10月21日起，2016年11月30日止。</p>
				<p class="active-tip1"><b>活动规则:</b></p>
				<p class="active-tip1">凡于汇盈金服有过任意投资的用户，均可在活动期内，通过推荐好友注册投资等方式获得“推荐星”，以兑换好礼或参与幸运抽奖。</p>
				<div class="active-detail-btn"><a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/mgm10/recommend/hd-detial.jsp'}">更多详情</a></div>
			</div>
			<img src="${ctx}/images/mgm10/active-tri.png"/>
			<img src="${ctx}/images/mgm10/active-rights.png" class="active-rights"/>
	    </div>
    </div>
	<div id="mark">
		<img src="${ctx}/images/mgm10/red5.png" class="red-img">
	</div>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/mgm10/awardRotate.js"></script>
	<script src="${ctx}/js/mgm10/jq22.js" type="text/javascript" charset="utf-8"></script>
	<script>
	
	$(".nav li").click(function(){
		var idx = $(this).index();
		setCookie("dhtab",idx);
	})

	</script>
<script type="text/javascript">
var inviteUserUrl = "${inviteUserUrl}";
var prizeDrawList = eval('(' + '${prizeDrawList}' + ')');
var turnplate={
		groupCode:[],
		restaraunts:[],				//大转盘奖品名称
		colors:[],					//大转盘奖品区块对应背景颜色
		outsideRadius:153,			//大转盘外圆的半径
		textRadius:128,				//大转盘奖品位置距离圆心的距离
		insideRadius:56,			//大转盘内圆的半径
		startAngle:0,				//开始角度
		
		bRotate:false				//false:停止;ture:旋转
};

$(document).ready(function(){
	//动态添加大转盘的奖品与奖品区域背景颜色
	turnplate.restaraunts = new Array();
	/*	turnplate.restaraunts.push("碧根果一袋");
	turnplate.restaraunts.push("年货红包");
	turnplate.restaraunts.push("水果拼盘300元月卡");
	turnplate.restaraunts.push("2元现金红包");
	turnplate.restaraunts.push("夏威夷果一袋");
	turnplate.restaraunts.push("3元现金红包");
	turnplate.restaraunts.push("松子一袋");
	turnplate.restaraunts.push("5元现金红包");
*/
	turnplate.colors = new Array();
/*	turnplate.colors.push("#9d3aed");
	turnplate.colors.push("#521187");
	turnplate.colors.push("#9d3aed");
	turnplate.colors.push("#521187");
	turnplate.colors.push("#9d3aed");	
	turnplate.colors.push("#521187");
	turnplate.colors.push("#9d3aed");
	turnplate.colors.push("#521187");
	*/
	for(var i=0; i<prizeDrawList.length; i++){
		turnplate.restaraunts.push(prizeDrawList[i].prizeName);
		turnplate.groupCode.push(prizeDrawList[i].prizeGroupCode);
		if(i%2==0){
			turnplate.colors.push("#9d3aed");
		}else{
			turnplate.colors.push("#521187");
		}
	}
	var rotateTimeOut = function (){
		$('#wheelcanvas').rotate({
			angle:0,
			animateTo:2160,
			duration:8000,
			callback:function (){
				alert('网络超时，请检查您的网络设置！');
			}
		});
	};

	//旋转转盘 item:奖品位置; txt：提示语;
	var rotateFn = function (item, txt){
		var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length*2));
		if(angles<270){
			angles = 270 - angles; 
		}else{
			angles = 360 - angles + 270;
		}
		$('#wheelcanvas').stopRotate();
		$('#wheelcanvas').rotate({
			angle:0,
			animateTo:angles+1800,
			duration:8000,
			callback:function (){
				turnplate.colors[item-1]="#f39800";
				var a = turnplate.colors;
				drawRouletteWheel()
				//$('#mark').fadeIn();
				turnplate.bRotate = !turnplate.bRotate;
				/*$('#mark').click(function(){
					$(this).hide();
				});*/
				$(".pop").fadeIn();
				$(".lottery-pop").show();
				var widthPop = $(".pop-2").width();
				$(".pop-2").height(widthPop*1.31);
			}
		});
	};
	
	var item = 1;
	var check = true;
	$('.pointer-click').click(function (){
		if(check){
			check = false;
			$.ajax({
				url : 'doPrizeDraw',
				type : "POST",
				async : true,
				data : "sign=" + $("#sign").val(),
				success : function(result) {
					if(result.status == 0){
						$("#prizeNameForDraw").text(result.prizeName);
						if(result.prizePicUrl){
							$("#prizeUrlForDrawSuccess").attr({src: result.prizePicUrl});
						}
						$("#remarkForDraw").text(result.remark);
						$("#prizeCountForDraw").text(result.prizeCount);
						$("#recommendUsedForDraw").text("使用推荐星：" + result.recommendCost + "个");
						$("#successMsgForDraw").text(result.successMsg);
						for(var i=0; i<turnplate.groupCode.length; i++){
							if(turnplate.groupCode[i] == result.groupCode){
								item = i+1;
								rotateFn(item, turnplate.restaraunts[item - 1]);
							}
						}
					}else if(result.errCode == 2){
						// 推荐星不足
						$(".pop").show();
						$(".pop-error").show();
						check = true;
					}else {
						$(".pop").show();
						$("#wxtsMsg").text(result.statusDesc);
						$("#wxtsPop").show();
						check = true;
					}
				},
				error : function(err) {
					$(".pop").show();
					$("#wxtsMsg").text("数据取得失败!");
					$("#wxtsPop").show();
					check = true;
				}
			});
		}
		if(turnplate.bRotate)return;
		turnplate.bRotate = !turnplate.bRotate;
		//获取随机数(奖品个数范围内)
		//var item = rnd(1,turnplate.restaraunts.length);
		//奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
		//rotateFn(item, turnplate.restaraunts[item-1]);
		
		/* switch (item) {
			case 1:
				rotateFn(252, turnplate.restaraunts[0]);
				break;
			case 2:
				rotateFn(216, turnplate.restaraunts[1]);
				break;
			case 3:
				rotateFn(180, turnplate.restaraunts[2]);
				break;
			case 4:
				rotateFn(144, turnplate.restaraunts[3]);
				break;
			case 5:
				rotateFn(108, turnplate.restaraunts[4]);
				break;
			case 6:
				rotateFn(72, turnplate.restaraunts[5]);
				break;
			case 7:
				rotateFn(36, turnplate.restaraunts[6]);
				break;
			case 8:
				rotateFn(360, turnplate.restaraunts[7]);
				break;
			case 9:
				rotateFn(324, turnplate.restaraunts[8]);
				break;
			case 10:
				rotateFn(288, turnplate.restaraunts[9]);
				break;
		} */
		console.log(item);
	});
});

function rnd(n, m){
	var random = Math.floor(Math.random()*(m-n+1)+n);
	return random;
	
}


//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
window.onload=function(){
	var dhActive = getCookie("dhtab") || 1;
	$(".nav").children().eq(dhActive).trigger("click");
	drawRouletteWheel();
};

function drawRouletteWheel() {    
  var canvas = document.getElementById("wheelcanvas");    
  if (canvas.getContext) {
	  //根据奖品个数计算圆周角度
	  var arc = Math.PI / (turnplate.restaraunts.length/2);
	  var ctx = canvas.getContext("2d");
	  //在给定矩形内清空一个矩形
	  ctx.clearRect(0,0,422,422);
	  //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式  
	  ctx.strokeStyle = "#FFBE04";
	  //font 属性设置或返回画布上文本内容的当前字体属性
	  ctx.font = '12px Microsoft YaHei';      
	  for(var i = 0; i < turnplate.restaraunts.length; i++) {       
		  var angle = turnplate.startAngle + i * arc;		 
		  ctx.fillStyle = turnplate.colors[i];
		  ctx.beginPath();
		  //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）    
		  ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);    
		  ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
		  ctx.stroke();  
		  ctx.fill();
		  //锁画布(为了保存之前的画布状态)
		  ctx.save();

		  //改变画布文字颜色
		  var b = i+2;
		  if(b%2){
		  	 ctx.fillStyle = "#FFFFFF";
		  	}else{
		  	 ctx.fillStyle = "#FFFFFF";
		  	};
		  
		  //----绘制奖品开始----
		 	
		  	  	  
		  var text = turnplate.restaraunts[i];
		  var line_height = 14;
		  //translate方法重新映射画布上的 (0,0) 位置
		  ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);
		  
		  //rotate方法旋转当前的绘图
		  ctx.rotate(angle + arc / 2 + Math.PI / 2);
		  
		  /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
		 /*  if(text.indexOf("盘")>0){//判断字符进行换行
			  var texts = text.split("盘");
			  for(var j = 0; j<texts.length; j++){
				  ctx.font = j == 0?'bold 20px Microsoft YaHei':'bold 18px Microsoft YaHei';
				  if(j == 0){
					  ctx.fillText(texts[j]+"盘", -ctx.measureText(texts[j]+"盘").width / 2, j * line_height);
				  }else{
					  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height*1.2); //调整行间距
				  }
			  }
		  }else if(text.indexOf("盘") == -1 && text.length>8){//奖品名称长度超过一定范围 
			  text = text.substring(0,8)+"||"+text.substring(8);
			  var texts = text.split("||");
			  for(var j = 0; j<texts.length; j++){
				  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
			  }
		  }else{
		  		
			  //在画布上绘制填色的文本。文本的默认颜色是黑色
 
			  //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
			   ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
		  }  */
		  
		  if (text.length <= 6) { //判断字符进行换行
              var texts = [text];
              ctx.font = '12px Microsoft YaHei';
              for (var j = 0; j < texts.length; j++) {
                  ctx.fillText(texts[j] , -ctx.measureText(texts[j]).width / 2, j * line_height);
              }
          } else if (text.length > 6) { //奖品名称长度超过一定范围 
              ctx.font='10px Microsoft YaHei';
              var stxt = parseInt(text.length/2);
              var nowtext = text.substring(0, stxt) + "||" + text.substring(stxt);
              var texts = nowtext.split("||");
              for (var j = 0; j < texts.length; j++) {
                  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
              }
          } else {
              //在画布上绘制填色的文本。文本的默认颜色是黑色
              //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
              ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
          }
		  
		  //添加对应图标
		  
		  if(text.indexOf(turnplate.restaraunts[0])>=0){
			   var img= document.getElementById("drawimg0");
			   var width = img.width;
			   var height = img.height;
			   var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH);  
		  };
		  if(text.indexOf(turnplate.restaraunts[1])>=0){
			  var img= document.getElementById("drawimg1");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  }; 
			  ctx.drawImage(img,-15,20,30,newH);  
		  };
		  if(text.indexOf(turnplate.restaraunts[2])>=0){
			  var img= document.getElementById("drawimg2");		
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH);  
		  };
		  if(text.indexOf(turnplate.restaraunts[3])>=0){
			  var img= document.getElementById("drawimg3");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[4])>=0){
			  var img= document.getElementById("drawimg4");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*50;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); ;  
		  };
		  if(text.indexOf(turnplate.restaraunts[5])>=0){
			  var img= document.getElementById("drawimg5");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[6])>=0){
			  var img= document.getElementById("drawimg6");			  
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  
		  if(text.indexOf(turnplate.restaraunts[7])>=0){
			  var img= document.getElementById("drawimg7");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[8])>=0){
			  var img= document.getElementById("drawimg8");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[9])>=0){
			  var img= document.getElementById("drawimg9");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[10])>=0){
			  var img= document.getElementById("drawimg10");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  if(text.indexOf(turnplate.restaraunts[11])>=0){
			  var img= document.getElementById("drawimg11");
			  var width = img.width;
			  var height = img.height;
			  var newH = height/width*30;
			  img.onload=function(){  
				  ctx.drawImage(img,-15,20,30,newH);      
			  };  
			  ctx.drawImage(img,-15,20,30,newH); 
		  };
		  
		  
		  //把当前画布返回（调整）到上一个save()状态之前 
		  ctx.restore();
		  //----绘制奖品结束----
	  }     
  } 
};



</script>
</body>
</html>