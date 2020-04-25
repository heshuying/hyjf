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

<shiro:hasPermission name="nifareportlog:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="互金协会报送日志" />

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
							<shiro:hasPermission name="nifareportlog:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${nifareportlogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${nifareportlogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
									<col style="width:55px;" />
								</colgroup>
								<thead>
								    <tr>
										<th class="center">序号</th>	
										<th class="center">文件包信息</th>
										<th class="center">文件包名</th>
										<th class="center">数据处理日期</th>
										<th class="center">上传时间</th>
										<th class="center">文件状态</th>
										<th class="center">反馈文件状态</th>
										<th class="center">操作</th>
								</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty nifareportlogForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
 											<c:forEach items="${nifareportlogForm.recordList}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((nifareportlogForm.paginatorPage - 1) * nifareportlogForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.packageInformation}"></c:out></td>
													<td class="center"><c:out value="${record.uploadName}"></c:out></td>
													<td class="center"><c:out value="${record.historyData }"></c:out></td>
													<td class="center"><c:out value="${record.strUpdateTime}"></c:out></td>
													<td class="center">
														<c:if test="${record.fileUploadStatus==0}">
														<c:out value="未处理"></c:out>
														</c:if>
														<c:if test="${record.fileUploadStatus==1}">
															<c:out value="成功"></c:out>
														</c:if>
														<c:if test="${record.fileUploadStatus==2}">
															<c:out value="失败"></c:out>
														</c:if>
													</td>
													<td class="center">
														<c:if test="${record.feedbackResult==0}">
															<c:out value="未处理"></c:out>
														</c:if>
														<c:if test="${record.feedbackResult==1}">
															<c:out value="成功"></c:out>
														</c:if>
														<c:if test="${record.feedbackResult==2}">
															<c:out value="失败"></c:out>
														</c:if>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
																<a class="btn btn-transparent btn-xs fn-DownFile" data-id="${record.id}"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="下载文件包">下载文件包</a>
																<c:if test="${record.feedbackResult==1}">
																	<a class="btn btn-transparent btn-xs fn-Feedback" data-id="${record.id }" data-toggle="tooltip"
																	   tooltip-placement="top" data-original-title="下载反馈文件">下载反馈文件</a>
																</c:if>
														</div>
													</td>											
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="nifareportlog:SEARCH">
 								<hyjf:paginator id="equiPaginator" hidden="paginator-page" paginator="${nifareportlogForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value="${nifareportlogForm.id}"  />
 			<input type="hidden" name="paginatorPage" id="paginator-page" value="${nifareportlogForm.paginatorPage == null ? 0 : nifareportlogForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>文件状态</label>
				<select name="fileUploadStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq nifareportlogForm.fileUploadStatus}">selected="selected"</c:if>>未处理</option>
					<option value="1" <c:if test="${'1' eq nifareportlogForm.fileUploadStatus}">selected="selected"</c:if>>成功</option>
					<option value="2" <c:if test="${'2' eq nifareportlogForm.fileUploadStatus}">selected="selected"</c:if>>失败</option>
				</select>
			</div>
			<div class="form-group">
				<label>反馈文件状态</label>
				<select name="feedbackResult" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq nifareportlogForm.feedbackResult}">selected="selected"</c:if>>未处理</option>
					<option value="1" <c:if test="${'1' eq nifareportlogForm.feedbackResult}">selected="selected"</c:if>>成功</option>
					<option value="2" <c:if test="${'2' eq nifareportlogForm.feedbackResult}">selected="selected"</c:if>>失败</option>
				</select>
			</div>
			<div class="form-group">
				<label>数据处理日期</label>
				<input type="text" name="historyData"
					   class="form-control input-sm underline"
					   value="${nifareportlogForm.historyData}" />
			</div>
			<div class="form-group">
				<label>上传时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="uploadImeStart" id="start-date-time" class="form-control underline" value="${nifareportlogForm.uploadImeStart}" />
						<i class="ti-calendar"></i>
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="uploadImeEnd" id="end-date-time" class="form-control underline" value="${nifareportlogForm.uploadImeEnd}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/datacenter/nifareportlog/nifareportlog.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
