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
<shiro:hasPermission name="borrowfirst:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款初审" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款初审</h1>
			<span class="mainDescription">借款初审借款初审借款初审借款初审的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="borrowfirst:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowFirstForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowFirstForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<!-- <th class="center">项目名称</th> -->
										<th class="center">借款人用户名</th>
										<th class="center">资产来源</th>
										<th class="center">借款金额</th>
										<th class="center">借款期限</th>
										<th class="center">初审状态</th>
										<th class="center">定时发标时间</th>
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
													<td class="center">${(borrowFirstForm.paginatorPage -1 ) * borrowFirstForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"/></td>
													<%-- <td><c:out value="${record.borrowName }"/></td> --%>
													<td><c:out value="${record.username }"/></td>
													<td><c:out value="${record.instName }"></c:out></td>
													<td align="right"><c:out value="${record.account }"/></td>
													<td align="right"><c:out value="${record.borrowPeriod }"/></td>
													<td class="center"><c:out value="${record.verifyStatusName}"/></td>
													<td class="center"><c:out value="${record.ontime }"/></td>
													<td class="center"><c:out value="${record.addtime }"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowfirst:BORROW_BAIL">
																<c:if test="${record.verifyStatus eq 0}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Bail" data-borrownid="${record.borrowNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="已交保证金">已交保证金</a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="borrowfirst:BORROW_FIRST_AUDIT">
																<c:if test="${record.verifyStatus eq 1}">
																	<a class="btn btn-transparent btn-xs tooltips fn-first" data-pageurl="${pageUrl}" data-borrownid="${record.borrowNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="初审">初审</a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="borrowfirst:BORROW_FIRE">
																<c:if test="${record.verifyStatus eq 2 or record.verifyStatus eq 3}">
																	<a class="btn btn-transparent btn-xs tooltips fn-fire" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }"
																		data-toggle="tooltip" tooltip-placement="top"  data-original-title="发标">发标</a>
																</c:if>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowfirst:BORROW_BAIL">
																		<c:if test="${record.verifyStatus eq 0}">
																			<li>
																				<a class="fn-Bail" data-borrownid="${record.borrowNid }">已交保证金</a>
																			</li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="borrowfirst:BORROW_FIRST_AUDIT">
																		<c:if test="${record.verifyStatus eq 1}">
																			<li>
																				<a class="fn-first" data-pageurl="${pageUrl}" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }">初审</a>
																			</li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="borrowfirst:BORROW_FIRE">
																		<c:if test="${record.verifyStatus eq 2 or record.verifyStatus eq 3}">
																			<li>
																				<a class="fn-fire" data-borrownid="${record.borrowNid }">发标</a>
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
												<!-- <td></td> -->
												<td></td>
												<td></td>
												<td align="right"><c:out value="${sumAccount }"></c:out></td>
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
							<shiro:hasPermission name="borrowfirst:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${borrowFirstForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowfirst:SEARCH">
				<input type="hidden" name=pageUrl id="pageUrl" value=""/>
				<input type="hidden" name=moveFlag id="moveFlag" value="BORROW_FIRST"/>
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowFirstForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowFirstForm.borrowNidSrch}" />
				</div>
<%-- 				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${borrowFirstForm.borrowNameSrch}" />
				</div> --%>
				<div class="form-group">
					<label>借款期限</label>
					<input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${borrowFirstForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
				</div>
				<div class="form-group">
					<label>借款人用户名</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowFirstForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>资产来源</label>
					<select name="instCodeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1" varStatus="status">
							<option value="${inst.instCode }" 
								<c:if test="${inst.instCode eq borrowFirstForm.instCodeSrch}">selected="selected"</c:if>>
								<c:out value="${inst.instName}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>初审状态</label>
					<select name="verifyStatusSrch" id="verifyStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${verifyStatusList }" var="verifyStatus" begin="0" step="1" varStatus="status">
							<option value="${verifyStatus.nameCd }"
								<c:if test="${verifyStatus.nameCd eq borrowFirstForm.verifyStatusSrch}">selected="selected"</c:if>>
								<c:out value="${verifyStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowFirstForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowFirstForm.timeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowfirst/borrowfirst.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
