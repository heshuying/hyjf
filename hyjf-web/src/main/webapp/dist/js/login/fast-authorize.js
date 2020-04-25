$('.check label').click(function(){
	if($('#readAgreement').is(':checked')){
		$('.btn-sq').removeClass('disabled');
		
	}else{
		$('.btn-sq').addClass('disabled');
	}
})
$('.btn-sq').click(function(){
	//点击授权
	if(!$(this).hasClass('disabled')){
		$.ajax({
			type:"post",
			url:"",
			data:$('#fastForm').serialize(),
			success:function(data){
				if(data.status==1){
					//跳转
					loction.href=data.url
				}else{
					//状态不对的报错
					utils.alert({
						id:"error",
						content:data.error
					});
				}
			},
			//请求失败报错
			error:function(){
				utils.alert({
					id:"error",
					content:"网络错误，请刷新页面"
				});
			}
		});
	}
})

