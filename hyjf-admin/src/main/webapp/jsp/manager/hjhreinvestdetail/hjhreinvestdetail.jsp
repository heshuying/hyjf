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
		<tiles:putAttribute name="pageTitle" value="复投原始标的" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">复投原始标的</h1>
			<span class="mainDescription">复投原始标的</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="plancapitallist:VIEW">
				      		<li class="active"><a href="${webRoot}/manager/hjhreinvestdetail/init?date=${reinvestdetailForm.date}&planNid=${reinvestdetailForm.planNid}">复投原始标的</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="plancapitallist:VIEW">
				      		<li><a href="${webRoot}/manager/hjhreinvestdebt/init?date=${reinvestdetailForm.date}&planNid=${reinvestdetailForm.planNid}">复投承接债权</a></li>
				      	</shiro:hasPermission>
				    </ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="plancapitallist:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${reinvestdetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${reinvestdetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
										<th class="center">智投编号</th>
										<th class="center">用户名</th>
										<th class="center">推荐人</th>
										<th class="center">用户属性</th>
										<th class="center">项目编号</th>
										<th class="center">出借利率</th>
										<th class="center">借款期限</th>
										<th class="center">授权服务金额</th>
										<th class="center">还款方式</th>
										<th class="center">投标方式</th>
										<th class="center">开始计息时间</th>
										<th class="center">出借时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(reinvestdetailForm.paginatorPage -1 ) * reinvestdetailForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.accedeOrderId }"/></td>
													<td class="center"><c:out value="${record.planNid }"/></td>
													<td align="center"><c:out value="${record.userName }" /></td>
													<td align="center"><c:out value="${record.inviteUserName }" /></td>
													<td align="center">
														<c:choose>
															<c:when test="${record.userAttribute eq '0'}">无主单</c:when>
															<c:when test="${record.userAttribute eq '1'}">有主单</c:when>
															<c:when test="${record.userAttribute eq '2'}">线下员工</c:when>
															<c:when test="${record.userAttribute eq '3'}">线上员工</c:when>
														</c:choose>
													</td>
													<td align="center"><c:out value="${record.borrowNid }" /></td>
													<td align="center"><c:out value="${record.expectApr }" /></td>
													<td class="center">
														<c:out value="${record.borrowPeriod }" /><c:out value="${record.isMonth }" />
													</td>
													<td align="center"><c:out value="${record.accedeAccount }" /></td>
													<td align="center"><c:out value="${record.borrowStyle }" /></td>
													<td align="center">
														<c:choose>
															<c:when test="${record.investType eq '0'}">手动投标</c:when>
															<c:when test="${record.investType eq '1'}">预约投标</c:when>
															<c:when test="${record.investType eq '2'}">自动投标</c:when>
														</c:choose>
													</td>
													<td align="center"><c:out value="${record.countInterestTime }" /></td>
													<td align="center"><c:out value="${record.addTime }" /></td>
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
                                        <td align="center"><fmt:formatNumber value="${total }" pattern="#,##0.00#"/></td>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                    </tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${reinvestdetailForm.paginator}"></hyjf:paginator>
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
				<input type="hidden" name="planNid" id="planNid" value = "${reinvestdetailForm.planNid}"/>
				<input type="hidden" name="date" id="date" value = "${reinvestdetailForm.date}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${reinvestdetailForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="accedeOrderIdSrch" class="form-control input-sm underline" value="${reinvestdetailForm.accedeOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${reinvestdetailForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" class="form-control input-sm underline" value="${reinvestdetailForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>借款期限</label>
					<input type="text" name="lockPeriodSrch" class="form-control input-sm underline" value="${reinvestdetailForm.lockPeriodSrch}" />
				</div>
				<div class="form-group">
					<label>投标方式</label>
					<select name="investTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${reinvestdetailForm.investTypeSrch eq '0'}">selected="selected"</c:if>>手动投标</option>
						<option value="1" <c:if test="${reinvestdetailForm.investTypeSrch eq '1'}">selected="selected"</c:if>>预约投标</option>
						<option value="2" <c:if test="${reinvestdetailForm.investTypeSrch eq '2'}">selected="selected"</c:if>>自动投标</option>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="按天计息，到期还本还息" <c:if test="${reinvestdetailForm.borrowStyleSrch eq '按天计息，到期还本还息'}">selected="selected"</c:if>>按天计息，到期还本还息</option>
						<option value="按月计息，到期还本还息" <c:if test="${reinvestdetailForm.borrowStyleSrch eq '按月计息，到期还本还息'}">selected="selected"</c:if>>按月计息，到期还本还息</option>
						<option value="等额本息" <c:if test="${reinvestdetailForm.borrowStyleSrch eq '等额本息'}">selected="selected"</c:if>>等额本息</option>
						<option value="等额本金" <c:if test="${reinvestdetailForm.borrowStyleSrch eq '等额本金'}">selected="selected"</c:if>>等额本金</option>
						<option value="先息后本" <c:if test="${reinvestdetailForm.borrowStyleSrch eq '先息后本'}">selected="selected"</c:if>>先息后本</option>
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
