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

<shiro:hasPermission name="accedelist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="加入明细" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">加入明细</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<div class="tab-content">
				<div class="tab-pane fade in active">
				
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${accedeForm.paginator.firstPage ? ' disabled' : ''}"></c:set><!-- 原 joindetailForm -->
						<c:set var="jspNextDsb" value="${accedeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="accedelist:EXPORT">
							<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>	
						<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" 
							data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
							<i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
						</a>
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
								<th class="center">智投订单号</th>
								<th class="center">智投编号</th>
								<th class="center">服务回报期限</th>
								<th class="center">用户名</th>
								<th class="center">授权服务金额(元)</th>
								<th class="center">已出借金额(元)</th>
								
								<th class="center">待还总额(元)</th>
								<th class="center">待还本金(元)</th>
								<th class="center">待还利息(元)</th>
								
								<th class="center">操作平台</th>
								<th class="center">订单状态</th>
								<th class="center">开始计息时间</th>
								<th class="center">授权服务时间</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty accedeForm.recordList}">
									<tr>
										<td colspan="15">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${accedeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((accedeForm.paginatorPage - 1) * accedeForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
											<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
											<td class="center"><c:out value="${record.debtLockPeriod }"></c:out>
												<c:if test="${record.isMonth eq 0}">
													天
												</c:if>
												<c:if test="${record.isMonth eq 1}">
													个月
												</c:if>
											</td>
											<td class="center"><c:out value="${record.userName }"></c:out></td>
											<td class="center"><c:out value="${record.accedeAccount }"></c:out></td>
											<td class="center"><c:out value="${record.alreadyInvest }"></c:out></td>
											
											<td class="center"><c:out value="${record.waitTotal }"></c:out></td>
											<td class="center"><c:out value="${record.waitCaptical }"></c:out></td>
											<td class="center"><c:out value="${record.waitInterest }"></c:out></td>
											
											<td class="center">
												<c:if test="${record.platform eq '0' }">
													PC
												</c:if>
												<c:if test="${record.platform eq '1' }">
													微官网
												</c:if>
												<c:if test="${record.platform eq '2' }">
													android
												</c:if>
												<c:if test="${record.platform eq '3' }">
													IOS
												</c:if>
												<c:if test="${record.platform eq '4' }">
													其他
												</c:if>
											</td>
											<!-- 订单状态 ：0自动投标中 2自动投标成功 3锁定中 5退出中 7已退出 99 自动出借异常-->
											<td class="center">
												<!-- <c:if test="${record.orderStatus eq '0' }">
													自动投标中
												</c:if>
												<c:if test="${record.orderStatus eq '2' }">
													自动投标成功
												</c:if>
												<c:if test="${record.orderStatus eq '3' }">
													锁定中
												</c:if>
												<c:if test="${record.orderStatus eq '5' }">
													退出中
												</c:if>
												<c:if test="${record.orderStatus eq '7' }">
													已退出
												</c:if>
												<c:if test="${record.orderStatus eq '99' }">
													自动出借异常
												</c:if> -->
												<c:out value="${record.orderStatus}"></c:out>
											</td>
											
											<td class="center">
												<c:out value="${record.countInterestTime }"></c:out>
											</td>
											<td class="center">
												<c:out value="${record.createTime }"></c:out>
											</td>
										
											<!-- 操作 -->
											<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="accedelist:VIEW">
															<a class="btn btn-transparent btn-xs fn-TenderInfo" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }"
																	data-toggle="tooltip" data-placement="top" data-original-title="出借明细"><i class="fa fa-list-ul fa-white"></i></a>
														</shiro:hasPermission>
														<!-- 协议发送状态 ：0未发送 1已发送 -->
															<shiro:hasPermission name="accedelist:AUTOTENDEREXCEPTION">
																<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }" data-toggle="tooltip" data-placement="top" data-original-title="处理"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group">
															<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																<shiro:hasPermission name="accedelist:VIEW">
																	<li>
																		<a class="fn-TenderInfo" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }">出借明细</a>
																	</li>
																</shiro:hasPermission>
																	<shiro:hasPermission name="accedelist:AUTOTENDEREXCEPTION">
																		<li><a class="fn-Modify" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }">处理</a></li>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${accedeForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="accedelist:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${accedeForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="accedeOrderIdSrch" id="accedeOrderIdSrch" class="form-control input-sm underline" value="${accedeForm.accedeOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="debtPlanNidSrch" id="debtPlanNidSrch" class="form-control input-sm underline" value="${accedeForm.debtPlanNidSrch}" />
				</div>
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${accedeForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>推荐人</label>
					<input type="text" name="refereeNameSrch" id="refereeNameSrch" class="form-control input-sm underline" value="${accedeForm.refereeNameSrch}" />
				</div>
				<div class="form-group">
					<label>操作平台</label>
					<select name="platformSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0" <c:if test="${'0' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="PC"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="微官网"></c:out>
						</option>
						<option value="2" <c:if test="${'2' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="android"></c:out>
						</option>
						<option value="3" <c:if test="${'3' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="ios"></c:out>
						</option>
					</select>
				</div>

				<div class="form-group">
					<label>授权服务时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"><input type="text" name="searchStartDate" id="start-date-time" class="form-control underline" value="${accedeForm.searchStartDate}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="searchEndDate" id="end-date-time" class="form-control underline" value="${accedeForm.searchEndDate}" />
					</div>
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/autotenderexception/autotenderexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
