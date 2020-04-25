$(function(){
	$('.type a').click(function(){ //选择充值方式
		if(!$(this).hasClass('check')){
			$('.type a').removeClass('check').parents('.type').find('input').val($(this).data('val'));
			$(this).addClass('check');
			if($(this).hasClass('onlinebank')){
				$("#rechargeForm li.offline").hide();
				$('#rechargeForm li.bound-card').show()
				$("#rechargeForm li.card").show();
				$("#rechargeForm li.recharge").show();
				$("#rechargeForm li.type .hint").show();
				$("#rechargeForm .sub").show();
			}else{
				$("#rechargeForm li.card").hide();
				$("#rechargeForm li.recharge").hide();
				$("#rechargeForm li.type .hint").hide();
				$('#rechargeForm li.bound-card').hide();
				$("#rechargeForm li.offline").show();
				$("#rechargeForm .sub").hide();
			}
		}
	});
	//表单内验证
	$("#rechargeForm").validate({
		rules: {
			money:{
				required:true,
				number:true,
				min:1
			},
			card:{
		    	required:true,
		    	number:true,
		    	isBankCard:true
		   }			
	   },
	    messages:{
	    	money:{
				required:'请输入充值金额',
				number:'请输入数字',
				min:'最低充值金额应不小于1元'
			},
			card:{
		    	required:'请输入银行卡号',
		    	number:'请输入正确的银行卡号',
		    	isBankCard:'请输入正确的银行卡号'
		   }	
	    },

	});
	$('#rechargeForm .sub').click(function(){
		$('#rechargeForm') .submit()
	});
});
