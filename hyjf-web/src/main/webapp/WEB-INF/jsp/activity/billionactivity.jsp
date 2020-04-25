<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/active/billionActive.css?version=${version}" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="ten-billion-banner">
			<div class="ten-billion-bg01"></div>
			<div class="ten-billion-bg02"></div>
			<div class="ten-billion-bg03"></div>
			<div class="account-data" style="display: none;" data-txt="${amount}"></div>
			<div class="ten-billion-cont">
				<div><img class="ten-billion-img" src="${ctx}/img/active/billion/account-digital01.png" alt="" /></div>
				<div class="account-content">
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div calss="billion-douhao"><img src="${ctx}/img/active/billion/billion-douhao.png"/></div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div calss="billion-douhao"><img src="${ctx}/img/active/billion/billion-douhao.png"/></div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div calss="billion-douhao"><img src="${ctx}/img/active/billion/billion-douhao.png"/></div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
					 <div class='account-invest'>
					 	<div class='account-invest-digital'></div>
					 </div>
				</div>
			    <div><img class="ten-billion-img" src="${ctx}/img/active/billion/account-digital02.png" alt="" /></div>
		    </div> 
		</div>
	    <div class="ten-billion-main">
	    	<div class="billion-active-tab">
	    		<ul>
	    			<li  data-index="0" <c:if test='${activityFlag==1}'>class="activeTab"</c:if>>
	    				<a href="javascript:;">满心满亿好运好礼</a>
	    				<p><span></span></p>
	    			</li>
	    			<li data-index="1" <c:if test='${activityFlag==2}'>class="activeTab"</c:if>>
	    				<a href="javascript:;">助力百亿感恩有你</a>
	    				<p><span></span></p>
	    			</li>
	    			
	    			<li data-index="2" <c:if test='${activityFlag==3 && billionThirdStarFlag==1}'>class="activeTab"</c:if>>
	    				<a href="javascript:;">百亿狂欢秒杀开启</a>
	    				<p><span></span></p>
	    			</li>
	    			<li data-index="3">
	    				<a href="javascript:;">用心聆听更好服务</a>
	    				<p><span></span></p>
	    			</li>
	    			<li data-index="4">
	    				<a href="javascript:;">从心起航遇见未来</a>
	    				<p><span></span></p>
	    			</li>
	    		</ul>
	    		<div class="clearboth"></div>
	    	</div>
	    	<div class="billion-active-list">
		        <div class="active-section-0" <c:if test='${activityFlag==1}'>style="display: block;"</c:if>>
		        	<div class="first-active-top"></div>
		        	<div class="first-active-ban">
		        		<ul>
		        			<c:forEach items="${billionOneRecords }" var="record" begin="0" step="1" varStatus="status">
		        				<c:if test="${record.accordMoney == 100 }">
				        			<li <c:if test="${record.status != 2}">class="reward-color-blue"</c:if>
				        				<c:if test="${record.status == 2}">class="reward-color-purple"</c:if> >
				        				<div class="first-active-full">
				        					<p>
				        						满<span>100亿</span>
				        					</p>
				        				</div>
				        				<div class="bianxian"></div>
				        				<div class="first-active-reward">
				        					<div class="active-reward-cont">
				        						<div class="active-reward-1">
				        							<p>
				        							Powerbeats3<br>
				        							by Dr.Dre Wireless<br>
				        							入耳式耳机<br>
				        							<span>（价值1488元）</span>
				        							</p>
				        						</div>
				        						<c:choose>
													<c:when test="${record.status == 2}">
														<c:choose> 
															<c:when test="${record.userId == sessionUserId }">
																<a class="win-gift"  href="javaScript:;">
																	<img src="${ctx}/img/active/billion/win-gift.png" alt="" />恭喜中奖<span><c:out value="${record.userName}"></c:out></span>
																</a>
															 </c:when>
															 <c:otherwise>
																<a href="javaScript:;"> 获奖用户<span class="prizeUserName"><c:out value="${record.userName}"></c:out></span></a>
															 </c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<a  href="${ctx}/project/initProjectList.do?projectType=HZT">立即投资 </a>
													</c:otherwise>
												</c:choose>
				        					</div>
				        				</div>
				        			</li>
		        				</c:if>
		        				<c:if test="${record.accordMoney == 101 }">
		        					<li <c:if test="${record.status == 0}">class="reward-color-orange"</c:if>
		        						<c:if test="${record.status == 1}">class="reward-color-blue"</c:if>
				        				<c:if test="${record.status == 2}">class="reward-color-purple"</c:if> >
				        			
				        				<div class="first-active-reward">
				        					<div class="active-reward-cont">
				        						<div class="active-reward-2">
				        							<p>
				        							iPad mini 4<br>
				        							<span>（价值2888元）</span>
				        							</p>
				        						</div>
				        						<c:choose>
													<c:when test="${record.status == 2}">
															<c:choose> 
																<c:when test="${record.userId == sessionUserId }">
																	<a class="win-gift"  href="javaScript:;">
																		<img src="${ctx}/img/active/billion/win-gift.png" alt="" />恭喜中奖<span><c:out value="${record.userName}"></c:out></span>
																	</a>
																 </c:when>
																 <c:otherwise>
																 	<a  href="javaScript:;">获奖用户<span class="prizeUserName"><c:out value="${record.userName}"></c:out></span></a>
																 </c:otherwise>
															</c:choose>
													</c:when>
													<c:when test="${record.status == 1}">
														<a href="${ctx}/project/initProjectList.do?projectType=HZT">立即投资 </a>
													</c:when>
													<c:otherwise>
														<a  href="javaScript:;">即将开始</a>
													</c:otherwise>
												</c:choose>
				        					</div>
				        				</div>
				        				<div class="bianxian"></div>
				        				<div class="first-active-full">
				        					<p>
				        						满 <span>101亿</span>
				        					</p>
				        				</div>
				        			</li>
		        				</c:if>
		        				<c:if test="${record.accordMoney == 102}">
				        			<li <c:if test="${record.status == 0}">class="reward-color-orange"</c:if>
		        						<c:if test="${record.status == 1}">class="reward-color-blue"</c:if>
				        				<c:if test="${record.status == 2}">class="reward-color-purple"</c:if> >
				        				<div class="first-active-full">
				        					<p>
				        						满<span>102亿</span>
				        					</p>
				        				</div>
				        				<div class="bianxian"></div>
				        				<div class="first-active-reward">
				        					<div class="active-reward-cont">
				        						<div class="active-reward-3">
				        							<p>
				        							Sony微单相机<br>
				        							  ILCE-6000L
				        							<span>（价值4299元）</span>
				        							</p>
				        						</div>
				        						<c:choose>
													<c:when test="${record.status == 2}">
															<c:choose> 
																<c:when test="${record.userId == sessionUserId }">
																	<a class="win-gift"  href="javaScript:;">
																		<img src="${ctx}/img/active/billion/win-gift.png" alt="" />恭喜中奖<span><c:out value="${record.userName}"></c:out></span>
																	</a>
																 </c:when>
																 <c:otherwise>
																 	<a  href="javaScript:;">获奖用户<span class="prizeUserName"><c:out value="${record.userName}"></c:out></span></a>
																 </c:otherwise>
															</c:choose>
													</c:when>
													<c:when test="${record.status == 1}">
														<a href="${ctx}/project/initProjectList.do?projectType=HZT">立即投资 </a>
													</c:when>
													<c:otherwise>
														<a  href="javaScript:;">即将开始</a>
													</c:otherwise>
												</c:choose>

				        					</div>
				        				</div>
				        			</li>
		        				</c:if>		        				
		        				
		        				<c:if test="${record.accordMoney == 103}">
		        					<li <c:if test="${record.status == 0}">class="reward-color-orange"</c:if>
		        						<c:if test="${record.status == 1}">class="reward-color-blue"</c:if>
				        				<c:if test="${record.status == 2}">class="reward-color-purple"</c:if> >
				        				<div class="first-active-reward">
				        					<div class="active-reward-cont">
				        						<div class="active-reward-4">
				        							<p>
				        							iPhone 7 Plus<br>
				        							<span>（价值6388元）</span>
				        							</p>
				        						</div>
				        						<c:choose>
													<c:when test="${record.status == 2}">
															<c:choose> 
																<c:when test="${record.userId == sessionUserId }">
																	<a class="win-gift"  href="javaScript:;">
																		<img src="${ctx}/img/active/billion/win-gift.png" alt="" />恭喜中奖<span><c:out value="${record.userName}"></c:out></span>
																	</a>
																 </c:when>
																 <c:otherwise>
																 	<a  href="javaScript:;">获奖用户<span class="prizeUserName"><c:out value="${record.userName}"></c:out></span></a>
																 </c:otherwise>
															</c:choose>
													</c:when>
													<c:when test="${record.status == 1}">
														<a href="${ctx}/project/initProjectList.do?projectType=HZT">立即投资 </a>
													</c:when>
													<c:otherwise>
														<a  href="javaScript:;">即将开始</a>
													</c:otherwise>
												</c:choose>
				        					</div>
				        				</div>
				        				<div class="bianxian"></div>
				        				<div class="first-active-full">
				        					<p>
				        						满 <span>103亿</span>
				        					</p>
				        				</div>
				        			</li>
		        				</c:if>
		        				<c:if test="${record.accordMoney == 104}">
		        					<li <c:if test="${record.status == 0}">class="reward-color-orange"</c:if>
		        						<c:if test="${record.status == 1}">class="reward-color-blue"</c:if>
				        				<c:if test="${record.status == 2}">class="reward-color-purple"</c:if> >
				        				<div class="first-active-full">
				        					<p>
				        						满<span>104亿</span>
				        					</p>
				        				</div>
				        				<div class="bianxian"></div>
				        				<div class="first-active-reward">
				        					<div class="active-reward-cont">
				        						<div class="active-reward-5">
				        							<p>
				        							Macbook<br>
				        							<span>（价值9888元）</span>
				        							</p>
				        						</div>
				        						<c:choose>
													<c:when test="${record.status == 2}">
															<c:choose> 
																<c:when test="${record.userId == sessionUserId }">
																	<a class="win-gift"  href="javaScript:;">
																		<img src="${ctx}/img/active/billion/win-gift.png" alt="" />恭喜中奖<span><c:out value="${record.userName}"></c:out></span>
																	</a>
																 </c:when>
																 <c:otherwise>
																 	<a  href="javaScript:;">获奖用户<span class="prizeUserName"><c:out value="${record.userName}"></c:out></span></a>
																 </c:otherwise>
															</c:choose>
													</c:when>
													<c:when test="${record.status == 1}">
														<a href="${ctx}/project/initProjectList.do?projectType=HZT">立即投资 </a>
													</c:when>
													<c:otherwise>
														<a  href="javaScript:;">即将开始</a>
													</c:otherwise>
												</c:choose>
				        					</div>
				        				</div>
				        			</li>
		        				</c:if>
		        			</c:forEach>
		        		</ul>
		        	</div>
		        	<div class="first-active-bot">
		        		<img src="${ctx}/img/active/billion/first-active-bot.png" alt="" />
		        	</div>
		        </div>
		    	<div class="active-section-1" <c:if test='${activityFlag==2}'>style="display: block;"</c:if>>
		    		<div class="second-active-top"></div>
		    		<div class="second-active-ban">
		    		<input type="hidden" value="" id="countdown"/>
		    			<div class="second-active-going">
		    			<input type="hidden" name="time100" id="time100" value="${billionSecondTimeBean.time100}"/>
		    			<input type="hidden" name="time101" id="time101" value="${billionSecondTimeBean.time101}"/>
		    			<input type="hidden" name="time102" id="time102" value="${billionSecondTimeBean.time102}"/>
		    			<input type="hidden" name="time103" id="time103" value="${billionSecondTimeBean.time103}"/>
		    			<input type="hidden" name="time104" id="time104" value="${billionSecondTimeBean.time104}"/>
		    			<input type="hidden" name="time105" id="time105" value="${billionSecondTimeBean.time105}"/>
		    			<input type="hidden" name="stage" id="stage" value="${billionSecondTimeBean.stage}"/>
		    			<input type="hidden" name="minStage" id="minStage" value="${billionSecondTimeBean.minStage}"/>
		    			<input type="hidden" name="nowTime" id="nowTime" value="${billionSecondTimeBean.nowTime}"/>
		    			<input type="hidden" name="isEnd" id="isEnd" value="${billionSecondTimeBean.isEnd}"/>
		    			
		    				<div class="second-active-list">
		    					
		    					<div class="<c:if test="${billionSecondTimeBean.stage == 0}"> unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage >= 1}"> end-section</c:if> reward-countining" id="finish_1">
			    					<img />
			    					<p><span>100亿</span>累计投资</p>
			    				</div>
			    				<div class="<c:if test="${billionSecondTimeBean.stage == 1}"> get-reward-sign</c:if> reward-boundary" data-num="0" id = "stage_dom1">
                                     <img />
                                     <div></div>
			    				</div>
			    				<div class="<c:if test="${billionSecondTimeBean.stage == 0}">unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage == 1}"> now-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage > 1}"> end-section</c:if> reward-countining" id="finish_2">
			    					<img />
			    					<p><span>101亿</span>累计投资</p>
			    				</div>
			    				<div class="<c:if test="${billionSecondTimeBean.stage == 2}"> get-reward-sign</c:if> reward-boundary" data-num="1" id = "stage_dom2">
			    					 <img />
			    					 <div></div>
			    				</div>
			    				<div class="<c:if test="${billionSecondTimeBean.stage < 2}">unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage == 2}"> now-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage > 2}"> end-section</c:if> reward-countining" id="finish_3">
			    					<img />
			    					<p><span>102亿</span>累计投资</p>
			    				</div>
		    				</div>
		    				<div class="second-active-list">
		    					<div class="<c:if test="${billionSecondTimeBean.stage < 4}">unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage == 4}"> now-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage > 4}"> end-section</c:if> reward-countining" id="finish_5">
			    					<img />
			    					<p><span>104亿</span>累计投资</p>
			    				</div> 
			    				<div class="<c:if test="${billionSecondTimeBean.stage == 4}"> get-reward-sign</c:if> reward-boundary" data-num="3" id = "stage_dom4">
			    				     <img />
			    				     <div></div>
			    				</div>
			    				<div class="<c:if test="${billionSecondTimeBean.stage < 3}">unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage == 3}"> now-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage > 3}"> end-section</c:if> reward-countining" id="finish_4">
			    					<img />
			    					<p><span>103亿</span>累计投资</p>
			    				</div>
		    				</div>
		    				<div class="second-active-list ">
		    					<div class="<c:if test="${billionSecondTimeBean.stage < 5}">unstart-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage == 5}">now-section</c:if>
		    								<c:if test="${billionSecondTimeBean.stage > 5}">end-section</c:if> reward-countining" id="finish_6">
		    					    <img />
			    					<p><span>105亿</span>累计投资</p>
		    				    </div>
		    				</div>
		    			    <div class="<c:if test="${billionSecondTimeBean.stage == 5}">get-reward-sign</c:if> reward-line-left reward-boundary" data-num="4" id = "stage_dom5">
                                 <img />
                                 <div></div>
 			    			</div>
			    			<div class="<c:if test="${billionSecondTimeBean.stage == 3}">get-reward-sign</c:if> reward-line-right reward-boundary" data-num="2" id = "stage_dom3">
                                 <img />
                                 <div></div>
			    			</div>
		    			</div>
		    		    <div class="second-active-setting">
		    		    	<div class="reward-set-tab">
		    		    		<ol>
		    		    			<li></li>
		    		    			<li  class="setActive"></li>
		    		    		</ol>
		    		    	</div>
		    		    	<div class="reward-set-list">
		    		    		<ul>
		    		    			<li>
		    		    				<h3>奖励设置</h3>
		    		    				<table border="0" cellspacing="0" cellpadding="0" class="reward-set-static">
		    		    					<tr>
		    		    						<th class="table-one">投资金额范围</th>
		    		    						<th class="table-two">助力奖</th>
		    		    						<th class="table-three">使用说明</th>
		    		    					</tr>
		    		    					<tr>
		    		    						<td>100万及以上</td>
		    		    						<td class="golden-font">1.2%加息券</td>
		    		    						<td>金额范围：0-100000元</td>
		    		    					</tr>
		    		    					<tr>
		    		    						<td>30万-100万（不含）</td>
		    		    						<td class="golden-font">300元代金券</td>
		    		    						<td>金额范围：单笔投资达100000元可用</td>
		    		    					</tr>
		    		    					<tr>
		    		    						<td>5万-30万（不含）</td>
		    		    						<td class="golden-font">100元代金券</td>
		    		    						<td>金额范围：单笔投资达50000元可用</td>
		    		    					</tr>
		    		    					<tr>
		    		    						<td>5万（不含）以下</td>
		    		    						<td class="golden-font">30元代金券</td>
		    		    						<td>金额范围：单笔投资达20000元可用</td>
		    		    					</tr>
		    		    				</table>
		    		    			</li>
		    		    			<li>
		    		    				<h3>奖品发放</h3>
		    		    				<div class="reward-name-list">
		    		    					<div class="reward-name-title">
		    		    						<p class="reward-one">用户名</p>
		    		    						<p class="reward-two">投资金额</p>
		    		    						<p class="reward-three">获得奖励</p>
		    		    					</div>
		    		    					<table border="0" cellspacing="0" cellpadding="0" class="reward-get-announce">
		    		    					
			    		    					<c:forEach items="${billionSecondRecords }" var="record" begin="0" step="1" varStatus="status">
					    		    				<c:choose>
														<c:when test="${sessionUserId == record.userId }">
															<tr class="my-reward-name">
						    		    						<td><c:out value="${record.userName }"></c:out></td>
						    		    						<td><fmt:formatNumber value="${record.tenderMoney }" pattern="#,##0.00#" />元</td>
						    		    						<td>
						    		    							<c:forEach items="${billionPrizeTypes }" var="prizeType" begin="0" step="1">
						    		    								<c:if test="${prizeType.nameCd eq record.prizeId}"><c:out value="${prizeType.name }"></c:out></c:if>
						    		    							</c:forEach>
						    		    						</td>
						    		    					</tr>
														</c:when>
														<c:otherwise>
															<tr>
						    		    						<td class="prizeUserName"><c:out value="${record.userName }"></c:out></td>
						    		    						<td> <fmt:formatNumber value="${record.tenderMoney }" pattern="#,##0.00#" />元</td>
						    		    						<td>
						    		    							<c:forEach items="${billionPrizeTypes }" var="prizeType" begin="0" step="1">
						    		    								<c:if test="${prizeType.nameCd eq record.prizeId}"><c:out value="${prizeType.name }"></c:out></c:if>
						    		    							</c:forEach>
						    		    						</td>
						    		    					</tr>
														</c:otherwise>
													</c:choose>
			    		    					</c:forEach>
		    		    					</table>
		    		    				</div>
		    		    			</li>
		    		    		</ul>
		    		    	</div>
		    		    </div>
		    		</div>
		    	    <div class="second-active-bot">
		    	    	<img src="${ctx}/img/active/billion/second-active-bot.png" alt="" />
		    	    </div>
		    	</div>
		    	<div class="active-section-2" <c:if test='${activityFlag==3 && billionThirdStarFlag==1}'>style="display: block;"</c:if>>
		    		<div class="third-active-top"></div>
		    		<input type = "hidden" id="nextKillId" value = "<c:out value='${nextKillId}'></c:out>"/>
		    		<input type = "hidden" id="nextKillTime" value = "<c:out value='${nextKillTime}'></c:out>"/>
		    		<input type = "hidden" id="currentTime" value = "<c:out value='${currentTime}'></c:out>"/>
		    		<input type = "hidden" id="remainingNum" value = "<c:out value='${remainingNum}'></c:out>"/>
		    		<input type = "hidden" id="billionThirdStarFlag" value = "<c:out value='${billionThirdStarFlag}'></c:out>"/>
		    		<div class="third-active-ban">
		    			<div  class="third-active-cont">
		    				<ul id="ticket-list">
		    				
		    				<c:forEach items="${billionThirdList }" var="config" begin="0" step="1" varStatus="status">
		    				<li>
		    					<input type = "hidden" value = "<c:out value='${config.id }'></c:out>" class = "kill"/>
		    						<div class="active-ticket-top">
		    							<p>
		    								<span class="fontWeight"><c:out value='${config.couponQuota }'></c:out></span>
		    								<c:if test="${config.couponType==2 }">
		    								<span><i>%</i>加息券</span>
		    								</c:if>
		    								<c:if test="${config.couponType==3 }">
		    								<span><i>yuan</i>代金券</span>
		    								</c:if>
		    							</p>
		    							<b><c:out value='${config.tenderQuota }'></c:out></b>
		    						</div>
		    						
		    						<c:if test="${billionThirdStarFlag==0 }">
			    						<a class="colorgray" id="style-${config.id }">敬请期待！</a>
		    						</c:if>
		    						<c:if test="${billionThirdStarFlag==2 }">
			    						<a class="colorgray" id="style-${config.id }">活动结束！</a>
		    						</c:if>
		    						<c:if test="${billionThirdStarFlag==1 }">
			    						<c:if test="${config.status==1 }">
				    						<a class="colorgray" id="style-${config.id }">敬请期待！</a>
			    						</c:if>
			    						
			    						<c:if test="${config.status==0 }">
				    						<c:if test="${config.killStatus==1 }">
				    							<c:if test="${config.remainingNum==0 }">
				    							<a class="colorgray" id="style-${config.id }">已抢光</a>
				    							</c:if>
				    							<c:if test="${config.remainingNum>0 }">
				    							<a class="colorpurple clickEvent" id="style-${config.id }">
				    								<img src='${ctx}/img/active/billion/click-sign.png'/>
				    								点我抢券
				    								<p>剩余<span><c:out value='${config.remainingNum }'></c:out></span>张</p>
				    							</a>
				    							</c:if>
				    						</c:if>
				    						
				    						<c:if test="${config.killStatus==0 }">
				    							<a  class="colordarkblue" id="style-${config.id }">
				    							<c:out value='${config.secKillTime }'></c:out>开抢</a>
				    						
				    						</c:if>
				    						<c:if test="${config.killStatus==2 }">
				    							<a class="colordarkblue"  id="style-${config.id }">
				    							<c:out value='${config.secKillTime }'></c:out>开抢</a>
				    						
				    						</c:if>	
			    						</c:if>
		    						</c:if>
		    					</li>
		    				</c:forEach>
		    					<%-- 
		    					<li>
		    					<input type = "hidden" value = "" class = "kill"/>
		    						<div class="active-ticket-top">
		    							<p>
		    								<span class="fontWeight">30</span>
		    								<span>
		    									<i>yuan</i>
		    									代金券
		    								</span>
		    							</p>
		    							<b>(单笔投资达1万元可用)</b>
		    						</div>
		    						<a href="#" class="colorpurple">
		    							<img src='${ctx}/img/active/billion/click-sign.png'/>
		    							点我抢券
		    							<p>剩余<span>10</span>张</p>
		    						</a>
		    					</li>
		    					<li>
		    					<input type = "hidden" value = "" class = "kill"/>
		    						<div class="active-ticket-top">
		    							<p>
		    								<span class="fontWeight">20</span>
		    								<span>
		    									<i>yuan</i>
		    									代金券
		    								</span>
		    							</p>
		    							<b>(单笔投资达1万元可用)</b>
		    						</div>
		    						<a href="#" id="style-3" class="colordarkblue">18：00开抢</a>
		    					</li>
		    					<li>
		    					<input type = "hidden" value = "" class = "kill"/>
		    						<div class="active-ticket-top">
		    							<p>
		    								<span class="fontWeight">10</span>
		    								<span>
		    									<i>%</i>
		    									加息券
		    								</span>
		    							</p>
		    							<b>(限额1万)</b>
		    						</div>
		    						<a href="#" class="colordarkblue">18：00开抢</a>
		    					</li> --%>
		    			    </ul>
		    			</div>
		    		</div>
		    	    <div class="third-active-bot">
		    	    	<img src="${ctx}/img/active/billion/third-active-bot.png" alt="" />
		    	    </div>
		    	    <div class="third-active-popup" style="display:none">
		    	    	<div class="third-popup-bg"></div>
		    	    	<div class="third-popup-cont">
		    	    		<div class="ticket-popup"></div>
		    	    	</div>
		    	    </div>
		    	</div>
		    	<div class="active-section-3">
			    	<div class="fourth-active-top"></div>
			    		<div  class="fourth-active-ban">
			    			<p>汇盈金服全国免费热线升级为</p>
				    		<p class="phone-color">400-900-7878</p>
				    		<p>每天<span>9：00-18：00</span>倾情为您服务，欢迎来电咨询</p>
			    		</div>
                </div>
		    	<div class="active-section-4">
		    	<div class="fifth-active-top"></div>
		    			<div class="fifth-active-ban">
		    				<img src="${ctx}/img/active/billion/fifth-active-img01.jpg" alt="" />
		    				<img src="${ctx}/img/active/billion/fifth-active-img02.jpg" alt="" />
		    				<img src="${ctx}/img/active/billion/fifth-active-img03.jpg" alt="" />
		    				<img src="${ctx}/img/active/billion/fifth-active-img04.jpg" alt="" />
		    				<img src="${ctx}/img/active/billion/fifth-active-img05.jpg" alt="" />
		    				<img src="${ctx}/img/active/billion/fifth-active-img06.jpg" alt="" />
		    			</div>
	    	</div>
	    	</div>
	    </div>
	    <jsp:include page="/footer.jsp"></jsp:include>
	    <script src="${ctx}/js/activity/billionActive.js?version=${version}"></script>
	    <script>
$('.ticket-popup').delegate("a.i-know","click",function(){
	$('.third-active-popup').hide();
})
/* $('.i-know').click(function(){
	$('.third-active-popup').hide();
}) */

//-------------------------------------------------------------------------------------------------
$(function(){
	$(".clickEvent").click(function(){
		$this = $(this);
		$li=$this.parent();
		$kill_val = $li.find(".kill").val();
/* 		_data = {"killId":$kill_val};//赋值  */
		secKillResult($kill_val);
	})
	
})
var successFuction,errorFunction;//回调函数
function successFuction(data){
		
		var billionThirdList=data.billionThirdList;
		
		
		$('#ticket-list').empty();
		
		var ticketlist='';
		for (var i=0;i<billionThirdList.length;i++){
			var billionThird=billionThirdList[i];
			ticketlist =ticketlist+ '<li>'+
			'<input type = "hidden" value = "'+billionThird.id+'" class = "kill"/>'+
			'<div class="active-ticket-top">'+
			'<p>'+
			'<span class="fontWeight">'+billionThird.couponQuota+'</span>'+
			'<span>';
			
			if(billionThird.couponType==2){
				ticketlist =ticketlist+ '<i>%</i>'+
				'加息券'+
				'</span>';
			}else if(billionThird.couponType==3){
				ticketlist =ticketlist+ '<i>yuan</i>'+
				'代金券'+
				'</span>';
			}
			
			ticketlist =ticketlist+'</p>'+
			'<b>'+billionThird.tenderQuota+'</b>'+
			'</div>';
			
			if(billionThird.status==0){
				if(billionThird.killStatus==1){
					if(billionThird.remainingNum==0){
						ticketlist =ticketlist+'<a class="colorgray" id="style-'+billionThird.id+'">已抢光</a></li>';
					}
					if(billionThird.remainingNum>0){
						ticketlist =ticketlist+'<a class="colorpurple" onclick = "secKillResult('+billionThird.id+')" id="style-'+billionThird.id+'">'+
						'<img src="${ctx}/img/active/billion/click-sign.png"/>点我抢券'+
						'<p>剩余<span>'+billionThird.remainingNum+'</span>张</p></a></li>';
					}
				}
				if(billionThird.killStatus==0||billionThird.killStatus==2){
					ticketlist =ticketlist+'<a  class="colordarkblue" id="style-'+billionThird.id+'">'+billionThird.secKillTime+'开抢</a></li>';
				}
			}else if(billionThird.status==1){
				ticketlist =ticketlist+'<a class="colorgray" id="style-'+billionThird.id+'">敬请期待！</a></li>';
			}else{
				ticketlist =ticketlist+'<a class="colorgray" id="style-'+billionThird.id+'">活动结束！</a></li>';
			}
			
			
		}

		
		
		$('#ticket-list').append(ticketlist)
		
		$("#nextKillTime").val(data.nextKillTime);
		$("#currentTime").val(data.currentTime);
		$("#nextKillId").val(data.nextKillId);
		$("#remainingNum").val(data.remainingNum);
		
		var timer = setInterval(CountDown,1000);
		var endTime = $("#nextKillTime").val();
		var nowTime = $("#currentTime").val();
		var t = endTime-nowTime;
		var nextKillId = $("#nextKillId").val();
            }
	
	function errorFunction(){
            }

	//秒杀结果
	function secKillResult(killid){

		var _data = null;//传递到后台数据
		var _action = webPath + "/activity/billion/userPartakeBillionThirdActivity.do";//接口的地址
		/* $this = $(this);
		$li=$this.parent();
		$kill_val = $li.find(".kill").val();
		_data = {"killId":$kill_val};//赋值 */
		_data=killid;
		mothodAjax(_action,_data,successFuction,errorFunction);
	}
	
	function mothodAjax(_action,_data,successFuction,errorFunction){
		_data = {"killId":_data};//赋值 
		$.ajax({
            type: "get",
            url: _action,
            data: _data ,
            dataType: "json",
            async:true,
            success: function(data){
            	if(data.error == 1){
            		
            		
            		var popupmsg = '<h3>'+
            		'恭喜您'+
            		/* '很抱歉'+ */ 
            		'</h3>'+
            		'<p>'+
            		data.msg+
            		/* '恭喜您成功获得十元代金券'+ */
            		'</p>'+
            		'<a href="'+webPath + data.host+'" class="go-invest">'+
            		/* '去投资'+ */
            		'立即使用'+ 
            		'</a>'+
            		'<a class="i-know">我知道了</a>'+
					'<div class="clearboth"></div>';
					
					$('.third-active-popup').show();
					$('.ticket-popup').empty();
            		$('.ticket-popup').append(popupmsg);
            		
            		
            	}else if(data.error == 0){
            		if(data.status==1){
            			// 未登录跳转到登录页面
						window.location.href = webPath + data.host;
            			return;
            		}
            		var popupmsg = '<h3>'+
            		/* '恭喜您'+ */
            		'很抱歉'+ 
            		'</h3>'+
            		'<p>'+
            		data.msg+
            		/* '恭喜您成功获得十元代金券'+ */
            		'</p>'+
            		'<a href="'+webPath + data.host+'" class="go-invest">'+
            		'去投资'+
            		/* '立即使用'+ */
            		'</a>'+
            		'<a class="i-know">我知道了</a>'+
					'<div class="clearboth"></div>';
					
					$('.third-active-popup').show();
					$('.ticket-popup').empty();
            		$('.ticket-popup').append(popupmsg);
            		
            	}
            		successFuction(data);
            		
            },
            error:function(XMLHttpRequest, textStatus, errorThrown) {
            	errorFunction();
            }
                });
	}
	
</script>
	</body>
</html>


<!-- <script>
$(function(){
	var _data = null;//传递到后台数据
	var  _action = null;//接口的地址
	var successFuction,errorFunction;//回调函数
	var $countVal = $('#countdown').val();
	_data = {"position":$countVal,"endtime":$countVal,"nowtime":$countVal};
	function activeAjax(_action,_data,successFuction,errorFunction){
		$.ajax({
	            type: "get",
	            url: _action,
	            data: _data ,
	            dataType: "json",
	            async:false,
	            success: function(data){
	            	
	            },
	            error:function(XMLHttpRequest, textStatus, errorThrown) {
	            	errorFunction();
	            }
	    });
    }

activeAjax(_action,_data,successFuction,errorFunction)
});
</script> -->