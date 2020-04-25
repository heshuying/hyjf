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
        "money": {
            "number": true,
            "min" :  getMinMoney(),
			"max" : getMaxMoney(),
			"maxlength":9,
			"increase" : {
				depends : function() {
					// 有递增金额才验证
					return $("#increase").val() != "" && $("#money").val() != "";
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
        "money": {
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
        assess:$("#riskFlag").val() // 是否进行过风险测评
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
        return false;
    }else if(userStat.open == "0"){//未开户验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"您尚未开户，请先开通银行存管账户",
            btntxt:"立即测评",
            fnconfirm: function(){
                window.location.href = webPath + "/bank/web/user/bankopen/init.do";
            }
        });
        return false;
    }else if(userStat.setPwd == "0"){//未设置交易密码验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"请先去设置交易密码。",
            fnconfirm: function(){
                window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
            }
        });
        return false;
    }else if(userStat.assess == "0"){//未测评验证
        utils.alert({ 
            id: "verfiDialog", 
            content:"为符合监管政策要求<br/>投资前必须进行<span class='highlight'>“风险测评”</span><br/>以便更好地保障您的利益",
            btntxt:"立即测评",
            fnconfirm: function(){
                window.location.href =  webPath+ "/financialAdvisor/financialAdvisorInit.do";
            }
        });
        return false;
    }
    if ($("#productForm").valid() != true) { //前端表单验证
        return false;
    } 
    //投资校验接口
    prodRes = productValidate();
    if(prodRes != ""){
    	alertConfirm(prodRes);//弹窗传入“同步”结果，
    }
});
//弹出确认投资弹窗
function alertConfirm(prodRes) {
    if(prodRes.status == true){
        //后台验证通过后，验证前台，验证通过弹出确认框
        var fnAlertConfirm = function() {
            if ($("#productForm").valid() == true && checkToken()) {
            	setCookie("productToken","1",360);
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
    var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
    var couponGrantId = $("#couponInput").data("id");
    //返回变量
    var res = "";
    $.ajax({
        type : "POST",
        async : false,
        url : webPath + "/bank/web/user/tender/investCheck.do",
        dataType : 'json',
        data : {
            "nid" : $('#borrowNid').val(),
            "money" : money,
            "couponGrantId" : couponGrantId
        },
        success : function(data) {
        	res = data;
	        if (data.status == true) {
	        	//设置投资确认框
	            setConfirmMessage();
	        } 
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
var moneyTimeout; //存储
$("#money").keyup(function(){
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
function moneyChanged(couponGrantId){
	couponGrantId = couponGrantId || "";
	delAjaxError();
    if(moneyTimeout){
        clearTimeout(moneyTimeout);
    }
    moneyTimeout = setTimeout(function(){
        //设置历史回报
        var gains = getGains(couponGrantId);
        setGains(gains);
        $("#goSubmit").removeClass("unava");// 输入结果返回后使投资按钮可用
    },500);
    
}
function getGains(couponGrantId){//获取历史回报
    var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
    var income ="0.00";
    $.ajax({
        type : "POST",
        async : false,
        url : webPath + "/bank/web/user/tender/investInfo.do",
        dataType : 'json',
        data : {
            "nid" : $('#borrowNid').val(),
            "money" : money,
            "couponGrantId":couponGrantId
        },
        success : function(data) {
            if (data.status == true) {
                income = data.earnings;
                //重新选择优惠券
                if (data.isThereCoupon == "1") {
    				var couponData = data.couponConfig;
    				var id=couponData.userCouponId;
    				var type=couponData.couponType;
    				var txt='<span class="num">'+couponData.couponQuotaStr + (couponData.couponType == 2 ? "%" : "元")+'</span>' + (couponData.couponType == 2 ? "加息券" : couponData.couponType == 1 ? "体验金" : "代金券");
    			    _stageCoupon(id, type, txt);
    			    _setCoupon(); //设置优惠券
    			    $("#couponInput").val(couponData.userCouponId);
    			} else {
    				var id="";
    				var type="";
    				var txt='您有  <span class="num">' + data.couponAvailableCount + '</span> 张优惠券可用';
    			    _stageCoupon(id, type, txt); //暂存当前优惠券信息
    			    _setCoupon(); //设置优惠券
    			    $("#couponInput").val("");
    			}
                //确认历史回报
                $("#totalInterest").val(data.earnings);
                
            }else if(data.status == false){
            	if($("#money").valid()){
            		$("#money").parent().parent().append('<div id="ajaxerror" class="error">'+data.message+'</div>');
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
    
    return income;
}
function setGains(val){//设置历史回报
    $("#income").text(val+" 元");
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
	productMax = $("#projectData").data("tendermax");; //项目最大投资金额 
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
    $("#money").change();
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
    return fully;
}
//输出全投金额
function _setFully(fully) {
    var moneyInput = $("#money");
    moneyInput.val(fully);
    moneyInput.valid();//验证金额
}



/* 优惠券
==============================================================================
==============================================================================
*/

//弹窗内暂存，确定后确认变更
var coupon = {
    current: { //存储当前已选优惠券信息
        id: "",
        type: "",
        txt: ""
    },
    stage: { //暂存当前待选优惠券信息
        id: "",
        type: "",
        txt: ""
    }
}

//优惠券弹窗
$("#goCoupon").click(function() {
    var couponipt = $("input#couponInput"); //优惠券input
    var id = couponipt.data("id"), //当前优惠券id
        type = couponipt.data("type"), //当前优惠券类型
        txt = couponipt.data("txt"); //当前优惠券文字
	    //拉取优惠券列表
	    _getUserCouponList();
	    _setCurrentCoupon(id, type, txt); //保存当前优惠券信息
	    _stageCoupon(id, type, txt); //暂存当前优惠券信息
	    alertCoupon(); //弹出优惠券弹窗
});
//优惠券选择
$("#couponDialog").delegate(".coupon-item","click",function() {
    var _this = $(this);
    if(_this.attr("id") == "unava"){
        return false;
    }
    var id = _this.data("id"),
        type = _this.data("type"), //当前优惠券类型
        txt = _this.data("txt");
    var checked = !_this.hasClass("checked"); //目前需要的是否是checked
    var hadCoupon = $("#couponInput").data("count") == "0" ? false : true; //是否有优惠券
    if (checked) {
        //设置选中状态
        _removeChecked();
        _this.addClass("checked");
        
    } else {
        //设置未选中状态
        _removeChecked();
        if (!hadCoupon) {
            txt = "暂无优惠券";
        }
        id = "-1";
    }
    _stageCoupon(id, type, txt);
    
});

//弹出优惠券弹窗
function alertCoupon() {
    var fnAlertCouponConfirm = function() {
        //优惠券确认事件
        _setCoupon(); //设置优惠券
        moneyChanged(coupon.current.id);
        if (coupon.current.id < 0) {
        	var couponAvailableCount = $("#couponAvailableCount").val();
			$("#goCoupon").html('<span> 您有 <span class="num">'+couponAvailableCount+'</span> 张优惠券可用 ');
		}
        utils.alertClose("couponDialog");
    }
    utils.alert({ id: "couponDialog", fnconfirm: fnAlertCouponConfirm });
}

function _removeChecked() {
    //移除已选优惠券
    $(".coupon-item.checked").removeClass("checked");
}
//设置当前优惠券
function _setCurrentCoupon(id, type, txt) {
    coupon.current = {
        id: id,
        type: type,
        txt: txt
    };
    //设置选择框里内选中样式
    if(id != null && id != ""){
        $(".coupon-item[data-id="+id+"]").addClass("checked");
    }
}
//暂存优惠券
function _stageCoupon(id, type, txt) {
    coupon.stage = {
        id: id,
        type: type,
        txt: txt
    }
}
//获取优惠券
function _getUserCouponList() {
    var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
    var couponGrantId = $("#couponInput").data("id");
    $.ajax({
        type : "POST",
        async : false,
        url : webPath + "/coupon/getProjectAvailableUserCoupon.do",
        dataType : 'json',
        data : {
            "nid" : $('#borrowNid').val(),
            "money" : money,
            "couponGrantId" : couponGrantId
        },
        success : function(data) {
            var ava = data.availableCouponList;
            var nava = data.notAvailableCouponList;
            var box = $(".alert-coupon-content");
            var avabox = box.children("#available");
            var navabox = box.children("#unavailable");
            var avaStr = "";
            var navaStr = "";
            if (ava.length != 0 || nava.length != 0) {
                for (var avai = 0; avai < ava.length; avai++) {
                    avaStr += '<div class="coupon-item ' + (_formatType(ava[avai].couponType)) 
                        + '" data-type="' + ava[avai].couponTypeStr 
                        + '" data-id="' + ava[avai].userCouponId 
                        + '" data-txt="' + ava[avai].couponQuota+(_formatVal(ava[avai].couponType))+ava[avai].couponType + '">' 
                        + '<div class="main">' 
                            + '<span class="num">' + ava[avai].couponQuota + '</span>' 
                            + '<span class="unit">' + _formatVal(ava[avai].couponType) + '</span>'
                            + '<span class="type">' + ava[avai].couponType + '</span></div>'
                            + '<div class="attr">'
                            + '<table> <tr>'
                            + '<td width="72" align="right">投资金额：</td>'
                            + '<td>'+ava[avai].tenderQuota+'</td></tr>'
                            + '<tr><td align="right">操作平台：</td>'
                            + '<td>'+ava[avai].couponSystem+'</td></tr>'
                            + '<tr><td align="right">投资期限：</td>'
                            + '<td>'+ava[avai].projectExpiration+'</td></tr>'                           
                            + '<tr><td align="right">项目类型：</td>'
                            + '<td>'+ava[avai].projectType+'</td></tr>'                 
                            + '</table>  </div>'
                            + '<div class="date">'+ava[avai].couponAddTime+'~'+ava[avai].couponEndTime+'</div>'
                            + '<div class="checked">已选中</div>'
                        + '</div>';
                }

                for (var navai = 0; navai < nava.length; navai++) {
                    var h = nava[navai];
                    navaStr += '<div class="coupon-item unava">' 
                        + '<div class="main">' 
                            + '<span class="num">' + h.couponQuota + '</span>' 
                            + '<span class="unit">' + _formatVal(h.couponType) + '</span>'
                            + '<span class="type">' + h.couponType + '</span></div>'
                            + '<div class="attr">'
                            + '<table> <tr>'
                            + '<td width="72" align="right">投资金额：</td>'
                            + '<td>'+h.tenderQuota+'</td></tr>'
                            + '<tr><td align="right">操作平台：</td>'
                            + '<td>'+h.couponSystem+'</td></tr>'
                            + '<tr><td align="right">投资期限：</td>'
                            + '<td>'+h.projectExpiration+'</td></tr>'                           
                            + '<tr><td align="right">项目类型：</td>'
                            + '<td>'+h.projectType+'</td></tr>'  
                            + '</table>  </div>'
                            + '<div class="date">'+h.couponAddTime+'~'+h.couponEndTime+'</div>'
                        + '</div>';
                }
                avabox.html(avaStr);
                navabox.html(navaStr);
            } else {
                avabox.parent().html('<div class="nocoupon">暂无可用优惠券</div>');
            }

            // 根据类型获取样式class
            function _formatType(type) {
                var color = "";
                if (type == "加息券") {
                    color = "jxq";
                } else if (type == "体验金") {
                    color = "tyj";
                } else if (type == "代金券") {
                    color = "djq";
                }
                return color;
            }
            // 根据类型获取值的样式
            function _formatVal(type) {
                var str = "";
                if (type == "加息券") {
                    str = '%';
                } else if (type == "体验金") {
                    str = '元';
                } else if (type == "代金券") {
                    str = '元';
                }
                return str;
            }
        },
        error : function() {
            alert("网络异常，请检查您的网络");
        }
    });
    
}


//设置优惠券
function _setCoupon() {
    //将暂存区信息 保存到当前优惠券
    coupon.current = coupon.stage;
    //设置显示的文字
    $("#goCoupon").children("span").html(coupon.current.txt);
    //存储数据到元素
    $("#couponInput").data(coupon.current)
        //存储优惠券id到表单
        .val(coupon.current.id);
    $("#couponGrantId").val(coupon.current.id);
    //优惠券变动，收益变动方法
}

//投资记录
/*$(document).click(function(e) {
    if ($(e.target).attr("id") == "money-error" || $(e.target).attr("id") == "ajaxErr") {
        $(e.target).remove();
    }
});*/
$(document).ready(function() {
    /**
     * 获取投资记录
     */
    getProjectInvestListPage();
    /**
     * 投资记录分页点击
     */
    $(document).on("click", ".investClass", function() {
        investList($(this).data("page"));
    });
    //汇消费
	if ($("#type").val() == '8') {
		/**
		 * 债权信息列表
		 */
		getProjectConsumeListPage();
		$(document).on("click", ".consumeClass", function() {
			consumeList($(this).data("page"));
		});
	}
    
    
});
/**
 * 获取投资记录
 */
function getProjectInvestListPage() {
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/project/getHtjProjectInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
}
/**
 * 获取投资记录成功回调
 */
function projectInvestPageSuccessCallback(data) {

    var projectInvestList = data.projectInvestList;
    var investTotal = "加入总人次：" + data.investTimes;
    var investTimes = "加入金额：" + data.investTotal + "元";
    // 挂载数据
    var projectInvestListStr = "";
    if (projectInvestList.length == 0) {
        projectInvestListStr = projectInvestListStr + '<tr>' + '<td colspan="4"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无投资记录</p></div></td>' + '</tr>'
    } else {
        for (var i = 0; i < projectInvestList.length; i++) {
            var projectInvest = projectInvestList[i];
            var vipflg = "";
            if (projectInvest.vipId != "0") {
                vipflg = '<div class="tag">VIP</div>';
            }
            projectInvestListStr = projectInvestListStr 
                    + '<tr>'
                    + '   <td><span>' + projectInvest.userName + '</span>'+vipflg+'</td>' 
                    + '   <td>' + projectInvest.account + '</td>' 
                    + '   <td>' + projectInvest.client + '</td>'
                    + '   <td><span class="dark">' + projectInvest.investTime + '</span></td>' 
                    + ' </tr>'
        }
    }
    
    $("#projectInvestList").html(projectInvestListStr);
    $("#investTotal").html(investTotal);
    $("#investTimes").html(investTimes);
}
/**
 * 获取投资记录失败回调
 */
function projectInvestPageErrorCallback(data) {

}
/**
 * 获取投资记录分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function investList(paginatorPage) {
    $("#paginatorPage").val(paginatorPage);
    $("#pageSize").val(pageSize);
    doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/project/getHtjProjectInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
}


/**
 * 获取汇消费债权记录
 */
function getProjectConsumeListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	doRequest(projectConsumePageSuccessCallback, projectConsumePageErrorCallback, webPath + "/bank/web/borrow/getBorrowConsume.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "consumeClass", "consume-pagination");
}

/**
 * 汇消费债权列表成功回调
 */
function projectConsumePageSuccessCallback(data) {
	var projectConsumeList = data.projectConsumeList;
	// 挂载数据
	var projectConsumeListStr = "";
	if (projectConsumeList.length == 0) {
		projectConsumeListStr = projectConsumeListStr + '<tr>' + '	<td colspan="3"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div></td>' + '</tr>'
	} else {
		for (var i = 0; i < projectConsumeList.length; i++) {
			var projectConsume = projectConsumeList[i];
			projectConsumeListStr = projectConsumeListStr
									+ '<tr>' 
									+ '   <td>' + projectConsume.name + '</td>' 
									+ '   <td>' + projectConsume.idCard + '</td>' 
									+ '   <td>' + projectConsume.account + '</td>' 
									+ '</tr>'
		}
	}
	$("#projectConsumeList").html(projectConsumeListStr);
}

/**
 * 汇消费债权列表失败回调
 */
function projectConsumePageErrorCallback(data) {

}

/**
 * 汇消费债权列表分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function consumeList(paginatorPage) {
	$("#paginatorPage").val(paginatorPage);
	$("#pageSize").val(pageSize);
	doRequest(projectConsumePageSuccessCallback, projectConsumePageErrorCallback, webPath + "/project/getProjectConsume.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "consumeClass", "consume-pagination");
}

//设置确认投资页数据
function setConfirmMessage() {
    var confirmValue = $("#confirmValue");// 投资金额
    var confirmCouponName = $("#confirmCouponName");// 优惠券名称
    var confirmInterest = $("#confirmInterest");// 收益
    var confirmRealMoney = $("#confirmRealMoney");// 实际支付金额
    
    confirmValue.text(_couponDefaultValue($("#money").val()));
    var couponGrantId = $("#couponInput").data("id");
    if(couponGrantId=''||couponGrantId<=0){
    	//确认优惠券信息
    	confirmCouponName.text('未使用优惠券');
    }else{
    	confirmCouponName.html(_couponDefaultValue($("#couponInput").data("txt")));
    }
    
    confirmInterest.text(_couponDefaultValue($("#totalInterest").val()) + "元");
    confirmRealMoney.text(_couponDefaultValue($("#money").val()));
}

function _couponDefaultValue(val) {
    return val == "" || typeof val == "undefined" ? "0" : val;
}


if($(".new-detail-img-c").find("li").length > 0){
    baguetteBox.run('.new-detail-img-c', {
        animation : 'fadeIn'
    });
}
//打开新连接
function openNew(url) {
	window.open(url);
}

var coutdownEle = $("#counterTimer");
function getEndTime(){
	var endTime  = "";
	var serverNow = coutdownEle.data("now")*1000;
	var end = coutdownEle.data("date")*1000;
	var localNow = new Date();
	var sub = serverNow - localNow.getTime();
	endTime = end - sub;
	var date = new Date(endTime+1000);
	return date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
}
if (coutdownEle.length > 0) {
    var options = {
        color: "black",
        dayTextNumber: 2,
        displayDay: false,
        displayHour: true,
        displayLabel: true,
        displayMinute: true,
        displaySecond: true,
        onFinish: function() {
            //utils.refresh();
        },
        color: "white",
        reflection: false,
        style: "flip",
        textGroupSpace: 15,
        textSpace: 0,
        timeText: getEndTime(),
        timeZone: 8,
        width: "260",
        displayLabel: false
    };
    coutdownEle.children(".countdown").jCountdown(options);
}

$(function(){
	//token过期刷新
	var productToken = getCookie("productToken");
	if(productToken != ""){
		setCookie("productToken","");
		utils.refresh();
	}
})
if(isLast()){
	moneyChanged();
}