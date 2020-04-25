$(document).ready(function() {
	$(".drag").drag();
	// 引导页 翻页
	var findNext = function() {
		var $findNext = $(".findNext");
		var next = function() {
			var _this = $(this);
			var $thisForm = _this.parents(".findForm");
			var $thisDiv = _this.parents(".newRegContent ");
			var $findPos = $(".findPos");
			var $topIndex = $thisForm.index();
			var $newRegInput = $(".newRegInput");
			var $phoneLength = $newRegInput.val().length;// 手机号长度
			var $dragBgWidth = $(".drag_bg").width();// drag宽度
			var $dragInput = $("#dragInput") // drag input 验证
			var $newRegInputLength = $("#newRegVerify").val().length;//
			var $setPwd = $(".findSetPwd").val();
			var $confirmPwd = $(".findConfirmPwd").val();
			if ($topIndex === 2) {
				if ($phoneLength == 11 && ($dragBgWidth == null || $dragInput == null || ($dragInput.prop("checked") == true && $dragBgWidth > 200))) {
					// var params = {};
					// params.username =
					// $("#username").val();
					// params.password =
					// $("#password").val();
					// params.captcha = $("#captcha").val();
					// alert($("#form1").serialize());
					var successCallback = function(data) {
						// 下一步
						findShow();
					};
					var errorCallback = function(data) {
						showTip("提示", data.error, "tip", "tempid");
						{
							$("#divdrag").html("<div id=\"drag\" class=\"drag\"></div><input type=\"checkbox\" id=\"dragInput\" value=\"\" name=\"dragInput\" />");
							$(".drag").drag();
						}
					};
					doRequest(successCallback, errorCallback, webPath + "/user/findpassword/form1Submit.do", $("#pwForm").serialize());
				}
			}
			if ($topIndex === 3) {
				if ($newRegInputLength == 6) {
					var successCallback = function(data) {
						// 下一步
						findShow();
					};
					var errorCallback = function(data) {
						showTip("提示", data.error, "tip", "tempid");
					};
					var params = {};
					params.newRegPhoneNum = $("#newRegPhoneNum").val();
					params.newRegVerify = $("#newRegVerify").val();
					params.validCodeType = $("#validCodeType").val();
					doRequest(successCallback, errorCallback, webPath + "/user/findpassword/form2Submit.do", params);
				}
			}
			if ($topIndex === 4) {
				if (($setPwd == $confirmPwd) && $setPwd.length >= 6 && $setPwd.length <= 16 && $confirmPwd.length >= 6 && $confirmPwd.length <= 16) {
					var successCallback = function(data) {
						// 下一步
						findShow();
					};
					var errorCallback = function(data) {
						showTip("提示", data.error, "tip", "tempid");
					};
					var params = {};
					params.password1 = $("#password1").val();
					params.password2 = $("#password2").val();
					params.newRegPhoneNum = $("#newRegPhoneNum").val();
					params.newRegVerify = $("#newRegVerify").val();
					doRequest(successCallback, errorCallback, webPath + "/user/findpassword/form3Submit.do", params);
				}
			}
			// 隐藏 显示 下一步 当前位置显示
			function findShow() {
				$findPos.eq($topIndex - 1).addClass("askCurPos").siblings().removeClass("askCurPos")
				$thisDiv.hide(500);
				$thisForm.next().find(".newRegContent ").show(500);
				var $phoneNum = $newRegInput.val();
				$(".phoneNum").text($phoneNum);
			}
		}
		// 点击返回修改
		$findNext.on("click", next);
		$(".findChangeNum").on("click", function() {
			$(this).parents(".newRegContent").hide(500);
			$(this).parents(".findForm").prev().find(".newRegContent").show(500);
			$(".findPos").eq(0).addClass("askCurPos").siblings().removeClass("askCurPos");
			{
				$("#divdrag").html("<div id=\"drag\" class=\"drag\"></div><input type=\"checkbox\" id=\"dragInput\" value=\"\" name=\"dragInput\" />");
				$(".drag").drag();
			}
		})
	}
	findNext();
	$.validator.setDefaults({
		debug : true
	})
	$("#btnGetVerify").click(function() {
		
		var params = {};
		var _self = $(this);
		params.newRegVerifyCode = $("#newRegVerifyCode").val();
		params.newRegPhoneNum = $("#newRegPhoneNum").val();
		params.validCodeType = $("#validCodeType").val();
		var successCallback = function(data) {
			// alert("登录成功");
			// 获取验证码提示
			var clock = '';
			var nums = 60;
			var btn = _self;
			// btn.disabled = true; //将按钮置为不可点击
			btn.attr("disabled", true);
			btn.text(nums + '秒后重新获取');
			btn.css({
				"color" : "#d6d6d6",
				"border-color" : "#d6d6d6",
				"cursor" : "not-allowed"
			})
			clock = setInterval(doLoop, 1000);
			// showTip("提示", "发送验证码成功", "tip","tempid");
			function doLoop() {
				nums--;
				if (nums > 0) {
					btn.text(nums + '秒后可重新获取');
				} else {
					clearInterval(clock); // 清除js定时器
					btn.attr("disabled", false);
					btn.text('点击发送验证码');
					nums = 60; // 重置时间
					btn.css({
						"color" : "#cda972",
						"border-color" : "#cda972",
						"cursor" : "pointer"
					})
				}
			}
		};
		var errorCallback = function(data) {
			// showTip("提示", data.error, "tip","tempid");
			var errorSpan = '<div class="error">' + data.error + '</div>';
			$("div.error").remove();
			$(".regIntDivlast .poR").after(errorSpan)
		}
		if($("#newRegVerifyCode").valid() && !_self.hasClass("phoneCodeAAA")){
			doRequest(successCallback, errorCallback, webPath + "/user/regist/sendcode.do", params);
		}
	});
	// 在键盘按下并释放及提交后验证提交表单
	$(".findForm-1").validate({
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
//		onkeyup : false,
		rules : {
			newRegPhoneNum : {
				required : true,
				isMobile : true,
			/*
			 * remote : { url : webPath +
			 * "/user/findpassword/checkUserAction.do", type : "get", data : {
			 * newRegPhoneNum : function() { return $( "#newRegPhoneNum")
			 * .val(); } }, beforeSend : function() { // var _checking = //
			 * $("#checking"); // _checking.prev(".field_notice").hide(); //
			 * _checking.next("label").hide(); // $(_checking).show(); },
			 * complete : function() { // $("#checking").hide(); } }
			 */
			},
			dragInput : "required"
		},
		messages : {
			newRegPhoneNum : {
				required : "请填写手机号",
			/* minlength : "手机号不能小于11位", */
			/*
			 * number : "请输入数字", remote : "无效的手机号"
			 */
			},
			dragInput : "滑动滑块解锁"
		}
	});
	$(".findForm-2").validate({
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
//		onkeyup : false,
		rules : {
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
				remote : {
					url : webPath + "/user/regist/checkcode.do",
					type : "get",
					data : {
						newRegPhoneNum : function() {
							return $("#newRegPhoneNum").val();
						},
						newRegVerify : function() {
							return $("#newRegVerify").val();
						},
						validCodeType : function() {
							return $("#validCodeType").val();
						}
					},
					beforeSend : function() {
						// var _checking =
						// $("#checking");
						// _checking.prev(".field_notice").hide();
						// _checking.next("label").hide();
						// $(_checking).show();
					},
					complete : function() {
						// $("#checking").hide();
					}
				}
			}
		},
		messages : {
			newRegVerifyCode : {
				required : "请填写验证码",
				remote : "验证码不正确"
			},
			newRegVerify : {
				required : "请填写短信校验码",
				minlength : "验证码不能小于 6个字母",
				remote : "短信校验码不正确"
			},
		}
	});
	$(".findForm-3").validate({
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
//		onkeyup : false,
		rules : {
			password1 : {
				required : true,
				ispwd : true
			},
			password2 : {
				required : true,
				equalTo : "#password1",
				ispwd : true
			}
		},
		messages : {
			password1 : {
				required : "请填写密码"
			},
			password2 : {
				required : "请填写确认密码",
				equalTo : "两次密码输入不一致"
			}
		}
	});

	// 手机号正确后验证码才能显示正常
	$("#newRegVerifyCode").on("keyup", function() {
		setTimeout(function(){
			if ($("#newRegVerifyCode").valid()) {
				$("#btnGetVerify").removeClass("phoneCodeAAA").prop("disabled",false);
			} else {
				$("#btnGetVerify").addClass("phoneCodeAAA").prop("disabled",true);
			}
		},300);
	})
	changeImg();
});

function changeImg() {
	var imgSrc = $("#imgObj");
	imgSrc.attr("src", chgUrl(webPath + "/user/regist/getcaptcha.do"));
}
// 时间戳
// 为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
function chgUrl(url) {
	var timestamp = (new Date()).valueOf();
	url = url + "?timestamp=" + timestamp;
	return url;
}
