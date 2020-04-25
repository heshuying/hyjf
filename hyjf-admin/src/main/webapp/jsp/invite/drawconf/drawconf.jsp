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


<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">兑换配置</h1>
			<span class="mainDescription">兑换配置</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
			      		<li><a href="${webRoot}/inviteUser/init">账户信息</a></li>
			      	</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/getRecommend/init">推荐星获取明细</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/usedRecommend/init">推荐星使用明细</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/exchangeconf/conf/init">兑换配置</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li class="active"><a href="javascript:void(0);">抽奖配置</a></li>
					</shiro:hasPermission>
			    </ul>
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${confForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${confForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="activitylist:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加奖品">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission> 
								<shiro:hasPermission name="activitylist:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 公司环境列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
								<col style="width:55px;" />
								<col />
								<col />
								<col style="width:93px;" />
							</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">奖品名称</th>
										<th class="center">奖品类型</th>
										<th class="center">奖品图片</th>
										<th class="center">排序</th>
										<th class="center">中奖几率</th>
										<th class="center">数量上限</th>
										<th class="center">已抽奖数量</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record"
												begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((confForm.paginatorPage - 1) * confForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.prizeName }"></c:out></td>
													<td class="center"><c:out value="${record.prizeType eq 1 ? '实物奖品' : '优惠券'}"></c:out></td>
													<td class="center">
														<a href="${record.prizePicUrl}" target="_blank" class="thumbnails-wrap"
																data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true"
																data-title="图片预览" data-content="<img src='${record.prizePicUrl}' style='max-height:350px;'/>">
															<img src="${record.prizePicUrl}" />
														</a>
													</td>
													<td class="center"><c:out value="${record.prizeSort }"></c:out></td>
													<td class="center"><c:out value="${record.prizeProbability == null? '0' : record.prizeProbability}"></c:out>%</td>
													<td class="center"><c:out value="${record.prizeQuantity }"></c:out></td>
													<td class="center"><c:out value="${record.prizeQuantity - record.prizeReminderQuantity }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="activitylist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.prizeGroupCode }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="activitylist:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-UpdateBy"
																	data-id="${record.prizeGroupCode }" data-status="${record.prizeStatus}" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="${record.prizeStatus== '0' ? '禁用' : '启用' }"><i
																	class="fa fa-lightbulb-o fa-white"></i></a>
															</shiro:hasPermission>
														</div>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${confForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${confForm.paginatorPage}" />
			<div class="form-group">
				<label>奖品类型</label> 
				<select name="prizeTypeSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${prizeType }" var="prizeType" begin="0" step="1" >
							<option value="${prizeType.nameCd }"
								<c:if test="${prizeType.nameCd eq confForm.prizeTypeSrch}">selected="selected"</c:if>>
								<c:out value="${prizeType.name }"></c:out>
							</option>
						</c:forEach>
					</select>
			</div>
			<div class="form-group">
				<label>状态</label> <select name="prizeStatusSrch"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${confForm.prizeStatusSrch=='0'}">selected="selected"</c:if>>启用</option>
					<option value="1"
						<c:if test="${confForm.prizeStatusSrch=='1'}">selected="selected"</c:if>>禁用</option>
				</select>
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
			<style>
			.thumbnails-wrap {
				border: 1px solid #ccc;
				padding: 3px;
				display: inline-block;
			}
			.thumbnails-wrap img {
				
				min-width: 35px;
				height: 22px;
			}
			.popover {
				max-width: 500px;
			}
			.popover img {
				max-width: 460px;
			}
			</style>
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/invite/drawconf/drawconf.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
