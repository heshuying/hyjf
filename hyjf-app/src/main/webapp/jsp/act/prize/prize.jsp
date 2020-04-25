<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalabe=0" />
<link rel="stylesheet" href="${ctx}/css/indiana.css" />
<title>投资夺宝 一箭双雕</title>
</head>
<body style="font-size: 14px;">
	<input id="sign" name="sign" type="hidden" value="${sign}" />
	<input id="platform" name="platform" type="hidden" value="${platform}" />
	<input id="version" name="version" type="hidden" value="${version}" />
	<div class="indinaInvest">
		<div class="indianaBg-1"></div>
		<div class="indianaBg-2">
			<p>
				您当前有<span class="prizeCount">${prizeCount}</span>次夺宝机会
			</p>
			<a href="hyjf://jumpInvest/?" id="tenderBtn">立即投资</a>
		</div>
		<div class="indianaBg-3 indiana-ban">
			<div class="indiana-left">
				<p>${P0001.prizeName}</p>
			</div>

			<!--抽奖进行中-->
			<div class="indiana-right">
				<input id="prizeStatus" name="prizeStatus" type="hidden" value="${P0001.prizeStatus}" /> 
				<input id="prizeCode" name="prizeCode" type="hidden" value="${P0001.prizeCode}" /> 
				<input id="userName" name="userName" type="hidden" value="${P0001.userName}" />
				<input id="canPrize" name="canPrize" type="hidden" value="${P0001.canPrize}" />
				<p>
					总需：<span class="totalNum">${P0001.allPersonCount}</span>人次
				</p>
				<div class="indianaPercent">
					<div class="joinPercent"></div>
				</div>
				<div class="peoplenum">
					<span class="left-align"> <i class="jionsNum">${P0001.joinedPersonCount}</i>
						已参与人数
					</span> <span class="right-align"> <i class="remainNum">${P0001.allPersonCount-P0001.joinedPersonCount}</i>
						剩余人次
					</span>
				</div>
				<div class="clear">
					<a href="javascript:;" class="indianaStart" id="${P0001.prizeSelfCode}">马上夺宝</a>
				</div>
			</div>

			<!--抽奖完成-->
			<div class="winPrize">
				<p>
					中奖用户：<span>chXXXXXXX</span>
				</p>
				<div class="winBg">
					中奖幸运码： <span>2d141grp</span>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div class="indianaBg-4 indiana-ban">
			<input id="prizeStatus" name="prizeStatus" type="hidden" value="${P0002.prizeStatus}" /> 
			<input id="prizeCode" name="prizeCode" type="hidden" value="${P0002.prizeCode}" /> 
			<input id="userName" name="userName" type="hidden" value="${P0002.userName}" />
			<input id="canPrize" name="canPrize" type="hidden" value="${P0002.canPrize}" />
			<div class="indiana-left">
				<p>${P0002.prizeName}</p>
			</div>

			<!--抽奖进行中-->
			<div class="indiana-right">
				<p>
					总需：<span class="totalNum">${P0002.allPersonCount}</span>人次
				</p>
				<div class="indianaPercent">
					<div class="joinPercent"></div>
				</div>
				<div class="peoplenum">
					<span class="left-align"> <i class="jionsNum">${P0002.joinedPersonCount}</i>
						已参与人数
					</span> <span class="right-align"> <i class="remainNum">${P0002.allPersonCount-P0002.joinedPersonCount}</i>
						剩余人次
					</span>
				</div>
				<div class="clear">
					<a href="javascript:;" class="indianaStart" id="${P0002.prizeSelfCode}">马上夺宝</a>
				</div>
			</div>

			<!--抽奖完成-->
			<div class="winPrize">
				<p>
					中奖用户：<span>chXXXXXXX</span>
				</p>
				<div class="winBg">
					中奖幸运码： <span>2d141grp</span>
				</div>
			</div>

			<div class="clear"></div>
		</div>
		<div class="indianaBg-5 indiana-ban">
			<input id="prizeStatus" name="prizeStatus" type="hidden" value="${P0003.prizeStatus}" /> 
			<input id="prizeCode" name="prizeCode" type="hidden" value="${P0003.prizeCode}" /> 
			<input id="userName" name="userName" type="hidden" value="${P0003.userName}" />
			<input id="canPrize" name="canPrize" type="hidden" value="${P0003.canPrize}" />
			<div class="indiana-left">
				<p>${P0003.prizeName}</p>
			</div>

			<!--抽奖进行中-->
			<div class="indiana-right">
				<p>
					总需：<span class="totalNum">${P0003.allPersonCount}</span>人次
				</p>
				<div class="indianaPercent">
					<div class="joinPercent"></div>
				</div>
				<div class="peoplenum">
					<span class="left-align"> <i class="jionsNum">${P0003.joinedPersonCount}</i>
						已参与人数
					</span> <span class="right-align"> <i class="remainNum">${P0003.allPersonCount-P0003.joinedPersonCount}</i>
						剩余人次
					</span>
				</div>
				<div class="clear">
					<a href="javascript:;" class="indianaStart" id="${P0003.prizeSelfCode}">马上夺宝</a>
				</div>
			</div>

			<!--抽奖完成-->
			<div class="winPrize">
				<p>
					中奖用户：<span>chXXXXXXX</span>
				</p>
				<div class="winBg">
					中奖幸运码： <span>2d141grp</span>
				</div>
			</div>


			<div class="clear"></div>
		</div>
		<div class="indianaBg-6">
			<div class="myfortuneBtn">
				<a href="javascript:;">我的幸运码</a>
			</div>
		</div>
		<div class="indianaBg-7">
				<p>活动时间：2016年8月11日至2016年9月10日</p>
				<p>活动规则：<br>活动期内，新注册用户累计投资金额首次达1000元，即可领取<span style="color:#f0fd38;">50元代金券</span>；此外，投资每满1000元更可获得一次夺宝机会赢取<span style="color:#f0fd38;">潮流数码大奖</span>！</p>
				<p>用户登陆后，于投资夺宝活动页面选择心仪奖品参与夺宝，即可收获一条幸运码用于夺宝抽奖。</p>
				<p>当参与人次达到总需人次或活动截止时，系统将从该奖品发放的幸运码中随机抽取一条获奖！</p>
				<p>同一奖品参与夺宝次数越多，中奖几率越大哟~机会有限，速来夺取吧！</p>
				<p>汇盈金服为您准备了<span style="color:#f0fd38;">炫酷耳机、尖端数码</span>，更有<span style="color:#f0fd38;">iPhone6s Plus</span>等你来拿！</p>
				<p>愿幸运的你，大奖现金，一箭双雕，丰收盛夏！</p>
				<p>奖励发放：<br>1.实物奖品将于活动结束后统一采购发放，如遇货源紧张，发放时间相应顺延。</p>
				<p>2.代金券将于用户投标成功后发放至用户账户，登陆后于“优惠券”中查看。</p>
				<p class="zhushi">注：<br>1.仅投资新手汇、汇直投项目金额参与活动。</p>
				<p class="zhushi">2.50元代金券包含一张20元代金券和一张30元代金券，均自发放之日起30日内有效，过期作废。20元代金券需投资汇直投项目单笔投资2000元及以上可用，30元代金券需投资汇直投项目单笔投资5000元及以上可用。</p>
				<p class="indiana-fontsize">本活动与Apple Inc.无关。<br>本活动最终解释权归汇盈金服所有。</p>
			</div>

	</div>

	<!-- 夺宝成功获得幸运码 -->
	<div class="congratulations bothconfort" id="successPop">
		<div class="congratulationBg"></div>
		<!--成功获得幸运码-->
		<div class="congratulation-con">
			<div class="congrate-content">
				<p class="awards"></p>
				<p class="getFortune-code">
					获得幸运码 <span></span>
				</p>
				<a href="javascript:;" class="getFortune-sure closepage">确定</a>
			</div>
		</div>
	</div>
	
	<!-- 没有夺宝机会 -->
	<div class="congratulations bothconfort" id="failPop">
		<div class="congratulationBg"></div>
	<div class="congratulation-con congratulation-lose" >
		<div class="nochance">
			<p>抱歉，您当前没有夺宝机会！</p>
			<a href="hyjf://jumpInvest/?" id="operationBtn">去投资</a> <a
				class="getFortune-sure closepage" href="javascript:;">再看看</a>
			<div class="clear"></div>
		</div>
	</div>
	</div>
	
	<!-- 夺宝人次已满 -->
	<div class="congratulations bothconfort" id="failPop2">
		<div class="congratulationBg"></div>
	<div class="congratulation-con congratulation-lose" >
		<div class="nochance">
			<p>抱歉，夺宝人次已满！</p> <a style="margin-left: 1.9678rem; !important;"
				class="getFortune-sure fullPrize" href="javascript:;">知道了</a>
			<div class="clear"></div>
		</div>
	</div>
	</div>

	<!--我的兑奖码开始-->
	<div class="myFortune bothconfort">
		<div class="congratulationBg"></div>
		<div class="myFortune-con">
			<div class="myFortune-content">
                 <p>
					<span style="width:2.5547987rem">时间</span>
					<span style="width:2.5529952rem">夺宝奖品</span>
					<span style="width:1.480306rem">幸运码</span>
					</p>
				<!--我的兑奖码列表-->
				<div class="expiryCode-con">
				</div>
				<a href="javascript:;" class="getFortune-sure closepage">关闭</a>
			</div>
		</div>
	</div>
	<!--我的兑奖码结束-->
</body>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"
	charset="utf-8"></script>
<script>
	function changeFZ() {
		var w = $(window).width();
		setHtmlFz(w / 10);
	}
	function setHtmlFz(fz) {
		$("html").css("font-size", fz + "px");
	}
	window.onload = function() {
		changeFZ();
	}
	window.onresize = function() {
		changeFZ();
	}
</script>
<script>
	var indianali = $('.indiana-ban');
	var both = $('.bothconfort');
	var getfortunes = $('.myfortuneBtn');
	// 参与人数进度条与获奖商品名称显示
	indianali.each(function() {
		var mn = $(this).find('.joinPercent');
		// 总需人次
		var totalNums = $(this).find('.totalNum').text();
		// 已参与人次
		var jionsNums = $(this).find('.jionsNum').text();
		// 参与夺宝按钮数组
		var indianaStarts = $(this).find('.indianaStart');
		// 获得幸运码弹层
		var congrates = $('#successPop');
		percents = jionsNums * 100 / totalNums;
		mn.animate({
			"width" : percents + "%"
		});
		
		// 初始化开奖情况
		// 奖品是否开奖
		var prizeStatus = $(this).find("#prizeStatus").val();
		// 中奖码
		var prizeCode = $(this).find("#prizeCode").val();
		// 中奖用户
		var userName = $(this).find("#userName").val();
		if(prizeStatus == 1){
			// 中奖用户
			$(this).find(".winPrize").children('p').children("span").text(userName);
			// 中奖码
			$(this).find(".winPrize").children('div').children("span").text(prizeCode);
			// 弹层显示
			$(this).find(".winPrize").show();
			$(this).find(".indiana-right").hide();
		}else{
			// 弹层隐藏
			$(this).find(".winPrize").hide();
			$(this).find(".indiana-right").show();
		}
		// 是否可以夺宝
		var canPrize = $(this).find("#canPrize").val();
		// 夺宝按钮挂载点击事件
		indianaStarts.each(function(){
			// 取得奖品识别码
			var prizeSelfCode = $(this).prop("id");
			
			if(canPrize=='false'){
				$(this).prop("class","indianaEnd");
				$(this).text("等待开奖");
				return;
			}
			$(this).click(function(){
				var thisObj = $(this);
				// 夺宝按钮不可用
				thisObj.prop("disabled","disabled");
				var url = "${ctx}/prize/userPrize?prizeSelfCode="+prizeSelfCode;
				//var url = "http://test.hyjf.com:8080/hyjf-app/prize/userPrize?prizeSelfCode="+prizeSelfCode;
				var data = {};
				data.sign = $('#sign').val();
				data.platform = $('#platform').val();
				data.version = $('#version').val();
				// 异步请求
				doRequest(congrate, url, data);
				// 参与夺宝回调函数
				function congrate(data) {
					// 设置奖品名称
					awardsname = thisObj.parents('.indiana-right').siblings(
							'.indiana-left').children('p').text();
					congrates.find('.awards').text(awardsname);
					//设置幸运码
					congrates.find('.getFortune-code span').text(data.prizeCode);
					// 弹层夺宝幸运码
					congrates.show();
					// 设置夺宝次数
					$(".prizeCount").text(data.prizeCount);
					var joinedPersonCount = data[prizeSelfCode].joinedPersonCount;
					var allPersonCount = data[prizeSelfCode].allPersonCount;
					// 已参与人次
					thisObj.parents('.indiana-right').find(".jionsNum").text(joinedPersonCount);
					// 剩余人次
					thisObj.parents('.indiana-right').find(".remainNum").text(allPersonCount-joinedPersonCount);
					// 计算参与百分比
					percents = joinedPersonCount * 100 / allPersonCount;
					thisObj.parents('.indiana-right').find(".joinPercent").animate({
						"width" : percents + "%"
					});
					if(data.canPrize==false){
						thisObj.prop("class","indianaEnd");
						thisObj.text("等待开奖");
					}else{
						// 夺宝按钮可用
						thisObj.removeAttr("disabled");
					}
				}
			});
		});
	});
	// 我的抽奖码按钮挂载事件
	getfortunes.click(function() {
		var url = "${ctx}/prize/userPrizeList";
		// var url = "http://test.hyjf.com:8080/hyjf-app/prize/userPrizeList";
		var data = {};
		data.sign = $('#sign').val();
		// 异步请求
		doRequest(fortune, url, data);
		// 我的抽奖码回调函数
		function fortune(data) {
			if(data.prizeCodeList.length>0){
				$('.expiryCode-con').append("<table></table>");
				var table = $('.expiryCode-con').find("table");
				
				// 拼装我的抽奖码列表
				$.each(data.prizeCodeList,function(n,obj){
					if(obj.prizeFlg == 1){
						table.append("<tr style='color: #f0fd37'><td style='background: url(../img/tuoyuan.png) no-repeat left;background-size:10% 100%;padding-left:0.2rem;width:2.5547987rem'>"+obj.addTime+"</td><td style='width:2.5529952rem'>"+obj.prizeName+"</td><td style='width:1.720306rem'>"+obj.prizeCode+"</td></tr>");
					}else{
						table.append("<tr><td style='width:2.5547987rem'>"+obj.addTime+"</td><td style='width:2.5529952rem'>"+obj.prizeName+"</td><td style='width:1.720306rem'>"+obj.prizeCode+"</td></tr>");
					}
					
				});
			}/* else{
				// 如果没有抽奖码
				$('.expiryCode-con').text("您还没有领取夺宝码！");
			} */
			// 弹层显示
			$('.myFortune').show();
		}
	});

	// 幸运码和兑奖码关闭弹层
	both.each(function() {
		surebtn = $(this).find('.closepage');
		surebtn.click(function() {
			$(this).parents('.bothconfort').hide();
			// 清空我的兑奖码列表数据
			$('.expiryCode-con').empty();
		});
	});
	
	$('.fullPrize').click(function() {
		$(this).parents('.bothconfort').hide();
		window.location.href = "/hyjf-app/prize/init?sign="+$('#sign').val();;
	});

	// 封装ajax
	// 需要传的data有夺宝总人数、已参与人数、剩余人数,中奖幸运码,我的兑奖码
	function doRequest(successCallback, url, data) {
		try {
			var paramsInner = {
				url : url,
				async: false,
				type : 'post',
				dataType : 'json',
				success : function(data) {
					if (data.status == '0') {
						successCallback(data);
					} else {
						if(data.errorCode == '1'){
							// 未登录 跳转到登录页面
							window.location.href = data.request;
						}else if(data.errorCode == '2'){
							// 已没有夺宝机会
							$('#failPop').show();
						}else if(data.errorCode == '3'){
							// 已没有夺宝机会
							$('#failPop2').show();
							
						}

					}
				}
			};
			paramsInner.data = data;
			$.ajax(paramsInner);
		} catch (e) {
			var err = {
				'result_code' : 1,
				'error_code' : 1,
				'error' : e.message
			};
		}
	}
</script>
</html>