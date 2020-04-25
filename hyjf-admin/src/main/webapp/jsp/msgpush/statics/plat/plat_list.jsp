<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="msgpushplatstatics:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="消息推送-平台消息统计" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">消息推送-平台消息统计</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${platStaticsForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${platStaticsForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="msgpushplatstatics:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
 									
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">标签</th>
										<th class="center">日期</th>
										<th class="center">目标推送数</th>
										<th class="center">送达数</th>
										<th class="center">阅读数</th>
										<th class="center">送达百分比</th>
										<th class="center">iOS目标推送数</th>
										<th class="center">iOS送达数</th>
										<th class="center">iOS阅读数</th>
										<th class="center">iOS送达百分比</th>
										<th class="center">Android目标推送数</th>
										<th class="center">Android送达数</th>
										<th class="center">Android阅读数</th>
										<th class="center">Android送达百分比</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty platStaticsForm.recordList}">
											<tr>
												<td colspan="15">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${platStaticsForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((platStaticsForm.paginatorPage - 1) * platStaticsForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center">
													<c:forEach items="${tagList }" var="tag" begin="0" step="1">
															<c:if test="${tag.id eq record.tagId}"> <c:out value="${tag.tagName }"></c:out> </c:if>
													</c:forEach>	
													</td>
													<td class="center"><hyjf:date value="${record.staDate }"></hyjf:date></td>
													<td class="center"><c:out value="${record.destinationCount }"></c:out></td>
													<td class="center"><c:out value="${record.sendCount }"></c:out></td>
													<td class="center"><c:out value="${record.readCount }"></c:out></td>
													<td class="center">
														<c:if test="${record.destinationCount == 0 }">
															0.0%
														</c:if>
														<c:if test="${record.destinationCount > 0 }">
															<fmt:formatNumber value="${record.sendCount / record.destinationCount * 100}" type="number" pattern="0.00" />%
														</c:if>
													</td>
													<td class="center"><c:out value="${record.iosDestinationCount }"></c:out></td>
													<td class="center"><c:out value="${record.iosSendCount }"></c:out></td>
													<td class="center"><c:out value="${record.iosReadCount }"></c:out></td>
													<td class="center">
														<c:if test="${record.iosDestinationCount == 0 }">
															0.0%
														</c:if>
														<c:if test="${record.iosDestinationCount > 0 }">
															<fmt:formatNumber value="${record.iosSendCount / record.iosDestinationCount * 100}" type="number" pattern="0.00" />%
														</c:if>
													</td>
													<td class="center"><c:out value="${record.androidDestinationCount }"></c:out></td>
													<td class="center"><c:out value="${record.androidSendCount }"></c:out></td>
													<td class="center"><c:out value="${record.androidReadCount }"></c:out></td>
													<td class="center">
														<c:if test="${record.androidDestinationCount == 0 }">
															0.0%
														</c:if>
														<c:if test="${record.androidDestinationCount > 0 }">
															<fmt:formatNumber value="${record.androidSendCount / record.androidDestinationCount * 100}" type="number" pattern="0.00" />%
														</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${platStaticsForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${platStaticsForm.paginatorPage}" />
			<div class="form-group">
				<label>标签</label> 
				<select name="tagIdSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${tagList}" var="tag" begin="0" step="1" varStatus="status">
						<option value="${tag.id }" <c:if test="${tag.id eq platStaticsForm.tagIdSrch}">selected="selected"</c:if>> <c:out value="${tag.tagName }"></c:out></option>
					</c:forEach> 
				</select>
			</div>
			
			<div class="form-group">
				<label>发送日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDateSrch" id="start-date-time" class="form-control underline" value="${platStaticsForm.startDateSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endDateSrch" id="end-date-time" class="form-control underline" value="${platStaticsForm.endDateSrch}" />
				</div>
			</div>
			
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.thumbnails-wrap {
				border: 1px solid #ccc;
				padding: 3px;
				display: inline-block;
			}
			.thumbnails-wrap img {
				
				min-width: 35px;
				height: 22px;
			}
			.popover {
				max-width: 500px;
			}
			.popover img {
				max-width: 460px;
			}
			</style>
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/msgpush/statics/plat/plat_list.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
