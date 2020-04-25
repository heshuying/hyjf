<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<shiro:hasPermission name="batchborrowrecover:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="放款明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">批次放款</h1>
			<span class="mainDescription">批次放款的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="batchborrowrecover:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${formForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrow:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
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
										<%-- add by LSY START --%>
										<%--<th class="center">资产来源</th>--%>
										<%-- add by LSY END --%>
										<th class="center">批次号</th>
										<th class="center">借款金额</th>
										<th class="center">应放款</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">应收服务费</th> --%>
										<%--<th class="center">放款服务费</th>--%>
										<%-- upd by LSY END --%>
										<th class="center">总笔数</th>
										<th class="center">成功金额</th>
										<th class="center">成功笔数</th>
										<th class="center">失败金额</th>
										<th class="center">失败笔数</th>
										<th class="center">提交时间</th>
										<th class="center">更新时间</th>
										<th class="center">批次状态</th>
										<%--<th class="center">是否加息</th>--%>
										<%--<th class="center">加息放款状态</th>--%>
										<th class="center">银行回执说明</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<%-- UPD BY LSY START --%>
											<%-- <tr><td colspan="16">暂时没有数据记录</td></tr> --%>
											<tr><td colspan="19">暂时没有数据记录</td></tr>
											<%-- UPD BY LSY END --%>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(form.paginatorPage - 1 ) * form.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<%-- add by LSY START --%>
													<%--<td class="center"><c:out value="${record.instName }"></c:out></td>--%>
													<%-- add by LSY END --%>
													<td class="center"><c:out value="${record.batchNo }"></c:out></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.borrowAccount}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.batchAmount}" /></td>
													<%--<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.batchServiceFee}" /></td>--%>
													<td class="center"><c:out value="${record.batchCounts }"></c:out></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.sucAmount}" /></td>
													<td class="center"><c:out value="${record.sucCounts }"></c:out></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.failAmount}" /></td>
													<td class="center"><c:out value="${record.failCounts }"></c:out></td>
													<td class="center"><c:out value="${record.createTime }"></c:out></td>
													<td class="center"><c:out value="${record.updateTime }"></c:out></td>
													<td class="center"><c:out value="${record.statusStr }"></c:out></td>
													<%--<td class="center"><c:out value="${record.increaseInterestFlag }"></c:out></td>--%>
													<%--<td class="center"><c:out value="${record.extraYieldStatus}"></c:out></td>--%>
													<td class="center"><c:out value="${record.data }"></c:out></td>
													<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="batchborrowrecover:INFO">
																<a class="btn btn-transparent btn-xs tooltips" href="${webRoot}/manager/borrow/borrowrecover/init?borrowNidSrch=${record.borrowNid }"
																		data-toggle="tooltip" data-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="batchborrowrecover:QUERYBATCHDETAIL"> 
																<a class="btn btn-transparent btn-xs tooltips queryBatchDetails"  
																		data-toggle="tooltip"  data-placement="top" data-borrowNid="${record.borrowNid }" data-original-title="查询批次交易明细"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission> 
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm queryBatchDetails" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																<shiro:hasPermission name="batchborrowrecover:INFO">
																	<li>
																		<a href="${webRoot}/manager/borrow/borrowrecover/init">详情</a>
																	</li>
																</shiro:hasPermission>
																<shiro:hasPermission name="batchborrowrecover:QUERYBATCHDETAIL"> 
																	<li>
																		<a class="queryBatchDetails"  data-borrowNid="${record.borrowNid }"> 查询批次交易明细</a>
																	</li>
																</shiro:hasPermission> 
																</ul>
															</div>
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
										<%--<th>&nbsp;</th>--%>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumObject.sumBorrowAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumBatchAmount }" pattern="#,##0.00#"/></td>
										<%--<td align="right"></td>--%>
										<td align="center"><fmt:formatNumber value="${sumObject.sumBatchCounts }" pattern="#,##0"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumSucAmount }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumObject.sumSucCounts }" pattern="#,##0"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumFailAmount }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumObject.sumFailCounts }" pattern="#,##0"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<%--<th>&nbsp;</th>--%>
										<%--<th>&nbsp;</th>--%>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="batchborrowrecover:SEARCH">
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
			<shiro:hasPermission name="batchborrowrecover:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<input type="hidden" name="deatilBorrowNid" id="deatilBorrowNid" value="" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNid" id="borrowNid" class="form-control input-sm underline" value="${form.borrowNid}" />
				</div>
				<%-- add by LSY START --%>
				<%--<div class="form-group">--%>
					<%--<label>资产来源</label>--%>
					<%--<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">--%>
						<%--<option value=""></option>--%>
						<%--<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">--%>
							<%--<option value="${inst.instCode }"--%>
								<%--<c:if test="${inst.instCode eq form.instCodeSrch}">selected="selected"</c:if>>--%>
								<%--<c:out value="${inst.instName }"></c:out>--%>
							<%--</option>--%>
						<%--</c:forEach>--%>
					<%--</select>--%>
				<%--</div>--%>
				<%-- add by LSY END --%>
				<div class="form-group">
					<label>批次号</label>
					<input type="text" name="batchNo" id="batchNo" class="form-control input-sm underline" value="${form.batchNo}" />
				</div>
				<div class="form-group">
					<label>批次状态</label>
					<select name="status" id="status" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${recoverStatusList }" var="recoverStatus" begin="0" step="1" varStatus="status">
							<option value="${recoverStatus.nameCd }"
								<c:if test="${recoverStatus.nameCd eq form.status}">selected="selected"</c:if>>
								<c:out value="${recoverStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>批次提交时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="submitTimeStartSrch" id="start-date-time" class="form-control underline" value="${form.submitTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="submitTimeEndSrch" id="end-date-time" class="form-control underline" value="${form.submitTimeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/batchcenter/borrowrecover/batchborrowrecover.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
