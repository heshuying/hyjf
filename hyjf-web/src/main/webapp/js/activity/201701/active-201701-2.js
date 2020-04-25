
var riddlePopUp = {
	"overlayer":$(".riddle-overlayer"),
	"success": $(".riddle-pop.success"),
	"faild": $(".riddle-pop.faild"),
	"wrong": $(".riddle-pop.wrong"),
	"start": $(".riddle-pop.start"),
	"showRiddlePop": function(statu) {
        riddlePopUp.overlayer.fadeIn();
        riddlePopUp.wrong.find(".pop-title").show();
        $(".riddle-pop." + statu).fadeIn();
    },
    "hideRiddlePop": function(statu) {
        riddlePopUp.overlayer.fadeOut();
        $(".riddle-pop." + statu).fadeOut();
    }
}

if(riddleData.userAnswerFlag == "0" && riddleData.questionId != ""){
	if(riddleData.showAnswerFlag == "0"){
		timmerClear();//结束计时
		$(".riddle-box .timmer").hide();
		$(".riddle-box .riddle-btn.start").hide();
		openScroll(); //打开题目卷轴
	}else{
		riddleOver();
	}
	
}

$(".riddle-btn.start").click(function(){
	//开始答题点击操作
	if(riddleData.isLogin == "0"){
		window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
	}else{
		riddleStartConfirm();
	}
	
})

$(".riddle-btn.confirm").click(function(){
	//答案提交操作
	riddleSubmit();
})

$("#riddleConfirmBtn").click(function(){
	//确认答题
	var self = $(this);
	if(self.attr("clicked") == 1){
		return false;
	}
	self.attr("clicked","1");
	$.ajax({
		"type":"post",
		"url":webPath +"/activity/newyear/insertUserAnswerRecordInit.do",
		"data":{"questionId":riddleData.questionId},
		"async":false,
		"success":function(res){
			res=JSON.parse(res);
			if(res.status == "0"){
				riddleStart();
			}else if(res.status == "1"){
				systomErrorFun();
			}
			self.attr("clicked","0");
		},
		error:function(){
			systomErrorFun();
		}
	})
	
})

function systomErrorFun(){
	riddlePopUp.wrong.find(".pop-content-title").html("您的页面已经过期，请刷新重试");//设置成功文字
	riddlePopUp.hideRiddlePop("start");
	riddlePopUp.showRiddlePop("wrong");
	
	riddlePopUp.wrong.find(".pop-closer").click(function(){
		window.location.href = window.location.href;
	});
}
var timmerInterval;
function timmerStart(){
	//开始倒计时
	var wrapper = $(".timmer");
	wrapper.text("2:00").show();
	var total = 120;
	timmerInterval = setInterval(function(){
		if(total <1){
			timmerClear()//计时结束
			riddleFaild();//答题结束
		}else{
			total-=1;
			wrapper.text(_timmerFormat(total));
		}
	},1000);

	function _timmerFormat(ms){
		//格式化分：秒
		return parseInt(ms/60)+":"+_twoDig(parseInt(ms%60));
	}
	function _twoDig(i) {
		//不足两位补零
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }
}

function timmerClear(){
	//停止倒计时
	if(timmerInterval != undefined){
		clearInterval(timmerInterval);
	}
	return false;
}
function openScroll(){
	//打开卷轴
	var s = $(".lartern-scroll");
	s.css("background-image","url("+riddleData.img+")");//填充答案图片
	s.animate({"height":175},300); //展开卷轴
}
function riddleFaild(){
	//答题失败
	riddlePopUp.faild.find(".pop-content-title").html(riddleData.pophtml);//设置失败文字
	riddlePopUp.faild.find(".pop-closer").click(function(){
		riddleOver();//答题结束
	});
	riddlePopUp.hideRiddlePop("wrong");
	riddlePopUp.showRiddlePop("faild");
}
function riddleSuccess(){
	//答题成功
	//riddleOver();//答题结束
	timmerClear();//结束计时
	riddlePopUp.showRiddlePop("success");//弹出成功弹窗
}
function riddleWrong(){
	//答题错误
	riddlePopUp.showRiddlePop("wrong");
}

function riddleStartConfirm(){
	//开始答题确认
	$.ajax({
		"type":"post",
		"url":webPath +"/activity/newyear/check.do",
		"data":{"questionId":riddleData.questionId},
		"async":false,
		success:function(res){
			res=JSON.parse(res);
//			console.info(res);
			if(res.checkStatus == "1"){
	    		riddlePopUp.showRiddlePop("start");
	    	}else if(res.noUserFlg){
	    		window.location.href = webPath + "/user/login/init.do?retUrl=/activity/newyear/init.do";
	    	}else{
	    		console.info(res.noUserFlg);
	    		riddlePopUp.wrong.find(".pop-content-title").html(res.message1+"</br>"+res.message2);//设置成功文字
	    		riddlePopUp.showRiddlePop("wrong");
	    	}
		},
		error:function(){
			systomErrorFun();
		}
	})
	
}
function riddleStart(){
	//开始答题
	riddlePopUp.hideRiddlePop("start"); //隐藏确认弹窗
	timmerStart();
	openScroll(); //打开题目卷轴
	$(".riddle-btn.start").hide(); //隐藏开始答题按钮
	$(".riddle-form").fadeIn();//显示答题表单
}

function riddleOver(){
	//答题结束
	timmerClear();//结束计时
	$(".riddle-btn.start").hide();
	$(".riddle-box .timmer").hide();
	openScroll(); //打开题目卷轴
	riddlePopUp.hideRiddlePop("wrong");
	showTips();//显示
	showAnswer();//显示答案
}
function riddleSubmit(){
	//提交答案
	var val = $("#riddleVal").val();
	$.ajax({
		"type":"post",
		"url":webPath +"/activity/newyear/updateUserAnswerRecord.do",
		"async":false,
		"data":{"questionId":riddleData.questionId,"userAnswer":val,},
		success:function(res){
			res=JSON.parse(res);
			if(res.isCorrect==1){
				riddlePopUp.success.find(".pop-content-title").html("回答正确！<br/>您已成功点亮今日红灯笼！<br/><br/><em>您的奖励增值为"+res.prompt+"元代金券"+res.couponCount+"张</em>");//设置成功文字
	    		riddleSuccess(); //答题成功
	    	}else{
	    		riddleWrong(); //答题错误
	    	}
		},
		error:function(){
			systomErrorFun();
		}
	})
	
}

function showAnswer(){
	//显示答案
	$(".riddle-answer").text("谜底："+riddleData.answer).show();
	$(".riddle-form").hide();
}
var tipsInterval;
function showTips(){
	//显示小贴士
	var len = riddleData.tips.length;
	var showlen = len;
	$(".riddle-tips").empty().show();
	tipsInterval = setInterval(function(){
		if(showlen<=len){
			$(".riddle-tips").text(riddleData.tips.substring(0,showlen));
			showlen++;
		}else{
			clearInterval(tipsInterval);
		}
	},150);
}