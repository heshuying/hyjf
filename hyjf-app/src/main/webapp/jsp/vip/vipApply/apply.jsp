<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/club.css">
<title>会员Club</title>
</head>
<body>
<input id="sign" name="sign" type="hidden" value="${sign}"/> 
<input id="platform" name="platform" type="hidden" value="${platform}"/> 
<input id="version" name="version" type="hidden" value="${version}"/>
  	<div class="vip-main response">
  		<div class="vip-1"><img src="${ctx}/img/vip-1.jpg"/>
  		</div>
  		<div class="vip-2">
  		<div class="vip-join-now border-radius3">
	  			<c:choose>
	            <c:when test="${!vipFlg }">
	            	会员CLUB升级中，敬请期待
	            </c:when>
	            <c:otherwise>
	            	<a href="javascript:;" id="vipDetailBtn">查看当前等级</a>
	            </c:otherwise>
            </c:choose>
	  		</div>
  			<img src="${ctx}/img/vip-2.jpg"/>
  		</div>
  		<div class="vip-3"><img src="${ctx}/img/vip-3.jpg"/></div>
  		<div class="vip-4"><img src="${ctx}/img/vip-4.jpg"/></div>
  		<div class="vip-5"><img src="${ctx}/img/vip-5.jpg"/></div>
		<div class="vip-1"><img src="${ctx}/img/vip-6.jpg"/></div>
  		<div class="vip-2"><img src="${ctx}/img/vip-7.jpg"/></div>
  		<div class="vip-3"><img src="${ctx}/img/vip-8.jpg"/></div>
  		<div class="vip-4"><img src="${ctx}/img/vip-9.jpg"/></div>
  		<div class="vip-5"><img src="${ctx}/img/vip-10.jpg"/></div>
  	</div>
  	
    <!-- 弹出层  -->
  	<div class="balance-serv-tel">
    	<div class="balance-serv-bg"></div>
    	<div class="balance-serv-cont">
    		<div class="sevice-cont">
    			<p>
    				开通汇付天下资金托管账户，领取<br />
    				会员超值礼包
    			</p>
    			<div class="serv-sure">
    				<ul>
    					<li class="serv-sure-btn"><a href="javascript:;" id="operationBtn">立即开通</a></li>
    				    <li class="service-line"></li>
    				    <li class="serv-cancel-btn">我知道了</li>
    				</ul>
    			</div>
    		</div>
    	</div>
    </div>
     <!-- 弹出层2  -->
  	<div class="balance-serv-tel2 hide">
    	<div class="balance-serv-bg"></div>
    	<div class="balance-serv-cont">
    		<div class="sevice-cont">
    			<p>
    				会员重复购买，请刷新页面！
    			</p>
    			<div class="serv-sure">
    				<ul>
    				    <li class="serv-cancel-btn2">我知道了</li>
    				</ul>
    			</div>
    		</div>
    	</div>
    </div>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript">
  	$(function(){
			var width = $(window).width();
			var divWidth = $(".vip-join-now").width();
			var divHeight = $(".vip-join-now").height();
			divWidth = divWidth < 215 ? 215 : divWidth;
			var left = (divWidth+10)/2 ;
			
			$(".vip-join-now").css("margin-left",-left+"px");
			//$(".vip-join-now").css("line-height",divHeight+"px");
			$('.serv-cancel-btn').on("click",function(){
				$('.balance-serv-tel').hide();
			});
			$('.serv-cancel-btn2').on("click",function(){
				$('.balance-serv-tel2').hide();
			});
			applyVipBtnInit();
			vipDetailBtnInit();
		});
		
	/**
	 * vip申请按钮初始化
	 */
	function applyVipBtnInit(){
		$("#applyVipBtn").click(function(){
			$.ajax({
				type : "POST",
				async: false,
				url : "${ctx}/vip/apply/applyCheck",
				dataType : 'json',
				data : {
					"sign" : $('#sign').val(),
					"platform" : $('#platform').val(),
					"version" : $('#version').val()
				},
				success : function(data) {
					if (data.status == '0') {
						// 校验成功
						// 跳转到汇付转账页面
						window.location.href = "hyjf://jumpH5/?{'url':'"+data.request+"'}";
					} else {
						// 校验失败
						if(data.errorCode=='1'){
							// 未登录跳转到登录页面
							window.location.href = data.request;
						}else if(data.errorCode=='2'){
							// 未开户
							$("#operationBtn").click(function(){
								// 跳转到开户页面
								window.location.href = "hyjf://jumpH5/?{'url':'"+data.request+"'}";
							});
							$('.balance-serv-tel').show();
						}else if(data.errorCode=='3'){
							$("#operationBtn").click(function(){
								// 跳转到充值页面
								window.location.href = data.request;
							});
							$('.balance-serv-tel').find("p").text("可用余额不足");
							$('.balance-serv-tel').find("#operationBtn").text("立即充值");
							// 余额不足
							$('.balance-serv-tel').show();
						}else if(data.errorCode=='4'){
							// vip重复购买
							$('.balance-serv-tel2').removeClass("hide").show();

						}
						
					}
				},
				error : function() {
					console.info("回调失败");
				}
			});
		});
	}
	
	/**
	 * vip查看按钮初始化
	 */
	function vipDetailBtnInit(){
		$("#vipDetailBtn").click(function(){
			// 跳转到汇付转账页面
			window.location.href = "${ctx}/vip/userVipDetailInit?sign="+$("#sign").val();
		});
	}
</script>
</html>