<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
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
        <div class="container">
            <!-- start 内容区域 -->  
            <div class="plan-banner">
            	<div class="newzone-title">智投专区</div>
                <div class="newzone-article">分散投资 ， 期限固定</div>
                <div class="newzone-btn">享有用户保障机制</div> 
            </div>
             <div class="bond-investlist">
                <div class="plan-attr">          
	                <c:forEach items="${htjRecordList }" var="record" begin="0" step="1" varStatus="status">     	
		                    <div class="plan-attritem">
		                        <div class="plan-title">
		                            <p class="plan-title-fl">${record.planName }</p>
		                            <p class="plan-title-fr"><span>每万元收益</span><span class="plan-color">${record.income }</span><span>元</span></p>
		                        </div>
		                        <div class="plan-content">
		                            <div class="plan-item-fl">
		                            	<c:set value="${ fn:split(record.planApr, '.') }" var="planAprStr" />
		                                <p><span class="lg">${planAprStr[0]}</span><span class="sm">.${planAprStr[1]}</span><span class="xs">%</span></p>
		                                <div class="pos">历史年回报率</div>
		                            </div>
		                            <div class="line"></div>
		                            <div class="plan-item-md">
		                                <div class="attr2-val">
		                                    <span class="md">${record.planPeriod }</span>
		                                    <span class="xs">个月</span>
		                                </div>                              
		                                <div class="pos">计划期限</div>
		                            </div>
		                            <div class="line"></div>
		                            <div class="plan-item-fr">
		                                <div class="attr2-val">
		                                    <span class="md">${record.planAccount }</span>
		                                    <span class="xs">元</span>
		                                </div>
		                                <div class="pos">计划金额</div> 
		                            </div>
		                        </div>
		                        <a href="${ctx}/plan/getPlanDetail.do?planNid=${record.planNid}"  class="join-btn" itemid="pll${record.status+1}">
		                     	    <c:choose>
										<c:when test="${record.status == '0' || record.status == '1' }">
											立即加入 
										</c:when>
										<c:when test="${record.status == '2' || record.status == '3'}">
											查看详情
										</c:when>
										<c:otherwise>
											查看详情
										</c:otherwise>
									</c:choose>
								</a>
		                    </div>
	                    </a>
	                </c:forEach>  
                </div>
                
                
                <!--投资数据-->
                <div class="plan-data-attr">
                    <div class="plan-data">
                        <div class="paln-invest-title">
                            <p class="plan-title-fl"><span class="icon1"></span>汇添金投资数据</p>
                            <p class="plan-title-fr">数据来源汇盈金服内部统计，实时更新</p>
                        </div>
                        <div class="fn-clear ui-invest-dl-info pl25 pr25 mt10 pt10"> 
                        <dl class="text-center  border-right-gray">
                            <dt class="text-big"><span class="cross-line"></span>&nbsp;加入汇添金计划&nbsp;<span class="cross-line"></span></dt>
                            <dd>
                           		<c:set value="${ fn:split(htjInvestData.totalAmount, '.') }" var="totalAmountStr" />
	                            <em class="font-36px">${totalAmountStr[0]}</em>
	                            <em class="text-small">.${totalAmountStr[1]}</em>
	                            <em class="unit">万元</em>
                            </dd>
                        </dl>
                        <dl class="text-center border-right-gray">
                            <dt class="text-big"><span class="cross-line"></span>&nbsp;为用户赚取&nbsp;<span class="cross-line"></span></dt>
                            <dd>
                           		<c:set value="${ fn:split(htjInvestData.totalEarnAmount, '.') }" var="totalEarnAmountStr" />
	                            <em class="font-36px">${totalEarnAmountStr[0]}</em>
	                            <em class="text-small">.${totalEarnAmountStr[1]}</em>
	                            <em class="unit">万元</em>
                            </dd>
                        </dl>
                        <dl class="text-center ">
                            <dt class="text-big"><span class="cross-line"></span>&nbsp;加入总人次&nbsp;<span class="cross-line"></span></dt>
                            <dd>
                            <em class="font-36px">${htjInvestData.totalJoinNum }</em>
                            <em class="unit">次</em>
                            </dd>
                        </dl>
                    </div>
                </div>
                <div class="bond-list" id="bond-list">
                    <div class="plan-icon"><span class="icon2"></span>历史期数与收益</div>
                    
                    <div class="bond-thead">
                        <div>
                            <div class="bond-div pt1">计划名称</div>
                            <div class="bond-div pt2">历史年回报率</div>
                            <div class="bond-div pt3">计划金额</div>
                            <div class="bond-div pt4">计划期限</div>
                            <div class="bond-div pt5" style="padding-left: 30px;">进度</div>
                            <div class="bond-div pt6">状态</div>
                        </div>
                    </div>
                    <div id="planList">
                    	<ul>
                            <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                        </ul>
                    </div>
                    <!--分页-->
			        <div class="pages-nav" id="new-pagination"></div>  
                </div> 
             </div>   
             
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 导航栏定位  -->
	<script>setActById("indexPlan");</script>
	<script src="${cdn}/js/plan/planList.js?version=${version}" type="text/javascript"></script>
</body>
</html>
