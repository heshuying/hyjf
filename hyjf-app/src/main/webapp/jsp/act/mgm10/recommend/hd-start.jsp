<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="font-size: 37.6812px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/css/mgm.css"/>
<style>
.hidden{
	visibility: hidden;
	margin-left:0!important;
}
</style>
<title>活动详情</title>
</head>
	<input type="hidden" value="${flag}" id="flag"/>
	<body class="bg-purple">
		 <div class="response hd-start">
 
      <!--nav ul begin-->
      <ul class="rule_nav" id="rule_nav">
        <li id="recover_id_li" class="rule_nav_cell rule_nav_selected" >获得记录<span></span></li>
        <li id="now_id_li" class="rule_nav_cell" >使用记录</li>
      </ul>
      <!--nav ul end-->
      <!--content begin-->
      <div class="rule_content">
      	<div class="rule_0">
      	    <img class="myStart-img" src="${ctx}/images/mgm10/myStart_bg01.jpg" alt="" />
      		<div  class="rule-cont">
      		    <div>
	            <ul class="attr_scroll" id="recover">
	                <!-- 列表  -->
	                <c:forEach items="${userRecommendStarPrizeList}" var="record" begin="0" step="1" varStatus="status">
			            <li class="invest-list-li">
			              	<div class="get-record-list">
			              		<div class="get-record">
			              	 		<p class="width-1"><c:out value="${record.recommendSource }"></c:out></p>
			              	 		<p class="width-2 record-num"><span><c:out value="${record.recommendCount }"></c:out></span></p>
			              	 		<p class="width-3 record-img  arrows-btn"><img src="${ctx}/images/mgm10/myStart-arrows.png" alt="" /></p>
			              	    </div>
			              	    <div class="record-coupop">
			              	    	<div class="record-coupop-cont">
			              	    		<c:if test='${record.source != 3}'>
			              	    			<p>推荐好友<span><c:out value="${record.inviteByUser }"></c:out></span></p>
			              	    		</c:if>
			              	    		<c:if test='${record.source == 3}'>
			              	    			<c:forEach items="${record.inviteUserName}" var="usernane" begin="0" step="1" varStatus="status">
			              	    				<c:if test="${status.index==0}">
			              	    				<p>推荐好友<span><c:out value="${usernane}"></c:out></span></p>
			              	    				</c:if>
			              	    				<c:if test="${status.index!=0}">
			              	    				<p><span class="hidden">推荐好友</span></span><span><c:out value="${usernane}"></c:out></span></p>
			              	    				</c:if>
			              	    				
			              	    			</c:forEach>
			              	    		</c:if>
				              		    <p>奖励时间<span><c:out value="${record.sendTime }"></c:out></span></p>
			              	    	</div>
				              	</div>
				              	 <div class="record-line"></div>
			              	</div>
			            </li>
		            </c:forEach>
	                <!-- 列表  -->
	            </ul>
      		    </div>
      		    <c:if test="${userRecommendStarPrizeListSize==0}">
		        <div class="record-loading" id="nomoreresults_recover">暂无获得记录...</div>
		        </c:if>
      		</div>
        </div>
        <div class="rule_0">
            <img class="myStart-img" src="${ctx}/images/mgm10/myStart_bg05.jpg" alt="" />
      		<div class="rule-cont">
	      		<div>
		           <ul class="attr_scroll" id="now">
		                <!-- 列表  -->
		                <c:forEach items="${userRecommendStarUsedList}" var="record" begin="0" step="1" varStatus="status">
			            <li class="invest-list-li">
			              	<div class="get-record-list">
			              		<div class="get-record">
			              	 		<p class="width-1"><c:out value="${record.prizeName }"></c:out></p>
			              	 		<p class="width-2 record-num"><span><c:out value="${record.prizeCount }"></c:out></span></p>
			              	 		<p class="width-3 record-img  arrows-btn"><img src="${ctx}/images/mgm10/myStart-arrows.png" alt="" /></p>
			              	    </div>
			              	    <div class="record-coupop">
			              	    	<div class="record-coupop-cont">
			              	    		<p>使用方式<span><c:out value="${record.prizeKind }"></c:out></span></p>
				              		    <p>使用时间<span><c:out value="${record.addTime }"></c:out></span></p>
			              	    	</div>
				              	</div>
				              	 <div class="record-line"></div>
			              	</div>
			            </li>
			            </c:forEach>
		                <!-- 列表  -->
		           </ul>
	      		</div>
	      		<c:if test="${userRecommendStarUsedListSize==0}">
		        <div class="record-loading" id="nomoreresults_recover">暂无使用记录...</div>
		        </c:if>
      	    </div>
        </div>
        <div class="clearboth"></div>
    </div>
    <img class="myStart-img height2" src="${ctx}/images/mgm10/myStart_bg03.jpg" alt="" />
    <div class="myStart-top-img-holder"></div>
	</body>
	<script type="text/javascript" src="${ctx}/js/mgm10/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/mgm10/tabChange.js"></script>
	<script type="text/javascript" src="${ctx}/js/mgm10/windowCoupop.js"></script>
	<script>
	$(function(){
		var liWidth = $(".rule_nav_cell").width();
		 var width = $(".rule_0").width();
		 var flag = $("#flag").val();
		 $(".rule_nav_cell").eq(flag).addClass("rule_nav_selected").siblings().removeClass("rule_nav_selected");
          $(".rule_content").css("marginLeft",-flag*width+'px');        
          $(".rule_nav_cell span").css("left",flag*liWidth+'px');
	})
	 var stopScroll_recover = false;
     var stopScroll_now = false;
     
     //初始化默认第一个按钮
     
     	$('#now').attr('start','');
		$('#now').attr('scrollpagination','disabled');
         if(stopScroll_recover==false){
		$('#recover').attr('start','am-active');
		$('#recover').attr('scrollpagination','enabled');
		start();
        }
        
	$('#recover_id_li').click(function(){
		
		$('#now').attr('start','');
		$('#now').attr('scrollpagination','disabled');
         if(stopScroll_recover==false){
		$('#recover').attr('start','am-active');
		$('#recover').attr('scrollpagination','enabled');
		start();
        }
	});
	
	
	$('#now_id_li').click(function(){
		$('#recover').attr('start','');
		$('#recover').attr('scrollpagination','disabled');
         if(stopScroll_now==false){
		$('#now').attr('start','am-active');
		$('#now').attr('scrollpagination','enabled');
		start();
        }
	});
	
		function start(){
        $('.attr_scroll').each(function(){
			
			var total_json_page = "{$total_json_page}";
			var _this = $(this);
			 
			var isstart = $(_this[0]).attr('start');
			var type = $(_this[0]).attr('id');
			 
			if(isstart != 'am-active'){
				return true;
			}
			//alert(isstart);
			
				
			_this.scrollPagination(
			
			{	
			
				'isstart':isstart, // the url you are fetching the results
				'contentPage': "{:U('Weixin/User/ajax_recover_investments')}", // the url you are fetching the results
				'contentData': {'type':type,'totalpage':total_json_page[type]}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
				'scrollTarget': $(window), // who gonna scroll? in this example, the full window
				'heightOffset': 0, // it gonna request when scroll is 10 pixels before the page ends
				'beforeLoad': function(){ // before load function, you can display a preloader div

					$('#loading_'+type).fadeIn('fast');
				 
                    $('#'+type).attr('scrollpagination','disabled');
				},
				
				'afterLoad': function(elementsLoaded){ // after loading content, you can use this function to animate your new elements
					
					 $('#'+type).attr('scrollpagination','enabled');
					 $('#loading_'+type).fadeOut('fast');
					 
					 var i = 0;
					 $(elementsLoaded).fadeInWithDelay();
					 if ($(this).children().size() > 0){ // if more than 100 results already loaded, then stop pagination (only for testing)
					 
						$('#nomoreresults_'+type).fadeIn();
						$(this).stopScrollPagination();
					 }
					 
					if(elementsLoaded.length==0){
					   if(type=='recover'){
						stopScroll_recover = true; 
                        }else if(type=='now'){
                            stopScroll_now = true; 
                        }
						$('#nomoreresults_'+type).fadeIn('slow');
						$('#'+type).stopScrollPagination();
						//$('#nomoreresults_'+type).fadeOut();
					};
				}
			});
		});
   }
   
        
	// code for fade in element by element
	$.fn.fadeInWithDelay = function() {
		var delay = 0;
		return this.each(function() {
			$(this).delay(delay).animate({
				opacity: 1
			}, 200);
			delay += 100;
		});
	};
 $('.rule_nav_cell').click(function(){
 	var len1=$(this).index();
 	var grays = $(".gray_bg>div:eq("+len1+")");
 	var pp = grays.height();
 	var kk = grays.find('.attr_scroll').children('li').length;
 	$html = "<div class='no-more'>已加载全部信息</div>";
 	if(kk>=1 && kk<10){
 		grays.find('.no-more').remove();
 		grays.append($html);
 		return false;
 	}
 	$('html').height(pp+95);
 });
	</script>
</html>
