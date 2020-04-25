<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<section class="breadcrumbs">
	     <div class="container">
	         <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/user/pandect/pandect.do">账户中心</a> &gt; <a href="${ctx}/user/assetmanage/init.do">资产管理</a> &gt; <a href="${ctx}/bank/user/credit/userCanCreditList.do">我要转让</a> &gt; 转让详情</div>
	     </div>
	 </section>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="transfer-details">
            	<form action="${ctx}/bank/user/credit/savetendertocredit.do" method="post" id="creditForm" name="creditForm">
            		<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /> <!-- 服务费授权状态 -->
    				<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" />
	            	<input type="hidden" name="borrowNid" id="borrowNid" value="${creditResult.data.borrowNid}" />
					<input type="hidden" name="tenderNid" id="tenderNid" value="${creditResult.data.tenderNid}" />
					<input type="hidden" name="tenderPeriod" id="tenderPeriod" value="${creditResult.data.tenderPeriod}" />
					<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
					<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
					<input type="hidden" id="concessionRateDown" value="${concessionRateDown}" title="折让率下限"/>
	                <input type="hidden" id="concessionRateUp" value="${concessionRateUp}" title="折让率上限"/>
	                <input type="hidden" id="attornRate" name="attornRate" value="${attornRate}" title="转让服务费率">
	                <input type="hidden" id="toggle" value="${toggle}" title="债转开关">
	                <input type="hidden" id="closeDes" value="${closeDes}" title="关闭提示信息">

					<input type="hidden" name="presetProps" id="presetProps" value="">
	                <div class="transfer-top">
	                    <div class="title">项目详情</div>
	                    <ul class="list">
	                        <li>
	                            <span class="basic-label">项目编号：</span>
	                            <span class="basic-value">${creditResult.data.borrowNid}</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">历史年回报率：</span>
	                            <span class="basic-value value">${creditResult.data.borrowApr}%</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">项目期限：</span>
									<span class="basic-value">${creditResult.data.borrowPeriod}</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">当前持有：</span>
	                            <span class="basic-value">${creditResult.data.tenderPeriod}天</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">当前剩余：</span>
	                            <span class="basic-value">${creditResult.data.lastDays}天</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">还款方式：</span>
	                            <span class="basic-value">${creditResult.data.borrowStyleName}</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">持有本金：</span>
	                            <span class="basic-value"><fmt:formatNumber value="${creditResult.data.creditAccount}" pattern="#,###" />元</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">当前期次：</span>
	                            <span class="basic-value">第${creditResult.data.periodNow}期（共<c:if test="${creditResult.data.borrowStyle eq 'endmonth'}">${fn:substring(creditResult.data.borrowPeriod, 0, 1)}</c:if>
								<c:if test="${creditResult.data.borrowStyle eq 'endday' or creditResult.data.borrowStyle eq 'end'}">1</c:if>期）</span>
	                        </li>
	                        <li>
	                            <span class="basic-label">投资时间：</span>
	                            <span class="basic-value">${creditResult.data.tenderTime}</span>
	                        </li>
	                        
	                    </ul>
	                </div>
	                <div class="transfer-mid">
	                    <div class="title">转让债权</div>
	                    <div class="main">
	                        <ul>
	                            <li>
	                                <span class="basic-label fn-right">转让本金 </span>
	                                <span class="basic-main" id="creditAccount">${creditResult.data.creditAccount}元</span>
	                                <input type="hidden" name="creditAccountValue" id="creditAccountValue" value="${creditResult.data.creditAccount}" />
	                            </li>
	                            <li>
	                                <span class="basic-label fn-right">本金折让率</span>
	                                <span class="basic-main">
	                                    <div class="gw_num">
	                                        <em class="jian">-</em>
	                                        <input type="text" id="creditDiscount" value="${concessionRateDown}" class="num" disabled/>
	                                        <em class="add">+</em>
	                                        <input type="hidden" id="creditVal" name="creditDiscount" value="${concessionRateDown}"/>
	                                    </div>
	                                    <div class="text">%</div>
	                                    <div class="text sm">（${concessionRateDown}%~${concessionRateUp}%）</div>
	                                </span>
	                            </li>
	                            <li>
	                                <span class="basic-label fn-right">预计本金折让金额 </span>
	                                <span class="basic-main" id="discountMoney">${creditResult.calData.creditPrice}元</span>
	                            </li>
	                            <li>
	                                <span class="basic-label fn-right">预计持有期收益</span>
	                                <span class="basic-main" id="interest">${creditResult.calData.assignInterestAdvance}元</span>
	                            </li>
	                            <li>
	                                <span class="basic-label fn-right">预计服务费</span>
	                                <span class="basic-main">
	                                    <div class="text" style="margin-left: 0;" id="creditFee">${creditResult.calData.creditFee}元</div>
	                                    <div class="text">（当前转让服务费率为${attornRate}%）</div>
	                                </span>
	                            </li>
	                            <li>
	                                <span class="basic-label fn-right">预计到账金额</span>
	                                <span class="basic-main">
	                                    <div class="value lg" id="moneyGet">${creditResult.calData.expectInterest}</div>
	                                    <div class="text bom">元</div>
	                                </span>
	                            </li>
	                        </ul>
	                    </div>
	                </div>
	                <div class="transfer-bom">
	                    <div class="title">验证信息</div>
	                    <div class="main">
	                        <div class="tel">
	                            <span class="tel-label">手机号</span>
	                            <span class="tel-main">${creditResult.mobile}</span>
	                        </div>
	                        <div id="slide-box">
	                            <div id="slideUnlock">
	                                <input type="hidden" value="" name="lockable" id="lockable"/>
	                                <div id="slider">
	                                    <span id="label"><i></i></span>
	                                    <span class="slideunlock-lable-tip">移动滑块验证</span>
	                                    <span id="slide-process"></span>
	                                </div> 
	                            </div>
	                        </div>
	                        <label class="validate">
	                            <span class="tit">手机验证</span>
	                            <input type="text" class="code" id="telcode" name="telcode" maxlength="6" placeholder="输入验证码">
	                            <span class="get-code disable"><em class="rule"></em>获取验证码</span>
	                        </label>
	                        <div class="transfer-btn">
	                            <button type="submit" id ="confirmBtn" class="btn sub">确认转让</button>
	                            <a href="${ctx}/bank/user/credit/userCanCreditList.do"  class="cancel">取消转让 >></a>
	                        </div>
	                     
	                    </div>
	                </div>
	                  
	                <div class="arr">
                    <p class="title-sm">转让说明</p>
                    <ul>
                        <li class="ui-title-top-sm">1、本金折让率？</li>
                        <li class="ui-title-bom">折让率是指本金折让的百分比，折让率越高，成交完成的速度越快；
                        </li>
                        <li class="ui-title-top">2、预计本金折让金额</li>
                        <li class="ui-title-bom">本金折让金额=转让本金*本金折让率；投资者在承接这部分本金时，少支付的金额；
                        </li>
                        <li class="ui-title-top">3、预计持有收益</li>
                        <li class="ui-title-bom">预计持有收益=本期历史回报-本期出让本金*预期年化*剩余天数/360；为当期本金在持有期内预计所得的收益；
                        </li>
                        <li class="ui-title-top">4、预计服务费</li>
                        <li class="ui-title-bom">服务费=[ 转让本金+预计持有收益 -本金折让金额] * 转让服务费率
                        </li>
                        <li class="ui-title-top">5、预计到账金额</li>
                        <li class="ui-title-bom">预计到账金额=转让本金+预计持有收益-预计本金折让金额-预计服务费
                        </li>
                    </ul>
                </div>
                </form>
            </div>
             
            <!-- end 内容区域 -->            
        </div>
    </article>
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
    		<div class="btn btn-primary single" id="authInvesPopConfirm">立即授权</div>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.slideunlock.min.js"></script>
	<script src="${cdn}/dist/js/rights-manage/transfer-details.js?version=${version}"></script>
	<!-- 设置定位  -->
	<script>setActById("mytender");</script>
</body>
</html>