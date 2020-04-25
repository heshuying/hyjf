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
            <h4>开通定向转账</h4>
        </div>
    </div>
    <div class="section direct">
        <div class="container-1084">
            <div class="direct-open-row">
                <div class="direct-open-col">
                    <div class="title">转出账户</div>
                    <dl>
                        <dt>用户名</dt>
                        <dd>${directForm.outUserName}</dd>
                        <dt>姓名</dt>
                        <dd>${directForm.outTureName}</dd>
                        <dt>手机</dt>
                        <dd>${directForm.outMobile}</dd>
                    </dl>
                </div>
                <div class="direct-open-col">
                    <form id="directForm" action="${ctx}/direct/bindUser.do">
                     <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
             		 <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
	     			 <input type="hidden" id="outUserId" name="outUserId" value="${directForm.outUserId}" />
	     			 <input type="hidden" id="inUserId" name="inUserId" value="" />
                    <div class="title">转入账户</div>
                    <dl>
                        <dt>用户名</dt>
                        <dd><input class="direct-input" autocomplete="off" name="inUserName" id="inUserName" placeholder="请输入定向转账账户用户名" value="" maxlength="30" type="text"></dd>
                        <dt>姓名</dt>
                        <dd id="viewname"></dd>
                        <dt>手机</dt>
                        <dd id="viewmobile"></dd>
                    </dl>
                    </form>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="btn-box">
                <a href="javascript:void;" class="btn confirm" id="directConfirm">确认</a>
                <a href="javascript:void;" class="btn cancel" id="cancel">取消</a>
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
    <script>
    var formstat = false;
    $("#directConfirm").on("click",function(){
    	//项目提交
    	usernameCheck();
    	if(formstat){
    		if(checkToken() == true){
    			$("#directForm").submit();
    		}
    	}
    })
    $("#inUserName").on("blur",function(){
    	$("#inUserName-error").remove();
    	usernameCheck();
    })
	function usernameCheck(){
		var url = webPath+"/direct/check.do";
		var data = {
				"inUserName":$("#inUserName").val()
		};
		$.ajax({
			"url" : url,
			"type": "POST",
			"async":false,
			"data":data,
			"success": function(data){
				if(data.error == '1'){
					var ipt = $("#inUserName");
					ipt.parent().append("<span id='inUserName-error' class='error'>"+data.data+"</span>");
					$("#viewname").text("");
					$("#viewmobile").text("");
					$("#inUserId").val("");
					formstat = false;
				}else{
					$("#viewname").text(data.truename);
					$("#viewmobile").text(data.mobile);
					$("#inUserId").val(data.userid);
					formstat = true;
				}
			},
			error : function(data){
				$("#viewname").text("");
				$("#viewmobile").text("");
				$("#inUserId").val("");
				formstat = false;
			}
		})
	}
    $("#cancel").click(function(){
    	window.location.href = document.referrer;
    });
    </script>
	</body>
</html>
