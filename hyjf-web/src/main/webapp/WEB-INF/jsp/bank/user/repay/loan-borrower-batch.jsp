<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/common.jsp"%>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<link rel="stylesheet" href="${cdn}/dist/css/lib/bootstrap-datepicker3.standalone.css">
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
        <form id="info" action="${ctx}/bank/web/user/repay/orgUserStartBatchRepay.do">
        <input type="hidden"  id="errCode" value="${errCode}">
        <input type="hidden"  id="userId" name="userId" value="${orgUserId}">
        <input type="hidden"  id="listSize" name="listSize" value="0">
        <input type="hidden"  id="repayTotal" name="repayTotal" value="${repayInfo.repayAccount}">
        <input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" /> 
        <input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /> 
            <!-- start 内容区域 -->   
            
                <div class="loan-borrower">
                    <div class="loan-borrower-batch-top">
                        <div class="borrower-title-2">
	                	    <div class="loan-divright">
	                	    	<form>
	                                <label for="startDate">应还时间</label>
	                                <input type="text" class="date start" id="startDate" name="startDate" value="${startDate}" autocomplete="cc-end" placeholder="开始日期">
	                                <label for="endDate">至</label>
	                                <input type="text" class="date end" id="endDate" name="endDate" value="${endDate}"  autocomplete="cc-start" placeholder="结束日期">
	                                <span id="timeSerch" class="btn sm">搜索</span>
                                </form>
                            </div>            
                        </div>
                        <div class="borrower-title-main">
                        	
	                        	<div class="borrower-item">
	                        		<div class="total">
	                        			<span><img src="${cdn}/dist/images/loan-icon2.png"></span>
	                               		<span>应还总额：<em>${repayInfo.repayAccount}</em> 元</span>
	                        		</div>
	                                <div class="details">
	                                	<span>应还本金：<em>${repayInfo.repayCapital}</em> 元</span>
	                                	<span>应还利息：<b>${repayInfo.repayInterest}</b> 元</span>
	                                	<span>还款服务费：<b>${repayInfo.manageFee}</b>元</span>
	                                	<br/>
	                                	<span>提前减息：<b>${repayInfo.chargeInterest}</b> 元</span>
	                                	<span>延期利息：<b>${repayInfo.delayInterest}</b> 元 </span>
	                                	<span>逾期利息：<b>${repayInfo.lateInterest}</b> 元</span>
	                                </div>
	                            </div>
	                            <div class="borrower-arr">
		                                <label>平台登录密码</label>
		                                <input type="password" id="password" name="password" class="password text">
		                                <span id="pwdsubmit" class="btn">确认</span>	    
		                                <span class="small" style="display:none;"><span class="icon iconfont icon-zhu"></span>密码输入错误</span>                            
	                            </div>
                        </div>
                    </div>
                    <div class="loan-borrower-batch-bottom" >
                        <div class="loan-thead">
                            <span class="l1">应还时间</span>
                            <span class="l2">项目编号</span>
                            <span class="l3">期数</span>
                            <span class="l4">历史年回报率</span>
                            <span class="l5">借款金额</span>
                            <span class="l6">本期应还总额</span>
                        </div>
                        <ul id = "repayList">
                            <li style="padding: 0;overflow: hidden;height: 270px;line-height: inherit;">
                                <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                            </li>
                        </ul>
                        <div id="repayListEmptyStyle"></div>
                        <div class="pages-nav" id="repay-pagination">
		                </div>
                    </div>
                </div>
            </form>
            <!-- end 内容区域 -->            
        </div>
          <!-- 缴费授权返回地址 -->
    <script type="text/javascript">
		document.cookie = 'authPayUrl='+window.location.href+';path=/'
    </script>
    <!-- 缴费授权返回地址 -->
    <div class="alert" id="authInvesPop" style="margin-top: -154.5px;width:350px;display: none;">
    	<div onclick="utils.alertClose('authInvesPop')" class="close">
    		<span class="iconfont icon-cha"></span>
    	</div>
    	<div class="icon tip"></div>
    	<div class='content prodect-sq'>
    		
    	</div>
    	<div class="btn-group">
    		<div class="btn btn-primary single" id="authInvesPopConfirm">立即开通</div>
    	</div>
    </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
	<script src="${cdn}/dist/js/lib/bootstrap-datepicker.min.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn}/dist/js/loan/loan-batch.js"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.md5.js" charset="utf-8"></script>
</body>
</html>