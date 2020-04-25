<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>${creditResult.data.borrowNid}项目详情 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<c:if test="${creditResult.resultFlag==0}">
		<div class="zr-start-detail">
		<jsp:include page="/subMenu.jsp"></jsp:include>
			<div class="container-1200">
				<h2>
					<span class="fl-l title">${creditResult.data.borrowName}</span> <span
						class="fl-l id">${creditResult.data.borrowNid}</span> <a href="${ctx}/bank/user/credit/usercreditlist.do" class="back fl-r">&lt;&lt;返回</a>
				</h2>
				<div class="info">
					<div class="item">
						<div class="num">
							<span><fmt:formatNumber value="${creditResult.data.creditAccount}" pattern="#,###" /></span>
						</div>
						<div class="title">债权本金（元）</div>
					</div>
					<div class="item">
						<div class="num highlight">
							<span>${creditResult.data.borrowApr}</span><em>%</em>
						</div>
						<div class="title">历史年回报率</div>
					</div>
					<div class="item">
						<div class="num">
						<c:if test="${creditResult.data.borrowStyle eq 'endday'}">
							<span class="day-count">${creditResult.data.borrowPeriod}</span><em>天</em>
						</c:if>
						<c:if test="${creditResult.data.borrowStyle ne 'endday'}">
							<span class="day-count">${creditResult.data.borrowPeriod}</span><em>个月</em>
						</c:if>
						</div>
						<div class="title">融资期限</div>
					</div>
					<div class="item">
	                    <div class="num"><span class="day-count">${creditResult.data.tenderPeriod}</span><em>天</em></div>
	                    <div class="title">持有期限</div>
	                </div>
					<div class="item">
						<div class="num">
							<span>${creditResult.data.lastDays}</span><em>天</em>
						</div>
						<div class="title">剩余期限</div>
					</div>
				</div>
			</div>
		</div>
		<div class="zr-start-section">
			<div class="container-1200">
				<h2 class="title">转让债权</h2>
				<form action="${ctx}/credit/savetendertocredit.do" id="creditForm" name="creditForm" method="post">
					<input type="hidden" name="borrowNid" id="borrowNid" value="${creditResult.data.borrowNid}" />
					<input type="hidden" name="tenderNid" id="tenderNid" value="${creditResult.data.tenderNid}" />
					<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" />
					<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" />
					<input type="hidden" name="presetProps" id="presetProps" value="">
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
							<th width="175">转让本金</th>
							<td width="160"><span class="highlight creditAccount">${creditResult.data.creditAccount}</span> 元</td>
							<td></td>
						</tr>
						<tr>
							<th>折让率</th>
							<td>
								<div class="perc-calc">
									<div class="ctrl-btn less">-</div>
									<div class="ctrl-number">
										<input type="text" value="0.5" maxlength="3" id="creditDiscount" name="creditDiscount" disabled>
										<div class="unit">%</div>
									</div>
									<div class="ctrl-btn more">+</div>
								</div>
							</td>
							<td>折让率0.5％~2％；折让率越高，成交机会越高</td>
						</tr>
						<tr>
							<th>转让价格</th>
							<td><span class="creditPrice">${creditResult.data.creditAccount}</span>元</td>
							<td>转让价格=本金金额-本金金额*折让率</td>
						</tr>
						<tr>
							<th>服务费(预计)</th>
							<td><span class="creditFee">${creditResult.data.creditAccount}</span>元</td>
							<td>成交金额的1.0%；将在交易成功时收取</td>
						</tr>
						<tr>
							<th>历史回报</th>
							<td><span class="interest">${creditResult.data.creditAccount}</span>元</td>
							<td>本金+本金持有期利息-本金*折让率-服务费</td>
						</tr>
					</table>
					<h2 class="title">手机验证</h2>
					<table border="0" cellpadding="0" cellspacing="0">
						<tr>
	                        <th width="175">验证码</th>
	                        <td width="160">
	                        	<input type="text" class="zr-start-ipt" name="vercode" id="vercode" maxlength="4" >
	                        	<span id="vercodeTip" class="error"></span>
	                        </td>
	                        <td><img src="${cdn}/credit/getcaptcha.do?v='+Math.random()" onclick="this.src = this.src+'?v='+Math.random();" width="120" height="30" title="点击重新加载验证码" alt="点击重新加载验证码"></td>
	                    </tr>
	                    <tr>
	                        <th>短信验证码</th>
	                        <td><input type="text" id="telcode" name="telcode" class="zr-start-ipt" maxlength="6"></td>
	                        <td>
	                            <input class="newzr-start-ver-code " value="获取验证码" type="button" id="phonecode" name="phonecode">
	                            <span id="telcodeTip" class="error"></span>
	                            <input type="hidden" name="phone" id="phone" value="13210002383">
	                        </td>
	                    </tr>
						<tr>
							<th colspan="3"><a class="confirm-btn btn avalible" id="confirmBtn">确认转让</a><span style="display:none;" class="confirm-btn btn avalible" id="confirmWait">正在提交...</span><a href="${ctx}/credit/usercreditlist.do" class="cancel-btn btn avalible">取消转让</a>
							</th>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</c:if>
	<c:if test="${creditResult.resultFlag!=0}">
		<script>
			window.history.go(-1);
		</script>
	</c:if>
		<div class="settlement_mask"></div>
	<div class="settlement js_zr js_zra">
		<a class="zr_close js_close" href="javascript:void(0)"
			onclick="popoutWin()">×</a>
		<div class="qr_main">
			<h3>提示信息</h3>
			<h4 class="zr_pay" id="popmsg">提交成功</h4>
			<div class="btns">
				<button type="button" onclick="popoutWin()">确定</button>
			</div>
		</div>
	</div>
	<script>setActById('userCredit');</script>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function creditPriceAndFee(){
			jQuery(".creditPrice").html((parseFloat(jQuery(".creditAccount").html())*(1-jQuery("#creditDiscount").val()/100)).toFixed(2));
			var borrowNid = jQuery("#borrowNid").val();
			var tenderNid = jQuery("#tenderNid").val();
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/bank/user/credit/expectcreditfee.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"borrowNid":borrowNid,
					"tenderNid":tenderNid,
					"creditDiscount":jQuery("#creditDiscount").val()
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
						jQuery(".creditFee").html((data.data.assignPay*0.01).toFixed(3).substring(0,(data.data.assignPay*0.01).toFixed(3).lastIndexOf('.')+3) );
						jQuery(".interest").html(data.data.expectInterest);
					}else{
						popupWin(data.msg);
					}
				}
			});
		}
	</script>
	<script>
		var less = $(".less");
		var more = $(".more");
		var step = 0.1;
		creditPriceAndFee();
		less.click(function() {
			percCtrl("less");
			creditPriceAndFee();
		});
		more.click(function() {
			percCtrl("more");
			creditPriceAndFee();
		});
		$("#creditDiscount").keyup(function() {
			var val = $(this).val();
			$(this).val(validPerc(val));
			creditPriceAndFee();
		})
		function percCtrl(action) {
			//百分比控制
			var ipt = $("#creditDiscount");
			var percVal = parseFloat(ipt.val());
			if (action === "more" && percVal < 2) {
				var res = parseFloat(percVal += step).toFixed(1);
				ipt.val(validPerc(res));
			} else if (action === "less" && percVal > 0.5) {
				var res = parseFloat(percVal -= step).toFixed(1);
				ipt.val(validPerc(res));
			}
		}
		function validPerc(val) {
			//验证百分比
			var res;
			if (isNaN(val)) {
				res = "0.5";
			} else if (val > 2) {
				res = "2.0";
			} else if (val < 0.5) {
				res = "0.5";
			} else {
				res = val;
			}
			return res;
		}
		function popupWin(msg) {
			$('.settlement_mask').fadeIn();
			$('.js_zr').fadeIn();
			$("#popmsg").text(msg);
		}
		function popoutWin() {
			$('.settlement_mask').fadeOut();
			$('.js_zr').fadeOut();
		}
	</script>
</body>
<script type="text/javascript">
$(document).ready(function(){
	//验证码失去焦点事件
	jQuery("#vercode").blur(function(){
		var vercode = jQuery(this).val();
		if(vercode!=null && vercode!=""){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/bank/user/credit/checkcaptcha.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"captchaCode":vercode
				},
				error: function(request) {
			        popupWin("连接服务器出错,请稍后重试");
			    },
				success: function(data){
					if(data){
						jQuery("#vercodeTip").html("");
					}else{
						jQuery("#vercodeTip").html("验证码错误,请重新输入");
					}
				}
			});
		}else{
			jQuery("#vercodeTip").html("请输入验证码");
		}
		
	});
	
	//获取手机验证码点击事件
	jQuery("#phonecode").click(function(){
		if($("#phonecode").prop("disabled") == true){
			return false;
		}
		if(jQuery("#vercode").val()!=null && jQuery("#vercode").val()!=""){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/bank/user/credit/sendcode.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"captcha":jQuery("#vercode").val()
				},
				error: function(request) {
			        popupWin("连接服务器出错,请稍后重试");
			    },
				success: function(data){
					if(data.resultFlag==0){
						sendCode();
						//popupWin("手机验证码已经发送完成");
					}else{
						jQuery("#telcodeTip").html(data.msg);
					}
				}
				
			});	
		}else{
			jQuery("#vercodeTip").html("请输入验证码");
		}
	});
	
	//短信验证码失去焦点事件
	jQuery("#telcode").blur(function(){
		var telcode = jQuery(this).val();
		if(vercode!=null && vercode!=""){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/bank/user/credit/checkcode.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"code":telcode
				},
				error: function(request) {
			        popupWin("连接服务器出错,请稍后重试");
			    },
				success: function(data){
					if(data){
						jQuery("#telcodeTip").html("");
					}else{
						jQuery("#telcodeTip").html("验证码错误,请重新输入");
					}
				}
			});
		}else{
			jQuery("#telcodeTip").html("请输入验证码");
		}
		
	});
	
	//债转提交保存
	jQuery("#confirmBtn").click(function(){
		$("#presetProps").value= JSON.stringify(sa.getPresetProperties())
		jQuery("#confirmBtn").hide();
		jQuery("#confirmWait").show();
		if($("#paymentAuthOn").val() == "1" && $("#paymentAuthStatus").val() != "1"){
	    	utils.alert({
	            id: "authInvesPop",
	            type:"confirm",
	            content:"<div>应合规要求，出借、提现等交易前需<br>进行以下授权：</div><div class='status-box'><div class='off'>自动投标，自动债转，服务费授权。</div></div>",
	            alertImg:"msg",
	            fnconfirm: function(){
	                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/authRequirePage.do";
	            }
	        });
	    	return false;
	    }else{
			$.ajax({
				cache: true,
				type: "POST",
				url:"${ctx}/bank/user/credit/savetendertocredit.do",
				data:$('#creditForm').serialize(),// 你的formid
				async: "async",
			    error: function(request) {
			        popupWin("连接服务器出错,请稍后重试");
			        jQuery("#confirmWait").hide();
			    	jQuery("#confirmBtn").show();
			    },
			    success: function(data) {
				    if(data.resultFlag==0){
				    	//跳转到投资列表
				    	window.location.href="${ctx}/bank/user/credit/tendertocreditresult.do?creditNid="+data.data;
				    }else{
				    	popupWin(data.msg);
				    	jQuery("#confirmWait").hide();
				    	jQuery("#confirmBtn").show();
				    }
			    }
			});
		});
	}
	
})

//获取验证码
var clock = '';
var nums = 60;
function sendCode()
{	
	var btn = document.getElementById('phonecode');
	btn.disabled = true; //将按钮置为不可点击
	btn.style.color = "#d6d6d6";
	btn.style.borderColor = "#d6d6d6";
	btn.style.cursor = "not-allowed";
	btn.value = nums+'秒后可重新获取';
	clock = setInterval(doLoop, 1000); //一秒执行一次
}
function doLoop(){
    nums--;
    var btn = document.getElementById('phonecode');
    if(nums > 0){
        btn.value = nums+'秒后可重新获取';
    }else{
        clearInterval(clock); //清除js定时器
        btn.disabled = false;
        btn.value = '点击发送验证码';
        nums = 60; //重置时间
        btn.style.color = "#cda972";
        btn.style.borderColor = "#cda972";
        btn.style.cursor = "pointer"
    }
}
</script>
</html>