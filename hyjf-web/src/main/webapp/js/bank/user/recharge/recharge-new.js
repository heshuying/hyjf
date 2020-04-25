$(function(){
	var mobFlag=true;
	$('.recharge-tab div.tab').each(function(i,el){ //选择充值方式
		$(el).click(function(){
			$('.recharge-tab div').removeClass('select')
			$(this).addClass('select')
			$('.acount-recharge').hide().eq(i).show()
		})
	});
	//表单内验证
	$("#rechargeForm").validate({
		submitHandler:function(form){
			form.submit();
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
	   },
	    messages:{
	    	money:{
				required:'请输入充值金额',
				min:'最低充值金额应不小于1元'
			},
			mobile:{
				required:'请输入银行预留手机号',
				isMobile:'请输入正确手机号'
			}
	    },

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
		}else if($("#paymentAuthStatus").val() != "1" && $("#paymentAuthOn").val() == "1"){
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
		            content:"<div>应合规要求，还款、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>服务费授权，还款授权</div></div>",
		            alertImg:"msg",
		            fnconfirm: function(){
		                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
		            }
		        });
			}else{
				utils.alert({ 
		            id: "authInvesPop", 
		            type:"confirm",
		            content:"<div>应合规要求，还款、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>服务费授权</div></div>",
		            alertImg:"msg",
		            fnconfirm: function(){
                        // 神策服务费授权
		                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
		            }
		        });
			}
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
	        	setTimeout(function(){
	        		$('#rechargeForm') .submit();
	        	},300);
	    	}
		}
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