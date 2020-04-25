<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇添金 - 账户总览 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <div class="user-credit-banner">
        <div class="hd-innernav">
            <jsp:include page="/subMenu.jsp"></jsp:include>
        </div>
        <div class="container-1200">
           	 <div class="htj-detail-title">${planinfo.debtPlanNid }  &nbsp; &nbsp;  <a href="${ctx}/user/planinfo/xieyi.do?planNid=${planinfo.debtPlanNid }&planOrderId=${planinfo.accedeOrderId}" class="show-term" target="_blank"><span class="highlight" style="font-size:22px;">计划协议</span></a></div>
            <div class="touzi-con htj htj-detail">
                <div class="item">
                    <div class="tit">加入金额：${planinfo.accedeAccount }元</div>
                    <div class="tit">加入时间：${planinfo.createTimeFen }</div>
                </div>
                <div class="item">
                    <div class="tit">历史年回报率：${planinfo.expectApr }%</div>
                    <div class="tit">退出日期：${planinfo.liquidateShouldTime }</div>
                </div>
                <div class="item">
                    <div class="tit">锁定期：${planinfo.debtLockPeriod }个月</div>
                    <div class="tit">最晚到账日期：${planinfo.lastRepayTime }</div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>
    <div class="project-tabbing htj-detail-tabbing" style="padding-top:30px;">
        <div class="container-1200">
            <div class="htj-detail-summary sdz">  <!-- sgz:申购中,sdz:锁定中,ytc:已退出 -->
                <div class="title">资产统计</div>
                <div class="clearfix"></div>
                <div class="content"><span>累计投资金额：<span class="highlight">${investSum }</span>元</span><span>历史回报：<span class="highlight">${expectIntrest }</span>元</span></div>
            </div>
            <div class="panel-head">持有项目列表</div>
            <ul class="project-tab-panel">
                <li class="active">
                    <table>
                        <tbody>
                            <tr>
                                <th>项目编号</th>
                                <th>项目总期限</th>
                                <th>投资金额</th>
                                <th>投资时间</th>
                                <th>预计退出时间</th>
                                <th width="120">操作</th>
                            </tr>
                            <c:choose>
								<c:when test="${empty debtInvestList}">
									<tr><td colspan="13">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
								<c:forEach items="${debtInvestList }" var="record" begin="0" step="1" varStatus="status">
									<tr>
									<c:if test="${record.type eq '1'}">
									<td class="center"><a href="${ctx}/credit/planwebcredittender.do?creditNid=${record.borrowNid }" target="_blank">HZR${record.borrowNid }</a></td>
									<td class="center"><c:out value="${record.borrowPeriod }"></c:out>天</td>
									</c:if>
									<c:if test="${record.type eq '0'}">
									<td class="center"><a href="${ctx}/project/getHtjProjectDetail.do?borrowNid=${record.borrowNid }" target="_blank">${record.borrowNid }</a></td>
									<td class="center"><c:out value="${record.borrowPeriod }"></c:out>
									<c:if test="${record.isDay eq '1'}">天</c:if>
										<c:if test="${record.isDay eq '0'}">个月</c:if>
									</td>
									</c:if>
										<td class="center"><c:out value="${record.account }"></c:out></td>
										<td class="center"><c:out value="${record.createTime }"></c:out></td>
										<td class="center"><c:out value="${record.expectExitTime }"></c:out></td>
										 <td>	
										 <c:if test="${record.type eq '1'}">
										 <a href="${ctx}/credit/planusercreditcontract.do?creditNid=${record.borrowNid }&creditTenderNid=${record.orderId }" target="_blank" class="highlight">债转协议</a>
										</c:if>
										<c:if test="${record.type eq '0'}">
									 	<c:if test="${not empty record.status and record.status ne '0' }">
										 <a href="${ctx}/user/planinfo/goConDetail.do?borrowNid=${record.borrowNid }&nid=${record.orderId }" target="_blank" class="highlight">投资协议</a>
										 </c:if>
										</c:if>
										</td>
									</tr>
								</c:forEach>
								</c:otherwise>
							</c:choose>
                        </tbody>
                    </table>
                </li>

                
                
            </ul>
        </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <script>
    subMenu("planinfo");
        $(".project-tab").click(function(e) {
            var _self = $(e.target);
            if (_self.is("li")) {
                var idx = _self.attr("panel");
                var panel = _self.parent().siblings(".project-tab-panel");
                _self.siblings("li.active").removeClass("active");
                _self.addClass("active");
                panel.children("li.active").removeClass("active");
                panel.children("li[panel="+idx+"]").addClass("active");
            }
        })
    </script>
</body>

</html>
