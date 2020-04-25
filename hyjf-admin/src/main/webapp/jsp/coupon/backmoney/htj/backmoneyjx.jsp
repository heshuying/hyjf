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


<shiro:hasPermission name="HTJBACKMONEYJX:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="优惠券回款-加息券" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">优惠券回款-加息券</h1>
			<span class="mainDescription">本功能可以查询列表。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
				<shiro:hasPermission name="HTJBACKMONEYTY:VIEW">
		      		<li><a href="${webRoot}/coupon/backmoneyty/htj/init">体验金</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="HTJBACKMONEYJX:VIEW">
		      		<li  class="active"><a href="${webRoot}/coupon/backmoneyjx/htj/init">加息券</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="HTJBACKMONEYDJ:VIEW">
		      		<li><a href="${webRoot}/coupon/backmoneydj/htj/init">代金券</a></li>
		      	</shiro:hasPermission>
		    </ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${couponBackMoneyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${couponBackMoneyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="HTJBACKMONEYJX:EXPORT">
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
								<th class="center">用户优惠券id</th>
								<th class="center">优惠券类型编号</th>
								<th class="center">智投编号</th>
								<th class="center">回款期数</th>
								<th class="center">出借金额</th>
								<th class="center">应回款（元）</th>
								<th class="center">转账订单号</th>
								<th class="center">状态</th>
								<th class="center">使用时间</th>
								<th class="center">应回款日期</th>
								
								<th class="center">实际回款日期</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty recordList}">
									<tr>
										<td colspan="14">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${recordList }" var="record"
										begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out
													value="${status.index+((couponBackMoneyForm.paginatorPage - 1) * couponBackMoneyForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.nid }"></c:out></td>
											<td class="center"><c:out value="${record.username }"></c:out></td>
											<td class="center"><c:out value="${record.couponUserCode }"></c:out></td>
											<td class="center"><c:out value="${record.couponCode }"></c:out></td>
											
											<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
											<td class="center"><c:out value="${record.recoverPeriod }"></c:out></td>
											<td class="center"><c:out value="${record.recoverCapital }"></c:out></td>
											<td class="center"><c:out value="${record.recoverInterest }"></c:out></td>
											<td class="center"><c:out value="${record.transferId }"></c:out></td>
											<td class="center"><c:out value="${record.receivedFlg }"></c:out></td>
											<td class="center"><c:out value="${record.addTime }"></c:out></td>
											<td class="center"><c:out value="${record.recoverTime }"></c:out></td>
											
											<td class="center"><c:out value="${record.recoverYestime }"></c:out></td>
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
										
										<td class="center"><fmt:formatNumber value="${investTotal}" type="number" pattern="#,##0.00#" /></td>
										<td class="center"><fmt:formatNumber value="${recoverInterest}" type="number" pattern="#,##0.00#" /></td>
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
						action="searchAction" paginator="${couponBackMoneyForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<!-- <input type="hidden" name="ids" id="ids" /> -->
			<input type="hidden" name="couponType"  value='2'/>
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${couponBackMoneyForm.paginatorPage}" />
			<div class="form-group">
				<label>订单号：</label> 
				<input type="text" name="orderId" class="form-control input-sm underline"  maxlength="20" value="${ couponBackMoneyForm.orderId}" />
			</div>
			<div class="form-group">
				<label>用户名：</label> 
				<input type="text" name="username" class="form-control input-sm underline"  maxlength="20" value="${ couponBackMoneyForm.username}" />
			</div>
			<div class="form-group">
				<label>用户优惠券id：</label> 
				<input type="text" name="couponUserCode" class="form-control input-sm underline"  maxlength="20" value="${ couponBackMoneyForm.couponUserCode}" />
			</div>
			<div class="form-group">
				<label>优惠券类别编号：</label> 
				<input type="text" name="couponCode" class="form-control input-sm underline"  maxlength="20" value="${ couponBackMoneyForm.couponCode}" />
			</div>
			<div class="form-group">
				<label>智投编号：</label>
				<input type="text" name="borrowNid" class="form-control input-sm underline"  maxlength="20" value="${ couponBackMoneyForm.borrowNid}" />
			</div>
			
			<div class="form-group">
				<label>状态：</label>
				<select name="couponReciveStatus" id="couponReciveStatus" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${couponReciveStatusList }" var="couponReciveStatus" begin="0" step="1" varStatus="status">
						<option value="${couponReciveStatus.nameCd }"
							<c:if test="${couponReciveStatus.nameCd eq couponBackMoneyForm.couponReciveStatus}">selected="selected"</c:if>>
							<c:out value="${couponReciveStatus.name }"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>应回款日期：</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${couponBackMoneyForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${couponBackMoneyForm.timeEndSrch}" />
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
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/coupon/backmoney/htj/backmoneyjx.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
