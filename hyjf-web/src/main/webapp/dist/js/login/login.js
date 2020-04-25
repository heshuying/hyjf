$(function() {
	$("#loginForm .sub").click(function() {
		$("#loginForm").submit();
	})
	$('#loginUserName').change(function(){
		$('.error-time').hide()
	})
	$("#loginForm").validate(
			{
				errorPlacement : function(error, element) {
					$('.error-time').hide()
					error.appendTo(".error-box");
				},
				// errorLabelContainer:".error-box",
				groups : {
					form : "loginUserName loginPassword"
				},
				submitHandler : function(form) {
					var successCallback = function(data) {
						// 神策关联登录用户
						sa && sa.login(data.userid)
						/*
						  登录结果
						  login
						  @params entrance  入口
						  @params login_type 登录方式  String default："密码登录"
						  @params success 是否成功 Boolean
						  @params error_message 错误信息  String
						*/
						sa && sa.track('login',{
						  entrance: document.referrer,   
						  login_type: "密码登录",
						  success: true,
						  error_message: ''
						})

						clearForm("loginForm");
						// modify by zhangjp 支持登录完成后跳转回原页面 20161014 start
						if (data.retUrl) {
							if (data.retUrl == "/activity/2018qixi/invest_list.do") {
                                location.href = webPath + "/activity/2018qixi/activity_qixi.do"
							} else {
                                location.href = webPath + data.retUrl;
							}
						} else {
							location.href = webPath
									+ "/user/pandect/pandect.do";
						}
						// modify by zhangjp 支持登录完成后跳转回原页面 20161014 start

					};
					var errorCallback = function(data) {
						/*
						  登录结果
						  login
						  @params entrance  入口
						  @params login_type 登录方式  String default："密码登录"
						  @params success 是否成功 Boolean
						  @params error_message 错误信息  String
						*/
						sa && sa.track('login',{
						  entrance: document.referrer,   
						  login_type: "密码登录",
						  success: false,
						  error_message: data.error
						})
						utils.alert({
							id : "tempidDialog",
							type : "tip",
							content : data.error
						});
						if(data.errornumber!=''){
							$('.error-time').html(data.errornumber).show()
						}
						
					}
					var params = {};
					var presetProps = sa.getPresetProperties();
                    presetProps._distinct_id = sa.store.getFirstId() || sa.store.getDistinctId();
					params.loginUserName = $("#loginUserName").val();
					params.loginPassword = $.md5($("#loginPassword").val());
					// 神策预置属性
					params.presetProps = JSON.stringify(presetProps);
					doRequest(successCallback, errorCallback, webPath
							+ "/user/login/login.do", params);
				},
				rules : {
					loginUserName : {
						required : true,
					// maxlength : 16
					},
					loginPassword : {
						required : true,
					// ispwd:true,
					// minlength : 6,
					// maxlength : 16
					}
				},
				messages : {
					loginUserName : {
						required : '请输入手机号/用户名',
						maxlength : '请输入正确的手机号/用户名'
					},
					loginPassword : {
						required : '请输入密码',
					// ispwd:'请输入正确的密码',
					// minlength : "请输入正确的密码",
					// maxlength : "请输入正确的密码"
					}
				},

			});
})
