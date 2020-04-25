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
<shiro:hasPermission name="borrowRegist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款初审" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">标的备案</h1>
			<span class="mainDescription">借款初审借款初审借款初审借款初审的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="borrowRegist:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowRegistForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowRegistForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>

							<input type="hidden" id="pageToken" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<%-- UPD BY LIUSHOUYI 合规检查 START --%><%--
										<th class="center">项目名称</th>
										<th class="center">借款人</th>
										--%><th class="center">借款人用户名</th>
										<%--UPD BY LIUSHOUYI 合规检查 END --%>
										<th class="center">項目类型</th>
										<th class="center">借款金额</th>
										<th class="center">借款期限</th>
										<th class="center">出借利率</th>
										<th class="center">还款方式</th>
										<th class="center">标的备案状态</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowRegistForm.paginatorPage -1 ) * borrowRegistForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"/></td>
													<%-- DEL BY LIUSHOUYI 合规检查 START
													<td><c:out value="${record.borrowName }"/></td>
													DEL BY LIUSHOUYI 合规检查 END --%>
													<td><c:out value="${record.username }"/></td>
													<td align="right"><c:out value="${record.projectTypeName }"/></td>
													<td align="right"><c:out value="${record.account }"/></td>
													<td align="right"><c:out value="${record.borrowPeriod }"/></td>
													<td align="right"><c:out value="${record.borrowApr }"/></td>
													<td class="center"><c:out value="${record.borrowStyleName }"/></td>
													<td class="center"><c:out value="${record.registStatusName}"/></td>
													<td class="center"><c:out value="${record.addtime }"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowRegist:DEBT_REGIST">
																<c:if test="${record.registStatus eq 0  or record.registStatus eq 4}">
																	<a class="btn btn-transparent btn-xs tooltips fn-debtRegist" data-borrownid="${record.borrowNid }" data-borrowname="${record.borrowName }"  data-username="${record.username }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="备案">备案</a>
																</c:if>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowRegist:DEBT_REGIST">
																		<c:if test="${record.registStatus eq 0  or record.registStatus eq 4}">
																			<li>
																				<a class="fn-DebtRegist" data-borrownid="${record.borrowNid }">备案</a>
																			</li>
																		</c:if>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>
											<tr>
												<th class="center">总计</th>
												<td></td>
												<td></td>
												<%-- DEL BY LIUSHOUYI 合规检查 START
												<td></td>
												DEL BY LIUSHOUYI 合规检查 END --%>
												<td></td>
												<td align="right"><c:out value="${sumAccount }"></c:out></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>						
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowRegist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${borrowRegistForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowRegist:SEARCH">
				<input type="hidden" name="moveFlag" id="moveFlag" value="BORROW_FIRST"/>
				<input type="hidden" id="borrowNid" name="borrowNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowRegistForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowRegistForm.borrowNidSrch}" />
				</div>
				<%-- DEL BY LIUSHOUYI 合规检查 START
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${borrowRegistForm.borrowNameSrch}" />
				</div>
				DEL BY LIUSHOUYI 合规检查 START --%>
				<div class="form-group">
					<label>借款期限</label>
					<input type="text" name="borrowPeriodSrch" id="borrowPeriodSrch" class="form-control input-sm underline" value="${borrowRegistForm.borrowPeriodSrch}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
				</div>
				<div class="form-group">
					<%-- UPD BY LIUSHOUYI 合规检查 START
					<label>借款人</label>
					--%><label>借款人用户名</label>
					<%-- UPD BY LIUSHOUYI 合规检查 END --%>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowRegistForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowCd }"
								<c:if test="${borrowProjectType.borrowCd eq borrowRegistForm.projectTypeSrch}">selected="selected"</c:if>>
								<c:out value="${borrowProjectType.borrowName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
							<option value="${borrowStyle.nid }" 
								<c:if test="${borrowStyle.nid eq borrowRegistForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>备案状态</label>
					<select name="registStatusSrch" id="registStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${registStatusList }" var="registStatus" begin="0" step="1" varStatus="status">
							<option value="${registStatus.nameCd }"
								<c:if test="${registStatus.nameCd eq borrowRegistForm.registStatusSrch}">selected="selected"</c:if>>
								<c:out value="${registStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowRegistForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowRegistForm.timeEndSrch}" />
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
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowregist/borrowregist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
