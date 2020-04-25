var timmer;
Number.prototype.toFixed2 = function() {
	return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
}
var tabs = $(".new-detail-tab");
tabs.each(function(){
    var li = $(this).children();
    var len = li.length;
    li.css("width",100/len+"%");
});



$(".new-detail-main").click(function(e) {
	var _self = $(e.target);
	if (_self.is("li")) {
		var idx = _self.attr("panel");
		var panel = _self.parent().next(".new-detail-tab-panel");
		_self.siblings("li.active").removeClass("active");
		_self.addClass("active");
		panel.children("li.active").removeClass("active");
		panel.children("li[panel=" + idx + "]").addClass("active");
	}
})
$.validator.addMethod("increase", function(value, element, params) {
	/*递增金额验证*/
	var increase = parseFloat($("#increase").val()); //递增金额
	var total = parseFloat($("#projectData").data("total")); //可投金额
	var tendermin = parseFloat($("#projectData").data("tendermin"));//起投金额
	if(total<tendermin || total<increase){
		//若金额出现异常，检查是否是最后一笔交易
		return total == value;
	}
    return (value-tendermin)%increase == 0; //投资金额对倍增金额求模为0

},"投资递增金额须为"+$("#increase").val()+"元的整数倍");

var validate = $("#detailForm").validate({
	"rules" : {
		"money" : {
			"required" : {
				depends : function() {
					//登录才验证
					return $("#userData").data("openflag");
				}
			},
			"number" : true,
			"min" : getMinMoney(),
			"max" : getMaxMoney(),
			"increase" : {
				depends:function(){
					//有递增金额才验证
					return $("#increase").val()!= "" && !isNaN($("#increase").val());
				}
			}
		},
		"termcheck" : {
			required : {
				depends : function() {
					return $(".appoint-term").length;
				}
			}
		}
	},
	"messages" : {
		"money" : {
			"required" : "亲，您还没有填写预约金额",
			"number" : "亲，只能填写数字",
			"min" : "预约金额应"+(isLast()?"等于":"大于 ") + getMinMoney() +"元",
			"max" : "预约金额应"+(isLast()?"等于":"小于") + getMaxMoney() + "元"
		},
		"termcheck" : "请先阅读并同意《预约投标服务需知》",
		"increase" : "预约递增金额须为"+$("#increase").val()+"元的整数倍"
	},
	"ignore" : ".ignore",
	errorPlacement : function(error, element) {
		error.insertAfter(element.parent());
	},
	submitHandler : function(form) {
		tenderCheck(form);
	}
});
//判断是否是最后一笔
function isLast(){
	var res ;
	if($("#isLast").val() == "true"){
		res = true;
	}else{
		res = false;
	}
	return res;
}
$("a.confirm-btn").click(function() {
	if(!checkTested()){//判断是否测评过
		showTip(null, "为更好保障您的投资利益，<br/>您须在完成风险测评后才可进行投资", "tip", "testPop",{"confirm":"立即测评"});
	}else if($(this).hasClass("avaliable") ){
		$("#detailForm").submit();
	}
})
//测评弹窗回调
function dealAction(id){
	if(id == "testPop"){ 
		window.location.href =webPath+ "/financialAdvisor/financialAdvisorInit.do";
	}
} 

//检测是否测评过
function checkTested(){
	var res ;
	$.ajax({
		type : "POST",
		async : false,
		url : webPath +"/financialAdvisor/getUserEvalationResultByUserId.do",
		dataType : 'json',
		success : function(data) {
			//true ： 测评过      false：未测评
			if(data.userEvaluationResultFlag==1){
				res = true;
			}else{
				res = false;
			}
		}
	})
	return res;
}

//全投的时候应该判断，用户余额，可投资金额，最大投资金额
$(".money-btn").click(function() {
	if($(this).hasClass("available")){
		var res = getMaxInvestMoney();
		//投资金额
		$("#money").val(res);
		$("#money").trigger("change");
		$("#money").valid();
		priceChanged($("#money"));	
	}
})
//金额变动
 $("#money").keyup(function() {
		var _self = $(this);
		var val = _self.val();
		if(!checkNumber(val)){
			_self.val(val.replace(/[^\d]/g,''));
		}
		if(timmer){
		    clearTimeout(timmer);
		}
		timmer = setTimeout(function(){
		    priceChanged($("#money"));
		},500);
}).keydown(function(){
    $("#ajaxErr").remove();
});

function checkNumber(val){
    /*检测是否是数字*/
    var reg = /^[0-9]*$/;
    if(reg.test(val)){
        return true;
    }
    return false;
}

//获取全投金额
function getMaxInvestMoney() {
	//项目最大投资金额
	var maxMoney =  isNaN(parseInt($("#projectData").data("tendermax")))? 0 :parseInt($("#projectData").data("tendermax"));
	//项目可投资金额
	var total = isNaN(parseInt($("#projectData").data("total")))? 0 :parseInt($("#projectData").data("total"));
	//可用余额
	var balance = isNaN(parseInt($("#userData").data("balance")))? 0 :parseInt($("#userData").data("balance"));
	var res = 0;
	if($("#userData").data("balance") == ""){
		res = (total >= maxMoney ? maxMoney : total)
	}else{
		if(balance == 0){
			window.location.href = webPath + "/recharge/rechargePage.do";
		}else{
			res = (total >= balance ? balance : total)
			res = (res >= maxMoney ? maxMoney : res);
		}
	}
	return res;
}

//获取最大的投资金额
function getMaxMoney() {
	//项目最大投资金额
	var maxMoney =  isNaN(parseInt($("#projectData").data("tendermax")))? 0 :parseInt($("#projectData").data("tendermax"));
	//项目可投资金额
	var total = isNaN(parseInt($("#projectData").data("total")))? 0 :parseInt($("#projectData").data("total"));
	//可用余额
	var res =  (total >= maxMoney ? maxMoney : total)
	return res;
}

//获取最小的投资金额
function getMinMoney() {
	//项目最大投资金额
	var min = isNaN(parseInt($("#projectData").data("tendermin")))? 0 :parseInt($("#projectData").data("tendermin"));
	//项目可投资金额
	var total =isNaN(parseInt($("#projectData").data("total")))? 0 :parseInt($("#projectData").data("total"));
	//项目剩余金额
	var res = (total <= min ? total : min);
	return res;
}
//ajax请求获取历史回报
function priceChanged(ipt) {
	var money = isNaN(parseInt($('#money').val())) ? 0 :parseInt($('#money').val());
	$.ajax({
		type : "POST",
		async:false,
		url : webPath + "/user/invest/investInfo.do",
		dataType : 'json',
		data : {
			"nid" : $('#nid').val(),
			"money" : money,
			"appoint" : "1"
		},
		success : function(data) {
			if (data.status == true) {
				$("#income").text(data.earnings);
			} else {
				$("#income").text("0.00");
			}
		},
		error : function() {
			var ipt = $("#money");
			if($("#ajaxErr").length){
				$("#ajaxErr").html("网络异常，请检查您的网络").show();
			}else{
				ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
			}
			
		}
	});
	ipt.valid();
}
//ajax校验投资
function tenderCheck(form) {
	var money = isNaN(parseInt($('#money').val())) ? 0 :parseInt($('#money').val());
	$.ajax({
		type : "POST",
		async:false,
		url : webPath + "/user/invest/appointCheck.do",
		dataType : 'json',
		data : {
			"nid" : $('#nid').val(),
			"money" : $('#money').val(),
			"termcheck":$('#termcheck').val()
		},
		success : function(data) {
			if (data.status == true) {
				if(checkToken() == true){
					form.submit();
				}else{
					//错误信息为
					var ipt = $("#money");
					if($("#ajaxErr").length){
						$("#ajaxErr").html("请不要重复提交！").show();
					}else{
						ipt.parent().parent().append("<span id='ajaxErr' class='error'>请不要重复提交！</span>");
					}
				}
			//跳转登陆
			} else if(data.errorCode =="707"){
				window.location.href = webPath + "/user/login/init.do";
			} else if(data.errorCode =="708"){
				window.location.href = webPath + "/user/openaccount/init.do";	
			} else {
				//错误信息为
				var ipt = $("#money");
				if($("#ajaxErr").length){
					$("#ajaxErr").html(data.message).show();
				}else{
					ipt.parent().parent().append("<span id='ajaxErr' class='error'>"+data.message+"</span>");
				}
			}
		},
		error : function() {
			var ipt = $("#money");
			if($("#ajaxErr").length){
				$("#ajaxErr").html("网络异常，请检查您的网络").show();
			}else{
				ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
			}
		}
	});
}
//初始化幻灯
baguetteBox.run('.new-detail-img-c', {
	animation : 'fadeIn'
});

$(document).click(function(e){
	if($(e.target).attr("id") == "money-error" || $(e.target).attr("id") == "termcheck-error" || $(e.target).attr("id") == "ajaxErr"){
		$(e.target).remove();
	}
});
$(document).ready(function() {
	/**
	 * 获取公司动态
	 */
	getProjectInvestListPage();
	$(document).on("click", ".investClass", function() {
		investList($(this).data("page"));
	});

	if ($("#type").val() == '8') {
		/**
		 * 获取公司动态
		 */
		getProjectConsumeListPage();
		$(document).on("click", ".consumeClass", function() {
			consumeList($(this).data("page"));
		});
	}
});
function getProjectInvestListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	doRequest(
		projectInvestPageSuccessCallback, 
		projectInvestPageErrorCallback,
		webPath + "/project/getProjectInvest.do?borrowNid="+ $("#borrowNid").val(), 
		$("#listForm").serialize(),
		true,
		"investClass",
		"invest-pagination"
	);
}
/**
 * 获取公司动态信息成功回调
 */
function projectInvestPageSuccessCallback(data) {
	
	var projectInvestList = data.projectInvestList;
	var investTotal = data.investTotal;
	var investTimes = data.investTimes;
	// 挂载数据
	var projectInvestListStr = "";
	if(projectInvestList.length==0){
		projectInvestListStr = projectInvestListStr
		+ '<tr>' 
		+ '	<td rowspan="4">未查询到数据</td>'
		+ '</tr>'
	}else{
		var projectInvestListHead = '';
		projectInvestListHead = projectInvestListHead
		+ '	<tr>'
		+ '		<th>用户名</th>'
		+ '		<th width="310">投标金额</th>'
		+ '		<th width="370">投标时间</th>'
		+ '		<th width="210">投标来源</th>'
		+ '	</tr>'
		$("#projectInvestListHead").html(projectInvestListHead);
		for (var i = 0; i < projectInvestList.length; i++) {
			var projectInvest = projectInvestList[i];
			var vipflg = "";
			if(projectInvest.vipId != "0"){
				vipflg = '<img src="'+webPath+'/img/vip/vip_'+projectInvest.vipLevel+'.png" class="vipflg" width="18"/>';
			}
			projectInvestListStr = projectInvestListStr
			+ '<tr>' 
			+ '   <td>' +vipflg+' '+ projectInvest.userName + '</td>'
			+ '   <td><span class="highlight">￥' + projectInvest.account + '</span></td>'
			+ '   <td>' + projectInvest.investTime + '</td>' 
			+ '   <td>' + projectInvest.client + '</td>'
			+ ' </tr>'
		}
	}
	$("#projectInvestList").html(projectInvestListStr);
	$("#investTotal").html(investTotal);
	$("#investTimes").html(investTimes);
}
/**
 * 获取公司动态信息失败回调
 */
function projectInvestPageErrorCallback(data) {

}
/**
 * 分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function investList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage);
	$("#pageSize").val(pageSize);
	doRequest(projectInvestPageSuccessCallback, 
		projectInvestPageErrorCallback,
		webPath + "/project/getProjectInvest.do?borrowNid=" + $("#borrowNid").val(),
		$("#listForm").serialize(),
		true,
		"investClass",
		"invest-pagination"
	);
}
function getProjectConsumeListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	doRequest(
		projectConsumePageSuccessCallback,
		projectConsumePageErrorCallback, 
		webPath + "/project/getProjectConsume.do?borrowNid=" + $("#borrowNid").val(),
		$("#listForm").serialize(),
		true,
		"consumeClass", 
		"consume-pagination"
	);
}

/**
 * 获取公司动态信息成功回调
 */
function projectConsumePageSuccessCallback(data) {
	var projectConsumeList = data.projectConsumeList;
	// 挂载数据
	var projectConsumeListStr = "";
	if(projectConsumeList.length==0){
		projectConsumeListStr = projectConsumeListStr
		+ '<tr>' 
		+ '	<td rowspan="3">未查询到数据</td>'
		+ '</tr>'
	}else{
		var projectConsumeListHead = '';
		projectConsumeListHead = projectConsumeListHead
		+ '	<tr>'
		+ '		<th width="270">姓名</th>'
		+ '		<th>身份证</th>'
		+ '		<th width="430">融资金额</th>'
		+ '</tr>'
		$("#projectConsumeListHead").html(projectConsumeListHead);
		for (var i = 0; i < projectConsumeList.length; i++) {
			var projectConsume = projectConsumeList[i];
			projectConsumeListStr = projectConsumeListStr 
			+ '<tr>' 
			+ '   <td>' + projectConsume.name + '</td>' 
			+ '   <td>' + projectConsume.idCard + '</td>'
			+ '   <td><span class="highlight">￥' + projectConsume.account + '</span></td>' 
			+ '</tr>'
		}
	}
	$("#projectConsumeList").html(projectConsumeListStr);
}

/**
 * 获取公司动态信息失败回调
 */
function projectConsumePageErrorCallback(data) {

}

/**
 * 分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function consumeList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage);
	$("#pageSize").val(pageSize);
	doRequest(
		projectConsumePageSuccessCallback,
		projectConsumePageErrorCallback, 
		webPath + "/project/getProjectConsume.do?borrowNid=" + $("#borrowNid").val(),
		$("#listForm").serialize(),
		true,
		"consumeClass",
		"consume-pagination"
	);
}

//保存充值页面地址，充值成功调用
function setCookie() {
	$.cookie('beforeUrl', null, {expires: 30, path: '/', domain: 'hyjf.com', secure: false});
	if($.cookie("beforeUrl")==null||$.cookie("beforeUrl")=="null"){
		$.cookie('beforeUrl', window.location.href, {expires: 30, path: '/', domain: 'hyjf.com', secure: false});
	}
}

$(".appoint-term .checkicon").click(function(){
	if($(this).hasClass("avaliable")){
		if(!$(".appoint-term .checkicon").hasClass("checked")){
			$(this).addClass("checked");
			$(this).children("input").prop("checked",true).valid();
		}else{
			$(this).removeClass("checked");
			$(this).children("input").prop("checked",false).valid();
		}
	}
})
var currTimmer;
var box = $("#time");
function renderTimer(){
	var ts = lastTime || parseInt(lastTime);
    if (ts > 0) {
        timer(box, ts);
    } else {
        clearInterval(currTimmer);
        //倒计时为0时执行
        $("#money").keyup(function(){
        	return false;
        }).removeClass("available");
        $(".confirm-btn").removeClass("avaliable").addClass("disabled").text("预约完成");
        $(".money-btn").removeClass("available");
        $(".checkicon").removeClass("avaliable");
    }
    if(!currTimmer){
    	currTimmer = setInterval(function() {
    		renderTimer();
        }, 1000);
    }
}
//倒计时
function timer(box, ts) {
    /*
     * @param box 存放倒计时容器
     * @param ts 剩余时间
     * @param current 当前时间
     */
    ts = ts-1000;
    lastTime = ts;
    var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10); //计算剩余的天数  
    var hh = parseInt(ts / 1000 / 60 / 60, 10); //计算剩余的小时数  
    var mm = parseInt(ts / 1000 / 60 % 60, 10); //计算剩余的分钟数  
    var ss = parseInt(ts / 1000 % 60, 10); //计算剩余的秒数  
    dd = checkTime(dd);
    hh = checkTime(hh);
    mm = checkTime(mm);
    ss = checkTime(ss);
    box.html("预约截止时间:<strong>" + hh + "</strong> 小时 <strong>" + mm + "</strong> 分 <strong>"+ss+"</strong>秒");
}

function checkTime(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}

function openVIP(){
	window.location.href=webPath+"/vip/apply/init.do";
}
//vip购买弹出层
function setCookieVip(c_name, value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays)
	document.cookie = c_name + "=" + escape(value) +";path=/"+ ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}
$(".vip-buy-close").click(function(){
	$(".vip-buy-pop-div").animate({top:"-62px"},600).animate({backgroundSize:"42%"},400,function(){
		$(".vip-buy-pop").fadeOut(800);
		$("html").css("overflow","auto");
	})
	
});