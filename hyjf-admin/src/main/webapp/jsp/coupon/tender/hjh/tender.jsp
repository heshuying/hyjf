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
		<tiles:putAttribute name="pageTitle" value="优惠券使用" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">优惠券使用</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="COUPONTENDERHZT:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${couponTenderForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${couponTenderForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="COUPONTENDERHZT:EXPORT">
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
										<th class="center">订单号</th>
										<th class="center">用户名</th>
										<th class="center">用券时属性</th>
										<th class="center">用户优惠券id</th>
										<th class="center">优惠券类型编号</th>
										<th class="center">优惠券类型</th>
										<th class="center">面值</th>
										<th class="center">智投编号</th>
										<th class="center">出借金额</th>
										<th class="center">项目期限</th>
										<th class="center">出借利率</th>
										<th class="center">还款状态</th>
										<th class="center">操作平台</th>
										<th class="center">使用时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="16">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((couponTenderForm.paginatorPage - 1) * couponTenderForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.username}"></c:out></td>
													<td class="center"><c:out value="${record.attribute==0?'无主单': record.attribute==1? '有主单' : record.attribute==2 ? '线下员工' : record.attribute==3 ? '线上员工' : ''}"></c:out></td>
													<td class="center"><c:out value="${record.couponUserCode }"></c:out></td>
													<td class="center"><c:out value="${record.couponCode }"></c:out></td>
													<td class="center"><c:out value="${record.couponTypeStr}"></c:out></td>
													<td class="center"><c:out value="${record.couponQuota }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td align="center"><fmt:formatNumber value="${record.account}" type="number" pattern="#,##0.00#" /> </td>
													<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
													<td class="center"><c:out value="${record.receivedFlgAll }"></c:out></td>
													<td class="center"><c:out value="${record.operatingDeck }"></c:out></td>
													<td class="center"><c:out value="${record.orderDate }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="COUPONTENDERHZT:INFO">
																<a class="btn btn-transparent btn-xs fn-Info" 
																data-id="${record.userId }_${record.borrowNid }_${record.orderId }_${record.id }" 
																data-toggle="tooltip" tooltip-placement="top" data-original-title="详情">
																<i class="fa fa-list-ul"></i></a>
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
																	<shiro:hasPermission name="COUPONTENDERHZT:INFO">
																		<li><a class="fn-Info" 
																		data-id="${record.userId }_${record.borrowNid }_${record.orderId }_${record.id }" >详情</a></li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>
											<tr>
												<td class="center">总计</td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td align="center"><fmt:formatNumber value="${investTotal}" type="number" pattern="#,##0.00#" /> </td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="searchAction" paginator="${couponTenderForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>

		</tiles:putAttribute>
		

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value="${couponTenderForm.id}" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${couponTenderForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>订单号</label> 
				<input type="text" name="orderId" class="form-control input-sm underline"  maxlength="20" value="${ couponTenderForm.orderId}" />
			</div>
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="username" class="form-control input-sm underline"  maxlength="20" value="${ couponTenderForm.username}" />
			</div>
			<div class="form-group">
				<label>优惠券编号</label> 
				<input type="text" name="couponCode" class="form-control input-sm underline"  maxlength="20" value="${ couponTenderForm.couponCode}" />
			</div>
			<div class="form-group">
					<label>优惠券类型</label>
					<select name="couponType" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="1" <c:if test="${couponTenderForm.couponType=='1'}">selected</c:if>>体验金</option>
						<option value="2" <c:if test="${couponTenderForm.couponType=='2'}">selected</c:if>>加息券</option>
						<option value="3" <c:if test="${couponTenderForm.couponType=='3'}">selected</c:if>>代金券</option>
					</select>
			</div>
			<div class="form-group">
					<label>操作平台</label>
					<select name="operatingDeck" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${couponTenderForm.operatingDeck=='0'}">selected</c:if>>pc</option>
						<option value="1" <c:if test="${couponTenderForm.operatingDeck=='1'}">selected</c:if>>微官网</option>
						<option value="2" <c:if test="${couponTenderForm.operatingDeck=='2'}">selected</c:if>>android</option>
						<option value="3" <c:if test="${couponTenderForm.operatingDeck=='3'}">selected</c:if>>ios</option>
					</select>
			</div>
			<div class="form-group">
				<label>状态：</label>
				<select name="couponReciveStatus" id="couponReciveStatus" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${couponReciveStatusList }" var="couponReciveStatus" begin="0" step="1" varStatus="status">
						<option value="${couponReciveStatus.nameCd }"
							<c:if test="${couponReciveStatus.nameCd eq couponTenderForm.couponReciveStatus}">selected="selected"</c:if>>
							<c:out value="${couponReciveStatus.name }"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>使用时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${couponTenderForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${couponTenderForm.timeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/coupon/tender/hjh/tender.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
