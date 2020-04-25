<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<title>我的投资 - 汇盈金服官网</title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/jquery-ui.css" />
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="user-credit-banner">
        <div class="hd-innernav">
            <jsp:include page="/subMenu.jsp"></jsp:include>
        </div>
		          <div class="container-1200">
		            <div class="touzi-con htj">
		                <div class="item">
		                    <div class="tit first">待收总额：
		                    <c:choose >
		                    <c:when test="${empty account.planAccountWait }">0.00</c:when>
		                    <c:otherwise>  ${account.planAccountWait }</c:otherwise>
		                    </c:choose> 
		                		  元</div>
		                    <div class="tit"><span class="first">待收本金：${account.planCapitalWait }元</span>  <span>待收收益：
		                    <c:choose >
		                    <c:when test="${empty account.planInterestWait }">0.00</c:when>
		                    <c:otherwise>  ${account.planInterestWait }</c:otherwise>
		                    </c:choose>元</span></div>
		                </div>
		                <div class="item">
		                    <div class="tit">累计收益（元）</div>
		                    <div class="num highlight"> <c:choose >
		                    <c:when test="${empty account.planRepayInterest }">0.00</c:when>
		                    <c:otherwise>  ${account.planRepayInterest }</c:otherwise>
		                    </c:choose> </div>
		                </div>
		                <div class="item">
		                    <div class="tit">累计加入（元）</div>
		                    <div class="num highlight"> <c:choose >
		                    <c:when test="${empty account.planAccedeTotal }">0.00</c:when>
		                    <c:otherwise>  ${account.planAccedeTotal }</c:otherwise>
		                    </c:choose></div>
		                </div>
		                <div class="clearfix"></div>
		            </div>
		        </div>
		    </div>
    </div>
    <div class="project-tabbing user-credit-tabbing">
        <div class="container-1200">
         	<input type="hidden" id="planStatus" name="planStatus" value="${planStatus }">
            <ul class="project-tab">
                <li panel="0" data-status = "4" >申购中</li>
                <li panel="1" data-status = "5"  class="active">锁定中</li>
                <li panel="2" data-status = "11">已退出</li>
            </ul>
            <div class="clearfix"></div>
            <ul class="project-tab-panel">
                  <li panel="0">
                    <table id="pant0" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye0">
						<!-- 分页栏模板 -->
					</div>
                  </li>
                  <li panel="1" class="active">
					<table id="pant1" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye1">
						<!-- 分页栏模板 -->
					</div>
                  </li>
                  <li panel="2">
					<table id="pant2" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye2">
						<!-- 分页栏模板 -->
					</div>
                  </li>
            </ul>
        </div>
    </div>
    <div class="settlement_mask"></div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/jquery-ui.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/datepicker-zh-CN.js?version=${version}" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/fill.js?version=${version}" ></script>
    <script type="text/javascript" src="${cdn}/js/user/planinfo/planinfolist.js?version=${version}" ></script>
</body>
</html>