var bankType=$('#bankType').val()

$('#cashForm #withdrawmoney').keyup(function(){//设置到账金额
	var that=this;
	var num=$(that).val()
	if(num>1){
        num--;
        num = num.toFixed(2);
	}else{
		num=0
	}
	//验证最大金额，决定是否出现开户行
	var _self = $(this);
    var val = _self.val();
    var reg = /(^0|^\.)|[^\d\.]|(\.\.)/g;
    _self.val(val.replace(reg, ''));
 // 大于50000 大额 + 1元手续费
	if((_self.val()>50001 || _self.val()==50001)){
		$('ul li.bank').show()
	}else{
		$('ul li.bank').hide()
	}
	$('.to-accout span').text(function(){
		return utils.toDecimal2(num,true)
	})
})

$('#cashForm .sub').click(function(){//提交
	$('#cashForm').submit()
})


var stageNum; //暂存要删除的银行卡
$(document).ready(function() {
	function refreshPage(){
		window.location.href = window.location.href
	}
	$(".delete-card").click(function(){
		//删除银行卡操作
		stageNum = $(this).parent(".item").data("num");
		var content = "您确认要解绑这张银行卡吗？";
		var fnDeleteConfirm = function(){
			//删除银行卡回调
			$.ajax({
				url:webPath + "/bank/web/deleteCard/deleteCard.do?cardId="+stageNum,
				data:"",
				success:function(req){
					if(req.status == "false"){
						//删除失败
						utils.alert({
							id:"deleteTip",
							content:"抱歉，银行卡解绑错误，请联系客服！",
							fnconfirm:refreshPage
						});
					}else if(req.status == "money"){
						//余额还有
						utils.alert({
							id:"deleteTip",
							content:"抱歉，您还有部分余额，请清空后再解绑银行卡！",
							fnconfirm:refreshPage
						});
					}else {
						//删除成功
						$(".item[data-num="+stageNum+"]").remove();
						utils.alert({
							id:"deleteTip",
							content:"恭喜您！您的普通银行卡解绑成功",
							fnconfirm:refreshPage
						});
					}
				}
			});
			utils.alertClose("deleteCardDialog");
		}
		utils.alert({
			id:"deleteCardDialog",
			type:"confirm",
			content:content,
			fnconfirm:fnDeleteConfirm
		});
	});
	
	// 在键盘按下并释放及提交后验证提交表单
	$("#cashForm").validate({
		submitHandler : function(form) {
			var content="";
			if($("#isSetPassWord").val() != "1"){
				content = "请先设置交易密码。";
				utils.alert({
					id:"goSetPassPop",
					content:content,
					btntxt:"去设置",
					fnconfirm:function(){
						//未设置交易密码跳转至设置交易密码页面
						window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
					}
				});
			}else if($("#isBindCard").val() != "1"){
				content = "您尚未绑定银行卡，请先绑定银行卡";
				utils.alert({
					id:"goBindCardPop",
					content:content,
					btntxt:"去绑定",
					fnconfirm:function(){
						//未绑定银行卡 跳转至绑卡页面
						window.location.href = webPath + "/bank/web/bindCard/index.do";
					}
				});
			
			}else if($("#paymentAuthOn").val() == "1" && $("#paymentAuthStatus").val() != "1"){
				if($("#roleId").val() == 1){
					utils.alert({ 
			            id: "authInvesPop", 
			            type:"confirm",
			            content:"<div>应合规要求，出借、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>自动投标，自动债转，服务费授权。</div></div>",
			            alertImg:"msg",
			            fnconfirm: function(){
			                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
			            }
			        });
				}else if($("#roleId").val() == 2){
					utils.alert({ 
			            id: "authInvesPop", 
			            type:"confirm",
			            content:"<div>应合规要求，还款、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>服务费授权，还款授权。</div></div>",
			            alertImg:"msg",
			            fnconfirm: function(){
			                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
			            }
			        });
				}else{
					utils.alert({ 
			            id: "authInvesPop", 
			            type:"confirm",
			            content:"<div>应合规要求，还款、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>服务费授权。</div></div>",
			            alertImg:"msg",
			            fnconfirm: function(){
	                        // 神策服务费授权
			                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
			            }
			        });
				}
				
			}else if (checkToken()) {
				setCookie("withdrawToken","1",360);
				/*
				  申请提现
				  submit_withdraw
				  @params withdraw_amount  提现金额 Number
				  @params withdraw_type 提现方式 String  [普通提现，大额提现]
				  @params fee_amount 提现手续费  Number 1

				*/
				// 大于50000 大额 + 1元手续费
				sa && sa.track('submit_withdraw',{
				  withdraw_amount: Number($('#cashForm #withdrawmoney').val()),
				  withdraw_type: $('#cashForm #withdrawmoney').val()>= 50001 ? '大额提现' :'普通提现',
			      bank_name: $("#bankName").val(),
				  fee_amount: 1
				})

				setTimeout(function(){
					form.submit();
				},500)
			}
		},

		errorElement : "span",
		rules : {
			withdrawmoney : {
				required : true,
				checkMoney : true,
				min : parseFloat(1.01),
				withdrawmoney:true,
				max : parseFloat($("#total").val().replace(/,/g,''))
			},
			card : {
				required : true
			},
			payAllianceCode:{
				required:true
			}
		},
		messages : {
			withdrawmoney : {
				required : "请输入金额",
				checkMoney : "金额必须大于0且为整数或小数，小数点后不超过2位。",
				min : "最低提现金额应大于 1 元",
				withdrawmoney:'当天充值资金当天无法提现，请调整取现金额。',
				max : "超出可用金额"
			},
			payAllianceCode:{
				required:'请输入开户行号'
			}
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

$(function(){
	//token过期刷新
	var withdrawToken = getCookie("withdrawToken");
	if(withdrawToken != ""){
		setCookie("withdrawToken","");
		utils.refresh();
	}
	
})