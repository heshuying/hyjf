//表单验证
$.validator.addMethod("increase", function(value, element, params) {
	/* 递增金额验证 */
	var increase = parseFloat($("#increase").val()); // 递增金额
	var total = parseFloat($("#projectData").data("total")); // 可投金额
	var tendermin = parseFloat($("#projectData").data("tendermin"));// 起投金额
	var tendermax = parseFloat($("#projectData").data("tendermax"));// 最大投资金额
	if (total < tendermin || total < increase) {
		// 若金额出现异常，检查是否是最后一笔交易 
		return total == value;
	}else if(value == total || value == tendermin || value == tendermax ){
		//验证投资金额是否等于可投金额 或 起投金额
		return true;
	}
	return (value - tendermin) % increase == 0; // 投资金额对倍增金额求模为0

}, "投资递增金额须为" + $("#increase").val() + "元的整数倍");
var productForm = $("#productForm").validate({
    "rules": {
        "assignCapital": {
            "required": true,
            "number": true,
            "min" :  getMinMoney(),
			"max" : getMaxMoney(),
			"maxlength":9,
			"increase" : {
				depends : function() {
					// 有递增金额才验证
					return $("#increase").val() != "" && $("#assignCapital").val() != "";
				}
			}
        },
        "termcheck": {
            "required": {
                "depends": function() {
                    //弹窗内验证 协议是否勾选
                    return $("#confirmDialog").is(":visible");
                }
            }
        }
    },
    "messages": {
        "assignCapital": {
            "required": "亲，您还没有填写投资金额",
            "number": "亲，只能填写数字",
            "maxlength":"您输入的金额太大了",
            "min" : "投资金额应" + (isLast() ? "等于" : "大于 ") + getMinMoney() + "元",
            "max" : "投资金额超过项目剩余！"
        },
        "termcheck": "请先阅读并同意汇盈金服投资协议",
        "increase" : "投资递增金额须为" + $("#increase").val() + "元的整数倍"
    },
    errorElement: "div",
    "ignore": ".ignore",
    errorPlacement: function(error, element) {
        if(error.attr("id") == "termcheck-error"){
            //当协议勾选错误 插入到弹层中
            $(".product-term").append(error);
        }else{
            error.insertAfter(element.parent());
        }
        
    },
    submitHandler: function(form) {
        form.submit();
    }
});
//判断是否是最后一笔
function isLast() {
	return $("#isLast").val() == "true" || $("#isLast").val() == true;
}
//获取最大的投资金额
function getMaxMoney() {
	// 项目最大投资金额
	var maxMoney =  parseInt($("#projectData").data("tendermax"));
	// 项目可投资金额
	var total =  parseInt($("#projectData").data("total"));
	// 可用余额
	var res = (total >= maxMoney ? maxMoney : total);
	return res;
}

// 获取最小的投资金额
function getMinMoney() {
	//优惠券为代金券 或 体验金 且未输入值时不需要最小值
	if (($("#couponType").val() == "3" || $("#couponType").val() == "1") && $("#money").val()) {
		return 0;
	}
	// 项目最大投资金额
	var min = parseInt($("#projectData").data("tendermin"));
	// 项目可投资金额
	var total = parseInt($("#projectData").data("total"));
	// 项目剩余金额
	var res = (total <= min ? total : min);
	
	return res;
}
//产品详情tab切换
$(".tab-tags li").click(function(e) {
    var _self = $(this);
    var idx = _self.attr("panel");
    var panel = $(".tab-panels");
    _self.siblings("li.active").removeClass("active");
    _self.addClass("active");
    panel.children("li.active").removeClass("active");
    panel.children("li[panel=" + idx + "]").addClass("active");
});

//弹窗协议
$(".term-checkbox").click(function() {
    var _this = $(this);
    var checkbox = $("#productTerm");
    var checked = checkbox.prop("checked");
    checked = !checked;
    checkbox.prop("checked", checked);
    checked ? _this.addClass("checked") : _this.removeClass("checked");
    checkbox.valid();//验证金额
});


/* 立即投资
==============================================================================
==============================================================================
*/
$("#goSubmit").click(function() {
    var prodRes; //后台接口验证项目可用情况
    var userStat = { //用户状态
            login:$("#loginFlag").val(), //是否登录
            open:$("#openFlag").val(), //是否开户
            setPwd:$("#setPwdFlag").val(), //是否设置交易密码,
            assess:$("#riskFlag").val(), // 是否进行过风险测评
            isUserValid:$("#isUserValid").val() //是否禁用
        };
    if ($(this).hasClass("unava")){ //不可用标签 unava
        return false;
    }
    if (userStat.login == "0"){ //验证未登录
        utils.alert({ 
            id: "verfiDialog", 
            content:"您尚未登录，请先去登录",
            fnconfirm: function(){
                window.location.href = webPath + "/user/login/init.do";
            }
        });
        return false;;
    }else if(userStat.open == "0"){//未开户验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"您尚未开户，请先开通银行存管账户",
            fnconfirm: function(){
                window.location.href = webPath + "/bank/web/user/bankopen/init.do";
            }
        });
        return false;;
    }else if(userStat.setPwd == "0"){//未设置交易密码验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"请先去设置交易密码。",
            fnconfirm: function(){
                window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
            }
        });
        return false;;
    }else if(userStat.assess == "0"){//未测评验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"为符合监管政策要求<br/>投资前必须进行<span class='highlight'>“风险测评”</span><br/>以便更好地保障您的利益",
            btntxt:"立即测评",
            fnconfirm: function(){
                window.location.href =  webPath+ "/financialAdvisor/financialAdvisorInit.do";
            }
        });
        return false;;
    }else if(userStat.isUserValid == "1"){//用户被禁用
        utils.alert({ 
            id: "verfiDialog", 
            content:"该用户已被禁用"
        });
        return false;;
    }
    if ($("#productForm").valid() != true) { //前端验证
        return false;
    } 
    //投资校验接口
    prodRes = productValidate();
    if(prodRes != ""){
    	alertConfirm(prodRes);//弹窗传入“同步”结果，
    }
});
//TODO
//弹出确认投资弹窗
function alertConfirm(prodRes) {
    if(prodRes.status == true){
        //后台验证通过后，验证前台，验证通过弹出确认框
        var fnAlertConfirm = function() {
            if ($("#productForm").valid() == true && checkToken()) {
            	setCookie("assignToken","1",360);
                productForm.currentForm.submit();
            }
        }
        utils.alert({ id: "confirmDialog", fnconfirm: fnAlertConfirm });
    }else{
        //设置错误弹窗确认操作
        var code = prodRes.errorCode;
        if (code == "707") {//未登录
        	utils.alert({
				id:"setLoginPop",
				content:"您尚未登录，请先去登录",
				fnconfirm:function(){
					window.location.href = webPath + "/user/login/init.do";
				}
			});
        } else if (code == "708") {//未开户
        	utils.alert({
				id:"setOpenPop",
				content:"您尚未开户，请先开通银行存管账户",
				btntxt:"立即开户",
				fnconfirm:function(){
					window.location.href = webPath + "/bank/web/user/bankopen/init.do";
				}
			});
        } else if(code == "709"){//未设置交易密码
        	utils.alert({
				id:"setPwdPop",
				content:"请先去设置交易密码。",
				fnconfirm:function(){
					window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
				}
			});
        } else if(code == "710"){//未测评
        	utils.alert({
				id:"testPop",
				content:"为符合监管政策要求<br/>投资前必须进行<span class='highlight'>“风险测评”</span><br/>以便更好地保障您的利益",
	            btntxt:"立即测评",
				fnconfirm:function(){
					window.location.href = webPath+ "/financialAdvisor/financialAdvisorInit.do";
				}
			});
        } else if(code == "714"){
        	utils.alert({
				id:"testPop",
				content:"为符合监管政策要求<br/>投资前必须进行<span class='highlight'>“风险测评”</span><br/>以便更好地保障您的利益",
	            btntxt:"立即测评",
				fnconfirm:function(){
					window.location.href = webPath+ "/financialAdvisor/financialAdvisorInit.do";
				}
			});
        }else {
            // 错误信息为
            utils.alert({
				id:"errPop",
				type:"alert",
				content:prodRes.message
			});
        }
    }
}
//后台接口验证项目可用情况
function productValidate() {
    $("#goSubmit").addClass("unava");//暂时设置按钮不可用
    var assignCapital = isNaN(parseInt($('#assignCapital').val())) ? 0 : parseInt($('#assignCapital').val());
    //返回变量
    var res = "";
    $.ajax({
        type : "POST",
        async : false,
        url : webPath + "/bank/user/credit/webcheckcredittenderassign.do",
        dataType : 'json',
        data : {
            "creditNid" : $('#creditNid').val(),
            "assignCapital" : assignCapital
        },
        success : function(data) {
        	res = data;
	        $("#goSubmit").removeClass("unava");//移除不可用标识
        },
        error : function() {
            utils.alert({
				id:"errPop",
				type:"alert",
				content:"网络异常，请检查您的网络",
				fnconfirm:utils.refresh
			});
        }
    });
    return res;
}
/* 计算历史回报
==============================================================================
==============================================================================
*/
var moneyTimeout;
$("#assignCapital").keyup(function(){
	var _self = $(this);
	var val = _self.val();
	var reg = /^[0]|[^0-9]/g;
	$("#goSubmit").addClass("unava");//暂时设置按钮不可用
	_self.val(val.replace(reg, ''));
	moneyChanged();
}).change(function(){
    moneyChanged();
});
function delAjaxError(){
	$("#ajaxerror").remove();
}
/**
 * 获取投资确认及收益信息
 * @returns
 */
function moneyChanged(){
	delAjaxError();
	if(moneyTimeout){
		clearTimeout(moneyTimeout);
	}
    moneyTimeout = setTimeout(function(){
    	if($("#assignCapital").val() == ""){
    		cleanInfo();
    		return;
    	}
        //设置历史回报
        getGains();
        $("#goSubmit").removeClass("unava");// 输入结果返回后使投资按钮可用
    },500);
}
function getGains(){//获取历史回报
    var money = isNaN(parseInt($('#assignCapital').val())) ? 0 : parseInt($('#assignCapital').val());
    $.ajax({
    	type: "POST",
		async: "async",
		url: webPath + "/bank/user/credit/webCreditTenderInterest.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"creditNid":$("#creditNid").val(),
			"assignCapital":$("#assignCapital").val()
		},
        success : function(data) {
            if (data.resultFlag==0) {
            	//历史回报
				$("#income").html(data.creditAssign.assignInterest + "元");
				//实际支付
				$("#act_pay_num").html(data.creditAssign.assignPay + "元");
				//垫付利息
				$("#assign_interest_advance").html(data.creditAssign.assignInterestAdvance + "元");
				// 弹出浮层数据提示
				// 认购本金
				$("#assign_capital_confirm").html(data.creditAssign.assignCapital);
				// 垫付利息
				$("#assign_interest_advance_confirm").html(data.creditAssign.assignInterestAdvance + "元");
				// 历史回报
				$("#income_confirm").html(data.creditAssign.assignInterest + "元");
				// 实际支付
				$("#act_pay_num_confirm").html(data.creditAssign.assignPay + "元");
            }else if(data.resultFlag!=0){
            	if($("#assignCapital").valid()){
            		$("#assignCapital").parent().parent().append('<div id="ajaxerror" class="error">'+data.message+'</div>');
            	}
            } 
        },
        error : function() {
            utils.alert({
 				id:"errPop",
 				type:"alert",
 				content:"网络异常，稍后再试"
 			});
        }
    });
}
/* 全投
==============================================================================
==============================================================================
*/
$("#fullyBtn").click(function() {
	var balance = $("#userBalance").val(), //余额
    productMin = $("#projectData").data("tendermin"), //起投
    productAscend = $("#increase").val(), //递增金额
    productAvail = $("#projectData").data("total"); //项目可投金额
	productMax = $("#projectData").data("tendermax"); //项目最大投资金额
	if($("#loginFlag").val() == "0"){
		//未登录
		utils.alert({ 
            id: "verfiDialog", 
            content:"您尚未登录，请先去登录",
            fnconfirm: function(){
                window.location.href = webPath + "/user/login/init.do";
            }
        });
        return false;
	}else if(balance<productMin){
    	//余额不足
    	utils.alert({ 
            id: "balanceLowDialog", 
            content:"可用金额不足，请充值！",
            fnconfirm: function(){
                window.location.href = webPath + "/bank/web/user/recharge/rechargePage.do";
            }
        });
    	return false;
    }
	var fully = _getFully(balance,productMin,productAscend,productAvail,productMax);
    _setFully(fully);
    $("#assignCapital").change();
});

//计算全投金额
/* balance余额
 * productMin起投
 * productAscend递增金额
 * productAvail项目可投金额
 * productMax项目最大投资金额 
*/
function _getFully(balance,productMin,productAscend,productAvail,productMax) {
    var balanceAvail = parseInt((balance - productMin) / productAscend) * productAscend + productMin; //可投资余额
    var fully = Math.min(balanceAvail, productAvail, productMax); //全投金额
    fully = getAssignPay(fully);
    return fully;
}
//全投获取历史回报
function getAssignPay(fully){
	var assignPay = 0;
    $.ajax({
    	type: "POST",
		async:false,
		url: webPath + "/bank/user/credit/webCreditTenderInterest.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"creditNid":$("#creditNid").val(),
			"assignCapital":fully
		},
        success : function(data) {
            if (data.resultFlag==0) {
            	assignPay=data.creditAssign.assignCapitalMax;
            }else if(data.resultFlag!=0){
            	if($("#assignCapital").valid()){
            		$("#assignCapital").parent().parent().append('<div id="ajaxerror" class="error">'+data.message+'</div>');
            	}
            } 
        },
        error : function() {
            utils.alert({
 				id:"errPop",
 				type:"alert",
 				content:"网络异常，稍后再试"
 			});
        }
    });
    return assignPay;
}

//输出全投金额
function _setFully(fully) {
    var moneyInput = $("#assignCapital")
    moneyInput.val(fully);
    moneyInput.valid();//验证金额
}

//只能输入数字
function checkOnlyNumber(val){
    /*检测是否是数字*/
    var reg = /^[0-9]*$/;
    if(reg.test(val)){
        return true;
    }
    return false;
}
/**
 * 清零收益信息
 * @returns
 */
function cleanInfo(){
	//历史回报
	$("#income").html( "0.00元");
	//实际支付
	$("#act_pay_num").html("0.00元");
	//垫付利息
	$("#assign_interest_advance").html( "0.00元");
}
$(document).ready(function() {
    /**
     * 投资记录分页点击
     */
    $(document).on("click", ".assignClass", function() {
    	getAssignList($(this).data("page"));
    });
});
/**
 * 获取投资记录
 */
function getAssignListPage() {
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    doRequest(assignPageSuccessCallback, assignPageErrorCallback, webPath + "/credit/planwebcredittenderpage.do?creditNid=" + $("#creditNid").val(), $("#listForm").serialize(), true, "assignClass", "assignListPage");
}
/**
 * 获取投资记录成功回调
 */
function assignPageSuccessCallback(data) {
    var assignList = data.recordList;
    var assignTotal = "承接总人次：";
    var assignTimes = "承接金额：";
    // 挂载数据
    var assignListStr = "";
    if (assignList.length == 0) {
    	assignListStr = assignListStr + '<tr>' + '<td colspan="4"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div></td>' + '</tr>'
        assignTotal += "0";
        assignTimes += "0元";
    } else {
        for (var i = 0; i < assignList.length; i++) {
            var assign = assignList[i];
            var vipflg = "";
            if (assign.assignType != "0") {
                vipflg = '<div class="tag">VIP</div>';
            }
            assignListStr = assignListStr 
                + '<tr>'
                + '   <td><span>' + assign.userName + '</span>'+vipflg+'</td>' 
                + '   <td>' + assign.assignCapital + '</td>' 
                + '   <td>' + assign.updateUserName + '</td>'
                + '   <td><span class="dark">' + assign.createUserName + '</span></td>' 
                + ' </tr>'
        }
        assignTotal += data.assignTotal;
        assignTimes += data.assignTimes+"元";
    }
    $("#projectAssignList").html(assignListStr);
    $("#assignTotal").html(assignTotal);
    $("#assignTimes").html(assignTimes);
}
/**
 * 获取投资记录失败回调
 */
function assignPageErrorCallback(data) {

}
/**
 * 获取投资记录分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function getAssignList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage);
    $("#pageSize").val(pageSize);
    doRequest(assignPageSuccessCallback, assignPageErrorCallback, webPath + "/credit/planwebcredittenderpage.do?creditNid=" + $("#creditNid").val(), $("#listForm").serialize(), true, "assignClass", "assignListPage");
}

$(function(){
	//token过期刷新
	var productToken = getCookie("assignToken");
	if(productToken != ""){
		setCookie("assignToken","");
		utils.refresh();
	}
})
if($(".new-detail-img-c").find("li").length > 0){
    baguetteBox.run('.new-detail-img-c', {
        animation : 'fadeIn'
    });
}
