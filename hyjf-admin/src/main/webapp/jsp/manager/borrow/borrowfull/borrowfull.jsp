<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="borrowfull:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款复审" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款复审</h1>
			<span class="mainDescription">借款复审复审复审复审复审的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">				
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="borrowfull:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowFullForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowFullForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
						<br/>
						<%-- 列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width:55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">项目编号</th>
									<%-- UPD BY LIUSHOUYI 合规检查 START --%><%--
									<th class="center">项目名称</th>
									<th class="center">借款人</th>
									--%><th class="center">借款人用户名</th>
									<%--UPD BY LIUSHOUYI 合规检查 END --%>
									<th class="center">借款金额</th>
									<th class="center">借款期限</th>
									<th class="center">借到金额</th>
									<%-- upd by LSY START --%>
									<%-- <th class="center">服务费</th> --%>
									<%--<th class="center">放款服务费</th>--%>
									<%-- upd by LSY END --%>
									<th class="center">复审状态</th>
									<th class="center">满标/到期时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty recordList}">
										<tr><td colspan="11">暂时没有数据记录</td></tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<c:set var="colorCss" value=""></c:set>
												<c:if test="${record.accountFlag eq 1 }">
													<c:set var="colorCss" value='style="color:red;"'></c:set>
												</c:if>
												<td class="center"><span ${colorCss }>${(borrowFullForm.paginatorPage -1 ) * borrowFullForm.paginator.limit + status.index + 1 }</span></td>
												<td class="center"><span ${colorCss }><c:out value="${record.borrowNid }"/></span></td>
												<%-- DEL BY LIUSHOUYI 合规检查 START
												<td><span ${colorCss }><c:out value="${record.borrowName }"/></span></td>
												DEL BY LIUSHOUYI 合规检查 END --%>
												<td><span ${colorCss }><c:out value="${record.username }"/></span></td>
												<td align="right"><span ${colorCss }><fmt:formatNumber value="${record.account}" type="number" pattern="#,##0.00#" /></span></td>
												<td align="right"><span ${colorCss }><c:out value="${record.borrowPeriod }"/></span></td>
												<td align="right"><span ${colorCss }><fmt:formatNumber value="${record.borrowAccountYes}" type="number" pattern="#,##0.00#" /></span></td>
												<%--<td align="right"><span ${colorCss }><c:out value="${record.serviceScale }"/></span></td>--%>
												<td class="center"><span ${colorCss }><c:out value="${ record.reverifyStatusName }" /></span></td>
												<td class="center"><span ${colorCss }><c:out value="${record.overTime }"/></span></td>
												<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowfull:BORROW_FULL">
																<c:if test="${record.reverifyStatus eq 0 }">
																	<c:if test="${record.accountFlag eq 0}">
																	
																		<%--计划复审手动操作 del by liushouyi <c:if test="${record.isEngineUsed eq 0}"> --%>
																			<a class="btn btn-transparent btn-xs tooltips fn-Full" data-borrownid="${record.borrowNid }"
																				data-toggle="tooltip" tooltip-placement="top" data-original-title="复审"><i class="fa fa-legal"></i></a>
																		<%--计划复审手动操作 del by liushouyi </c:if>	 --%>		
																			
																	</c:if>
																	<c:if test="${record.accountFlag eq 1 }">
																		<span ${colorCss }>爆标</span>
																	</c:if>
																</c:if>
															</shiro:hasPermission>
																<c:if test="${record.reverifyStatus ne 0}">
																	<a class="btn btn-transparent btn-xs tooltips fn-RepeatInfo" data-borrownid="${record.borrowNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="放款明细"><i class="fa fa-list-ul fa-white"></i></a>
																</c:if>
															<shiro:hasPermission name="borrowfull:BORROW_OVER">
																<a class="btn btn-transparent btn-xs tooltips fn-Over" data-borrownid="${record.borrowNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="流标"><i class="fa fa-remove"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<shiro:hasPermission name="borrowfull:BORROW_FULL">
																			<c:if test="${record.reverifyStatus eq 0 }">
																		    	<c:if test="${empty record.status }">
																		    		<%--计划复审手动操作 del by liushouyi <c:if test="${record.isEngineUsed eq 0}"> --%>
																						<a class="fn-Full" data-borrownid="${record.borrowNid }">复审</a>
																					<%-- </c:if> --%>
																				</c:if>
																				<c:if test="${record.accountFlag eq 1 }">
																					<span ${colorCss }>爆标</span>
																				</c:if>
																			</c:if>
																		</shiro:hasPermission>
																		<c:if test="${record.reverifyStatus ne 6  }">
																			<a class="btn btn-transparent btn-xs tooltips fn-RepeatInfo" data-borrownid="${record.borrowNid }"
																				data-toggle="tooltip" tooltip-placement="top" data-original-title="放款明细">放款明细</a>
																		</c:if>
																		<shiro:hasPermission name="borrowfull:BORROW_OVER">
																			<a class="btn btn-transparent btn-xs tooltips fn-Over" data-borrownid="${record.borrowNid }"
																				data-toggle="tooltip" tooltip-placement="top" data-original-title="流标">流标</a>
																		</shiro:hasPermission>
																	</li>
																</ul>
															</div>
														</div>
												</td>
											</tr>
										</c:forEach>					
									</c:otherwise>
								</c:choose>
							<%-- add by LSY START --%>
							<tr>
								<th class="center">总计</th>
								<th>&nbsp;</th>
								<%-- DEL BY LIUSHOUYI 合规检查  START
								<th>&nbsp;</th>
								DEL BY LIUSHOUYI 合规检查 END --%>
								<th>&nbsp;</th>
								<td align="right"><fmt:formatNumber value="${sumAccount.sumAccount }" pattern="#,##0.00#"/></td>
								<th>&nbsp;</th>
								<td align="right"><fmt:formatNumber value="${sumAccount.sumBorrowAccountYes }" pattern="#,##0.00#"/></td>
								<%--<td align="right"></td>--%>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
							</tr>
							<%-- add by LSY END --%>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<shiro:hasPermission name="borrowfull:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${borrowFullForm.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowfull:SEARCH">
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowFullForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowFullForm.borrowNidSrch}" />
				</div>
				<%-- DEL BY LIUSHOUYI 合规检查 START
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${borrowFullForm.borrowNameSrch}" />
				</div>
				DEL BY LIUSHOUYI 合规检查 START --%>
				<div class="form-group">
					<%-- UPD BY LIUSHOUYI 合规检查 START --%><%--
					<label>借款人</label>
					--%><label>借款人用户名</label>
					<%-- UPD BY LIUSHOUYI 合规检查 END --%>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowFullForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>满标时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowFullForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowFullForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
			
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowfull/borrowfull.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>	

