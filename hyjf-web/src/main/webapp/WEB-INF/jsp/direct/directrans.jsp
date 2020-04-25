<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>开通定向转账 - 汇盈金服官网</title>
	    <jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="banner-111" style="background-image:url(${cdn}/img/banner_recharge.jpg); ">
        <div class="container-1084">
            <h4>定向转账</h4>
        </div>
    </div>
    <div class="section direct">
        <div class="container-1084">
            <div class="direct-open-row">
                <div class="direct-open-col">
                    <div class="title">转出账户</div>
                    <dl>
                        <dt>用户名</dt>
                        <dd>${directForm.outUserName }</dd>
                        <dt>可用金额</dt>
                        <dd>${directForm.outUserBalance }元  </dd>
                        <dt>汇付账号</dt>
                        <dd>${directForm.outChinapnrUsrcustid }</dd>
                    </dl>
                </div>
                <div class="direct-open-col">
                    <div class="title">转入账户</div>
                    <dl>
                        <dt>用户名</dt>
                        <dd>${directForm.inUserName }</dd>
                        <dt>可用金额</dt>
                        <dd>${directForm.inUserBalance }元 </dd>
                        <dt>汇付账号</dt>
                        <dd>${directForm.inChinapnrUsrcustid }</dd>
                    </dl>
                    
                </div>
            </div>
            <form id="direcTransForm" action="${ctx}/direct/direcTrans.do">
                <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
             	<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
                <input type="hidden" id="userData" data-balance="${directForm.outUserBalance }"/>
                <input type="hidden" id="outUserId" name="outUserId" value="${directForm.outUserId}" />
                <input type="hidden" id="inUserId" name="inUserId" value="${directForm.inUserId}" />
                <input type="hidden" id="smsCodeId" name="smsCodeId" value="" />
                <input type="hidden" id="smsCode" name="smsCode" value="" />
                
                <div class="direct-input-box">
                    <dl>
                        <dt>转账金额</dt>
                        <dd><input class="direct-input" autocomplete="off" name="transAmt" id="transAmt" placeholder="请输入转账金额" value="" type="text" maxlength="12">  人民币（元）</dd>
                    </dl>
                    
                    <dl>
                        <dt>手机号码</dt>
                        <dd><input id="mobile" class="direct-input" autocomplete="off" name="mobile" placeholder="请输入手机号码" value="${directForm.outMobile }" disabled="disabled" maxlength="11" type="text"/>  
                        <input class="newRegGetVer" value="获取验证码" type="button" id="sendcode" /> 
                        </dd>
                    </dl>
                     <dl>
                        <dt>验证码</dt>
                        <dd><input class="direct-input" autocomplete="off" name="code" placeholder="请输入收到的验证码" value="" maxlength = "6" type="text"> </dd>
                    </dl>
                    
                </div>
            </form>
            <div class="btn-box">
                <a href="javascript:;" class="btn confirm" id="directConfirm">确认</a>
                <a href="javascript:;" class="btn cancel" id="cancel">取消</a>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    
    <div class="tipsRecharge">
        <div class="container-1084">
            <div class="title">定向转账说明</div>
            <ol>
                <li>什么是定向转账：用户之间进行账户余额的支付；</li>
                <li>定向转账有什么好处：可以减少用户充值费用，可以指定账户进行资金划拨；
                <li>定向转账适用范围：只有借款人账户可以使用；</li>
                <li>如何开通定向转账：用户可以在我的账户--账户总览-开通定向转账设置；</li>
            </ol>
        </div>
    </div>
		<jsp:include page="/footer.jsp"></jsp:include>	
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
    <script>
    var balance = parseFloat($("#userData").data("balance"));
    $.validator.addMethod("isTransNum", function(value, element) {
    	var reg = /^\d+\.?\d{0,2}$/;
    	return this.optional(element) || (reg.test(value));
    	}, "金额格式不正确"); 
    var validation = $("#direcTransForm").validate({
    	"rules":{
            "transAmt":{
                "required" : true,
                "isTransNum":true,
                "max":balance
            },
            "mobile":{
            	"required" : true,
            	"isMobile" : true
            },
            "code":{
            	"required":true,
            	"equalTo":"#smsCode"
            }
            
        },
        "messages":{
            "transAmt":{
                "required" : "请输入转账金额",
                "isTransNum":"金额格式不正确(两位小数)",
                "max":"转账金额超出可用金额"
            },
            "mobile":{
            	"required" : "请输入手机号码",
            	"isMobile" : "请输入正确的手机号码",
            	"remote":"手机号码不匹配"
            },
            "code":{
            	"required":"请输入验证码",
            	"equalTo":"请输入正确的验证码"
            }
        },
        //ignore : ".ignore",
        submitHandler : function(form) {
        	if(checkToken() == true){
            	form.submit();
        	}
        }
    })
   
    $("#directConfirm").on("click",function(){
    	$("#direcTransForm").submit();
    });
 
	function sendCode(btn){
		var url = webPath+"/direct/sendSmsCode.do";
		var data = {
				"mobile":$("#mobile").val()
		};
		var btn = $(".newRegGetVer");
		$.ajax({
			"url" : url,
			"type": "POST",
			"async":false,
			"data":data,
			"success": function(data){
				if(data.error == '1'){
					var ipt = $("#mobile");
					ipt.parent().append("<span id='mobile-error' class='error'>"+data.data+"</span>");
					formstat = false;
				}else{
					//获取验证码提示
					var clock = '';
					var nums = 60;
					btn.prop("disabled",true).val(nums+'秒后重新获取').addClass("phoneCodeAAA");
					clock = setInterval(doLoop, 1000); 
					function doLoop()
					{
						nums--;
						if(nums > 0){
							btn.val(nums+'秒后可重新获取');
						}else{
							clearInterval(clock); //清除js定时器
							btn.prop("disabled",false).val('点击发送验证码').removeClass("phoneCodeAAA");
							nums = 60; //重置时间
							
						}
					}
					$("#smsCode").val(data.code);
					$("#smsCodeId").val(data.smsCodeId);
					formstat = true;
				}
			},
			error : function(data){
				btn.prop("disabled",false).removeClass("phoneCodeAAA");
				showTip("提示", data.error, "error");
				formstat = false;
			},
			complete:function(){
				btn.prop("disabled",true).addClass("phoneCodeAAA");
			}
		})
	}
	//判断手机号是否正确
    function testPhoneNum(value) {    
	  var length = value.length;    
	  return (length == 11 && /^(((13[0-9]{1})|(14[5,7,9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test(value));    
	};
	function verIsMobile(value) {
	    //验证是否是手机号
	    var length = value.toString().length;
	    return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value);
	}
	//手机号正确后验证码才能显示正常
	$("input[name=mobile]").on("blur",function(){
		var _self = this;
		var _selfValue = _self.value;
		var checkPhone = verIsMobile(_selfValue);
		if(checkPhone){
			$(".newRegGetVer").removeClass("phoneCodeAAA");
		}else{
			$(".newRegGetVer").addClass("phoneCodeAAA");
		}
	})
	
	//点击发送验证码
	$(".newRegGetVer").click(function() {
		var _self = this;
		if($("#mobile").valid() && $("#transAmt").valid()){
			sendCode();
		}
	});
	
	$("#cancel").click(function(){
		window.location.href = document.referrer;
	});
    </script>
	</body>
</html>
