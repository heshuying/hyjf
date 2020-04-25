<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>注册有礼 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-201609.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	
   <div class="active-201609-1"></div>
    <!-- 第一步 -->
    <c:if test="${status==0}">
    	<div class="active-201609-2"></div>
    	<div class="active-201609-3 step1">
        <div class="container-1300">
            <a href="${url}" class="active-201609-btn btn-step1"></a>
        </div>
    </div>
    </c:if>
    <!-- end第一步 -->

    <!-- 第二步 -->
    <c:if test="${status==1}">
    	<div class="active-201609-2 step2"></div> 
    	<div class="active-201609-3 step2">
        <div class="container-1300">
            <a href="${url}" class="active-201609-btn btn-step2"></a>
        </div>
    </div>
    </c:if>
    <!-- end第二步 -->

    <!-- 老用户 -->
    <c:if test="${status==2}">
    	<div class="active-201609-2 unava"></div> 
    	<div class="active-201609-3 unava">
       	<div class="container-1300">
           	<a href="${url}" class="active-201609-btn btn-invest"></a>
       	</div>
    </div>
    </c:if>
    <!-- end老用户 -->
    <div class="active-201609-4">
        <div class="container-1300">
            <a href="${XSHurl}" class="active-201609-btn btn-experence"></a>
        </div>
    </div>
    <div class="active-201609-5">
        <div class="container-1300">
            <div class="active-201609-rules">
                <p class="active-201609-rules-title">活动时间：</p>
                <p class="active-201609-rules-content">2016年9月19日起，2016年10月18日止。</p>

                <p class="active-201609-rules-title">活动内容：</p>
                <p class="active-201609-rules-content">凡于活动期内成功注册汇盈金服的用户，即可免费领取68元现金红包。</p>

                <p class="active-201609-rules-title">奖励发放：</p>
                <p class="active-201609-rules-content">现金红包将于用户注册成功后发放至用户汇盈金服账户，登陆后于“优惠券”中查看。</p>

                <p class="active-201609-rules-title">注：</p>
                <p class="active-201609-rules-content">1.68元现金红包中包含18元、20元、30元代金券各一张，均自发放之日起30日内有效，过期作废。 <br/>
                2.18元代金券单笔投资达1000元可用，20元代金券单笔投资达2000元可用，30元代金券单笔投资达5000元可用。<br/>
                3.本活动代金券仅限投资汇直投项目使用。
                </p>
                <p class="active-201609-rules-copyright">
                    本活动最终解释权归汇盈金服所有
                </p>
            </div>
        </div>
    </div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	
	
	</body>
</html>