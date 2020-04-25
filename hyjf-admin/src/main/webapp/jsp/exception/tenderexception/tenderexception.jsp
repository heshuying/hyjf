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

<shiro:hasPermission name="tenderexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借爆标处理" />
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="tenderexception:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<!-- <th class="center">用户属性</th> -->
										<th class="center">推荐人</th>	
										<th class="center">项目编号</th>
										<th class="center">出借利率</th>
										<th class="center">借款期限</th>
										<th class="center">还款方式</th>
										<th class="center">出借金额</th>
										<th class="center">出借单号</th>
										<th class="center">操作平台</th>
										<th class="center">投标方式</th>
										<th class="center">出借时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(form.paginatorPage - 1 ) * form.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.username }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.attribute }"></c:out></td> --%>
													<td class="center"><c:out value="${record.referrerName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
													<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td align="right"><c:out value="${record.nid }"></c:out></td>
													<td align="right"><c:out value="${record.client }"></c:out></td>
													<td align="right"><c:out value="${record.investType }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center">
														<shiro:hasPermission name="tenderexception:TENDER_BACK">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs fn-TenderBack"  data-nid="${record.nid }"  data-toggle="tooltip" data-placement="top" data-original-title="投标撤销"><i class="fa fa-gavel"></i></a>
														</div>
														</shiro:hasPermission>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="tenderexception:TENDER_BACK">
																	<li>
																		<a class="btn btn-transparent btn-xs fn-TenderBack"  data-nid="${record.nid }"  data-toggle="tooltip" data-placement="top" data-original-title="投标撤销">投标撤销</a>
																	</li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>				
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="tenderexception:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${form.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="tenderexception:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<input type="hidden" name="nid" id="nid" value="" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>出借单号</label>
					<input type="text" name="nidSrch" id="nidSrch" class="form-control input-sm underline" value="${form.nidSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${form.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>出借人</label>
					<input type="text" name="tenderUserNameSrch" id="tenderUserNameSrch" class="form-control input-sm underline" value="${form.tenderUserNameSrch}" />
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${form.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${form.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/tenderexception/tenderexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
