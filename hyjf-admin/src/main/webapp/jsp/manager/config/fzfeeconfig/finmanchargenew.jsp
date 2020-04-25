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

<shiro:hasPermission name="finmanchargenew:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇直投融资管理费-新" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇直投融资管理费-新</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<shiro:hasPermission name="projecttype:VIEW">
					<li><a href="${webRoot}/manager/config/projecttype/init">项目类型</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="finsercharge:VIEW">
					<li><a href="${webRoot}/manager/config/finsercharge/init">放款服务费</a></li>
				</shiro:hasPermission>
		      	<shiro:hasPermission name="finmancharge:VIEW">
		      		<li><a href="${webRoot}/manager/config/finmancharge/init">汇直投融资管理费</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/finmanchargenew/init">汇直投融资管理费-新</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="borrowflow:VIEW">
		      		<li><a href="${webRoot}/manager/config/borrowflow/init">项目流程配置-新</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="finhxfmancharge:VIEW">
		      		<li><a href="${webRoot}/manager/config/finhxfmancharge/init">汇消费融资管理费</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="sendtype:VIEW">
					<li><a href="${webRoot}/manager/config/sendtype/init">发标/复审</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${finmanchargeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${finmanchargeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="finmanchargenew:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
								
								<th class="center">项目类型</th>
								<th class="center">资产来源</th>
								<th class="center">产品类型</th>
								
								<th class="center">类型</th>
								<th class="center">期限</th>
								
								<th class="center">自动发标利率</th>
								<th class="center">服务费率</th>
								
								<th class="center">管理费率</th>
								<th class="center">收益差率</th>
								<th class="center">状态</th>
								<th class="center">备注</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty finmanchargeForm.recordList}">
									<tr>
										<td colspan="13">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${finmanchargeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((finmanchargeForm.paginatorPage - 1) * finmanchargeForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.projectName }"></c:out></td>
											<td class="center"><c:out value="${record.instName }"></c:out></td>
											<td class="center"><c:out value="${record.assetTypeName }"></c:out></td>
											<td class="center"><c:out value="${record.manChargeType }"></c:out></td>
											<td class="center"><c:out value="${record.manChargeTime }"></c:out>
												<c:if test="${record.manChargeType == '天标' }">
													天
												</c:if>
												<c:if test="${record.manChargeType == '月标' }">
													个月
												</c:if>
											</td>
											<td class="center"><c:out value="${record.autoBorrowApr }"></c:out></td>
											<td class="center"><c:out value="${record.chargeRate }"></c:out></td>
											<td class="center"><c:out value="${record.manChargeRate }"></c:out></td>
											<td class="center"><c:out value="${record.returnRate }"></c:out></td>
											<td class="center"><c:out value="${record.status == '0' ? '启用' : '禁用'  }"></c:out></td>
											<td class="center"><c:out value="${record.remark }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="finmanchargenew:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-manchargecd="${record.manChargeCd }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="finmanchargenew:DELETE">
														<a class="btn btn-transparent btn-xs fn-Delete" data-manchargecd="${record.manChargeCd }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
													</shiro:hasPermission>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<shiro:hasPermission name="finmanchargenew:MODIFY">
																<li><a class="fn-Modify" data-manchargecd="${record.manChargeCd }">修改</a></li>
															</shiro:hasPermission>
															<shiro:hasPermission name="finmanchargenew:DELETE">
																<li><a class="fn-Delete" data-manchargecd="${record.manChargeCd }">删除</a></li>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${finmanchargeForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="finmanchargenew:SEARCH">
				<input type="hidden" name="manChargeCd" id="manChargeCd" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${finmanchargeForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>类型</label>
					<select name="manChargeTypeSear" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${enddayMonthList }" var="record" begin="0" step="1" varStatus="status">
							<option value="${record.nameCd }"
								<c:if test="${record.nameCd eq finmanchargeForm.manChargeTypeSear}">selected="selected"</c:if>>
								<c:out value="${record.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>资产来源</label>
					<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
							<option value="${inst.instCode }"
								<c:if test="${inst.instCode eq finmanchargeForm.instCodeSrch}">selected="selected"</c:if>>
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
								<c:if test="${assetType.assetType eq finmanchargeForm.assetTypeSrch}">selected="selected"</c:if>>
								<c:out value="${assetType.assetTypeName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>期限</label>
					<input type="text" name="manChargeTimeSear" id="manChargeTimeSear" class="form-control input-sm underline" value="${finmanchargeForm.manChargeTimeSear}" />
				</div>
				<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSear" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowClass }"
								<c:if test="${borrowProjectType.borrowClass eq finmanchargeForm.projectTypeSear}">selected="selected"</c:if>>
								<c:out value="${borrowProjectType.borrowName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="statusSear" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0" <c:if test="${'0' eq finmanchargeForm.statusSear}">selected="selected"</c:if>>
							<c:out value="启用"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq finmanchargeForm.statusSear}">selected="selected"</c:if>>
							<c:out value="禁用"></c:out>
						</option>
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/finmanchargenew/finmanchargenew.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
