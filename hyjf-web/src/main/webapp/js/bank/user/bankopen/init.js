var nums = 60;
$(document).ready(function() {
	// 注册协议勾选显示隐藏
	$(".terms-check").click(function(e) {
		var _self = $(e.target), checkbox;
		if(_self.hasClass("error")){return false;}
		if (_self.hasClass("checkicon")) {
			checkbox = _self.siblings("input[type=checkbox]");
			_self = _self.parent();
		} else {
			checkbox = _self.find("input[type=checkbox]");
		}
		if (!_self.hasClass("terms")) {
			if (_self.hasClass("checked")) {
				_self.removeClass("checked");
				checkbox.prop("checked", false);
			} else {
				_self.addClass("checked");
				checkbox.prop("checked", true);
			}
			$("#bankOpenForm").validate().element(checkbox);
		}
	})
	//获取验证码
	var smsStat = true;
	$("#sendCode").click(function() {
		var _self = $(this);
		if(smsStat === false){
			return false;
		}
		var successCallback = function(data) {
			// 获取验证码提示
			var clock = '';
			var btn = _self;
			btn.val(nums + '秒后重新获取');
			clock = setInterval(doLoop, 1000);
			// showTip("提示", "发送验证码成功", "success");
			function doLoop() {
				nums--;
				if (nums > 0) {
					btn.val(nums + '秒后可重新获取');
				} else {
					smsStat = true;
					clearInterval(clock); // 清除js定时器
					btn.removeClass("phoneCodeAAA");
					btn.val('点击发送验证码');
					nums = 60; // 重置时间
				}
			}
		};
		var errorCallback = function(data) {
			smsStat = true;
			showTip("提示", data.message, "tip","verError");
			
		};
		var verCodeSuccessCallback = function() {
			// 发送手机验证码
			doRequest(successCallback, errorCallback, webPath + "/bank/web/user/bankopen/sendCode.do", $('#bankOpenForm').serialize());
		};
		verCodeSuccessCallback();
	});
	var validate = $("#bankOpenForm").validate({
		errorElement : "div",
		"rules": {
	        "trueName": {
	            "required":true
	        },
	        "idNo": {
	            "required":true,
	            "minlength":15,
	            "maxlength":20,
	            "isIdCardNo":true
	        },
	        "cardNo": {
	            "required":true,
	            "minlength":11,
	            "maxlength":19,
	            "isBankCard":true
	        },
	        "mobile":{
	        	"required":true,
				"number" : true,
				"minlength" : 11,
				"isMobile" : true,
				"maxlength" : 11,
				"remote" : {
					"url" : webPath + "/bank/web/user/bankopen/mobileCheck.do",
					"type" : "post",
					"data" : {
						"param" : function() {
							return $('#mobile').val();
						}
					},
					"beforeSend" : function() {
					},
					"complete" : function() {
					}
				}
	        },
	        "smsCode": {
	            "required":true,
	            "minlength":6,
	            "maxlength":6,
	        },
	        "bankLow":"required"
	    },
	    "messages": {
	        "trueName": {
	            "required":"请填写真实姓名"
	        },
	        "idNo": {
	            "required":"请填写身份证号码",
	            "minlength":"请输入正确的身份证号码",
	            "maxlength":"请输入正确的身份证号码",
	            "isIdCardNo":"请输入正确的身份证号码"
	        },
	        "cardNo": {
	            "required":"请填写银行卡号",
	            "minlength":"请填写正确的银行卡号码",
	            "maxlength":"请填写正确的银行卡号码",
	            "isBankCard":"请填写正确的银行卡号码"
	        },
	        "mobile":{
	        	"required":"请填写手机号",
				"number" : "请填写您的真实手机号码",
				"minlength" : "请填写您的真实手机号码",
				"isMobile" : "请填写您的真实手机号码",
				"maxlength" : "请填写您的真实手机号码",
				"remote" : "手机号已存在"
			},
	        "smsCode": {
	            "required":"请填写短信验证码",
	            "minlength":"短信校验码不正确",
	            "maxlength":"短信校验码不正确",
	            "remote" : "短信校验码不正确"
	        },
	        "bankLow":"请阅读并同意开户协议"
	    },
	    "ignore": ".ignore",
	    "onfocusout": false,
		"onclick" : false,
	    submitHandler: function(form) {
	    	ajaxSubmitForm();
	    }
	    
	});
	function ajaxSubmitForm() {
		var successCallback = function(data) {
			clearForm("bankOpenForm");
			location.href = webPath +"/bank/user/transpassword/setPassword.do";
		};
		var errorCallback = function(data) {
			showTip("提示", data.message, "tip","regError");
		};
		doRequest(successCallback, errorCallback, webPath + "/bank/web/user/bankopen/open.do", $('#bankOpenForm').serialize());
	}
});
