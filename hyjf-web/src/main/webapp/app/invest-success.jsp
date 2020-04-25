<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
     <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="invest-success">
                    <div class="top-attr">
                        <img src="../dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">恭喜您投资成功！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div><span>加入金额：</span><span class="value">10,000,000.00</span><span> 元</span></div>
                            </li>
                            <li>
                                <div><span>历史回报：</span><span class="value">100.00 </span><span> 元</span></div>
                            </li>
                            <li class="mid-border">
                                <div>
                                    <span class="icon iconfont icon-piaofang"></span>
                                    <span>优惠券：</span><span class="value">1.5%</span><span>加息券</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <a href="javascript:;" class="open">查看交易明细</a>
                        <a href="javascript:;" class="open">返回项目详情</a>                    
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
        
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>