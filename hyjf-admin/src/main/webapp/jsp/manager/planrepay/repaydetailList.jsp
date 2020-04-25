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

<shiro:hasPermission name="planrepay:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款明细" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款明细</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			
			<!-- 新增tab区分 -->
				<ul class="nav nav-tabs" id="myTab">
					<!-- 还款明细 -->
					<shiro:hasPermission name="planrepay:VIEW">
			      		<li class="active"><a href="${webRoot}/manager/hjhplan/planrepay/repayInfoAction?accedeOrderIdSrch=${orderId }">还款明细 </a></li>
			      	</shiro:hasPermission>
			      	
			    	 <!-- 还款-承接明细  -->	
					<shiro:hasPermission name="planrepay:VIEW">
						<li><a href="${webRoot}/manager/hjhplan/planrepay/creditTenderAction?accedeOrderIdSrch=${orderId}">还款-承接明细</a></li>
					</shiro:hasPermission>	
				</ul>
			
			<div class="tab-content">
				<div class="tab-pane fade in active">							
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${repayForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${repayForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
					</div>
					<%-- 列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">项目编号</th>
								<th class="center">智投编号</th>
								<th class="center">资产来源</th>
								<th class="center">还款方式</th>
								<th class="center">产品类型</th>							
								<th class="center">借款人</th>
								
								<th class="center">应还本金</th>
								<th class="center">应还利息</th>
								<th class="center">应还本息</th>
								
								<th class="center">已还本息</th>
								<th class="center">还款服务费</th>
								<th class="center">还款状态</th>
								
								<th class="center">实际还款时间</th>
								<th class="center">应还款时间</th>
								
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
											<td class="center"><c:out value="${status.index+((repayForm.paginatorPage - 1) * repayForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
											<td class="center"><c:out value="${record.planNid }"></c:out></td>
											<td class="center"><c:out value="${record.instName }"></c:out></td>
											<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
											<td class="center"><c:out value="${record.projectTypeName }"></c:out></td>
											
											<td class="center"><c:out value="${record.borrowUserName }"></c:out></td>
											
											<td class="center"><c:out value="${record.repayAccountCapital }"></c:out></td>
											<td class="center"><c:out value="${record.repayAccountInterest }"></c:out></td>
											<td class="center"><c:out value="${record.repayAccountAll }"></c:out></td>
											<td class="center"><c:out value="${record.repayAccountYes }"></c:out></td>
											<td class="center"><c:out value="${record.repayFee }"></c:out></td>
											<td class="center"><c:if test="${record.status==0 }">
												还款中
												</c:if> <c:if test="${record.status==1 }">
												已还款
												</c:if>
											</td>
											<td class="center"><c:out value="${record.repayActionTime }"></c:out></td>
											<td class="center"><c:out value="${record.repayLastTime }"></c:out></td>
														
											<td class="center">
												<%-- <c:if test="${record.isMonth}"> --%>
														<c:set var="idstr"
															value="${record.borrowNid}"></c:set>
														<c:set var="accedeOrderId"
															value="${accedeOrderId}"></c:set>
														<c:set var="nidstr"
															value="${record.nid}"></c:set>
														<c:set var="ismonth"
															value="${record.isMonth}"></c:set>
														<shiro:hasPermission name="borrowrepayment:INFO">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<a class="btn btn-transparent btn-xs fn-toRepayPlanDetail" data-id="${idstr}" data-nid="${nidstr}" data-ismonth="${ismonth}"
																		data-toggle="tooltip" data-placement="top" data-original-title="详情">详情</a>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<li>
																			<a class="fn-toRepayPlanDetail" data-id="${idstr}"  data-nid="${nidstr}" data-ismonth="${ismonth}">详情</a>
																		</li>
																	</ul>
																</div>
															</div>
														</shiro:hasPermission>
												<%-- </c:if>	 --%>	
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							<%-- add by LSY START --%>
							<tr>
								<th class="center">总计</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<td align="center"><fmt:formatNumber value="${sumObject.sumRepayAccountCapital }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumObject.sumRepayAccountInterest }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumObject.sumRepayAccountAll }" pattern="#,##0.00#"/></td>
								<td align="center">&nbsp;</td>
								<td align="center">&nbsp;</td>
								<td align="center">&nbsp;</td>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
							</tr>
							<%-- add by LSY END --%>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="repayInfoAction" paginator="${repayForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planrepay:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${repayForm.paginatorPage}" />
				<input type="hidden" name="accedeOrderIdSrch" id="accedeOrderIdSrch" value="${repayForm.accedeOrderIdSrch}" />
				
				<input type="hidden" name="accedeOrderId" id="accedeOrderId" value="${accedeOrderId}" />
				
				<input type="hidden" name="borrowNid" id="borrowNid" value="" />
				<input type="hidden" name="nid" id="nid" value="" />
				<input type="hidden" name="isMonth" id="isMonth" value="" />
				<!-- 查询条件 -->
			</shiro:hasPermission>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/planrepay/repaydetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
