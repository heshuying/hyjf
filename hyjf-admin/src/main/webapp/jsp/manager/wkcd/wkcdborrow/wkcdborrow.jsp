<%@page import="com.hyjf.common.util.GetDate"%>
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

<%-- <shiro:hasPermission name="wkcdBorrow:VIEW" > --%>
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="微可车贷资产汇总" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">微可车贷资产汇总</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- <shiro:hasPermission name="wkcdBorrow:SEARCH"> --%>
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${wkcdBorrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${wkcdBorrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="wkcdBorrow:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							<%-- </shiro:hasPermission> --%>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center" style="width:90px">序号</th>
										<th class="center">微可唯一标识</th>
										<th class="center">用户名</th>
										<th class="center">借款人姓名</th>
										<th class="center">手机号</th>
										<th class="center">借款金额</th>
										<th class="center">年化率</th>
										<th class="center">期限</th>
										<th class="center">车牌</th>
										<th class="center">车型</th>
										<th class="center">所属门店</th>
										<th class="center">微可审核状态</th>
										<th class="center">汇盈审核状态</th>
										<th class="center">对应项目编号</th>
										<th class="center">汇盈审核时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty wkcdBorrowForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${wkcdBorrowForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">
													   ${status.count}
													</td>
													<td class="center"><c:out value="${record.wkcdId}"></c:out></td>
													<td class="center"><c:out value="${record.userName}"></c:out></td>
													<td class="center"><c:out value="${record.truename}"></c:out></td>
													<td class="center"><c:out value="${record.mobile}"></c:out></td>
													<td class="center"><c:out value="${record.borrowAmount}"></c:out></td>
													<td class="center"><c:out value="${record.apr}"></c:out></td>
													<td class="center"><c:out value="${record.wkcdBorrowPeriod}"></c:out></td>
													<td class="center"><c:out value="${record.carNo}"></c:out></td>
													<td class="center"><c:out value="${record.carType}"></c:out></td>
													<td class="center"><c:out value="${record.carShop}"></c:out></td>
													<td class="center"><c:out value="${record.wkcdStatus}"></c:out></td>
													<td class="center">
													  <c:choose>
													     <c:when test="${record.hyjfStatus==0}">未审核</c:when>
													     <c:when test="${record.hyjfStatus==1}">审核通过</c:when>
													     <c:when test="${record.hyjfStatus==2}">审核未通过</c:when>
													     <c:otherwise></c:otherwise>
													  </c:choose>
													</td>
													<td class="center"><c:out value="${record.borrowNid}"></c:out></td>
													<td class="center">
													  <c:if test="${record.checkTime!=null}">
													       <hyjf:date value="${record.checkTime}"></hyjf:date>
													  </c:if>
													</td>
													<td class="center">
													   <shiro:hasPermission name="wkcdBorrow:AUDIT">
                                                         <a class="btn btn-transparent btn-xs fn-Detail" data-id="${record.id }" 
																	data-toggle="tooltip" data-placement="top" data-original-title="查看并审核"><i class="fa fa-file-text"></i></a>	
														 <a class="btn btn-transparent btn-xs fn-Download" data-id="${record.id }" href="${webRoot}/manager/wkcdBorrow/download?id=${record.wkcdId}"
																	data-toggle="tooltip" data-placement="top" data-original-title="下载附件"><i class="fa fa-arrow-down"></i></a>												
													   </shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<%-- <shiro:hasPermission name="wkcdBorrow:SEARCH"> --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${wkcdBorrowForm.paginator}"></hyjf:paginator>
							<%-- </shiro:hasPermission> --%>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<%-- <shiro:hasPermission name="wkcdBorrow:SEARCH"> --%>
			    <input type="hidden" name="id" id="id" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${wkcdBorrowForm.paginatorPage == null ? 0 : wkcdBorrowForm.paginatorPage}"  />
				<input type="hidden" name="fid" id="fid" value="${fid}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>手机号</label>
					<input type="text" id="i_mobile" name="mobile" class="form-control input-sm underline" maxlength="20" value="${wkcdBorrowForm.mobile}"/>
				</div>
				<div class="form-group">
					<label>用户名</label>
					<input type="text" id="i_userName" name="userName" class="form-control input-sm underline" maxlength="20" value="${wkcdBorrowForm.userName}"/>
				</div>
				<div class="form-group">
					<label>汇盈审核状态</label>
					<select id="s_hyjfStatus" name="hyjfStatus" class="form-control input-sm underline">
					   <option value="">请选择</option>
					   <option value="0" <c:if test="${wkcdBorrowForm.hyjfStatus == 0}">selected="selected"</c:if>>未审核</option>
					   <option value="1" <c:if test="${wkcdBorrowForm.hyjfStatus == 1}">selected="selected"</c:if>>审核通过</option>
					   <option value="2" <c:if test="${wkcdBorrowForm.hyjfStatus == 2}">selected="selected"</c:if>>审核不通过</option>
					</select>
				</div>
			<%-- </shiro:hasPermission> --%>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/wkcd/wkcdborrow/wkcdborrow.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
<%-- </shiro:hasPermission> --%>
