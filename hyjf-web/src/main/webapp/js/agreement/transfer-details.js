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
        
        alert(webPath + "/bank/user/credit/sendcode.do");
        
        // 请求验证码发送接口
		jQuery.ajax({
			type: "POST",
			async: "async",
			url: webPath + "/bank/user/credit/sendcode.do",
			contentType:"application/x-www-form-urlencoded;charset=utf-8",
			dataType: "json",
			data: {
			},
			error: function(request) {
		        popupWin("连接服务器出错,请稍后重试");
		    },
			success: function(data){
				if(data.resultFlag==0){
					//popupWin("手机验证码已经发送完成");
				}else{
					jQuery("#telcodeTip").html(data.msg);
				}
			}
			
		});
    }
	//滚动解锁
    var slider = new SliderUnlock("#slider", {
        duration: 0 
    }, function(){
        $('#slide-process').text('验证成功');
        $('#slide-box').append('<span class="lock"></span>')
        $('#label').unbind()
        $('.get-code').removeClass('disable')
        $('#slide-box').find('span.error').remove()
    }, function(){
        $("#slide-process").width(slider.index)
    });
    slider.init();
        //添加滑动的验证
    jQuery.validator.addMethod("isUnlock", function(value, element) {
        var val=parseInt(value);
        return this.optional(element) ||(val==1) ;
    }, "请移动滑块进行验证");
        //表单验证
    $("#registerForm").validate({
        onkeyup:false,
        ignore: ".ignore",
        rules: {
            code:{
                required:true,
                rangelength:[4,8],
            },
            lockable:{
                isUnlock:true,
            },
        },
        
        messages:{
            code:{
                required:'请输入验证码',
                rangelength:'请输入正确的验证码',
            },
            lockable:{
                isUnlock:'请移动滑块进行验证'
            },
        },
        //更改错误信息加入的位置
        errorPlacement: function(error, element) {
            if(element.parent().is('#slideUnlock')){
                error.appendTo(element.parents('#slide-box'))
            }else{
                error.appendTo(element.parent());  
            }
            
        },
        success:function(value, element, param){
            if(!$(element).siblings().is('.i-success')){
                $(element).parent().append('<span class="i-success iconfont icon-duihao"></span>')
            }
        },
        onfocusout: function( element ) {
            /*1.帮助提示信息移除*/
            $(element).parent().children(".i-success").remove();
    
            this.element( element );
    
        }
    });
	     //提交按钮
    $("#registerForm .sub").click(function(){
        if(!$(this).hasClass('disable')){
            $("#registerForm").submit()
        }
    })
	//发送验证码相关操作,判断
	$('.get-code').click(function(){
		if(mobFlag&&$('#lockable').val()==1){
			countdown($('.get-code'),60,'<em class="rule"></em>获取验证码')
//			$.ajax({
//				type:"get",
//				url:""
//			});
		}
	})
	//加的效果
    $(".add").click(function(){
        var n=$(this).prev().val();
        var num=parseFloat(parseFloat(n).toFixed(1));
        var numnew=parseFloat(num+0.1).toFixed(1);
        if(num>=2.0){
            return;
        }else{
            $(this).prev().val(numnew);
            creditPriceAndFee();
       }
    });
            //减的效果
    $(".jian").click(function(){
    var n=$(this).next().val();
    var num=parseFloat(parseFloat(n).toFixed(1));
    var numnew=parseFloat(num-0.1).toFixed(1);
        if(num<=0.2){
            return;
        }else{
            $(this).next().val(numnew);
            creditPriceAndFee()
        }
    });
    //手机号隐藏
    var tel = $('.tel-main').html();  
    var mtel = tel.substr(0, 3) + '****' + tel.substr(7);  
    $('.tel-main').text(mtel);  
    
    creditPriceAndFee();
    
	function creditPriceAndFee(){
		jQuery("#discountMoney").html((parseFloat(jQuery("#creditAccountValue").val())*(jQuery("#discount").val())).toFixed(2));
		var borrowNid = jQuery("#borrowNid").val();
		var tenderNid = jQuery("#tenderNid").val();
		jQuery.ajax({
			type: "POST",
			async: "async",
			url: webPath +"/bank/user/credit/expectcreditfee.do",
			contentType:"application/x-www-form-urlencoded;charset=utf-8",
			dataType: "json",
			data: {
				"borrowNid":borrowNid,
				"tenderNid":tenderNid,
				"creditDiscount":jQuery("#discount").val()
			},
			error: function(request) {
		        popupWin("连接服务器出错,请稍后重试");
		    },
			success: function(data){
				if(data.resultFlag==0){
					//assignInterest	预计收益 
					//assignInterestAdvance	垫付利息
					//assignPay	实付金额  
					//assignPayInterest	债转利息
					//creditAccount	债转本息
					//creditCapital	可转本金
					//creditInterest	债转期全部利息
					//creditPrice	折后价格
					jQuery("#creditFee").html((data.data.assignPay*0.01).toFixed(3).substring(0,(data.data.assignPay*0.01).toFixed(3).lastIndexOf('.')+3) );
					jQuery("#interest").html(data.data.expectInterest);
				}else{
					popupWin(data.msg);
				}
			}
		});
	}

})