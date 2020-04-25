$(function(){
	//点选银行卡
	$('#cashForm .card .card-content').click(function(){
		$('#cashForm .card .i-duihao').remove();
		$('#cardId').val($(this).parent().data('id'));
		$(this).parent().append('<span class="i-duihao"></span>');
	})

	//删除银行卡
	$('#cashForm .card .delete').click(function(){
		var that=this;
		utils.alert({
			id:'deleteCard',
			title:'确认要删除吗',
			content:'确定要删除此银行卡吗',
			type:'confirm',
			fnconfirm:function(){
				$.ajax({
					type:'post',
					url: webPath + '/deleteCard/deleteCard.do',
					data:{
						cardId : $(that).parents('.card').data('id') 
					},
					success:function(data){
						if(data.status == "success"){
							if($('#cardId').val()==$(that).parents('.card').data('id')){
								$('#cardId').val('');
							}
							$(that).parents('.card').remove();
							utils.alertClose('deleteCard')
						}else{
							utils.alertClose('deleteCard')
							utils.alert({
								id:'errorBox',
								title:'错误',
								content:'删除银行卡失败，请稍后重试'
							})
						}
					},
					error:function(){
						utils.alertClose('deleteCard')
						utils.alert({
							id:'errorBox',
							title:'错误',
							content:'删除失败'
						})
					}
				})
			}
		});
	})
	
	//设置表单验证
	$("#cashForm").validate({//表单验证
		submitHandler:function(form){
			if($('#cardId').val()==""){
				utils.alert({
					id:'noticeBox',
					title:'提示',
					content:'请选择银行卡'
				})
			}else if(checkToken() == true){
				setCookie("withdrawHuifuToken","1",360);
				form.submit();
			}
		},
		onkeyup:false,
		rules: {
			money:{
				required : true,
				checkMoney : true,
				min : parseFloat(1.01),
				max : parseFloat($("#total").val().replace(/,/g, ""))
			}		
	   },
	    messages:{
	    	money:{
				required:'请输入提现金额',
				checkMoney : "金额必须大于0且为整数或小数，小数点后不超过2位",
				min:'最低提现金额应不小于1元',
				max:'提现金额不能超过账户余额'
			},
	    }
	});
	$('#cashForm .sub').click(function(){//提交
		$('#cashForm').submit();
	})
})
$(function(){
	//token过期刷新
	var withdrawHuifuToken = getCookie("withdrawHuifuToken");
	if(withdrawHuifuToken != ""){
		setCookie("withdrawHuifuToken","");
		utils.refresh();
	}
})
