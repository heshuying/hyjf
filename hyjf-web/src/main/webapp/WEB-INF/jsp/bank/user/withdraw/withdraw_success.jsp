<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="${cdn}/dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">恭喜您,提现成功！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li class="recharge">
                                <div>您已成功从账户转出：<span class="value">${amt} </span>元</div>
                            </li>
                            <li class="recharge">
                                <div>手续费：<span class="value">${fee} </span>元</div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <a href="${ctx}/user/pandect/pandect.do" class="open">返回</a>
                        <a href="${ctx}/bank/user/trade/initTradeList.do?tabIndex='withdraw'" class="open">查看详情</a>                    
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>