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


<shiro:hasPermission name="couponconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="优惠券管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">优惠券管理</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="couponconfig:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${couponConfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${couponConfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="couponconfig:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加">发行<i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="couponconfig:EXPORT">
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
							</shiro:hasPermission>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">优惠券名称</th>
										<th class="center">优惠券编号</th>
										<th class="center">优惠券类型</th>
										<th class="center">面值</th>
										<th class="center">发行数量</th>
										<th class="center">已发放数量</th>
										<th class="center">有效期</th>
										<th class="center">发行时间</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty couponConfigForm.recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${couponConfigForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((couponConfigForm.paginatorPage - 1) * couponConfigForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.couponName }"></c:out></td>
													<td class="center"><c:out value="${record.couponCode }"></c:out></td>
													<td class="center"><c:out value="${record.couponType == '1' ? '体验金' : record.couponType == '2' ? '加息券' : '代金券' }"></c:out></td>
													<td class="center">
													<c:if test="${record.couponType == '1' or record.couponType == '3'}">
														<c:out value="${record.couponQuota }"></c:out>元
													</c:if>
													<c:if test="${record.couponType == '2'}">
														<c:out value="${record.couponQuota }"></c:out>%
													</c:if>
													
													</td>
													<td class="center"><c:out value="${record.couponQuantity}"></c:out></td>
													<td class="center"><c:out value="${record.issueNumber }"></c:out></td>
													<td class="center"><c:out value="${record.expirationDate }"></c:out></td>
													<td class="center"><c:out value="${record.addTime }"></c:out></td>
													<td class="center">
														<c:choose>
															<c:when test="${record.status == 1 }">待审核</c:when>
															<c:when test="${record.status == 2 }">已发行</c:when>
															<c:otherwise>审核未通过</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															
															<c:choose>
																<c:when test="${record.status == 1}">
																	<shiro:hasPermission name="couponconfig:AUDIT">
																		<a class="btn btn-transparent btn-xs fn-Fix" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="审核"><i class="fa fa-gavel"></i></a>
																	</shiro:hasPermission>
																</c:when>
																<c:when test="${record.status == 3}">
																	<shiro:hasPermission name="couponconfig:DELETE">
																		<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
																	</shiro:hasPermission>
																</c:when>
																<c:otherwise>
																	<shiro:hasPermission name="couponconfig:MODIFY">
																		<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
																	</shiro:hasPermission>
																	<c:if test="${record.issueNumber == 0}">
																	<shiro:hasPermission name="couponconfig:DELETE">
																		<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
																	</shiro:hasPermission>
																	</c:if>
																</c:otherwise>
															</c:choose>
															
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:choose>
																		<c:when test="${record.status == 1}">
																			<shiro:hasPermission name="couponconfig:AUDIT">
																				<li><a class="fn-fix" data-id="${record.id }">审核</a></li>
																			</shiro:hasPermission>
																		</c:when>
																		<c:when test="${record.status == 3}">
																			<shiro:hasPermission name="couponconfig:DELETE">
																				<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
																			</shiro:hasPermission>
																		</c:when>
																		<c:otherwise>
																			<shiro:hasPermission name="couponconfig:MODIFY">
																				<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																			</shiro:hasPermission>
																			<c:if test="${record.issueNumber == 0}">
																			<shiro:hasPermission name="couponconfig:DELETE">
																				<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
																			</shiro:hasPermission>
																			</c:if>
																		</c:otherwise>
																	</c:choose>
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
								action="init" paginator="${couponConfigForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value="${couponConfigForm.id}" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${couponConfigForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>优惠券名称</label> 
				<input type="text" name="couponNameSrch" class="form-control input-sm underline"  maxlength="20" value="${couponConfigForm.couponNameSrch}" />
			</div>
			<div class="form-group">
				<label>优惠券编号</label> 
				<input type="text" name="couponCodeSrch" class="form-control input-sm underline"  maxlength="20" value="${couponConfigForm.couponCodeSrch}" />
			</div>
			<div class="form-group">
					<label>优惠券类型</label>
					<select name="couponTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1" <c:if test="${couponConfigForm.couponTypeSrch=='1'}">selected</c:if>>体验金</option>
						<option value="2" <c:if test="${couponConfigForm.couponTypeSrch=='2'}">selected</c:if>>加息券</option>
						<option value="3" <c:if test="${couponConfigForm.couponTypeSrch=='3'}">selected</c:if>>代金券</option>
					</select>
			</div>
			<div class="form-group">
					<label>状态</label>
					<select name="status" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1" <c:if test="${couponConfigForm.status=='1'}">selected</c:if>>待审核</option>
						<option value="2" <c:if test="${couponConfigForm.status=='2'}">selected</c:if>>已发行</option>
						<option value="3" <c:if test="${couponConfigForm.status=='3'}">selected</c:if>>审核不通过</option>
					</select>
			</div>
			<div class="form-group">
				<label>发行时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${couponConfigForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${couponConfigForm.timeEndSrch}" />
				</div>
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

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/coupon/config/config.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
