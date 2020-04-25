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
<shiro:hasPermission name="plancapitallist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="复投债转标的" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">复投债转标的</h1>
			<span class="mainDescription">复投债转标的</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="plancapitallist:VIEW">
				      		<li><a href="${webRoot}/manager/hjhreinvestdetail/init?date=${reinvestdebtForm.date}&planNid=${reinvestdebtForm.planNid}">复投原始标的</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="plancapitallist:VIEW">
				      		<li class="active"><a href="${webRoot}/manager/hjhreinvestdebt/init?date=${reinvestdebtForm.date}&planNid=${reinvestdebtForm.planNid}">复投承接债权</a></li>
				      	</shiro:hasPermission>
				    </ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="plancapitallist:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${reinvestdebtForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${reinvestdebtForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="plancapitallist:EXPORT">
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
										<th class="center">智投订单号</th>
										<th class="center">承接智投编号</th>
										<th class="center">承接订单号</th>
										<th class="center">承接人</th>
										<th class="center">出让人</th>
										<th class="center">债转编号</th>
										<th class="center">原项目编号</th>
										<th class="center">还款方式</th>
										<th class="center">承接本金</th>
										<th class="center">垫付利息</th>
										<th class="center">实际支付金额</th>
										<th class="center">承接方式</th>
										<th class="center">项目总期数</th>
										<th class="center">承接时所在期数</th>
										<th class="center">承接时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="16">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(reinvestdebtForm.paginatorPage -1 ) * reinvestdebtForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.assignPlanOrderId }"/></td>
													<td class="center"><c:out value="${record.assignPlanNid }"/></td>
													<td align="center"><c:out value="${record.assignOrderId }" /></td>
													<td align="center"><c:out value="${record.userName }" /></td>
													<td align="center"><c:out value="${record.creditUserName }" /></td>
													<td align="center"><c:out value="${record.creditNid }" /></td>
													<td align="center"><c:out value="${record.borrowNid }" /></td>
													<td align="center"><c:out value="${record.borrowStyle }" /></td>
													<td align="center"><c:out value="${record.assignCapital }" /></td>
													<td align="center"><c:out value="${record.assignInterestAdvance }" /></td>
													<td align="center"><c:out value="${record.assignPay }" /></td>
													<td align="center">
														<c:choose>
															<c:when test="${record.assignType eq '0'}">自动承接</c:when>
															<c:when test="${record.assignType eq '1'}">手动承接</c:when>
														</c:choose>
													</td>
													<td align="center"><c:out value="${record.borrowPeriod }" /></td>
													<td align="center"><c:out value="${record.assignPeriod }" /></td>
													<td align="center"><c:out value="${record.assignTime }" /></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
                                    <%-- 总计 --%>
                                    <tr>
                                        <th class="center">总计</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <td align="center"><fmt:formatNumber value="${total.assignCapital }" pattern="#,##0.00#"/></td>
                                        <td align="center"><fmt:formatNumber value="${total.assignInterestAdvance }" pattern="#,##0.00#"/></td>
                                        <td align="center"><fmt:formatNumber value="${total.assignPay }" pattern="#,##0.00#"/></td>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                    </tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${reinvestdebtForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="plancapitallist:SEARCH">
				<input type="hidden" name="export" id="export" />
				<%-- 页面跳转时传入两个查询字段 --%>
				<input type="hidden" name="planNid" id="planNid" value = "${reinvestdebtForm.planNid}"/>
				<input type="hidden" name="date" id="date" value = "${reinvestdebtForm.date}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${reinvestdebtForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="assignPlanOrderIdSrch" class="form-control input-sm underline" value="${reinvestdebtForm.assignPlanOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>承接智投编号</label>
					<input type="text" name="assignPlanNidSrch" class="form-control input-sm underline" value="${reinvestdebtForm.assignPlanNidSrch}" />
				</div>
				<div class="form-group">
					<label>承接订单号</label>
					<input type="text" name="assignOrderIdSrch" class="form-control input-sm underline" value="${reinvestdebtForm.assignOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>承接人</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${reinvestdebtForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>出让人</label>
					<input type="text" name="creditUserNameSrch" class="form-control input-sm underline" value="${reinvestdebtForm.creditUserNameSrch}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNidSrch" class="form-control input-sm underline" value="${reinvestdebtForm.creditNidSrch}" />
				</div>
				<div class="form-group">
					<label>原项目编号</label>
					<input type="text" name="borrowNidSrch" class="form-control input-sm underline" value="${reinvestdebtForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>承接方式</label>
					<select name="assignTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${reinvestdebtForm.assignTypeSrch eq '0'}">selected="selected"</c:if>>自动承接</option>
						<option value="1" <c:if test="${reinvestdebtForm.assignTypeSrch eq '1'}">selected="selected"</c:if>>手动承接</option>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="按天计息，到期还本还息" <c:if test="${reinvestdebtForm.borrowStyleSrch eq '按天计息，到期还本还息'}">selected="selected"</c:if>>按天计息，到期还本还息</option>
						<option value="按月计息，到期还本还息" <c:if test="${reinvestdebtForm.borrowStyleSrch eq '按月计息，到期还本还息'}">selected="selected"</c:if>>按月计息，到期还本还息</option>
						<option value="等额本息" <c:if test="${reinvestdebtForm.borrowStyleSrch eq '等额本息'}">selected="selected"</c:if>>等额本息</option>
						<option value="等额本金" <c:if test="${reinvestdebtForm.borrowStyleSrch eq '等额本金'}">selected="selected"</c:if>>等额本金</option>
						<option value="先息后本" <c:if test="${reinvestdebtForm.borrowStyleSrch eq '先息后本'}">selected="selected"</c:if>>先息后本</option>
					</select>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/hjhreinvestdetail/hjhreinvestdetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
