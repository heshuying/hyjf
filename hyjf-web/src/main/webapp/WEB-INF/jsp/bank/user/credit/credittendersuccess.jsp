<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess" style="position: relative;padding-bottom: 12%;">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn}/dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">恭喜您,承接成功!</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div><span>承接金额：</span><span class="value"><fmt:formatNumber value="${creditTender.assignCapital }" pattern="#,##0.00" /></span><span> 元</span></div>
                            </li>
                            <li>
                                <div><span>历史回报：</span><span class="value">${creditTender.assignInterest } </span><span> 元</span></div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <a href="${ctx}/bank/user/trade/initTradeList.do" class="open">查看交易明细</a>
						<a href="${ctx}/bank/user/credit/initWebCreditPage.do" class="open">继续承接</a>               
                    </div>
                </div>
                <!-- <div class="invest-bom-bg"> </div> -->
                <img src="${cdn}/dist/images/gongzhonghao-banner@2x.png?v=20171123" alt="" id="gzhImg" />
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>