<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="borrowexceptiondel:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款异常列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款异常列表</h1>
			<span class="mainDescription">借款异常列表的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="borrowexceptiondel:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowexceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowexceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="borrowexceptiondel:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
									</shiro:hasPermission>
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
										<th class="center">借款进度</th>
										<th class="center">借款期限</th>
										<th class="center">出借利率</th>
										<th class="center">还款方式</th>
										<th class="center">项目状态</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty borrowexceptionForm.sort and borrowexceptionForm.col eq 'b.recover_last_time' }">
													<a href="#" class="fn-Sort" data-col="b.recover_last_time" data-sort="<c:if test="${ borrowexceptionForm.sort eq 'asc' }">desc</c:if><c:if test="${ borrowexceptionForm.sort eq 'desc' }">asc</c:if>">放款时间</a>&nbsp;<i class="fa fa-sort-${borrowexceptionForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="b.recover_last_time" data-sort="desc">放款时间</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="13">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowexceptionForm.paginatorPage - 1 ) * borrowexceptionForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td align="right"><c:out value="${record.borrowAccountScale }"></c:out></td>
													<td align="right"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td align="right"><c:out value="${record.borrowApr }"></c:out></td>
													<td><c:out value="${record.borrowStyleName }"></c:out></td>
													<td class="center"><c:out value="${record.status }"></c:out></td>
													<td class="center"><c:out value="${record.recoverLastTime }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center">
														<c:if test="${record.delFlag eq '1' }">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="borrowexceptiondel:DELETE">
																	<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.borrowNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="borrowexceptiondel:DELETE">
																			<li><a class="fn-Delete" data-id="${record.borrowNid }">删除</a></li>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</div>
														</c:if>
														<c:if test="${record.revokeFlag eq '1' }">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowexceptiondel:REVOKE">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.borrowNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="标的撤销"></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="borrowexceptiondel:REVOKE">
																<a class="fn-UpdateBy" data-id="${record.borrowNid }" >标的撤销</a>
															</shiro:hasPermission>
														</div>
														</c:if>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowexceptiondel:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowexceptionForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowexceptiondel:SEARCH">
				<input type="hidden" name=moveFlag id="moveFlag" value="BORROW_LIST"/>
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="projectType" id="projectType" />
				<input type="hidden" name="col" id="col" value="${borrowexceptionForm.col}" />
				<input type="hidden" name="sort" id="sort" value="${borrowexceptionForm.sort}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowexceptionForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowexceptionForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${borrowexceptionForm.borrowNameSrch}" />
				</div>
				<div class="form-group">
					<label>借款人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowexceptionForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>项目状态</label>
					<select name="statusSrch" id="statusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0">初审中</option>
						<option value="10">待发布</option>
					</select>
				</div>
				<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowCd }"
								<c:if test="${borrowProjectType.borrowCd eq borrowexceptionForm.projectTypeSrch}">selected="selected"</c:if>>
								<c:out value="${borrowProjectType.borrowName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
							<option value="${borrowStyle.nid }" 
								<c:if test="${borrowStyle.nid eq borrowexceptionForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowexceptionForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowexceptionForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>		

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/borrowexception/borrowexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
