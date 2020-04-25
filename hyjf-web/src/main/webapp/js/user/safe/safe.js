$(document).ready(function() {
	
	$("#bindCancle").click(function(){
		$("#inUserName").val("");
		$("#viewname").text("");
		$("#viewmobile").text("");
	});
	
	$(".safeban-tab").click(function(e) {
		var _self = $(e.target);
		if (_self.is("li")) {
			var idx = _self.attr("panel");
			var panel = $(".safeban-tab-list ul");
			_self.siblings("li.active").removeClass("active");
			_self.addClass("active");
			panel.children("li.active").removeClass("active");
			panel.children("li[panel=" + idx + "]").addClass("active");
		}
	})
	// 修改显示 隐藏
	var securityShow = function() {
		var _self = $(this);
		var parent = _self.parent().parent();
		var next = parent.next();
		_self.hide();
		next.show();
	};
	var securityShow2 = function() {
		var _self = $(this);
		var parent = _self.parent();
		var next = parent.next();
		_self.hide();
		next.show();
	};
	var cancleShow = function() {
		var _self = $(this);
		var parent = _self.parents(".Security-con");
		parent.hide();
		parent.prev().find("i").show();
		parent.prev().prev().find("i").show();
	};
	var safeCheck = function() {
		var selfInput = $(this);
		// var selfInput = _self.find("input");
		var check = selfInput.hasClass("safechecked");
		if (check == false) {
			selfInput.addClass("safechecked");
		} else {
			selfInput.removeClass("safechecked");
		}
	};
	// 修改密码
	$("#modpass").validate({
		submitHandler : function() {
			// parent.hide();
			// parent.next().show();
			// parent.prev().find(".security-change").show();
			// 修改密码
			var successCallback = function(data) {
				showTip("提示", "修改密码成功", "tip", "updatePassword");
			};
			var errorCallback = function(data) {
				showTip("提示", data.error, "tip", "tempid");
				// var _self = $(this);
				// _self.parent().hide().prev().show().prev().show();
			}
			doRequest(successCallback, errorCallback, webPath + "/user/safe/updatePassword.do", $('#modpass').serialize());
		},// 这是关键的语句，配置这个参数后表单不会自动提交，验证通过之后会去调用的方法
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
		onkeyup : false,
		rules : {
			oldpass : {
				required : true,
				minlength : 6,
				maxlength : 16,
				remote : {
					url : webPath + "/user/safe/checkParam.do",
					contentType : "application/json; charset=utf-8",
					type : "get",
					data : {
						name : "oldpass",
						oldpass : function() {
							return $('#oldpass').val();
						}
					}
				}
			},
			password : {
				required : true,
				minlength : 6,
				maxlength : 16,
				remote : {
					url : webPath + "/user/safe/checkParam.do",
					contentType : "application/json; charset=utf-8",
					type : "get",
					data : {
						name : "password",
						password : function() {
							return $('#password').val();
						}
					}
				}
			},
			repassword : {
				required : true,
				minlength : 6,
				maxlength : 16,
				equalTo : "#password",
				remote : {
					url : webPath + "/user/safe/checkParam.do",
					contentType : "application/json; charset=utf-8",
					type : "get",
					data : {
						name : "repassword",
						password : function() {
							return $('#password').val();
						},
						repassword : function() {
							return $('#repassword').val();
						}
					}
				}
			}
		},
		messages : {
			oldpass : {
				required : "请填写原密码",
minlength : "登录密码(6-16位数字和字母组合,首位是字母)",
				maxlength : "登录密码(6-16位数字和字母组合,首位是字母)",				remote : "原密码不正确"
			},
			password : {
				required : "请填写新密码",
				minlength : "输入新登录密码(6-16位数字和字母组合,首位是字母)",
				maxlength : "输入新登录密码(6-16位数字和字母组合,首位是字母)",
				remote : "输入新登录密码(6-16位数字和字母组合,首位是字母)"			},
			repassword : {
				required : "请填写确认密码",
				minlength : "输入新登录密码(6-16位数字和字母组合,首位是字母)",
				maxlength : "输入新登录密码(6-16位数字和字母组合,首位是字母)",				equalTo : "两次密码输入不一致",
				remote : "输入新登录密码(6-16位数字和字母组合,首位是字母)"			},
		}
	});
	var reSend = function() {
		var _self = $(this);
		var parent = _self.parents(".Security-con");
		parent.hide();
		parent.prev().show()
	}
	$(".security-change").on("click", securityShow);
	$(".security-change2").on("click", securityShow2);
	$(".security-cancle").on("click", cancleShow);
	// cheeck show
	$(".input-style").on("click", safeCheck);
	// 重新发送邮件
	$(".re-send").on("click", reSend);
	customForm.init();// 初始化
	// 修改用户名
	var openChangeNickname = function() {
		var _self = $(this);
		_self.hide().prev().hide().end().next().show();
	}
	// 修改昵称点击确定
	var changeNicknameSubmit = function() {
		var successCallback = function(data) {
			showTip("提示", "修改昵称成功", "tip", "updatenickname");
		};
		var errorCallback = function(data) {
			showTip("提示", data.error, "tip", "tempid");
			// var _self = $(this);
			// _self.parent().hide().prev().show().prev().show();
		}
		doRequest(successCallback, errorCallback, webPath + "/user/safe/updatenickname.do", $('#nicknameForm').serialize());
	}
	// 修改昵称点击取消
	var changeNicknameCancle = function() {
		var _self = $(this);
		_self.parent().hide().prev().show().prev().show();
	}
	// 打开修改昵称窗口
	$(".safe-resign").on("click", openChangeNickname);
	// 修改昵称点击确定
	$(".safe-changenickname-submit").on("click", changeNicknameSubmit);
	// 修改昵称点击取消
	$(".safe-changenickname-cancle").on("click", changeNicknameCancle);
	// 前端验证
	$().ready(function() {
		$.validator.setDefaults({
			debug : true
		})
		// 在键盘按下并释放及提交后验证提交表单
		$(".safePwd").validate({
			errorElement : "span",
			rules : {
				oldpass : {
					required : true,
					minlength : 6//,
//					remote : {
//						type : "POST",
//						url : "<{eval url('site/ajaxmobile')}>",
//						contentType : "application/json; charset=utf-8",
//						data : {
//							password : function() {
//								return $("#oldpass").val();
//							}
//						}
//					}
				},
				password : {
					required : true,
					ispwd : true//,
//					remote : {
//						type : "POST",
//						url : "<{eval url('site/ajaxmobile')}>",
//						contentType : "application/json; charset=utf-8",
//						data : {
//							password : function() {
//								return $("#oldpass").val();
//							}
//						}
//					}
				},
				repassword : {
					required : true,
					ispwd : true,
					equalTo : "#repassword"
				}
			},
			messages : {
				oldpass : {
					required : "请输入原密码",
//					remote : "密码错误",
					minlength : "用户名必需由两个字母组成"
				},
				password : {
					required : "请输入密码",
					number : "请输入数字",
//					remote : "新密码不能与原密码一样!"
				},
				repassword : {
					required : "请再次输入密码",
					equalTo : "两次密码输入不一致"
				}
			}
		});
		// email
		$("#bindemail").validate({
			errorElement : "div",
			rules : {
				email : {
					required : true,
					email : true//,
//					remote : {
//						type : "POST",
//						url : "<{eval url('site/ajaxmobile')}>",
//						data : {
//							password : function() {
//								return $("#email").val();
//							}
//						}
//					}
				}
			},
			messages : {
				email : {
					required : "输入绑定的邮箱地址，提高安全等级",
					email : "请输入有效的电子邮件地址"//,
//					remote : "邮箱已被占用"
				}
			}
		});
	});
	// 紧急联系人
	$("#emergencyContactForm").validate({
		submitHandler : function() {
			// parent.hide();
			// parent.next().show();
			// parent.prev().find(".security-change").show();
			// 修改密码
			var successCallback = function(data) {
				showTip("提示", "修改紧急联系人成功", "tip", "updateRelation");
			};
			var errorCallback = function(data) {
				showTip("提示", data.error, "tip", "tempid");
				// var _self = $(this);
				// _self.parent().hide().prev().show().prev().show();
			}
			doRequest(successCallback, errorCallback, webPath + "/user/safe/updateRelation.do", $('#emergencyContactForm').serialize());
		},// 这是关键的语句，配置这个参数后表单不会自动提交，验证通过之后会去调用的方法
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
		onkeyup : false,
		rules : {
			relationId : {
				required : true
			},
			rlName : {
				required : true,
				minlength : 2,
				maxlength : 4,
				remote : {
					url : webPath + "/user/safe/checkParam.do",
					type : "get",
					contentType : "application/json; charset=utf-8",
					data : {
						name : "rlName",
						rlName : function() {
							return $('#rlName').val();
						}
					}
				}
			},
			rlPhone : {
				required : true,
				minlength : 11,
				maxlength : 11,
				remote : {
					url : webPath + "/user/safe/checkParam.do",
					type : "get",
					contentType : "application/json; charset=utf-8",
					data : {
						name : "rlPhone",
						rlPhone : function() {
							return $('#rlPhone').val();
						}
					}
				}
			}
		},
		messages : {
			relationId : {
				required : "联系人关系不能为空"
			},
			rlName : {
				required : "紧急联系人姓名不能为空",
				minlength : "紧急联系人长度2-4位",
				maxlength : "紧急联系人长度2-4位",
//				remote : "紧急联系人长度2-4位"
			},
			rlPhone : {
				required : "紧急联系电话不能为空",
				minlength : "紧急联系电话长度11位",
				maxlength : "紧急联系电话长度11位",
//				remote : "紧急联系电话格式不正确"
			}
		}
	});
	
	$("#directForm").validate({
		submitHandler : function(form) {
			form.submit();
		},// 这是关键的语句，配置这个参数后表单不会自动提交，验证通过之后会去调用的方法
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
		onkeyup : false,
		rules:{
			inUserName : {
				required:true
			}
		},
		messages:{
			inUserName : {
				required : "请输入定向转账关联账户"
			}
		}
	})
	// 获取验证码,验证原手机号
	$("#sendcode").click(function() {
		var successCallback = function(data) {
			showTip("提示", "发送验证码成功", "tip", "tempid");
			successCallback1()
			function successCallback1() {
				// alert("登录成功");
				// 获取验证码提示
				var clock = '';
				var nums = 60;
				var btn = $("#sendcode");
				// btn.disabled = true; //将按钮置为不可点击
				btn.attr("disabled", true)
				btn.val(nums + '秒后重新获取');
				btn.css({
					"cursor" : "not-allowed",
					"background-color" : "#d9d7d7",
					"font-size" : "12px"
				})
				clock = setInterval(doLoop, 1000);
				// showTip("提示", "发送验证码成功", "tip","tempid");
				function doLoop() {
					nums--;
					if (nums > 0) {
						btn.val(nums + '秒后可重新获取');
					} else {
						clearInterval(clock); // 清除js定时器
						btn.attr("disabled", false);
						btn.val('获取验证码');
						nums = 60; // 重置时间
						btn.css({
							"color" : "white",
							"background-color" : "#CDA972",
							"cursor" : "pointer",
							"font-size" : "16px"
						})
					}
				}
			}
			;
		};
		var errorCallback = function(data) {
			showTip("提示", data.error, "tip", "tempid");
			// var _self = $(this);
			// _self.parent().hide().prev().show().prev().show();
		}
		doRequest(successCallback, errorCallback, webPath + "/bank/user/transpassword/sendCode.do", {
			"mobile" : $("#oldMobile").val()
		});
	});
	// 获取验证码,给新手机号发送信息
	$("#sendnewCode").click(function() {
		var successCallback = function(data) {
			$("#srvAuthCode").val(data.info);
			showTip("提示", "发送验证码成功", "tip", "tempid");
			successCallback2()
			function successCallback2() {
				// alert("登录成功");
				// 获取验证码提示
				var clock = '';
				var nums = 60;
				var btn = $("#sendnewCode");
				// btn.disabled = true; //将按钮置为不可点击
				btn.attr("disabled", true)
				btn.val(nums + '秒后重新获取');
				btn.css({
					"cursor" : "not-allowed",
					"background-color" : "#d9d7d7",
					"font-size" : "12px"
				})
				clock = setInterval(doLoop, 1000);
				// showTip("提示", "发送验证码成功", "tip","tempid");
				function doLoop() {
					nums--;
					if (nums > 0) {
						btn.val(nums + '秒后可重新获取');
					} else {
						clearInterval(clock); // 清除js定时器
						btn.attr("disabled", false);
						btn.val('获取验证码');
						nums = 60; // 重置时间
						btn.css({
							"color" : "white",
							"background-color" : "#CDA972",
							"cursor" : "pointer",
							"font-size" : "16px"
						})
					}
				}
			}
			;
		};
		var errorCallback = function(data) {
			showTip("提示", data.message, "tip", "tempid");
			// var _self = $(this);
			// _self.parent().hide().prev().show().prev().show();
		}
		doRequest(successCallback, errorCallback, webPath + "/bank/user/transpassword/sendPlusCode.do", {
			"mobile" : $("#newMobile").val()
		});
	});
	// 绑定新手机号,下一步
	$("#updateMobileNext").click(function() {
		$.post(webPath + "/bank/user/transpassword/checkCode.do", {
			"mobile" : $("#oldMobile").val(),
			"code" : $("#oldCode").val()
		}, function(data) {
			if (data.errorCode == "707") {
				window.location.href = webPath + "/user/login/init.do";
			} else if (data.errorCode == "708") {
				window.location.href = webPath + "/user/openaccount/init.do";
			}
			if (data) {
				$("#div_updateModile1").hide();
				$("#div_updateModile2").show();
			} else {
				showTip("提示", "验证验证码失败", "tip", "tempid");
			}
		});
	});
	// 绑定新手机号,立即绑定
	$("#bindNewMobile").click(function() {
		$.post(webPath + "/bank/user/transpassword/mobileModify.do", {
			"srvAuthCode" : $("#srvAuthCode").val(),
			"newMobile" : $("#newMobile").val(),
			"smsCode" : $("#newCode").val()
		}, function(data) {
				if (data.errorCode == "707") {
					window.location.href = webPath + "/user/login/init.do";
				} else if (data.errorCode == "708") {
					window.location.href = webPath + "/user/openaccount/init.do";
				}
				if (data.status) {
					showTip("提示", data.message, "tip", "mobileModifyPop");
				} else {
					showTip("提示", data.message, "tip", "tempid");
				}
		});
	});
	// 发送邮件验证
	$(".safe-submit-email").click(function() {
		$.post(webPath + "/user/safe/sendEmail.do", {
			"email" : $("#email").val()
		}, function(data) {
			if (data.errorCode == "707") {
				window.location.href = webPath + "/user/login/init.do";
			} else if (data.errorCode == "708") {
				window.location.href = webPath + "/user/openaccount/init.do";
			}
			if (data.status) {
				$(".safe-per-mail").html($("#email").val());
				var _self = $("#sendEmail");
				var parent = _self.parents(".Security-con");
				var parentForm = _self.parents("form");
				parent.hide();
				parent.next().show();
				parent.prev().find(".security-change").show();
				$("#toEmailHtml").attr("href", "http://mail." + $("#email").val().substring($("#email").val().indexOf("@") + 1));
			} else {
				showTip("提示", data.error, "tip", "tempid");
			}
		});
	});
	
	
	$("#rechargeSms").click(function() {
		updateSmsConfig("rechargeSms");
	});
	$("#withdrawSms").click(function() {
		updateSmsConfig("withdrawSms");
	});
	$("#investSms").click(function() {
		updateSmsConfig("investSms");
	});
	$("#recieveSms").click(function() {
		updateSmsConfig("recieveSms");
	});
});
function updateSmsConfig(id) {
	var value = 0;
	if ($("#" + id).hasClass("safechecked")) {
		value = 0;
	} else {
		value = 1;
	}
	$.post(webPath + "/user/safe/updateSmsConfig.do", {
		"key" : id,
		"value" : value
	}, function(data) {
		if (data.errorCode == "707") {
			window.location.href = webPath + "/user/login/init.do";
		} else if (data.errorCode == "708") {
			window.location.href = webPath + "/user/openaccount/init.do";
		}
		if (data.status) {
		} else {
			showTip("提示", data.error, "tip", "tempid");
		}
	});
}
function dealAction(id) {
	if (id == "updatePassword") {
		location.href = webPath + "/user/safe/init.do";
	}
	if (id == "updatenickname") {
		location.href = webPath + "/user/safe/init.do";
	}
	if (id == "updateRelation") {
		location.href = webPath + "/user/safe/init.do";
	}
	if (id == "checkcode") {
		location.href = webPath + "/user/safe/init.do";
	}
	if(id == "mobileModifyPop"){
		location.href = webPath + "/user/safe/init.do";
	}
}
// 关系赋值
var safeRelation = function() {
	var _self = $(this);
	var relation = _self.prop("value")
	$("#relationId").val(relation);
}
$("#relationUl li").on("click", safeRelation);


//修改关联账户
var formstat = false;
$("#directConfirm").on("click",function(){
	//项目提交
	usernameCheck();
	if(formstat){
		if(checkToken() == true){
			$("#directForm").submit();
		}
	}
})
$("#inUserName").on("blur",function(){
	$("#inUserName-error").remove();
	usernameCheck();
})
function usernameCheck(){
	var url = webPath+"/direct/check.do";
	var data = {
			"inUserName":$("#inUserName").val()
	};
	$.ajax({
		"url" : url,
		"type": "POST",
		"async":false,
		"data":data,
		"success": function(data){
			if(data.error == '1'){
				var ipt = $("#inUserName");
				ipt.parent().append("<div id='inUserName-error' class='error'>"+data.data+"</div>");
				$("#viewname").text("");
				$("#viewmobile").text("");
				$("#inUserId").val("");
				formstat = false;
			}else{
				$("#viewname").text(data.truename);
				$("#viewmobile").text(data.mobile);
				$("#inUserId").val(data.userid);
				formstat = true;
			}
		},
		error : function(data){
			$("#viewname").text("");
			$("#viewmobile").text("");
			$("#inUserId").val("");
			formstat = false;
		}
	})
}

$("#setPsw").click(function(){
	showTip("", "银行处理中,请稍后", "tip", "setPassword");

})

$("#newPhoneCancelBtn,#oldPhoneCancelBtn").click(function(){
	$("#oldCode,#newCode").val("");
})
