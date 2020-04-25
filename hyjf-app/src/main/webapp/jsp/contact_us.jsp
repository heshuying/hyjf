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
		<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title></title>
	</head>
	<body class="about-us-body">
		<div class="specialFont response" >
			<!-- 累计成交开始 -->
				<div class="about-us-contact" style="margin:0 0 15px 0">
					<ul>
						<li>客服电话<span ><a href="tel:400-065-5000" style="font-size:14px" class="colorBlue">400-065-5000(9:00-18:00)</a></span></li>
						<li>客服邮箱<span>info@hyjf.com</span></li>
						<li>微信公众号<span>汇盈金服</span></li>
						<li>官方QQ群<span>97756631</span></li>
					</ul>
				</div>
				<div class="about-us-contact" >
					<ul>
						<li>公司电话（总部）<span><a href="tel:021-23570077" style="font-size:14px;" class="colorBlue">021-23570077</a></span></li>
						<li class="about-contact-li" style="height:60px !important">公司地址<span style="text-align:right;line-height:20px;margin-top:10px">上海市长宁区延安西路2299号世贸商城2401-2404室</span></li>
					</ul>
				</div>
		</div>
		<script src="${ctx}/js/doT.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
		
		<%-- <script type="text/javascript" src="${ctx}/js/fullPage.min.js" ></script> --%>
		<script>
		$.fillTmplByAjax("/hyjf-app/homepage/getTotalStatics",null, "#about_deal", "#tmpl-data");
		</script>
		<!-- <script>
		var runPage = new FullPage({
			 
			  id : 'pageContain',                            // id of contain
			  slideTime : 800,                               // time of slide
			  continuous : false,                            // create an infinite feel with no endpoints
			  effect : {                                     // slide effect
			          transform : {
			            translate : 'Y',                      // 'X'|'Y'|'XY'|'none'
			            scale : [.1, 1],                      // [scalefrom, scaleto]
			            rotate : [0, 0]                       // [rotatefrom, rotateto]
			          },
			          opacity : [0.5, 1]                       // [opacityfrom, opacityto]
			      },                           
			  mode : 'touch,click,nav:navBar',               // mode of fullpage
			  easing : 'ease'                                // easing('ease','ease-in','ease-in-out' or use cubic-bezier like [.33, 1.81, 1, 1];
			    //  ,onSwipeStart : function(index, thisPage) {   // callback onTouchStart
			    //    return 'stop';
			    //  }
			    //  ,beforeChange : function(index, thisPage) {   // callback before pageChange
			    //    return 'stop';
			    //  }
			    //  ,callback : function(index, thisPage) {       // callback when pageChange
			    //    alert(index);
			    //  };
			});           
		</script> -->
		<script src="${ctx}/js/jq22.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>