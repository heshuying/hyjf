<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>相关协议</title>
		<style>
			body{
				width:100%;
				height:100%;
				background-color: #f3f3f3;
			}
			.agreement-items{
				margin-top:.6989rem;
			}
			.agreement-item{
			  border-top:1px solid rgb(229, 229, 229);
			  border-bottom:1px solid rgb(229, 229, 229);
			  background-color: rgb(255, 255, 255);
			  width: 100%;
  				height: 1.79rem;
			  z-index: 60;
			  margin-top:.1475rem
			}
			.agreement-item a{
			  font-size: .42rem;
			  font-family: "FZLTHJW";
			  color: rgb(73, 167, 246);
			  line-height: 1;
			  text-align: left;
			  z-index: 62;
			  display:block;
			  margin-top:.6914rem;
			  margin-left:.6761rem;
			  }
			  .footer-tips{
			  display:block;
			  width:100%;
			  font-size: .42rem;
			  font-family: "FZLTHJW";
			  color: rgb(137, 137, 137);
			  position: absolute;
			  bottom: 1.3389rem;
			  text-align:center;
			  }
		</style>
	</head>
	<body>
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<div class="agreement-items">
			<div class="agreement-item">
				<a href="javascript:;" id="goHjhInfo">《投资服务协议》</a>
			</div>
			<div class="agreement-item">
				<a href="javascript:;" id="goConfirmationOfInvestmentRisk">《投资风险确认书》</a>
			</div>
			<div class="agreement-item">
				<a href="javascript:;" id="goHjhDiaryAgreement">《借款协议》</a>
			</div>
		</div>
		<span class="footer-tips">市场有风险，投资需谨慎</span>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
	        document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	        $(window).on('resize', function () {
	            document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	        });
	        
	        /**
	        **三个协议跳转对应的链接 
	        **/
	        var jumpcommend = '${jumpcommend}';
	        var ctx = '${ctx}';
	        var borrowNid = '${borrowNid}';
	        var accedeOrderId = '${accedeOrderId}';
	        var sign = '${sign}';
	        var userId = '${userId}';
	        var recoverAccount = '${recoverAccount}';
	        $("#goHjhInfo").prop("href", jumpcommend+"://jumpH5/?{'url':'"+location.origin+ctx+"/hjhagreement/hjhInfo.do?planNid="+borrowNid+"&accedeOrderId="+accedeOrderId+"&sign="+sign+"'}");
	        $("#goConfirmationOfInvestmentRisk").prop("href",jumpcommend+"://jumpH5/?{'url':'"+location.origin+ctx+"/hjhagreement/confirmationOfInvestmentRisk.do'}" );
	        $("#goHjhDiaryAgreement").prop("href", jumpcommend+"://jumpH5/?{'url':'"+location.origin+ctx+"/hjhagreement/hjhDiaryAgreement.do?borrowNid="+borrowNid+"&accedeOrderId="+accedeOrderId+"&userId="+userId+"&account="+recoverAccount+"'}");
    </script>
		
	</body>
</html>