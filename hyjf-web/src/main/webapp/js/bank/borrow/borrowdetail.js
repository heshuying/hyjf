var timmer;
Number.prototype.toFixed2 = function() {
	return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
}
var tabs = $(".new-detail-tab");
tabs.each(function() {
	var li = $(this).children();
	var len = li.length;
	li.css("width", 100 / len + "%");
});

// 设置暂存选项
var couponStage = $("#couponPop").find(".coupon-pop-box").children("ul.available").clone();// 暂存优惠券选项
$("#useCoupon").click(
		function() {
			var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
			var couponGrantId = $('#couponGrantId').val();
			$.ajax({
				type : "POST",
				async : false,
				url : webPath + "/coupon/getProjectAvailableUserCoupon.do",
				dataType : 'json',
				data : {
					"nid" : $('#nid').val(),
					"money" : money,
					"couponGrantId" : couponGrantId
				},
				success : function(data) {
					var ava = data.availableCouponList;
					var nava = data.notAvailableCouponList;
					var box = $(".coupon-pop-box");
					var avabox = box.children(".available");
					var navabox = box.children(".unavailable");
					var avaStr = "";
					var navaStr = "";
					if (ava.length != 0 || nava.length != 0) {
						for (var avai = 0; avai < ava.length; avai++) {
							avaStr += '<li>' + '<div class="coupon ' + (_formatColor(ava[avai].couponType)) + ' ' + (couponGrantId == ava[avai].userCouponId ? "checked" : "") + '" data-type="' + ava[avai].couponType + '" data-val="' + ava[avai].couponQuota + '" data-num="' + ava[avai].userCouponId
									+ '" data-end="' + ava[avai].endTime + '">' + '<div class="left-side">' + '<div class="value">' + (_formatVal(ava[avai].couponType, ava[avai].couponQuota)) + '</div>' + '<div class="txt">' + ava[avai].couponType + '</div>' + '</div>' + '<div class="right-side">'
									+ '<div class="number"><span>编号：</span>' + ava[avai].couponUserCode + '</div>' + '<div class="number"><span>金额范围：</span></div>' + '<div class="area">' + ava[avai].tenderQuota + '</div>' + '<div class="title">' + ava[avai].couponAddTime + ' - '
									+ ava[avai].couponEndTime + ' </div>' + '</div>' + '<div class="cover-default">点击选择</div>' + '<div class="cover-checked">已选中</div>' + '</div>' + '</li>';
						}

						for (var navai = 0; navai < nava.length; navai++) {
							var h = nava[navai];
							navaStr += '<li>' + '<div class="coupon grey">' + '<div class="left-side">' + '<div class="value">' + (_formatVal(nava[navai].couponType, nava[navai].couponQuota)) + '</div>' + '<div class="txt">' + nava[navai].couponType + '</div>' + '</div>'
									+ '<div class="right-side">' + '<div class="number"><span>编号：</span>' + nava[navai].couponUserCode + '</div>' + '<div class="number"><span>金额范围：</span></div>' + '<div class="area">' + nava[navai].tenderQuota + '</div>' + '<div class="title">'
									+ nava[navai].couponAddTime + ' - ' + nava[navai].couponEndTime + ' </div>' + '</div>' + '</div>' + ' <div class="coupon-una-txt">' + nava[navai].remarks + '</div>' + '</li>';
						}
						avabox.html(avaStr);
						navabox.html(navaStr);
					} else {
						avabox.parent().html('<div class="nocoupon">暂无可用优惠券</div>');
					}

					// 根据类型获取样式class
					function _formatColor(type) {
						var color = "";
						if (type == "加息券") {
							color = "yellow";
						} else if (type == "体验金") {
							color = "gold";
						} else if (type == "代金券") {
							color = "gold";
						}
						return color;
					}
					// 根据类型获取值的样式
					function _formatVal(type, val) {
						var str = "";
						if (type == "加息券") {
							str = val + '<span class="unit">%</span>';
						} else if (type == "体验金") {
							str = '<span class="unit">¥</span>' + val;
						} else if (type == "代金券") {
							str = '<span class="unit">¥</span>' + val;
						}
						return str;
					}

				},
				error : function() {
					/*
					 * var ipt = $("#money"); ipt.parent().parent().append("<span
					 * id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
					 */
				}
			});

			popUpWin({
				"ele" : "#couponPop"
			});
		})
$("#couponPop").find(".confirm").click(function() {
	couponStage = $("#couponPop").find(".coupon-pop-box").children("ul.available").clone();// 暂存优惠券选项
	setCouponTxt();
});
$("#couponPop").find(".cancel").click(function() {
	// 取消时重置优惠券选项
	setTimeout(function() {
		$("#couponPop").find(".coupon-pop-box").children("ul.available").html(couponStage.html());
		// setCouponTxt();
	}, 800);
});

$(".coupon-pop-box").children(".available").delegate(".coupon", "mouseenter", function(e) {
	var _self = $(this);
	var hover = _self.children(".cover-default");
	var checked = _self.children(".cover-checked");
	// 未被选中时hove效果
	if (!_self.hasClass("checked")) {
		hover.fadeIn(200);
	}
}).delegate(".coupon", "mouseleave", function() {
	var _self = $(this);
	var hover = _self.children(".cover-default");
	hover.fadeOut(200);
}).delegate(".coupon", "click", function() {
	// 选择优惠券事件
	var _self = $(this);
	var checked = _self.children(".cover-checked");
	var hover = _self.children(".cover-default");
	var radio = _self.children("input:radio");
	if (_self.hasClass("checked")) {
		_self.removeClass("checked");
		radio.prop("checked", false);
		hover.fadeIn(200);
	} else {
		_self.parent("li").parent("ul").children("li").children(".coupon.checked").removeClass("checked");
		_self.parent("li").parent("ul").children("li").children(".coupon.checked").children("inpout:radio").prop("checked", false);
		_self.addClass("checked");
		radio.prop("checked", true);
		hover.fadeOut(200);
	}
})

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
	/* 递增金额验证 */
	var increase = parseFloat($("#increase").val()); // 递增金额
	var total = parseFloat($("#projectData").data("total")); // 可投金额
	var tendermin = parseFloat($("#projectData").data("tendermin"));// 起投金额
	var tendermax = parseFloat($("#projectData").data("tendermax"));// 最大投资金额
//	if ($("#couponType").val() == "3" || $("#couponType").val() == "1") {
//		//代金券和体验金 验证需验证券里的金额
//		value = value+$("#couponValue").val();
//	}
	if (total < tendermin || total < increase) {
		// 若金额出现异常，检查是否是最后一笔交易
		return total == value;
	}else if(value == total || value == tendermin || value == tendermax ){
		//验证投资金额是否等于可投金额 或 起投金额
		return true;
	}
	return (value - tendermin) % increase == 0; // 投资金额对倍增金额求模为0

}, "投资递增金额须为" + $("#increase").val() + "元的整数倍");
var detailForm;
var validate = $("#detailForm").validate({
	"rules" : {
		"money" : {
			"required" : {
				depends : function() {
					// 登录才验证
					if ($("#userData").data("openflag")) {
						if ($("#couponType").val() == "3") {
							return false;
						}
						if ($("#couponType").val() == "1") {
							return false;
						}
						return true;
					}
					// 验证代金券（有代金券无金额可投）
				}
			},
			"number" : true,
			"min" :  getMinMoney(),
			"max" : getMaxMoney(),
			"increase" : {
				depends : function() {
					// 有递增金额才验证
					return $("#increase").val() != "" && !isNaN($("#increase").val()) && $("#money").val() != "";
				}
			}
		},
		"termcheck" : {
			required : true
		}
	},
	"messages" : {
		"money" : {
			"required" : "亲，您还没有填写投资金额",
			"number" : "亲，只能填写数字",
			"min" : "投标金额应" + (isLast() ? "等于" : "大于 ") + getMinMoney() + "元",
			"max" : "投标金额应" + (isLast() ? "等于" : "小于") + getMaxMoney() + "元"
		},
		"termcheck" : "请先阅读并同意汇盈金服投资协议",
		"increase" : "投资递增金额须为" + $("#increase").val() + "元的整数倍"
	},
	"ignore" : ".ignore",
	errorPlacement : function(error, element) {
		error.insertAfter(element.parent());
	},
	submitHandler : function(form) {
		detailForm = form;
		if(tenderCheck(detailForm)){
			setConfirmMessage();
			popUpWin({
				"ele" : "#confirmPop"
			});
		}
	}
});
// 判断是否是最后一笔
function isLast() {
	var res;
	if ($("#isLast").val() == "true") {
		res = true;
	} else {
		res = false;
	}
	return res;
}
$("a.confirm-btn").click(function() {
	if ($(this).hasClass("btn-disabled")) {
		return false;
	} /*else if(!checkTested()){//判断是否测评过
		showTip(null, "为更好保障您的投资利益，<br/>您须在完成风险测评后才可进行投资", "tip", "testPop",{"confirm":"立即测评"});
	}*/else {
		$("#detailForm").submit();
	}
});
//测评弹窗回调
function dealAction(id){
	if(id == "testPop"){ 
		window.location.href = webPath+ "/financialAdvisor/financialAdvisorInit.do";
	}else if(id== "setPwdPop"){
		//未设置交易密码 跳转到设置交易密码
		window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
	}else if(id == 'setLoginPop'){
		//未登录 
		window.location.href = webPath + "/user/login/init.do";
	}else if(id == 'setOpenPop'){
		//未开户
		window.location.href = webPath + "/bank/web/user/bankopen/init.do";
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
function setConfirmMessage() {
	var confirmValue = $("#confirmValue");// 投资金额
	var confirmIncome = $("#confirmIncome");// 历史回报
	var confirmCoupon = $("#confirmCoupon");// 优惠券类型
	var confirmCouponArea = $("#confirmCouponArea");// 优惠券使用范围
	var confirmCouponIncome = $("#confirmCouponIncome");// 优惠券收益
	var coupon = $("#couponType");
	confirmValue.text("投资金额：" + _couponDefaultValue($("#money").val()));
	confirmIncome.text("历史回报：" + _couponDefaultValue(coupon.data("couponinterest")));
	if (coupon.val() != "") {
		confirmCoupon.text(coupon.data("confirmcoupon"));
		confirmCouponArea.text("使用范围：" + coupon.data("confirmcouponarea"));
		confirmCouponIncome.text("历史回报：" + coupon.data("confirmcouponincome"));
	} else {
		confirmCoupon.text("无");
		confirmCouponArea.text("");
		confirmCouponIncome.text("");
	}
}

function _couponDefaultValue(val) {
	return val == "" || typeof val == "undefined" ? "0" : val;
}
$("#confirmedBtn").click(function() {
	var tokenChecked = checkToken()
	if ( tokenChecked == true && tenderCheck(detailForm) == true) {
		setCookie("productDetailToken","1",360);
		detailForm.submit();
	} else if(tokenChecked == false){
		// 错误信息为
		var ipt = $("#money");
		if ($("#ajaxErr").length) {
			$("#ajaxErr").text("请不要重复提交！").show();
		} else {
			ipt.parent().parent().append("<span id='ajaxErr' class='error'>请不要重复提交！</span>");
		}
	}
})
// 全投的时候应该判断，用户余额，可投资金额，最大投资金额
$(".money-btn.available").click(function() {
	var res = getMaxInvestMoney();
	// 投资金额
	$("#money").val(res);
	$("#money").trigger("change");
	$("#money").valid();
	priceChanged($("#money"));
	tenderCheck(detailForm);
})
// 金额变动
$("#money").keyup(function() {
	var _self = $(this);
	var val = _self.val();
	$(".confirm-btn").addClass("btn-disabled"); // 输入结果返回前使投资按钮不可用
	if (!checkNumber(val)) {
		_self.val(val.replace(/[^\d]/g, ''));
	}
	if (timmer) {
		clearTimeout(timmer);
	}
	timmer = setTimeout(function() {
		priceChanged($("#money"));
		$(".confirm-btn").removeClass("btn-disabled");// 输入结果返回后使投资按钮可用
	}, 300);

}).keydown(function() {
	$("#ajaxErr").remove();
});

function checkNumber(val) {
	/* 检测是否是数字 */
	var reg = /^[0-9]*$/;
	if (reg.test(val)) {
		return true;
	}
	return false;
}

// 获取全投金额
function getMaxInvestMoney() {
	// 项目最大投资金额
	var maxMoney = isNaN(parseInt($("#projectData").data("tendermax"))) ? 0 : parseInt($("#projectData").data("tendermax"));
	// 项目可投资金额
	var total = isNaN(parseInt($("#projectData").data("total"))) ? 0 : parseInt($("#projectData").data("total"));
	// 可用余额
	var balance = isNaN(parseInt($("#userData").data("balance"))) ? 0 : parseInt($("#userData").data("balance"));
	//起投金额
	var tendermin = isNaN(parseInt($("#projectData").data("tendermin"))) ? 0 : parseInt($("#projectData").data("tendermin"));
	//递增金额
	var increase = parseFloat($("#increase").val()); // 递增金额
	//是否最后一笔投资
	var islast = isLast();
	var res = 0;
	var balanceAvail = 0;//可用余额递增部分
	
	
	if (balance == 0) {
		window.location.href = webPath + "/recharge/rechargePage.do";
	} else if(balance>=tendermin){
		//可用余额大于起投金额
		balanceAvail = parseInt((balance-tendermin)/increase)*increase+tendermin;
		res = Math.min(balanceAvail,total,maxMoney);
	}else{
		res = tendermin;
	}
	return res;
}

// 获取最大的投资金额
function getMaxMoney() {
	// 项目最大投资金额
	var maxMoney = isNaN(parseInt($("#projectData").data("tendermax"))) ? 0 : parseInt($("#projectData").data("tendermax"));
	// 项目可投资金额
	var total = isNaN(parseInt($("#projectData").data("total"))) ? 0 : parseInt($("#projectData").data("total"));
	// 可用余额
	var res = (total >= maxMoney ? maxMoney : total)
	return res;
}

// 获取最小的投资金额
function getMinMoney() {
	//优惠券为代金券 或 体验金 且未输入值时不需要最小值
	if (($("#couponType").val() == "3" || $("#couponType").val() == "1") && $("#money").val()) {
		return 0;
	}
	// 项目最大投资金额
	var min = isNaN(parseInt($("#projectData").data("tendermin"))) ? 0 : parseInt($("#projectData").data("tendermin"));
	// 项目可投资金额
	var total = isNaN(parseInt($("#projectData").data("total"))) ? 0 : parseInt($("#projectData").data("total"));
	// 项目剩余金额
	var res = (total <= min ? total : min);
	
	return res;
}
// ajax请求获取历史回报
//TODO ajax请求获取历史回报
function priceChanged(ipt) {
	var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
	$(".confirm-btn").addClass("btn-disabled");
	$.ajax({
		type : "POST",
		async : false,
		url : webPath + "/bank/web/user/tender/investInfo.do",
		dataType : 'json',
		data : {
			"nid" : $('#nid').val(),
			"money" : money
		},
		success : function(data) {
			if (data.status == true) {
				$("#income").text(data.earnings);
			} else {
				$("#income").text("0.00");
			}

			if (data.isThereCoupon == "1" && data.earnings != 0) {
				var couponData = data.couponConfig;
				var couponDefaultTxt = $("#useCoupon").prev();
				var couponDefault = $("#useCoupon");
				couponDefaultTxt.html('<span>已选择 <span class="num">' + couponData.couponQuotaStr + (couponData.couponType == 2 ? "%" : "元") + '</span> ' + (couponData.couponType == 2 ? "加息券" : couponData.couponType == 1 ? "体验金" : "代金券") + '</span>'); // 1体验金
																																																															// 2加息券
																																																															// 其他
				couponDefault.html('重新选择');
				$("#couponGrantId").val(couponData.userCouponId);
				$("#couponValue").val(couponData.couponQuotaStr);
				$("#couponType").val(couponData.couponType);
				$("#couponType").data("confirmcoupon", couponData.couponDescribe)
				$("#couponType").data("confirmcouponarea", couponData.tenderQuotaRange);
				$("#couponType").data("confirmcouponincome", couponData.couponInterest);
				$("#couponType").data("couponinterest", data.capitalInterest);

			} else {

				//if (data.ifVip == 1) {
					var couponData = data.couponConfig;
					var couponDefaultTxt = $("#useCoupon").prev();
					var couponDefault = $("#useCoupon");
					couponDefaultTxt.html('<span>您有  <span class="num">' + data.couponAvailableCount + '</span> 张优惠券可用</span>');
					couponDefault.html('选择优惠券');
					$("#couponGrantId").val("");
					$("#couponValue").val("");
					$("#couponType").val("");
					//$("#couponType").data("couponinterest", data.capitalInterest);
//				} else {
//					var couponData = data.couponConfig;
//					var couponDefaultTxt = $("#useCoupon").prev();
//					var couponDefault = $("#useCoupon");
//					couponDefaultTxt.html('<span>您有  <span class="num">' + data.couponAvailableCount + '</span> 张优惠券可用</span>');
//					couponDefault.html('选择优惠券');
//					$("#couponGrantId").val("");
//					$("#couponType").val("");
//					$("#couponType").data("couponinterest", data.capitalInterest);
//				}
				$("#couponType").data("couponInterest", data.capitalInterest);
				$("#couponType").data("couponinterest", data.capitalInterest);//add by cwyang 获得历史回报
				
			}
			$(".confirm-btn").removeClass("btn-disabled");
		},
		error : function() {
			var ipt = $("#money");
			ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
			$(".confirm-btn").removeClass("btn-disabled");
		}
	});
	ipt.valid();
}
// ajax校验投资
function tenderCheck(form) {
	var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
	var couponGrantId = $('#couponGrantId').val();
	var res = false;
	$.ajax({
		type : "POST",
		async : false,
		url : webPath + "/bank/web/user/tender/investCheck.do",
		dataType : 'json',
		data : {
			"nid" : $('#nid').val(),
			"money" : $('#money').val(),
			"couponGrantId" : couponGrantId
		},
		success : function(data) {
			if (data.status == true) {
				res = true;
				// 跳转登陆
			} else if (data.errorCode == "707") {//未登录
				showTip(null, "您尚未登录，请先去登录", "tip", "setLoginPop");
			} else if (data.errorCode == "708") {//未开户
				showTip(null, "您尚未开户，请先去开户", "tip", "setOpenPop");
			} else if(data.errorCode == "709"){//未设置交易密码
				showTip(null, "请先去设置交易密码。", "tip", "setPwdPop");
			} else if(data.errorCode == "710"){//未测评
				showTip(null, "为更好保障您的投资利益，<br/>您须在完成风险测评后才可进行投资", "tip", "testPop");
			} else {
				// 错误信息为
				var ipt = $("#money");
				if ($("#ajaxErr").length) {
					$("#ajaxErr").text(data.message).show();
				} else {
					ipt.parent().parent().append("<span id='ajaxErr' class='error'>" + data.message + "</span>");
				}
			}
		},
		error : function() {
			var ipt = $("#money");
			if ($("#ajaxErr").length) {
				$("#ajaxErr").text("网络异常，请检查您的网络").show();
			} else {
				ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
			}
		}
	});
	return res;
}
// 初始化幻灯
baguetteBox.run('.new-detail-img-c', {
	animation : 'fadeIn'
});

$(document).click(function(e) {
	if ($(e.target).attr("id") == "money-error" || $(e.target).attr("id") == "ajaxErr") {
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
	doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/bank/web/borrow/getBorrowInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
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
	if (projectInvestList.length == 0) {
		projectInvestListStr = projectInvestListStr + '<tr>' + '	<td rowspan="4">未查询到数据</td>' + '</tr>'
	} else {
		var projectInvestListHead = '';
		projectInvestListHead = projectInvestListHead + '	<tr>' + '		<th>用户名</th>' + '		<th width="310">投标金额</th>' + '		<th width="370">投标时间</th>' + '		<th width="210">投标来源</th>' + '	</tr>'
		$("#projectInvestListHead").html(projectInvestListHead);
		for (var i = 0; i < projectInvestList.length; i++) {
			var projectInvest = projectInvestList[i];
			var vipflg = "";
			if (projectInvest.vipId != "0") {
				vipflg = '<img src="' + webPath + '/img/vip/vip_' + projectInvest.vipLevel + '.png" class="vipflg" width="18"/>';
			}
			projectInvestListStr = projectInvestListStr + '<tr>' + '   <td>' + vipflg + ' ' + projectInvest.userName + '</td>' + '   <td><span class="highlight">￥' + projectInvest.account + '</span></td>' + '   <td>' + projectInvest.investTime + '</td>' + '   <td>' + projectInvest.client + '</td>'
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
	doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/bank/web/borrow/getBorrowInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
}
function getProjectConsumeListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	doRequest(projectConsumePageSuccessCallback, projectConsumePageErrorCallback, webPath + "/bank/web/borrow/getBorrowConsume.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "consumeClass", "consume-pagination");
}
/**
 * 获取公司动态信息成功回调
 */
function projectConsumePageSuccessCallback(data) {
	var projectConsumeList = data.projectConsumeList;
	// 挂载数据
	var projectConsumeListStr = "";
	if (projectConsumeList.length == 0) {
		projectConsumeListStr = projectConsumeListStr + '<tr>' + '	<td rowspan="3">未查询到数据</td>' + '</tr>'
	} else {
		var projectConsumeListHead = '';
		projectConsumeListHead = projectConsumeListHead + '	<tr>' + '		<th width="270">姓名</th>' + '		<th>身份证</th>' + '		<th width="430">融资金额</th>' + '</tr>'
		$("#projectConsumeListHead").html(projectConsumeListHead);
		for (var i = 0; i < projectConsumeList.length; i++) {
			var projectConsume = projectConsumeList[i];
			projectConsumeListStr = projectConsumeListStr + '<tr>' + '   <td>' + projectConsume.name + '</td>' + '   <td>' + projectConsume.idCard + '</td>' + '   <td><span class="highlight">￥' + projectConsume.account + '</span></td>' + '</tr>'
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
	doRequest(projectConsumePageSuccessCallback, projectConsumePageErrorCallback, webPath + "/project/getProjectConsume.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "consumeClass", "consume-pagination");
}
// 保存充值页面地址，充值成功调用
function setCookie() {
	$.cookie('beforeUrl', null, {
		expires : 30,
		path : '/',
		domain : 'hyjf.com',
		secure : false
	});
	if ($.cookie("beforeUrl") == null || $.cookie("beforeUrl") == "null") {
		$.cookie('beforeUrl', window.location.href, {
			expires : 30,
			path : '/',
			domain : 'hyjf.com',
			secure : false
		});
	}
}

function setCouponTxt(checked) {
	/*
	 * @param checked 选填，优惠券元素 例如 $(".coupon[data-num=3201554100]")
	 * setCouponTxt($(".coupon[data-num=3201554101]"))
	 */
	if (checked) {
		// 外部调用设置使用优惠券，传checked参数
		checked.click();
	}
	var checked = checked || $("#couponPop").find(".coupon.checked"); // 获取被选中优惠券
	var useTxt = $("#useCoupon");// 打开优惠券link按钮
	var couponLen = $("#couponPop").find(".available").children().length;// 可用优惠券数量
	var couponDefaultTxt = useTxt.prev();// 优惠券文字默认显示
	if (checked.length) {
		var num = checked.data("num");// 优惠券id
		var val = checked.data("val");
		$('#couponGrantId').val(num);
		$('#couponValue').val(val);
	} else {
		$('#couponGrantId').val(-1);
		$('#couponValue').val("");
	}

	if (timmer) {
		clearTimeout(timmer);
	}
	timmer = setTimeout(function() {
		changCouponed($("#money"));
	}, 300);
}

// ajax请求改变使用优惠券时获取历史回报
function changCouponed(ipt) {
	var money = isNaN(parseInt($('#money').val())) ? 0 : parseInt($('#money').val());
	var couponGrantId = $('#couponGrantId').val();
	$.ajax({
		type : "POST",
		async : false,
		url : webPath + "/bank/web/user/tender/investInfo.do",
		dataType : 'json',
		data : {
			"nid" : $('#nid').val(),
			"money" : money,
			"couponGrantId" : couponGrantId
		},
		success : function(data) {
			if (data.status == true) {
				$("#income").text(data.earnings);
			} else {
				$("#income").text("0.00");
			}

			if (data.isThereCoupon == "1" && data.earnings != 0) {
				var couponData = data.couponConfig;
				var couponDefaultTxt = $("#useCoupon").prev();
				var couponDefault = $("#useCoupon");
				couponDefaultTxt.html('<span>已选择 <span class="num">' + couponData.couponQuotaStr + (couponData.couponType == 2 ? "%" : "元") + '</span> ' + (couponData.couponType == 2 ? "加息券" : couponData.couponType == 1 ? "体验金" : "代金券") + '</span>');
				couponDefault.html('重新选择');
				$("#couponGrantId").val(couponData.userCouponId);
				$("#couponvalue").val(couponData.couponQuotaStr);
				$("#couponType").val(couponData.couponType);
				$("#couponType").data("confirmcoupon", couponData.couponDescribe)
				$("#couponType").data("confirmcouponarea", couponData.tenderQuotaRange);
				$("#couponType").data("confirmcouponincome", couponData.couponInterest);
				$("#couponType").data("couponinterest", data.capitalInterest);
			} else {
				var couponData = data.couponConfig;
				var couponDefaultTxt = $("#useCoupon").prev();
				var couponDefault = $("#useCoupon");
				couponDefaultTxt.html('<span>您有  <span class="num">' + data.couponAvailableCount + '</span> 张优惠券可用</span>');
				couponDefault.html('选择优惠券');
				$("#couponGrantId").val("");
				$("#couponvalue").val("");
				$("#couponType").val("");
				$("#couponType").data("couponInterest", data.capitalInterest);
			}
		},
		error : function() {
			var ipt = $("#money");
			ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
		}
	});
	ipt.valid();
}

// 设置列表内默认被选优惠券
function setCouponListCheck(id) {
	$(".coupon-pop-box").find(".coupon[data-num=" + id + "]").addClass("checked");
}

function openVIP() {
	window.location.href = webPath + "/vip/apply/init.do";
}
// vip购买弹出层
function setCookieVip(c_name, value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays)
	document.cookie = c_name + "=" + escape(value) + ";path=/" + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}
$(".vip-buy-close").click(function() {
	$(".vip-buy-pop-div").animate({
		top : "-62px"
	}, 600).animate({
		backgroundSize : "42%"
	}, 400, function() {
		$(".vip-buy-pop").fadeOut(800);
		$("html").css("overflow", "auto");
	})

})

$(".appoint-term .checkicon").click(function() {
	if ($(this).hasClass("avaliable")) {
		if (!$(".appoint-term .checkicon").hasClass("checked")) {
			$(this).addClass("checked");
			$(this).children("input").prop("checked", true).valid();
		} else {
			$(this).removeClass("checked");
			$(this).children("input").prop("checked", false).valid();
		}
	}
})

function openNew(url) {
	window.open(url);
}
//同步按钮
$(".syncAccount").click(function(){
	var _self = $(this);
	_self.addClass("syncing");
	$.ajax({
		type:"post",
		url : webPath + "/bank/user/synbalance/init.do"
	})
	.done(function(res){
		if(res.status == true){
			$("#bankBalance").html(res.info);
		}else{
			showTip(null, res.message, "tip", "syncPop");
		}
		_self.removeClass("syncing");
	})
	.fail(function(){
		_self.removeClass("syncing");
	})
})

$(function(){
	//token过期刷新
	var rechargeToken = getCookie("productDetailToken");
	if(rechargeToken != ""){
		setCookie("productDetailToken","");
		window.location.href = window.location.href;
	}
	
})