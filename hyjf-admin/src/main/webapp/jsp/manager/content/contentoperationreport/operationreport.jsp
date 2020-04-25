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
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<shiro:hasPermission name="operationreport:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="运营报告配置" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">运营报告配置</h1>
			<span class="mainDescription">本功能可以增加修改删除运营报告。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
								<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${contentoperationreportForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${contentoperationreportForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="operationreport:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="添加运营报告">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="operationreport:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
									   data-toggle="tooltip" data-placement="bottom"
									   data-original-title="导出Excel">
										导出Excel <i class="fa fa-Export"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
								   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
								   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
								<%-- 公司环境列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col />
									<col />
									<col style="width:93px;" />
								</colgroup>
								<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">标题名称</th>
									<th class="center">累计交易额</th>
									<th class="center">累计赚取收益</th>
									<th class="center">平台注册人数</th>
									<th class="center">月（季/年）成交笔数</th>
									<th class="center">月（季/年）成交金额</th>
									<th class="center">月（季/年）为用户赚取收益</th>
									<th class="center">状态</th>
									<th class="center">发布时间</th>
									<th class="center">操作</th>
								</tr>
								</thead>
								<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty contentoperationreportForm.recordList}">
										<tr>
											<td colspan="11">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${contentoperationreportForm.recordList }" var="record"
												   begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((contentoperationreportForm.paginatorPage - 1) * contentoperationreportForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.cnName }"></c:out></td>
												<td class="center">
													<c:out value="${record.allAmount }"></c:out>
												</td>
												<td class="center">
													<c:out value="${record.allProfit }"></c:out>
												</td>
												<td class="center">
													<c:out value="${record.registNum }"></c:out>
												</td>
												<td class="center">
													<c:out value="${record.successDealNum }"></c:out>
												</td>
												<td class="center">
													<c:out value="${record.operationAmount }"></c:out>
												</td>
												<td class="center">
													<c:out value="${record.operationProfit }"></c:out>
												</td>
												<td class="center">
													<c:choose>
													<c:when test="${record.isRelease == 0 }">未发布</c:when>
													<c:when test="${record.isRelease == 1 }">已发布</c:when>
													<c:otherwise>未发布</c:otherwise>
													</c:choose>
												</td>
												<td class="center">
													<c:out value="${record.releaseTimeStr }"></c:out>
												</td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<a class="btn btn-transparent btn-xs fn-View" target="_blank" href="${webUrl}/report/initMonthReport.do?id=${record.id}" data-toggle="tooltip" tooltip-placement="top" data-original-title="预览" target="_blank"><i class="ti-layers-alt"></i></a>
														<shiro:hasPermission name="operationreport:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															<%--<c:choose>
															<c:when test="${record.isRelease == 0 }">
																<a class="btn btn-transparent btn-xs tooltips fn-Publish" data-id="${record.id }" data-release="0"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="发布"><i class="fa fa-lightbulb-o fa-white"></i></a>
															</c:when>
															<c:otherwise>
																<a class="btn btn-transparent btn-xs tooltips fn-again-Publish" data-id="${record.id }" data-release="1"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="再次发布"><i class="fa fa-lightbulb-o fa-white"></i></a>
															</c:otherwise>
															</c:choose>--%>

														</shiro:hasPermission>
														<shiro:hasPermission name="operationreport:DELETE">
															<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
														</shiro:hasPermission>

													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																<li><a class="fa-white" href=""  target="_blank">预览</a></li>
																<shiro:hasPermission name="operationreport:MODIFY">
																	<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="operationreport:DELETE">
																	<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
											action="init" paginator="${contentoperationreportForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="pageStatus" id="pageStatus" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${contentoperationreportForm.paginatorPage}" />
			<div class="form-group">
				<label>名称</label>
				<select name="typeSearch" class="form-control underline form-select2">
					<%--<c:if test="${recharge.nameCd eq rechargeForm.statusSearch}">selected="selected"</c:if>>--%>
					<option value="">请选择</option>
					<option value="1" <c:if test="${'1' eq contentoperationreportForm.typeSearch}">selected</c:if> >1月运营报告</option>
					<option value="2" <c:if test="${'2' eq contentoperationreportForm.typeSearch}">selected</c:if>>2月运营报告</option>
					<option value="3" <c:if test="${'3' eq contentoperationreportForm.typeSearch}">selected</c:if>>3月运营报告</option>
					<option value="4" <c:if test="${'4' eq contentoperationreportForm.typeSearch}">selected</c:if>>4月运营报告</option>
					<option value="5" <c:if test="${'5' eq contentoperationreportForm.typeSearch}">selected</c:if>>5月运营报告</option>
					<option value="6" <c:if test="${'6' eq contentoperationreportForm.typeSearch}">selected</c:if>>6月运营报告</option>
					<option value="7" <c:if test="${'7' eq contentoperationreportForm.typeSearch}">selected</c:if>>7月运营报告</option>
					<option value="8" <c:if test="${'8' eq contentoperationreportForm.typeSearch}">selected</c:if>>8月运营报告</option>
					<option value="9" <c:if test="${'9' eq contentoperationreportForm.typeSearch}">selected</c:if>>9月运营报告</option>
					<option value="10" <c:if test="${'10' eq contentoperationreportForm.typeSearch}">selected</c:if>>10月运营报告</option>
					<option value="11" <c:if test="${'11' eq contentoperationreportForm.typeSearch}">selected</c:if>>11月运营报告</option>
					<option value="12" <c:if test="${'12' eq contentoperationreportForm.typeSearch}">selected</c:if>>12月运营报告</option>
					<option value="13" <c:if test="${'13' eq contentoperationreportForm.typeSearch}">selected</c:if>>第一季度运营报告</option>
					<option value="14" <c:if test="${'14' eq contentoperationreportForm.typeSearch}">selected</c:if>>第三季度运营报告</option>
					<option value="15" <c:if test="${'15' eq contentoperationreportForm.typeSearch}">selected</c:if>>上半年运营报告</option>
					<option value="16" <c:if test="${'16' eq contentoperationreportForm.typeSearch}">selected</c:if>>年度运营报告</option>
				</select>
			</div>
			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startCreate" id="start-date-time" class="form-control underline" value="${contentoperationreportForm.startCreate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endCreate" id="end-date-time" class="form-control underline" value="${contentoperationreportForm.endCreate}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
					name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/content/contentoperationreport/operationreport.js"></script>
		</tiles:putAttribute>

	</tiles:insertTemplate>
</shiro:hasPermission>
