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
<shiro:hasPermission name="recoverexception:VIEW">
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
							<shiro:hasPermission name="recoverexception:SEARCH">
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
							<colgroup>
								<col style="width:55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">项目编号</th>
									<th class="center">项目名称</th>
									<th class="center">借款人</th>
									<th class="center">借款金额</th>
									<th class="center">借款期限</th>
									<th class="center">借到金额</th>
									<th class="center">服务费</th>
									<th class="center">状态</th>
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
												<td class="center">${(form.paginatorPage -1 ) * form.paginator.limit + status.index + 1 }</td>
												<td class="center"><c:out value="${record.borrowNid }"/></td>
												<td><c:out value="${record.borrowName }"/></td>
												<td><c:out value="${record.username }"/></td>
												<td align="right"><c:out value="${record.account }"/></td>
												<td align="right"><c:out value="${record.borrowPeriod }"/></td>
												<td align="right"><c:out value="${record.borrowAccountYes }"/></td>
												<td align="right"><c:out value="${record.serviceScale }"/></td>
												<td class="center">
													<c:if test="${ record.status eq '0' }" >放款中</c:if>
													<c:if test="${ record.status eq '2' }" >处理中</c:if>
													<c:if test="${ record.status eq '9' }" >
													<span class="text-red">放款失败
													<shiro:hasPermission name="recoverexception:INFO">
													&nbsp;<i class="fa fa-red fa-info-circle error-info" data-trigger="click" data-toggle="popover" data-html="true" data-placement="bottom" data-title="详细信息"  data-content="<c:out value="${record.data == null ? '放款失败' : record.data }"></c:out>"></i>
													</shiro:hasPermission>
													</span>
													</c:if>
												</td>
												<td class="center"><c:out value="${record.overTime }"/></td>
												<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="recoverexception:RECOVER_EXCEPTION">
															<c:if test="${ record.status eq '9' }" >
																<a class="btn btn-transparent btn-xs tooltips fn-Repeat" data-borrownid="${record.borrowNid }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="重新放款"><i class="fa fa-gavel"></i></a>
															</c:if>
															</shiro:hasPermission>
															<a class="btn btn-transparent btn-xs tooltips fn-RepeatInfo" data-borrownid="${record.borrowNid }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="放款明细"><i class="fa fa-list-ul fa-white"></i></a>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<shiro:hasPermission name="recoverexception:RECOVER_EXCEPTION">
																		<c:if test="${ record.status eq '9' }" >
																			<a class="btn btn-transparent btn-xs tooltips fn-Repeat" data-borrownid="${record.borrowNid }"
																				data-toggle="tooltip" tooltip-placement="top" data-original-title="重新放款">重新放款</a>
																		</c:if>
																		</shiro:hasPermission>
																		<a class="btn btn-transparent btn-xs tooltips fn-RepeatInfo" data-borrownid="${record.borrowNid }"
																			data-toggle="tooltip" tooltip-placement="top" data-original-title="放款明细">放款明细</a>
																	</li>
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
						<shiro:hasPermission name="recoverexception:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${form.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="recoverexception:SEARCH">
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${form.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${form.borrowNameSrch}" />
				</div>
				<div class="form-group">
					<label>借款人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${form.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>满标时间</label>
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

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<style>
			.popover {
				max-width: 600px;
			}
			.error-info {
				cursor:pointer;
			}
			</style>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/recoverexception/recoverexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>

