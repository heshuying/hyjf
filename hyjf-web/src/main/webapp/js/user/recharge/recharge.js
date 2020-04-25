var stageNum; //暂存要删除的银行卡
$(document).ready(
		function() {
			//火狐浏览器刷新会保留原值
        	$("#bankCode").val("");
        	$("#rechargeType").val("0");
        	
			var rech = $('.irech');
		    var bank = rech.find('.select-bank'); //选择银行
		    var bdir = bank.find('.banks-dir');
		    var mode = rech.find('.rech-mode'); //充值方式
		    var mdir = mode.find('.banks-dir');
		    var mfee = 0; //费率
		    var State = true;
		    var targetClass = function(a, b) {
		        a.addClass('on').siblings().removeClass('on');
//		        b.find('.iconfont').hide();
//		        a.find('.iconfont').show();
		    };
		    
		    /**
		     * 充值金额输入框失去焦点
		     */
//		    $('#chargeSum').on('blur',function(){
//		    	charge();
//		    });
		    
		    /**
		     * 充值金额输入框键盘事件
		     */
		    $('#chargeSum').on('keyup',function(event){
		    	updateCharge(event);
		    });

		    /**
		     * 网关充值单击事件
		     */
		    $('#netBank').on('click', function() {
		    	var bCodeVal = $("#person-bank").find(".banks-dir.on").attr("id") || "";
		        $("#bankCode").val(bCodeVal);
		        $('#rechargeType').val('0');//将充值类型设置为0
		        var tar = $(this);
		        targetClass(tar, mdir);
		        $('#person-bank').show();//展示元素
		        $('#bank-desc').show();//展示元素
		        $('#corp-bank').hide();//企业网银隐藏
		        $('#online-bank').hide();//隐藏快捷充值规则说明
		        //charge();
		    });
		    /**
		     * 企业充值单击事件
		     */
//		    $('#netCorBank').on('click', function() {
//		    	var bCodeVal = $("#corp-bank").find(".banks-dir.on").attr("id") || "";
//		        $("#bankCode").val(bCodeVal);
//		        $('#rechargeType').val('1');//将充值类型设置为0
//		        var tar = $(this);
//		        targetClass(tar, mdir);
//		        $('#corp-bank').show();//展示元素
//		        $('#bank-desc').show();//展示元素
//		        $('#person-bank').hide();//个人网银隐藏
//		        $('#online-bank').hide();//隐藏快捷充值规则说明
//		        charge();
//		    });
		    /**
		     * 快捷支付充值单击事件
		     */
		    $('#onlinebank').on('click', function() {
		        $('#rechargeType').val('2');
		        $("#chargeSum").removeAttr("readonly");
		        var tar = $(this);
		        targetClass(tar, mdir);
		        $('#person-bank').hide();
		        $('#corp-bank').hide();//企业网银隐藏
		        $('#bank-desc').hide();
		        $('#online-bank').show();
		        charge();
		        $("#chargeSum").val() == "" || $("#chargeSum").valid();
		        
		    });

//		    mdir.click(function() {
//		        var tar = $(this);
//		        targetClass(tar, mdir);
//		        if (tar.hasClass('netBank')) {
//		            $('#selectBanks').show();
//		            var selected_bank = $(".select-bank .cur");
//		        } else {
//		            $('#selectBanks').hide();
//		        }
//		        
//		    });

//		    bdir.click(function() {
//		        var tar = $(this);
//		        targetClass(tar, bdir);
//		        $("#bankCode").val(tar.attr('id'));
//		        charge();
//		        $("#chargeSum").val() == "" || $("#chargeSum").valid();
//		        //getBanksDesc($(this).attr('id'));
//		    });
//
//		    if (bdir.length) {
//
//		    };
//		    bank.find('li:lt(5)').show();

//		    if (bdir.hasClass('on')) {
//		    	//getBanksDesc(bdir.filter('.on').attr('id'));
//		    };

		    function charge() {
		        if ($('#rechargeType').val() == '2') {//如果充值类型是快捷充值
		            var chargeSum = 0;
		          //  if ($('#chargeSum').val() != '') {//如果充值金额文本框有数值
		                chargeSum = $('#chargeSum').val();
		                //提交表单，获取手续费和实际到账金额
		                doRequest(rechargeInfoSuccessCallback, rechargeInfoErrorCallback, webPath + "/bank/web/user/recharge/rechargeInfo.do", $('#loginForm').serialize(), false, null, null);
		           // }
		        }else if($('#rechargeType').val() == '1') {//如果是企业用户充值
		            var chargeSum = 0;
		           // if ($('#chargeSum').val() != '') {//如果充值金额文本框有数值
		                chargeSum = $('#chargeSum').val();
		                //提交表单，获取手续费和实际到账金额
		                doRequest(rechargeInfoSuccessCallback, rechargeInfoErrorCallback, webPath + "/recharge/rechargeInfo.do", $('#loginForm').serialize(), false, null, null);
		           // }
		        }else {//如果是网关充值
		            var chargeSum = 0;
		           // if ($('#chargeSum').val() != '') {//如果充值金额文本框有数值
		                chargeSum = $('#chargeSum').val();
		                if(chargeSum==""){
		                	chargeSum="0";
		                }
		                //提交表单，获取手续费和实际到账金额
		                doRequest(rechargeInfoSuccessCallback, rechargeInfoErrorCallback, webPath + "/recharge/rechargeInfo.do", $('#loginForm').serialize(), false, null, null);
		           // }
		        }
		    }
		    
		    /**
		     * 获取充值信息成功回调 
		     */
		    function rechargeInfoSuccessCallback(data){
		    	$('#curBalance').html(data.fee);
		    	$('#sumBalance').html(data.balance);
		    }
		    
		    /**
		     * 获取充值信息失败回调
		     */
		    function rechargeInfoErrorCallback(data){
		    	if(data.errorCode == "707"){
		    		window.location.href = webPath + "/user/login/init.do";
		    	}else if(data.errorCode == "708"){
		    		window.location.href = webPath + "/user/openaccount/init.do";
		    	}
		    }


		    function updateCharge(event) {
		        if (event.keyCode == 37 || event.keyCode == 39) {
		            return false;
		        }
		        if ($('#chargeSum').val() < 1) {
		            $('#chargeSum').val("");
		        }
		        $('#chargeSum').val($('#chargeSum').val().replace(/[^0-9]/g,''));
		        //charge();
		    }
		    $.validator.addMethod("bankRequired", function(value, element, params) {
		        return $("#bankCode").val() != "";
		    }, "请选择银行");
		    var validation = $("#loginForm").validate({
		        "rules":{
		            "money":{
//		            	"bankRequired":{
//		                	depends:function(){
//		                		return $("#rechargeType").val() == 0 || $("#rechargeType").val() == 1
//		                	}
//		                },
		                "required" : true,
		                "number": true,
		                "min":1
		                
		            }
		        },
		        "messages":{
		            "money":{
//		            	"bankRequired":"请选择银行",
		                "required" : "请填写充值金额",
		                "number": "金额必须是数字",
		                "min":"充值金额必须为大于等于 1.00 元"
		                
		            }
		        },
		        submitHandler : function(form) {
		        	if (checkToken() == true) {
		        		form.submit();
					}
				}
		    })
		    
		    $("#unbundling").click(function(){
		    	
	    		var content = "您确认要解绑这张银行卡吗？";
	    		var txt = {
	    				"confirm":"确认",
	    				"cancel":"我再想想"
	    		}
	    		showTip(null, content, "confirm", "unbundlingTip",txt);
	    		
	    	})
	    	function dealAction(id){
	    		if(id == "unbundlingTip"){
	    			stageNum = $(this).parent(".item").data("num");
	    			window.location.href = webPath + "/bank/web/deleteCard/deleteCard.do?cardId="+stageNum;
	    		}
	    	}
	    	//解绑成功提示
	    	function unbindSuccess(){
	    		var content = "恭喜您！ 您的普通银行卡删除成功";
	    		showTip(null, content, "tip", "unbundlingSuccess");
	    	}
	    	//解绑失败提示
	    	function unbindFailed(){
	    		var content = "抱歉！您的普通提现卡删除失败，<br/>	请与客服人员联系。";
	    		showTip(null, content, "tip", "unbundlingFailed");
	    	}
			
		});
