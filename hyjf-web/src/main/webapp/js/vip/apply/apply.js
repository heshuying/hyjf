/**
 * vip申请按钮初始化
 */
function applyVipBtnInit(){
	$("#applyVipBtn,#applyVipBtn2").click(function(){
		$.ajax({
			type : "POST",
			async: false,
			url : webPath + "/vip/apply/applyCheck.do",
			dataType : 'json',
			data : {
				"account" : $('#account').val()
			},
			success : function(data) {
				if (data.status == true) {
					// 校验成功
					// 跳转到汇付转账页面
					window.location.href = webPath + data.host;
				} else {
					// 校验失败
					if(data.errorCode=='1'){
						// 未登录跳转到登录页面
						window.location.href = webPath + data.host;
					}else if(data.errorCode=='2'){
						// 未开户
						$("#openAccount").click(function(){
							// 跳转到开户页面
							window.location.href = webPath + data.host;
						});
						popUpWin({'ele':'#openAccountPop'});
					}else if(data.errorCode=='3'){
						$("#recharge").click(function(){
							// 跳转到充值页面
							window.location.href = webPath + data.host;
						});
						// 余额不足
						popUpWin({'ele':'#rechargePop'});
					}else if(data.errorCode=='4'){
						// vip重复购买
						popUpWin({'ele':'#repeatVip'});
					}
					
				}
			},
			error : function() {
				// 
				console.info("回调失败");
				// ipt.parent().parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
			}
		});
	});
}

/**
 * vip查看按钮初始化
 */
function vipDetailBtnInit(){
	$("#vipDetailBtn,#vipDetailBtn2").click(function(){
		// 跳转到汇付转账页面
		window.location.href = webPath + "/vip/manage/init.do";
	});
}





~function(){
	applyVipBtnInit();
	vipDetailBtnInit();
	
}();