$(function() {
	var wxphone = $(".wxPhone");
	var wxcode = $(".wxCode");
	var wxInvestInput = $(".wxInvestInput");
	var newRegReg = $(".newRegReg");
	var getcode = $('.getCode');
	
	if(wxInvestInput.val().length > 0 && wxphone.val().length == 11){
		getcode.addClass('getcodeGan').removeAttr("disabled"); //页面加载时带有充值金额和手机号时，验证码可点击
	}
	//充值金额未输入时，聚焦手机号输入框时提示输入充值金额
	wxphone.focus(function() {
		var wxInvestInput = $(".wxInvestInput");
		var wxInvestLength = wxInvestInput.val().length;
		if(wxInvestLength == 0) {
			$.mvalidateTip('请输入充值金额')
			wxInvestInput.focus();
		}
	});
	//充值金额和手机号未输入时，聚焦验证码输入框时提示输入充值金额和手机号
	wxcode.focus(function() {
		var wxInvestInput = $(".wxInvestInput"),wxphone = $(".wxPhone");
		var wxInvestLength = wxInvestInput.val().length;
		var wxphoneLength = wxphone.val().length;
		if(wxInvestLength == 0) {
			$.mvalidateTip('请输入充值金额')
			wxInvestInput.focus();
		} else if(wxphoneLength < 11) {
			$.mvalidateTip('请输入11位手机号')
			wxphone.focus();
		}
	});
	//实时监测充值金额
	wxInvestInput.bind('input propertychange', function() {
		if($(this).val().length > 0 && wxphone.val().length == 11) {
			getcode.addClass('getcodeGan').removeAttr("disabled"); //充值金额和手机号位数都校验成功，获得邀请码可用
			if(wxcode.val().length == 6) { //充值金额、手机号和验证码位数校验成功，提交按钮可用
				newRegReg.removeClass("invest-invaild").removeAttr("disabled");
			} else {
				newRegReg.addClass("invest-invaild").prop("disabled", "disabled");
			}
		} else {
			getcode.removeClass('getcodeGan').prop("disabled", "disabled");
			newRegReg.addClass("invest-invaild").prop("disabled", "disabled");
		}
	});
	//实时监测手机号
	wxphone.bind('input propertychange', function() {
		if($(this).val().length == 11) {
			getcode.addClass('getcodeGan').removeAttr("disabled"); //充值金额和手机号位数都校验成功，获得邀请码可用
			if(wxcode.val().length == 6) { //充值金额、手机号和验证码位数校验成功，提交按钮可用
				newRegReg.removeClass("invest-invaild").removeAttr("disabled");
			} else {
				newRegReg.addClass("invest-invaild").prop("disabled", "disabled");
			}
		} else {
			getcode.removeClass('getcodeGan').prop("disabled", "disabled");
			newRegReg.addClass("invest-invaild").prop("disabled", "disabled");
		}
	});
	//实时监测邀请码
	wxcode.bind('input propertychange', function() {
		if($(this).val().length == 6) { //邀请码校验成功，提交按钮可用
			newRegReg.removeClass("invest-invaild").removeAttr("disabled");
		} else {
			newRegReg.addClass("invest-invaild").prop("disabled", "disabled");
		}
	});
})