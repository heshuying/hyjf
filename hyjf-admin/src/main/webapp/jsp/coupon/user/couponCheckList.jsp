<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<shiro:hasPermission name="couponuser:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="优惠券列表" />
		 
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">优惠券列表</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<ul class="nav nav-tabs" id="myTab">
						<li class="active"><a href="javascript:void(0);">手动批量发放列表</a></li>
						<li ><a href="${webRoot}/coupon/user/init">优惠券批量发放明细</a></li>
					</ul>
					<ul></ul>
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
									<c:set var="jspPrevDsb" value="${couponCheckForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${couponCheckForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="couponuser:IMPORT">
									    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Import"
											data-toggle="tooltip" data-placement="bottom" data-original-title="手动批量发放">手动批量发放<i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="couponuser:DELETE">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-batchDelete"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="批量删除"> 批量删除 <i class="fa"></i></a>
                                </shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="hidden-xs center">
											<div align="left" class="checkbox clip-check check-primary checkbox-inline"
												 data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
										<th class="center">序号</th>
										<th class="center">手动批量发放文件（.csv）</th>
										<th class="center">发放状态</th>
										<th class="center">审核意见备注</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty couponCheckForm.recordList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${couponCheckForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<c:if test="${record.status != '2' }">
														<div class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck"   name="id" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
														</c:if>
													</td>
													<td class="center"><c:out value="${status.index+((couponCheckForm.paginatorPage - 1) * couponCheckForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.fileName }"></c:out></td>
													<td class="center" id="flag">
														<c:if test="${record.status == '1' }">
															<c:out value="待审核"></c:out>
														</c:if>
														<c:if test="${record.status == '2' }">
															<c:out value="审核通过"></c:out>
														</c:if>
														<c:if test="${record.status == '3' }">
															<c:out value="审核不通过"></c:out>
														</c:if>
														<c:if test="${record.status == '4' }">
															<c:out value="审核异常"></c:out>
														</c:if>
													</td>
													<td class="center"><c:out value="${record.mark }"></c:out></td>
													<td class="center">${record.add_time}</td>

													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
                                                            <shiro:hasPermission name="couponuser:AUDIT">
															<c:if test="${record.status == '1' }">
																<a class="btn btn-transparent btn-xs fn-check"
																   data-toggle="tooltip" tooltip-placement="top" data-provides="${record.filePath },${record.id}" data-original-title="审核"><i class="fa ">【审核】</i></a>
															</c:if>
                                                            </shiro:hasPermission>
                                                            <shiro:hasPermission name="couponuser:DOWNLOAD_TEMPLATE">
																<a class="btn btn-transparent btn-xs fn-Download" data-idx="${record.id}"   data-toggle="tooltip" tooltip-placement="top" data-original-title="下载"><i class="fa  fa fa-white">【下载】</i></a>
                                                            </shiro:hasPermission>
                                                            <shiro:hasPermission name="couponuser:DELETE">
															<c:if test="${record.status == '1'|| record.status == '3'}">
																<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }"  data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa ">【删除】</i></a>
															</c:if>
                                                            </shiro:hasPermission>
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
								action="init" paginator="${couponCheckForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="filePath" id="filePath" />
			<input type="hidden" name="fileName" id="fileName" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${couponCheckForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartAddSrch" id="start-date-time" class="form-control underline" value="${couponCheckForm.timeStartAddSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndAddSrch" id="end-date-time" class="form-control underline" value="${couponCheckForm.timeEndAddSrch}" />
				</div>
			</div>
			<div class="form-group">
					<label>发放状态</label>
					<select name="status" id="status"  class="form-control underline form-select2">
						<option value=""></option>
						<option value="1" <c:if test="${couponCheckForm.status=='1'}">selected</c:if>>待审核</option>
						<option value="2" <c:if test="${couponCheckForm.status=='2'}">selected</c:if>>审核通过</option>
						<option value="3" <c:if test="${couponCheckForm.status=='3'}">selected</c:if>>审核不通过</option>
					</select>
			</div>


		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/coupon/user/couponCheckList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>