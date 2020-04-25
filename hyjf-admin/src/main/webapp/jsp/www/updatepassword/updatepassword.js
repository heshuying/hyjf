var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 修改密码
	updatePasswordAction : "updatePasswordAction"
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	clickPwCheck:function(pw){
		console.log(111)
		var _this=$('#'+pw)
    	var val=_this.val()
    	var box=$('.strength-box')
    	var num=0
    	if(val.length>7){
    		if(_this.valid()){
    			num=this.score(val)
    			if(num>=75){
    				$('.strength-box div').addClass('light')
    			}else if(num>=40){
    				$('.strength-box div').eq(0).addClass('light')
    				$('.strength-box div').eq(1).addClass('light')
    			}else{
    				$('.strength-box div').eq(0).addClass('light')
    			}
    		}
    		else{
        		$('.strength-box div').removeClass('light')
        		$('.strength-box span').html('')
        		
        	}
    	}else{
    		$('.strength-box div').removeClass('light')
    		$('.strength-box span').html('')
    		
    	}
	},
	score:function score(s){
	      var num=0
	      if(s.length<10){
	        num+=5
	      }else if(s.length>=10&&s.length<=12){
	        num+=10
	      }else{
	        num+=25
	      }
	      if(s.match(/[A-Za-z]/g)==null){
	        num+=0
	      }else if(s.match(/[A-Z]/g)==null){
	        num+=10
	      }else if(s.match(/[a-z]/g)==null){
	        num+=10
	      }else if(/[A-Z]/.test(s)&&/[a-z]/.test(s)){
	        num+=20
	      }
	      if(s.match(/\d/g)==null){
	        num+=0
	      }else if(s.match(/\d/g).length==1){
	        console.log('ddd')
	        num+=10
	      }else if(s.match(/\d/g).length>1){
	        num+=20
	      }
	      if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g)==null){
	        num+=0
	      }else if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g).length==1){
	        num+=10
	      }else if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g).length>1){
	        num+=25
	      }
	      if(/\d/.test(s)&&/[a-z]/.test(s)&&/[A-Z]/.test(s)&&/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/.test(s)){
	        num+=5
	      }else if(/\d/.test(s)&&/[a-zA-Z]/.test(s)&&/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/.test(s)){
	        num+=3
	      }else if(/\d/.test(s)&&/[a-zA-Z]/.test(s)){
	        num+=2
	      }
	      return num
	    },
	confirmClkAct : function() {
		if (Page.validate.check(false)
				&& Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要更改登录密码么？", "info", {
				closeOnConfirm : false,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消",
				showCancelButton : true,
			}, function(isConfirm, tid) {
				if (isConfirm) {
					Page.coverLayer("正在操作数据，请稍候...");
					tid = setTimeout(function() {
						swal.close();
					}, 100);
					$.post(Action.updatePasswordAction, $("#mainForm")
							.serialize(), function(data) {
						clearTimeout(tid);
						Page.coverLayer();
						Page.confirm("操作成功", data.info, "info", {
							closeOnConfirm : false,
							showCancelButton : false
						}, function() {
							window.location.reload();
						});
					});
				}
			});
		}
	}

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */

$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Save").click(Events.confirmClkAct);
		
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
		Page.validate.addRule([
           {
               ele:"#newPassword2",
               datatype:"*8-16",
               nullmsg:"请输入密码！",
               recheck:"newPassword1"
           }
       ]);
		Page.validate.tipmsg.w['*8-16']="密码8-16位，必须包含字母、数字、符号至少两种！";
		Page.validate.tipmsg.reck="两次密码不同！";
		$('#newPassword1').keyup(function(){
			var _this=$(this)
	    	var val=_this.val()
	    	var box=$('.strength-box')
	    	var num=0
	    	console.log(val)
	    	if(val.length>7){
    			num=Events.score(val)
    			console.log(num)
    			if(num>=75){
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').addClass('light')
    			}else if(num>=40){
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').eq(0).addClass('light')
    				$('.strength-box div').eq(1).addClass('light')
    			}else{
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').eq(0).addClass('light')
    			}
	    	}else{
	    		$('.strength-box div').removeClass('light')
	    		$('.strength-box span').html('')
	    		
	    	}
		})
		Page.validate.addRule([ {
			ele : "#oldPassword",
			datatype : "*1-20",
			ajaxurl : "checkPasswordAction"
		} ]);
		Page.validate.addRule([ {
			ele : "#newPassword1",
			datatype :/^(?![0-9]+$)(?![a-zA-Z]+$)(?![\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]+$)[0-9A-Za-z\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]{8,16}$/,
		} ]);
	}
}),

Page.initialize();
