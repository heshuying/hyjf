<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<input type="hidden" id="loginFlag" value="${loginFlag}">
	<article class="main-content idx-content">
        <section class="section-banner">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                	<c:forEach items="${bannerList}" var="record" begin="0" step="1" varStatus="status">
                		<a class="swiper-slide" href="${record.url}" style="background-image: url(${cdn}${record.image});"></a>
					</c:forEach>
                </div>
                <div class="swiper-pagination"></div>
            </div>
            <div class="container">
            	<!-- 未登陆 -->
	            <c:if test="${loginFlag == '0' }">
	                <div class="data-show status1">
	                    <p class="pre-desc">历史年回报率最高</p>
	                    <div class="money">
	                        <div class="money-cen">
	                        	10.<span class="num-f">50</span><span>%</span>
	                        </div>
	                    </div>
	                     <a class="red" href="${ctx}/user/regist/init.do" itemid="ds1">注册领888元礼包</a>
	                </div>
	             </c:if>
                <!-- 已登录并已开户 -->
                <c:if test="${loginFlag == '1' && openFlag == '1' }">
	                <div class="data-show status2">
	                    <p class="pre-desc">
	                    <i class="<c:if test="${helloFlag == 0}">day</c:if><c:if test="${helloFlag == 1}">afternoon</c:if><c:if test="${helloFlag == 2}">night</c:if>"></i>
	                    	${helloWord}
	                    </p>
	                    <div class="line"></div>
	                    <p class="desc">您已累计赚取收益<i class="i-eye"></i></p>
	                    <div class="val"><span>¥</span><c:out value="${userInterest}"></c:out></div>
	                    <div class="val hide P-t7">******</div>
	                    <p class="coupon-tip">您有 <a href="${ctx}/user/invite/toInvite.do" itemid="ds2"><c:out value=" ${couponCount}"></c:out></a> 张优惠券<c:if test="${couponCount > 0}">还未使用</c:if></p>
	                    <a class="red" href="${ctx}/user/pandect/pandect.do" itemid="ds1">进入我的账户</a>
	                </div>
                </c:if>

                <!-- 登录未开户 有优惠券-->
                <c:if test="${loginFlag == '1' && openFlag == '0' && couponCount > 0 }">
                    <div class="data-show status3">
	                    <p class="pre-desc">欢迎来到汇盈金服</p>
	                    <div class="line"></div>
	                    <p class="pre-desc">历史年回报率<span>11%</span></p>
	                  	<p class="coupon-tip">您有 <a href="${ctx}/user/invite/toInvite.do" itemid="ds2"><c:out value=" ${couponCount}"></c:out></a> 张优惠券<c:if test="${couponCount > 0}">还未使用</c:if></p>
	                    <a class="red" href="${ctx}/bank/web/user/bankopen/init.do" itemid="ds1">立即开户</a>
	                </div>
                </c:if>
                 <!-- 登录未开户 无优惠券-->
                <c:if test="${loginFlag == '1' && openFlag == '0' && couponCount == 0 }">
                    <div class="data-show status4">
	                    <p class="pre-desc">欢迎来到汇盈金服</p>
	                    <div class="coupon"></div>
	                    <a class="red" href="${ctx}/bank/web/user/bankopen/init.do" itemid="ds1">立即开户</a>
	                </div>
                </c:if>
            </div>
        </section>
        <section class="section-idx-notice">
            <div class="container">
                <div class="icon iconfont icon-tongzhi"></div>
                <div class="notice-wrapper">
                    <a href="${ctx }/contentarticle/getNoticeInfo.do?id=${noticeInfo.id }" itemid="nt1" class="notice-item"> <c:out value="${noticeInfo.title }"></c:out>  <span> <fmt:formatDate value="${noticeInfo.createTime }" pattern="yyyy-MM-dd" /></span></a>
                </div>
                <a href="${ctx }/contentarticle/siteNotices.do" itemid="ntm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
            </div>
        </section>
        <section class="section-main">
            <div class="container">
            	<div class="chara-box">
                    <div class="title-main"><span class="title-icon chara1"></span> 品牌优势 </div>
                    <p>稳健运营${yearSum}年，累计为用户赚取${interestSum}亿元</p>
                </div>
                <%-- <div class="chara-box">
                    <div class="title-main"><span class="title-icon chara1"></span> 品牌优势 </div>
                    <p>平台成交额${tenderSum}亿 累计为用户赚取${interestSum}亿</p>
                </div> --%>
                <div class="chara-box">
                    <div class="title-main"><span class="title-icon chara2"></span> 美国上市</div>
                    <p>美国公众公司品牌，OTC股票代码：SFHD</p>
                </div>
                <div class="chara-box">
                    <div class="title-main"><span class="title-icon chara3"></span> 银行存管</div>
                    <p>江西银行资金存管，平台无法触碰用户资金</p>
                </div>
            </div>
            <div class="container">
                <div class="title-bar">
                    <span class="title-bar-main">新手专区</span>
                    <span class="line"></span>
                    <span>新手通道 出借专享</span>
                    <a href="${ctx}/bank/web/borrow/newBorrowList.do" itemid="pr-ztm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
                </div>
                <c:forEach items="${newProjectList}" var="record" begin="0" step="1" varStatus="status">
                <c:choose>
					<c:when test="${record.status == '11' }">
						<a class="newcomer"   href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${record.borrowNid}">
					</c:when>
					<c:otherwise>
						<a class="newcomer"   href="${ctx}/bank/web/borrow/newBorrowList.do">
					</c:otherwise>
				</c:choose>

                	<div class="attr-box">
						<div class="attr-item attr1">
							<c:set value="${ fn:split(record.borrowApr, '.') }" var="borrowAprStr" />
							<p class="rate"> <span class="lg">${borrowAprStr[0]}</span><span class="sm">.${borrowAprStr[1]}</span>
								<span class="xs">%<c:if test="${record.isIncrease == true}">+${record.borrowExtraYield}%</c:if></span></p>
							<p class="alt">历史年回报率</p>

						</div>
                        <div class="attr-item attr2">
                            <p class="day"><span>${record.borrowPeriod}</span> ${record.borrowPeriodType}</p>
                            <p class="alt">项目期限</p>
                        </div>
                        <div class="attr-item attr3">
                            <p class="word">新手注册，奖励多多</p>
                        </div>
                        <div class="link">
                        	<c:choose>
								<c:when test="${record.status == '11' }">
									 立即出借
								</c:when>
								<c:otherwise>
									查看详情
								</c:otherwise>
							</c:choose>
                        </div>
                    </div>
                </a>
                </c:forEach>



                <%-- <div class="newcomer-content">
                    <c:forEach items="${newProjectList}" var="record" begin="0" step="1" varStatus="status">
						<a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${record.borrowNid}" class="newcomer-item <c:if test="${status.index == 0}">fst</c:if>" itemid="ncl${status.index+1}">
	                        <div class="title">${record.borrowNid}</div>
	                        <p><span class="tag">新手专享</span></p>
	                        <div class="attr">
	                            <div class="attr-item attr1">
	                            	<c:set value="${ fn:split(record.borrowApr, '.') }" var="borrowAprStr" />
	                                <span class="lg">${borrowAprStr[0]}</span><span class="sm">.${borrowAprStr[1]}</span> <span class="xs">%</span>
	                                <c:if test="${record.borrowExtraYield != null && record.borrowExtraYield ne '' && record.borrowExtraYield ne '0.00'}">
		                               	<div class="poptag-pos">
		                                    <div class="poptag">+${record.borrowExtraYield}%</div>
		                                </div>
		                            </c:if>
	                                <p>历史年回报率</p>
	                            </div>
	                            <div class="attr-item attr2">
	                                <span class="sm">${record.borrowPeriod}</span><span class="xs">${record.borrowPeriodType}</span>
	                                <p>项目期限</p>
	                            </div>
	                        </div>
	                        <div class="link"><div class="btn sm">
		                        <c:choose>
									<c:when test="${record.status == '11' }">
										 立即加入
									</c:when>
									<c:otherwise>
										查看详情
									</c:otherwise>
								</c:choose>
								<span class="iconfont icon-more"></span>
	                      	</div></div>
	                    </a>
	                    <c:if test="${status.index == 0}"><div class="line"></div> </c:if>
					</c:forEach>
                </div>
                --%>
            </div>
			<!-- 汇计划修改 -->
			<div class="container overflow-visible clearfloat">
				<div class="title-bar" style="height: 65px;position: relative;top: -3px;">
					<span class="title-bar-main"><img src="${ctx }/dist/images/hot-plan@2x.png" width="119" height="32"  alt="" /></span>
					<span class="line"></span>
					<span>分散投标 省时省力</span>
					<a href="${ctx}/hjhplan/initPlanList.do" itemid="pr-plm" class="bar-more" style="position: relative;top: 12px;">查看更多<span class="icon iconfont icon-more"></span></a>
				</div>
				<div class="plan-list">
					<ul class="clearfloat">
						<li class="plan-banner">
                    		<a>
                    			<img src="${cdn}/dist/images/planbanner@2x.png" width="212" height="315" />
                    		</a>
                    	</li>
                        <c:forEach items="${hjhPlanList}" var="record" begin="0" step="1" varStatus="status">
							<li>
								<a href="${ctx}/hjhdetail/getPlanDetail.do?planNid=${record.planNid}" >
									<div class="title"><i class="iconfont  icon-shijian"></i> ${record.planPeriod}
										<c:if test="${record.isMonth eq 0}">
											天
										</c:if>
										<c:if test="${record.isMonth eq 1}">
											个月
										</c:if>
									</div>
									<div class="attr-item attr1">
										<c:set value="${ fn:split(record.planApr, '.') }" var="planAprStr" />
										<span class="lg">${record.planApr}</span><span class="xs">%<%--<c:if test="${record.isIncrease ==  'true'}">+${record.borrowExtraYield}%</c:if>--%></span>
										<!--<div class="poptag-pos">
											<div class="poptag">+1%</div>
										</div>-->
										<p>参考年回报率</p>
									</div>
									<div class="attr-item attr2">
										<div class="residue-box">
                                    		<div class="attr2-key">开放额度</div>
	                                    	<div class="attr2-val"><span class="md">${record.availableInvestAccount}</span> <span class="xs">元</span></div>
	                                    </div>
                                    </div>
			                        <c:if test="${record.statusName eq '稍后开启'}">
			                        	<div class="link later">${record.statusName}</div>
			                        </c:if>
			                        <c:if test="${record.statusName ne '稍后开启'}">
			                        	<div class="link">授权服务</div>
			                        </c:if>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<!-- 汇计划修改end -->
            <div class="container overflow-visible">
                <div class="title-bar">
                    <span class="title-bar-main">散标推荐</span>
                    <span class="line"></span>
                    <span>项目直投 自主选择</span>
                    <a href="${ctx}/bank/web/borrow/initBorrowList.do" itemid="pr-ztm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
                </div>
                <div class="list-table">
                    <ul class="table">
                        <li class="thead">
                                <div class="th w1">历史年回报率</div>
                                <div class="th w2">项目编号</div>
                                <div class="th w3">项目期限</div>
                                <div class="th w4">项目金额</div>
                                <div class="th w5">状态</div>
                        </li>
                        <c:forEach items="${projectList}" var="record" begin="0" step="1" varStatus="status">
                            <li class="tr">
	                            <a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid=${record.borrowNid}" itemid="bnl${record.status+1}">
									<div class="td w1">
										<span>${record.borrowApr}</span> %<c:if test="${record.isIncrease ==  'true'}">+${record.borrowExtraYield}%</c:if>
									</div>
	                                <div class="td w2">
	                                	${record.borrowNid}
	                                </div>
	                                <div class="td w3">${record.borrowPeriod}${record.borrowPeriodType}</div>
	                                <div class="td w4">${record.borrowAccount} 元</div>
	                                <div class="td w5">
			                            <c:choose>
											<c:when test="${record.status == '10' }">
												<div class="dark"><div class="timeout" id="list${record.status+1}" data-start="${nowTime}" data-end="${record.time}"></div></div>
											</c:when>
											<c:when test="${record.status == '11' }">
												<div class="btn sm">出借</div>
											</c:when>
											<c:when test="${record.status == '12' }">
												<span class="dark">复审中</span>
											</c:when>
											<c:when test="${record.status == '13' }">
												<span class="dark">还款中</span>
											</c:when>
											<c:otherwise>
												查看详情
											</c:otherwise>
										</c:choose>
	                                </div>
	                            </a>
	                        </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <%-- <div class="container overflow-visible">
                <div class="title-bar">
                    <span class="title-bar-main">计划推荐</span>
                    <span class="line"></span>
                    <span>分散投资 期限固定</span>
                    <a href="${ctx}/plan/initPlanList.do" itemid="pr-plm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
                </div>
                <div class="plan-list">
                    <ul>
                    	<c:forEach items="${planList}" var="record" begin="0" step="1" varStatus="status">
	                         <li>
	                            <a href="${ctx}/plan/getPlanDetail.do?planNid=${record.planNid}" itemid="pll${record.status+1}">
	                                <div class="title">${record.planName}</div>
	                                <div class="attr-item attr1">
	                                	<c:set value="${ fn:split(record.planApr, '.') }" var="planAprStr" />
	                                    <span class="lg">${planAprStr[0]}</span><span class="sm">.${planAprStr[1]}</span> <span class="xs">%</span>
	                                    <p>历史年回报率</p>
	                                </div>
	                                <div class="attr-item attr2">
	                                    <div class="attr2-key">项目期限</div>
	                                    <div class="line"></div>
	                                    <div class="attr2-val"><span class="md">${record.planPeriod}</span> <span class="xs">个月</span></div>
	                                </div>
	                                <div class="link">
	           			                 <c:choose>
											<c:when test="${record.status == '0' || record.status == '1'}">
												立即加入
											</c:when>
											<c:when test="${record.status == '2' || record.status == '3'}">
												查看详情
											</c:when>
											<c:otherwise>
												查看详情
											</c:otherwise>
										</c:choose>
	                                <span class="iconfont icon-more"></span></div>
	                            </a>
	                        </li>
						</c:forEach>
                    </ul>
                </div>
                <div class="clearfloat"></div>
            </div> --%>
            <div class="container">
                <div class="news-content">
                    <div class="title-bar">
                        <span class="title-bar-main">公司动态</span>
                        <a href="${ctx}/contentarticle/getCompanyDynamicsList.do" itemid="newsm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
                    </div>
                    <div class="first-item">
                        <a href="${ctx}/contentarticle/getCompanyDynamicsDetail.do?id=${companyArticle.id}" itemid="nsl1">
                            ${companyArticle.title }
                        </a>
                    </div>
                    <div class="news-list">
                        <div class="first-li">
                            <a href="${ctx}/contentarticle/getCompanyDynamicsDetail.do?id=${companyArticle.id}" class="title" itemid="nsl2">${companyArticle.title }</a>
                            <p>
                            <c:choose>
							     <c:when test="${fn:length(companyArticle.content) > 50}">
							     	 <c:out value="${fn:substring(companyArticle.content, 0, 50)}...." />
							     </c:when>
							     <c:otherwise>
							     	 <c:out value="${companyArticle.content}" />
							     </c:otherwise>
						    </c:choose>
                            </p>
                        </div>
                        <ul>
                         	<c:forEach items="${companyDynamicsList}" var="record" begin="0" step="1" varStatus="status">
                         		<li><a href="${ctx}/contentarticle/getCompanyDynamicsDetail.do?id=${record.id}" itemid="nsl3"><i class="icon"></i>
                         		        <c:choose>
										     <c:when test="${fn:length(record.title) > 26}">
										     	 <c:out value="${fn:substring(record.title, 0, 26)}..." />
										     </c:when>
										     <c:otherwise>
										     	 <c:out value="${record.title}" />
										     </c:otherwise>
									    </c:choose>
                         			</a>
                         		<span><fmt:formatDate value="${record.createTime }" pattern="yyyy-MM-dd" /></span></li>
                         	</c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="report-content">
                    <div class="title-bar">
                        <span class="title-bar-main">运营报告</span>
                        <a href="${ctx}/aboutus/report.do" itemid="reportm" class="bar-more">查看更多<span class="icon iconfont icon-more"></span></a>
                    </div>
                    <div class="report-first">
                        <a href="${ctx}/aboutus/report.do" itemid="rp1">
                            <div class="cp">汇盈金服</div>
                            <div class="cn">平台运营报告</div>
                            <div class="en">MONTHY OPERATION REPORT</div>
                        </a>
                    </div>
                </div>
            </div>
        </section>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	
	 <!-- 导航栏定位  -->
	<script>
		var swiperUri = "${cdn}/dist/js/lib/swiper3.jquery.min.js";
		if($.browser.msie && $.browser.version < 10){
			swiperUri = "${cdn}/dist/js/lib/swiper.jquery.min.js";
		}
		$('body').append($("<script>").attr("src",swiperUri));
		setActById("indexHome");
    </script>
    <script src="${cdn}/dist/js/index.js?version=${version}"></script>
	
</body>
</html>
