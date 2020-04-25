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

<shiro:hasPermission name="certerrorlog:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="应急中心错误日志" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">记录</h1>
			<span class="mainDescription">本功能可以修改查询相应的记录。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="certerrorlog:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
								</colgroup>
								<thead>
								    <tr>
										<th class="center">请求类型</th>
										<th class="center">上传时间</th>
										<th class="center">上报结果</th>
										<th class="center">批次号</th>
										<th class="center">失败次数</th>
										<th class="center">操作</th>
								</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty form.recordList}">
											<tr><td colspan="6">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
 											<c:forEach items="${form.recordList}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">
														<c:if test="${record.infType==1}">
															<c:out value="用户数据"></c:out>
														</c:if>
														<c:if test="${record.infType==2}">
															<c:out value="散标"></c:out>
														</c:if>
														<c:if test="${record.infType==6}">
															<c:out value="散标状态"></c:out>
														</c:if>
														<c:if test="${record.infType==81}">
															<c:out value="还款计划"></c:out>
														</c:if>
														<c:if test="${record.infType==82}">
															<c:out value="债权信息"></c:out>
														</c:if>
														<c:if test="${record.infType==83}">
															<c:out value="转让项目"></c:out>
														</c:if>
														<c:if test="${record.infType==84}">
															<c:out value="转让状态"></c:out>
														</c:if>
														<c:if test="${record.infType==85}">
															<c:out value="承接信息"></c:out>
														</c:if>
														<c:if test="${record.infType==4}">
															<c:out value="交易流水"></c:out>
														</c:if>
													</td>
													<td class="center"><hyjf:datetimeformat value="${record.sendTime}"></hyjf:datetimeformat></td>
													<td class="center">
														<c:if test="${record.sendStatus==0}">
															<c:out value="初始"></c:out>
														</c:if>
														<c:if test="${record.sendStatus==1}">
															<c:out value="成功"></c:out>
														</c:if>
														<c:if test="${record.sendStatus==9}">
															<c:out value="失败"></c:out>
														</c:if>
														<c:if test="${record.sendStatus==99}">
															<c:out value="无响应"></c:out>
														</c:if>
													</td>
													<td class="center"><c:out value="${record.logOrdId}"></c:out></td>
													<td class="center"><c:out value="${record.sendCount}"></c:out></td>
													<td class="center">
														<shiro:hasPermission name="certerrorlog:VIEW">
														<a class="btn btn-o btn-primary btn-sm btn-xs btn-cksb" data-txcounts='${record.resultMsg }'
														   data-toggle="tooltip" data-placement="bottom" data-original-title="查看失败原因"> 查看失败原因 </a>
														</shiro:hasPermission>
														<shiro:hasPermission name="certerrorlog:MODIFY">
														<a class="btn btn-o btn-primary btn-sm btn-xs btn-cxpp" data-txcounts='${record.id }'
														   data-toggle="tooltip" data-placement="bottom" data-original-title="错误次数置为3"> 重新跑批 </a>
														</shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="certerrorlog:SEARCH">
 								<hyjf:paginator id="equiPaginator" hidden="paginator-page" paginator="${form.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value="${form.id}"  />
 			<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage == null ? 0 : form.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>请求类型</label>
				<select name="infType" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1" <c:if test="${'1' eq form.infType}">selected="selected"</c:if>>用户数据</option>
					<option value="2" <c:if test="${'2' eq form.infType}">selected="selected"</c:if>>散标</option>
					<option value="6" <c:if test="${'6' eq form.infType}">selected="selected"</c:if>>散标状态</option>
					<option value="81" <c:if test="${'81' eq form.infType}">selected="selected"</c:if>>还款计划</option>
					<option value="82" <c:if test="${'82' eq form.infType}">selected="selected"</c:if>>债权信息</option>
					<option value="83" <c:if test="${'83' eq form.infType}">selected="selected"</c:if>>转让项目</option>
					<option value="84" <c:if test="${'84' eq form.infType}">selected="selected"</c:if>>转让状态</option>
					<option value="85" <c:if test="${'85' eq form.infType}">selected="selected"</c:if>>承接信息</option>
					<option value="4" <c:if test="${'4' eq form.infType}">selected="selected"</c:if>>交易流水</option>
				</select>
			</div>
			<div class="form-group">
				<label>上报时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="sendStartTimeStr" id="start-date-time" class="form-control underline" value="${form.sendStartTimeStr}" />
						<i class="ti-calendar"></i>
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="sendEndtTimeStr" id="end-date-time" class="form-control underline" value="${form.sendEndtTimeStr}" />
				</div>
			</div>

			<div class="form-group">
				<label>发送状态</label>
				<select name="sendStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq form.sendStatus}">selected="selected"</c:if>>初始</option>
					<option value="1" <c:if test="${'1' eq form.sendStatus}">selected="selected"</c:if>>成功</option>
					<option value="9" <c:if test="${'9' eq form.sendStatus}">selected="selected"</c:if>>失败</option>
					<option value="99" <c:if test="${'99' eq form.sendStatus}">selected="selected"</c:if>>无响应</option>
				</select>
			</div>

			<div class="form-group">
				<label>批次号</label>
				<input type="text" name="logOrdId" class="form-control underline" value="${form.logOrdId}" />
			</div>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/certsendexception/certerrorlog.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
