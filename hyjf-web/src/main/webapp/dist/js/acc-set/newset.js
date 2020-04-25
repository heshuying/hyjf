$(function() {
	$('.btn-set').click(function(){
		var rechargeSms='';
		var withdrawSms='';
		var investSms='';
		var recieveSms='';
		var isSmtp='';
		if($('#rechargeSms').attr('checked')){
			rechargeSms=0;
		}else{
			rechargeSms=1;
		}
		if($('#withdrawSms').attr('checked')){
			withdrawSms=0;
		}else{
			withdrawSms=1;
		}
		if($('#investSms').attr('checked')){
			investSms=0;
		}else{
			investSms=1;
		}
		if($('#recieveSms').attr('checked')){
			recieveSms=0;
		}else{
			recieveSms=1;
		}
		if($('#isSmtp').attr('checked')){
			isSmtp=0;
		}else{
			isSmtp=1;
		}
		
		
		$.ajax({
			url : webPath+"/user/safe/updateMessageNotificationAction.do",
			type : "POST",
			data: {
				"rechargeSms" : rechargeSms,
				"withdrawSms" : withdrawSms,
				"investSms" : investSms,
				"recieveSms" : recieveSms,
				"isSmtp" : isSmtp
			},
			success : function(data) {
				if(data.status==0){
					$('.success').show();
				}else{
					
				}
			},
			error : function(err) {
				
			}
		});
		
		
	})
	$('.magic-checkbox a').click(function(){
		var _self = $(this);
		_self.toggleClass('bg');
		if (_self.hasClass('bg')) {
	      	_self.find('input').attr('checked', true);
	      	
	   	} else {
	      	_self.find('input').attr('checked', false);
	      	
	   	}
		$('.success').hide();
	})

});