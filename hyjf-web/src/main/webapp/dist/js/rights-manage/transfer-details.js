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
				utils.alert({
     				id:"errPop",
     				type:"alert",
     				content:"连接服务器出错,请稍后重试"
     			});
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
    var creditForm = $("#creditForm").validate({
        onkeyup:false,
        ignore: ".ignore",
        rules: {
        	telcode:{
                required:true,
                rangelength:[4,8],
                remote : {
					url : webPath + "/bank/user/credit/checkcode.do",
					type : "post",
					data : {
						code : function() {
							return $("#telcode").val();
						},
					},
					beforeSend : function() {
						// var _checking =
						// $('#checking');
						// _checking.prev('.field_notice').hide();
						// _checking.next('label').hide();
						// $(_checking).show();
					},
					complete : function() {
						// $('#checking').hide();
					}
				}
            },
            lockable:{
                isUnlock:true,
            },
        },
        messages:{
        	telcode:{
        		required:'请输入验证码',
                rangelength:'请输入正确的验证码',
            	remote :'验证码不正确'
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
        },
        submitHandler: function(form) {
            var presetProps = JSON.stringify(sa.getPresetProperties())
            $("#presetProps").val(presetProps)
            // 判断是否开启债转  0 关闭   1开启
            if ($("#toggle").val() == "0") {
                utils.alert({
                    id: "errPop",
                    type: "alert",
                    content: $("#closeDes").val()
                });
                return false;
            }
            if ($("#paymentAuthOn").val() == "1" && $("#paymentAuthStatus").val() != "1") {
                utils.alert({
                    id: "authInvesPop",
                    type: "confirm",
                    content: "<div>交易过程中，会收取相应费用<br>请进行授权。</div><div class='status-box'><div class='off'>如：提现手续费，债转服务费等。</div></div>",
                    alertImg: "msg",
                    fnconfirm: function () {
                        window.location.href = webPath + "/bank/user/auth/paymentauthpageplus/page.do";
                    }
                });
                return false;
            }else if (checkToken()) {
                form.submit();
            }
        }
    });

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
	// 折让率下限
	var minDiscount = $("#concessionRateDown").val();
	// 折让率上限
	var maxDiscount = $("#concessionRateUp").val();
	//加的效果
    $(".add").click(function(){
        var n=$(this).prev().val();
        var num=parseFloat(parseFloat(n).toFixed(1));
        var numnew=parseFloat(num+0.1).toFixed(1);
        if(num>=maxDiscount){
            return;
        }else{
            $(this).prev().val(numnew);
            $("#creditVal").val(numnew)
            creditPriceAndFee();
       }
    });
            //减的效果
    $(".jian").click(function(){
    var n=$(this).next().val();
    var num=parseFloat(parseFloat(n).toFixed(1));
    var numnew=parseFloat(num-0.1).toFixed(1);
        if(num<=minDiscount){
            return;
        }else{
            $(this).next().val(numnew);
            $("#creditVal").val(numnew)
            creditPriceAndFee()
        }
    });
    //手机号隐藏
    var tel = $('.tel-main').html();  
    var mtel = tel.substr(0, 3) + '****' + tel.substr(7);  
    $('.tel-main').text(mtel);  
    
    creditPriceAndFee();
    
	function creditPriceAndFee(){
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
				"creditDiscount":jQuery("#creditVal").val()
			},
			error: function(request) {
		        utils.alert({
     				id:"errPop",
     				type:"alert",
     				content:"连接服务器出错,请稍后重试"
     			});
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
					// 预计本金折让金额
					jQuery("#discountMoney").html(data.data.creditPrice+"元");
					// 预计持有期收益
					jQuery("#interest").html(data.data.assignInterestAdvance+"元");
					// 预计服务费
					jQuery("#creditFee").html(data.data.creditFee+"元");
					// 预计到账金额
					jQuery("#moneyGet").html(data.data.expectInterest);
					
				}else{
					utils.alert({
		     				id:"errPop",
		     				type:"alert",
		     				content:data.msg
		     			});
				}
			}
		});
	}

})