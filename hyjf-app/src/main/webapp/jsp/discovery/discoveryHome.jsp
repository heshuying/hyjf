<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>热门活动</title>
	</head>
	<body class="bg_grey" style="font-size: 14px;">
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" id="testType" name="sign" value="${sign}" />
		<input type="hidden" id="token" name="token" value="${token}" />
		<input type="hidden" id="version" name="version" value="${version}" />
		<input type="hidden" id="userId" name="userId" value="${userId}" />
		<section class="discovery-top">
			<div class="discovery-top-1 fl"><a href="" class="discovery-a-1" style="display:inline-block;width:100%"><img src="${ctx}/img/discovery-1.jpg"/></a></div>
			<div class="discovery-top-2 fr">
				<a  href="#" class="hy-partnerShare" style="display:inline-block;width:100%">
					<div class="fl">
						<p>邀请好友</p>
						<span>与好友一起赚</span>
					</div>
					<img src="${ctx}/img/discovery-2.jpg" class="fl"/>
				</a>
			</div>
			<div class="discovery-top-3 fr">
				<a href="" class="discovery-a-2  ">
					<div class="fl">
						<p>风险测评</p>
						<span class="testType"></span>
					</div>
					<img src="${ctx}/img/discovery-3.jpg" class="fl"/>
				</a>
			</div>
		</section>
		<section class="discovery-bottom">
			<c:if test="${isVip}">
				<a href="" class="discovery-a-3  "><img src="${ctx}/img/discovery-list-1.jpg"/></a>
			</c:if>
			<a href="" class="discovery-a-4  "><img src="${ctx}/img/discovery-list-2.jpg"/></a>
			<a href="" class="discovery-a-5  "><img src="${ctx}/img/discovery-list-3.jpg"/></a>
			<a href="#" class="discovery-a-6 hy-jumpFeedback"><img src="${ctx}/img/discovery-list-4.jpg"/></a>
		</section>
		<section class="discovery-call">
			<p>客服电话<span class="fr"><a href="" class="getContactNumber"></a></span></p>
		</section>
	</body>
	<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
	<script>
		function changeFZ(){
    		var w = $(window).width();
    		setHtmlFz(w/10);
    	}
    	function setHtmlFz(fz){
    		$("html").css("font-size",fz+"px");
    	}
    	window.onresize = function(){
    		changeFZ();
    	}
    	$(function(){
    		changeFZ(); 
    	})
	</script>
	<script>
	//查询url参数
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	$(function(){
		var url = location.origin;
		var sign = getQueryString("sign");
		var token = $("#token").val();
		var version = '${version}';
		var platform = getQueryString("platform");
		$(".discovery-a-1").attr('href',hyjfArr.hyjf+'://jumpH5Encode/?{"url":"'+url+'/hyjf-app/act/actlist.do?sign='+sign+'&platform='+platform+'&token='+token+ '&version=' + version +'"}');
		//根据token判断是否登录
		if(token){
			$(".discovery-a-2").attr('href',hyjfArr.hyjf+'://jumpH5Encode/?{"url":"'+url+'/hyjf-app/financialAdvisor/init?sign='+sign+'&platform='+platform+ '&version=' + version +'"}');
		}else{
			$(".discovery-a-2").attr('href',hyjfArr.hyjf+'://jumpLogin/?');
		}
		$(".discovery-a-3").attr('href',hyjfArr.hyjf+'://jumpH5Encode/?{"url":"'+url+'/hyjf-app/vip/userVipDetailInit?sign='+sign+'&platform='+platform+ '&version=' + version +'"}');
		$(".discovery-a-4").attr('href',hyjfArr.hyjf+'://jumpH5Encode/?{"url":"'+url+'/hyjf-app/jsp/newcomer.jsp?sign='+sign+'&token='+token+ '&version=' + version +'"}');
		$(".discovery-a-5").attr('href',hyjfArr.hyjf+'://jumpH5Encode/?{"url":"'+url+'/hyjf-app/jsp/aboutUs.jsp?sign='+sign+'&platform='+platform+ '&version=' + version +'"}');
	})
	</script>
	<script>
	
	~function getAdvisorType(){
		var data = {};
		data.sign = $("#testType").val();
		$.ajax({
    		type:"post",
    		url:"/hyjf-app/discovery/getEvalationResult.do",
    		data:data,
    		datatype:"json",
    		async:false,
    		success:function(data){
    			console.info(12312312);
    			$(".testType").html(data.evalationResult)
    		}
    	})
	}(); 
	
	/* getAdvisorType() */
	</script>
	<script>
			/* 获取客服电话 */
			$.ajax({
				type:"get",
				data:null,
				dataType:"json",
				url:"${ctx}/homepage/getServicePhoneNumber",
				success:function(data){
					var dataNumber = data.servicePhoneNumber;
					if(!dataNumber){$(".discovery-call").hide()}
					var num1 = dataNumber.substr(0,3);
					var num2 = dataNumber.substr(3,3);
					var num3 = dataNumber.substr(6);
					var num = num1+"-"+num2+"-"+num3
					$(".getContactNumber").prop("href",hyjfArr.hyjf+'://callCenter/?{"number":"'+num+'"}')
					.text(num1+"-"+num2+"-"+num3)
				}
			})
		</script>
</html>