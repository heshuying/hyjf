if(actStatus == 0 || actStatus == 2){
	$("#cardJin").addClass("nocard");
	$("#cardJi").addClass("nocard");
	$("#cardNa").addClass("nocard");
	$("#cardFu").addClass("nocard");
}
var flgPage1 = true;
$(".page-item").click(function(){
	var self = $(this);
	if(self.hasClass("page1")){
		$("#page1").addClass("active");
		$("#page2").removeClass("active");
		if(flgPage1){
			zclipInit();
			flgPage1 =false;
		}
		
	}else if(self.hasClass("page2")){
		$("#page1").removeClass("active");
		$("#page2").addClass("active");
	}
})

var stage;
var currentType;
var popUp = {
    'overlayer': $(".craker-overlayer"),
    "card": $(".card-pop"), //送好友卡片弹窗
    "crakerSuccess": $(".craker-pop.success"), //点燃鞭炮成功弹窗
    "crakerFaild": $(".craker-pop.faild"), //点燃鞭炮失败弹窗

    "showCardPop": function(ele) {
        var txt = $(ele).data("text");
        currentType = $(ele).attr("data-type"); 
        popUp.overlayer.fadeIn();
        popUp.card.find(".info").find("span").text("“" + txt + "”");
        popUp.card.fadeIn();
    },
    "hideCardPop": function() {
        popUp.overlayer.fadeOut();
        popUp.card.fadeOut();
        getCardData();
    },
    "showCrakerPop": function(statu) {
        popUp.overlayer.fadeIn();
        $(".craker-pop." + statu).fadeIn();
    },
    "hideCrakerPop": function(statu) {
        popUp.overlayer.fadeOut();
        $(".craker-pop." + statu).fadeOut();
        getCardData();
    }
}
var crakerbtn = $(".crakerbtn"); //点燃爆竹按钮
var ua = navigator.userAgent;
if (ua.indexOf("MSIE 6.0") > -1 || ua.indexOf("MSIE 7.0") > -1 || ua.indexOf("MSIE 8.0") > -1) {
    crakerIEInit(); //IE8以下 爆竹燃放用gif
} else {
    crakerInit(); //非IE8以下 爆竹燃放用canvas
}

function crakerIEInit() {
    $("#crakerbox").append("<img src='../../img/active/active_201701/baozhu0.png' id='crakerImg'/>");
    crakerbtn.click(function() {
        crakerbtn.hide();
        $("#crakerImg").attr("src", "../../img/active/active_201701/baozhu.gif");
        setTimeout(function() {
            $("#crakerImg").attr("src", "../../img/active/active_201701/baozhu0.png");
            crakerbtn.show();
        }, 2000);
    })
}

function crakerInit() {
    stage = new createjs.Stage(document.getElementById("crakerCVS"));
    var spriteSheet = new createjs.SpriteSheet({
        framerate: 16,
        "images": ["../../img/active/active_201701/baozhuani.png"],
        "frames": {
            "regX": 276.5,
            "height": 768,
            "count": 24,
            "regY": 0,
            "width": 553
        },
        "animations": {
            "run": [1, 7, "run", 1],
            "stop": [0, 0, 'stop', 1]
        }
    });
    var grant = new createjs.Sprite(spriteSheet, "stop");
    grant.x = stage.canvas.width / 2;
    grant.y = 0;

    stage.addChild(grant);
    createjs.Ticker.timingMode = createjs.Ticker.RAF;
    createjs.Ticker.addEventListener("tick", stage);
    handleStopStart();


    function handleStopStart() {
        crakerbtn.show();
        grant.gotoAndStop("stop");
    }

    function handleRunStart() {
        crakerbtn.hide();
        grant.gotoAndPlay("run");
    }
    crakerbtn.click(function() {
        if (isLogin == 1) {
        	$.ajax({
        		type : "POST",
        		async: false,
        		url : webPath + "/activity/newyear/doPrizeDraw.do",
        		dataType : 'json',
        		data : {
        		},
        		success : function(data) {
        			if (data.status == 0 ) {
        				// 校验成功
        				if(data.prizeType == 1){
        		            popUp.crakerSuccess.find(".pop-content-title").html("恭喜您点燃鞭炮获得<br/><em>" + data.prizeName + "</em>一部<br/>");
        		            popUp.crakerSuccess.find(".pop-content-info").html("iPhone 7 Plus将于活动结束后3个工作日内，由客服电话回访核实用户信息后，统一采购发放，如遇货源短缺，发放时间相应顺延。");
        				}else if(data.prizeType == 0){
        		            popUp.crakerSuccess.find(".pop-content-title").html("恭喜您获得<br/>" + data.prizeName + "一张<br/>");
        		            popUp.crakerSuccess.find(".pop-content-info").html("代金券奖励将于用户成功抽取后，由系统自动发放至用户汇盈金服账户，用户登陆后可于“优惠券”中查看。");

        				}
        				
        				//点燃成功
        	            handleRunStart();
        	            
        	            setTimeout(function() {
        	                handleStopStart();
        	                popUp.showCrakerPop("success"); //弹出成功提示
        	            }, 2000);
        	            
        	            
        			} else {
        				// 校验失败
        				if(data.errCode == 2){
        					//未集齐 已登录财神卡不齐版
        		            popUp.crakerFaild.find(".pop-content-title").html("每集齐一套“金”“鸡”“纳”“福”财神卡即可获得一次点爆竹赢大奖机会，100%中奖。");
        		            //没开始 弹出提示
        		            //popUp.crakerFaild.find(".pop-content-title").html("活动尚未开始！<br/>活动开始时间：2017年2月5日");
        		            //已经开始 未登录 跳登录
        		            /* window.location.href = "./" */
        				}else if(data.errCode == 10){
        					popUp.crakerFaild.find(".pop-content-title").html("活动未开始");
        				}else if(data.errCode == 11){
        					popUp.crakerFaild.find(".pop-content-title").html("活动已结束");
        				}else if(data.errCode == 9){
        					window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
        				}else{
        					//其他  由于XXXX原因，您的爆竹没点着，稍后重新点燃试试吧！
        					popUp.crakerFaild.find(".pop-content-title").html("由于XXXX原因，您的爆竹没点着，稍后重新点燃试试吧！");
        				}
        				
        				popUp.showCrakerPop("faild"); //弹出失败提示
        			}
        		},
        		error : function() {
        			popUp.crakerFaild.find(".pop-content-title").html("由于XXXX原因，您的爆竹没点着，稍后重新点燃试试吧！");
        			popUp.showCrakerPop("faild"); //弹出失败提示
        		}
        	});
        	
        	
            
        } else {
            // 未登录
        	window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
        }
    })
}
//卡片减少操作
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
        var countbox = obj.find(".count-num");
        countbox.text(data);
        if (data <= 0) {
            obj.addClass("nocard");
        }else{
        	obj.removeClass("nocard");
        }
    }
}

//金鸡纳福卡片点击送好友
$(".card .hoverbox").click(function() {
	if(isLogin == 1){
		popUp.showCardPop(this);
	}else{
		window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
	}
})
$(".card-btn.cancel").click(function() {
    popUp.hideCardPop();
})

function verIsMobile(value) {
    //验证是否是手机号
    var length = value.toString().length;
    return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value);
}

//赠送好友卡片手机号验证
$("#cardMobile").on("keyup", function(event) {
    var self = $(this);
    var val = self.val();
    var kc = event.which || event.keyCode;

    //限制输入数字
    if ((kc >= 65 && kc <= 90) || (kc >= 186 && kc <= 222) || kc == 106 || kc == 107 || (kc >= 109 && kc <= 111)) {
        return false;
    }
    //限制11位
    if (val.length > 11) {
        self.val(val.substr(0, 11));
    } else if (val.length == 11 && verIsMobile(val)) {
        //校验用户姓名
    	$.ajax({
    		type : "POST",
    		async: false,
    		url : webPath + "/activity/newyear/doPhoneNumCheck.do",
    		dataType : 'json',
    		data : {
    			"phoneNum": val
    		},
    		success : function(data) {
    			if (data.status == 0 ) {
    				// 校验成功
    				if(data.isValidPhoneNum == 0){
    					if(data.userName && data.userName.length >= 1){
        					data.userName = data.userName.substr(0,1) + "**";
        				} 
        				var userName = data.userName ? data.userName: "好友未开户";
        				
    					popUp.card.find(".verf-txt").text("用户姓名：" + userName);
    				}else if(data.isValidPhoneNum == 1){
    					popUp.card.find(".verf-txt").text("您的好友尚未注册，快去邀请TA注册开户吧！");
    				}
    				if(data.isValidPhoneNum ==2){
    					popUp.card.find(".verf-txt").text("好友未开户");
    				}
    			} else {
    				// 校验失败
    				alert(data.statusDesc);
    			}
    		},
    		error : function() {
    			alert("未知异常")
    		}
    	});
        //未注册
        
    } else if (val.length == 11 && !verIsMobile(val)) {
        popUp.card.find(".verf-txt").text("请输入正确手机号");
    }
})

//赠送卡片确认操作
$(".card-btn.confirm").click(function() {
    var val = $("#cardMobile").val();
    if (val.length !== 11 || !verIsMobile(val)) {
        popUp.card.find(".verf-txt").text("请输入正确手机号");
    } else {
        //后台传值
    	$.ajax({
    		type : "POST",
    		async: false,
    		url : webPath + "/activity/newyear/doCardSend.do",
    		dataType : 'json',
    		data : {
    			"phoneNum": val,
    			"cardIdentifier": currentType
    		},
    		success : function(data) {
    			if (data.status == 0) {
    				// 校验成功
    				if(data.userName && data.userName.length >= 1){
    					data.userName = data.userName.substr(0,1) + "**";
    				} 
    				var userName = data.userName ? data.userName: "好友未开户";
    				
    				popUp.card.find(".verf-txt,.card-input").hide();
    		        popUp.card.find(".success-txt").show().html("赠送卡片：" + data.cardName + "<br/>赠送手机：" + data.phoneNum + "<br/>赠送姓名：" + userName);
    		        popUp.card.find(".info").html("恭喜您财神卡赠送成功！");
    		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
    		        popUp.card.find(".card-btn.done").show();
    			} else {
    				// 校验失败
    				if(data.errCode == 2){
    					popUp.card.find(".verf-txt,.card-input").hide();
        		        popUp.card.find(".success-txt").show().html("您输入的手机号码对应的用户未注册");
        		        popUp.card.find(".info").html("温馨提示：");
        		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
        		        popUp.card.find(".card-btn.done").show();
    					
    				}else if(data.errCode == 3){
    					popUp.card.find(".verf-txt,.card-input").hide();
        		        popUp.card.find(".success-txt").show().html("不能赠送给自己");
        		        popUp.card.find(".info").html("温馨提示：");
        		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
        		        popUp.card.find(".card-btn.done").show();
    					
    				}else if(data.errCode == 4){
    					popUp.card.find(".verf-txt,.card-input").hide();
        		        popUp.card.find(".success-txt").show().html("您的卡片数量已不足");
        		        popUp.card.find(".info").html("温馨提示：");
        		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
        		        popUp.card.find(".card-btn.done").show();
    					
    				}else {
    					popUp.card.find(".verf-txt,.card-input").hide();
        		        popUp.card.find(".success-txt").show().html(data.statusDesc);
        		        popUp.card.find(".info").html("温馨提示：");
        		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
        		        popUp.card.find(".card-btn.done").show();
    				}
    			}
    		},
    		error : function() {
    			popUp.card.find(".verf-txt,.card-input").hide();
		        popUp.card.find(".success-txt").show().html(未知异常);
		        popUp.card.find(".info").html("温馨提示：");
		        popUp.card.find(".card-btn.confirm,.card-btn.cancel").hide();
		        popUp.card.find(".card-btn.done").show();
    		}
    	});
        
    }
})
$(".card-btn.done").click(function() {
    //关闭赠送卡片成功后的弹窗
    popUp.hideCardPop();
    setTimeout(function() {
        popUp.card.find(".success-txt").empty().hide();
        popUp.card.find(".verf-txt").empty().show();
        popUp.card.find(".card-input").val("").show();
        popUp.card.find(".info").html("已选择<span></span>字财神卡");
        popUp.card.find(".card-btn.confirm,.card-btn.cancel").show();
        popUp.card.find(".card-btn.done").hide();
    }, 500);
    
    getCardData();

})

function zclipInit(){
	$("#copy").zclip({
		path: webPath+"/js/ZeroClipboard.swf",
		copy: $("#invite-link").html(),
		afterCopy: function() { /* 复制成功后的操作 */
			$(".invite-copy-hint").show().delay(1500).fadeOut(300);
		}
	});
}

zclipInit();

function getCardData(){
	if(isLogin == 0 || actStatus != 1){
		return;
	}
	
	$.ajax({
		type : "POST",
		async: false,
		url : webPath + "/activity/newyear/getCardData.do",
		dataType : 'json',
		data : {
		},
		success : function(data) {
			if (data.status == 0) {
				// 校验成功
				setCardCount("all", data);
			} else {
				// 校验失败
				if(data.errCode == 9){
					window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
				}else{
					alert(data.statusDesc);
				}
			}
		},
		error : function() {
			alert("未知异常")
		}
	});
}
if(actStatus == 1){
	getCardData();
}

$("#qrcode").each(function() {
	$(this).qrcode({
		text: $("#qrcodeValue").attr("value"),
		render: "canvas", 
		width: 171, 
		height: 171,
		typeNumber: 0, 
		correctLevel: QRErrorCorrectLevel.L,
		background: "#ffffff",
		foreground: "#000000"
	})
});