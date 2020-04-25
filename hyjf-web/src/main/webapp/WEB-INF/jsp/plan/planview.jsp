<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇添金 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${cdn}/css/baguetteBox.min.css" />
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <div class="new-detail-con">
        <div class="new-detail-inner">
            <h4>
                <span class="title">计划编号：${planDetail.planNid }</span>
                <span class="date"><span>起投金额：${planIntroduce.planAccedeMinAccount }元</span><span>倍增投资金额：${planIntroduce.planAccedeIncreaseAccount } 元</span></span>
            </h4>
            <div class="new-detail-hd">
                <div class="hd1">
                    <div class="con1" style="width: 300px;">
                        <div class="num">
                            <fmt:formatNumber value="${planDetail.planAccount }" pattern="#,###" />
                            <span>元</span>
                        </div>
                        <div class="con-title">计划金额</div>
                    </div>
                    <div class="con2" style="width: 235px;">
                        <div class="num highlight">
                            ${planDetail.planApr }<span>%</span>
                        </div>
                        <div class="con-title">平台建议的历史年回报率
                            <span class="icon-info">
                                <em>平台建议的历史年回报率仅供投资人参考，不代表未来实际收益。</em>
                            </span>
                        </div>
                    </div>
                    <div class="con3">
                        <div class="num">${planDetail.planPeriod }
                            <span>个月</span>
                        </div>
                        <div class="con-title">锁定期</div>
                    </div>
                </div>
                <div class="hd2">
                		汇添金计划项目预览
                </div>
                <div class="hd3">
                    <div class="infor">
                        <span class="icon-safe"></span> 安全保障计划
                    </div>
                    <div class="infor">
                        	项目历史回报：
                        <span class="highlight">¥ <fmt:formatNumber value="${planDetail.planInterest}" pattern="#,###.00" /></span>元
                    </div>
                    <div class="infor">申购开始时间：${planDetail.buyBeginTime }</div>
                    <div class="infor">申购结束时间：${planDetail.buyEndTime }</div>
                    <div class="infor infor-font">温馨提示：市场有风险，投资需谨慎</div>
                    <div class="infor infor-font">建议投资者类型：稳健型及以上</div>
                </div>
            </div>
            <div class="new-detail-main">
                <ul class="new-detail-tab">
                    <li panel="0" class="active">计划介绍</li>
                    <li panel="1" >债权列表</li>
                    <li panel="2" >加入记录</li>
                    <li panel="3">安全保障</li>
                    <li panel="4">常见问题</li>
                </ul>
                <ul class="new-detail-tab-panel">
                    <li panel="0" class="active">
                        <dl class="new-detail-dl">
                            <dt>计划概念</dt>
                            <dd>${planIntroduce.planConcept }</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>计划原理</dt>
                            <dd>
                               ${planIntroduce.planTheory }
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>计划说明</dt>
                            <dd>
                                <table class="new-detail-tb1" cellspacing="0" cellpadding="0" border="0">
                                    <tr>
                                        <th>计划名称</th>
                                        <td>${planIntroduce.planName }</td>
                                    </tr>
                                    <tr>
                                        <th>投资范围</th>
                                        <td>请参考债权列表</td>
                                    </tr>
                                    <tr>
                                        <th>加入条件</th>
                                        <td>${planIntroduce.planAccedeMinAccount } 元起，以 ${planIntroduce.planAccedeIncreaseAccount } 元的倍数递增</td>
                                    </tr>
                                    <tr>
                                        <th>加入上限</th>
                                        <td>${planIntroduce.planAccedeMaxAccount }元</td>
                                    </tr>
                                    <tr>
                                        <th>历史回报</th>
                                        <td>平台建议的历史年回报率${planDetail.planApr }％，实际收益以运营情况为准。</td>
                                    </tr>
                                    <tr>
                                        <th>退出方式</th>
                                        <td>到期退出</td>
                                    </tr>
                                    <tr>
                                        <th>退出日期</th>
                                        <td>锁定期结束后，开始清算退出</td>
                                    </tr>
                                    <tr>
                                        <th>到账时间</th>
                                        <td>退出日期后，预计${planDetail.debtQuitPeriod }个工作日内到账</td>
                                    </tr>
                                    <tr>
                                        <th>服务费</th>
                                        <td>参见《汇添金投资服务协议》</td>
                                    </tr>
                                </table>
                            </dd>
                        </dl>
                    </li>
                    <li panel="1">
                        <div class="new-detail-tb-t">本计划投资的项目限汇添金专属资产和转让资产；债权列表动态变化，具体以实际投资为准。</div>
                          <table class="new-detail-tb">
                            <thead id="projectConsumeListHead">
                            </thead>
                            <tbody id="projectConsumeList">
                            </tbody>
                        </table>
	                        <div class="clearfix"></div>
							<div class="new-pagination" id="consume-pagination"></div>
                    </li>
                    <li panel="2">
                        <div class="new-detail-tb-t">加入总人次： <strong  id="investTimes"></strong> 加入金额：<strong  id="investTotal"></strong>元 </div>
                        <table class="new-detail-tb">
                            <thead id="projectInvestListHead">
                            </thead>
                            <tbody id="projectInvestList">
                            </tbody>
                        </table>
                        <div class="clearfix"></div>
						<div class="new-pagination" id="invest-pagination"></div>
                    </li>
                    <li panel="3">
                        <dl class="new-detail-dl">
                            <dt>风控保障措施</dt>
                            <dd> ${planRiskControl.controlMeasures }</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>风险保证金措施</dt>
                            <dd>${planRiskControl.controlBail }</dd>
                        </dl>
                    </li>
                    <li panel="4">
                        <dl class="new-detail-dl">
                            <dd>
                               ${planQuestion.question }
                               </dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/baguetteBox.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/plan/planDetail.js?version=${version}" charset="utf-8"></script>
    
</body>

</html>
