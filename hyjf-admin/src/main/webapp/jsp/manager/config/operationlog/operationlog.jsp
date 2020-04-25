<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

<shiro:hasPermission name="operationlog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="操作日志费率" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">操作日志</h1>
			<span class="mainDescription">本功能可以查询相应的操作日志信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="operationlog:SEARCH">
				      		<li  class="active"><a href="${webRoot}/manager/config/operationlog">费率</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="operationloglist:SEARCH">
				      		<li><a href="${webRoot}/manager/newbankcard/bankcardlist"></a>**</li>
				      	</shiro:hasPermission>
				    </ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="operationlog:SEARCH">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${operationlogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${operationlogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<shiro:hasPermission name="operationlog:EXPORT">
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
										<%-- 角色列表一览 --%>
										<table id="equiList" class="table table-striped table-bordered table-hover">
											<colgroup>
												<col style="width:55px;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
											</colgroup>
											<thead>
												<tr>
													<th class="center">序号</th>
													<th class="center">资产来源</th>
													<th class="center">产品类型</th>
													<th class="center">期限</th>
													<th class="center">自动发标利率</th>
													<th class="center">服务费</th>
													<th class="center">管理费</th>
													<th class="center">收益差率</th>
													<th class="center">逾期利率</th>
													<th class="center">逾期免息天数</th>
													<th class="center">状态</th>
													<th class="center">修改类型</th>
													<th class="center">操作人</th>
													<th class="center">操作时间</th>
												</tr>
											</thead>
											<tbody id="bankcardTbody">
												<c:choose>
													<c:when test="${empty operationlogForm.recordList}">
														<tr><td colspan="14">暂时没有数据记录</td></tr>
													</c:when>
													<c:otherwise>
														<c:forEach items="${operationlogForm.recordList }" var="record" begin="0" step="1" varStatus="status">
															<tr>
																<td class="center">${(operationlogForm.paginatorPage-1)*operationlogForm.paginator.limit+status.index+1 }</td>
																<td class="center"><c:out value="${record.instName }"></c:out></td>
																<td class="center"><c:out value="${record.assetTypeName }"></c:out></td>
																<td class="center"><c:out value="${record.borrowPeriods }"></c:out></td>
																<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
																<td class="center"><c:out value="${record.serviceFee }"></c:out></td>
																<td class="center"><c:out value="${record.manageFee }"></c:out></td>
																<td class="center"><c:out value="${record.revenueDiffRate }"></c:out></td>
																<td class="center"><c:out value="${record.lateInterestRate }"></c:out></td>
																<td class="center"><c:out value="${record.lateFreeDays }"></c:out></td>
																<td class="center"><c:out value="${record.statusName }"></c:out></td>
																<td class="center"><c:out value="${record.modifyTypeSrch }"></c:out></td>
																<td class="center"><c:out value="${record.name }"></c:out></td>
																<td class="center"><c:out value="${record.createTimeString }"></c:out></td>
															</tr>
														</c:forEach>					
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<%-- 分页栏 --%>
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="infoAction" paginator="${operationlogForm.paginator}"></hyjf:paginator>
										<br/><br/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<%-- <input type="hidden" name="userId" id="userId" value= "${operationlogForm.userId}"/> --%>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${operationlogForm.paginatorPage}" />
			<!-- 检索条件 -->
			<%-- <div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${bankcardListForm.userName}" />
			</div> --%>
			<div class="form-group">
				<label>资产来源</label>
				<select name="instCodeSrch" id="instCodeSrch" class="form-control input-sm form-select2">
					<option value=""></option>
					<c:forEach items="${hjhInstConfigs }" var="inst" begin="0" step="1">
						<option value="${inst.instCode }"
							<c:if test="${inst.instCode eq operationlogForm.instCodeSrch}">selected="selected"</c:if>>
							<c:out value="${inst.instName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>产品类型</label>
				<select name="assetTypeSrch" id="assetTypeSrch" class="form-control input-sm form-select2">
					<option value=""></option>
					<c:forEach items="${hjhAssetTypes }" var="assetType" begin="0" step="1">
						<option value="${assetType.assetType }"
							<c:if test="${assetType.assetType eq operationlogForm.assetTypeSrch and not empty operationlogForm.assetTypeSrch}">selected="selected"</c:if>>
							<c:out value="${assetType.assetTypeName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>期限</label> 
				<input type="text" name="borrowPeriodSrch" class="form-control input-sm underline"  maxlength="20" value="${operationlogForm.borrowPeriodSrch}" />
			</div>
			<div class="form-group">
				<label>修改类型</label>
				<select name="modifyTypeSrch" class="form-control input-sm form-select2">
					<option value=""></option>
					<c:forEach items="${updateTypes }" var="cardpro" begin="0" step="1">
						<option value="${cardpro.typeId }"
							<c:if test="${cardpro.typeId eq operationlogForm.modifyTypeSrch}">selected="selected"</c:if>>
							<c:out value="${cardpro.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>操作人</label> 
				<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${operationlogForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>操作时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="recieveTimeStartSrch" id="start-date-time" class="form-control underline" value="${operationlogForm.recieveTimeStartSrch}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="recieveTimeEndSrch" id="end-date-time" class="form-control underline" value="${operationlogForm.recieveTimeEndSrch}" />
					</span>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogpanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/operationlog/operationlog.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
