// JavaScript Document
function b(){	
	t = parseInt(x.css('top'));
	y.css('top','22px');	
	x.animate({top: t - 22 + 'px'},'slow');	//22为每个li的高度
	if(Math.abs(t) == h-22){ //22为每个li的高度
		y.animate({top:'0px'},'slow');
		z=x;
		x=y;
		y=z;
	}
	setTimeout(b,5000);//滚动间隔时间 现在是3秒
}
$(document).ready(function(){
	function aa(){
		$('.swap').html($('.news_li').html());
		x = $('.news_li');
		y = $('.swap');
		h = $('.news_li li').length * 22; //22为每个li的高度
		setTimeout(b,5000);//滚动间隔时间 现在是3秒
	}
	setTimeout(aa,5000)
	$(".nav li").on("click",function(){
		var _self = $(this);
		var index = _self.index();
		_self.addClass("active").siblings().removeClass("active");
		var n = 1-index;
		$(".active-"+index).show();
		$(".active-"+n).hide();
	})
	//调整兑换好礼图片位置
	var aw = $(window).width()*0.26;
	$(".goods-item>img").css("margin-top",-aw/2+"px");
	//载入后设置切换的页面
//	var flag = 1;
//	$(".active-"+flag).show().siblings().hide();
//	$(".nav li").eq(flag).addClass("active").siblings().removeClass("active");
	var url = window.location.host;
	$(".contact-us-abstract a").attr('href','hyjf://jumpH5/?{"url":"http://'+url+'/hyjf-app/jsp/aboutUs2.jsp"}')
	//		点击增加减少购买数量
var inputTxt =	$('input[type=number]').val();
		var remain = $.trim($('.remainNum').text());
		var remainNum =  parseInt(remain);  
		    function change(a){
				var txtNum = parseInt($('input[type=tel]').val());
				$('input[type=tel]').val(txtNum+a);
				return;
		    };
		    //设置兑换数量
		    var lengthItem = $(".goods-item-btn").length;
		    for(var i=0;i<lengthItem;i++){
		    	var btnNum = $(".goods-item-btn").eq(i).text().replace(/[^0-9]/ig,"");
		    	$(".goods-item-btn").eq(i).next().text(btnNum);
		    }
//		    点击增加购买数量
			var itemLen = $(".goods-item").length;
			for(var j=0;j<itemLen;j++){
				//修改数字 改变星数
				$('.num').eq(j).keyup(function(){
					var val = +$(this).val().replace(/\D/g,'');
					if(val === 0){
						$(this).val(1);
					}else{
						$(this).val(val);
					}
					var btn = $(this).parent().next();
					var n = +btn.next().text();
					var num = +$(this).prev().val();
					var a = btn.text();
					var a1 = a.substring(0,5);
					var a2 = a.substr(-3);
					var va = $(this).val()*n;
					$(this).parent().next().text(a1+va.toString()+a2);
				})
				$('.increase-click').eq(j).click(function(){
					$('.tb-stock i').removeClass('unclick');
					var btn = $(this).parent().next();
					var n = +btn.next().text();
					var num = +$(this).prev().val();
					var a = btn.text();
					var a1 = a.substring(0,5);
					var a2 = a.substr(-3);
					var num1 = (num+1)*n ;
					//拼接字符串
					var text1 = a1+num1.toString()+a2;
					btn.text(text1);
					var inputTxt =	+$(this).prev().val();
					if(inputTxt >= remainNum){
						$(this).addClass('unclick');
					}else{
						$(this).prev().val(inputTxt+1);
						$(this).prev().data('num',parseInt(inputTxt)+1).trigger('change');
					}
				});
//			点击减少购买数量
			$('.reduce-click').eq(j).click(function(){
				$('.tb-stock i').removeClass('unclick');
				var btn = $(this).parent().next()
				var n = +btn.next().text();
				var num = +$(this).next().val();
				var a = btn.text();
				var a1 = a.substring(0,5);
				var a2 = a.substr(-3);
				var num1 = (num-1)*n ;
				if(num>1){
					var text1 = a1+num1.toString()+a2;
				btn.text(text1);
				}
				var inputTxt =	+$(this).next().val();
				    if(inputTxt >1){  
				        $(this).next().val(inputTxt-1)
				        $(this).next().data('num',parseInt(inputTxt)-1).trigger('change');
				    }else{  
				        $(this).addClass('unclick');
				    }
			});
			//点击兑换
			$(".goods-item-btn-click").eq(j).on("click",function(){
				//获取范围 星数量
				var ran = $(this).parent().find("p").text();
				var prizeName = $(this).parent().find(".goods-item-top:first").find("i").text();
				var starNum = +$(this).prev().find("input").val() || 0;
				var starValNum = +$(this).next().text(); 
				var groupCode = $(this).parent().find("input").val();
				//选中星的数量
				var starTotal = +starNum*starValNum;
				//当前星的数量
				var starNumTotal  = +$("#starsNumNow").text()||0;
				$(".pop-2-title span").text(prizeName);
				$(".pop-2-main-p span").text("将使用推荐星："+starTotal+"个");
				$(".pop-2-main-p label").text(starNum);
				$("#groupCodeConfirm").val(groupCode);
				$(".pop-2-text").text(ran);
				$(".pop").show();
				//判断剩余券数量是否够用
				var restNum = +$(this).parent().find(".goods-rest span").text();
				if(restNum<starNum){
					$(".pop").show();
					$(".pop-outnumber").show();
					var widthPop = $(".pop-outnumber").width() ;
					$(".pop-outnumber").height(widthPop*1.31);
				}else{
					if(starTotal>starNumTotal){
						$(".pop-error").show();
					}else{
						$(".pop-commit").show();
					}
					var widthPop = $(".pop-commit").width() ;
					$(".pop-commit").height(widthPop*1.31);
					$(".pop-2").height(widthPop*1.31);
					$(".pop-error").height(widthPop*1.31);
				}
			})
		}
			$(".pop-commit .active-detail-btn").click(function(){
				$(".pop-commit").hide();
				var groupCode = $("#groupCodeConfirm").val();
				var changeCount = $("#forChangeCount").text();
				$.ajax({
					url : 'prizeChangeCheck',
					type : "POST",
					async : true,
					data : "groupCode=" + groupCode + "&changeCount=" + changeCount + "&sign=" + $("#sign").val(),
					success : function(result) {
						if(result.status == 0){
							$.ajax({
								url : 'doPrizeChange',
								type : "POST",
								async : true,
								data : "groupCode=" + groupCode + "&changeCount=" + changeCount +  "&sign=" + $("#sign").val(),
								success : function(result2) {
									if(result2.status == 0){
										$(".pop-2-title span").text(result2.prizeName);
										$(".pop-2-main-p span").text("使用推荐星："+result2.recommendCost+"个");
										$(".pop-2-main-p label").text(result2.prizeCount);
										$("#prizeUrlForChangeSuccess").attr({src: result2.prizePicUrl});
										$("#successMsgForChange").text(result2.successMsg);
										$("#popForChangeSuccess").show();
									}else if(result2.errCode == 2){
										// 推荐星不足
										$(".pop-error").show();
									}else if(result2.errCode == 4){
										$("#wxtsMsg").text("很抱歉，可兑换数量不足");
										$("#wxtsPop").show();
									}else {
										$("#wxtsMsg").text(result2.statusDesc);
										$("#wxtsPop").show();
									}
								},
								error : function(err) {
									$("#wxtsMsg").text("数据取得失败!");
									$("#wxtsPop").show();
								}
							});
						}else if(result.errCode == 2){
							// 推荐星不足
							$(".pop-error").show();
						}else if(result.errCode == 4){
							$("#wxtsMsg").text("很抱歉，可兑换数量不足");
							$("#wxtsPop").show();
						}else {
							$("#wxtsMsg").text(result.statusDesc);
							$("#wxtsPop").show();
						}
						
					},
					error : function(err) {
						$("#wxtsMsg").text("数据取得失败!");
						$("#wxtsPop").show();
					}
				});
				
			})
			$("#num").change(function(){
				var dataNum = $('#num').data('num');
					$('.actual-pay').text(dataNum);
			})
				//垂直居中图片
	var heightItem = $(".goods-item>div");
	var heightImg = $(".goods-item>img").height();
	var top = (heightItem-heightImg)/2;
	$(".goods-item>img").css("margin-top",top+"px");
	//关闭弹出
	$(".pop-close").on("click",function(){
		$(".pop").hide();
		window.location.reload();
	})
	$(".pop-ok").on("click",function(){
		$(".pop").hide();
	})
	$("#inviteButtonClick").on("click",function(){
		$(".pop-error").hide();
		$(".pop").hide();
		window.location.href =  inviteUserUrl;
	})
	//兑换后刷新页面
	$(".pop-2 .active-detail-btn").on("click",function(){
		window.location.reload();
	})
	
	
//弹层高度
	var $wWidth = $(window).width();
	$('.pop-error').height($wWidth);
	$('.pop-outnumber').height($wWidth);
})



function setCookie(c_name, value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays)
	document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}
/** 根据cookie名称获取值 */
function getCookie(c_name) {
	if (document.cookie.length > 0) {
		var c_start = document.cookie.indexOf(c_name + "=");
		var c_end;
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1;
			c_end = document.cookie.indexOf(";", c_start);
			if (c_end == -1) {
				c_end = document.cookie.length;
			}
			return unescape(document.cookie.substring(c_start, c_end));
		}
	}
	return "";
}
function checkCookie(cname) {
	var cname = getCookie(cname);
	if (cname != null && cname != "") {
		return true;
	} else {
		return false;
	}
}