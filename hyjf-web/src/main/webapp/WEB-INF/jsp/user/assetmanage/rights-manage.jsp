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
	
	<article class="main-content" style="padding-top: 0;">
		<jsp:include page="/subMenu.jsp"></jsp:include>
        <div class="container" style="margin-top:69px">
            <!-- start 内容区域 -->
            <div class="rights-manage" style="margin-top: 15px">
                <div class="rights-top">
                    <div class="rights-arr">
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwait+account.planAccountWait, ".")[0]}' pattern='#,##0.' /></span>
                                <span class="sm">${fn:split(account.bankAwait+account.planAccountWait, ".")[1]}</span>
                            </div>
                            <p class="rights-grey">待收总额（元）</p>
                        </div>
                        <div class="rights-line"></div>
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="black"><fmt:formatNumber value='${fn:split(account.bankAwaitCapital+account.planCapitalWait, ".")[0]}' pattern='#,##0.' /></span>
                                <span class="black-sm">${fn:split(account.bankAwaitCapital+account.planCapitalWait, ".")[1]}</span>
                            </div>
                            <p class="rights-grey">待收本金（元）</p>
                        </div>
                        <div class="rights-line"></div>
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="black"><fmt:formatNumber value='${fn:split(account.bankAwaitInterest+account.planInterestWait, ".")[0]}' pattern='#,##0.' /></span>
                                <span class="black-sm">${fn:split(account.bankAwaitInterest+account.planInterestWait, ".")[1]}</span>
                            </div>
                            <p class="rights-grey">待收收益（元）</p>
                        </div>
                    </div>
                    <div class="rights-main">
                        <div class="rights-fl">
                            <div id="main" style="width:300px;height:300px;"></div>
                        </div>
                   	  	<div class="rights-fr">
	                         <p class="p1">资产</p>
	                         <p class="p2">待收金额（元）</p>
	                         <p class="p3">待收本金（元）</p>
	                         <p class="p4">待收收益（元）</p>
	                         <ul class="table-main">
	                             <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">散标</span></li>
	                             <li class="p2"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwait, ".")[1]}</span></li>
	                             <li class="p3"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitCapital, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitCapital, ".")[1]}</span></li>
	                             <li class="p4"><span class="lg"><fmt:formatNumber value='${fn:split(account.bankAwaitInterest, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.bankAwaitInterest, ".")[1]}</span></li>
	                         </ul>
	                         <ul class="table-main">
	                             <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">智投</span></li>
	                             <li class="p2"><span class="lg"><fmt:formatNumber value='${fn:split(account.planAccountWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planAccountWait, ".")[1]}</span></li>
	                             <li class="p3"><span class="lg"><fmt:formatNumber value='${fn:split(account.planCapitalWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planCapitalWait, ".")[1]}</span></li>
	                             <li class="p4"><span class="lg"><fmt:formatNumber value='${fn:split(account.planInterestWait, ".")[0]}' pattern='#,##0.' /><span class="sm">${fn:split(account.planInterestWait, ".")[1]}</span></li>
	                         </ul>
	                         <input type="hidden" id="accountAwait" value="<fmt:formatNumber value='${account.bankAwait+account.planAccountWait}' pattern='#,##0.00' />">
	                         <input type="hidden" id="bankAccountAwait" value="${account.bankAwait}">
	                         <input type="hidden" id="planAccountWait" value="${account.planAccountWait}">
	                         <input type="hidden" id="toggle" value="${toggle}">
                             <input type="hidden" id="closeDes" value="${closeDes}">
                        </div>
                    </div>
                </div>
                <div class="rights-list">
                    <ul class="tab-tags">
                        <li class="tab-tagsli show1 active">散标</li>
                        <li class="tab-tagsli show2 currentHoldPlanClass">智投</li>
                    </ul>
                    <div class="tab-itempar">
                        <ul class="tab-divul">
                            <li class="tab-div-li active-in currentHoldObligatoryRightClass" onclick="onShow(this)"><a href="javascript:void(0)">当前持有
                            (<span id="currentHoldObligatoryRightCount"></span>)
                            </a></li>
                            <li class="tab-div-li repayMentClass" onclick="onShow(this)"><a href="javascript:void(0)">已回款
                            (<span id="repayMentCount"></span>)
                            </a></li>
                            <li class="tab-div-li myCreditListClass" onclick="onShow(this)"><a href="javascript:void(0)">转让记录
                            (<span id="tenderCreditDetailCount"></span>)
                            </a></li>
                            <a href="${ctx}/bank/user/credit/userCanCreditList.do" class="ui-button-orange">我要转让</a>
                        </ul>
                        <ul class="tab-divin">
                            <li class="show">
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/历史年回报率</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)" class="currentHoldObligatoryRightOrderBy" data-val="1" >持有本金<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">待收总额</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)" class="currentHoldObligatoryRightOrderBy" data-val="2">出借时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)" class="currentHoldObligatoryRightOrderBy" data-val="3">预计还款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="currentHoldObligatoryRight">
                                        	<tr>
                                        		<td colspan="8">
                                        			<div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                        		</td>
                                        	</tr>
                                        </tbody>
                                    </table>
									<div class="pages-nav" id="currentHoldObligatoryRight-pagination"></div>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/历史年回报率</th>
                                                <th class="ui-list-title pl3" >
                                                    <a href="javascript:void(0)" class="repayMentOrderBy" data-val="1" >出借金额<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">还款总额</th>
                                                <th class="ui-list-title pl5" >
                                                    <a href="javascript:void(0)" class="repayMentOrderBy" data-val="2">实际收益<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6" >
                                                    <a href="javascript:void(0)" class="repayMentOrderBy" data-val="3">还款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                            </tr>
                                        </thead>
                                        <tbody id='repayMent'>
                                        </tbody>
                                    </table>
                                    <div class="pages-nav" id="repayMent-pagination"></div>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%;">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title zr1">项目编号</th>
                                                <th class="ui-list-title zr2">原始转让本金</th>
                                                <th class="ui-list-title zr3">折让比例</th>
                                                <th class="ui-list-title zr4">转让时间</th>
                                                <th class="ui-list-title zr5">剩余期限</th>
                                                <th class="ui-list-title zr6">已转让金额</th>
                                                <th class="ui-list-title zr7">累计收到金额</th>
                                                <th class="ui-list-title zr8">说明</th>
                                                <th class="ui-list-title zr9" style="padding-left: 20px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="creditList">
                                        </tbody>
                                    </table>
                                    <div class="pages-nav" id="mycreditlist-pagination"></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="tab-parents">
                        <ul class="tab-divul2">
                            <li class="tab-div-li active-in currentHoldPlanClass" onclick="onShow(this)"><a href="javascript:void(0)">持有中
                            (<span id="currentHoldPlanCount">0</span>)
                            </a></li>
                            <li class="tab-div-li repayMentPlanClass" onclick="onShow(this)"><a href="javascript:void(0)">已退出
                            (<span id="repayMentPlanCount">0</span>)
                            </a></li>
                        </ul>
                        <ul class="tab-divin2">
                            <li>
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">服务名称</th>
                                                <th class="ui-list-title pl2">期限/参考年回报率</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)" class="currentHoldPlanOrderBy" data-val="1">授权服务金额<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">参考回报</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)" class="currentHoldPlanOrderBy" data-val="2">授权服务时间<i class="icon iconfont icon-jiantou" ></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="currentHoldPlan">
                                        </tbody>
                                    </table>
                                    <div class="pages-nav" id="currentHoldPlan-pagination"></div>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">服务名称</th>
                                                <th class="ui-list-title pl2">期限/参考年回报率</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)" class="repayMentPlanOrderBy" data-val="1">授权服务金额<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)" class="repayMentPlanOrderBy" data-val="2">已获回报<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)" class="repayMentPlanOrderBy" data-val="3">回款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody id="repayMentPlan">
                                        </tbody>
                                    </table>
                                    <div class="pages-nav" id="repayMentPlan-pagination"></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="wraper"></div>
                <div class="wraper-main">
                    <div class="top">
                        <p class="fn-left" id="bidNid"></p>
                        <span class="fn-btn" id="creditStatusNow">部分转让</span>
                        <i onclick="closePop()" class="iconfont icon-chahao1"></i>
                        <p class="fn-time" id="creditTime"></p>
                    </div>
                    <div class="fn-info">
                        <div class="fn-div">
                            <p class="fn-div-lg" id="creditCapital">0.00</p>
                            <p class="fn-div-text">原始转让本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="assignCapital">0.00</p>
                            <p class="fn-div-text">已转让本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="assignInterestAdvance">0.00</p>
                            <p class="fn-div-text">已收垫付利息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="creditFee">0.00</p>
                            <p class="fn-div-text">已付服务费</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg value" id="moneyGet">0.00</p>
                            <p class="fn-div-text">累计到账金额</p>
                        </div>
                    </div>
                    <div class="ui-list-header">转让明细</div>
                    <div class="ui-box">
                        <table>
                            <thead>
                                <tr>
                                    <th class="ui-list-title pl1">用户</th>
                                    <th class="ui-list-title pl2">购买本金</th>
                                    <th class="ui-list-title pl3">垫付利息</th>
                                    <th class="ui-list-title pl4">实付金额</th>
                                    <th class="ui-list-title pl5">服务费</th>
                                    <th class="ui-list-title pl6">到账金额</th>
                                    <th class="ui-list-title pl7">承接时间</th>
                                    <th class="ui-list-title pl8">操作</th>
                                </tr>
                            </thead>
                            <tbody id="assignedList">
                            </tbody>
                        </table>
                    </div>
                    <div class="arr">
                        <p>注</p>
                        <ul>
                            <li>1. 实付金额=购买本金*（1-折让率）+垫付利息；</li>
                            <li>2. 服务费=实付金额 * 服务费率）；</li>
                            <li>3. 到账金额=实付金额-手续费.</li>
                        </ul>
                    </div>
                    <div class="fn-bom"><a href="" class="ui-btn">确认</a></div>
                </div>
                <div class="wraper-main-con">
                    <div class="top">
                        <p class="fn-left" id="borrow_nid"></p>
                        <i onclick="closePop2()" class="iconfont icon-chahao1"></i>
                        <p class="fn-time">出借时间：<span id="add_time"></span></p>
                    </div>
                    <div class="fn-info">
                        <div class="fn-div">
                            <p class="fn-div-lg  value" id="recoverAccountYes">0.00</p>
                            <p class="fn-div-text">已收本息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="recoverCapitalWait">0.00</p>
                            <p class="fn-div-text">待收本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="recoverInterestWait">0.00</p>
                            <p class="fn-div-text" >待收利息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg" id="recoverAccountWait">0.00</p>
                            <p class="fn-div-text value" >待收本息</p>
                        </div>
                    </div>
                    <div class="ui-list-header">还款计划</div>
                    <div class="ui-box">
                        <table>
                            <thead>
                                <tr>
                                    <th class="ui-list-title pc1">期数</th>
                                    <th class="ui-list-title pc2">待收本息</th>
                                    <th class="ui-list-title pc3">待收本金</th>
                                    <th class="ui-list-title pc4">待收利息</th>
                                    <th class="ui-list-title pc5">待收时间</th>
                                    <th class="ui-list-title pc6">已收本息</th>
                                    <th class="ui-list-title pc7 awaylengh">状态</th>
                                </tr>
                            </thead>
                            <tbody id="currentHoldRepayMentPlan">
                            </tbody>
                        </table>
                    </div>
                    <div class="fn-bom"><a href="" class="ui-btn btn">确认</a></div>
                </div>
            </div>
            <!-- end 内容区域 -->
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/echarts.common.min.js"></script>
	<script src="${cdx}/js/user/assetmanage/right-manage.js?version=${version}" type="text/javascript" charset="utf-8"></script>
	<!-- 设置定位  -->
	<script>setActById("mytender");</script>
</body>
</html>