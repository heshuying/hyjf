<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>会员申请 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<input id="account" name="account" type="hidden" value="${account}"/>
	<div class="vip-page">
        <div class="vip1"></div>
        <div class="vip2">
            <div class="vip-add-btn">
            <c:choose>
	            <c:when test="${!vipFlg }">
	            	<a href="javascript:;" id="applyVipBtn" class="vip-add-copy scaleout">立即加入</a>
	                <a href="javascript:;" class="vip-add ">立即加入</a>
	            </c:when>
	            <c:otherwise>
	            	<a href="javascript:;" id="vipDetailBtn" class="vip-add-copy scaleout">查看当前等级</a>
	                <a href="javascript:;" class="vip-add ">查看当前等级</a>
	            </c:otherwise>
            </c:choose>
            </div>

            <!-- 已经加入会员 -->
            <!-- <div class="vip-add-btn">
                <a href="javascript:;" class="vip-add-copy scaleout">查看当前等级</a>
                <a href="javascript:;" class="vip-add">查看当前等级</a>
            </div> -->
        </div>
        <div class="vip3"></div>
        <div class="vip4"></div>
        <div class="vip5"></div>
        <div class="vip6"></div>
        <div class="vip7">
<div class="vip-add-btn">
	            <c:choose>
		            <c:when test="${!vipFlg }">
		            	<a href="javascript:;" id="applyVipBtn2" class="vip-add-copy scaleout">立即加入</a>
		                <a href="javascript:;" class="vip-add ">立即加入</a>
		            </c:when>
		            <c:otherwise>
		            	<a href="javascript:;" id="vipDetailBtn2" class="vip-add-copy scaleout">查看当前等级</a>
		                <a href="javascript:;" class="vip-add ">查看当前等级</a>
		            </c:otherwise>
	            </c:choose>
			</div>
                    </div>
        <div class="clearfix"></div>
    </div>

    <!-- 遮罩层 -->
    <div class="pop-overlayer"></div>
    <div class="pop-box pop-tips" id="openAccountPop">
        <a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
        <div class="pop-main">
            <div class="pop-txt">您尚未开户，马上去开户！</div>
            <div class="btns">
                <a href="javascript:;" class="cancel" onclick="popOutWin()">取消</a>
                <a href="javascript:;" id="openAccount" class="confirm">去开户</a>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    <div class="pop-box pop-tips" id="rechargePop">
        <a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
        <div class="pop-main">
            <div class="pop-txt">您的账户余额不足，马上去充值！</div>
            <div class="btns">
                <a href="javascript:;" class="cancel" onclick="popOutWin()">取消</a>
                <a href="javascript:;" id="recharge"  class="confirm">去充值</a>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    <div class="pop-box pop-tips" id="repeatVip">
        <a class="pop-close" href="javascript:void(0);" onclick="popOutWin()"></a>
        <div class="pop-main">
            <div class="pop-txt">会员重复购买，请刷新页面！</div>
            <div class="clearfix"></div>
        </div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
		setActById('hdVIP');
	</script>
	<script src="${cdn}/js/drag.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn}/js/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn }/js/vip/apply/apply.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>