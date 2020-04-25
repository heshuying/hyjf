var bankType=$('#bankType').val()
$('#cashForm #money').keyup(function(){//设置到账金额
	var that=this;
	var num=$(that).val()
	if(num>1){
		num--
	}else{
		num=0
	}
	$('.to-accout span').text(function(){
		return utils.toDecimal2(num,true)
	})
	//验证最大金额，决定是否出现开户行
	var _self = $(this);
    var val = _self.val();
    var reg = /^[0]|[^0-9]/g;
    _self.val(val.replace(reg, ''));
    
    if(bankType==7||bankType==56){//20万出现开户行号
    	if(!(_self.val()<50000)){
    		$('ul li.bank').show()
    	}else{
    		$('ul li.bank').hide()
    	}
    }else{//5万出现开户行号
    	if(!(_self.val()<200000)){
    		$('ul li.bank').show()
    	}else{
    		$('ul li.bank').hide()
    	}
    }
})
$("#cashForm").validate({//表单验证
	onkeyup:false,
	rules: {
		money:{
			required:true,
			number:true,
			min:1,
			
		},
		card:{
	    	required:true,
	    	number:true,
	    	isBankCard:true
	    },
	    khBankId:{
	   		required:true
	    }
   },
    messages:{
    	money:{
			required:'请输入提现金额',
			number:'请输入数字',
			min:'最低提现金额应不小于1元'
		},
		card:{
	    	required:'请输入银行卡号',
	    	number:'请输入正确的银行卡号',
	    	isBankCard:'请输入正确的银行卡号'
	    },
	    khBankId:{
	   		required:'请输入开户行号'
	    }
    }
});
$('#cashForm .sub').click(function(){//提交
	$('#cashForm').submit()
})
