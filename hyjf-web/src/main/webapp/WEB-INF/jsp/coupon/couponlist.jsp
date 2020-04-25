<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>我的优惠券 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${cdn}/css/jquery-ui.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="user-credit-banner">
        <jsp:include page="/subMenu.jsp"></jsp:include>
        <h2>我的优惠券</h2>
    </div>
	<div class="project-tabbing user-credit-tabbing">
        <div class="container-1200">
            <ul class="project-tab">
                <li panel="0" class="active" onclick="changetype(0)">未使用</li>
                <li panel="1" onclick="changetype(1)">已使用</li>
                <li panel="2" onclick="changetype(4)">已失效</li>
            </ul>
            <div class="clearfix"></div>
            <ul class="project-tab-panel">
                <li panel="0" class="active">
                    <table id="pant0">
                    </table>
                    <div class="new-pagination" id="fenye0">
						<!-- 分页栏模板 -->
					</div>
                </li>
                <li panel="1">
                    <table id="pant1">
                    </table>
                    <div class="new-pagination" id="fenye1">
						<!-- 分页栏模板 -->
					</div>
                </li>
                <li panel="2">
                    <table id="pant2">
                    </table>
                    <div class="new-pagination" id="fenye2">
						<!-- 分页栏模板 -->
					</div>
                </li>
                
            </ul>
        </div>
    </div>
    <div class="clearfix"></div>
    
	
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/jquery-ui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/datepicker-zh-CN.js" charset="utf-8"></script>
	<script src="${cdn}/js/fill.js" type="text/javascript"></script>
	<script type="text/javascript">
	    setActById('userCoupon');
	    var usedFlag=0;
		$(".project-tab").click(function(e) {
	        var _self = $(e.target);
	        if (_self.is("li")) {
	            var idx = _self.attr("panel");
	            var panel = _self.parent().siblings(".project-tab-panel");
	            _self.siblings("li.active").removeClass("active");
	            _self.addClass("active");
	            panel.children("li.active").removeClass("active");
	            panel.children("li[panel="+idx+"]").addClass("active");
	        }
	    })
	</script>
	<script src="${cdn}/js/coupon/couponlist.js?version=${version}" type="text/javascript"></script>
	<!-- 设置定位  -->
	<script>setActById("myReward");</script>
	</body>
</html>