<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="debtborrow:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款列表</h1>
			<span class="mainDescription">借款列表的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="debtborrow:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${debtborrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${debtborrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="debtborrow:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="添加借款">添加借款 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="debtborrow:EXPORT">
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
										<th class="center">项目编号</th>
										<th class="center">项目名称</th>
										<th class="center">借款人</th>
										<th class="center">项目申请人</th>
										<th class="center">借款金额</th>
										<th class="center">借款进度</th>
										<th class="center">借款期限</th>
										<th class="center">出借利率</th>
										<th class="center">还款方式</th>
										<th class="center">项目状态</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty debtborrowForm.sort and debtborrowForm.col eq 'b.recover_last_time' }">
													<a href="#" class="fn-Sort" data-col="b.recover_last_time" data-sort="<c:if test="${ debtborrowForm.sort eq 'asc' }">desc</c:if><c:if test="${ debtborrowForm.sort eq 'desc' }">asc</c:if>">放款时间</a>&nbsp;<i class="fa fa-sort-${debtborrowForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="b.recover_last_time" data-sort="desc">放款时间</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
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
													<td class="center">${(debtborrowForm.paginatorPage - 1 ) * debtborrowForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.applicant }"></c:out></td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td align="right"><c:out value="${record.borrowAccountScale }"></c:out></td>
													<td align="right"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td align="right"><c:out value="${record.borrowApr }"></c:out></td>
													<td><c:out value="${record.borrowStyleName }"></c:out></td>
													<td class="center"><c:out value="${record.status }"></c:out></td>
													<td class="center"><c:out value="${record.recoverLastTime }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center">
														
															<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="debtborrow:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }"
																		data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="borrowinvest:VIEW">
															<c:if test="${record.status eq '出借中' or record.status eq '复审中'}">
																<a class="btn btn-transparent btn-xs fn-TenderInfo" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }"
																		data-toggle="tooltip" data-placement="top" data-original-title="出借明细"><i class="fa fa-list-ul fa-white"></i></a>
															</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="debtborrow:PREVIEW">
																<a class="btn btn-transparent btn-xs" target="_blank" href="https://www.hyjf.com/project/getHtjProjectPreview.do?borrowNid=${record.borrowNid }" 
																		data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="ti-layers-alt"></i></a>
															</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="debtborrow:MODIFY">
																		<li>
																			<a class="fn-Modify" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }">修改</a>
																		</li>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="borrowinvest:VIEW">
																		<c:if test="${record.status eq '出借中' or record.status eq '复审中'}">
																		<li>
																			<a class="fn-TenderInfo" data-borrownid="${record.borrowNid }" data-projecttype="${record.projectType }">出借明细</a>
																		</li>
																		</c:if>
																		</shiro:hasPermission>
																		
																		<shiro:hasPermission name="debtborrow:PREVIEW">
																		<li>
																			<a target="_blank" href="https://www.hyjf.com/project/getHtjProjectPreview.do?borrowNid=${record.borrowNid }" >预览</a>
																		</li>
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
							<shiro:hasPermission name="debtborrow:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${debtborrowForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="debtborrow:SEARCH">
				<input type="hidden" name=moveFlag id="moveFlag" value="BORROW_LIST"/>
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="projectType" id="projectType" />
				<input type="hidden" name="col" id="col" value="${debtborrowForm.col}" />
				<input type="hidden" name="sort" id="sort" value="${debtborrowForm.sort}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtborrowForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${debtborrowForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${debtborrowForm.borrowNameSrch}" />
				</div>
				<div class="form-group">
					<label>借款期限</label>
					<input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${debtborrowForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
				</div>
				<div class="form-group">
					<label>借款人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${debtborrowForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>项目状态</label>
					<select name="statusSrch" id="statusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStatusList }" var="borrowStatus" begin="0" step="1" varStatus="status">
							<option value="${borrowStatus.nameCd }"
								<c:if test="${borrowStatus.nameCd eq debtborrowForm.statusSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowCd }"
								<c:if test="${borrowProjectType.borrowCd eq debtborrowForm.projectTypeSrch}">selected="selected"</c:if>>
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
								<c:if test="${borrowStyle.nid eq debtborrowForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>放款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="recoverTimeStartSrch" id="recover-start-date-time" class="form-control underline" value="${debtborrowForm.recoverTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="recoverTimeEndSrch" id="recover-end-date-time" class="form-control underline" value="${debtborrowForm.recoverTimeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${debtborrowForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${debtborrowForm.timeEndSrch}" />
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

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrow/debtborrow.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
