<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/NewYear.css"/>
<title>汇盈金服新年活动</title>
</head>
	<body>
		<div class="ny2017-pop hide">
			<div class="ny2017-pop-main">
				<h5>确认答题</h5>
				<div class="ny2017-pop-main-content">
					<img src="${ctx}/images/newyear/newyear2017-pop-top.png"/>
					<div class="ny2017-pop-main-content-text">
						今日灯谜即将开启，您准备好了吗？点击确认后，2分钟倒计时即刻开始，时间耗尽前回答正确即可获得奖励，中途退出将失去今日答题机会。
					</div>
					<img src="${ctx}/images/newyear/newyear2017-pop-down.png"/>
				</div>
				<div class="ny2017-pop-ok ny2017-pop-ok-click-1">确认</div>
				<div class="ny2017-pop-ok ny2017-pop-answer-ok hide">确认</div>
				<div class="ny2017-pop-cancle ny2017-pop-cancle-click-2">取消</div>
			</div>
			<div class="ny2017-pop-bg"></div>
		</div>
		<div class="newYear">
			<div class="newYear-top">
				<img src="${ctx}/images/newyear/newYear-ban-1.jpg" alt="" />
				<img src="${ctx}/images/newyear/newYear-ban-2.jpg" alt="" />
				<img src="${ctx}/images/newyear/newYear-ban-3.jpg" alt="" />
			</div>
			<div class="newYear-tab">
				<ul>
					<li <c:if test="${tabFlag==1}">class="tabActive"</c:if>>
						财神来敲我家门
						<span></span>
					</li>
					<li <c:if test="${tabFlag==2}">class='tabActive'</c:if>>
						红红火火闹元宵
					    <span></span>
					</li>
				</ul>
			</div>
		    <div class="newYear-cont">
		    	<div class="newYear-active-1 <c:if test="${tabFlag==1}">contActive</c:if>">
		    		<img src="${ctx}/images/newyear/active-one-ban1.jpg" alt="" />
		    		<div class="active-lottery">
		    			<ul class="active-lottery-ul">
		    				<li id="cardJin" cardType="1">
		    					<p>金</p>
		    				    <a><img src="" alt="" /></a>
		    					<span style="display:none">0</span>
		    				</li>
		    				<li id="cardJi" cardType="2">
		    					<p>鸡</p>
		    				    <a><img src="" alt="" /></a>
		    					<span style="display:none">0</span>
		    				</li>
		    				<li id="cardNa" cardType="3">
		    					<p>纳</p>
		    				    <a><img src="" alt="" /></a>
		    					<span style="display:none">0</span>
		    				</li>
		    				<li id="cardFu" cardType="4" class="active1-last">
		    					<p>福</p>
		    				    <a><img src="" alt="" /></a>
		    					<span style="display:none">0</span>
		    				</li>
		    				<li class="clear"></li>
		    			</ul>
		    		</div>
		    	    <div class="active-prize">
		    	    	<div class="active-ending">
		    	    		<p>活动已经结束！</p>
		    	    	</div>
		    	    	<div class="active-baozhu-list" style="width: 0;height: 0;overflow: hidden;">
		    	    		<img src="${ctx}/images/newyear/baozhu1.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu2.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu3.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu4.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu5.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu6.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu7.png" alt="" />
		    	    		<img src="${ctx}/images/newyear/baozhu8.png" alt="" />
		    	    	</div>
		    	    	<div class="baozhu"></div>
	    	    		<a id="fireBaoZhu" class="baozhu-btn btn-style">点爆竹，赢大奖</a>
		    	    </div>
		    	    <div class="active-bot">
		    	    	<div id="inviteBtn">
		    	    		<a class="invite-btn btn-style" href="hyjf://activityToShare/?{'title':'汇盈金服高收益专业金融服务平台','content':'灵活投资，稳健安全，尽享投资乐趣，快来体验专属于你的指尖财富','image':'https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png','url':'https://wx.hyjf.com/index.php?s=/Weixin/Activites/traffic/page_id/2119/id/${userId==null?'':userId}.html'}">邀请好友注册</a>
		    	    	</div>
		    	    	<div class="active-rules">
		    	    		<p>活动规则：</p>
		    	    		<a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/cnNewYear/rule-1.jsp'}">财神卡获得方式&gt;&gt;</a>
		    	    		<a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/cnNewYear/rule-1.jsp'}">奖励如何发放&gt;&gt;</a>
		    	    		<p class="active-one-zhushi">
		    	    			注：<br />
		    	    			1.本活动仅限投资汇直投、新手汇、汇添金项目金额参与。<br />
		    	    			2.通过好友赠送方式获得的财神卡，其种类由好友指定；通过其他方式获得的财神卡，由系统随机发放。<br />
		    	    			3.未于活动期内成功使用财神卡抽奖的用户，视为自动放弃获奖机会，奖励不予发放。<br />
		    	    			4.本活动所发优惠券奖励均自用户获得之日起15日内有效，过期作废。
		    	    		</p>
		    	    	</div>
		    	    </div>
		    	</div>
		    	<div class="newYear-active-2 <c:if test="${tabFlag==2}">contActive</c:if>">
		    		<div class="ny2017-1">
		    			<h5>2017年2月5日至2017年2月11日</h5>
		    			<p>活动期内，凡汇盈金服用户每日登陆皆可获得一次猜灯谜机会，2分钟内答对谜题即可获得代金券奖励。</p>
		    		</div>
		    		<input type="hidden" id="questionId" value="${questionId}">
		    		<input type="hidden" id="sign" value="${sign}">
		    		<div class="ny2017-2">
		    			<div class="ny2017-time hide"><div class="ny2017-time-1"></div></div>
		    		</div>
		    		<c:if test="${userAnswerFlag==1}">
			    		<div class="ny2017-3 hide">
			    			<c:if test="${empty questionImageName}">
			    				<img src=""/>
			    			</c:if>
			    			
			    			<c:if test="${not empty questionImageName}">
			    				<img src="${ctx}/images/newyear/${questionImageName}.png"/>
			    			</c:if>
			    			
			    			<!--小贴士-->
			    			<div class="hide"></div>
			    		</div> 
		    		</c:if>
		    		<c:if test="${userAnswerFlag==0}">
			    		<div class="ny2017-3">
			    			<img src="${ctx}/images/newyear/${questionImageName}.png"/>
			    			<!--小贴士-->
			    			<div <c:if test='${showAnswerFlag==0}'>class="hide"</c:if>>小贴士：${questionHint}</div>
			    		</div> 
		    		</c:if>
		    		<div class="ny2017-4">
		    			<div>
		    			<c:if test="${loginFlag==0}">
		    				<a href="${loginUrl}" class="ny2017-input-before ny2017-input hide nline-block">我要猜灯谜</a>
		    			</c:if>
		    			<c:if test="${loginFlag==1&&userAnswerFlag==1}">
		    				<c:if test='${showAnswerFlag==1}'>
		    					<input type="text" name=""   readonly="readonly" class="ny2017-input ny2017-input-before ny2017-input-click" placeholder="" value="我要猜灯谜"/>
		    				</c:if>
		    			</c:if>
		    			<div class="ny2017-input-ok-button ny2017-input-ok-button-click hide">确认</div>
		    			<c:if test="${loginFlag==1&&userAnswerFlag==0}">
			    			<!--谜底-->
			    			<input type="text" name=""   readonly="readonly" class="ny2017-input-outcome <c:if test='${showAnswerFlag==0}'>hide</c:if>" placeholder="" value="谜底:${questionAnswer}"/>
		    			</c:if>
		    			</div>
		    		</div>
		    		<c:if test="${canReceiveFlag==1}">
			    		<!--获得的奖励-->
			    		<div class="ny2017-6 ny2017-6-total">
			    			<c:if test="${lanternFestivalFlag==1}">
	                    			<p>您的优惠券已增值为：</p>
	                    		</c:if>
	                    		<c:if test="${lanternFestivalFlag==2}">
	                    			<p>恭喜您已获得：</p>
	                    		</c:if>
			    			<p><span>${userPresentCumulativeCoupon}</span>
			    			<c:if test="${userPresentCumulativeCouponCount==1}">
			    				一张
			    			</c:if>
			    			<c:if test="${userPresentCumulativeCouponCount==2}">
			    				两张
			    			</c:if>
			    			</p>
			    		</div>
		    		</c:if>
		    		<img src="${ctx}/images/newyear/newyear2017-2-5.png" class="ny2017-5"/>
		    		<div class="ny2017-6">
		    			<div class="ny2017-6-main">
		    				<c:forEach items="${userLanternIllumineList}" var="record" begin="0" step="1" varStatus="status">
								<div class="ny2017-6-item">
			    					<p>${record.answerTime }日</p>
			    					<c:if test="${record.userAnswerResult==0}">
			    						<img src="${ctx}/images/newyear/newyear2017-prize-get.png"/>
			    					</c:if>
			    					<c:if test="${record.userAnswerResult==1}">
			    						<img src="${ctx}/images/newyear/newyear2017-prize-not.png"/>
			    					</c:if>
			    				</div>
                            </c:forEach>
		    			</div>
		    			<div class="ny2017-rules">
		    				<p class="ny2017-rules-title">活动规则:</p>
		    				<p class="ny2017-rules-p"><a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/cnNewYear/rule-2.jsp'}">奖励设置&gt;&gt;</a></p>
		    				<p class="ny2017-rules-p"><a href="hyjf://jumpH5/?{'url':'${host}/jsp/act/cnNewYear/rule-2.jsp'}">奖励发放&gt;&gt;</a></p>
		    			</div>
		    			<div class="ny2017-notes">
		    				<p>注:</p>
			    			<p>本活动所发代金券均自用户获得之日起15日内有效，过期作废。</p>
		    			</div>
		    		</div>
		    		<div class="ny2017-7">
		    			<p>本活动与Apple Inc.无关</p>
		    			<p>本活动最终解释权归汇盈金服所有</p>
		    		</div>
		    	</div>
		    </div>
		</div>
		<!--点燃爆竹后获得奖品弹层-->
	    <div class="newYear-tips-1 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="tips-cont-prize">
	    			<p class="prize-name">
	    			</p>
	    			<!--获得代金券-->
	    			<!--<p class="prize-name">
	    				恭喜您点燃鞭炮获得<br />
	    				<span>"10元代金券"</span>
	    			</p>-->
	    			<div class="tips-line"></div>
	    			<p class="prize-introduct">
	    				<!--代金券奖励将于用户抽奖后由系统自动发放至用户汇盈金服账户，用户登录后于“优惠券”中查看。-->
	    			</p>
	    			<a href="javascript:;" class="knew-btn close-btn">我知道了</a>
	    		</div>
	    	</div>
	    </div>
	    <!--点击爆竹燃放提示-->
	    <div class="newYear-tips-2 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="tips-cont-tishi tips-cont-prize">
	    			<p class="tishi-name">
	    				每集齐一套"金""鸡""纳""福"财神卡即可获得一次点爆竹赢大奖机会，100%中奖。
	    			</p>
	    			<a href="javascript:;" class="knew-btn close-btn">我知道了</a>
	    		</div>
	    	</div>
	    </div>
	    <!--燃放爆竹失败提示-->
	    <div class="newYear-tips-3 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="tips-cont-sorry tips-cont-prize">
	    			<p class="sorry-name">
	    				很抱歉，由于<span>xxxx</span>原因，<br>
	    				您的爆竹燃放失败！
	    			</p>
	    			<a href="javascript:;" class="knew-btn close-btn">我知道了</a>
	    		</div>
	    	</div>
	    </div>
	    <!--送好友卡片输入好友手机号弹层-->
	    <div class="newYear-tips-4 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="tips-invite">
	    			<p class="invite-type">已选择“<span class="txt-change"></span>”字财神卡</p>
	    			<input type="hidden" id="qq" value="李***" />
	    			<input type="text" class="invite-frid" id="fridNum" placeholder="输入好友手机号" maxlength="11"/>
	    			<div class="invite-message">请输入正确的手机号！</div>
	    			<div class="invite-frid-tip"></div>
	    			<div class="send-condition-btn">
	    				<a href="javascript:;" class="send-sure">确认</a>
	    				<a href="javascript:;" class="send-quxiao close-btn">取消</a>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	   <!--赠送卡片成功弹层-->
	    <div class="newYear-tips-5 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="tips-invite">
	    			<p class="invite-type">财神卡赠送成功！</p>
	    			<div class="invite-detial">
	    				<p>
	    					赠送卡片：
	    					<span class="send-ticket-name"></span>
	    				</p>
	    				<p>
	    					赠送手机号：
	    					<span class="send-frid-num"></span>
	    				</p>
	    				<p>
	    					赠送姓名：
	    					<span class="send-frid-name"></span>
	    				</p>
	    			</div>
	    			<div class="send-condition-btn">
	    				<a href="javascript:;" class="send-again close-btn">确认</a>
	    			</div>
	    			
	    		</div>
	    	</div>
	    </div>
	    <!--系统异常时，卡片未送出-->
	    <div class="newYear-tips-6 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<div class="miss-invite tips-invite">
	    			<p class="invite-type">
	    				由于<span>xxxx</span>原因<br />
	    				导致财神卡送卡失败<br />
	    				请稍后再试.....
	    			</p>
	    			<div class="send-condition-btn">
	    				<a href="javascript:;" class="send-again close-btn">确认</a>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <!--好友未注册时，邀请遮罩-->
	    <div class="newYear-tips-7 tips-close">
	    	<div class="tips-bg"></div>
	    	<div class="tips-content">
	    		<img src="${ctx}/images/newyear/wxshare.png" alt="" />
	    	</div>
	    </div>
	</body>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/newyear/newYear.js"></script>
<script type ="text/javascript">
	document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
	$(window).on( 'resize', function () {
	document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
	});
</script>
 <!-------------------------------------活动一的js--------------------------------------->
<script>
	var loginFlag = "${loginFlag}";
	var actStatus = "${actAStatus}";
	
	if(loginFlag == 0){
		$("#fireBaoZhu").attr('href','hyjf://jumpLogin/?');
		
		$('.active-lottery-ul li').each(function(i){
			$(this).children("span").css("display", "none");
			$(this).children('a').children('img').attr('src','${ctx}/images/newyear/active-'+i+'-1.png');
			$(this).children('a').attr('href','hyjf://jumpLogin/?');
		});
		
		$("#inviteBtn").hide();
	}
	
	if(actStatus ==2){
		$('.active-ending').show();
	}
	
	function getCardData(){
		if(loginFlag == 0){
			return;
		}
		
		$.ajax({
			type : "POST",
			async: false,
			url : "/hyjf-app/activity/newyearactivity/getCardData.do",
			dataType : 'json',
			data : {
				"sign":$("#sign").val()
			},
			success : function(data) {
				if (data.status == 0) {
					// 校验成功
					setCardCount("all", data);
				} else {
					// 校验失败
					alert(data.statusDesc);
				}
			},
			error : function() {
			}
		});
	}

	if(actStatus ==1){
		getCardData();
	}
	
	function setCardCount(id, data) {
	    if (id == "all") {
	        //全部卡片减少
	        _setSingleCard($("#cardJin"), data.countJin);
	        _setSingleCard($("#cardJi"), data.countJi);
	        _setSingleCard($("#cardNa"), data.countNa);
	        _setSingleCard($("#cardFu"), data.countFu);
	    } else {
	        //单个卡片减少
	        _setSingleCard($(id));
	    }

	    function _setSingleCard(obj, data) {
	        var countbox = obj.find("span");
	        countbox.text(data);
	        if (data <= 0) {
	        	countbox.hide();
	        }else{
	        	countbox.show();
	        }
	    }
	    
	    cardStatusInit();
	}

	function checkUser(str){
		var re = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/;
		return re.test(str);
	}
	
	 /*-------------------页面加载时默认显示的活动tab-------------------------*/
	/* if(){ //默认显示活动一的tab
		$('#actTab-1').addClass('tabActive').siblings().removeClass('tabActive');
	}else{ //默认显示活动二的tab
		$('#actTab-2').addClass('tabActive').siblings().removeClass('tabActive');
	} */
	  /*-------------------输入手机号完成后，验证手机号是否正确，并查询好友姓名-------------------------*/
	$('#fridNum').keyup(function(){
		var fValue = $.trim($(this).val());
		var fLenght =  fValue.length;
		if(fLenght == 11){	//手机号输完11位时
			if(checkUser(fValue)){ //验证手机号格式正确
				
			 //调用函数，进行手机号交互
             $.ajax({
         		type : "POST",
         		async: false,
         		url : "/hyjf-app/activity/newyearactivity/doPhoneNumCheck.do",
         		dataType : 'json',
         		data : {
         			"sign":$("#sign").val(),
         			"phoneNum": fValue
         		},
         		success : function(data) {
         			if (data.status == 0 ) {
         				// 校验成功
	         			$('.invite-message').hide();
						$('.invite-frid-tip').show();
						$('.invite-frid-tip').empty();
						
         				if(data.isValidPhoneNum == 0){
         					if(data.userName && data.userName.length >= 1){
	        					data.userName = data.userName.substr(0,1) + "**";
	        				} 
	        				var userName = data.userName ? data.userName: "好友未开户";
         					$('.invite-frid-tip').append('用户姓名：<span>'+ userName +'</span>');
         				}else if(data.isValidPhoneNum == 1){
         					$('.invite-frid-tip').append('好友尚未注册，快邀请TA注册开户吧！');
         				}
         				if(data.isValidPhoneNum ==2){
         					$('.invite-frid-tip').append('好友未开户');
         				}
         			} else {
         				// 校验失败
         				$('.newYear-tips-6').find('.invite-type').empty();
                    	$('.newYear-tips-6').find('.invite-type').append(data.statusDesc);
                       	$('.newYear-tips-6').show();
         			}
         		},
         		error : function() {
         			$('.newYear-tips-6').find('.invite-type').empty();
                	$('.newYear-tips-6').find('.invite-type').append("请检查您的网络连接是否已打开");
                   	$('.newYear-tips-6').show();
         		}
         	});
	
		}else{ //手机号格式错误时，提示示错误信息
				$('.invite-frid-tip').hide();
		    	$('.invite-message').show();
			}
		
	}else{
		$('.invite-frid-tip').empty();
	    $('.invite-message').hide();	
	}
});
	/*-------------------点击确认按钮时，校验并回传信息，显示赠送好友卡片的详细信息--------------------------*/
	$('.send-sure').click(function(){ //点击确认按钮，验证手机号
		var fValue = $.trim($('#fridNum').val());
		var fLenght =  fValue.length;
		var ticketName =  $(this).parents('.newYear-tips-4').find('.txt-change').text();
	    if(fLenght !=11){ //手机号不足11位时，提示示错误信息
	    	$('.invite-frid-tip').hide();
		    $('.invite-message').show();
	    }else{ 
	        if(checkUser(fValue)){
	        	$('.newYear-tips-4').hide();
	        	
	        	$.ajax({
	        		type : "POST",
	        		async: false,
	        		url : "/hyjf-app/activity/newyearactivity/doCardSend.do",
	        		dataType : 'json',
	        		data : {
	        			"sign":$("#sign").val(),
	        			"phoneNum": fValue,
	        			"cardIdentifier": cardType
	        		},
	        		success : function(data) {
	        			if (data.status == 0) {
	        				// 校验成功
	        				if(data.userName && data.userName.length >= 1){
	        					data.userName = data.userName.substr(0,1) + "**";
	        				} 
	        				var userName = data.userName ? data.userName: "好友未开户";
	        		        $('.newYear-tips-5').show();
	        	    		$('.newYear-tips-5').find('.send-ticket-name').text(data.cardName);
	        	    		$('.newYear-tips-5').find('.send-frid-num').text(data.phoneNum);
	        	    		$('.newYear-tips-5').find('.send-frid-name').text(userName); 
	        			} else {
	        				// 校验失败
	        				if(data.errCode == 2){
	            		        $('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append("您输入的手机号码对应的用户未注册");
		                       	$('.newYear-tips-6').show();
	        					
	        				}else if(data.errCode == 3){
	            		        $('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append("不能赠送给自己");
		                       	$('.newYear-tips-6').show();
	        					
	        				}else if(data.errCode == 4){
	            		        $('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append("您的卡片数量已不足");
		                       	$('.newYear-tips-6').show();
	        				}else {
	            		        $('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append(data.statusDesc);
		                       	$('.newYear-tips-6').show();
	        				}
	        			}
	        		},
	        		error : function() {
	        			$('.newYear-tips-6').find('.invite-type').empty();
                    	$('.newYear-tips-6').find('.invite-type').append("请检查您的网络连接是否已打开");
                       	$('.newYear-tips-6').show();
	        		}
	        	});
	        	
		    }
		 }
	});
	
//  判断金 鸡 纳 福是否有卡,并显示相应的颜色
    function cardStatusInit(){
		$('.active-lottery-ul li').each(function(){
			var cardCount = $(this).children('span').text();
			var indx = $(this).index();
			if(cardCount>0){
				$(this).children('a').children('img').attr('src','${ctx}/images/newyear/active-'+indx+'-2.png');
			}else{
				$(this).children('a').children('img').attr('src','${ctx}/images/newyear/active-'+indx+'-1.png');
			}
		});
	}
	
    cardStatusInit();

	var cardType;
	//	点击 金 鸡 纳 福 四个字显示相应的弹层
	$('.active-lottery-ul li').click(function(){
         if(loginFlag == 1){ //判断是否登录,登录状态
        	 cardType = $(this).attr("cardType");
	          var num = $(this).children('span').text();
	    	  if(num > 0){ //有卡时，点击相应的字显示相对的弹层
	    		    var txt = $(this).find('p').text();
	    		    $('.newYear-tips-4').show();
	    			$('.newYear-tips-4').find('.txt-change').empty();
	    			$('.newYear-tips-4').find('.txt-change').text(txt);
	    			$('.invite-frid-tip').empty();
	    			$('#fridNum').attr("placeholder", "输入好友手机号")
	    			$('#fridNum').val("");
	    		}else{ //无卡的时候无法点击
	    			return false;
	    		}
	       }else{
	            //未登录 ，跳转登陆页
	       }
	
	 });
	
/*---------------------点击鞭炮燃放按钮，判断是否有机会抽奖----------------------------------*/	
    if(loginFlag == 1){
		$('#fireBaoZhu').click(function(){
			var spanLen = $('.active-lottery-ul li').children('span').length;
			if(loginFlag == 1){ //登录状态
	        	$.ajax({
	        		type : "POST",
	        		async: false,
	        		url : "/hyjf-app/activity/newyearactivity/doPrizeDraw.do",
	        		dataType : 'json',
	        		data : {
	        			"sign":$("#sign").val()
	        		},
	        		success : function(data) {
	        			if (data.status == 0 ) {
	        				// 校验成功
	        				baozhu();
	        				
							setTimeout(function(){
								$('.baozhu').removeClass('boom')
								
								$('.newYear-tips-1').show();
				                $('.newYear-tips-1').find('.prize-name').empty();
						        $('.newYear-tips-1').find('.prize-introduct').empty();
						        
		        				if(data.prizeType == 1){
		        					$('.newYear-tips-1').find('.prize-name').append(
	   					        	'恭喜您点燃鞭炮获得<br />'+
	   					        	'<span>'+data.prizeName+'</span>'+
	   					    		'一部<br />'+
	   					    		'128GB版，颜色随机'
	   					            );
	   	                            $('.newYear-tips-1').find('.prize-introduct').append('“iPhone 7 Plus”将于活动结束后3个工作日内，由客服电话回访核实用户信息后，统一采购发放，如遇货源短缺，发放时间相应顺延。');
		        				}else if(data.prizeType == 0){
		        					$('.newYear-tips-1').find('.prize-name').append(
								        '恭喜您点燃鞭炮获得<br />'+
							          	'<span>'+data.prizeName+'</span>'
								    ); 
				                        
				                    $('.newYear-tips-1').find('.prize-introduct').append('代金券奖励将于用户抽奖后由系统自动发放至用户汇盈金服账户，用户登录后于“优惠券”中查看。' );
		        				}
							},2000)
	        	            
	        	            //getCardData();
	        			} else {
	        				// 校验失败
	        				if(data.errCode == 2){
	        					$('.newYear-tips-2').show();
	        					
	        				}else if(data.errCode == 10){
	        					$('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append(data.statusDesc);
		                       	$('.newYear-tips-6').show();
	        				}else if(data.errCode == 11){
	        					$('.newYear-tips-6').find('.invite-type').empty();
		                    	$('.newYear-tips-6').find('.invite-type').append(data.statusDesc);
		                       	$('.newYear-tips-6').show();
	        				}else {
	        					 //执行错误返回
								
								$('.newYear-tips-3').find('.sorry-name').empty();
								$('.newYear-tips-3').find('.sorry-name').append(
										'很抱歉，由于'+
										'<span>'+ xxxx+'</span>'+
										'原因，<br>您的爆竹燃放失败！'
								);
								$('.newYear-tips-3').show();
	        				}
	        				
	        			}
	        		},
	        		error : function() {
	        			$('.newYear-tips-3').find('.sorry-name').empty();
						$('.newYear-tips-3').find('.sorry-name').append("请检查您的网络连接是否已打开");
						$('.newYear-tips-3').show();
	        		}
	        	});
					 
			}else{
				//未登录 ，跳转登陆页
				$('.newYear-tips-3').find('.sorry-name').empty();
				$('.newYear-tips-3').find('.sorry-name').append("你还没有登录");
				$('.newYear-tips-3').show();
			}
		});
    }
    /*爆竹燃放特效 */
	function baozhu(){
		var bz= $('.baozhu');
		if(bz.hasClass('boom')){
			return false;
		}
		bz.addClass('boom');
		$('.baozhu-btn').hide();
		$('.baozhu-btn').prop('disabled','true');
	}
	/*点击取消按钮时，重新加载数据*/
	$('.send-quxiao').click(function(){
		$('.newYear-tips-4').find('#fridNum').val('');
		$('.newYear-tips-4').find('.invite-frid-tip').empty();
		$('.newYear-tips-4').find('.invite-message').hide();
	});
	/*点击中奖弹层我知道了,重新加载页面 */
	$('.newYear-tips-1 .close-btn').click(function(){
		getCardData();
		$('.baozhu-btn').show();
		$('.baozhu-btn').prop('disabled','false');
	});
	$('.newYear-tips-2 .close-btn').click(function(){
		getCardData();
		$('.baozhu-btn').show();
		$('.baozhu-btn').prop('disabled','false');
	});
	$('.newYear-tips-3 .close-btn').click(function(){
		getCardData();
		$('.baozhu-btn').show();
		$('.baozhu-btn').prop('disabled','false');
	});	
	
	$('.newYear-tips-5 .close-btn').click(function(){
		getCardData();
	});	
	$('.newYear-tips-6 .close-btn').click(function(){
		getCardData();
	});	
</script>
 <!-------------------------------------活动二的js--------------------------------------->
<script type="text/javascript">
			//tab2 js
			var popShow = function(){$(".ny2017-pop").show();}
			var checkClick = function(){
				var _this = $(this);
				$.ajax({
					type:"post",
					data:{"sign":$("#sign").val(),"questionId":$("#questionId").val()},
					datatype:"json",
					url:"/hyjf-app/activity/newyearactivity/check.do",
					async:false,
					success:function(data){
						data=JSON.parse(data);
						if(data.checkStatus==0){
							$(".ny2017-pop-main h5").text("很抱歉");
							$(".ny2017-pop-main-content-text").html(data.message1+'<br/>'+data.message2);
							$(".ny2017-pop-cancle").hide();
							$(".ny2017-pop").show();
							$(".ny2017-pop-ok").on("click",function(){
								$(".ny2017-pop").hide();
								window.location.href = window.location.href;
							})
							return
						}
							popShow();
							//点击确定开始答题
							$(".ny2017-pop-ok-click-1").on("click",function(){
								var self = $(this);
								if(self.attr("clicked") == 1){
									return false;
								}
								self.attr("clicked","1");
								returnFlag=0;
								if(data.checkStatus==1){
									$.ajax({
										type:"post",
										url:"/hyjf-app/activity/newyearactivity/insertUserAnswerRecordInit.do",
										data:{"sign":$("#sign").val(),"questionId":$("#questionId").val()},
										datatype:"json",
										async:false,
										success:function(data){
											data=JSON.parse(data);
											if(data.status==1){
												$(".ny2017-pop-main h5").text("很抱歉");
												$(".ny2017-pop-main-content-text").html("您的页面已经过期，请刷新重试");
												$(".ny2017-pop-cancle").hide();
												$(".ny2017-pop").show();
												$(".ny2017-pop-ok").addClass("ny2017-pop-ok-outdate")
												$(".ny2017-pop-ok-outdate").on("click",function(){
													window.location.href = window.location.href;
												})
												returnFlag=1;
												return
											}
											self.attr("clicked","0");
										}
									})
								}
								if(returnFlag==1){
									return
								}
								popShow=null;
								_this.removeClass(".ny2017-pop-ok-click-1");
								$(".ny2017-pop").fadeOut();
								$(".ny2017-3").slideDown(800);
								//更改input样式
								_this.removeClass("ny2017-input-before").addClass("ny2017-input-after ny2017-input-new").removeAttr("readonly").val("").prop("placeholder","请输入答案");
								$(".ny2017-input-ok-button").show();
								startTimerClock();
								$(".ny2017-time").fadeIn();
								$(".ny2017-pop-ok-click-1").hide();
								$(".ny2017-pop-answer-ok").show();
							})
					}
				})
			}
			$(".ny2017-input-click").one("click",checkClick)
			//点击取消隐藏
			$(".ny2017-pop-cancle").on("click",function(){
				$(".ny2017-pop").hide();
				window.location.href = window.location.href;
			})
			//判断输入
			$(".ny2017-input").on("keyup",function(event){
				var event = event || window.event;
				if(event.keyCode==13){
					//正确答案
					$.ajax({
					type:"post",
					data:{"sign":$("#sign").val(),"questionId":$("#questionId").val(),"userAnswer":$(this).val()},
					datatype:"json",
					async:false,
					url:"/hyjf-app/activity/newyearactivity/updateUserAnswerRecord.do",
					success:function(data){
						data=JSON.parse(data);
						if(data.isCorrect==1){
							clearTimeout(timerClock);
							$(".ny2017-pop-main h5").text("恭喜您");
							$(".ny2017-pop-main-content-text").html('<p>回答正确!</p><p>您已点亮了今日的红灯笼！</p><p>您的奖励增值为'+data.prompt+'元代金券'+data.couponCount+'张</p>');
							$(".ny2017-pop-ok").removeClass(".ny2017-pop-ok-click-1").text("我知道了");
							$(".ny2017-pop-cancle").hide();
							$(".ny2017-pop").show();
							$(".ny2017-pop-ok").on("click",function(){
								location.reload();
							})
						}else{/*错误答案*/
							$(".ny2017-pop-main h5").text("很抱歉");
							$(".ny2017-pop-main-content-text").html('<p>回答错误!</p><p>还有时间，再试试其他答案吧！</p>');
							$(".ny2017-pop-ok").removeClass(".ny2017-pop-ok-click-1").text("我知道了");
							$(".ny2017-pop-cancle").hide();
							$(".ny2017-pop").show();
							$(".ny2017-pop-answer-ok").on("click",function(){
								$(".ny2017-pop").hide()
							})
						}
					}
				})
				}
			})
			//点击确定提交
			$(".ny2017-input-ok-button-click").on("click",function(event){
					//正确答案
					$.ajax({
					type:"post",
					data:{"sign":$("#sign").val(),"questionId":$("#questionId").val(),"userAnswer":$(".ny2017-input").val()},
					datatype:"json",
					url:"/hyjf-app/activity/newyearactivity/updateUserAnswerRecord.do",
					success:function(data){
						data=JSON.parse(data);
						if(data.isCorrect==1){
							clearTimeout(timerClock);
							$(".ny2017-pop-main h5").text("恭喜您");
							$(".ny2017-pop-main-content-text").html('<p>回答正确!</p><p>您已点亮了今日的红灯笼！</p><p>您的奖励增值为'+data.prompt+'元代金券'+data.couponCount+'张</p>');
							$(".ny2017-pop-ok").removeClass(".ny2017-pop-ok-click-1").text("我知道了");
							$(".ny2017-pop-cancle").hide();
							$(".ny2017-pop").show();
							$(".ny2017-pop-ok").on("click",function(){
								window.location.href = window.location.href;
							})
						}else{/*错误答案*/
							$(".ny2017-pop-main h5").text("很抱歉");
							$(".ny2017-pop-main-content-text").html('<p>回答错误!</p><p>还有时间，再试试其他答案吧！</p>');
							$(".ny2017-pop-ok").removeClass(".ny2017-pop-ok-click-1").text("我知道了");
							$(".ny2017-pop-cancle").hide();
							$(".ny2017-pop").show();
							$(".ny2017-pop-answer-ok").on("click",function(){
								$(".ny2017-pop").hide()
							})
						}
					}
				})
			})
			 //倒计时
		  //开始计时器
		 	var  remainingTime = 119;//倒计时时间
			var timerClock = null;
		 	var m = 1;
		 	var n =2;
			var startTimerClock =function(){
				clearTimeout(timerClock);
				if(remainingTime <= 0){
					endTimerClock();
					return;
				}
				var second = remainingTime-(n-1)*60;
				if(second<10 && second>0){
					$(".ny2017-time-1").text(m+':0'+second+'');
				}else if(second<0){
					second=second+60;
					$(".ny2017-time-1").text(m+':'+second);
					n=n-1;
				}else if(second==60 || second ==0){
					second=0;
					$(".ny2017-time-1").text(m+':0'+second);
					m=m-1;
				}else{
					$(".ny2017-time-1").text(m+':'+second);
				}
				timerClock = setTimeout(function(){
					startTimerClock();
					remainingTime--;
				}, 1000);
			};
			//结束计时器
			function endTimerClock(){
				clearTimeout(timerClock);
				isTimeing = false;//重置倒计时状态
				remainingTime = 119;
				$(".ny2017-time-1").text("0:00")
				$(".ny2017-pop-main h5").text("好可惜");
				$(".ny2017-pop-main-content-text").html('<p>答题时间已用尽！</p><p>明天再来试试吧！</p>');
				$(".ny2017-pop-ok").removeClass(".ny2017-pop-ok-click-1").text("我知道了");
				$(".ny2017-pop-cancle").hide();
				$(".ny2017-pop").show();
				$(".ny2017-pop-ok").on("click",function(){
					window.location.href = window.location.href;
				})
			};
		</script>
</html>