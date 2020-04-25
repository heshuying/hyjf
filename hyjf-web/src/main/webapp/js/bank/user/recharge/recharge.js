$(function(){
	//验证码倒计时
	var mobFlag=true;
	function countdown(el,s,w){ //获取手机验证码
		mobFlag=false;
		var w=w || '获取验证码' //结束后的文案
		var s=parseInt(s);
		el.text(s+'s');
		var time=window.setInterval(function(){
			s--;
			if(s>0||s==0){
				el.text(s+'s');
			}else{
				el.html(w);
				mobFlag=true;
				clearInterval(time);
			}
		},1000)
		return time
	}
	//充值验证码获取
	$('#rechargeForm .get-code').click(function(){
		if(mobFlag&&$("#phoneNum").valid()){
			var t=countdown($('.get-code'),60,'<em class="rule"></em>获取验证码')
			$.ajax({
				type:"post",
				url:webPath + "/bank/web/user/recharge/sendCode.do",
				data:$('#rechargeForm').serialize(),
				success:function(data){
					if(!data.status){
						utils.alert({id:'errorCode',title:'错误',content:data.message});
						$('#code').val('');
						clearInterval(t)
						$('.get-code').html('<em class="rule"></em>获取验证码');
						mobFlag=true
					}else{
						$('#smsSeq').val(data.smsSeq)
					}
				},
				error:function(){
					utils.alert({id:'errorCode',title:'错误',content:'发送失败'});
					$('#code').val('');
					clearInterval(t);
					$('.get-code').html('<em class="rule"></em>获取验证码');
					mobFlag=true
				}
			});
		}
	})
	
	
	
	//绑卡验证码获取
	$('#bindCardForm .get-code').click(function(){
		if(mobFlag&&$("#card").valid()&&$("#mobile").valid()){
			console.log(11)
			var t=countdown($('.get-code'),60,'<em class="rule"></em>获取验证码')
			$.ajax({
				type:"post",
				url:webPath + "/bank/web/bindCard/sendPlusCode.do", //绑卡验证码地址
				data:$('#bindCardForm').serialize(),
				success:function(data){
					if(!data.status){
						utils.alert({id:'errorCode',title:'错误',content:data.message});
						$('#code').val('');
						clearInterval(t)
						$('.get-code').html('<em class="rule"></em>获取验证码');
						mobFlag=true
					}else{
						$('#smsSeq').val(data.info)
					}
				},
				error:function(){
					utils.alert({id:'errorCode',title:'错误',content:'发送失败'});
					$('#code').val('');
					clearInterval(t);
					$('.get-code').html('<em class="rule"></em>获取验证码');
					mobFlag=true
				}
			});
		}
	})
	//绑卡验证码获取 end
	
	
	
	
	$('.type a').click(function(){ //选择充值方式
		if(!$(this).hasClass('check')){
			$('.type a').removeClass('check').parents('.type').find('input').val($(this).data('val'));
			$(this).addClass('check');
			
			var formName = "";
			if(isBundCardFlag == 0){
				formName = "bindCardForm";
			}else{
				formName = "rechargeForm";
			}
			
			if($(this).hasClass('onlinebank')){
				$('#rechargeType').val('0');//将充值类型设置为0
				$("#" + formName + " li.offline").hide();
				$("#" + formName + " li.bound-card").show()
				$("#" + formName + " li.card .note1").show();
				$("#" + formName + " li.card .note2").hide();
				$("#" + formName + " li.recharge").show();
				$("#" + formName + " li.type .hint").show();
				$("#" + formName + " .sub").show();
			}else{
				$('#rechargeType').val('2');
				$("#" + formName + " li.card .note1").hide();
				$("#" + formName + " li.card .note2").show();
				$("#" + formName + " li.recharge").hide();
				$("#" + formName + " li.type .hint").hide();
				$("#" + formName + " li.bound-card").hide();
				$("#" + formName + " li.offline").show();
				$("#" + formName + " .sub").hide();
			}
		}
	});
	//表单内验证
	$("#rechargeForm").validate({
		submitHandler:function(form){
			form.submit();
//            $.ajax({
//            	type:'POST',
//            	url:webPath + "/bank/web/user/recharge/rechargeOnline.do",
//            	data:$('#rechargeForm').serialize(),
//            	success:function(data){
//            		if(data.status){
//            			location.href=data.rechargeUrl;
//            		}else{
//            			utils.alert({
//            				id:"errorSubmit",
//            				type:"alert",
//            				content:data.message,
//            				fnconfirm:utils.refresh
//            			});
//            		}
//            	},
//            	error:function(){
//            		utils.alert({
//        				id:"errorSubmit",
//        				type:"alert",
//        				content:"网络错误，请刷新页面",
//        				fnconfirm:utils.refresh
//        			});
//            	}
//            })
        },
		rules: {
			money:{
				required:true,
				min:1
			},
			mobile:{
				required:true,
				isMobile:true,
			},
			cardNo:{
		    	required:true,
		    	isBankCard:true
		   }			
	   },
	    messages:{
	    	money:{
				required:'请输入充值金额',
				min:'最低充值金额应不小于1元'
			},
			mobile:{
				required:'请输入银行预留手机号',
				isMobile:'请输入正确手机号'
			},
			cardNo:{
		    	required:'请输入银行卡号',
		    	isBankCard:'请输入正确的银行卡号'
		   }	
	    },

	});
	
	$("#bindCardForm").validate({
		rules: {
			mobile:{
				required:true,
				isMobile:true,
			},
			smsCode:{
				required:true,
			},
			cardNo:{
		    	required:true,
		    	isBankCard:true
		   }			
	   },
	    messages:{
	       cardNo:{
		    	required:'请输入银行卡号',
		    	isBankCard:'请输入正确的银行卡号'
		   },
		   mobile:{
				required:'请输入银行预留手机号',
				isMobile:'请输入正确手机号'
		   },
		   smsCode:{
			    required:'请输入验证码'
		   },
	    },

	});
	//绑卡提交
	$('.bindsub').click(function(){
		if($('#bindCardForm').valid()){
			if (checkToken() == true) {
	    		$.ajax({
					type:"post",
					url:webPath + "/bank/web/bindCard/bindCardPlus.do", //绑卡地址
					data:$('#bindCardForm').serialize(),
					success:function(data){
						if(!data.status){
							utils.alert({id:'errorBind',title:'绑卡失败',content:data.message,type:"alert",fnconfirm:utils.refresh});
						}else{
							utils.alert({id:'errorBind',title:'绑卡成功',content:'恭喜您，银行卡绑卡成功',type:"alert",fnconfirm:utils.refresh});
						}
					},
					error:function(){
						utils.alert({id:'errorBind',title:'绑卡失败',content:'绑卡失败，请稍后重试',type:"alert",fnconfirm:utils.refresh});
					}
				});
			}
		}

	});
	
	$('#rechargeForm .sub').click(function(){
		if($('#isSetPassword').val() != '1'){
			utils.alert({
				id:"setPassword",
				content:'请先设置交易密码。',
				btntxt:"去设置",
				fnconfirm:function(){
					//未设置交易密码跳转至设置交易密码页面
					window.location.href = webPath + "/bank/user/transpassword/setPassword.do";
				}
			});
		}else if($("#paymentAuthStatus").val().length == 1 && $("#paymentAuthStatus").val() != "1"){
			utils.alert({ 
	            id: "authInvesPop", 
	            type:"confirm",
	            content:"<div>部分交易过程中，会收取相应费用<br>请进行授权。</div><div class='status-box'><div class='off'>例如：提现手续费，债转服务费等。</div></div>",
	            alertImg:"msg",
	            fnconfirm: function(){
	                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/page.do";
	            }
	        });
		}else if($('#rechargeForm').valid()){
	    	if (checkToken() == true) {
	        	setCookie("rechargeToken","1",360);

	        	/*
	        	  提交充值
	        	  submit_recharge
	        	  @params entrance  入口
	        	  @params recharge_amount 充值金额 number
	        	  @params bank_name 银行名称  String

	        	*/
	        	sa && sa.track('submit_recharge',{
	        	  entrance: document.referrer,   
	        	  recharge_amount: Number($("#money").val()),
	        	  bank_name: $("#bankName").val()
	        	})
	        	$('#rechargeForm') .submit();
	    	}
		}
	});
});



var stageNum; //暂存要删除的银行卡
$(document).ready(function() {
	function refreshPage(){
		window.location.href = window.location.href
	}
	$("#unbundling").click(function(){
		//删除银行卡操作
		stageNum = $('#cardId').val();
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
	
});
$(function(){
	//token过期刷新
	var rechargeToken = getCookie("rechargeToken");
	if(rechargeToken != ""){
		setCookie("rechargeToken","");
		utils.refresh();
	}
})
$("#money").keyup(function(){
    var _self = $(this);
    var val = _self.val();
    var reg = /^[0]|[^0-9]/g;
    _self.val(val.replace(reg, ''));

})