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
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.bxslider.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/idangerous.swiper.css"/>
		<title></title>
	</head>
	<body>
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<div class="back_img"><div><img src="" alt="" /><span></span></div></div>
		<div class="openPop"></div>
		<div class="openPopDiv">
			<div>
				<h5>开通资金安全托管账户</h5>
				<img src="${ctx}/img/openAccount.png" alt="" class="openAccountImg">
				<p class="tac text_grey">汇盈金服使用汇付天下的第三方资金托管服务，投资更安全！</p>
				<span class="openPopDivSpan"><a class="colorOrange" href="">马上开通</a></span>
				<span class="openPopDivClose">稍后开通 </span>			</div>
		</div>
		<div class="specialFont response detail_container">
		<input type="hidden" name="platform" id="platform" value= "${platform}"/>
		<c:if test="${not empty token}">
			<input type="hidden" name="token" id="token" value= "${token}"/>
			<input type="hidden" name="openAccountUrl" id="openAccountUrl" value= "${openAccountUrl}"/>
			<input type="hidden" name="mobile" id="mobile" value= "${mobile}"/>
		</c:if>	
		<c:if test="${not empty consumeUrl}">
			<input type="hidden" name="consumeUrl" id="consumeUrl" value= "${consumeUrl}"/>
		</c:if>
		<input type="hidden" name="sign" id="sign" value= "${sign}"/>
		<input type="hidden" name="randomString" id="randomString" value= "${randomString}"/>
		<input type="hidden" name="projectType" id="projectType" value= "${projectDeatil.type}"/>
		<input type="hidden" name="order" id="order" value= "${order}"/>
			<!--列表详情头部开始-->
			<div class="detailHead">
                <span class= "detailHeadLeft">
					<i class="orange"><b>
					${projectDeatil.borrowApr}
					</b>%</i>
					<em>历史年回报率</em>
				</span>
				<span class= "detailHeadMiddle">
					<i>
					<b>
					${projectDeatil.borrowPeriod}
					</b></i>
					<em>项目期限</em>
				</span>
                <span class= "detailHeadRight">
					<i><b>
					${projectDeatil.investAccount}
					</b>元</i>
					<em>项目剩余</em>
				</span>
				<c:if test="${projectDeatil.status eq 10}">
				<span class="startDate">
					${projectDeatil.onTime}开标
					</span>
				</c:if>
				<c:if test="${projectDeatil.status ne 10}">
				<span class="process">
					<i>
						<b></b>
					</i>
					<small>已投资<span id= "centNum">${projectDeatil.borrowSchedule}</span>%</small>
				</span>
				</c:if>
				<div class="clearBoth"></div>
			</div>
			<!--列表详情头部结束-->			
			<div class="grayHeight"></div>			
			<!--列表详情内容开始-->
			<div class="detailContent">
				<ul>
					<li>
						<span class="left">项目编号：</span>
						<span class="right">${projectDeatil.borrowNid }</span>
					</li>
					<li>
						<span class="left">债权本金：</span>
						<span class="right">${projectDeatil.account}元</span>
					</li>
					<li>
						<span class="left">折价比例：</span>
						<span class="right">0.0%</span>
					</li>
					<li>
						<span class="left">还款方式：</span>
						<span class="right return-type">${projectDeatil.repayStyle }</span>
					</li>
					<li>
						<span class="left">原项目：</span>
						<span class="right"><a href="">HZR2112999229 &gt;</a></span>
					</li>
					<li>
						<span class="left">转让时间：</span>
						<span class="right">2016-06-06 10:12:12</span>
					</li>
					<li>
						<span class="left">截止时间：</span>
						<span class="right">2016-06-06 10:12:12</span>
					</li>
				</ul>
				<div class="clearBoth"></div>
			</div>
			<div class="grayHeight"></div>			
			<!--tab开始-->
			<div class="tabTitle">
				<span class="active" id="tab1">${projectDeatil.tabOneName }<i class="detail_line"></i></span>
				<span id="tab4">${projectDeatil.tabFourName }</span>
			</div>
			<div class="tabContents">
				<!--项目信息开始-->
				<div class="tabItem active" id="infoOneId">
					<p class="pd015">此项目为“汇直投”债权转让服务。</p>
					<p class="pd015">债权转让达成后，债权拥有者将变更为新投资人，担保公司将继续对借款人的借款承担连带担保责任。</p>
					<c:if test="${not empty repayPlanList}">
					<c:if test="${projectDeatil.status ne 15}">
					<div class="item">
					<i class="icon-star"></i>
						<span>还款计划</span>
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td>时间</td>
								<td>还款金额(元)</td>
								<td>类型</td>
							</tr>
							<c:forEach items="${repayPlanList}" var="item" varStatus="vs">
								<tr>
									<td><c:out value="${item.repayTime}"></c:out></td>
									<td><c:out value="${item.repayTotal}"></c:out></td>
									<td><c:out value="${item.repayType}"></c:out></td>
								</tr>
							</c:forEach>
						</table>
					</div>
					</c:if>
					</c:if>
				</div>
				<!--项目信息结束-->				
				<!--投资记录开始-->
				<div class="tabItem" id="infoFourId">
					<div class="tac">
						<img src="${ctx}/img/loadingImg.gif" alt="" class="loadingImg">
					</div>
				</div>
				<!--投资记录结束-->
			</div>
			<div class="grayHeight"></div>
			<!--tab结束-->
			<!--列表详情内容结束-->			
			<!--列表详情底部开始-->
			<div class="foot-left"><a href=""><img src="${ctx}/img/cal.png"/></a></div>
			<div class="foot bg_grey_new">
				<!-- 定时发标 -->
				<c:if test="${projectDeatil.status eq 10}">
					<span>${projectDeatil.onTime}开标</span>
				</c:if>
				<!-- 投资中 -->
				<c:if test="${projectDeatil.status eq 11}">
					<span class="btn_bg_color textWhite"><a id="investNow"  class="textWhite">立即投资</a></span>
				</c:if>
				<!-- 复审中 -->
				<c:if test="${projectDeatil.status eq 12}">
					<span>复审中</span>
				</c:if>
				<!-- 还款中 -->
				<c:if test="${projectDeatil.status eq 13}">
					<span>还款中</span>
				</c:if>
				<!-- 已还款 -->
				<c:if test="${projectDeatil.status eq 14}">
					<span>已还款</span>
				</c:if>
				<!-- 已流标 -->
				<c:if test="${projectDeatil.status eq 15}">
					<span>已流标</span>
				</c:if>
			</div>
			<!--列表详情底部结束-->
		</div>	
		<script src="${ctx}/js/doT.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
		
		<script type="text/javascript">
		//设置计算器URL
		$(function(){
				var urlCurrent = window.location.host+"/hyjf-app/jsp/cal.jsp";
				var types = ["按月计息，到期还本还息","按天计息，到期还本还息","先息后本","等额本息","等额本金","按月计息，到期还本息","按天计息，到期还本息"]
				var percent = parseInt($(".detailHeadLeft b").text());
				var time = parseInt($(".detailHeadMiddle").text());
				var type = $.trim($(".return-type").text())
				for(var i = 0;i<5;i ++){
					if(types[i]==type){
						var calType = i;
					}
				}
				if(!calType){
					for(var j = 0;j<2;j ++){
						if(types[j+5]==type){
							var calType = j;
						}
					}
				}
				$(".foot-left a").prop("href",hyjfArr.hyjf+'://jumpH5/?{"url":"http://'+urlCurrent+'?percent='+percent+'&time='+time+'&calType='+calType+'"}')
				//设置图片居中
				var leftFootHeight = ($(".foot-left").height()-25)/2;
				$(".foot-left img").css("margin-top",leftFootHeight+"px")
				//设置计算器背景色
				if($(".foot span").hasClass("btn_bg_color")){
					$(".foot-left").css("background","#eb6100")
				}else{
					$(".foot-left").css("background","#999999")
				}
		})
    </script>    
	<script id="tmpl-fourData" type="text/x-dot-template">
{{? it.investList!=0}}
					<div class="item" id="moreTable2">
						<table border="0" cellspacing="0" cellpadding="0" class="borderNone" >
							<tr>
								<th></th>
								<th>用户名</th>
								<th>投资金额（元）</th>
								<th>投资时间</th>
							</tr>
							
							{{~ it.investList : v : index}}
								<tr>
<td class="padding-0">{{? v.vipId!=0}}<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>{{?}}</td>
            						<td>{{=v.userName}}</td>            						<td>{{=v.account}}</td>
            						<td>{{=v.investTime}}</td>
        						</tr>
							{{~}}
							
						</table>
						{{if(it.investListTotal>10){}}
						<div class="loadMore" >加载更多...</div>
						{{}else{}}
						<div></div>
						{{}}}
					</div>
					{{??}}<p class="tac">无投资记录</p>
{{?}}
	
	</script>
	<script>
	//no.4 load more
	$("#infoFourId").delegate(".loadMore","click",function(){
		var $page = Math.floor(($("#moreTable2 table tr").length-1)/10)+1;
		var $borrowNid = "${projectDeatil.borrowNid}";
		var $platform="${platform}";
		var $token="${token}"; 
		var $sign="${sign}";
		var $randomString="${randomString}";
		var $order="${order}";
			 if($token){		
				data = {borrowNid:$borrowNid,page:$page,platform:$platform,token:$token,sign:$sign,randomString:$randomString,order:$order};
			}else{
				data ={borrowNid:$borrowNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order};
			} 
			 $.ajax({
					type:"get",
			 		url:"${projectDeatil.tabFourUrl}",
					data:data,
					datatype:"json",
					success:function(data){
						var totalPage = Math.floor(data.investListTotal/10)+1;
						if(!data){
							$("#moreTable2 .loadMore").hide();
						}else if(data&&data.status=="0"){
							var str="";
							for(var i=0;i<data.investList.length;i++){
								/*加判断  */
								if(data.investList.vipId!=0){
									var proImg = '<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>';
								}
								str=str+"<tr><td>"+proImg+"</td><td>"+data.investList[i].userName+"</td><td>"+data.investList[i].account+"</td><td>"+data.investList[i].investTime+"</td></tr>";							}
							$("#moreTable2 table").append(str);
							if(totalPage==$page){
								$("#moreTable2 .loadMore").hide();
							}
						}						
					} 		
			}) 
		})
	</script>
	<script>
	//增加href中 borrowNid
	function addId(){
		var $borrowNid = "${projectDeatil.borrowNid}";
		var $order="${order}";
		var $token="${token}"; 
	    var $platform="${platform}";
	    var $sign="${sign}";
	    var $randomString="${randomString}";
	    var $mobile = $("#mobile").val();
	    var $openAccountUrl = $("#openAccountUrl").val();
	    var url = "${projectDeatil.investUrl}";
	    //
	    //console.log(url+'/?{\"url":\"'+$openAccountUrl+"/?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}')
	    if(url.indexOf("jumpLogin")!=-1){
	    	$("#investNow").prop("href",url+"/?");
	    }else if(url.indexOf("jumpH5")!=-1){    	
	    	var $nHref = url+'/?{"url":"'+$openAccountUrl+"?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}';
	    	$(".openPopDivSpan a").prop("href",$nHref);
			$("#investNow").on("click",function(){
				$(".openPopDiv,.openPop").show();
			})
			$(".openPopDivClose").on("click",function(){
				$(".openPopDiv,.openPop").hide();
			})
	    }else{
		    var $newHref = "${projectDeatil.investUrl}"+"/?{"+"\"borrowNid\":\""+ $borrowNid+"\"}";
		    $("#investNow").prop("href",$newHref);
	    }
	}
	addId();
	</script>
	<script>
	var $token=$("#token").val();
	$("#tab4").click(
			function(){
				if($token){
					$.fillTmplByAjax("${projectDeatil.tabFourUrl}",{"borrowNid":"${projectDeatil.borrowNid }","platform":"${platform}","token":"${token}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoFourId", "#tmpl-fourData");		
				}else{
					$.fillTmplByAjax("${projectDeatil.tabFourUrl}",{"borrowNid":"${projectDeatil.borrowNid }","platform":"${platform}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoFourId", "#tmpl-fourData");
				}
			}
	);
	</script>
	</body>
	<script src="${ctx}/js/util.js" type="text/javascript" charset="utf-8"></script>
</html>