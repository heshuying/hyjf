var stageNum; //暂存要删除的银行卡
$(document).ready(function() {
	
	$(".delete-card").click(function(){
		//删除银行卡操作
		stageNum = $(this).parent(".item").data("num");
		showTip("", "确认要删除银行卡么", "confirm", "deleteConfirm");
	})
	
	$.validator.addMethod("checkMoney", function(value, element, params) {
		return /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(value);
	});

	/**
	 * 充值金额输入框失去焦点
	 */
	$('#withdrawmoney').on('blur', function() {
		charge();
	});

	//	/**
	//	 * 充值金额输入框键盘事件
	//	 */
	//	$('#withdrawmoney').on('keyup', function(event) {
	//		charge();
	//	});

	// 在键盘按下并释放及提交后验证提交表单
	$(".regForm").validate({
		submitHandler : function(form) {
			if (checkToken() == true) {
				setCookie("rechargeToken","1",360);
				form.submit();
			}else if (checkToken() == false){
				var content = "请刷新页面进行重试";
				showTip(null, content, "tip", "tokenFalse");
			}
		},
		errorElement : "span",
		rules : {
			withdrawmoney : {
				required : true,
				checkMoney : true,
				min : parseFloat(1.01),
				max : parseFloat($("#total").val().replace(/,/g, ""))
			},
			card : {

				required : true
			}
		},
		messages : {
			withdrawmoney : {
				required : "请输入金额",
				checkMoney : "金额必须大于0且为整数或小数，小数点后不超过2位。",
				min : "最低提现金额应大于 1 元",
				max : "超过最大金额"
			},
			card : {
				required : "没有绑定提现银行卡"
			}
		},
		errorPlacement : function(error, element) {

			if (element.is("#hasBindCard")) {
				showTip(null, error.text(), "tip", "#hasBindCardErr");
			} else {
				error.appendTo(element.parent());
			}
		},

	});
	// $("#withdrawmoney").on("blur", function() {
	// if ($("#card-error").text().length > 0) {
	// $("#withdrawmoney-error").remove();
	// }
	// calcMoney();
	// });
	// $("#withdrawmoney").on("keyup", function() {
	// calcMoney();
	// })
	$(".widSubmit").on("click", function() {
		var cardError = $("#card-error").text();
		if (cardError) {
			$("#withdrawmoney-error").remove();
		}
	})
	// /* 计算实际金额 */
	// function calcMoney() {
	// var blance = parseFloat($("#total").val().replace(/,/g, "")); // 用户余额
	// var withdrawFees = parseFloat($("#withdrawmoney").val()); // 输入金额
	// if (withdrawFees < 0 || $("#withdrawmoney").val() == "" ||
	// isNaN(withdrawFees)) {
	// withdrawFees = "0.00";
	// } else if (blance <= withdrawFees + 1) {
	// withdrawFees = fixNum(withdrawFees - 1);
	// } else {
	// withdrawFees = fixNum(withdrawFees);
	// }
	// // $(".widRealMount").html(withdrawFees);
	// }
	
	//获取金额
	var $widAmount = parseFloat($(".widUseMoney").html());
	// 在键盘按下并释放及提交后验证提交表单
	  $(".regForm").validate({
		  	errorElement: "span",
		    rules: {
		      recharge: {
		        required: true,
		        min:1,
		        max:$widAmount
		      },
		      widCard:"required"
		    },
		    messages: {
		      recharge: {
		        required: "请输入金额",
		        min: "最低提现金额应大于等于 1 元",
		        max:"超过最大金额"
		      },
		      widCard:"请选择银行卡"
		    },
		    errorPlacement: function(error, element) {
		    	if(element.attr("name") == "widCard"){
		    		error.appendTo($(".widCards"));
		    	}else{
		    		error.appendTo(element.parent());
		    	}
		        
		    }
		});
});

	function charge() {
		var withdrawmoney = $("#withdrawmoney").val();
		doRequest(withdrawInFoSuccessCallback, withdrawInFoErrorCallback, webPath + "/user/withdraw/withdrawInfo.do?withdrawmoney=" + withdrawmoney, null, false, null, null);
	}
	
	/**
	 * 获取充值信息成功回调
	 */
	function withdrawInFoSuccessCallback(data) {
//		$(".widRealMount").html(data.balance);
		$("#withdrawmoney").val(data.showBalance);
	}
	
	/**
	 * 获取充值信息失败回调
	 */
	function withdrawInFoErrorCallback(data) {
		if (data.errorCode == "707") {
			window.location.href = webPath + "/user/login/init.do";
		} else if (data.errorCode == "708") {
			window.location.href = webPath + "/user/openaccount/init.do";
		}
	}
	
	function dealAction(param){
		if(param == "deleteTip"){
			location.reload();
		}
		if(param == "deleteConfirm"){
			//删除银行卡回调
			$.ajax({
				url:webPath + "/deleteCard/deleteCard.do?cardId="+stageNum,
				data:"",
				success:function(req){
					if(req.status == "success"){
						//删除成功
						$(".item[data-num="+stageNum+"]").remove();
						showTip("", "恭喜您！您的普通银行卡删除成功", "tip", "deleteTip");
					}else{
						//删除失败
						showTip("", "抱歉，银行卡删除错误，请联系客服！", "tip", "deleteTip");
					}
				}
			})
			
		}
	}
	$(function(){
		//token过期刷新
		var rechargeToken = getCookie("withdrawToken");
		if(rechargeToken != ""){
			setCookie("withdrawToken","");
			window.location.href = window.location.href;
		}
		
	})