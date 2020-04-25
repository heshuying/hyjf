$(document).ready(function() {
	$('.drag').drag();
	// $("#drag").hide();

	var chgUrl = function(url) {
		var timestamp = (new Date()).valueOf();
		/* url = url.substring(0,43); */
		if ((url.indexOf("&") >= 0)) {
			url = url + "×tamp=" + timestamp;
		} else {
			url = url + "?timestamp=" + timestamp;
		}
		return url;
	};
	// var changeImg = function() {
	// var imgSrc = $("#imgObj");
	// var src = imgSrc.attr("src");
	// imgSrc.attr("src", chgUrl(src));
	// };
	var ajaxSubmitForm = function() {
		// var params = {};
		// params.username = $("#username").val();
		// params.password = $("#password").val();
		// params.captcha = $("#captcha").val();
		// alert($('#form1').serialize());
		var successCallback = function(data) {
			clearForm("regForm");
			// modify by zhangjp 支持登录完成后跳转回原页面 20161014 start
			if(data.retUrl){
				location.href = webPath + data.retUrl;
			}else{
				location.href = webPath + "/user/pandect/pandect.do";
			}
			// modify by zhangjp 支持登录完成后跳转回原页面 20161014 start
			
		};
		var errorCallback = function(data) {
			showTip("提示", data.error, "tip", "tempid");
			{
				$("#divdrag").html("<div id=\"drag\" class=\"drag\"></div><input type=\"checkbox\" id=\"dragInput\" value=\"\" name=\"dragInput\" />");
				$(".drag").drag();
			}
		}
		var params = {};
		params.loginUserName = $("#loginUserName").val();
		params.loginPassword = $.md5($("#loginPassword").val());
		// add by zhangjp 支持登录完成后跳转回原页面 20161014 start
		params.retUrl = $("#retUrl").val();
		// add by zhangjp 支持登录完成后跳转回原页面 20161014 start
		doRequest(successCallback, errorCallback, webPath + "/user/login/login.do", params);
	}
	// $("#imgObj").click(function() {
	// changeImg();
	// });
	// $("#refreshCaptcha").click(function() {
	// changeImg();
	// });
	$("#regForm").validate({
		submitHandler : function() {
			ajaxSubmitForm();
		},// 这是关键的语句，配置这个参数后表单不会自动提交，验证通过之后会去调用的方法
		errorElement : "div",
		focusInvalid : false,
		onclick : false,
		onkeyup : false,
		rules : {
			loginUserName : {
				required : true,
				maxlength : 16
			// ,
			// remote : {
			// url : webPath + "/user/login/checkUserAction.do",
			// type : "get",
			// data : {
			// username : function() {
			// return $('#loginUserName').val();
			// }
			// }
			// }
			},
			loginPassword : {
				required : true,
				minlength : 6,
				maxlength : 16
			},
			dragInput : "required"
		},
		messages : {
			loginUserName : {
				required : "请填写手机号或用户名",
				maxlength : "用户名过长"// ,
			// remote : "手机号/用户名不存在"
			},
			loginPassword : {
				required : "请填写密码",
				minlength : "密码过短",
				maxlength : "密码过长"
			},
			// captcha : {
			// required : "请输入验证码",
			// remote : "请输入正确的验证码"
			// },
			dragInput : "滑动滑块解锁"
		}
	});
});
