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


<shiro:hasPermission name="label:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="标签设置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">标签设置</h1>
			<span class="mainDescription">本功能可以增加修改删除标签。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${labelForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${labelForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
 								<shiro:hasPermission name="label:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加新标签">添加 <i class="fa fa-plus"></i></a>
 								</shiro:hasPermission> 
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 标签列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">标签编号</th>
										<th class="center">标签名称</th>
										<th class="center">智投编号</th>
										<th class="center">标的实际利率</th>
										<th class="center">资产来源</th>
										<th class="center">产品类型</th>
										<th class="center">项目类型</th>
										<th class="center">还款方式</th>
										<th class="center">标签状态</th>
										<th class="center">使用状态</th>
										<th class="center">创建人</th>
										<th class="center">创建时间</th>
										<th class="center">修改人</th>
										<th class="center">修改时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty labelForm.recordList}">
											<tr>
												<td colspan="16">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${labelForm.recordList }"
												var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((labelForm.paginatorPage - 1) * labelForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.id }"></c:out></td>
													<td><c:out value="${record.labelName }"></c:out></td>
													<td><c:out value="${record.planNid }"></c:out></td>

													<td>
														<c:choose>
															<c:when test="${record.labelAprStart ne record.labelAprEnd}">
																<c:if test="${not empty record.labelAprStart and not empty record.labelAprEnd}">
																	<c:out value="${record.labelAprStart }%~${record.labelAprEnd }%"></c:out>
																</c:if>
																<c:if test="${empty record.labelAprStart and not empty record.labelAprEnd}">
																	<c:out value="~${record.labelAprEnd }%"></c:out>
																</c:if>
																<c:if test="${not empty record.labelAprStart and empty record.labelAprEnd}">
																	<c:out value="${record.labelAprStart }%~"></c:out>
																</c:if>
															</c:when>
															<c:otherwise>
																<c:if test="${not empty record.labelAprStart and not empty record.labelAprEnd}">
																	<c:out value="${record.labelAprStart }%"></c:out>
																</c:if>
																<c:if test="${empty record.labelAprStart and not empty record.labelAprEnd}">
																	<c:out value="${record.labelAprEnd }%"></c:out>
																</c:if>
																<c:if test="${not empty record.labelAprStart and empty record.labelAprEnd}">
																	<c:out value="${record.labelAprStart }%"></c:out>
																</c:if>
															</c:otherwise>
														</c:choose>
													</td>

													<td><c:out value="${record.instName }"></c:out></td>
													<td><c:out value="${record.assetTypeName }"></c:out></td>
													<td><c:out value="${record.projectTypeName }"></c:out></td>
													<td><c:out value="${record.borrowStyleName }"></c:out></td>
													<td class="center"><c:out
															value="${record.labelState== '1' ? '启用' : '停用' }"></c:out></td>
													<td class="center"><c:out
															value="${record.engineId !=null ? '已使用' : '未使用' }"></c:out></td>
													<td class="center"><c:out value="${record.createUser}"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td class="center"><c:out value="${record.updateUser}"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.updateTime }"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="label:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="label:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-UpdateBy"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="${record.labelState== '0' ? '启用' : '停用' }"><i
																	class="fa fa-lightbulb-o fa-white"></i></a>
															</shiro:hasPermission>
														</div>

														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light"
																	role="menu">
																	<shiro:hasPermission name="label:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${labelForm.paginator}"></hyjf:paginator>
							<br/> <br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${labelForm.paginatorPage}" />
			<div class="form-group">
				<label>标签名称</label>
				<input type="text" name="labelNameSrch" class="form-control input-sm underline" value="${labelForm.labelNameSrch}" />
			</div>

			<div class="form-group">
				<label>智投编号</label>
				<input type="text" name="planNidSrch" class="form-control input-sm underline" value="${labelForm.planNidSrch}"/>
			</div>

			<div class="form-group">
					<label>资产来源</label>
					<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
							<option value="${inst.instCode }"
								<c:if test="${inst.instCode eq labelForm.instCodeSrch}">selected="selected"</c:if>>
								<c:out value="${inst.instName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>产品类型</label>
					<select name="assetTypeSrch" id="assetTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
							<option value="${assetType.assetType }"
								<c:if test="${assetType.assetType eq labelForm.assetTypeSrch}">selected="selected"</c:if>>
								<c:out value="${assetType.assetTypeName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>

            <div class="form-group">
                <label>项目类型</label>
                <select name="projectTypeSrch" id="projectTypeSrch" class="form-control underline form-select2">
                    <option value=""></option>
                    <c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
                        <option value="${borrowProjectType.borrowCd }" data-cd="${borrowProjectType.borrowCd }"
                                <c:if test="${borrowProjectType.borrowCd eq labelForm.projectTypeSrch}">selected="selected"</c:if>>
                            <c:out value="${borrowProjectType.borrowName }"></c:out></option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>还款方式</label>
                </label>
                <select name="borrowStyleSrch" id="borrowStyleSrch" class="form-control underline form-select2">
                    <option value=""></option>
                    <c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
                        <option value="${borrowStyle.nid }" data-cd="${borrowStyle.name }"
                                <c:if test="${borrowStyle.nid eq labelForm.borrowStyleSrch}">selected="selected"</c:if>>
                            <c:out value="${borrowStyle.name}"></c:out>
                        </option>
                    </c:forEach>
                </select>
            </div>

			<div class="form-group">
				<label>标签状态</label> <select name="labelStateSrch"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${labelForm.labelStateSrch==0}">selected="selected"</c:if>>停用</option>
					<option value="1"
						<c:if test="${labelForm.labelStateSrch==1}">selected="selected"</c:if>>启用</option>
				</select>
			</div>

            <div class="form-group">
                <label>使用状态</label> <select name="engineIdSrch"
                                            class="form-control underline form-select2">
                <option value="">全部</option>
                <option value="0"
                        <c:if test="${labelForm.engineIdSrch==0}">selected="selected"</c:if>>未使用</option>
                <option value="1"
                        <c:if test="${labelForm.engineIdSrch==1}">selected="selected"</c:if>>已使用</option>
            </select>
            </div>
			
			<div class="form-group">
				<label>创建时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="createTimeStartSrch" id="start-date-time" class="form-control underline" value="${labelForm.createTimeStartSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="createTimeEndSrch" id="end-date-time" class="form-control underline" value="${labelForm.createTimeEndSrch}" />
				</div>
			</div>

            <div class="form-group">
                <label>修改时间</label>
            <div class="input-group input-daterange datepicker">
                <span class="input-icon"> <input type="text"
						name="updateTimeStartSrch" id="start-date-time1" class="form-control underline" value="${labelForm.updateTimeStartSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
                <input type="text" name="updateTimeEndSrch" id="end-date-time1" class="form-control underline" value="${labelForm.updateTimeEndSrch}" />
            </div>
            </div>


		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/label/hjhlabel.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
