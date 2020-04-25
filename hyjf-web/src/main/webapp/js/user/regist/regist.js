var nums = 60;
$(document).ready(function() {
	$(".drag").drag();
	// 注册协议勾选显示隐藏
	$(".terms-check").click(function(e) {
		var _self = $(e.target), checkbox;
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
			$("#regForm").validate().element(checkbox);
		}
	})
	//获取验证码
	$(".newRegGetVer").click(function() {
		_self = $(this)
		var successCallback = function(data) {
			// alert("登录成功");
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
					clearInterval(clock); // 清除js定时器
					btn.removeClass("phoneCodeAAA");
					btn.val('点击发送验证码');
					nums = 60; // 重置时间
				}
			}
			

		};
		var errorCallback = function(data) {
			showTip("提示", data.error, "tip","verError");
		}
		var verCodeSuccessCallback = function() {
			// 发送手机验证码
			doRequest(successCallback, errorCallback, webPath + "/user/regist/sendcode.do", $('#regForm').serialize());
		}
		if(!_self.hasClass("phoneCodeAAA")){
			// 验证图形验证码
			_self.addClass("phoneCodeAAA");
			doRequest(verCodeSuccessCallback, errorCallback, webPath + "/user/regist/checkcaptchajson.do", $('#regForm').serialize());
		}
	});
	var ajaxSubmitForm = function() {
		var successCallback = function(data) {
			clearForm("regForm");
			var sendCount = data.couponSendCount;
			if(sendCount > 0){
				location.href = webPath + "/user/regist/to_regist_success_coupon.do?userid=" + data.userid+"&activity68="+ data.activity68;
			}else{
				location.href = webPath + "/user/regist/to_regist_success.do?userid=" + data.userid+"&activity68="+ data.activity68;
			}
		};
		var errorCallback = function(data) {
			showTip("提示", data.error, "tip","regError");
			{
				$("#divdrag").html("<div id=\"drag\" class=\"drag\"></div><input type=\"checkbox\" id=\"dragInput\" value=\"\" name=\"dragInput\" />");
				$(".drag").drag();
			}
		};
		//注册
		doRequest(successCallback, errorCallback, webPath + "/user/regist/regist.do", $('#regForm').serialize());
		// var params = {};
		// params.username = $("#username").val();
		// params.password = $("#password").val();
		// params.captcha = $("#captcha").val();
		// alert($('#form1').serialize());
	}
	// 在键盘按下并释放及提交后验证提交表单
	$("#regForm").validate({
		submitHandler : function() {
			ajaxSubmitForm();
		},// 这是关键的语句，配置这个参数后表单不会自动提交，验证通过之后会去调用的方法
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
//		onkeyup : false,
		rules : {
			newRegAgree : "required",
			dragInput : "required",
			newRegPhoneNum : {
				required : true,
				number : true,
				minlength : 11,
				isMobile : true,
				maxlength : 11,
				remote : {
					url : webPath + "/user/regist/checkaction.do",
					type : "post",
					data : {
						name : function() {
							return "userName";
						},
						param : function() {
							return $('#newRegPhoneNum').val();
						}
					},
					beforeSend : function() {
						// var _checking =
						// $('#checking');
						// _checking.prev('.field_notice').hide();
						// _checking.next('label').hide();
						// $(_checking).show();
					},
					complete : function() {
						// $('#checking').hide();
					}
				}
			},
			newRegPsw : {
				required : true,
				ispwd : true,
			},
			// 图形验证码
			newRegVerifyCode : {
				required : true,
				remote : {
					url : webPath + "/user/regist/checkcaptcha.do",
					type : "post",
					data : {
						newRegVerifyCode : function() {
							return $("#newRegVerifyCode").val();
						}
					}
				}
			},
			newRegVerify : {
				required : true,
				maxlength : 6,
				minlength : 6,
				remote : {
					url : webPath + "/user/regist/checkcode.do",
					type : "post",
					data : {
						newRegVerifyCode : function() {
							return $("#newRegVerifyCode").val();
						},
						newRegPhoneNum : function() {
							return $("#newRegPhoneNum").val();
						},
						newRegVerify : function() {
							return $('#newRegVerify').val();
						},
						validCodeType : function() {
							return $('#validCodeType').val();
						}
					},
					beforeSend : function() {
						// var _checking =
						// $('#checking');
						// _checking.prev('.field_notice').hide();
						// _checking.next('label').hide();
						// $(_checking).show();
					},
					complete : function() {
						// $('#checking').hide();
					}
				}
			},
			newRegReferree : {
				required : false,
				maxlength : 11,
				remote : {
					url : webPath + "/user/regist/checkRecommend.do",
					type : "post",
					data : {
						newRegReferree : function() {
							return $('#newRegReferree').val();
						}
					},
					beforeSend : function() {
						// var _checking =
						// $('#checking');
						// _checking.prev('.field_notice').hide();
						// _checking.next('label').hide();
						// $(_checking).show();
					},
					complete : function() {
						// $('#checking').hide();
					}
				}
			},
			topic : {
				required : "#newsletter:checked",
				minlength : 2
			},
			agree : "required"
		},
		ignore : ".ignore",
		messages : {
			newRegPhoneNum : {
				required : "请填写手机号",
				minlength : "请填写您的真实手机号码",
				number : "请填写您的真实手机号码",
				remote : "手机号已存在"
			},
			newRegPsw : {
				required : "请设置登录密码",
				minlength : "密码长度6-16位"
			},
			newRegVerifyCode : {
				required : "请填写验证码",
				remote : "验证码不正确"
			},
			newRegVerify : {
				required : "请填写短信校验码",
				minlength : "验证码不能小于 6个字母",
				remote : "短信校验码不正确"
			},
			newRegReferree : {
				remote : "推荐人错误，请重新填写"
			},
			low : {
				required : "请勾选同意"
			},
			newRegAgree : "需同意协议",
			dragInput : "滑动滑块解锁"
		}
	});
	// 判断手机号是否正确
	function testPhoneNum(value) {
		var length = value.length;
		return (length == 11 && /^(((13[0-9]{1})|(14[5,7,9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test(value));
	}
	;

	// 手机号正确后验证码才能显示正常
	$("#newRegPhoneNum,#newRegVerifyCode,#newRegPsw").on("keyup", function() {
		setTimeout(function(){
			if ($("#newRegPhoneNum").valid() && $("#newRegVerifyCode").valid() && $("#newRegPsw").valid() && nums == 60) {
				$(".newRegGetVer").removeClass("phoneCodeAAA");
			} else {
				$(".newRegGetVer").addClass("phoneCodeAAA");
			}
		},300);
	});
	//获取图形验证码
	changeImg();
});

$("#newRegReferree,#newRegVerify").keyup(function() {
	var _self = $(this);
	var val = _self.val();
	if (!checkOnlyNumber(val)) {
		_self.val(val.replace(/[^\d]/g, ''));
	}
});
function checkOnlyNumber(val) {
	/* 检测是否是数字 */
	var reg = /^[0-9]*$/;
	if (reg.test(val)) {
		return true;
	}
	return false;
}

function openNew(url) {
	window.open(url);
}

function changeImg() {
	var imgSrc = $("#imgObj");
	imgSrc.attr("src", chgUrl(webPath + "/user/regist/getcaptcha.do")); //:8080/hyjf-web
}
// 时间戳
// 为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
function chgUrl(url) {
	var timestamp = (new Date()).valueOf();
	url = url + "?timestamp=" + timestamp;
	return url;
}